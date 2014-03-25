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

	private TranscodingService transcodingService;

	private final Long jobId = new Long(1);

	private Job mockJob = new Job();

	@Before
	public void setUp() {
		transcodingService = new TranscodingServiceImple(mediaSourceCopier, transcoder, thumbnailExtractor,
				createdFileSender, jobResultNotifier, jobStateChnager);

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Job.State newState = (Job.State) invocation.getArguments()[1];
				mockJob.changeState(newState);
				return null;
			}
		}).when(jobStateChnager).changeJobState(anyLong(), any(Job.State.class));
	}

	@Test
	public void transcodeSuccessfully() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumbnails = new ArrayList<File>();
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);

		transcodingService.transcode(jobId);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumbnails, jobId);
		verify(jobResultNotifier, only()).notifyToRequester(jobId);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		RuntimeException mockException = new RuntimeException();
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);
		try {
			transcodingService.transcode(jobId);
			fail("발생해야 함");
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);
		assertFalse(job.isSuccess());
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, never()).transcode(any(File.class), anyLong());
		verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}

}
