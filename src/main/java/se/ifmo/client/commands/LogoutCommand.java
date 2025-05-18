package se.ifmo.client.commands;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.server.database.UserService;

public class LogoutCommand extends Command{
    public LogoutCommand() {
        super("logout", "logout command");
    }

    @Override
    public Response execute(Request request) {
        boolean status = UserService.getInstance().logout(request.userRec());
        return new Response(status, status ? "Successful logout" : "Failed to logout");

    }
}
