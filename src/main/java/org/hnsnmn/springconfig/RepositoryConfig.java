package org.hnsnmn.springconfig;


import org.hnsnmn.domain.job.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 6:59
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RepositoryConfig {

	@Bean
	public JobRepository jobRepository() {
		return new JpaJobRepository();
	}
}
