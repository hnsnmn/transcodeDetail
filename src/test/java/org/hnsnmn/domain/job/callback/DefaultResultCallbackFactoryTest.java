package org.hnsnmn.domain.job.callback;

import org.hnsnmn.domain.job.ResultCallback;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오전 10:25
 * To change this template use File | Settings | File Templates.
 */
public class DefaultResultCallbackFactoryTest {
	private DefaultResultCallbackFactory callbackFactory = new DefaultResultCallbackFactory();

	@Test
	public void shouldCreateHttpResultCallbackWhenUrlIsHttp() {
		ResultCallback callback = callbackFactory.create("http://localhost:8080/transcode/callback");
		assertTrue(callback instanceof HttpResultCallback);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenUrlIsNotSupported() {
		callbackFactory.create("xxxx://localhost:8080/transcode/callback");
	}

}
