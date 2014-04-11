package org.hnsnmn.springconfig;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.infra.repositories.JobData;
import org.hnsnmn.infra.repositories.JobImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 11.
 * Time: 오전 11:06
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class JpaJobRepository implements JobRepository {

	private final MediaSourceFileFactory mediaSourceFileFactory;
	private final DestinationStorageFactory destinationStorageFactory;
	private final ResultCallbackFactory resultCallbackFactory;

	@PersistenceContext
	private EntityManager entityManager;

	public JpaJobRepository(MediaSourceFileFactory mediaSourceFileFactory,
							DestinationStorageFactory destinationStorageFactory,
							ResultCallbackFactory resultCallbackFactory) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Transactional
	@Override
	public Job findById(Long jobId) {
		JobData jobData = entityManager.find(JobData.class, jobId);
		if (jobData == null) {
			return null;
		}
		return createJobFromJobData(jobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return new JobImpl(jobData, mediaSourceFileFactory, destinationStorageFactory, resultCallbackFactory);
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
