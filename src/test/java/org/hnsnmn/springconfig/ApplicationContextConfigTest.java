package org.hnsnmn.springconfig;

import org.hnsnmn.application.transcode.DestinationStorageFactory;
import org.hnsnmn.application.transcode.MediaSourceFileFactory;
import org.hnsnmn.application.transcode.ResultCallbackFactory;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 4:36
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContextConfigTest {

	@Test
	public void getBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
		assertNotNull(context.getBean("dataSource", DataSource.class));
		assertNotNull(context.getBean("resultCallbackFactory", ResultCallbackFactory.class));
		assertNotNull(context.getBean("destinationStorageFactory", DestinationStorageFactory.class));
		assertNotNull(context.getBean("mediaSourceFileFactory", MediaSourceFileFactory.class));
	}
}
