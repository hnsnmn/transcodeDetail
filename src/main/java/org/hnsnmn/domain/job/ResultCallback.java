package org.hnsnmn.domain.job;

import static org.hnsnmn.domain.job.Job.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 6:08
 * To change this template use File | Settings | File Templates.
 */
public interface ResultCallback {
	void notifySuccessResult(long jobId);

	void notifyFailResult(long jobId, State lastState, String errorCause);
}
