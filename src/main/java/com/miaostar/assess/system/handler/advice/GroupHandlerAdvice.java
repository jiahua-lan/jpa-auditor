package com.miaostar.assess.system.handler.advice;

import com.miaostar.assess.system.exception.GroupNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

@RestControllerAdvice
public class GroupHandlerAdvice implements MessageSourceAware {
    private MessageSource source;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.source = messageSource;
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public HttpEntity<?> handleUserNotFoundException(WebRequest request) {
        String message = source.getMessage("Group.notFound", null, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singleton(message));
    }
}