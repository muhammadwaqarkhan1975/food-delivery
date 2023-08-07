package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer extends BaseEntity
{
    public enum CustomerType {
        VIP(1), LOYAL(2), NEW(3);

        final Integer priority;
        CustomerType(Integer priority)
        {
            this.priority = priority;
        }

        public Integer getPriority()
        {return this.priority;}

        public static CustomerType fromString(String name)
        {

            return Stream.of(CustomerType.values())
                    .filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(CustomerType.NEW);
        }
    }
    public enum CustomerStatus {
        ACTIVE,
        INACTIVE;
    }
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private CustomerStatus status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CustomerType type;

    @Column (name = "phone_number")
    private String phoneNumber;

    @Column (name = "avatar")
    private String avatar;
}
