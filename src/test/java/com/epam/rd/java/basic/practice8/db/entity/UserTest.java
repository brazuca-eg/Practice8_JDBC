package com.epam.rd.java.basic.practice8.db.entity;

import org.junit.Assert;
import org.junit.Test;



public class UserTest {

    @Test
    public void getLogin() {
        User user = new User();
        user.setLogin("ddd");
        Assert.assertEquals(user.getLogin(), "ddd");
    }

    @Test
    public void getId() {
        User user2 = new User();
        user2.setId(2);
        Assert.assertEquals(user2.getId(), 2);
    }

    @Test
    public void setId() {
        User user3 = new User();
        user3.setId(3);
        Assert.assertEquals(user3.getId(), 3);
    }

    @Test
    public void setLogin() {
        User user1 = new User();
        user1.setLogin("ddd");
        Assert.assertEquals(user1.getLogin(), "ddd");
    }

    @Test
    public void createUser() {


    }

    @Test
    public void testToString() {
        User user1 = new User();
        user1.setLogin("mmm");
        String s1 = user1.toString();
        String s2 = "User{login='mmm'}";
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setLogin("mmm");
        User user2 = new User();
        user2.setLogin("mmm");
        Assert.assertEquals(true, user1.equals(user2));
    }
}