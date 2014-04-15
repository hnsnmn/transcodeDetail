package org.hnsnmn.domain.job;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 5:59
 * To change this template use File | Settings | File Templates.
 */
public class Job {


	public enum State {
		COMPLETED, MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, SENDING, NOTIFYING, WAITING;

	}

	private Long id;
	private State state;

	private MediaSourceFile mediaSourceFile;
	private DestinationStorage destinationStorage;

	private ResultCallback callback;
	private List<OutputFormat> outputFormats;

	private ThumbnailPolicy thumbnailPolicy;

	public Job(Long id, State state, MediaSourceFile mediaSourceFile,
			   DestinationStorage destinationStorage,
			   List<OutputFormat> outputFormats,
			   ResultCallback callback,
			   ThumbnailPolicy thumbnailPolicy, String errorMessage) {
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
		this.outputFormats = outputFormats;
		this.callback = callback;
		this.state = state;
		this.thumbnailPolicy = thumbnailPolicy;
		this.exceptionMessage = errorMessage;
	}


	public Job(MediaSourceFile mediaSourceFile,
			   DestinationStorage destinationStorage,
			   List<OutputFormat> outputFormats,
			   ResultCallback callback, ThumbnailPolicy thumbnailPolicy) {
		this(null, State.WAITING, mediaSourceFile, destinationStorage, outputFormats, callback, thumbnailPolicy, null);
	}

	public Long getId() {
		return id;
	}

	private String exceptionMessage;

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	protected void changeState(State newState) {
		this.state = newState;
	}

	public boolean isExceptionOccurred() {
		return exceptionMessage != null;
	}

	public boolean isWaiting() {
		return state == State.WAITING;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	protected void exceptionOccurred(RuntimeException ex) {
		exceptionMessage = ex.getMessage();
		callback.notifyFailResult(id, state, exceptionMessage);
	}


	public List<OutputFormat> getOutputformats() {
		return Collections.unmodifiableList(outputFormats);
	}

	public ThumbnailPolicy getThumbnailPolicy() {
		return thumbnailPolicy;
	}

	public void transcode(Transcoder transcoder,
						  ThumbnailExtractor thumbnailExtractor) {
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
			notifyJobResultToRequester();
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
		return transcoder.transcode(multimediaFile, outputFormats);
	}

	private List<File> extractThumbnail(File multimediaFile, ThumbnailExtractor thumbnailExtractor) {
		changeState(State.EXTRACTINGTHUMBNAIL);
		return thumbnailExtractor.extract(multimediaFile, thumbnailPolicy);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumbnails) {
		changeState(State.SENDING);
		destinationStorage.save(multimediaFiles, thumbnails);
	}

	private void notifyJobResultToRequester() {
		changeState(State.NOTIFYING);
		callback.notifySuccessResult(id);
	}

	private void completed() {
		changeState(State.COMPLETED);
	}

	public <T> T exporter(Exporter<T> exporter) {
		exporter.addId(id);
		exporter.addState(state);
		exporter.addMediaSource(mediaSourceFile.getUrl());
		exporter.addDestinationStorage(destinationStorage.getUrl());
		exporter.addResultCallback(callback.getUrl());
		exporter.addOutputFormat(getOutputformats());
		exporter.addExceptionMessage(exceptionMessage);
		exporter.addThumbnailPolicy(thumbnailPolicy);
		return exporter.build();
	}

	public static interface Exporter<T> {
		public void addId(Long id);

		public void addState(State state);

		public void addMediaSource(String url);

		public void addDestinationStorage(String url);

		public void addResultCallback(String url);

		public void addExceptionMessage(String exceptionMessage);

		public void addOutputFormat(List<OutputFormat> outputFormats);

		public void addThumbnailPolicy(ThumbnailPolicy thumbnailPolicy);

		public T build();
	}
}
