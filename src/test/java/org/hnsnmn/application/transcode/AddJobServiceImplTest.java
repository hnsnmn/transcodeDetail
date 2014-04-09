package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오전 9:57
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddJobServiceImplTest {

	@Mock
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Mock
	private MediaSourceFile mockMediaSourceFile;
	@Mock
	private DestinationStorage mockDestinationStorage;
	@Mock
	private DestinationStorageFactory destinationStorageFactory;
	@Mock
	private JobRepository jobRepository;
	@Mock
	private ResultCallbackFactory resultCallbackFactory;
	@Mock
	private ResultCallback mockResultCallback;

	@Test
	public void addJob() {
		AddJobRequest request = new AddJobRequest();
		when(mediaSourceFileFactory.create(request.getMediaSource())).thenReturn(mockMediaSourceFile);
		when(destinationStorageFactory.create(request.getDestinationStorage())).thenReturn(mockDestinationStorage);
		when(resultCallbackFactory.create(request.getResultCallback())).thenReturn(mockResultCallback);

		final Long mockJobId = new Long(1);
		Job mockSavedJob = mock(Job.class);
		when(mockSavedJob.getId()).thenReturn(mockJobId);
		when(jobRepository.save(any(Job.class))).thenReturn(mockSavedJob);

		AddJobService addJobServiceImpl = new AddJobServiceImpl(mediaSourceFileFactory, destinationStorageFactory, resultCallbackFactory, jobRepository);
		Long jobId = addJobServiceImpl.addJob(request);

		assertNotNull(jobId);
		verify(jobRepository, only()).save(any(Job.class));
		verify(mediaSourceFileFactory, only()).create(request.getMediaSource());
		verify(destinationStorageFactory, only()).create(request.getDestinationStorage());
		verify(resultCallbackFactory, only()).create(request.getResultCallback());

	}

}
