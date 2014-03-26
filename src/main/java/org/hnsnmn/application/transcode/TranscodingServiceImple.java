package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 4:50
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImple implements TranscodingService {
	private final Transcoder transcoder;
	private final ThumbnailExtractor thumbnailExtractor;
	private final JobResultNotifier jobResultNotifier;

	private JobRepository jobRepository;

	public TranscodingServiceImple(Transcoder transcoder,
								   ThumbnailExtractor thumbnailExtractor,
								   JobResultNotifier jobResultNotifier, JobRepository jobRepository) {
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.jobResultNotifier = jobResultNotifier;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumbnailExtractor, jobResultNotifier);
	}
}
