package org.hnsnmn.domain.transcode;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 2:39
 * To change this template use File | Settings | File Templates.
 */
public interface MediaSourceCopier {
	File copy(Long jobId);
}
