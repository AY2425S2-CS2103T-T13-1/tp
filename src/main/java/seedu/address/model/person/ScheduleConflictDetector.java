package seedu.address.model.person;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for detecting conflicts between schedules.
 */
public class ScheduleConflictDetector {

    /**
     * Checks if a person has any schedule conflicts.
     * This method checks for conflicts between all types of schedules.
     *
     * @param person The person to check for conflicts.
     * @param newSchedule The new schedule to be added.
     * @return a ScheduleConflictResult with conflict information if there is a conflict, otherwise an empty result.
     * @throws NullPointerException if person or newSchedule is null
     */
    public static ScheduleConflictResult checkScheduleConflict(Person person, Schedule newSchedule) {
        Objects.requireNonNull(person, "Person cannot be null");
        Objects.requireNonNull(newSchedule, "Schedule cannot be null");

        if (newSchedule instanceof RecurringSchedule) {
            return checkRecurringScheduleConflict(person, (RecurringSchedule) newSchedule);
        } else if (newSchedule instanceof OneTimeSchedule) {
            return checkOneTimeScheduleConflict(person, (OneTimeSchedule) newSchedule);
        }
        // Should never reach here if all schedule types are properly handled
        assert false : "Unknown schedule type";
        return new ScheduleConflictResult();
    }
    /**
     * Checks for conflicts when adding a recurring schedule.
     *
     * @param person The person to check.
     * @param newRecurringSchedule The new recurring schedule.
     * @return A result containing conflict information if found.
     */
    private static ScheduleConflictResult checkRecurringScheduleConflict(
            Person person, RecurringSchedule newRecurringSchedule) {
        // Check against existing recurring schedules
        for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
            if (existingSchedule.getDay().equals(newRecurringSchedule.getDay())) {
                if (hasTimeOverlapBetweenSchedules(newRecurringSchedule, existingSchedule)) {
                    String description = createConflictDescription(newRecurringSchedule, existingSchedule,
                            "Recurring schedule conflict on " + existingSchedule.getDay());
                    return new ScheduleConflictResult(description, existingSchedule);
                }
            }
        }
        // Check against existing one-time schedules
        for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
            DayOfWeek oneTimeDayOfWeek = existingSchedule.getDate().getDayOfWeek();
            if (oneTimeDayOfWeek.equals(newRecurringSchedule.getDay())) {
                if (hasTimeOverlapBetweenSchedules(newRecurringSchedule, existingSchedule)) {
                    String description = createConflictDescription(newRecurringSchedule, existingSchedule,
                            "Recurring schedule conflict with one-time schedule on "
                                    + existingSchedule.getDateString());
                    return new ScheduleConflictResult(description, existingSchedule);
                }
            }
        }
        return new ScheduleConflictResult();
    }
    /**
     * Checks for conflicts when adding a one-time schedule.
     *
     * @param person The person to check.
     * @param newOneTimeSchedule The new one-time schedule.
     * @return A result containing conflict information if found.
     */
    private static ScheduleConflictResult checkOneTimeScheduleConflict(
            Person person, OneTimeSchedule newOneTimeSchedule) {
        // Check against existing one-time schedules
        for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
            if (existingSchedule.getDate().equals(newOneTimeSchedule.getDate())) {
                if (hasTimeOverlapBetweenSchedules(newOneTimeSchedule, existingSchedule)) {
                    String description = createConflictDescription(newOneTimeSchedule, existingSchedule,
                            "One-time schedule conflict on " + existingSchedule.getDateString());
                    return new ScheduleConflictResult(description, existingSchedule);
                }
            }
        }
        // Check against existing recurring schedules
        DayOfWeek oneTimeDayOfWeek = newOneTimeSchedule.getDate().getDayOfWeek();
        for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
            if (existingSchedule.getDay().equals(oneTimeDayOfWeek)) {
                if (hasTimeOverlapBetweenSchedules(newOneTimeSchedule, existingSchedule)) {
                    String description = createConflictDescription(newOneTimeSchedule, existingSchedule,
                            "One-time schedule conflict with recurring schedule on "
                            + existingSchedule.getDay() + " (" + newOneTimeSchedule.getDateString() + ")");
                    return new ScheduleConflictResult(description, existingSchedule);
                }
            }
        }
        return new ScheduleConflictResult();
    }

    /**
     * Checks for internal schedule conflicts within a single person's schedules.
     * This detects if the person has overlapping schedules with themselves.
     *
     * @param person The person to check for internal conflicts.
     * @return A list of conflict description strings.
     * @throws NullPointerException if person is null
     */
    public static List<String> checkInternalScheduleConflicts(Person person) {
        Objects.requireNonNull(person, "Person cannot be null");
        List<String> conflicts = new ArrayList<>();
        // Check each recurring schedule against other recurring schedules
        checkRecurringVsRecurringScheduleConflicts(person, conflicts);
        // Check each one-time schedule against other one-time schedules
        checkOneTimeVsOneTimeScheduleConflicts(person, conflicts);
        // Check recurring schedules against one-time schedules
        checkRecurringVsOneTimeScheduleConflicts(person, conflicts);
        return conflicts;
    }
    /**
     * Checks for conflicts between recurring schedules.
     *
     * @param person The person to check.
     * @param conflicts The list to add conflict descriptions to.
     */
    private static void checkRecurringVsRecurringScheduleConflicts(Person person, List<String> conflicts) {
        List<RecurringSchedule> recurringSchedules = new ArrayList<>(person.getRecurringSchedules());
        for (int i = 0; i < recurringSchedules.size(); i++) {
            for (int j = i + 1; j < recurringSchedules.size(); j++) {
                RecurringSchedule schedule1 = recurringSchedules.get(i);
                RecurringSchedule schedule2 = recurringSchedules.get(j);
                // If on the same day, check for time overlap
                if (schedule1.getDay().equals(schedule2.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(schedule1, schedule2)) {
                        String conflictPrefix = "Internal recurring schedule conflict on " + schedule1.getDay();
                        conflicts.add(createInternalConflictDescription(conflictPrefix, schedule1, schedule2));
                    }
                }
            }
        }
    }
    /**
     * Checks for conflicts between one-time schedules.
     *
     * @param person The person to check.
     * @param conflicts The list to add conflict descriptions to.
     */
    private static void checkOneTimeVsOneTimeScheduleConflicts(Person person, List<String> conflicts) {
        List<OneTimeSchedule> oneTimeSchedules = new ArrayList<>(person.getOneTimeSchedules());
        for (int i = 0; i < oneTimeSchedules.size(); i++) {
            for (int j = i + 1; j < oneTimeSchedules.size(); j++) {
                OneTimeSchedule schedule1 = oneTimeSchedules.get(i);
                OneTimeSchedule schedule2 = oneTimeSchedules.get(j);
                // If on the same date, check for time overlap
                if (schedule1.getDate().equals(schedule2.getDate())) {
                    if (hasTimeOverlapBetweenSchedules(schedule1, schedule2)) {
                        String conflictPrefix = "Internal one-time schedule conflict on " + schedule1.getDateString();
                        conflicts.add(createInternalConflictDescription(conflictPrefix, schedule1, schedule2));
                    }
                }
            }
        }
    }
    /**
     * Checks for conflicts between recurring and one-time schedules.
     *
     * @param person The person to check.
     * @param conflicts The list to add conflict descriptions to.
     */
    private static void checkRecurringVsOneTimeScheduleConflicts(Person person, List<String> conflicts) {
        List<RecurringSchedule> recurringSchedules = new ArrayList<>(person.getRecurringSchedules());
        List<OneTimeSchedule> oneTimeSchedules = new ArrayList<>(person.getOneTimeSchedules());
        for (RecurringSchedule recurringSchedule : recurringSchedules) {
            for (OneTimeSchedule oneTimeSchedule : oneTimeSchedules) {
                // Check if one-time schedule day of week matches recurring schedule day
                if (oneTimeSchedule.getDate().getDayOfWeek().equals(recurringSchedule.getDay())) {
                    if (hasTimeOverlapBetweenSchedules(recurringSchedule, oneTimeSchedule)) {
                        String conflictPrefix = "Internal schedule conflict between recurring and one-time schedule on "
                                + recurringSchedule.getDay() + " (" + oneTimeSchedule.getDateString() + ")";
                        conflicts.add(createInternalConflictDescription(conflictPrefix, recurringSchedule,
                                oneTimeSchedule));
                    }
                }
            }
        }
    }

    /**
     * Creates a conflict description string for internal conflicts.
     *
     * @param prefix The contextual prefix for the conflict message.
     * @param schedule1 The first schedule.
     * @param schedule2 The second schedule.
     * @return A formatted conflict description.
     */
    private static String createInternalConflictDescription(String prefix, Schedule schedule1, Schedule schedule2) {
        return String.format("%s between %s and %s (same client)",
                prefix,
                schedule1.getStartTime() + "-" + schedule1.getEndTime(),
                schedule2.getStartTime() + "-" + schedule2.getEndTime());
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
     * @throws NullPointerException if any schedule is null
     */
    private static boolean hasTimeOverlapBetweenSchedules(Schedule schedule1, Schedule schedule2) {
        Objects.requireNonNull(schedule1, "Schedule 1 cannot be null");
        Objects.requireNonNull(schedule2, "Schedule 2 cannot be null");
        int start1 = convertTimeToMinutes(schedule1.getStartTime());
        int end1 = convertTimeToMinutes(schedule1.getEndTime());
        int start2 = convertTimeToMinutes(schedule2.getStartTime());
        int end2 = convertTimeToMinutes(schedule2.getEndTime());
        return hasTimeOverlap(start1, end1, start2, end2);
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
        assert start1 <= end1 : "Start time 1 must be before or equal to end time 1";
        assert start2 <= end2 : "Start time 2 must be before or equal to end time 2";
        // Check if one range starts after the other ends
        return !(end1 <= start2 || end2 <= start1);
    }
    /**
     * Converts a time string in format "HHmm" to minutes since midnight.
     *
     * @param time Time in format "HHmm".
     * @return Number of minutes since midnight.
     * @throws NumberFormatException if time is not in the correct format
     */
    private static int convertTimeToMinutes(String time) {
        Objects.requireNonNull(time, "Time cannot be null");
        assert time.matches(Schedule.VALIDATION_REGEX_TIME) : "Time must be in HHmm format";
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2));
        return hours * 60 + minutes;
    }
}
