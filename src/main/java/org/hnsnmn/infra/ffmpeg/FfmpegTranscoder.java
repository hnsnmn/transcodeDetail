package org.hnsnmn.infra.ffmpeg;

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
	@Override
	public List<File> transcode(File mockMultimediaFile, List<OutputFormat> outputFormats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : outputFormats) {
			results.add(new File("."));
		}
		return results;
	}
}
