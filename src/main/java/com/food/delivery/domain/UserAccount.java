package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccount extends BaseEntity
{
    public enum UserAccountStatus {
        ACTIVE,
        INACTIVE;
    }

    public enum UserAccountType {
        CUSTOMER,RIDER,RESTAURANT,ADMIN;
    }

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "Last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private UserAccountStatus status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private UserAccountType type;

    @Column (name = "phone_number")
    private String phoneNumber;

    @Column (name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAddress> userAddresses = new ArrayList<>();

}
