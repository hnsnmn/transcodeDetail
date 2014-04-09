package org.hnsnmn.application.transcode;

import org.hnsnmn.domain.job.OutputFormat;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 4. 8.
* Time: 오전 10:51
* To change this template use File | Settings | File Templates.
*/
public class AddJobRequest {
	private String mediaSource;
	private String destinationStorage;
	private String resultCallback;
	private List<OutputFormat> outputFormats;

	public void setMediaSource(String mediaSource) {
		this.mediaSource = mediaSource;
	}

	public void setDestinationStorage(String destinationStorage) {
		this.destinationStorage = destinationStorage;
	}

	public void setOutputFormats(List<OutputFormat> outputFormats) {
		this.outputFormats = outputFormats;
	}

	public void setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
	}

	public String getMediaSource() {
		return mediaSource;
	}

	public String getDestinationStorage() {
		return destinationStorage;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

	public String getResultCallback() {
		return resultCallback;
	}

	@Override
	public String toString() {
		return "AddJobRequest [mediaSource=" + mediaSource
				+ ", destinationStorage=" + destinationStorage
				+ ", outputFormats=" + outputFormats
				+ ", resultCallback=" + resultCallback + "]";
	}
}
