package se.ifmo.server.models.classes;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import se.ifmo.server.models.enums.Color;
import se.ifmo.server.models.enums.DragonCharacter;

import lombok.*;
import se.ifmo.server.models.interfaces.Validatable;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@JacksonXmlRootElement(localName = "dragon")
public class Dragon implements Comparable<Dragon>, Validatable {
    @JacksonXmlProperty(isAttribute = true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Coordinates coordinates;
    @NonNull
    @JsonIgnore
    private java.time.LocalDate creationDate = java.time.LocalDate.now();
    @NonNull
    private Boolean speaking;
    @NonNull
    private Color color;
    @NonNull
    private DragonCharacter character; //Поле может быть null
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

