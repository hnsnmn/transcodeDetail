package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.Transcoder;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 27.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
public class TranscoderServiceTest {

	private Transcoder transcoder;

	@Test
	public void transcodeWithOnOutputFormat() {
		File multimediaFile = new File(".");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(640, 480, 300, "h264", "aac"));
		List<File> transcodeFiles = transcoder.transcode(multimediaFile, outputFormats);

		assertEquals(1, transcodeFiles.size());
	}
}
