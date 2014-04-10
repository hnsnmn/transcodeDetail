package org.hnsnmn.domain.job.callback;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.ResultCallback;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오전 10:28
 * To change this template use File | Settings | File Templates.
 */
public class HttpResultCallback implements ResultCallback {
	public HttpResultCallback(String url) {
		//To change body of created methods use File | Settings | File Templates.
	}

	@Override
	public void notifySuccessResult(long jobId) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void notifyFailResult(long jobId, Job.State lastState, String errorCause) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
