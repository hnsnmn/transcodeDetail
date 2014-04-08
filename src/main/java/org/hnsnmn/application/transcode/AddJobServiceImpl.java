package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.DestinationStorage;
import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.domain.job.MediaSourceFile;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 8.
* Time: 오전 10:51
* To change this template use File | Settings | File Templates.
*/
public class AddJobServiceImpl implements AddJobService {
	private final MediaSourceFileFactory mediaSourceFileFactory;
	private final DestinationStorageFactory destinationStorageFactory;
	private final JobRepository jobRepository;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory, JobRepository jobRepository) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.jobRepository = jobRepository;
	}

	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job savedJob = saveJob(job);
		return savedJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
		DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
		return new Job(mediaSourceFile, destinationStorage, request.getOutputFormats());
	}

	private Job saveJob(Job job) {
		return jobRepository.save(job);
	}
}
