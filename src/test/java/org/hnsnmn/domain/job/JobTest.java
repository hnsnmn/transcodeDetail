package org.hnsnmn.domain.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 6:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobTest {
	@Mock
	private MediaSourceFile mediaSource;
	@Mock
	private File sourceFile;
	@Mock
	private Transcoder transcoder;
	@Mock
	private List<OutputFormat> outputFormats;
	@Mock
	private List<File> multimediafiles;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private List<File> thumbnails;
	@Mock
	private ResultCallback callback;
	@Mock
	private DestinationStorage destination;
	private final long jobId = 1L;

	@Test
	public void jobShouldBeCreatedStateWhenCreated() {
		Job job = new Job(mediaSource, destination, outputFormats, callback);
//		assertEquals(Job.State.CREATED, job.getLastState());
		assertEquals(Job.State.WAITING, job.getLastState());
		assertTrue(job.isWaiting());
		assertFalse(job.isFinished());
		assertFalse(job.isSuccess());
		assertFalse(job.isExceptionOccurred());
	}

	@Test
	public void transcodeSuccessfully() {
		when(mediaSource.getSourceFile()).thenReturn(sourceFile);
		when(transcoder.transcode(sourceFile, outputFormats)).thenReturn(
				multimediafiles);
		when(thumbnailExtractor.extract(sourceFile, jobId)).thenReturn(
				thumbnails);

		Job job = createWaitingJobWithID(jobId);
		job.transcode(transcoder, thumbnailExtractor);
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertTrue(job.isSuccess());
		assertTrue(job.isFinished());

		verify(mediaSource, only()).getSourceFile();
		verify(destination, only()).save(multimediafiles, thumbnails);
		verify(callback, only()).notifySuccessResult(jobId);

	}

	private Job createWaitingJobWithID(long jobId) {
		return new Job(jobId, Job.State.WAITING, mediaSource, destination, outputFormats, callback, null);
	}

	@Test
	public void jobShouldThrownExceptionWhenFailGetSourceFile() {
		RuntimeException exception = new RuntimeException("exception");
		when(mediaSource.getSourceFile()).thenThrow(exception);

		Job job = createWaitingJobWithID(jobId);

		try {
			job.transcode(transcoder, thumbnailExtractor);
			fail("발생해야함.");
		} catch (Exception e) {
		}
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertFalse(job.isSuccess());
		assertTrue(job.isFinished());
		assertTrue(job.isExceptionOccurred());

		verify(mediaSource, only()).getSourceFile();
		verify(destination, never()).save(multimediafiles, thumbnails);
		verify(callback, only()).notifyFailResult(jobId, Job.State.MEDIASOURCECOPYING, "exception");
	}
}
