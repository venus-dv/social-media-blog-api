package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController() {
        accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterUserHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postCreateMessageHandler));

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
     *              generates responses within Javalin
     * @throws JsonProcessingException - will be thrown if there is an issue
     *                                   converting JSON into an object
     */
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);

            Message newMessage = MessageService.addMessage(message);

            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }
}