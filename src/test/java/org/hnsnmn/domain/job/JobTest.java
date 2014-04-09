package org.hnsnmn.domain.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 8.
 * Time: 오후 6:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobTest {
	@Mock
	private MediaSourceFile mediaSource;
	@Mock
	private File sourceFile;
	@Mock
	private Transcoder transcoder;
	@Mock
	private List<OutputFormat> outputFormats;
	@Mock
	private List<File> multimediafiles;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private List<File> thumbnails;
	@Mock
	private ResultCallback callback;
	@Mock
	private DestinationStorage destination;

	@Test
	public void successfully() {
		long jobId = 1L;
		when(mediaSource.getSourceFile()).thenReturn(sourceFile);
		when(transcoder.transcode(sourceFile, outputFormats)).thenReturn(
				multimediafiles);
		when(thumbnailExtractor.extract(sourceFile, jobId)).thenReturn(
				thumbnails);

		Job job = new Job(jobId, mediaSource, destination, outputFormats, callback);
		job.transcode(transcoder, thumbnailExtractor);

		verify(mediaSource, only()).getSourceFile();
		verify(destination, only()).save(multimediafiles, thumbnails);
		verify(callback, only()).notifySuccessResult(jobId);

	}

	@Test
	public void failGetSourceFile() {
		long jobId = 1L;
		RuntimeException exception = new RuntimeException("exception");
		when(mediaSource.getSourceFile()).thenThrow(exception);

		Job job = new Job(jobId, mediaSource, destination, outputFormats,
				callback);

		try {
			job.transcode(transcoder, thumbnailExtractor);
			fail("발생해야함.");
		} catch (Exception e) {
		}

		verify(mediaSource, only()).getSourceFile();
		verify(destination, never()).save(multimediafiles, thumbnails);
		verify(callback, only()).notifyFailResult(jobId, Job.State.MEDIASOURCECOPYING, "exception");
	}
}
