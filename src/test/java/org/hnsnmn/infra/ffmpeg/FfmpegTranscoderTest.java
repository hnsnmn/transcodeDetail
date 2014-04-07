package org.hnsnmn.infra.ffmpeg;

import org.hnsnmn.domain.job.*;
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
	private File multimediaFile;
	private List<OutputFormat> outputFormats;

	private OutputFormat mp4Format;
	private OutputFormat mp4Format2;
	private OutputFormat aviFormat;

	@Before
	public void setUp() {
		transcoder = new FfmpegTranscoder();
		multimediaFile = new File("src/test/resources/sample.avi");
		outputFormats = new ArrayList<OutputFormat>();

		mp4Format = new OutputFormat(160, 120, 150, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		mp4Format2 = new OutputFormat(80, 60, 80, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(160, 120, 150, Container.AVI, VideoCodec.MPEG4, AudioCodec.MP3);
	}

	@Test
	public void transcodeWithOneMp4OutputFormat() {
		outputFormats.add(mp4Format);
		outputFormats.add(mp4Format2);
		executeTranscoderAndAssert();
	}

	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		executeTranscoderAndAssert();

	}

	private void executeTranscoderAndAssert() {
		List<File> transcodeFiles = transcoder.transcode(multimediaFile, outputFormats);

		assertEquals(outputFormats.size(), transcodeFiles.size());
		for (int i = 0; i < outputFormats.size(); i++) {
			assertTrue(transcodeFiles.get(i).exists());
			VideoFormatVerifier.verifyVideoFormat(outputFormats.get(i), transcodeFiles.get(i));
		}

	}
}
