package org.hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 3:00
 * To change this template use File | Settings | File Templates.
 */
public interface ThumbnailExtractor {
	List<File> extract(File mockMultimediaFile, ThumbnailPolicy thumbnailPolicy);
}
