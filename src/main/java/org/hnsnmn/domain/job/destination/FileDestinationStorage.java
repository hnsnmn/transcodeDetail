package org.hnsnmn.domain.job.destination;

import org.hnsnmn.domain.job.DestinationStorage;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 5:38
 * To change this template use File | Settings | File Templates.
 */
public class FileDestinationStorage extends DestinationStorage {

	public FileDestinationStorage(String url) {
		super(url);
	}

	@Override
	public void save(List<File> multimediaFiles, List<File> thumbnails) {
		try {
			copy(multimediaFiles, getUrl());
		} catch (IOException ex) {
			throw new RuntimeException("Fail to copy: " + ex.getMessage(), ex);
		}

	}

	private void copy(List<File> files, String folder) throws IOException {
		for (File file : files) {
			copy(file, folder);
		}
	}

	private void copy(File source, String folder) throws IOException {
		String fileName = getFileName(source);
		File target = new File(folder, fileName);
		copy(source, target);
	}

	private String getFileName(File source) {
		return source.getName();
	}

	private void copy(File source, File target) throws IOException {
		BufferedInputStream is = null;
		BufferedOutputStream os = null;

		try {
			is = new BufferedInputStream(new FileInputStream(source));
			os = new BufferedOutputStream(new FileOutputStream(target));
			byte[] data = new byte[8096];
			int len = -1;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}

		} finally {
			closeStream(is);
			closeStream(os);
		}
	}

	private void closeStream(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}
}
