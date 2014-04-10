package org.hnsnmn.domain.job;

import static org.hnsnmn.domain.job.Job.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 6:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class ResultCallback {
	private String url;

	public ResultCallback(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void notifySuccessResult(Long jobId);

	public abstract void notifyFailResult(Long jobId, State lastState, String errorCause);
}
