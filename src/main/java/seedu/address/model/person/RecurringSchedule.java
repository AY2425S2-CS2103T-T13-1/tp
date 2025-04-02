package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DayOfWeek;
import java.util.regex.Pattern;

import seedu.address.model.util.DayOfWeekUtils;

/**
 * Represents a RecurringSchedule in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRecurringSchedule(String)}
 */
public class RecurringSchedule extends Schedule {

    public static final String MESSAGE_CONSTRAINTS = "Recurring schedules should be in the following format:"
            + " [day HHmm HHmm].";
    /*
     * A valid schedule format:
     * - Must start with a valid day of the week (full or abbreviated).
     * - Followed by two sets of four-digit times (HHmm format, 00:00 to 23:59).
     */
    public static final String VALIDATION_REGEX =
            "^(?i)(" + DayOfWeekUtils.DAY_OF_WEEK_REGEX + ")\\s"
                    + VALIDATION_REGEX_TIME + "\\s" // First HHmm (0000 - 2359)
                    + VALIDATION_REGEX_TIME + "$"; // Second HHmm (0000 - 2359)

    private static final Pattern PATTERN = Pattern.compile(VALIDATION_REGEX, Pattern.CASE_INSENSITIVE);

    private final DayOfWeek day;

    /**
     * Constructs a {@code RecurringSchedule}.
     *
     * @param schedule A valid recurring schedule string.
     */
    public RecurringSchedule(String schedule) {
        super(validateThenExtractStartTime(schedule), extractEndTime(schedule));
        requireNonNull(schedule);
        assert isValidRecurringSchedule(schedule) : "Schedule should be valid by this point";
        this.day = extractDay(schedule);
    }
    /**
     * Validates the schedule string and extracts the start time if valid.
     *
     * @param schedule The schedule string to validate and extract from.
     * @return The extracted start time.
     * @throws NullPointerException if schedule is null.
     * @throws IllegalArgumentException if schedule format is invalid.
     */
    private static String validateThenExtractStartTime(String schedule) {
        requireNonNull(schedule);
        checkArgument(isValidRecurringSchedule(schedule), MESSAGE_CONSTRAINTS);
        return extractStartTime(schedule);
    }

    /**
     * Extracts the day of week from a valid schedule string.
     *
     * @param schedule A valid schedule string.
     * @return The extracted day of week.
     */
    private static DayOfWeek extractDay(String schedule) {
        String[] parts = schedule.split(" ");
        assert parts.length >= 1 : "Schedule string should have at least day part";
        return DayOfWeekUtils.fromString(parts[0]);
    }

    /**
     * Extracts the start time from a valid schedule string.
     *
     * @param schedule A valid schedule string.
     * @return The extracted start time.
     */
    private static String extractStartTime(String schedule) {
        String[] parts = schedule.split(" ");
        assert parts.length >= 2 : "Schedule string should have at least day and start time parts";
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
        assert parts.length >= 3 : "Schedule string should have day, start time, and end time parts";
        return parts[2];
    }

    /**
     * Returns the day of the recurring schedule.
     *
     * @return The day of week for this schedule.
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Returns true if a given string is a valid recurring schedule.
     *
     * @param test The string to validate.
     * @return True if the string represents a valid recurring schedule, false otherwise.
     * @throws NullPointerException if test is null
     */
    public static boolean isValidRecurringSchedule(String test) {
        requireNonNull(test);
        return PATTERN.matcher(test).matches();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RecurringSchedule)) {
            return false;
        }

        RecurringSchedule otherRecurringSchedule = (RecurringSchedule) other;
        boolean isDayEquals = day.equals(otherRecurringSchedule.day);
        boolean isStartTimeEquals = startTime.equals(otherRecurringSchedule.startTime);
        boolean isEndTimeEquals = endTime.equals(otherRecurringSchedule.endTime);
        return isDayEquals && isStartTimeEquals && isEndTimeEquals;
    }

    @Override
    public int hashCode() {
        String toHash = day + " " + startTime + " " + endTime;
        return toHash.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return '[' + DayOfWeekUtils.getPascalCaseName(day) + " " + startTime + " " + endTime + ']';
    }

}
