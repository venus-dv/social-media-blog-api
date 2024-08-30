package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

/*
    Handles Message related-database queries

    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)

*/
public class MessageDAO {

    /**
     * @param message - an object modelling a message
     * @return message object
     */
    public Message insertMessage(Message message) {
        // db connection
        Connection conn = ConnectionUtil.getConnection();
        try {
            // SQL logic
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys();
            if (pKeyResultSet.next()) {
                int generated_message_id = (int) pKeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @return all messages
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message;";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * @param messageId - id of the particular message
     * @return message
     */
    public Message getMessageById(int messageId) {
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * @param messageId - id of the message to delete
     * @return message
     */
    public Message deleteMessageById(int messageId) {
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }

            // Delete the message if it exists
            if (message != null) {
                String deleteSql = "DELETE FROM message WHERE message_id = ?;";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * 
     * @param messageId      - id of a particular message
     * @param newMessageText - new message to replace old one
     * @return message
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(sql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                // Update the message_text if it is valid
                if (newMessageText != null && !newMessageText.isBlank() && newMessageText.length() <= 255) {
                    String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
                    PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                    updateStatement.setString(1, newMessageText);
                    updateStatement.setInt(2, messageId);
                    updateStatement.executeUpdate();

                    // Retrieve the updated message
                    String getUpdatedMessageSql = "SELECT * FROM message WHERE message_id = ?;";
                    PreparedStatement getUpdatedMessageStatement = conn.prepareStatement(getUpdatedMessageSql);
                    getUpdatedMessageStatement.setInt(1, messageId);
                    ResultSet updatedRs = getUpdatedMessageStatement.executeQuery();

                    if (updatedRs.next()) {
                        message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * 
     * @param accountId - the account id of user
     * @return list of messages from particular account
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
