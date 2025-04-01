package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents a generic schedule with a start time and end time.
 * This is the base class for {@link OneTimeSchedule} and {@link RecurringSchedule}.
 */
public abstract class Schedule {
    public static final String MESSAGE_TIME_CONSTRAINTS = "End time (second time) must be later than start time"
            + " (first time).";
    public static final String VALIDATION_REGEX_TIME = "(?:[01]\\d|2[0-3])[0-5]\\d"; //HHmm (0000 - 2359)
    private static final Logger logger = LogsCenter.getLogger(Schedule.class);
    protected final String startTime;
    protected final String endTime;


    /**
     * Constructs a {@code Schedule}.
     *
     * @param startTime The starting time of the schedule.
     * @param endTime   The ending time of the schedule.
     */
    public Schedule(String startTime, String endTime) {
        requireNonNull(startTime);
        requireNonNull(endTime);
        assert startTime.matches(VALIDATION_REGEX_TIME) : "Start time format invalid: " + startTime;
        assert endTime.matches(VALIDATION_REGEX_TIME) : "End time format invalid: " + endTime;
        this.startTime = startTime;
        this.endTime = endTime;
        logger.fine("Created schedule with start time: " + startTime + " and end time: " + endTime);
    }

    /**
     * Returns the start time of the schedule.
     *
     * @return Start time as a String.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the schedule.
     *
     * @return End time as a String.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Returns true if the end time is later than the start time in the given schedule.
     *
     * @param test A valid recurring schedule.
     * @return true if the end time is later than the start time, false otherwise.
     */
    public static boolean isValidTime(String test) {
        requireNonNull(test);
        try {
            String[] parts = test.split("\\s+");
            if (parts.length < 3) {
                logger.warning("Invalid test string format for isValidTime: " + test);
                return false;
            }
            String startTime = parts[1];
            String endTime = parts[2];
            if (!startTime.matches(VALIDATION_REGEX_TIME) || !endTime.matches(VALIDATION_REGEX_TIME)) {
                logger.warning("Time format invalid in isValidTime: " + test);
                return false;
            }

            int startHour = Integer.parseInt(startTime.substring(0, 2));
            int startMinute = Integer.parseInt(startTime.substring(2));
            int startTotal = startHour * 60 + startMinute;
            int endHour = Integer.parseInt(endTime.substring(0, 2));
            int endMinute = Integer.parseInt(endTime.substring(2));
            int endTotal = endHour * 60 + endMinute;

            return endTotal > startTotal;
        } catch (Exception e) {
            logger.warning("Error validating time: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the string representation of the schedule.
     */
    @Override
    public abstract String toString();
}
