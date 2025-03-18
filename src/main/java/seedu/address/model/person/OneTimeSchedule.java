package seedu.address.model.person;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's training date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidOneTimeSchedule(String)}
 */
public class OneTimeSchedule extends Schedule {

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

    public final String date;
    /**
     * Constructs a {@code OneTimeSchedule}.
     *
     * @param schedule A valid one-time schedule string.
     */
    public OneTimeSchedule(String schedule) {
        super(validateThenExtractStartTime(schedule), extractEndTime(schedule)); // Call Schedule constructor
        this.date = extractDate(schedule);
    }
    private static String validateThenExtractStartTime(String schedule) {
        requireNonNull(schedule);
        checkArgument(isValidOneTimeSchedule(schedule), MESSAGE_CONSTRAINTS);
        return extractStartTime(schedule);
    }

    private static String extractDate(String schedule) {
        String datePart = schedule.split(" ")[0];
        String[] dateComponents = datePart.split("/");

        // Pad with leading zeros if necessary
        String day = dateComponents[0].length() == 1 ? "0" + dateComponents[0] : dateComponents[0];
        String month = dateComponents[1].length() == 1 ? "0" + dateComponents[1] : dateComponents[1];

        // Handle optional year part (if it's there)
        String normalizedDate;
        if (dateComponents.length == 3) {
            String year = dateComponents[2];
            normalizedDate = day + "/" + month + "/" + year;
        } else {
            normalizedDate = day + "/" + month;
        }
        return normalizedDate;
    }

    private static String extractStartTime(String schedule) {
        return schedule.split(" ")[1];
    }

    private static String extractEndTime(String schedule) {
        return schedule.split(" ")[2];
    }

    public String getDate() {
        return date;
    }

    /**
     * Returns true if a given date is a valid one time date.
     */
    public static boolean isValidOneTimeSchedule(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return "[" + date + " " + startTime + " " + endTime + "]";
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
        Boolean isDateEquals = date.equals(otherOneTimeSchedule.date);
        Boolean isStartTimeEquals = startTime.equals(otherOneTimeSchedule.startTime);
        Boolean isEndTimeEquals = endTime.equals(otherOneTimeSchedule.endTime);
        return isDateEquals && isStartTimeEquals && isEndTimeEquals;
    }

    @Override
    public int hashCode() {
        String toHash = date + " " + startTime + " " + endTime;
        return toHash.hashCode();
    }


}
