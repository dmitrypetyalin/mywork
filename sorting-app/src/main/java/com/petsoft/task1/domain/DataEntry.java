package com.petsoft.task1.domain;

/**
 * 12.10.2019 10:37
 *
 * @author PetSoft
 */

public class DataEntry {
    private Long id;
    private Integer number;

    public DataEntry() {
    }

    public DataEntry(Integer number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "DataEntry{" +
                "id=" + id +
                ", number=" + number +
                '}';
    }
}
