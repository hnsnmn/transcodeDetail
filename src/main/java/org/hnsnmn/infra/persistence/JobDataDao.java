package org.hnsnmn.infra.persistence;


import org.springframework.data.repository.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 14.
 * Time: 오후 2:30
 * To change this template use File | Settings | File Templates.
 */
public interface JobDataDao extends Repository<JobData, Long> {
	public JobData save(JobData jobData);

	public JobData findById(Long id);
}
