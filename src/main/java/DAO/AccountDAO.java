package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

/**
 * Handles Account related-database queries
 */
public class AccountDAO {

    /**
     * @param account - an object modelling an Account
     * @return account object
     */
    public Account insertAccount(Account account) {
        // db connection
        Connection conn = ConnectionUtil.getConnection();
        try {
            // SQL logic
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys();
            if (pKeyResultSet.next()) {
                int generated_account_id = (int) pKeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve a certain account by username
     * 
     * @param username - account username to check for
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?;";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking credentials: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves a certain account
     * 
     * @param account - an object modelling an account
     * @return account object
     */
    public Account getAccount(Account account) {
        // db connection
        Connection conn = ConnectionUtil.getConnection();
        try {
            // SQL logic
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());

            // debug statement
            //System.out.println("\n\n Account: " + account.getUsername() + ", " + account.getPassword() +"\n\n");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account acct = new Account(rs.getInt("account_id"), rs.getString("username"), 
                rs.getString("password"));
                return acct;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a certain account
     * 
     * @param account - an object modelling an account
     * @return account object
     */
    public String getPassword(String password, Account account) {
        // db connection
        Connection conn = ConnectionUtil.getConnection();
        try {
            // SQL logic
            String sql = "SELECT password FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
