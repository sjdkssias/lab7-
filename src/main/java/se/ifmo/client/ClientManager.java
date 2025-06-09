package se.ifmo.client;

import se.ifmo.client.chat.Request;
import se.ifmo.client.chat.Response;
import se.ifmo.client.chat.UserRec;
import se.ifmo.client.commands.*;
import se.ifmo.server.models.classes.Dragon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

    public Response addDragon(Dragon dragon) throws IOException {
        List<Dragon> dragonList = Collections.singletonList(dragon);
        Request request = new Request(new AddCommand().getName(), null, dragonList, getCurrentUser());
        return client.sendRequest(request);
    }

    public Response removeKey(long id) throws IOException{
        List<String> args = Collections.singletonList(String.valueOf(id));
        Request request = new Request(new RemoveKeyCommand().getName(), args, null, getCurrentUser());
        return client.sendRequest(request);
    }

    public Response removeGreater(long id) throws IOException {
        List<String> args = Collections.singletonList(String.valueOf(id));
        Request request = new Request(new RemoveGreaterKeyCommand().getName(), args, null, getCurrentUser());
        return client.sendRequest(request);
    }

    public Response removeLower(long id) throws IOException {
        List<String> args = Collections.singletonList(String.valueOf(id));
        Request request = new Request(new RemoveLowerKeyCommand().getName(), args, null, getCurrentUser());
        return client.sendRequest(request);
    }

    public Response filterGreaterThanHead(float toothCount) throws IOException {
        List<String> args = Collections.singletonList(String.valueOf(toothCount));
        Request request = new Request(new FilterGreaterThanHeadCommand().getName(), args, null, getCurrentUser());
        return client.sendRequest(request);
    }

}
