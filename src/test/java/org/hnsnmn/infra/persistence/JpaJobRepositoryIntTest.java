package org.hnsnmn.infra.persistence;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.Container;
import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.callback.HttpResultCallback;
import org.hnsnmn.domain.job.destination.FileDestinationStorage;
import org.hnsnmn.domain.job.mediasource.LocalStorageMediaSourceFile;
import org.hnsnmn.springconfig.ApplicationContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 7:06
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class})
public class JpaJobRepositoryIntTest {
	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobRepository jobRepository;

	@Test
	public void findById() {
		Job job = jobRepository.findById(1L);
		assertNotNull(job);
		assertTrue(job.isWaiting());
		assertEquals(2, job.getOutputformats().size());
	}

	@Test
	public void save() {
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(60, 40, 150, Container.MP4));

		Job job = new Job(new LocalStorageMediaSourceFile("file://./video.avi"),
				new FileDestinationStorage("file://./target"), outputFormats,
				new HttpResultCallback("http://"));

		Job savedJob = jobRepository.save(job);
		assertNotNull(savedJob);
		assertNotNull(savedJob.getId());
		assertJobEquals(job, savedJob);
	}

	private void assertJobEquals(Job job, Job savedJob) {
		assertEquals(job.getOutputformats().size(), savedJob.getOutputformats().size());
	}
}
