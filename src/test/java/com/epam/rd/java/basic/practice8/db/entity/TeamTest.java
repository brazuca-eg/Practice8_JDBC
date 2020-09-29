package com.epam.rd.java.basic.practice8.db.entity;

import org.junit.Assert;
import org.junit.Test;



public class TeamTest {

    @Test
    public void getName() {
        Team team = new Team();
        team.setName("sss");
        Assert.assertEquals( team.getName(), "sss");
    }

    @Test
    public void getId() {
        Team team1 = new Team();
        team1.setId(2);
        Assert.assertEquals( team1.getId(), 2);
    }

    @Test
    public void setName() {
        Team team3 = new Team();
        team3.setName("bbb");
        Assert.assertEquals( team3.getName(), "bbb");
    }

    @Test
    public void setId() {
        Team team2 = new Team();
        team2.setId(5);
        Assert.assertEquals( team2.getId(), 5);
    }



    @Test
    public void testToString() {
        Team t1 = new Team();
        t1.setName("mmm");
        String s1 = t1.toString();
        Assert.assertEquals(s1, "Team{name='mmm'}");
    }

    @Test
    public void testEquals() {
        Team team1 = new Team();
        team1.setName("mmm");
        Team team2 = new Team();
        team2.setName("mmm");
        Assert.assertEquals(true, team2.equals(team1));
    }
}