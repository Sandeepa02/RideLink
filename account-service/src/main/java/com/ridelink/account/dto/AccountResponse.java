package com.ridelink.account.dto;

import com.ridelink.account.model.Account;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.Role;

public class AccountResponse {

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private AccountStatus status;

    public AccountResponse() {
    }

    public AccountResponse(String id, String fullName, String email,
                           String phone, Role role, AccountStatus status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getFullName(),
                account.getEmail(),
                account.getPhone(),
                account.getRole(),
                account.getStatus()
        );
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public AccountStatus getStatus() {
        return status;
    }
}