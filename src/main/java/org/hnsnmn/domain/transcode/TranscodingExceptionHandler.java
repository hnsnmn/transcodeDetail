package org.hnsnmn.domain.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 25.
 * Time: 오전 10:08
 * To change this template use File | Settings | File Templates.
 */
public interface TranscodingExceptionHandler {
	void notifyToJob(Long jobId, Exception ex);
}
