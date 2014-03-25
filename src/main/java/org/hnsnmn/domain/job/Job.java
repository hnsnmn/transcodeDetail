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

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	public void changeState(State newState) {
		this.state = newState;
	}

	public enum State {
		COMPLETED, MEDIASOURCECOPYING

	}
}
