package org.hnsnmn.application.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 9.
 * Time: 오후 5:16
 * To change this template use File | Settings | File Templates.
 */
public interface JobQueue {
	Long nextJobId();

	void add(Long jobId);

	@SuppressWarnings("serial")
	public class ClosedException extends RuntimeException {
	}
}
