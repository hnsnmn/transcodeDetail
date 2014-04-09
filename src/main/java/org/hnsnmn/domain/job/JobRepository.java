package org.hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 5:57
 * To change this template use File | Settings | File Templates.
 */
public interface JobRepository {
	Job findById(Long jobId);

	Job save(Job job);

	Job findEldestJobOfcreatedState();
}
