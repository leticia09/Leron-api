package com.leron.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@MappedSuperclass
public class GenericEntities {
    @Column(name= "deleted")
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "CREATED_IN")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdIn;

    @Column(name = "CHANGED_IN")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date changedIn;

    @Column(name = "status")
    private String status;

    @Column(name = "user_auth_id", nullable = false)
    private Long userAuthId;
}
