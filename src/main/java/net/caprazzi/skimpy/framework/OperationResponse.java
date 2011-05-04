package net.caprazzi.skimpy.framework;

import java.io.IOException;
import java.io.OutputStream;

import net.caprazzi.skimpy.ErrorCode;

/**
 * OperationResponse are typically returned by service methods and act as a bridge between the
 * service and http layers. A typical implementation will have methods specific to the service just called;
 * for example a LoginOperationResponse may have a getSession() method.
 * 
 * The implementation of writeBody should write a representation of the the reponse in a format
 * compatible with httpContentType.
 * 
 * @author matteo.caprari@gmail.com
 */
public interface OperationResponse {

	boolean isSuccess();
	
	ErrorCode getErrorCode();

	int getHttpStatus();

	String getMessage();

	String getHttpContentType();

	void writeBody(OutputStream os) throws IOException;
}
