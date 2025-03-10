package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's goals in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGoals(String)}
 */
public class Goals {

    public static final String MESSAGE_CONSTRAINTS = "Goals can take any values, and it should not be blank";

    /*
     * The first character of the goals must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s][\\p{ASCII}]*";

    public final String value;

    /**
     * Constructs an {@code Goal}.
     *
     * @param goals A valid goals.
     */
    public Goals(String goals) {
        requireNonNull(goals);
        checkArgument(isValidGoals(goals), MESSAGE_CONSTRAINTS);
        value = goals;
    }

    /**
     * Returns true if a given string is a valid goal.
     */
    public static boolean isValidGoals(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Goals)) {
            return false;
        }

        Goals otherGoals = (Goals) other;
        return value.equals(otherGoals.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
