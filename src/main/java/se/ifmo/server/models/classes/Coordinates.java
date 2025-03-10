package se.ifmo.server.models.classes;
import lombok.*;
import se.ifmo.server.models.interfaces.Validatable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates implements Validatable {
    @NonNull
    private Float x;
    @NonNull
    private Long y; //Поле не может быть null

    @Override
    public boolean validate() {
        if (x <= 0 || y <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}