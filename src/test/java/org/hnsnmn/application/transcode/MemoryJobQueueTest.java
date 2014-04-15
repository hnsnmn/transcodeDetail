package org.hnsnmn.application.transcode;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오전 10:47
 * To change this template use File | Settings | File Templates.
 */
public class MemoryJobQueueTest {

	@Test
	public void shouldBeFifo() {
		MemoryJobQueue queue = new MemoryJobQueue();
		Long jobId1 = 1L;
		Long jobId2 = 2L;
		queue.add(jobId1);
		queue.add(jobId2);
		assertEquals(jobId1, queue.nextJobId());
		assertEquals(jobId2, queue.nextJobId());
	}
}
