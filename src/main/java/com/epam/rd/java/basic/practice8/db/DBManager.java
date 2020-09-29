package com.epam.rd.java.basic.practice8.db;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DBManager {
    public static final Logger logger = Logger.getLogger( DBManager.class.getName());
    private static DBManager dbManager;
    private static final String EXC = "Exception message";
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_INSERT_USER_TO_TEAM = "INSERT INTO users_teams VALUES (?, ?)";
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String SQL_FIND_TEAM_BY_NAME = "SELECT * FROM teams WHERE name=?";
    private static final String SQL_DELETE_TEAM = "DELETE FROM teams WHERE name=?";
    private static final String SQL_UPDATE_TEAM = "UPDATE teams SET name=? WHERE id=?";
    private static final String SQL_INSERT_USER = "INSERT INTO users VALUES (DEFAULT ,?)";
    private static final String SQL_INSERT_TEAM = "INSERT INTO teams VALUES (DEFAULT ,?)";
    private static final String SQL_FIND_TEAMS_BY_USER_ID = "SELECT g.id, g.name FROM users_teams ug\n" +
            "JOIN users u ON ug.user_id = u.id\n" +
            "JOIN teams g ON ug.team_id = g.id\n" +
            "WHERE u.id = ?";
    private static final String SQL_FIND_ALL_TEAMS = "SELECT * FROM teams";
    private static final Lock CONNECTION_LOCK = new ReentrantLock();
    String url;


    public DBManager() {
        try {
            getUrl();
        } catch (IOException e) {
            logger.log(Level.SEVERE,EXC,e);
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,EXC,e);
            }
        }
    }

    private static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,EXC,e);
            }
        }
    }


    private void setAutocommit() {
        try {
            getConnection(url).setAutoCommit(true);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,EXC,e);
        }
    }

    private void getUrl() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("app.properties");
        properties.load(fileInputStream);
        url = properties.getProperty("connection.url");
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }


    public boolean insertUser(User user) throws SQLException {
        ResultSet ide = null;
        PreparedStatement preparedStatement = null;

        try {
            CONNECTION_LOCK.lock();
            preparedStatement = getConnection(url).prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            if (preparedStatement.executeUpdate() != 1) {
                return false;
            }
             ide = preparedStatement.getGeneratedKeys();
            if (ide.next()) {
                int idField = ide.getInt(1);
                user.setId(idField);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return false;
        } finally {
            close(preparedStatement);
            close(ide);
            CONNECTION_LOCK.unlock();
        }
        return true;
    }

    public boolean insertTeam(Team team) {
        PreparedStatement pr = null;
        ResultSet res = null;
        try {
            CONNECTION_LOCK.lock();
            pr = getConnection(url).prepareStatement(SQL_INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, team.getName());
            if (pr.executeUpdate() != 1) {
                return false;
            }
            res = pr.getGeneratedKeys();
            if (res.next()) {
                int idField = res.getInt(1);
                team.setId(idField);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return false;
        } finally {
            close(res);
            close(pr);
            CONNECTION_LOCK.unlock();
        }
        return true;
    }

    public List<User> findAllUsers() {
        Statement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).createStatement();
            rs = ps.executeQuery(SQL_FIND_ALL_USERS);
            while (rs.next()) {
                User user = new User();
                users.add(user);
                user.setLogin(rs.getString(2));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return Collections.emptyList();
        } finally {
            close(rs);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return users;
    }

    public List<Team> findAllTeams() {
        ResultSet rs = null;
        Statement ps = null;
        List<Team> groups = new ArrayList<>();
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).createStatement();
            rs = ps.executeQuery(SQL_FIND_ALL_TEAMS);
            while (rs.next()) {
                Team group = new Team();
                groups.add(group);
                group.setName(rs.getString(2));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return Collections.emptyList();
        } finally {
            close(rs);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return groups;
    }

    public User getUser(String login) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).prepareStatement(SQL_FIND_USER_BY_LOGIN);
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,EXC,e);
        } finally {
            close(rs);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return user;
    }


    public Team getTeam(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Team team = null;
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).prepareStatement(SQL_FIND_TEAM_BY_NAME);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,EXC,e);
        } finally {
            close(rs);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return team;
    }

    public boolean setTeamsForUser(User user, Team... teams) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            CONNECTION_LOCK.lock();
            getConnection(url).setAutoCommit(false);
            ps = getConnection(url).prepareStatement(SQL_INSERT_USER_TO_TEAM);
            for (Team team : teams) {
                ps.setInt(1, user.getId());
                ps.setInt(2, team.getId());
                ps.addBatch();
            }
            int[] usersGroups = ps.executeBatch();
            for (int i : usersGroups) {
                if (i != 1) {
                    return false;
                }
            }
            getConnection(url).commit();
            return true;
        } catch (SQLException ex) {
            try {
                getConnection(url).rollback();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,EXC,e);
            }
            System.out.println("Can't set groups");
        } finally {
            close(rs);
            close(ps);
            setAutocommit();
            CONNECTION_LOCK.unlock();
        }
        return false;
    }

    public List<Team> getUserTeams(User user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Team> groups = new ArrayList<>();
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).prepareStatement(SQL_FIND_TEAMS_BY_USER_ID);
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                Team team = new Team();
                groups.add(team);
                team.setId(rs.getInt(1));
                team.setName(rs.getString(2));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return Collections.emptyList();
        } finally {
            close(rs);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return groups;
    }

    public boolean deleteTeam(Team team) {
        PreparedStatement ps = null;
        ResultSet id = null;
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).prepareStatement(SQL_DELETE_TEAM);
            ps.setString(1, team.getName());
            if (ps.executeUpdate() != 1) {
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return false;
        } finally {
            close(id);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return true;
    }

    public boolean updateTeam(Team team) {
        PreparedStatement ps = null;
        ResultSet id = null;
        try {
            CONNECTION_LOCK.lock();
            ps = getConnection(url).prepareStatement(SQL_UPDATE_TEAM);
            ps.setString(1, team.getName());
            ps.setInt(2, team.getId());
            if (ps.executeUpdate() != 1) {
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,EXC,e);
            return false;
        } finally {
            close(id);
            close(ps);
            CONNECTION_LOCK.unlock();
        }
        return true;
    }
}
