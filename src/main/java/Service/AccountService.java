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
        /*
         * The registration will be successful if and only if the username is not blank,
         * the password is at least 4 characters long, and an Account with that username
         * does not already exist.
         */

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
}
