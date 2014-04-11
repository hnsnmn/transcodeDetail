package org.hnsnmn.infra.repositories;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.hnsnmn.domain.job.Job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 11.
 * Time: 오후 3:02
 * To change this template use File | Settings | File Templates.
 */
public class JobImpl extends Job {
	public JobImpl(JobData jobData, MediaSourceFileFactory mediaSourceFileFactory,
				   DestinationStorageFactory destinationStorageFactory,
				   ResultCallbackFactory resultCallbackFactory) {
		super(jobData.getId(), mediaSourceFileFactory.create(jobData.getSourceUrl()),
				destinationStorageFactory.create(jobData.getDestinationUrl()),
				jobData.getOutputFormats(),
				resultCallbackFactory.create(jobData.getCallbackUrl()));
	}
}
