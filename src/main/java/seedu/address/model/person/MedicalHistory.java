package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMedicalHistory(String)}
 */
public class MedicalHistory {

    public static final String MESSAGE_CONSTRAINTS = "Medical History can take any values";

    /*
     * Medical history accepts any ASCII characters, including a space as the first input.
     */
    public static final String VALIDATION_REGEX = ".*";

    public final String value;

    /**
     * Constructs an {@code MedicalHistory}.
     *
     * @param medicalHistory A valid medical history.
     */
    public MedicalHistory(String medicalHistory) {
        requireNonNull(medicalHistory);
        checkArgument(isValidMedicalHistory(medicalHistory), MESSAGE_CONSTRAINTS);
        value = medicalHistory;
    }

    /**
     * Returns true if a given string is a valid medical history.
     */
    public static boolean isValidMedicalHistory(String test) {
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
        if (!(other instanceof MedicalHistory)) {
            return false;
        }

        MedicalHistory otherMedicalHistory = (MedicalHistory) other;
        return value.equals(otherMedicalHistory.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
