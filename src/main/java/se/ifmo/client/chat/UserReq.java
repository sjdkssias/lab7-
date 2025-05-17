package se.ifmo.client.chat;

import lombok.Data;

import java.io.Serializable;

import static se.ifmo.server.crypt.Crypt.hashPassword;
@Data
public class UserReq implements Serializable {
    private String password;
    private String username;
    public UserReq (String username, String password){
        this.username = username;
        this.password = getHashPassword(password);
    }


    private String getHashPassword(String password){
        if (password!= null){
            return hashPassword(password);
        }
        return null;
    }
}
