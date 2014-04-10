package org.hnsnmn.domain.job.callback;

import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.ResultCallback;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 10.
* Time: 오전 10:28
* To change this template use File | Settings | File Templates.
*/
class DefaultResultCallbackFactory implements ResultCallbackFactory {
	public ResultCallback create(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return new HttpResultCallback(url);
		}
		throw new IllegalArgumentException();
	}
}
