package com.petsoft.employeemanagement.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * 21.02.2020 11:51
 *
 * @author PetSoft
 */

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "duty", nullable = false)
    private String duty;

    @OneToMany(fetch = LAZY, mappedBy = "role")
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(String duty) {
        this.duty = duty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", duty='" + duty + '\'' +
                '}';
    }
}

