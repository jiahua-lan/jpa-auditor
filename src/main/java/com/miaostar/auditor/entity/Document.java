package com.miaostar.auditor.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Document extends AbstractPersistable<Long> {

    @NotEmpty(message = "{document.title.NotEmpty}")
    private String title;
    @NotEmpty(message = "{document.content.NotEmpty}")
    private String content;

    @CreatedBy
    @ManyToOne
    private User author;

    @CreatedDate
    private LocalDateTime createTime;
}
