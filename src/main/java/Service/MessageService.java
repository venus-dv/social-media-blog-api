package Service;

import Model.Account;
import Model.Message;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;

/*
 *  Business rules and logic of Messages
 */
public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    /**
     * No-args constructor
     */
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an messageService when an messageDAO is provided.
     * This is used for testing messageService independently of MessageDAO.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * adds a new message
     * 
     * @param message - an object representing a new message.
     * @return the newly added message if the operation was successful, including
     *         the message_id
     */
    public Message addMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()
                || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("");
        }

        Account user = accountDAO.getAccountByID(message.getPosted_by());
        if (user == null) {
            throw new IllegalArgumentException("");
        }

        Message newMessage = messageDAO.insertMessage(message);

        return newMessage;
    }

    /**
     * @return all messages in the database
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * @param messageId - id of message we want to retrieve
     * @return message
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * 
     * @param messageId - id of the message to be deleted
     * @return message
     */
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    /**
     * 
     * @param messageId      - id of a particular message
     * @param newMessageText - new message to replace old
     * @return message
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        return messageDAO.updateMessageText(messageId, newMessageText);
    }

    /**
     * 
     * @param accountId - account id of user
     * @return list of messages from particular user
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
