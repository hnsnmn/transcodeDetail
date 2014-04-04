package org.hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 4.
 * Time: 오후 5:21
 * To change this template use File | Settings | File Templates.
 */
public enum Container {
	MP4(VideoCodec.H264, AudioCodec.AAC, "mp4");

	private final VideoCodec defaultVideoCodec;
	private final AudioCodec defaultAudioCodec;
	private final String fileExtenstion;


	private Container(VideoCodec defaultVideoCodec, AudioCodec defaultAudioCodec, String fileExtenstion) {
		this.defaultVideoCodec = defaultVideoCodec;
		this.defaultAudioCodec = defaultAudioCodec;
		this.fileExtenstion = fileExtenstion;
	}

	public VideoCodec getDefaultVideoCodec() {
		return defaultVideoCodec;
	}

	public AudioCodec getDefaultAudioCodec() {
		return defaultAudioCodec;
	}

	public String getFileExtenstion() {
		return fileExtenstion;
	}
}
