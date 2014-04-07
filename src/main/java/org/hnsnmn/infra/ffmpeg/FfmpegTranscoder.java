package org.hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.Transcoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 27.
 * Time: 오후 5:44
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegTranscoder implements Transcoder {
	private NamingRule namingRule;

	public FfmpegTranscoder(NamingRule namingRule) {
		this.namingRule = namingRule;
	}

	@Override
	public List<File> transcode(File multimediaFile, List<OutputFormat> outputFormats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : outputFormats) {
			results.add(transcode(multimediaFile, format)); // 기존 new File(".")로 처리
		}
		return results;
	}

	private File transcode(File sourceFile, OutputFormat format) {
		IMediaReader reader = ToolFactory.makeReader(sourceFile.getAbsolutePath());

		String outputFile = getFileName(format);
		VideoConverter converter = new VideoConverter(outputFile, reader, format);
		reader.addListener(converter);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}
		return new File(outputFile);
	}

	private String getFileName(OutputFormat format) {
		return namingRule.createName(format);
//		return "outputFile." + format.getFileExtenstion();
	}
}
