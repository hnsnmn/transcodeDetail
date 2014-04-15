package org.hnsnmn.springconfig;

import org.hnsnmn.domain.job.ThumbnailExtractor;
import org.hnsnmn.domain.job.Transcoder;
import org.hnsnmn.infra.ffmpeg.FfmpegTranscoder;
import org.hnsnmn.infra.ffmpeg.NamingRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 6:57
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class FfmpegConfig {

	@Bean
	public Transcoder transcoder() {
		return new FfmpegTranscoder(NamingRule.DEFAULT);
	}

	@Bean
	public ThumbnailExtractor thumbnailExtractor() {
		return new FfmpegThumbnailExtractor();
	}
}
