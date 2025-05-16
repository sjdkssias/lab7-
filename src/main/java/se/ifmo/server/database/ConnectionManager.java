package se.ifmo.server.database;


import se.ifmo.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    private static ConnectionManager instance;
    private static final String URL = "jdbc:postgresql:studs";
    private static final String USERNAME = "s472395";
    private static final String PASSWORD = "8JKvSCQ36P2fEQZH";

    private ConnectionManager(){
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Database connection OK");
            this.connection = getConnection();
        } catch (ClassNotFoundException | SQLException e){
            System.err.println("Connection error: " + e.getMessage());
            Server.logger.error(e);
            System.exit(1);//надо обработать
        }
    }

    public static ConnectionManager getInstance() {
        return instance != null ? instance : (instance = new ConnectionManager());
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public PreparedStatement prepare(String str, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(str);
        for (int i = 0; i < params.length; i++){
            stmt.setObject(i++, params[i]);
        }
        return stmt;
    }
}
