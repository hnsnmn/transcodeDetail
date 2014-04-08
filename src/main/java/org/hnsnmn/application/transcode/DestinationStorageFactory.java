package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.DestinationStorage;
import org.hnsnmn.domain.job.FileDestinationStorage;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오전 10:19
 * To change this template use File | Settings | File Templates.
 */
public interface DestinationStorageFactory {
	DestinationStorage create(String destinationStorage);

	DestinationStorageFactory DEFAULT = new DestinationStorageFactory() {
		@Override
		public DestinationStorage create(String destinationStorage) {
			if (destinationStorage.startsWith("file://")) {
				return new FileDestinationStorage(destinationStorage.substring("file://".length()));
			}
			throw new IllegalArgumentException("not supported destination storage : " + destinationStorage);
		}
	};
}
