package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "food")
@Getter
@Setter
public class Food extends BaseEntity
{
    public enum FoodStatus {ACTIVE,INACTIVE;}

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private FoodStatus status;
}
