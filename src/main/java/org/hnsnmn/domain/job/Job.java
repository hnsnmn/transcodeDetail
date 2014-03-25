package org.hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 5:59
 * To change this template use File | Settings | File Templates.
 */
public class Job {
	private State state;
	private RuntimeException occurredException;

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	public void changeState(State newState) {
		this.state = newState;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinished() {
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public Exception getOccurredException() {
		return occurredException;
	}


	public enum State {
		COMPLETED, MEDIASOURCECOPYING

	}
}
