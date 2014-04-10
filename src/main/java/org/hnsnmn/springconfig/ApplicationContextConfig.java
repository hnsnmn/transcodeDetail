package org.hnsnmn.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 10.
 * Time: 오후 4:38
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@Import({ DomanConfig.class })
@ImportResource("classpath:spring/datasource.xml")
public class ApplicationContextConfig {
}
