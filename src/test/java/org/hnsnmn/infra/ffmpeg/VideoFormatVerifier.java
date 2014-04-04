package org.hnsnmn.infra.ffmpeg;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import org.hnsnmn.domain.job.OutputFormat;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 4.
 * Time: 오후 2:36
 * To change this template use File | Settings | File Templates.
 */
public class VideoFormatVerifier {

	private IContainer container;

	private ICodec.ID audioCodec;
	private ICodec.ID videoCodec;
	private int width;
	private int heigth;

	private OutputFormat expectedFormat;
	private File videoFile;

	public VideoFormatVerifier(OutputFormat expectedFormat, File videoFile) {
		this.expectedFormat = expectedFormat;
		this.videoFile = videoFile;
	}

	public static void verifyVideoFormat(OutputFormat expectedFormat, File videoFile) {
		new VideoFormatVerifier(expectedFormat, videoFile).verify();
	}

	private void verify() {
		try {
			makeContainer();
			extractMetaInfoOfVideo();
			assertVideFile();
		} finally {
			closeContainer();
		}
	}

	private void makeContainer() {
		container = IContainer.make();
		int openResult = container.open(videoFile.getAbsolutePath(),
				IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed : " + openResult);
		}
	}

	private void extractMetaInfoOfVideo() {
		int numStreams = container.getNumStreams();
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				heigth = coder.getHeight();
			}
		}
	}

	private void assertVideFile() {
		assertEquals(expectedFormat.getWidth(), width);
		assertEquals(expectedFormat.getHeight(), heigth);
		assertEquals(expectedFormat.getVideoCodec(), CodecValueConverter.toDomainVideoCodec(videoCodec));
		assertEquals(expectedFormat.getAudioCodec(), CodecValueConverter.toDomainAudioCodec(audioCodec));
	}

	private void closeContainer() {
		if (container != null) {
			container.close();
		}
	}
}
