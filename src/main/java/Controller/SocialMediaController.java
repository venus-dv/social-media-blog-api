package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterUserHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

        return app;
    }

    /**
     * Handler for new user registration
     * 
     * @param ctx - the context object handles information HTTP requests
     *            and generates responses within Javalin
     * @throws JsonProcessingException - will be thrown if there is an issue
     *                                 converting JSON into an object
     */
    private void postRegisterUserHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);

            Account newAccount = accountService.addAccount(account);

            // Debug statement
            // System.out.println("\n\nNew Account: " + newAccount + "\n\n");

            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);

        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    /**
     * Handles user login
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin
     * @throws JsonProcessingException - will be thrown if there is an issue
     *                                 converting JSON into an object
     */
    private void postUserLoginHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);

            Account verifiedUser = accountService.verifyUser(account);

            ctx.json(mapper.writeValueAsString(verifiedUser));
            ctx.status(200);

        } catch (IllegalArgumentException e) {
            ctx.status(401).result(e.getMessage());
        }
    }

    /**
     * Handles creating a new message
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin
     * @throws JsonProcessingException - will be thrown if there is an issue
     *                                 converting JSON into an object
     */
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);

            Message newMessage = messageService.addMessage(message);

            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    /**
     * Handler to retrieve all messages
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin.
     */
    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(message);

        } else {
            ctx.json("");
        }
        ctx.status(200);
    }

    /**
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin.
     */
    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.json("");
        }
        ctx.status(200);
    }

    /**
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin.
     */
    private void updateMessageTextHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String requestBody = ctx.body();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(requestBody);
        } catch (JsonProcessingException e) {
            ctx.status(400).result("");
            return;
        }

        String newMessageText = jsonNode.path("message_text").asText();

        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            ctx.status(400).result("");
            return;
        }

        // Proceed with updating the message
        Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);

        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400).result("");
        }
    }

    /**
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates responses within Javalin.
     */
    private void getMessagesByAccountIdHandler(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(userId);

        ctx.json(messages);
        ctx.status(200);
    }
}