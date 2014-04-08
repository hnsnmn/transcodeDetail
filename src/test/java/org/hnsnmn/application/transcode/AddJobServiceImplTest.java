package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.DestinationStorage;
import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.domain.job.MediaSourceFile;
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

	@Test
	public void addJob() {
		AddJobRequest request = new AddJobRequest();
		when(mediaSourceFileFactory.create(request.getMediaSource())).thenReturn(mockMediaSourceFile);
		when(destinationStorageFactory.create(request.getDestinationStorage())).thenReturn(mockDestinationStorage);

		final Long mockJobId = new Long(1);
		Job mockSavedJob = mock(Job.class);
		when(mockSavedJob.getId()).thenReturn(mockJobId);
		when(jobRepository.save(any(Job.class))).thenReturn(mockSavedJob);

		AddJobService addJobService = new AddJobService(mediaSourceFileFactory, destinationStorageFactory, jobRepository);
		Long jobId = addJobService.addJob(request);

		assertNotNull(jobId);
		verify(jobRepository, only()).save(any(Job.class));
		verify(mediaSourceFileFactory, only()).create(request.getMediaSource());
		verify(destinationStorageFactory, only()).create(request.getDestinationStorage());

	}

	private class AddJobRequest {
		public String getMediaSource() {
			return null;  //To change body of created methods use File | Settings | File Templates.
		}

		public String getDestinationStorage() {
			return null;  //To change body of created methods use File | Settings | File Templates.
		}
	}

	private class AddJobService {
		public AddJobService(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory, JobRepository jobRepository) {
		}

		public Long addJob(AddJobRequest request) {
			return null;  //To change body of created methods use File | Settings | File Templates.
		}
	}
}
