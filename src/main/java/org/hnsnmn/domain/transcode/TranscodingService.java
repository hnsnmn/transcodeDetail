package org.hnsnmn.domain.transcode;

import java.io.File;
import java.util.List;

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

	public void transcode(Long jobId) {
		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		File multimediaFile = copyMultimediaSourceToLocal(jobId);

		// 로컬에 복사된 파일을 변환처리 한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);

		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		List<File> thumbnails = extractThumbnail(multimediaFile, jobId);

		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		sendCreatedFileToDestination(multimediaFiles, thumbnails, jobId);

		// 결과를 통지
		notifyJobResultToRequester(jobId);
	}


	private File copyMultimediaSourceToLocal(Long jobId) {
		return mediaSourceCopier.copy(jobId);
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		return transcoder.transcode(multimediaFile, jobId);
	}

	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
		return thumbnailExtractor.extract(multimediaFile, jobId);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumbnails, Long jobId) {
		createdFileSender.send(multimediaFiles, thumbnails, jobId);
	}

	private void notifyJobResultToRequester(Long jobId) {
		jobResultNotifier.notifyToRequester(jobId);
	}
}
