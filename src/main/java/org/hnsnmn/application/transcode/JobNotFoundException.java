package org.hnsnmn.application.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 9.
 * Time: 오전 11:31
 * To change this template use File | Settings | File Templates.
 */
public class JobNotFoundException extends RuntimeException {
	public JobNotFoundException(Long jobId) {
		super("Not found Job[" + jobId + "]");
	}
}
