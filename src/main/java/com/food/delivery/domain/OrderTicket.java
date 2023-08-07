package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Getter
@Setter
public class OrderTicket
{
    public enum OrderTicketStatus {ACTIVE,INACTIVE;}


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @GenericGenerator(name = "hibernateSequence", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "web_id", unique = true, nullable = false)
    private Long webId;

    @Column(name = "key")
    private String key;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_delivery_id")
    private FoodDelivery foodDelivery;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private OrderTicketStatus status;

    @PreUpdate
    public void setUpdateDate() {

        this.updateDate = LocalDateTime.now();

    }

    @PrePersist
    public void setKey() {

        this.key = StringUtils.hasText(this.key)? this.key: (this.key = UUID.randomUUID().toString());
        this.createdDate = LocalDateTime.now();
        setUpdateDate();
    }
}
