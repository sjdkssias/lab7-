package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.database.UserService;

public class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register", "command to register");
    }

    @Override
    public Response execute(Request request) {
        if (request.args().size() < 2) {

            return new Response("Failed to register, please write LOGIN AND PASSWORD" + request.args());
        }
        UserService.getInstance().register(request.userRec());
        return new Response("Successful register. You can use console app");
    }
}
