package com.epam.rd.java.basic.practice8.db.entity;

public class User {
    private int id;
    private String login;

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.login == ((User) o).login;
    }
}
