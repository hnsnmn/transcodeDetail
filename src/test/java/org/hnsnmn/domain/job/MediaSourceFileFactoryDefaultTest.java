package org.hnsnmn.domain.job;

import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 4:46
 * To change this template use File | Settings | File Templates.
 */
public class MediaSourceFileFactoryDefaultTest {
	private MediaSourceFileFactory factory = new DefaultMediaSourceFactory();

	@Test
	public void createLocalStorageMediaSourceFile() {
		MediaSourceFile sourceFile = factory.create("file://./src/test/resources/sample.avi");

		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(sourceFile.getSourceFile().exists());

	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedSource() {
		factory.create("xxx://www.daum.net");
		fail("must throw exception");
	}

	private class DefaultMediaSourceFactory implements MediaSourceFileFactory {
		@Override
		public MediaSourceFile create(String mediaSource) {
			if (mediaSource.startsWith("file://")) {
				String filePath = mediaSource.substring("file://".length());
				return new LocalStorageMediaSourceFile(filePath);
			}
			throw new IllegalArgumentException("not supported media source : " + mediaSource);
		}
	}
}
