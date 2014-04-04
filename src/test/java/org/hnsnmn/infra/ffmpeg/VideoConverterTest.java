package org.hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.hnsnmn.domain.job.OutputFormat;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 4.
 * Time: 오후 1:47
 * To change this template use File | Settings | File Templates.
 */
public class VideoConverterTest {

	@Test
	public void transocde() {
		IMediaReader reader = ToolFactory
				.makeReader("src/test/resources/sample.avi");
		OutputFormat outputFormat = new OutputFormat(160, 120, 150, "h264", "aac");
		VideoConverter writer = new VideoConverter("target/sample.mp4", reader,
				outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}
	}
}
