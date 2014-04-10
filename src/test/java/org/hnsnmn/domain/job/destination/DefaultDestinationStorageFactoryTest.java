package org.hnsnmn.domain.job.destination;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.domain.job.DestinationStorage;
import org.hnsnmn.domain.job.destination.DefaultDestinationStorageFactory;
import org.hnsnmn.domain.job.destination.FileDestinationStorage;
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
public class DefaultDestinationStorageFactoryTest {
	private DestinationStorageFactory factory = new DefaultDestinationStorageFactory();

	@Test
	public void shouldCreateFileDestinationStorage() {
		DestinationStorage destinationStorage = factory.create("file://usr/local");
		assertTrue(destinationStorage instanceof FileDestinationStorage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenUrlIsNotSupport() {
		factory.create("xxx://www.daum.net");
		fail("must throw exception");
	}

}
