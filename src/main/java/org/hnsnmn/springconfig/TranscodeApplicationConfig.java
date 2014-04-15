package org.hnsnmn.springconfig;

import org.hnsnmn.application.transcode.*;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.domain.job.ThumbnailExtractor;
import org.hnsnmn.domain.job.Transcoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 6:56
 * To change this template use File | Settings | File Templates.
 */
public class TranscodeApplicationConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private Transcoder transcoder;
	@Autowired
	private ThumbnailExtractor thumbnailExtractor;


	@Bean
	public TranscodingRunner transcodingRunner() {
		return new TranscodingRunner(transcodingService(), jobQueue());
	}

	@Bean
	public AddJobService addJobService() {
		return new AddJobServiceImpl(mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory,
				jobRepository, jobQueue());
	}

	@Bean
	public TranscodingService transcodingService() {
		return new TranscodingServiceImple(transcoder, thumbnailExtractor, jobRepository);
	}

	public JobQueue jobQueue() {
		return new MemoryJobQueue();
	}
}
