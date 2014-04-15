package org.hnsnmn.infra.persistence;

import org.hnsnmn.domain.job.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 11.
 * Time: 오후 3:02
 * To change this template use File | Settings | File Templates.
 */
public class JobImpl extends Job {
	private JobDataDao jobDataDao;

	public JobImpl(JobDataDao jobDataDao, Long id,
				   State state,
				   MediaSourceFile mediaSourceFile,
				   DestinationStorage destinationStorage,
				   List<OutputFormat> outputFormats,
				   ResultCallback callback,
				   String errorMessage) {
		super(id, state,mediaSourceFile, destinationStorage, outputFormats,
				callback, errorMessage);
		this.jobDataDao = jobDataDao;
	}

	@Override
	protected void changeState(State newState) {
		super.changeState(newState);
		jobDataDao.updateState(getId(), newState);
	}

	@Override
	protected void exceptionOccurred(RuntimeException ex) {
		String exceptionMessage = ExceptionMessageUtil.getMessage(ex);
		jobDataDao.updateExceptionMessage(getId(), exceptionMessage);
		super.exceptionOccurred(ex);
	}
}
