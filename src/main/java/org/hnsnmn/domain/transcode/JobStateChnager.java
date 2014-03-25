package org.hnsnmn.domain.transcode;

import org.hnsnmn.domain.job.Job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 6:25
 * To change this template use File | Settings | File Templates.
 */
public interface JobStateChnager {
	void changeJobState(Long jobId, Job.State jobState);
}
