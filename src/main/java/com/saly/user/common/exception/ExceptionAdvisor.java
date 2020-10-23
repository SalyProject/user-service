package com.saly.user.common.exception;

import static java.util.Optional.ofNullable;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionAdvisor implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultError<Object>> handleAll(final HttpServletRequest request, final Throwable e) {
        final ResultError<Object> error = ResultError.of(ErrorCode.INTERNAL_ERROR, e, request);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(HasError.class)
    public ResponseEntity<ResultError<Object>> impossibleOperationException(final HttpServletRequest request, HasError e) {
        ResultError<Object> resultError = ResultError.of(e, request);

        return new ResponseEntity<>(resultError, resultError.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultError<Object>> invalidRequestBody(final HttpServletRequest request, HttpMessageNotReadableException e) {
        final ResultError<Object> resultError = ResultError.of(new SallyException(ErrorCode.EMPTY_REQUEST_BODY), request);

        return new ResponseEntity<>(resultError, resultError.getHttpStatus());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (Void.TYPE.equals(ofNullable(returnType.getMethod()).map(Method::getReturnType).orElse(null))) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
        } else if (body == null && request.getURI().getQuery() == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
        }

        return body;
    }

}

