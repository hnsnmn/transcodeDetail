package org.hnsnmn.infra.ffmpeg;

import com.xuggle.xuggler.ICodec;
import org.hnsnmn.domain.job.AudioCodec;
import org.hnsnmn.domain.job.VideoCodec;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 4.
 * Time: 오후 3:02
 * To change this template use File | Settings | File Templates.
 */
public class CodecValueConverter {
	public static ICodec.ID fromDomain(VideoCodec videoCodec) {
		if (videoCodec == null) {
			return null;
		}
		switch (videoCodec) {
			case H264:
				return ICodec.ID.CODEC_ID_H264;
			case MPEG4:
				return ICodec.ID.CODEC_ID_MPEG4;
		}
		return null;
	}

	public static VideoCodec toDomainVideoCodec(ICodec.ID codecId) {
		if (codecId == null) {
			return null;
		}
		switch (codecId) {
			case CODEC_ID_H264:
				return VideoCodec.H264;
			case CODEC_ID_MPEG4:
				return VideoCodec.MPEG4;
		}
		return null;
	}

	public static ICodec.ID fromDomain(AudioCodec audioCodec) {
		if (audioCodec == null) {
			return null;
		}
		switch (audioCodec) {
			case AAC:
				return ICodec.ID.CODEC_ID_AAC;
			case MP3:
				return ICodec.ID.CODEC_ID_MP3;
		}
		return  null;
	}

	public static AudioCodec toDomainAudioCodec(ICodec.ID codecId) {
		if (codecId == null) {
			return null;
		}
		switch (codecId) {
			case CODEC_ID_AAC:
				return AudioCodec.AAC;
			case CODEC_ID_MP3:
				return AudioCodec.MP3;
		}
		return null;
	}
}
