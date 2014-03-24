package org.hnsnmn.domain.transcode;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 24.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranscodingServiceTest {

	@Mock
	private MediaSourceCopier mediaSourceCopier;

	@Test
	public void transcodeSuccessfully() {
		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		Long jobId = new Long(1);
		File multimediaFile = copyMultimediaSourceToLocal(jobId);

		// 로컬에 복사된 파일을 변환처리 한다.
		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		// 결과를 통지
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		return mediaSourceCopier.copy(jobId);
	}
}
