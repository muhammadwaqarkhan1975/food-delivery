package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rider_location")
@Getter
@Setter
public class RiderLocation
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @GenericGenerator(name = "hibernateSequence", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "web_id", unique = true, nullable = false)
    private Long webId;

    @Column(name = "key")
    private String key;

    @PrePersist
    public void setKey() {

        this.key = StringUtils.hasText(this.key)? this.key: UUID.randomUUID().toString();
    }

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private UserAccount rider;

    @Column (name = "latitude")
    private Double latitude;

    @Column (name = "longitude")
    private Double longitude;

}
