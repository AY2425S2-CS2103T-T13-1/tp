package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

import seedu.address.model.util.LocalDateUtils;

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
        "^" + LocalDateUtils.DATE_REGEX + "\\s" // Date
            + VALIDATION_REGEX_TIME + "\\s" // First HHmm (0000 - 2359)
            + VALIDATION_REGEX_TIME + "$"; // Second HHmm (0000 - 2359)

    public final LocalDate date;
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

    private static LocalDate extractDate(String schedule) {
        String datePart = schedule.split(" ")[0];
        return LocalDateUtils.localDateParser(datePart);
    }

    private static String extractStartTime(String schedule) {
        return schedule.split(" ")[1];
    }

    private static String extractEndTime(String schedule) {
        return schedule.split(" ")[2];
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return LocalDateUtils.toString(date);
    }

    /**
     * Returns true if a given date is a valid one time date.
     */
    public static boolean isValidOneTimeSchedule(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return "[" + getDateString() + " " + startTime + " " + endTime + "]";
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
        String toHash = getDateString() + " " + startTime + " " + endTime;
        return toHash.hashCode();
    }


}
