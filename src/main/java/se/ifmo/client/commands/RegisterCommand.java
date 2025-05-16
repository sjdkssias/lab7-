package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.database.UserService;

import java.sql.SQLException;

import static se.ifmo.server.crypt.Crypt.hashPassword;

public class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register", "command to register");
    }

    @Override
    public Response execute(Request request) {
        if (request.args().size() < 2) {

            return new Response("Failed to register, please write LOGIN AND PASSWORD" + request.args());
        }
        try {
            UserService.getInstance().register(request.args().get(0), hashPassword(request.args().get(1)));
            return new Response("Successful register. You can use console app");
        } catch (SQLException e) {
            return new Response("Registration error: " + e.getMessage());
        }
    }
}
