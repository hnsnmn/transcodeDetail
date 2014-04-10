package org.hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 26.
 * Time: 오전 10:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class DestinationStorage {
	private String url;

	public DestinationStorage(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void save(List<File> multimediaFiles, List<File> thumbnails);
}
