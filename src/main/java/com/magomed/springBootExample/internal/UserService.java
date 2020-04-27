package com.magomed.springBootExample.internal;

import com.magomed.springBootExample.api.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class UserService implements IUserService {

    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement prepStmt = con.prepareStatement(
                     sql)) {
            prepStmt.setInt(1, id);
            try (ResultSet rs = prepStmt.executeQuery()) {
                rs.next();
                long money = rs.getLong("MONEY");
                String country = rs.getString("COUNTRY");
                logger.info(sql + id + " executed");
                return new User(id, country, money);
            }
        }
    }

    @Override
    public void syncUser(User user) throws SQLException {
        String sql = "update users set money = ? where id = ? and country = ?";
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement prepStmt = con.prepareStatement(
                    sql)) {
                prepStmt.setLong(1, user.getMoney());
                prepStmt.setInt(2, user.getId());
                prepStmt.setString(3, user.getCountry());
                prepStmt.addBatch();
                executeBatches(prepStmt);
                con.commit();
                logger.info(sql + user.toString() + " executed");
            }
        }
    }


    @Override
    public void addUserActivities(List<ActivityTime> activityTime, int id) throws SQLException {
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            con.setAutoCommit(false);
            String sql = "insert into activity(user_id,stage,value) values (?,?,?)";
            try (PreparedStatement prepStmt = con.prepareStatement(
                    sql)) {
                for (ActivityTime a : activityTime) {
                    prepStmt.setInt(1, id);
                    prepStmt.setTimestamp(2, new Timestamp(a.getTimestamp()));
                    prepStmt.setInt(3, a.getActivity());
                    prepStmt.addBatch();
                }
                executeBatches(prepStmt);
                con.commit();
                logger.info(sql + " executed");
            }
        }
    }

    private void executeBatches(PreparedStatement prepStmt) throws SQLException {
        int[] numUpdates = prepStmt.executeBatch();
        for (int i = 0; i < numUpdates.length; i++) {
            if (numUpdates[i] == -2)
                logger.info("Execution " + i +
                        ": unknown number of rows updated");
            else
                logger.info("Execution " + i +
                        "successful: " + numUpdates[i] + " rows updated");
        }
    }
}
