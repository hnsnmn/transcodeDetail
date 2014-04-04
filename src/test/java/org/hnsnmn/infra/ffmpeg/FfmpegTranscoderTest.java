package org.hnsnmn.infra.ffmpeg;

import org.hnsnmn.domain.job.AudioCodec;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.Transcoder;
import org.hnsnmn.domain.job.VideoCodec;
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
		outputFormats.add(new OutputFormat(640, 480, 300, VideoCodec.H264, AudioCodec.AAC));
		List<File> transcodeFiles = transcoder.transcode(multimediaFile, outputFormats);

		assertEquals(1, transcodeFiles.size());
		assertTrue(transcodeFiles.get(0).exists());

		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0), transcodeFiles.get(0));
	}
}
