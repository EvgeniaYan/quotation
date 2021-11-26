package com.testtask.quotation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@RequiredArgsConstructor
@Table(name = "elvl")
public class Elvl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String isin;
}
