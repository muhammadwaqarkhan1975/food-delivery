package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant extends BaseEntity
{
    public enum RestaurantStatus {ACTIVE,INACTIVE;}

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private RestaurantStatus status;

    @Column(name = "logo")
    private String logo;

    @Column(name = "code")
    private String code;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserAccount owner;

}
