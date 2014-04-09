package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.*;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 8.
* Time: 오전 10:51
* To change this template use File | Settings | File Templates.
*/
public class AddJobServiceImpl implements AddJobService {
	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;
	private ResultCallback resultCallback;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory,
							 DestinationStorageFactory destinationStorageFactory,
							 JobRepository jobRepository,
							 ResultCallback resultCallback) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.jobRepository = jobRepository;
		this.resultCallback = resultCallback;
	}

	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job savedJob = saveJob(job);
		return savedJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
		DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
		return new Job(mediaSourceFile, destinationStorage, request.getOutputFormats(), resultCallback);
	}

	private Job saveJob(Job job) {
		return jobRepository.save(job);
	}
}
