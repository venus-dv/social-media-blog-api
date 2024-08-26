package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
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

        app.get("example-endpoint", this::exampleHandler);

        return app;
    }

    /**
     * Handler for new user registration
     * 
     * @param ctx - the context object handles information HTTP requests and
     *            generates
     *            responses within Javalin
     * @throws JsonProcessingException - will be thrown if there is an issue
     *                                 converting JSON into
     *                                 an object
     */
    private void postRegisterUserHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);

            System.out.println(account.toString() + "\n\n");

            Account newAccount = accountService.addAccount(account);

            // Debug statement
            System.out.println("\n\nNew Account: " + newAccount + "\n\n");

            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);

        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

}