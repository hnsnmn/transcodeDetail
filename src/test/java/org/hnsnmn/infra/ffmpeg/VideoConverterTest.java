package org.hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.hnsnmn.domain.job.AudioCodec;
import org.hnsnmn.domain.job.Container;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.VideoCodec;
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
	private static final String TRANSCODED_FILE = "target/sample.mp4";

	@Test
	public void transocde() {
		IMediaReader reader = ToolFactory.makeReader(SOURCE_FILE);
		OutputFormat outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		VideoConverter writer = new VideoConverter(TRANSCODED_FILE, reader,
				outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}

		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(TRANSCODED_FILE));
	}

	@Test
	public void transcodeWithOnlyContainer() {
		IMediaReader reader = ToolFactory.makeReader(SOURCE_FILE);
		OutputFormat outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.AVI);
		VideoConverter writer = new VideoConverter(TRANSCODED_FILE, reader, outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}

		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(TRANSCODED_FILE));
	}
}
