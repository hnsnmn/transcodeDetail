package org.hnsnmn.domain.transcode;


import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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
	private MediaSourceCopier mediaSourceCopier;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private CreatedFileSender createdFileSender;
	@Mock
	private JobResultNotifier jobResultNotifier;
	@Mock
	private JobStateChnager jobStateChnager;
	@Mock
	private JobRepository jobRepository;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;

	private TranscodingService transcodingService;

	private final Long jobId = new Long(1);

	private Job mockJob = new Job();

	private final File mockMultimediaFile = mock(File.class);
	private final List<File> mockMultimediaFiles = new ArrayList<File>();
	private final List<File> mockThumbnails = new ArrayList<File>();
	private final RuntimeException mockException = new RuntimeException();

	@Before
	public void setUp() {
		transcodingService = new TranscodingServiceImple(mediaSourceCopier, transcoder, thumbnailExtractor,
				createdFileSender, jobResultNotifier, jobStateChnager, transcodingExceptionHandler);

		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				State newState = (State) invocation.getArguments()[1];
				mockJob.changeState(newState);
				return null;
			}
		}).when(jobStateChnager).changeJobState(anyLong(), any(State.class));

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				RuntimeException ex = (RuntimeException) invocation.getArguments()[1];
				mockJob.exceptionOccurred(ex);
				return null;  //To change body of implemented methods use File | Settings | File Templates.
			}
		}).when(transcodingExceptionHandler).notifyToJob(anyLong(), any(RuntimeException.class));
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
		verify(mediaSourceCopier, only()).copy(jobId);

		if (verifyOption.transcoderNever)
			verify(transcoder, never()).transcode(mockMultimediaFile, jobId);
		else
			verify(transcoder, only()).transcode(mockMultimediaFile, jobId);

		if (verifyOption.thumbnailExtractorNever)
			verify(thumbnailExtractor, never()).extract(mockMultimediaFile, jobId);
		else
			verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);

		if (verifyOption.createdFileSenderNever)
			verify(createdFileSender, never()).send(mockMultimediaFiles, mockThumbnails, jobId);
		else
			verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumbnails, jobId);

		if (verifyOption.jobResultNotifierNever)
			verify(jobResultNotifier, never()).notifyToRequester(jobId);
		else
			verify(jobResultNotifier, only()).notifyToRequester(jobId);

	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);

		executeFaillingTranscodeAndAssertFail(State.MEDIASOURCECOPYING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = true;
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createdFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenThrow(mockException);

		executeFaillingTranscodeAndAssertFail(State.TRANSCODING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createdFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;
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
		public boolean createdFileSenderNever;
		public boolean jobResultNotifierNever;
	}
}
