package org.hnsnmn.domain.transcode;

import org.hnsnmn.domain.job.Job;

import java.io.File;
import java.util.List;

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

	public TranscodingServiceImple(MediaSourceCopier mediaSourceCopier, Transcoder transcoder,
								   ThumbnailExtractor thumbnailExtractor, CreatedFileSender createdFileSender,
								   JobResultNotifier jobResultNotifier, JobStateChnager jobStateChanger, TranscodingExceptionHandler transcodingExceptionHandler) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.transcoder = transcoder;
		this.thumbnailExtractor = thumbnailExtractor;
		this.createdFileSender = createdFileSender;
		this.jobResultNotifier = jobResultNotifier;
		this.jobStateChanger = jobStateChanger;
		this.transcodingExceptionHandler = transcodingExceptionHandler;
	}

	public void transcode(Long jobId) {
		changeJobState(jobId, Job.State.MEDIASOURCECOPYING);

		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		File multimediaFile = copyMultimediaSourceToLocal(jobId);

		changeJobState(jobId, Job.State.TRANSCODING);

		// 로컬에 복사된 파일을 변환처리 한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);

		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		List<File> thumbnails = extractThumbnail(multimediaFile, jobId);

		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		sendCreatedFileToDestination(multimediaFiles, thumbnails, jobId);

		// 결과를 통지
		notifyJobResultToRequester(jobId);

		changeJobState(jobId, Job.State.COMPLETED);
	}

	private void changeJobState(Long jobId, Job.State newJobState) {
		jobStateChanger.changeJobState(jobId, newJobState);
	}


	private File copyMultimediaSourceToLocal(Long jobId) {
		try {
			return mediaSourceCopier.copy(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		try {
			return transcoder.transcode(multimediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
		try {
			return thumbnailExtractor.extract(multimediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumbnails, Long jobId) {
		try {
			createdFileSender.send(multimediaFiles, thumbnails, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void notifyJobResultToRequester(Long jobId) {
		try {
			jobResultNotifier.notifyToRequester(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}
}
