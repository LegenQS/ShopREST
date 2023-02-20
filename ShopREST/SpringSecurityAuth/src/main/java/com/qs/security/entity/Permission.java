package com.qs.security.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name="Permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id", unique = true, nullable = false)
    private Integer perm_id;

    @Column(name = "role")
    private String role;
}
