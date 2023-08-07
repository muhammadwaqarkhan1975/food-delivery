package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address extends BaseEntity
{
    public enum AddressType {HOME,OFFICE, WORK;}

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "state")
    private String state;

    @Column(name = "street")
    private String street;

    @Column(name = "street2")
    private String street2;

    @Column (name = "address_type")
    @Enumerated(EnumType.ORDINAL)
    private AddressType addressType;

    @Column (name = "county")
    private String county;

    @Column (name = "latitude")
    private Double latitude;

    @Column (name = "longitude")
    private Double longitude;
}
