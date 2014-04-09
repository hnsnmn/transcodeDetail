package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.domain.job.ThumbnailExtractor;
import org.hnsnmn.domain.job.Transcoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 4:50
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImple implements TranscodingService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final Transcoder transcoder;
	private final ThumbnailExtractor thumbnailExtractor;

	private JobRepository jobRepository;

	public TranscodingServiceImple(Transcoder transcoder,
								   ThumbnailExtractor thumbnailExtractor,
								   JobRepository jobRepository) {
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		checkJobExists(jobId, job);
		transcode(job);
	}

	private void checkJobExists(Long jobId, Job job) {
		if (job == null) {
			throw new JobNotFoundException(jobId);
		}
	}

	private void transcode(Job job) {
		try {
			job.transcode(transcoder, thumbnailExtractor);
		} catch (RuntimeException ex) {
			logger.error("faile to do transcoding job {}", job.getId(), ex);
			throw ex;
		}
	}
}
