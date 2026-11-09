package com.iiitv.campus.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {

    private Role currentRole = Role.STUDENT;
    private String userName = "Raghav A.";
    private String userDetails = "B.Tech (CSE) · Sem V";

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
        if (currentRole == Role.STUDENT) {
            this.userName = "Raghav A.";
            this.userDetails = "B.Tech (CSE) · Sem V";
        } else if (currentRole == Role.FACULTY) {
            this.userName = "Dr. A. Mehta";
            this.userDetails = "Dept. of Computer Science";
        } else if (currentRole == Role.ADMIN) {
            this.userName = "Administrative Officer";
            this.userDetails = "Academic Affairs Office";
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public boolean hasRole(Role role) {
        return this.currentRole == role;
    }
}
