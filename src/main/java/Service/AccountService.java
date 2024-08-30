package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * Business rules and logic of Accounts
 */
public class AccountService {
    AccountDAO accountDAO;

    /**
     * No-args constructor, accountService instantiates a plain accountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when an accountDAO is provided.
     * This is used for testing accountService independently of AccountDAO.
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * adds a new registered account
     * 
     * @param account - an object representing a new account.
     * @return the newly added account if the operation was successful, including
     *         the account_id
     */
    public Account addAccount(Account account) {
        if (account.getUsername() != null && !account.getUsername().trim().isEmpty()) {
            String regex = ".{4,}";
            boolean pwAtLeastFour = account.getPassword().matches(regex);
            if (pwAtLeastFour) {
                if (!accountDAO.usernameExists(account.getUsername())) {
                    Account newAccount = accountDAO.insertAccount(account);

                    return newAccount;
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

    /**
     * Verifies whether username and password exist in the db
     * 
     * @param account - an object representing an existing account
     * @return the account if the operation was successful, including
     *         the account_id
     */
    public Account verifyUser(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("");
        }

        if (!accountDAO.usernameExists(username)) {
            throw new IllegalArgumentException("");
        }

        String storedPassword = accountDAO.getPassword(username, account);

        if (storedPassword == null || !storedPassword.equals(password)) {
            throw new IllegalArgumentException("");
        }

        return accountDAO.getAccount(account);
    }
}
