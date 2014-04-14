package org.hnsnmn.infra.persistence;

import org.hnsnmn.domain.job.*;
import org.hnsnmn.springconfig.ApplicationContextConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 14.
 * Time: 오후 1:56
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class })
public class JobIntTest {
	@Autowired
	private JobRepository jobRepository;

	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;

	@Before
	public void setUp() {
		transcoder = mock(Transcoder.class);
		thumbnailExtractor = mock(ThumbnailExtractor.class);
	}

	@Test
	public void jobShouldChangeStateInDB() {
		RuntimeException exception = new RuntimeException("강제발생");
		when(transcoder.transcode(any(File.class), anyListOf(OutputFormat.class))).thenThrow(exception);

		Long jobId = new Long(1);
		Job job = jobRepository.findById(jobId); // DB로부터 Job 로딩
		try {
			job.transcode(transcoder, thumbnailExtractor); // Job 기능 실행
		} catch (RuntimeException ex) {
		}

		Job updatedJob = jobRepository.findById(jobId); // DB에서 동일 Job로딩
		assertEquals(Job.State.TRANSCODING, job.getLastState());
		assertEquals(Job.State.TRANSCODING, updatedJob.getLastState());	// 반영 확인
	}
}
