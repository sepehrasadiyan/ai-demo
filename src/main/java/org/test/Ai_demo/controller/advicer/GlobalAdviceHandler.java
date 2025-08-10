package org.test.Ai_demo.controller.advicer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.test.Ai_demo.exception.SummarizerException;
import org.test.Ai_demo.exception.UserAccessException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalAdviceHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalAdviceHandler.class);

    @ExceptionHandler(UserAccessException.class)
    public ProblemDetail handleUserAccess(UserAccessException ex, HttpServletRequest req) {
        var status = switch (ex.code()) {
            case "USER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "USER_INACTIVE",
                 "QUOTA_EXCEEDED" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.FORBIDDEN;
        };
        log.warn("User access error [{}]: {}", ex.code(), ex.getMessage());
        return problem(status, ex.code(), ex.getMessage(), req, null);
    }

    @ExceptionHandler(SummarizerException.class)
    public ProblemDetail handleProvider(SummarizerException ex, HttpServletRequest req) {
        var status = switch (ex.code()) {
            case "INVALID_API_KEY" -> HttpStatus.UNAUTHORIZED;
            case "RATE_LIMIT" -> HttpStatus.TOO_MANY_REQUESTS;
            case "INVALID_REQUEST" -> HttpStatus.BAD_REQUEST;
            case "PROVIDER_ERROR" -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.BAD_GATEWAY;
        };
        log.warn("error {}", ex.getLocalizedMessage());
        log.warn("Provider error [{}]: {}", ex.code(), ex.getMessage());
        return problem(status, ex.code(), ex.getMessage(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage(),
                        "rejectedValue", String.valueOf(fe.getRejectedValue())))
                .toList();

        var pd = problem(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", req, errors);
        return pd;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(this::toViolationMap)
                .collect(Collectors.toList());

        var pd = problem(HttpStatus.BAD_REQUEST, "CONSTRAINT_VIOLATION", "Constraint violation", req, errors);
        return pd;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER",
                "Missing parameter: " + ex.getParameterName(), req, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.debug("Malformed JSON: {}", ex.getMessage());
        return problem(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", "Malformed JSON request body", req, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", ex.getMessage(), req, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return problem(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", ex.getMessage(), req, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return problem(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE", ex.getMessage(), req, null);
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleOther(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error", req, null);
    }

    private ProblemDetail problem(HttpStatus status,
                                  String code,
                                  String message,
                                  HttpServletRequest req,
                                  Object errorsOrNull) {
        var pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("code", code);
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", req.getRequestURI());

        // Add a trace id (prefer MDC, else header, else none)
        var traceId = MDC.get("traceId");
        if (traceId == null || traceId.isBlank()) {
            traceId = req.getHeader("X-Request-Id");
        }
        if (traceId != null && !traceId.isBlank()) {
            pd.setProperty("traceId", traceId);
        }

        if (errorsOrNull != null) {
            pd.setProperty("errors", errorsOrNull);
        }
        return pd;
    }

    private Map<String, String> toViolationMap(ConstraintViolation<?> v) {
        return Map.of(
                "path", String.valueOf(v.getPropertyPath()),
                "message", v.getMessage(),
                "invalidValue", String.valueOf(v.getInvalidValue())
        );
    }
}
