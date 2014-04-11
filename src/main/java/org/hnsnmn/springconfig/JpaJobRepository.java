package org.hnsnmn.springconfig;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 11.
 * Time: 오전 11:06
 * To change this template use File | Settings | File Templates.
 */
public class JpaJobRepository implements JobRepository {
	@Override
	public Job findById(Long jobId) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Job save(Job job) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Job findEldestJobOfcreatedState() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
