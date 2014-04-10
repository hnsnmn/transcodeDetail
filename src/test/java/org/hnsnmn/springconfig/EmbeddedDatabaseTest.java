package org.hnsnmn.springconfig;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 4:22
 * To change this template use File | Settings | File Templates.
 */
public class EmbeddedDatabaseTest {

	@Test
	public void dataSourceShouldExsits() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/datasource.xml");
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertNotNull(dataSource);
	}
}
