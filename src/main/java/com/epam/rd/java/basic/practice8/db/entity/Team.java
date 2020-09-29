package com.epam.rd.java.basic.practice8.db.entity;

public class Team {
    private int id;
    private String name;

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }


    public static Team createTeam(String name) {
        Team team = new Team();
        team.setName(name);
        return team;
    }



    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
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
        return this.name== ((Team) o).name;
    }
}
