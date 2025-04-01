package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generic schedule with a start time and end time.
 * This is the base class for {@link OneTimeSchedule} and {@link RecurringSchedule}.
 */
public abstract class Schedule {
    public static final String MESSAGE_TIME_CONSTRAINTS = "End time (second time) must be later than start time"
            + " (first time).";
    public static final String VALIDATION_REGEX_TIME = "(?:[01]\\d|2[0-3])[0-5]\\d"; //HHmm (0000 - 2359)
    protected final String startTime;
    protected final String endTime;

    /**
     * Constructs a {@code Schedule}.
     *
     * @param startTime The starting time of the schedule.
     * @param endTime   The ending time of the schedule.
     * @throws NullPointerException if any field is null
     */
    public Schedule(String startTime, String endTime) {
        requireNonNull(startTime);
        requireNonNull(endTime);
        // Defensive check for time format
        assert startTime.matches(VALIDATION_REGEX_TIME) : "Start time must be in HHmm format";
        assert endTime.matches(VALIDATION_REGEX_TIME) : "End time must be in HHmm format";
        this.startTime = startTime;
        this.endTime = endTime;
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
     * @param test A valid schedule string.
     * @return true if the end time is later than the start time, false otherwise.
     * @throws IllegalArgumentException if test is incorrectly formatted
     */
    public static boolean isValidTime(String test) {
        requireNonNull(test);
        String[] parts = test.split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Schedule string must have at least 3 parts");
        }
        String startTime = parts[1];
        String endTime = parts[2];
        if (!startTime.matches(VALIDATION_REGEX_TIME) || !endTime.matches(VALIDATION_REGEX_TIME)) {
            throw new IllegalArgumentException("Start and end times must be in HHmm format");
        }

        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(2));
        int startTotal = startHour * 60 + startMinute;
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(2));
        int endTotal = endHour * 60 + endMinute;

        return endTotal > startTotal;
    }

    /**
     * Returns the string representation of the schedule.
     */
    @Override
    public abstract String toString();
}
