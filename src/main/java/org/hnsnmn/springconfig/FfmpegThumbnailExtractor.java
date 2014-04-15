package org.hnsnmn.springconfig;

import org.hnsnmn.domain.job.ThumbnailExtractor;
import org.hnsnmn.domain.job.ThumbnailPolicy;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오후 4:26
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegThumbnailExtractor implements ThumbnailExtractor {
	@Override
	public List<File> extract(File mockMultimediaFile, ThumbnailPolicy thumbnailPolicy) {
		// 썸네일 크기, 시작시간, 끝시간, 간격
		return Collections.emptyList();
	}
}
