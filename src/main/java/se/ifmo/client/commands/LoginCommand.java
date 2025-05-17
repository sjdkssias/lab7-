package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.database.UserService;

import java.sql.SQLException;

public class LoginCommand extends Command{
    public LoginCommand() {
        super("login", "login command");
    }

    @Override
    public Response execute(Request request) {
        if (request.args().size() < 2){
            return new Response("Fail to login, because u didn't write login and password");
        }
        UserService.getInstance().login(request.args().get(0), request.args().get(1));
        return new Response("Successful login");

    }
}
