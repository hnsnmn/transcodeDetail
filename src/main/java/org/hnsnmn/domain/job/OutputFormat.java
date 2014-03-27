package org.hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 27.
 * Time: 오후 2:05
 * To change this template use File | Settings | File Templates.
 */
public class OutputFormat {
	private int width;
	private int height;
	private int bitrate;
	private String videoFormat;
	private String audioFormat;

	public OutputFormat(int width, int height, int bitrate, String videoFormat, String audioFormat) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.videoFormat = videoFormat;
		this.audioFormat = audioFormat;
	}
}
