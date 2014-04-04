package org.hnsnmn.infra.ffmpeg;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.Transcoder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 27.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegTranscoderTest {

	private Transcoder transcoder;

	@Before
	public void setUp() {
		transcoder = new FfmpegTranscoder();
	}

	@Test
	public void transcodeWithOnOutputFormat() {
		File multimediaFile = new File("src/test/resources/sample.avi");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(640, 480, 300, "h264", "aac"));
		List<File> transcodeFiles = transcoder.transcode(multimediaFile, outputFormats);

		assertEquals(1, transcodeFiles.size());
		assertTrue(transcodeFiles.get(0).exists());
		verifyTranscodedFile(outputFormats.get(0), transcodeFiles.get(0));
	}

	private void verifyTranscodedFile(OutputFormat outputFormat, File file) {
		IContainer container = IContainer.make();
		int openResult = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed:" + openResult);
		}
		int numStreams = container.getNumStreams();

		int width = 0;
		int height = 0;
		ICodec.ID videoCodec = null;
		ICodec.ID audioCodec = null;

		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				height = coder.getHeight();
			}
		}
		container.close();

		assertEquals(outputFormat.getWidth(), width);
		assertEquals(outputFormat.getHeight(), height);
		assertEquals(outputFormat.getVideoFormat(), videoCodec.toString());
		assertEquals(outputFormat.getAudioFormat(), audioCodec.toString());
	}
}
