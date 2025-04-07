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
            "One-time schedules should be in the following format: [date HHmm HHmm]. \n"
                    + "Date Formats: d/m or d/m/yy";

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Invalid date provided. Ensure the day, month, and year (if included) form a valid calendar date. "
            + "Days must match the given month (e.g., no 30th February), and leap years must be considered.";

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

    private final LocalDate date;

    /**
     * Constructs a {@code OneTimeSchedule}.
     *
     * @param schedule A valid one-time schedule string.
     * @throws NullPointerException if schedule is null
     * @throws IllegalArgumentException if schedule format is invalid
     */
    public OneTimeSchedule(String schedule) {
        super(validateThenExtractStartTime(schedule), extractEndTime(schedule)); // Call Schedule constructor
        assert isValidOneTimeSchedule(schedule) : "Schedule should be valid by this point";
        this.date = extractDate(schedule);
    }
    /**
     * Validates the schedule string and extracts the start time if valid.
     *
     * @param schedule The schedule string to validate and extract from.
     * @return The extracted start time.
     * @throws NullPointerException if schedule is null
     * @throws IllegalArgumentException if schedule format is invalid
     */
    private static String validateThenExtractStartTime(String schedule) {
        requireNonNull(schedule);
        checkArgument(isValidOneTimeSchedule(schedule), MESSAGE_CONSTRAINTS);
        return extractStartTime(schedule);
    }

    /**
     * Extracts the date from a valid schedule string.
     *
     * @param schedule A valid schedule string.
     * @return The extracted date.
     */
    private static LocalDate extractDate(String schedule) {
        String[] parts = schedule.split(" ");
        assert parts.length >= 1 : "Schedule string should have at least date part";
        String datePart = parts[0];
        return LocalDateUtils.localDateParser(datePart);
    }

    /**
     * Extracts the start time from a valid schedule string.
     *
     * @param schedule A valid schedule string.
     * @return The extracted start time.
     */
    private static String extractStartTime(String schedule) {
        String[] parts = schedule.split(" ");
        assert parts.length >= 2 : "Schedule string should have at least date and start time parts";
        return parts[1];
    }

    /**
     * Extracts the end time from a valid schedule string.
     *
     * @param schedule A valid schedule string.
     * @return The extracted end time.
     */
    private static String extractEndTime(String schedule) {
        String[] parts = schedule.split(" ");
        assert parts.length >= 3 : "Schedule string should have date, start time, and end time parts";
        return parts[2];
    }

    /**
     * Returns the date of the schedule.
     *
     * @return The schedule date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the date as a string in the format dd/mm/yy.
     *
     * @return The date string.
     */
    public String getDateString() {
        return LocalDateUtils.toString(date);
    }

    /**
     * Returns true if a given string is a valid one time schedule format.
     *
     * @param test The string to validate.
     * @return True if the string represents a valid one-time schedule, false otherwise.
     * @throws NullPointerException if test is null
     */
    public static boolean isValidOneTimeSchedule(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the given String has a valid date.
     */
    public static boolean isValidDate(String test) {
        requireNonNull(test);
        String datePart = test.split(" ")[0];
        return LocalDateUtils.isValidDateString(datePart);
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
        boolean isDateEquals = date.equals(otherOneTimeSchedule.date);
        boolean isStartTimeEquals = startTime.equals(otherOneTimeSchedule.startTime);
        boolean isEndTimeEquals = endTime.equals(otherOneTimeSchedule.endTime);
        return isDateEquals && isStartTimeEquals && isEndTimeEquals;
    }

    @Override
    public int hashCode() {
        String toHash = getDateString() + " " + startTime + " " + endTime;
        return toHash.hashCode();
    }
}
