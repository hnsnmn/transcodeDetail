package org.hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.hnsnmn.domain.job.AudioCodec;
import org.hnsnmn.domain.job.Container;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.VideoCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 4.
 * Time: 오후 1:47
 * To change this template use File | Settings | File Templates.
 */
public class VideoConverterTest {

	private static final int WIDTH = 160;
	private static final int HEIGHT = 120;
	private static final int BITRATE = 150;
	private static final String SOURCE_FILE = "src/test/resources/sample.avi";
	private IMediaReader reader;
	private OutputFormat outputFormat;
	private String outputFile;

	@Before
	public void setUp() throws Exception {
		reader = ToolFactory.makeReader(SOURCE_FILE);
	}

	@Test
	public void transocde() {
		initOutput(Container.MP4, VideoCodec.H264, AudioCodec.AAC, "target/sample.mp4");
		testVideoConverter();
	}

	@Test
	public void transcodeWithOnlyContainer() {
		initOutput(Container.AVI, "target/sample.avi");
		testVideoConverter();
	}

	private void initOutput(Container container, VideoCodec videoCodec, AudioCodec audioCodec, String outputFileName) {
		if (videoCodec == null && audioCodec == null) {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, container);
		} else {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, container, videoCodec, audioCodec);
		}
		outputFile = outputFileName;
	}

	private void testVideoConverter() {
		VideoConverter writer = new VideoConverter(outputFile, reader, outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}

		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(outputFile));
	}

	private void initOutput(Container container, String outputFileName) {
		initOutput(container, null, null, outputFileName);
	}
}
