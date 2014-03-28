package org.hnsnmn.infra.ffmpeg;

import com.xuggle.xuggler.*;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 28.
 * Time: 오전 10:43
 * To change this template use File | Settings | File Templates.
 */
public class XugglerTest {

	@Test
	public void getMetadataOfExistingAVIFile() {
		IContainer container = IContainer.make();
		int openResult = container.open("src/test/resources/sample.avi", IContainer.Type.READ, null);

		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed : " + openResult);
		}
		int numStreams = container.getNumStreams();
		System.out.printf("file \"%s\" : %d stream%s; ", "src/test/resources/sample.avi", numStreams,
				numStreams == 1 ? "" : "s");
		System.out.printf("bit rate: %d; ", container.getBitRate());
		System.out.printf("\n");

		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			System.out.printf("stream %d: ", i);
			System.out.printf("type: %s;", coder.getCodecType());
			System.out.printf("codec: %s; ", coder.getCodecID());
			System.out.printf("duration: %s ", stream.getDuration() == Global.NO_PTS ? "unknown" : "" + stream.getDuration());
			System.out.printf("start time: %s ", container.getStartTime() == Global.NO_PTS ? "unknown" : "" + stream.getStartTime());
			System.out.printf("timebase: %d/%d; ", stream.getTimeBase().getNumerator(),
					stream.getTimeBase().getDenominator());

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				System.out.printf("sample rate: %d; ", coder.getSampleRate());
				System.out.printf("channels: %d; ", coder.getChannels());
				System.out.printf("format: %s; ", coder.getSampleFormat());
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				System.out.printf("width: %d; ", coder.getWidth());
				System.out.printf("height: %d; ", coder.getHeight());
				System.out.printf("format: %s; ", coder.getPixelType());
				System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());
			}
			System.out.printf("\n");
		}
		container.close();
	}
}
