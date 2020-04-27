package com.magomed.application.internal.models;

import java.util.Objects;

public class User {
    private int id;
    private long money;
    private String country;

    public User(int id, String country, long money) {
        this.id = id;
        this.country = country;
        this.money = money;
    }

    public User() {
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int uuid) {
        this.id = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(country, user.country) &&
                Objects.equals(money, user.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", money=" + money +
                ", country='" + country + '\'' +
                '}';
    }
}
