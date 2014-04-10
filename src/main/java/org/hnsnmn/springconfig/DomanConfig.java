package org.hnsnmn.springconfig;


import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.callback.DefaultResultCallbackFactory;
import org.hnsnmn.domain.job.destination.DefaultDestinationStorageFactory;
import org.hnsnmn.domain.job.mediasource.DefaultMediaSourceFactory;
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

	@Bean
	public DestinationStorageFactory destinationStorageFactory() {
		return new DefaultDestinationStorageFactory();
	}

	@Bean
	public MediaSourceFileFactory mediaSourceFileFactory() {
		return new DefaultMediaSourceFactory();
	}
}
