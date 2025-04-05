package se.ifmo.server.models.interfaces;

/**
 * Interface representing a validatable object.
 * Any class implementing this interface must provide a validation mechanism
 * through the {@link #validate()} method to check if the object is in a valid state.
 */
public interface Validatable {

    /**
     * Validates the object to ensure it meets certain criteria.
     *
     * @return true if the object is valid, otherwise false.
     */
    boolean validate();
}
