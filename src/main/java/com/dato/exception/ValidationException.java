package com.dato.exception;


import com.dato.service.result.MyConstraintViolation;

import java.util.Set;

/**
 * 自定义异常
 */
public class ValidationException extends RuntimeException {




	public ValidationException(String message) {
		super( message );
	}

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause) {
		super( message, cause );
	}

	public ValidationException(Throwable cause) {
		super( cause );
	}

	public ValidationException(Set<MyConstraintViolation<Object>> result) {
		super(toString(result));
	}

	private static String toString(Set<MyConstraintViolation<Object>> result) {
		StringBuilder message = new StringBuilder();
		for (MyConstraintViolation<Object> violation : result) {
			message.append("校验的参数类是:").append(violation.getParamClass().getName()).append(" | ");
			message.append("校验的字段名是:").append(violation.getParamField().getName()).append(" | ");
			message.append("校验结果是:").append(violation.getExceptionMessage());
		}
		return message.toString();
	}


}
