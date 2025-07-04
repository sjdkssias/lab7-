package se.ifmo.server.database;


import se.ifmo.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    private static ConnectionManager instance;
    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private ConnectionManager(){
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = getConnection();
            System.out.println("Database connection OK");
        } catch (ClassNotFoundException | SQLException e){
            Server.logger.error(e);
            System.exit(1);
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
