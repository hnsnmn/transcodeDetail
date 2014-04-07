package org.hnsnmn.infra.ffmpeg;

import org.hnsnmn.domain.job.OutputFormat;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 7.
 * Time: 오전 9:57
 * To change this template use File | Settings | File Templates.
 */
public interface NamingRule {
	String createName(OutputFormat format);
}
