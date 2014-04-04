package org.hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.AMediaCoderMixin;
import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.event.*;
import com.xuggle.xuggler.*;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import org.hnsnmn.domain.job.OutputFormat;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_AUDIO;
import static com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

public class VideoConverter extends AMediaCoderMixin implements IMediaListener {
	/** The default pixel type. */
	private static final IPixelFormat.Type DEFAULT_PIXEL_TYPE = IPixelFormat.Type.YUV420P;

	/** The default sample format. */
	private static final IAudioSamples.Format DEFAULT_SAMPLE_FORMAT = IAudioSamples.Format.FMT_S16;

	/** The default time base. */
	private static final IRational DEFAULT_TIMEBASE = IRational.make(1,
			(int) Global.DEFAULT_PTS_PER_SECOND);

	// a map between input stream indicies to output stream indicies
	private Map<Integer, Integer> mOutputStreamIndices = new HashMap<Integer, Integer>();

	// a map between output stream indicies and streams
	private Map<Integer, IStream> mStreams = new HashMap<Integer, IStream>();

	// a map between output stream indicies and video converters
	private Map<Integer, IConverter> mVideoConverters = new HashMap<Integer, IConverter>();

	// streasm opened by this MediaWriter must be closed
	private final Collection<IStream> mOpenedStreams = new Vector<IStream>();

	// true if the writer should ask FFMPEG to interleave media
	private boolean mForceInterleave = true;

	// mask late stream exception policy
	private boolean mMaskLateStreamException = false;

	// the input container of packets
	private final IContainer mInputContainer;
	// the container format
	private IContainerFormat mContainerFormat;

	private int videoBitRateInKilobytes = 0;

	public VideoConverter(String url, IMediaReader reader,
						  OutputFormat outputFormat) {
		this(url, reader.getContainer(), outputFormat);
		if (reader.canAddDynamicStreams())
			throw new IllegalArgumentException(
					"inputContainer is improperly configured to allow "
							+ "dynamic adding of streams.");
	}

	public VideoConverter(String outputUrl, IContainer inputContainer,
						  OutputFormat outputFormat) {
		super(outputUrl, IContainer.make());

		this.videoBitRateInKilobytes = outputFormat.getBitrate();

		// verify that the input container is a readable type
		if (inputContainer.getType() != IContainer.Type.READ)
			throw new IllegalArgumentException(
					"inputContainer is improperly must be of type readable.");

		// verify that no streams will be added dynamically
		if (inputContainer.canStreamsBeAddedDynamically())
			throw new IllegalArgumentException(
					"inputContainer is improperly configured to allow "
							+ "dynamic adding of streams.");

		mInputContainer = inputContainer;
		// create format
		mContainerFormat = IContainerFormat.make();
		mContainerFormat.setOutputFormat(mInputContainer.getContainerFormat()
				.getInputFormatShortName(), getUrl(), null);

	}

	@Override
	public void onAddStream(IAddStreamEvent event) {
	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {
		encodeAudio(event.getStreamIndex(), event.getAudioSamples());
	}

	public void encodeAudio(int streamIndex, IAudioSamples samples) {
		if (null == samples)
			throw new IllegalArgumentException("NULL input samples");

		IStream stream = getStream(streamIndex);
		if (null == stream)
			return;

		IStreamCoder coder = stream.getStreamCoder();
		try {
			if (CODEC_TYPE_AUDIO != coder.getCodecType()) {
				throw new IllegalArgumentException("stream[" + streamIndex
						+ "] is not audio");
			}

			for (int consumed = 0; consumed < samples.getNumSamples();) {
				IPacket packet = IPacket.make();
				try {
					int result = coder.encodeAudio(packet, samples, consumed);
					if (result < 0)
						throw new RuntimeException("failed to encode audio");

					// update total consumed
					consumed += result;

					// if a complete packed was produced write it out
					if (packet.isComplete())
						writePacket(packet);
				} finally {
					if (packet != null)
						packet.delete();
				}
			}
		} finally {
			if (coder != null)
				coder.delete();
		}
	}

	private void writePacket(IPacket packet) {
		if (getContainer().writePacket(packet, mForceInterleave) < 0)
			throw new RuntimeException("failed to write packet: " + packet);
	}

	private IStream getStream(int inputStreamIndex) {
		// the output container must be open

		if (!isOpen())
			open();

		// if the output stream index does not exists, create it

		if (null == getOutputStreamIndex(inputStreamIndex)) {
			// If the header has already been written, then it is too late to
			// establish a new stream, throw, or mask optionally mask, and
			// exception regarding the tardy arrival of the new stream

			if (getContainer().isHeaderWritten())
				if (willMaskLateStreamExceptions())
					return null;
				else
					throw new RuntimeException(
							"Input stream index "
									+ inputStreamIndex
									+ " has not been seen before, but the media header has already been "
									+ "written.  To mask these exceptions call setMaskLateStreamExceptions()");

			// if an no input container exists, create new a stream from scratch

			if (null == mInputContainer) {
				//
				// NOTE: this is where the new stream code will go
				//
				throw new UnsupportedOperationException(
						"MediaWriter can not yet create streams without an input container.");
			}

			// otherwise use the input container as a guide to adding streams
			else {
				// the input container must be open
				if (!mInputContainer.isOpened())
					throw new RuntimeException(
							"Can't get stream information from a closed input IContainer.");

				// have a look through the input container streams
				for (int i = 0; i < mInputContainer.getNumStreams(); ++i) {
					// if input stream index does not map to an output stream
					// index, this is a new stream, add it
					if (null == mOutputStreamIndices.get(i))
						addStreamFromContainer(i);
				}
			}
		}

		// if the header has not been written, do so now
		if (!getContainer().isHeaderWritten()) {
			// if any of the existing coders are not open, open them now
			for (IStream stream : mStreams.values())
				if (!stream.getStreamCoder().isOpen())
					openStream(stream);

			// write the header
			int rv = getContainer().writeHeader();
			if (0 != rv)
				throw new RuntimeException("Error " + IError.make(rv)
						+ ", failed to write header to container "
						+ getContainer() + " while establishing stream "
						+ mStreams.get(getOutputStreamIndex(inputStreamIndex)));
		}

		// establish the coder for the output stream index

		IStream stream = mStreams.get(getOutputStreamIndex(inputStreamIndex));
		if (null == stream)
			throw new RuntimeException(
					"invalid input stream index (no stream): "
							+ inputStreamIndex);
		IStreamCoder coder = stream.getStreamCoder();
		if (null == coder)
			throw new RuntimeException(
					"invalid input stream index (no coder): "
							+ inputStreamIndex);

		// return the coder

		return stream;
	}

	public Integer getOutputStreamIndex(int inputStreamIndex) {
		return mOutputStreamIndices.get(inputStreamIndex);
	}

	public boolean willMaskLateStreamExceptions() {
		return mMaskLateStreamException;
	}

	private boolean addStreamFromContainer(int inputStreamIndex) {
		// get the input stream

		IStream inputStream = mInputContainer.getStream(inputStreamIndex);
		IStreamCoder inputCoder = inputStream.getStreamCoder();
		ICodec.Type inputType = inputCoder.getCodecType();
		ICodec.ID inputID = inputCoder.getCodecID();

		// if this stream is not a supported type, indicate failure
		if (!isSupportedCodecType(inputType))
			return false;

		IContainerFormat format = getContainer().getContainerFormat();

		switch (inputType) {
			case CODEC_TYPE_AUDIO:
				addAudioStream(inputStream.getIndex(), inputStream.getId(),
						format.establishOutputCodecId(inputID),
						inputCoder.getChannels(), inputCoder.getSampleRate());
				break;
			case CODEC_TYPE_VIDEO:
				addVideoStream(inputStream.getIndex(), inputStream.getId(),
						format.establishOutputCodecId(inputID),
						inputCoder.getFrameRate(), inputCoder.getWidth(),
						inputCoder.getHeight());
				break;
			default:
				break;
		}
		return true;
	}

	public boolean isSupportedCodecType(ICodec.Type type) {
		return (CODEC_TYPE_VIDEO == type || CODEC_TYPE_AUDIO == type);
	}

	public int addAudioStream(int inputIndex, int streamId, ICodec.ID codecId,
							  int channelCount, int sampleRate) {
		if (codecId == null)
			throw new IllegalArgumentException("null codecId");
		ICodec codec = ICodec.findEncodingCodec(codecId);
		if (codec == null)
			throw new UnsupportedOperationException(
					"cannot encode with codec: " + codecId);
		try {
			return addAudioStream(inputIndex, streamId, codec, channelCount,
					sampleRate);
		} finally {
			codec.delete();
		}
	}

	public int addAudioStream(int inputIndex, int streamId, ICodec codec,
							  int channelCount, int sampleRate) {
		// validate parameteres
		if (channelCount <= 0)
			throw new IllegalArgumentException("invalid channel count "
					+ channelCount);
		if (sampleRate <= 0)
			throw new IllegalArgumentException("invalid sample rate "
					+ sampleRate);

		// add the new stream at the correct index
		IStream stream = establishStream(inputIndex, streamId, codec);

		// configre the stream coder
		IStreamCoder coder = stream.getStreamCoder();
		coder.setChannels(channelCount);
		coder.setSampleRate(sampleRate);
		coder.setSampleFormat(DEFAULT_SAMPLE_FORMAT);

		// add the stream to the media writer
		addStream(stream, inputIndex, stream.getIndex());

		// return the new audio stream
		return stream.getIndex();
	}

	private IStream establishStream(int inputIndex, int streamId, ICodec codec) {
		// validate parameteres and conditions

		if (inputIndex < 0)
			throw new IllegalArgumentException("invalid input index "
					+ inputIndex);
		if (streamId < 0)
			throw new IllegalArgumentException("invalid stream id " + streamId);
		if (null == codec)
			throw new IllegalArgumentException("null codec");

		// if the container is not opened, do so

		if (!isOpen())
			open();

		// add the new stream at the correct index

		IStream stream = getContainer().addNewStream(codec);
		if (stream == null)
			throw new RuntimeException("Unable to create stream id " + streamId
					+ ", index " + inputIndex + ", codec " + codec);

		// if the stream count is 1, don't force interleave

		setForceInterleave(getContainer().getNumStreams() != 1);

		// return the new video stream

		return stream;
	}

	private void addStream(IStream stream, int inputStreamIndex,
						   int outputStreamIndex) {
		// map input to output stream indicies
		mOutputStreamIndices.put(inputStreamIndex, outputStreamIndex);

		// get the coder and add it to the index to coder map
		mStreams.put(outputStreamIndex, stream);

		// if this is a video coder, set the quality
		IStreamCoder coder = stream.getStreamCoder();
		if (CODEC_TYPE_VIDEO == coder.getCodecType())
			coder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
	}

	public int addVideoStream(int inputIndex, int streamId, ICodec.ID codecId,
							  IRational frameRate, int width, int height) {
		if (codecId == null)
			throw new IllegalArgumentException("null codecId");
		ICodec codec = ICodec.findEncodingCodec(codecId);
		if (codec == null)
			throw new UnsupportedOperationException(
					"cannot encode with codec: " + codecId);
		try {
			return addVideoStream(inputIndex, streamId, codec, frameRate,
					width, height);
		} finally {
			codec.delete();
		}
	}

	public int addVideoStream(int inputIndex, int streamId, ICodec codec,
							  IRational frameRate, int width, int height) {
		// validate parameteres
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("invalid video frame size ["
					+ width + " x " + height + "]");

		// add the new stream at the correct index
		IStream stream = establishStream(inputIndex, streamId, codec);

		// configre the stream coder
		IStreamCoder coder = stream.getStreamCoder();
		try {
			List<IRational> supportedFrameRates = codec
					.getSupportedVideoFrameRates();
			IRational timeBase = null;
			if (supportedFrameRates != null && supportedFrameRates.size() > 0) {
				IRational highestResolution = null;
				// If we have a list of supported frame rates, then
				// we must pick at least one of them. and if the
				// user passed in a frameRate, it must match
				// this list.
				for (IRational supportedRate : supportedFrameRates) {
					if (!IRational.positive(supportedRate))
						continue;
					if (highestResolution == null)
						highestResolution = supportedRate.copyReference();

					if (IRational.positive(frameRate)) {
						if (supportedRate.compareTo(frameRate) == 0)
							// use this
							highestResolution = frameRate.copyReference();
					} else if (highestResolution.getDouble() < supportedRate
							.getDouble()) {
						highestResolution.delete();
						highestResolution = supportedRate.copyReference();
					}
					supportedRate.delete();
				}
				// if we had a frame rate suggested, but we
				// didn't find a match among the supported elements,
				// throw an error.
				if (IRational.positive(frameRate)
						&& (highestResolution == null || highestResolution
						.compareTo(frameRate) != 0))
					throw new UnsupportedOperationException(
							"container does not"
									+ " support encoding at given frame rate: "
									+ frameRate);

				// if we got through the supported list and found NO valid
				// resolution, fail.
				if (highestResolution == null)
					throw new UnsupportedOperationException(
							"could not find supported frame rate for container: "
									+ getUrl());
				if (timeBase == null)
					timeBase = IRational.make(
							highestResolution.getDenominator(),
							highestResolution.getNumerator());
				highestResolution.delete();
				highestResolution = null;
			}
			// if a positive frame rate was passed in, we
			// should either use the inverse of it, or if
			// there is a supported frame rate, but not
			// this, then throw an error.
			if (IRational.positive(frameRate) && timeBase == null) {
				timeBase = IRational.make(frameRate.getDenominator(),
						frameRate.getNumerator());
			}

			if (timeBase == null) {
				timeBase = getDefaultTimebase();

				// Finally MPEG4 has some code failing if the time base
				// is too aggressive...
				if (codec.getID() == ICodec.ID.CODEC_ID_MPEG4
						&& timeBase.getDenominator() > ((1 << 16) - 1)) {
					// this codec can't support that high of a frame rate
					timeBase.delete();
					timeBase = IRational.make(1, (1 << 16) - 1);
				}
			}
			coder.setTimeBase(timeBase);
			timeBase.delete();
			timeBase = null;

			coder.setWidth(width);
			coder.setHeight(height);
			coder.setPixelType(DEFAULT_PIXEL_TYPE);

			applyBitrate(coder);

			// add the stream to the media writer
			addStream(stream, inputIndex, stream.getIndex());
		} finally {
			coder.delete();
		}

		// return the new video stream
		return stream.getIndex();
	}

	public IRational getDefaultTimebase() {
		return DEFAULT_TIMEBASE.copyReference();
	}

	private void applyBitrate(IStreamCoder coder) {
		if (videoBitRateInKilobytes > 0) {
			coder.setBitRate(videoBitRateInKilobytes * 1024);
		}
	}

	private void openStream(IStream stream) {
		// if the coder is not open, open it NOTE: MediaWriter currently
		// supports audio & video streams

		IStreamCoder coder = stream.getStreamCoder();
		try {
			ICodec.Type type = coder.getCodecType();
			if (!coder.isOpen() && isSupportedCodecType(type)) {
				// open the coder

				int rv = coder.open(null, null);
				if (rv < 0)
					throw new RuntimeException("could not open stream "
							+ stream + ": " + getErrorMessage(rv));
				mOpenedStreams.add(stream);
			}
		} finally {
			coder.delete();
		}
	}

	public void open() {
		// open the container
		if (getContainer().open(getUrl(), IContainer.Type.WRITE,
				mContainerFormat, true, false) < 0)
			throw new IllegalArgumentException("could not open: " + getUrl());

		// note that we should close the container opened here

		setShouldCloseContainer(true);
	}

	@Override
	public void onClose(ICloseEvent event) {
		if (isOpen())
			close();
	}

	public void close() {
		int rv;

		// flush coders
		flush();

		// write the trailer on the output conteiner
		if ((rv = getContainer().writeTrailer()) < 0)
			throw new RuntimeException("error " + IError.make(rv)
					+ ", failed to write trailer to " + getUrl());

		// close the coders opened by this MediaWriter
		for (IStream stream : mOpenedStreams) {
			IStreamCoder coder = stream.getStreamCoder();
			try {
				if ((rv = coder.close()) < 0)
					throw new RuntimeException("error " + getErrorMessage(rv)
							+ ", failed close coder " + coder);
			} finally {
				coder.delete();
			}
		}

		// expunge all referneces to the coders and resamplers

		mStreams.clear();
		mOpenedStreams.clear();
		mVideoConverters.clear();

		// if we're supposed to, close the container

		if (getShouldCloseContainer()) {
			if ((rv = getContainer().close()) < 0)
				throw new RuntimeException("error " + IError.make(rv)
						+ ", failed close IContainer " + getContainer()
						+ " for " + getUrl());
			setShouldCloseContainer(false);
		}
	}

	public void flush() {
		// flush coders
		for (IStream stream : mStreams.values()) {
			IStreamCoder coder = stream.getStreamCoder();
			if (!coder.isOpen())
				continue;

			// if it's audio coder flush that
			if (CODEC_TYPE_AUDIO == coder.getCodecType()) {
				IPacket packet = IPacket.make();
				while (coder.encodeAudio(packet, null, 0) >= 0
						&& packet.isComplete()) {
					writePacket(packet);
					packet.delete();
					packet = IPacket.make();
				}
				packet.delete();
			}

			// else flush video coder
			else if (CODEC_TYPE_VIDEO == coder.getCodecType()) {
				IPacket packet = IPacket.make();
				while (coder.encodeVideo(packet, null, 0) >= 0
						&& packet.isComplete()) {
					writePacket(packet);
					packet.delete();
					packet = IPacket.make();
				}
				packet.delete();
			}
		}
		// flush the container
		getContainer().flushPackets();
	}

	@Override
	public void onCloseCoder(ICloseCoderEvent event) {
	}

	@Override
	public void onFlush(IFlushEvent event) {
	}

	@Override
	public void onOpen(IOpenEvent event) {
	}

	@Override
	public void onOpenCoder(IOpenCoderEvent event) {
	}

	@Override
	public void onReadPacket(IReadPacketEvent event) {
	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		if (event.getImage() != null)
			encodeVideo(event.getStreamIndex(), event.getImage(),
					event.getTimeStamp(event.getTimeUnit()),
					event.getTimeUnit());
		else
			encodeVideo(event.getStreamIndex(), event.getPicture());
	}

	private void encodeVideo(int streamIndex, BufferedImage image,
							 long timeStamp, TimeUnit timeUnit) {
		// verify parameters

		if (null == image)
			throw new IllegalArgumentException("NULL input image");
		if (null == timeUnit)
			throw new IllegalArgumentException("NULL time unit");

		// try to set up the stream, and if we're not going to encode
		// it, don't bother converting it.
		IStream stream = getStream(streamIndex);
		if (null == stream)
			return;

		// convert the image to a picture and push it off to be encoded

		IVideoPicture picture = convertToPicture(streamIndex, image,
				MICROSECONDS.convert(timeStamp, timeUnit));

		try {
			encodeVideo(streamIndex, picture, image);
		} finally {
			if (picture != null)
				picture.delete();
		}
	}

	private IVideoPicture convertToPicture(int streamIndex,
										   BufferedImage image, long timeStamp) {
		// lookup the converter

		IConverter videoConverter = mVideoConverters.get(streamIndex);

		// if not found create one

		if (videoConverter == null) {
			IStream stream = mStreams.get(streamIndex);
			IStreamCoder coder = stream.getStreamCoder();
			videoConverter = ConverterFactory.createConverter(
					ConverterFactory.findDescriptor(image),
					coder.getPixelType(), coder.getWidth(), coder.getHeight(),
					image.getWidth(), image.getHeight());
			mVideoConverters.put(streamIndex, videoConverter);
		}

		// return the converter
		return videoConverter.toPicture(image, timeStamp);
	}

	private void encodeVideo(int streamIndex, IVideoPicture picture,
							 BufferedImage image) {
		// establish the stream, return silently if no stream returned
		if (null == picture)
			throw new IllegalArgumentException("no picture");

		IStream stream = getStream(streamIndex);
		if (null == stream)
			return;

		// verify parameters
		Integer outputIndex = getOutputStreamIndex(streamIndex);
		if (null == outputIndex)
			throw new IllegalArgumentException("unknow stream index: "
					+ streamIndex);
		if (CODEC_TYPE_VIDEO != mStreams.get(outputIndex).getStreamCoder()
				.getCodecType()) {
			throw new IllegalArgumentException("stream[" + streamIndex
					+ "] is not video");
		}
		// encode video picture

		// encode the video packet
		IPacket packet = IPacket.make();
		try {
			if (stream.getStreamCoder().encodeVideo(packet, picture, 0) < 0)
				throw new RuntimeException("failed to encode video");

			if (packet.isComplete())
				writePacket(packet);
		} finally {
			if (packet != null)
				packet.delete();
		}
	}

	private void encodeVideo(int streamIndex, IVideoPicture picture) {
		encodeVideo(streamIndex, picture, null);
	}

	@Override
	public void onWriteHeader(IWriteHeaderEvent event) {
	}

	@Override
	public void onWritePacket(IWritePacketEvent event) {
	}

	@Override
	public void onWriteTrailer(IWriteTrailerEvent event) {
	}

	public void setForceInterleave(boolean forceInterleave) {
		mForceInterleave = forceInterleave;
	}

	private static String getErrorMessage(int rv) {
		String errorString = "";
		IError error = IError.make(rv);
		if (error != null) {
			errorString = error.toString();
			error.delete();
		}
		return errorString;
	}

}

