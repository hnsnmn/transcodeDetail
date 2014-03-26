package org.hnsnmn.application.transcode;


import org.hnsnmn.domain.job.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hnsnmn.domain.job.Job.State;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranscodingServiceTest {

	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private JobResultNotifier jobResultNotifier;
	@Mock
	private JobStateChnager jobStateChnager;
	@Mock
	private JobRepository jobRepository;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private DestinationStorage destinationStroage;

	private TranscodingService transcodingService;

	private final Long jobId = new Long(1);

	private Job mockJob;
	private final File mockMultimediaFile = mock(File.class);
	private final List<File> mockMultimediaFiles = new ArrayList<File>();
	private final List<File> mockThumbnails = new ArrayList<File>();
	private final RuntimeException mockException = new RuntimeException();

	@Before
	public void setUp() {
		transcodingService = new TranscodingServiceImple(transcoder, thumbnailExtractor,
				jobResultNotifier, jobRepository);

		mockJob = new Job(jobId, mediaSourceFile, destinationStroage);

		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);
	}

	@Test
	public void transcodeSuccessfully() {
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		transcodingService.transcode(jobId);

		job = jobRepository.findById(jobId);
		assertTrue(job.isFinished());
		assertTrue(job.isSuccess());
		assertEquals(State.COMPLETED, job.getLastState());
		assertNull(job.getOccurredException());

		VerifyOption verifyOption = new VerifyOption();
		verifyCollaboration(verifyOption);

	}

	private void verifyCollaboration(VerifyOption verifyOption) {
		if (verifyOption.transcoderNever)
			verify(transcoder, never()).transcode(mockMultimediaFile, jobId);
		else
			verify(transcoder, only()).transcode(mockMultimediaFile, jobId);

		if (verifyOption.thumbnailExtractorNever)
			verify(thumbnailExtractor, never()).extract(mockMultimediaFile, jobId);
		else
			verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);

		if (verifyOption.destinationStorageNever)
			verify(destinationStroage, never()).save(mockMultimediaFiles, mockThumbnails);
		else
			verify(destinationStroage, only()).save(mockMultimediaFiles, mockThumbnails);

		if (verifyOption.jobResultNotifierNever)
			verify(jobResultNotifier, never()).notifyToRequester(jobId);
		else
			verify(jobResultNotifier, only()).notifyToRequester(jobId);

	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceFile() {
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);
		executeFaillingTranscodeAndAssertFail(State.MEDIASOURCECOPYING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = true;
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.destinationStorageNever = true;
		verifyOption.jobResultNotifierNever = true;
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenThrow(mockException);

		executeFaillingTranscodeAndAssertFail(State.TRANSCODING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.destinationStorageNever = true;
		verifyOption.jobResultNotifierNever = true;
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumbnailExtractor() {
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenThrow(mockException);

		executeFaillingTranscodeAndAssertFail(State.EXTRACTINGTHUMBNAIL);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.destinationStorageNever = true;
		verifyOption.jobResultNotifierNever = true;
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtCreatedFileSender() {
		doThrow(mockException).when(destinationStroage).save(mockMultimediaFiles, mockThumbnails);
		executeFaillingTranscodeAndAssertFail(State.SENDING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.jobResultNotifierNever = true;
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
		doThrow(mockException).when(jobResultNotifier).notifyToRequester(jobId);
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		executeFaillingTranscodeAndAssertFail(State.NOTIFYING);

		VerifyOption verifyOption = new VerifyOption();
		verifyCollaboration(verifyOption);
	}

	private void executeFaillingTranscodeAndAssertFail(State expectedLastState) {
		try {
			transcodingService.transcode(jobId);
			fail("발생해야 함");
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		assertEquals(expectedLastState, job.getLastState());
		assertNotNull(job.getOccurredException());
	}

	private class VerifyOption {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean destinationStorageNever;
		public boolean jobResultNotifierNever;
	}
}
