package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BaseEntity  implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @GenericGenerator(name = "hibernateSequence", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "web_id", unique = true, nullable = false)
    private Long webId;

    @Version
    @Column(nullable = false)
    private Integer version;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "key")
    private String key;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    public void setKey() {

        this.key = StringUtils.hasText(this.key)? this.key: (this.key = UUID.randomUUID().toString());
    }
}
