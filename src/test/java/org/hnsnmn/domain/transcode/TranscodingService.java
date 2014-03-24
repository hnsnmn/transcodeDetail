package org.hnsnmn.domain.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 4:50
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingService {
	private final MediaSourceCopier mediaSourceCopier;
	private final Transcoder transcoder;
	private final ThumbnailExtractor thumbnailExtractor;
	private final CreatedFileSender createdFileSender;
	private final JobResultNotifier jobResultNotifier;

	public TranscodingService(MediaSourceCopier mediaSourceCopier, Transcoder transcoder,
							  ThumbnailExtractor thumbnailExtractor, CreatedFileSender createdFileSender,
							  JobResultNotifier jobResultNotifier) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
	}
}
