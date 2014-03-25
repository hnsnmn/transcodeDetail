package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 4:50
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImple implements TranscodingService {
	private final MediaSourceCopier mediaSourceCopier;
	private final Transcoder transcoder;
	private final ThumbnailExtractor thumbnailExtractor;
	private final CreatedFileSender createdFileSender;
	private final JobResultNotifier jobResultNotifier;

	private JobStateChnager jobStateChanger;
	private TranscodingExceptionHandler transcodingExceptionHandler;
	private JobRepository jobRepository;

	public TranscodingServiceImple(MediaSourceCopier mediaSourceCopier, Transcoder transcoder,
								   ThumbnailExtractor thumbnailExtractor, CreatedFileSender createdFileSender,
								   JobResultNotifier jobResultNotifier, JobStateChnager jobStateChanger, TranscodingExceptionHandler transcodingExceptionHandler,
								   JobRepository jobRepository) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
		this.jobStateChanger = jobStateChanger;
		this.transcodingExceptionHandler = transcodingExceptionHandler;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(mediaSourceCopier, transcoder, thumbnailExtractor,
				createdFileSender, jobResultNotifier);
	}
}
