package com.miaostar.auditor.handler.advice;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonsHandlerAdvice implements MessageSourceAware {

    private MessageSource source;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.source = messageSource;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public HttpEntity<?> handleUsernameNotFoundException(WebRequest request, UsernameNotFoundException ex) {
        Locale locale = request.getLocale();
        String message = source.getMessage("Username.notFound", new Object[]{ex.getMessage()}, "用户不存在", locale);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singleton(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }
}
