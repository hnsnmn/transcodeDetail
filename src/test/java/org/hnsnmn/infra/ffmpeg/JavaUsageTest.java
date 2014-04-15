package org.hnsnmn.infra.ffmpeg;


import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오후 5:42
 * To change this template use File | Settings | File Templates.
 */
public class JavaUsageTest {

	@Test
	public void getInfo() throws InputFormatException, EncoderException {
		Encoder encoder = new Encoder();
		MultimediaInfo info = encoder.getInfo(new File("src/test/resources/sample.mp4"));

		System.out.println(info.getFormat());
		System.out.println(info.getAudio().getDecoder());
		System.out.println(info.getVideo().getDecoder());
	}
}
