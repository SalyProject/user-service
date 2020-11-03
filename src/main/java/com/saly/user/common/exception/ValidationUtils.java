package com.saly.user.common.exception;

import java.util.function.Predicate;

public class ValidationUtils {

    public static <T> void validateParam(T param, String message, Predicate<T> predicate) throws BadRequestException {
        if (!predicate.test(param)) {
            throw new BadRequestException(message);
        }
    }

//    public static <T> void validateParam(T param, ValidationMessage errorCode, Predicate<T> predicate, String debugInfo) throws BadRequestException {
//        if (!predicate.test(param)) {
//            throw new BadRequestException(errorCode, debugInfo);
//        }
//    }
//
//    public static <T, H> void validateParam(T param1, H param2, ValidationMessage errorCode, Tuple2Predicate<T, H> predicate) throws BadRequestException {
//        validateParam(param1, param2, errorCode, predicate, null);
//    }
//
//    public static <T, H> void validateParam(T param1, H param2, ValidationMessage errorCode, Tuple2Predicate<T, H> predicate, String debugInfo) throws BadRequestException {
//        if (!predicate.test(param1, param2)) {
//            throw new BadRequestException(errorCode, debugInfo);
//        }
//    }

    public interface Tuple2Predicate<T, H> {
        boolean test(T t, H h);
    }

}
