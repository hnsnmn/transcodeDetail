package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 9.
* Time: 오후 3:17
* To change this template use File | Settings | File Templates.
*/
public class TranscodingRunner {
	private final TranscodingService transcodingService;
	private final JobRepository jobRepository;

	public TranscodingRunner(TranscodingService transcodingService, JobRepository jobRepository) {
		this.transcodingService = transcodingService;
		this.jobRepository = jobRepository;
	}

	public void run() {
		Job job = getNextJob();
		runTranscoding(job);
	}

	private Job getNextJob() {
		return jobRepository.findEldestJobOfcreatedState();
	}

	private void runTranscoding(Job job) {
		if (job == null)
			return;
		transcodingService.transcode(job.getId());
	}
}
