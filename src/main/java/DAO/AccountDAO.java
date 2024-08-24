package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

/*  
    Handles Account related-database queries

    account_id int primary key auto_increment,
    username varchar(255) unique,
    password varchar(255)
    
*/
public class AccountDAO {

    /**
     * TODO: process new User registrations
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

            preparedStatement.setString(1, account.username);
            preparedStatement.setString(2, account.password);

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

}
