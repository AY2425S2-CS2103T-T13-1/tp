package seedu.address.model.person;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's training date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidOneTimeSchedule(String)}
 */
public class OneTimeSchedule {

    public static final String MESSAGE_CONSTRAINTS =
        "Dates must be in the format: date start end, either [d/m HHmm HHmm] or [d/m/yy HHmm HHmm].\n"
        + "Days and months can be 1 or 2 digits. Years (if included) must be 2 digits. "
        + "Times must be 4 digits in 24-hour format.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     *
     * The date-time input must follow the format:
     * [dd/mm HHmm HHmm] or [dd/mm/yy HHmm HHmm]
     *
     * Day: 1-31 (accepts leading zero)
     * Month: 1-12 (accepts leading zero)
     * Year: optional, but if present, must be two digits
     * Times: 24-hour time format
     */
    public static final String VALIDATION_REGEX =
        "([0-9]|[0-2][0-9]|3[0-1])/([0-9]|0[1-9]|1[0-2])(/\\d{2})?\\s" // Date
        + "([01][0-9]|2[0-3])[0-5][0-9]\\s" // Start time
        + "([01][0-9]|2[0-3])[0-5][0-9]"; // End time

    public final String value;

    /**
     * Constructs a {@code OneTimeDate}.
     *
     * @param date A valid one time date.
     */
    public OneTimeSchedule(String date) {
        requireNonNull(date);
        checkArgument(isValidOneTimeSchedule(date), MESSAGE_CONSTRAINTS);
        this.value = date;
    }

    /**
     * Returns true if a given date is a valid one time date.
     */
    public static boolean isValidOneTimeSchedule(String test) {
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
        if (!(other instanceof OneTimeSchedule)) {
            return false;
        }

        OneTimeSchedule otherOneTimeSchedule = (OneTimeSchedule) other;
        return value.equals(otherOneTimeSchedule.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }


}
