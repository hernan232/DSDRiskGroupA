package server.accountmanager.model;

/**
 * This class represents a User in the system. This is the basic class and it is in the top of the hierarchy of users of the system.
 * @author Hernán Darío Vanegas Madrigal
 */
public class User {
    public Account account;
    public AccountStatus status;

    /**
     * This is the constructor of the class. The constructor only receives an account associated with the user. The username in the account attribute is a unique identifier to the user. 
     * @param account It is the account associated with the user. It has The unique identifier to the user.
     */
    public User(Account account) {
        this.account = account;
    }  
}
