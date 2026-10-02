package com.ridelink.account.dto;

import com.ridelink.account.model.AccountStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private AccountStatus status;

    public UpdateStatusRequest() {}

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
