package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.ResultCallback;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 9.
 * Time: 오전 10:12
 * To change this template use File | Settings | File Templates.
 */
public interface ResultCallbackFactory {
	ResultCallback create(String resultCallback);
}
