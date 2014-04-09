package org.hnsnmn.domain.job.destination;

import org.hnsnmn.domain.job.DestinationStorage;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 5:38
 * To change this template use File | Settings | File Templates.
 */
public class FileDestinationStorage implements DestinationStorage {
	private String folder;

	public FileDestinationStorage(String folder) {
		this.folder = folder;
	}

	@Override
	public void save(List<File> multimediaFiles, List<File> thumbnails) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
