package org.hnsnmn.infra.repositories;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.OutputFormat;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JOB")
public class JobData {

	@Id
	@Column(name = "JOB_ID")
	private Long id;

	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private Job.State state;

	@Column(name = "SOURCE_URL")
	private String sourceUrl;

	@Column(name = "DESTINATION_URL")
	private String destinationUrl;

	@Column(name = "CALLBACK_URL")
	private String callbackUrl;

	@Column(name = "EXCEPTION_MESSAGE")
	private String exceptionMessage;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "JOB_OUTPUTFORMAT", joinColumns = { @JoinColumn(name = "JOB_ID") })
	@OrderColumn(name = "LIST_IDX")
	private List<OutputFormat> outputFormats;

	public Long getId() {
		return id;
	}

	public Job.State getState() {
		return state;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

}

