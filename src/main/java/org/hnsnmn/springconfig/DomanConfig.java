package org.hnsnmn.springconfig;


import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.callback.DefaultResultCallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 5:53
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class DomanConfig {

	@Bean
	public ResultCallbackFactory resultCallbackFactory() {
		return new DefaultResultCallbackFactory();
	}
}
