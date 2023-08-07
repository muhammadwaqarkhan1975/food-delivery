package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.UserAccount;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserAccountModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                               LocalDateTime createdDate, String email, String firstName, String lastName,
                               UserAccount.UserAccountStatus status, String phoneNumber, List<UserAddressModel> userAddressModels) { }
