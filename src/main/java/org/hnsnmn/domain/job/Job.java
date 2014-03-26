package org.hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 5:59
 * To change this template use File | Settings | File Templates.
 */
public class Job {

	private final Long id;
	private final MediaSourceFile mediaSourceFile;
	private final DestinationStorage destinationStorage;

	public Job(Long id, MediaSourceFile mediaSourceFile, DestinationStorage destinationStorage) {
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
	}

	public enum State {
		COMPLETED, MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, SENDING, NOTIFYING;

	}
	private State state;

	private Exception occurredException;
	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	private void changeState(State newState) {
		this.state = newState;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public Exception getOccurredException() {
		return occurredException;
	}


	private void exceptionOccurred(RuntimeException ex) {
		this.occurredException = ex;
	}

	public void transcode(Transcoder transcoder,
						  ThumbnailExtractor thumbnailExtractor,
						  JobResultNotifier jobResultNotifier) {
		try {

			// 미디어 원본으로부터 파일을 로컬에 복사한다.
			File multimediaFile = copyMultimediaSourceToLocal();

			// 로컬에 복사된 파일을 변환처리 한다.
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);

			// 로컬에 복사된 파일로부터 이미지를 추출한다.
			List<File> thumbnails = extractThumbnail(multimediaFile, thumbnailExtractor);

			// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
			sendCreatedFileToDestination(multimediaFiles, thumbnails);

			// 결과를 통지
			notifyJobResultToRequester(jobResultNotifier);
			completed();
		} catch (RuntimeException ex) {
			exceptionOccurred(ex);
			throw ex;
		}
	}

	private File copyMultimediaSourceToLocal() {
		changeState(State.MEDIASOURCECOPYING);
		return mediaSourceFile.getSourceFile();
	}


	private List<File> transcode(File multimediaFile, Transcoder transcoder) {
		changeState(State.TRANSCODING);
		return transcoder.transcode(multimediaFile, id);
	}

	private List<File> extractThumbnail(File multimediaFile, ThumbnailExtractor thumbnailExtractor) {
		changeState(State.EXTRACTINGTHUMBNAIL);
		return thumbnailExtractor.extract(multimediaFile, id);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumbnails) {
		changeState(State.SENDING);
		destinationStorage.save(multimediaFiles, thumbnails);
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		changeState(State.NOTIFYING);
		jobResultNotifier.notifyToRequester(id);
	}

	private void completed() {
		changeState(State.COMPLETED);
	}
}
