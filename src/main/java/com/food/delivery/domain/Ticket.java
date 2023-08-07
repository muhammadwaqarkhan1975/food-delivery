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
public class Ticket
{
    public enum TicketStatus {ACTIVE,INACTIVE;}
    public enum TicketReason {
        REASON_DELIVERY_TIME_EXCEED("Alert: Estimated time of delivery has been exceeded and order is still not in deliver status"),
        REASON_DELIVERY_TIME_EXCEED_VIP_CUSTOMER("High Alert: VIP customer's delivery time has been exceeded and order is still not in deliver status");

        private final String reason;
        TicketReason(String reason)
        {
            this.reason =reason;
        }

        public String getReason() {
            return reason;
        }
    }

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
    private TicketStatus status;

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
