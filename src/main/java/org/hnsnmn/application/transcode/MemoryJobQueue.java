package org.hnsnmn.application.transcode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오전 10:47
 * To change this template use File | Settings | File Templates.
 */
public class MemoryJobQueue implements JobQueue {
	private BlockingQueue<Long> queue = new LinkedBlockingDeque<Long>();

	@Override
	public void add(Long jobId) {
		queue.add(jobId);
	}

	@Override
	public Long nextJobId() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Blocking Queue interrupted");
		}
	}
}
