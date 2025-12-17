package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<TenantEntity> users;

    public RoleEntity(String name) {
        this.name = name;
    }
}