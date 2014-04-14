package org.hnsnmn.springconfig;


import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.JobRepository;
import org.hnsnmn.infra.repositories.JobDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 6:59
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.hnsnmn.infra.persistence")
public class RepositoryConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobDataDao jobDataDao;

	@Bean
	public JobRepository jobRepository() {
		return new DbJobRepository(jobDataDao,
				mediaSourceFileFactory,
				destinationStorageFactory,
				resultCallbackFactory);
	}
}
