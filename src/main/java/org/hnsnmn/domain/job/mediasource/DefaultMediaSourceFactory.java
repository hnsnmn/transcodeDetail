package org.hnsnmn.domain.job.mediasource;

import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.domain.job.MediaSourceFile;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 9.
* Time: 오전 10:55
* To change this template use File | Settings | File Templates.
*/
public class DefaultMediaSourceFactory implements MediaSourceFileFactory {
	@Override
	public MediaSourceFile create(String mediaSource) {
		if (mediaSource.startsWith("file://")) {
			return new LocalStorageMediaSourceFile(mediaSource);
		}
		throw new IllegalArgumentException("not supported media source : " + mediaSource);
	}
}
