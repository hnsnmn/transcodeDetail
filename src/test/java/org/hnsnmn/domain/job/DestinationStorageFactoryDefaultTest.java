package org.hnsnmn.domain.job;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 5:34
 * To change this template use File | Settings | File Templates.
 */
public class DestinationStorageFactoryDefaultTest {
	private DestinationStorageFactory factory = new DefaultDestinationStorageFactory();

	@Test
	public void createFileDestinationStorage() {
		DestinationStorage destinationStorage = factory.create("file://usr/local");
		assertTrue(destinationStorage instanceof FileDestinationStorage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedDestination() {
		factory.create("xxx://www.daum.net");
		fail("must throw exception");
	}

	private class DefaultDestinationStorageFactory implements DestinationStorageFactory {
		@Override
		public DestinationStorage create(String destinationStorage) {
			if (destinationStorage.startsWith("file://")) {
				return new FileDestinationStorage(destinationStorage.substring("file://".length()));
			}
			throw new IllegalArgumentException("not supported destination storage " + destinationStorage);
		}
	}
}
