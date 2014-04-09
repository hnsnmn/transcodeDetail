package org.hnsnmn.application.transcode;


import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 9.
 * Time: 오후 3:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {
	@Mock
	private Job job;
	@Mock
	private TranscodingService transcodingService;
	@Mock
	private JobRepository jobRepository;

	@Test
	public void runTranscodingWhenSuccessfullyWhenJobIsExists() {
		TranscodingRunner runner = new TranscodingRunner(transcodingService, jobRepository);
		when(jobRepository.findEldestJobOfcreatedState()).thenReturn(job);
		when(job.getId()).thenReturn(1L);

		runner.run();
		verify(transcodingService, only()).transcode(1L);
	}

	private class TranscodingRunner {
		private final TranscodingService transcodingService;
		private final JobRepository jobRepository;

		public TranscodingRunner(TranscodingService transcodingService, JobRepository jobRepository) {
			this.transcodingService = transcodingService;
			this.jobRepository = jobRepository;
		}

		public void run() {
			Job job = getNextJob();
			runTranscoding(job);
		}

		private Job getNextJob() {
			return jobRepository.findEldestJobOfcreatedState();
		}

		private void runTranscoding(Job job) {
			if (job == null)
				return;
			transcodingService.transcode(job.getId());
		}
	}
}
