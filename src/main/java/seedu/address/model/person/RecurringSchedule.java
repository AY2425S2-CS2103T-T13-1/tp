package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DayOfWeek;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
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
    public final DayOfWeek day;
    private static final Pattern pattern = Pattern.compile(VALIDATION_REGEX, Pattern.CASE_INSENSITIVE);
    private static final Logger logger = LogsCenter.getLogger(RecurringSchedule.class);

    /**
     * Constructs a {@code RecurringSchedule}.
     *
     * @param schedule A valid recurring schedule string.
     */
    public RecurringSchedule(String schedule) {
        super(validateThenExtractStartTime(schedule), extractEndTime(schedule));
        assert schedule != null : "Schedule string cannot be null";
        assert isValidRecurringSchedule(schedule) : "Invalid recurring schedule: " + schedule;
        this.day = extractDay(schedule);
        logger.fine("Created recurring schedule on day: " + this.day + " with times: "
                + getStartTime() + "-" + getEndTime());
    }
    private static String validateThenExtractStartTime(String schedule) {
        requireNonNull(schedule);
        checkArgument(isValidRecurringSchedule(schedule), MESSAGE_CONSTRAINTS);
        return extractStartTime(schedule);
    }

    private static DayOfWeek extractDay(String schedule) {
        try {
            return DayOfWeekUtils.fromString(schedule.split(" ")[0]);
        } catch (Exception e) {
            logger.warning("Error extracting day from schedule: " + e.getMessage());
            throw new IllegalArgumentException("Invalid day format in schedule: " + schedule, e);
        }
    }

    private static String extractStartTime(String schedule) {
        try {
            return schedule.split(" ")[1];
        } catch (Exception e) {
            logger.warning("Error extracting start time from schedule: " + e.getMessage());
            throw new IllegalArgumentException("Invalid start time format in schedule: " + schedule, e);
        }
    }

    private static String extractEndTime(String schedule) {
        try {
            return schedule.split(" ")[2];
        } catch (Exception e) {
            logger.warning("Error extracting end time from schedule: " + e.getMessage());
            throw new IllegalArgumentException("Invalid end time format in schedule: " + schedule, e);
        }
    }

    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Returns true if a given string is a valid recurring schedule.
     */
    public static boolean isValidRecurringSchedule(String test) {
        if (test == null) {
            logger.warning("Null schedule provided to isValidRecurringSchedule");
        }
        boolean isValid = pattern.matcher(test).matches();
        if (!isValid) {
            logger.fine("Invalid recurring schedule format: " + test);
        }
        return isValid;
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
        Boolean isDayEquals = day.equals(otherRecurringSchedule.day);
        Boolean isStartTimeEquals = startTime.equals(otherRecurringSchedule.startTime);
        Boolean isEndTimeEquals = endTime.equals(otherRecurringSchedule.endTime);
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
    public String toString() {
        return '[' + DayOfWeekUtils.getPascalCaseName(day) + " " + startTime + " " + endTime + ']';
    }
}
