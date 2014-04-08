package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.LocalStorageMediaSourceFile;
import org.hnsnmn.domain.job.MediaSourceFile;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오전 10:02
 * To change this template use File | Settings | File Templates.
 */
public interface MediaSourceFileFactory {
	MediaSourceFile create(String mediaSource);

	MediaSourceFileFactory DEFAULT = new MediaSourceFileFactory() {
		@Override
		public MediaSourceFile create(String mediaSource) {
			if (mediaSource.startsWith("file://")) {
				String filePath = mediaSource.substring("file://".length());
				return new LocalStorageMediaSourceFile(filePath);
			}
			throw new IllegalArgumentException("not supported media source : " + mediaSource);
		}
	};
}
