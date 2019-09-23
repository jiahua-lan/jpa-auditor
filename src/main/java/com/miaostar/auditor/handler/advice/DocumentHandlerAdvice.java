package com.miaostar.auditor.handler.advice;

import com.miaostar.auditor.exception.DocumentNotFoundException;
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
public class DocumentHandlerAdvice implements MessageSourceAware {

    private MessageSource source;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.source = messageSource;
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public HttpEntity<?> handleDocumentNotFoundException(WebRequest request) {
        String message = source.getMessage("Document.notFound", null, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singleton(message));
    }
}
