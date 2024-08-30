package Service;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;

/*
 *  Business rules and logic of Messages
 */
public class MessageService {
    MessageDAO messageDAO;

    /**
     * No-args constructor, messageService instantiates a plain messageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for an messageService when an messageDAO is provided.
     * This is used for testing messageService independently of MessageDAO.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * adds a new message
     * 
     * @param message - an object representing a new message.
     * @return the newly added message if the operation was successful, including
     *         the message_id
     */
    public Message addMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            throw new IllegalArgumentException("");
        }

        String regex = "^.{0,255}$";
        boolean messageLength = message.getMessage_text().matches(regex);
        if (!messageLength) {
            throw new IllegalArgumentException("");
        }

        int posted_by = message.getPosted_by();
        Account account = accountDAO.getAccountByID(message.getPosted_by());

        if (posted_by != Account.getAccountByID(message.getPosted_by()) ) {
            throw new IllegalArgumentException("");
        }

        if (message.getMessage_text() != null && !message.getMessage_text().trim().isEmpty()) {
            
            if (messageLength) {
                if (!messageDAO.usernameExists(message.getUsername())) {
                    Message newMessage = messageDAO.insertMessage(message);

                    return newMessage;
                } else {
                    throw new IllegalArgumentException("");
                }
            } else {
                throw new IllegalArgumentException("");
            }

        } else {
            throw new IllegalArgumentException("");
        }
    }
}
