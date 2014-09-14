package com.vtapadia.fifa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ef_roles")
public class Roles implements Serializable {
    public enum DefinedRole {
        USER,
        ADMIN;
    }

    @Column(name="user_id")
    @Id
    private String userId;

    @Enumerated(EnumType.STRING)
    @Id
    private DefinedRole role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DefinedRole getRole() {
        return role;
    }

    public void setRole(DefinedRole role) {
        this.role = role;
    }
}
