package se.ifmo.client;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.UserRec;
import se.ifmo.client.commands.LoginCommand;
import se.ifmo.client.commands.RegisterCommand;
import se.ifmo.client.commands.ShowCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClientManager {
    private final Client client;
    private UserRec currentUser = null;
    private static ClientManager instance;
    public ClientManager() {
        this.client = Client.getInstance();
    }

    public static synchronized ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    public Response login(String login, String password) throws IOException {
        UserRec curUser = new UserRec(login, password);
        List<String> args = Arrays.asList(login, password);
        Response response = client.sendRequest(new Request(new LoginCommand().getName(), args, null, curUser));
        if (response.success()){
            this.currentUser = curUser;
        }
        return response;
    }

    public Response register(String login, String password) throws IOException {
        UserRec curUser = new UserRec(login, password);
        List<String> args = Arrays.asList(login, password);
        Response response = client.sendRequest(new Request(new RegisterCommand().getName(), args, null, curUser));
        if (response.success()){
            this.currentUser = curUser;
        }
        return response;
    }

    public Response showAllDragons() throws IOException {
        Request request = new Request(new ShowCommand().getName(), null, null, getCurrentUser());
        return client.sendRequest(request);
    }

    private UserRec getCurrentUser(){
        return currentUser;
    }
    
}
