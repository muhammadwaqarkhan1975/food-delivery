package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sequence_number")
@Getter
@Setter
public class SequenceNumber
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @GenericGenerator(name = "order_sequence", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "web_id", unique = true, nullable = false)
    private Long webId;

    @Column(name = "name")
    private String name;
}
