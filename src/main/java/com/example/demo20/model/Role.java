package com.example.demo20.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    // Getter tay để tránh lỗi method reference
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter nếu cần
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
