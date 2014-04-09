package org.hnsnmn.domain.job.mediasource;

import org.hnsnmn.domain.job.MediaSourceFile;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 5:09
 * To change this template use File | Settings | File Templates.
 */
public class LocalStorageMediaSourceFile implements MediaSourceFile {
	private String filePath;

	public LocalStorageMediaSourceFile(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public File getSourceFile() {
		return new File(filePath);
	}
}
