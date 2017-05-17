/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.gamebuilder.controller;

import server.accountmanager.model.Host;
import server.accountmanager.model.Session;
import java.sql.*;
import server.accountmanager.model.User;
import server.accountmanager.model.AccountStatus;
import server.accountmanager.model.Account;
import server.accountmanager.model.Player;

/**
 *
 * @author David Ochoa
 */
public class SessionBuilder {

    //connection attributes

    private static final int port = 3306;
    private static final String serverPath = "localhost";
    private static final String dataBaseName = "dsdrisk";
    private final static String dataBaseUser = "dsdriskuser";
    private final static String dataBasePassword = "12345";

    private final static String driver = "com.mysql.jdbc.Driver";
    private final static String dataBase = "jdbc:mysql://" + serverPath + ":" + port + "/" + dataBaseName;
    private static Connection connection;
    private static Statement statement;
    
    //connection method
    public static void connectMySQL() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dataBase, dataBaseUser, dataBasePassword);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean createSession(User user, Session session) {
        Host host = new Host(user.account);
        // get actual session id and update in the class
        String querySelectId = "SELECT MAX(id) FROM session";
        try {
            ResultSet resultset = statement.executeQuery(querySelectId);
            boolean first = true;
            
            while (resultset.next()) {
                session.id = resultset.getInt("id") + 1;
                first = false;
            }
            if (first) {
                session.id = 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        //create session in database
        String queryInsertSession = "INSERT INTO session VALUES (\"" + session.map + "\", " + session.id + ", 'Start', \"" + session.type + "\")";
        //update host table
        String queryInsertHost = "INSERT INTO host VALUES (\"" + host.account.username + "\",  " + session.id + " )";
        //update player table with host, remember delete player rows when game fihish and make colors dinamic
        String queryInsertHostToPlayer = "INSERT INTO player VALUES(\"" + host.account.username
                + "\", 'YELLOW' , 'non-captured', 0, 0, 0,false,'HOST')";
        String queryUpdateHostStatus = "UPDATE user SET type = \"PLAYER\", status = \"playing\" WHERE username = " + user.account.username + "\";";
        
        //simulate another 3 players, this is for the first increment later the request handle this
        Player playerAux1 = new Player(Account.create(AccountStatus.ONLINE, "dochoau", "123", null));
        Player playerAux2 = new Player(Account.create(AccountStatus.ONLINE, "sareiza", "123", null));
        Player playerAux3 = new Player(Account.create(AccountStatus.ONLINE, "edalpin", "123", null));
        
        //insert the simulated  players into player table
        String query5 = "INSERT INTO player VALUES(\"" + playerAux1.account.username
                + "\", 'RED' , 'non-captured', 0, 0, 0,false,NULL)";
        String query6 = "INSERT INTO player VALUES(\"" + playerAux2.account.username
                + "\", 'BLUE' , 'non-captured', 0, 0, 0,false,NULL)";
        String query7 = "INSERT INTO player VALUES(\"" + playerAux3.account.username
                + "\", 'GREEN' , 'non-captured', 0, 0, 0,false,NULL)";

        try {
            // update tables: session, player,host, this the original update
            statement.executeUpdate(queryUpdateHostStatus);
            statement.executeUpdate(queryInsertSession);
            statement.executeUpdate(queryInsertHostToPlayer);
            statement.executeUpdate(queryInsertHost);
            
            session.numberOfPlayers++;
            //simulate new players, in the final increment this is done by the requestHandler class
            statement.executeUpdate(query5);
            session.numberOfPlayers++;
            statement.executeUpdate(query6);
            session.numberOfPlayers++;
            statement.executeUpdate(query7);
            session.numberOfPlayers++;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
