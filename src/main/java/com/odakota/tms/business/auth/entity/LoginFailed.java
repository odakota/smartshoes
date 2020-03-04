package com.odakota.tms.business.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "login_failed_tbl")
public class LoginFailed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_customer")
    private boolean isCustomer;

    @Column(name = "created_date")
    private Date createDate;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }
}
