package org.hnsnmn.infra.persistence;

import org.hnsnmn.domain.job.*;
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

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 7:06
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class})
public class DbJobRepositoryIntTest {
	@Autowired
	private JobRepository jobRepository;

	@Test
	public void findById() {
		Job job = jobRepository.findById(1L);
		assertNotNull(job);
		assertTrue(job.isWaiting());
		assertEquals(2, job.getOutputformats().size());
		assertNotNull(job.getThumbnailPolicy());
		assertEquals(ThumbnailPolicy.Option.FIRST, job.getThumbnailPolicy().getOption());
	}

	@Test
	public void save() {
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(60, 40, 150, Container.MP4));

		ThumbnailPolicy thumbnailPolicy = new ThumbnailPolicy();
		Job job = new Job(new LocalStorageMediaSourceFile("file://./video.avi"),
				new FileDestinationStorage("file://./target"), outputFormats,
				new HttpResultCallback("http://"), thumbnailPolicy);

		Job savedJob = jobRepository.save(job);
		assertNotNull(savedJob);
		assertNotNull(savedJob.getId());
		assertJobEquals(job, savedJob);
	}

	private void assertJobEquals(Job job, Job savedJob) {
		assertEquals(job.getOutputformats().size(), savedJob.getOutputformats().size());
	}
}
