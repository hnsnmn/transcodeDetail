package org.hnsnmn.application.transcode;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 2:52
 * To change this template use File | Settings | File Templates.
 */
public interface Transcoder {
	List<File> transcode(File mockMultimediaFile, Long jobId);
}
