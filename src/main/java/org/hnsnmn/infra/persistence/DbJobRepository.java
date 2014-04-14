package org.hnsnmn.infra.persistence;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 11.
 * Time: 오전 11:06
 * To change this template use File | Settings | File Templates.
 */
public class DbJobRepository implements JobRepository {

	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private ResultCallbackFactory resultCallbackFactory;
	private JobDataDao jobDataDao;

	public DbJobRepository(JobDataDao jobDataDao,
						   MediaSourceFileFactory mediaSourceFileFactory,
						   DestinationStorageFactory destinationStorageFactory,
						   ResultCallbackFactory resultCallbackFactory) {
		this.jobDataDao = jobDataDao;
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Override
	public Job findById(Long jobId) {
		JobData jobData = jobDataDao.findById(jobId);
		if (jobData == null) {
			return null;
		}
		return createJobFromJobData(jobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return new JobImpl(jobDataDao, jobData.getId(), jobData.getState(),
				mediaSourceFileFactory.create(jobData.getSourceUrl()),
				destinationStorageFactory.create(jobData.getDestinationUrl()),
				jobData.getOutputFormats(),
				resultCallbackFactory.create(jobData.getCallbackUrl()),
				jobData.getExceptionMessage());
	}

	@Override
	public Job save(Job job) {
		JobData.ExporterToJobData exporter = new JobData.ExporterToJobData();
		JobData jobData = job.exporter(exporter);
		JobData savedJobData = jobDataDao.save(jobData);
		return createJobFromJobData(savedJobData);
	}

	@Override
	public Job findEldestJobOfcreatedState() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
