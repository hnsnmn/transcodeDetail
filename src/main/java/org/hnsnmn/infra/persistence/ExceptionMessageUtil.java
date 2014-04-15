package org.hnsnmn.infra.persistence;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 4. 15.
 * Time: 오전 10:34
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionMessageUtil {

	public static String getMessage(Exception ex) {
		return ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage();
	}
}
