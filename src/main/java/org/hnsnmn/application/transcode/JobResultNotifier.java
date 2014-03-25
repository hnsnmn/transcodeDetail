package org.hnsnmn.application.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 3:27
 * To change this template use File | Settings | File Templates.
 */
public interface JobResultNotifier {
	void notifyToRequester(Long jobId);
}
