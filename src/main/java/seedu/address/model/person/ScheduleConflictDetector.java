package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Utility class for detecting conflicts between schedules.
 */
public class ScheduleConflictDetector {
    private static final Logger logger = LogsCenter.getLogger(ScheduleConflictDetector.class);

    /**
     * Checks if a person has any schedule conflicts.
     * This method checks for conflicts between all types of schedules.
     *
     * @param person The person to check for conflicts.
     * @param newSchedule The new schedule to be added.
     * @return a ScheduleConflictResult with conflict information if there is a conflict, otherwise an empty result.
     */
    public static ScheduleConflictResult checkScheduleConflict(Person person, Schedule newSchedule) {
        requireNonNull(person);
        requireNonNull(newSchedule);
        logger.fine("Checking schedule conflicts for person: " + person.getName() + " with new schedule: "
                + newSchedule);
        if (newSchedule instanceof RecurringSchedule) {
            RecurringSchedule newRecurringSchedule = (RecurringSchedule) newSchedule;
            // Check against existing recurring schedules
            for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
                if (existingSchedule.getDay().equals(newRecurringSchedule.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
                        String description = createConflictDescription(newSchedule, existingSchedule,
                                "Recurring schedule conflict on " + existingSchedule.getDay());
                        logger.info("Found recurring schedule conflict: " + description);
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
            // Check against existing one-time schedules
            for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
                java.time.DayOfWeek oneTimeDayOfWeek = existingSchedule.getDate().getDayOfWeek();
                if (oneTimeDayOfWeek.equals(newRecurringSchedule.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
                        String description = createConflictDescription(newSchedule, existingSchedule,
                                "Recurring schedule conflict with one-time schedule on "
                                        + existingSchedule.getDateString());
                        logger.info("Found recurring vs one-time schedule conflict: " + description);
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
        } else if (newSchedule instanceof OneTimeSchedule) {
            OneTimeSchedule newOneTimeSchedule = (OneTimeSchedule) newSchedule;
            // Check against existing one-time schedules
            for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
                if (existingSchedule.getDate().equals(newOneTimeSchedule.getDate())) {
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
                        String description = createConflictDescription(newSchedule, existingSchedule,
                                "One-time schedule conflict on " + existingSchedule.getDateString());
                        logger.info("Found one-time schedule conflict: " + description);
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
            // Check against existing recurring schedules
            java.time.DayOfWeek oneTimeDayOfWeek = newOneTimeSchedule.getDate().getDayOfWeek();
            for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
                if (existingSchedule.getDay().equals(oneTimeDayOfWeek)) {
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
                        String description = createConflictDescription(newSchedule, existingSchedule,
                                "One-time schedule conflict with recurring schedule on "
                                + existingSchedule.getDay() + " (" + newOneTimeSchedule.getDateString() + ")");
                        logger.info("Found one-time vs recurring schedule conflict: " + description);
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
        } else {
            logger.warning("Unknown schedule type: " + newSchedule.getClass().getName());
        }
        logger.fine("No schedule conflicts found for person: " + person.getName());
        return new ScheduleConflictResult();
    }

    /**
     * Checks for internal schedule conflicts within a single person's schedules.
     * This detects if the person has overlapping schedules with themselves.
     *
     * @param person The person to check for internal conflicts.
     * @return A list of conflict description strings.
     */
    public static List<String> checkInternalScheduleConflicts(Person person) {
        requireNonNull(person);
        logger.fine("Checking internal schedule conflicts for person: " + person.getName());
        List<String> conflicts = new ArrayList<>();
        // Check each recurring schedule against other recurring schedules
        List<RecurringSchedule> recurringSchedules = new ArrayList<>(person.getRecurringSchedules());
        for (int i = 0; i < recurringSchedules.size(); i++) {
            for (int j = i + 1; j < recurringSchedules.size(); j++) {
                RecurringSchedule schedule1 = recurringSchedules.get(i);
                RecurringSchedule schedule2 = recurringSchedules.get(j);
                assert schedule1 != null : "Recurring schedule should not be null";
                assert schedule2 != null : "Recurring schedule should not be null";
                // If on the same day, check for time overlap
                if (schedule1.getDay().equals(schedule2.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(schedule1, schedule2)) {
                        String conflictPrefix = "Internal recurring schedule conflict on " + schedule1.getDay();
                        String conflict = String.format("%s between %s and %s (same client)",
                                conflictPrefix,
                                schedule1.getStartTime() + "-" + schedule1.getEndTime(),
                                schedule2.getStartTime() + "-" + schedule2.getEndTime());
                        conflicts.add(conflict);
                        logger.info("Found internal recurring schedule conflict: " + conflict);
                    }
                }
            }
        }
        // Check each one-time schedule against other one-time schedules
        List<OneTimeSchedule> oneTimeSchedules = new ArrayList<>(person.getOneTimeSchedules());
        for (int i = 0; i < oneTimeSchedules.size(); i++) {
            for (int j = i + 1; j < oneTimeSchedules.size(); j++) {
                OneTimeSchedule schedule1 = oneTimeSchedules.get(i);
                OneTimeSchedule schedule2 = oneTimeSchedules.get(j);
                assert schedule1 != null : "One-time schedule should not be null";
                assert schedule2 != null : "One-time schedule should not be null";
                // If on the same date, check for time overlap
                if (schedule1.getDate().equals(schedule2.getDate())) {
                    if (hasTimeOverlapBetweenSchedules(schedule1, schedule2)) {
                        String conflictPrefix = "Internal one-time schedule conflict on " + schedule1.getDateString();
                        String conflict = String.format("%s between %s and %s (same client)",
                                conflictPrefix,
                                schedule1.getStartTime() + "-" + schedule1.getEndTime(),
                                schedule2.getStartTime() + "-" + schedule2.getEndTime());
                        conflicts.add(conflict);
                        logger.info("Found internal one-time schedule conflict: " + conflict);
                    }
                }
            }
        }
        // Check recurring schedules against one-time schedules
        for (RecurringSchedule recurringSchedule : recurringSchedules) {
            for (OneTimeSchedule oneTimeSchedule : oneTimeSchedules) {
                assert recurringSchedule != null : "Recurring schedule should not be null";
                assert oneTimeSchedule != null : "One-time schedule should not be null";
                // Check if one-time schedule day of week matches recurring schedule day
                if (oneTimeSchedule.getDate().getDayOfWeek().equals(recurringSchedule.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(recurringSchedule, oneTimeSchedule)) {
                        String conflictPrefix = "Internal schedule conflict between recurring and one-time schedule on "
                                + recurringSchedule.getDay() + " (" + oneTimeSchedule.getDateString() + ")";
                        String conflict = String.format("%s between %s and %s (same client)",
                                conflictPrefix,
                                recurringSchedule.getStartTime() + "-" + recurringSchedule.getEndTime(),
                                oneTimeSchedule.getStartTime() + "-" + oneTimeSchedule.getEndTime());
                        conflicts.add(conflict);
                        logger.info("Found internal recurring vs one-time schedule conflict: " + conflict);
                    }
                }
            }
        }
        if (conflicts.isEmpty()) {
            logger.fine("No internal schedule conflicts found for person: " + person.getName());
        } else {
            logger.info("Found " + conflicts.size() + " internal schedule conflicts for person: " + person.getName());
        }
        return conflicts;
    }

    /**
     * Creates a conflict description string for two overlapping schedules.
     *
     * @param newSchedule The new schedule being added or edited.
     * @param existingSchedule The existing schedule it conflicts with.
     * @param prefix The contextual prefix for the conflict message.
     * @return A formatted conflict description.
     */
    private static String createConflictDescription(Schedule newSchedule, Schedule existingSchedule, String prefix) {
        requireNonNull(newSchedule);
        requireNonNull(existingSchedule);
        requireNonNull(prefix);
        return prefix
                + " between " + newSchedule.getStartTime() + "-" + newSchedule.getEndTime()
                + " and " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime();
    }

    /**
     * Checks if there is a time overlap between two schedules.
     *
     * @param schedule1 The first schedule.
     * @param schedule2 The second schedule.
     * @return true if there is an overlap, false otherwise.
     */
    private static boolean hasTimeOverlapBetweenSchedules(Schedule schedule1, Schedule schedule2) {
        requireNonNull(schedule1);
        requireNonNull(schedule2);
        try {
            int start1 = convertTimeToMinutes(schedule1.getStartTime());
            int end1 = convertTimeToMinutes(schedule1.getEndTime());
            int start2 = convertTimeToMinutes(schedule2.getStartTime());
            int end2 = convertTimeToMinutes(schedule2.getEndTime());
            return hasTimeOverlap(start1, end1, start2, end2);
        } catch (Exception e) {
            logger.warning("Error checking time overlap between schedules: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if there is a time overlap between two time ranges.
     *
     * @param start1 Start time of first range in minutes.
     * @param end1 End time of first range in minutes.
     * @param start2 Start time of second range in minutes.
     * @param end2 End time of second range in minutes.
     * @return true if there is an overlap, false otherwise.
     */
    private static boolean hasTimeOverlap(int start1, int end1, int start2, int end2) {
        // Ensure end time is always after start time
        assert end1 > start1 : "End time must be after start time for first schedule";
        assert end2 > start2 : "End time must be after start time for second schedule";
        // Check if one range starts after the other ends
        return !(end1 <= start2 || end2 <= start1);
    }
    /**
     * Converts a time string in format "HHmm" to minutes since midnight.
     *
     * @param time Time in format "HHmm".
     * @return Number of minutes since midnight.
     */
    private static int convertTimeToMinutes(String time) {
        requireNonNull(time);
        if (!time.matches(Schedule.VALIDATION_REGEX_TIME)) {
            logger.warning("Invalid time format: " + time);
            throw new IllegalArgumentException("Time does not match expected format (HHmm): " + time);
        }
        try {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(2));
            return hours * 60 + minutes;
        } catch (NumberFormatException e) {
            logger.severe("Error parsing time: " + time + ", " + e.getMessage());
            throw new IllegalArgumentException("Cannot convert time to minutes: " + time, e);
        }
    }
}

