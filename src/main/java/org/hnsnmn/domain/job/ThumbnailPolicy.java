package org.hnsnmn.domain.job;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오후 3:53
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class ThumbnailPolicy {

	@Column(name = "TP_OPTION")
	@Enumerated(EnumType.STRING)
	private Option option;

	@Column(name = "TP_WIDTH")
	private int width;

	@Column(name = "TP_HEIGHT")
	private int height;

	@Column(name = "TP_BEGINTIME")
	private int beginTimeInSeconds;

	@Column(name = "TP_ENDTIME")
	private int endTimeInSeconds;

	@Column(name = "TP_INTERVAL")
	private int interval;

	public static enum Option {
		FIRST;
	}

	public Option getOption() {
		return option;
	}
	public void setOption(Option option) {
		this.option = option;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getBeginTimeInSeconds() {
		return beginTimeInSeconds;
	}

	public int getEndTimeInSeconds() {
		return endTimeInSeconds;
	}

	public int getInterval() {
		return interval;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBeginTimeInSeconds(int beginTimeInSeconds) {
		this.beginTimeInSeconds = beginTimeInSeconds;
	}

	public void setEndTimeInSeconds(int endTimeInSeconds) {
		this.endTimeInSeconds = endTimeInSeconds;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
