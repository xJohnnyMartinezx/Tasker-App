package com.codeup.tasker.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Calendar;


@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class AuditableBase{

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
//    @Temporal inserts the correct current time and date in DB. (using jakarta.persistence)
//    Other Date/Time classes were inserting the incorrect local time.
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdDate;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar modifiedDate;



}
