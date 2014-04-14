package org.hnsnmn.domain.job.destination;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.domain.job.DestinationStorage;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 9.
* Time: 오전 10:55
* To change this template use File | Settings | File Templates.
*/
public class DefaultDestinationStorageFactory implements DestinationStorageFactory {
	@Override
	public DestinationStorage create(String destinationStorage) {
		if (destinationStorage.startsWith("file://")) {
			return new FileDestinationStorage(destinationStorage);
		}
		throw new IllegalArgumentException("not supported destination storage " + destinationStorage);
	}
}
