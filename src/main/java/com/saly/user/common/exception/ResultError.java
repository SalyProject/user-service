package com.saly.user.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.saly.user.common.exception.ErrorCode.INTERNAL_ERROR;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@Slf4j
@Getter
public class ResultError<T> {

    @JsonIgnore
    private HttpStatus httpStatus;
    @JsonIgnore
    private ApiErrorCode error;

    private int errorCode;
    private String title;
    private String message;

    private ResultError(ApiErrorCode errorCode) {
        this.error = errorCode;
        this.errorCode = errorCode.getCode();
        this.title = errorCode.getTitle();
        this.message = errorCode.getMessage();

        this.httpStatus = errorCode.getHttpStatus();
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static <H> ResultError<H> of(HasError errors, HttpServletRequest request) {
        ApiErrorCode errorCode = errors.getErrorCode() != null ? errors.getErrorCode() : INTERNAL_ERROR;

        return ofWith(errorCode, errors, request).get(true);
    }

    public static <H> ResultError<H> of(ApiErrorCode errorCode, Throwable ex, HttpServletRequest request) {
        return ofWith(errorCode, ex, request).get(true);
    }

    public static Builder ofWith(ApiErrorCode errorCode, Throwable ex, HttpServletRequest request) {
        return new Builder(errorCode, ex, request);
    }

    static <T> void trace(ResultError<T> result, HttpServletRequest request, Throwable ex) {
        ApiErrorCode errorCode = result.getError();

        final boolean printStackTrace = !(ex instanceof HasError) || ((HasError) ex).isPrintStackTrace();

        if (INTERNAL_ERROR.equals(errorCode) || printStackTrace) {
            printError(result, request, ex);
            return;
        }

        traceWarm(result, request, ex, false);
    }

    static <T> void traceWarm(ResultError<T> result, HttpServletRequest request, Throwable ex, boolean debugLevel) {
        Throwable cause = Optional.ofNullable(ex).orElseGet(() -> new RuntimeException(""));
        StackTraceElement stackTraceElement[] = cause.getStackTrace();

        for (StackTraceElement traceElement : stackTraceElement) {
            if (cause instanceof HasError) {
                HasError hasErrors = (HasError) cause;
                printWarn(result, request, traceElement, hasErrors.getDebugInfo(), hasErrors.getErrorMessage(), debugLevel);
            } else {
                printWarn(result, request, traceElement, "", cause.getMessage(), debugLevel);
            }

            return;
        }
    }

    private static <T> void printWarn(ResultError<T> result, HttpServletRequest request, StackTraceElement traceElement,
                                      String debugInfo, String message, boolean debugLevel) {

        String path = request != null && StringUtils.isNoneBlank(request.getServletPath()) ? request.getServletPath() : "/";
        debugInfo = Optional.ofNullable(trimToNull(debugInfo)).orElse("no-debug-info");

        StringBuilder traceMessage = new StringBuilder();

        Integer errorCode = result.getErrorCode();
        traceMessage.append("Error [").append(errorCode).append("] ").append(path).append(" | ").append(traceElement.getFileName()).append(":")
                .append(traceElement.getLineNumber()).append(" | ").append(debugInfo).append(" | ").append(message);

        if (debugLevel) {
            log.debug(traceMessage.toString());
        } else {
            log.warn(traceMessage.toString());
        }
    }

    private static <T> void printError(ResultError<T> result, HttpServletRequest request, Throwable ex) {
        String path = request != null &&StringUtils.isNoneBlank(request.getServletPath()) ? request.getServletPath() : "/";

        StringBuilder traceMessage = new StringBuilder();
        Integer errorCode = result.getErrorCode();

        traceMessage.append("Error [").append(errorCode).append("] ").append(path);

        log.error(traceMessage.toString(), ex);
    }

    public static class Builder {

        private ResultError result;

        private Throwable ex;
        private HttpServletRequest request;

        private Builder(ApiErrorCode errorCode, Throwable ex, HttpServletRequest request) {
            this.result = new ResultError(errorCode);
            this.ex = ex;
            this.request = request;
        }

        public <H> ResultError<H> get(boolean printLogs) {
            if (printLogs) {
                trace(result, request, ex);
            }

            return result;
        }
    }
}
