package se.ifmo.server.models.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
@Data
public class User implements Serializable {
    @JsonIgnore
    private long uid;

    @NonNull
    private String username;

    @NonNull
    private String password;
}
