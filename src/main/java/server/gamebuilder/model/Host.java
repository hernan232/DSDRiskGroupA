package server.gamebuilder.model;

import server.accountmanager.model.Account;

/**
 * This class represents a host. A host is a user that creates a session. This
 * user can customize a new session in the Game Setup View.
 *
 * @author Hernán Darío Vanegas Madrigal
 */
public class Host extends Player {

    /**
     * This is the constructor of the class, this constructor is created in
     * order to convert a User into a specific type, but without change the
     * account attributes.
     *
     * @param account This reference represents the account associated to the
     * player that will be created.
     * @param color Represents the color of the host.
     */
    public Host(Account account, Color color) {
        super(account, color);
    }

    @Override
    public String toString() {
        return "Host{" + super.toString() + '}';
    }

}
