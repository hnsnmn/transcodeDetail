package org.hnsnmn.domain.job;

import org.hnsnmn.application.transcode.LocalStorageMediaSourceFile;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 4:46
 * To change this template use File | Settings | File Templates.
 */
public class MediaSourceFileFactoryDefaulttTest {
	@Test
	public void createLocalStorageMediaSourceFile() {
		MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;
		MediaSourceFile sourceFile = factory.create("file://./src/test/resources/sample.avi");

		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(sourceFile.getSourceFile().exists());

	}
}
