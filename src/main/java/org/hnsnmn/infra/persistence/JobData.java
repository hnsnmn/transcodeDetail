package org.hnsnmn.infra.persistence;

import org.hnsnmn.domain.job.Job;
import org.hnsnmn.domain.job.OutputFormat;
import org.hnsnmn.domain.job.ThumbnailPolicy;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JOB")
public class JobData {

	@Id
	@Column(name = "JOB_ID")
	@TableGenerator(name = "JOB_ID_GEN", table = "ID_GENERATOR", pkColumnName = "ENTITY_NAME", pkColumnValue = "JOB", valueColumnName = "ID_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "JOB_ID_GEN")
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

	@Embedded
	private ThumbnailPolicy thumbnailPolicy;

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

	public ThumbnailPolicy getThumbnailPolicy() {
		return thumbnailPolicy;
	}

	public static class ExporterToJobData implements Job.Exporter<JobData> {

		private JobData jobData = new JobData();

		@Override
		public void addId(Long id) {
			jobData.id = id;
		}

		@Override
		public void addState(Job.State state) {
			jobData.state = state;
		}

		@Override
		public void addMediaSource(String url) {
			jobData.sourceUrl = url;
		}

		@Override
		public void addDestinationStorage(String url) {
			jobData.destinationUrl = url;
		}

		@Override
		public void addResultCallback(String url) {
			jobData.callbackUrl = url;
		}

		@Override
		public void addExceptionMessage(String exceptionMessage) {
			jobData.exceptionMessage = exceptionMessage;
		}

		@Override
		public void addOutputFormat(List<OutputFormat> outputFormats) {
			jobData.outputFormats = outputFormats;
		}

		@Override
		public void addThumbnailPolicy(ThumbnailPolicy thumbnailPolicy) {
			jobData.thumbnailPolicy = thumbnailPolicy;
		}
		@Override
		public JobData build() {
			return jobData;
		}
	}

}

