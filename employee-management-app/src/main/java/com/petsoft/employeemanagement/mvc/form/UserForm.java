package com.petsoft.employeemanagement.mvc.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 14.11.2019 10:00
 *
 * @author PetSoft
 */

public class UserForm {
    private Long id;

    @Size(min = 2, max = 255)
    private String name;

    @Min(18)
    private Integer age;

    @Min(1)
    private Double salary;

    private Long roleId;

    public UserForm() {
    }

    public UserForm(Long id, String name, Integer age, Double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserForm [id=" + id + ", name=" + name + ", age=" + age
                + ", salary=" + salary + "]";
    }
}
