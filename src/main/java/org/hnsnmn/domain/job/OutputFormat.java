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
	private Container container;
	private VideoCodec videoCodec;
	private AudioCodec audioCodec;

	public OutputFormat(int width, int height, int bitrate, Container container, VideoCodec videoCodec, AudioCodec audioCodec) {
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.container = container;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
	}

	public OutputFormat(int width, int height, int bitrate, Container container) {
		this(width, height, bitrate, container, container.getDefaultVideoCodec(), container.getDefaultAudioCodec());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public VideoCodec getVideoCodec() {
		return videoCodec;
	}

	public AudioCodec getAudioCodec() {
		return audioCodec;
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getFileExtenstion() {
		return container.getFileExtenstion();
	}
}
