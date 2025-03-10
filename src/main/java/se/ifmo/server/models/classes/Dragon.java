package se.ifmo.server.models.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;
import se.ifmo.server.models.interfaces.Validatable;

@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Dragon implements Comparable<Dragon>, Validatable {
    @JsonIgnore
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Coordinates coordinates;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.time.LocalDate creationDate = java.time.LocalDate.now();
    @NonNull
    private Boolean speaking;
    @NonNull
    private Color color;
    private DragonCharacter character; //Поле может быть null
    @NonNull
    private DragonHead head;

    @Override
    public int compareTo(Dragon o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (creationDate == null) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dragon:" +
                " name='" + name + '\'' +
                ", coordinates=" + coordinates.toString() +
                ", creationDate=" + creationDate +
                ", speaking=" + speaking +
                ", color=" + color +
                ", character=" + character +
                ", head=" + head.getToothcount();
    }
}


