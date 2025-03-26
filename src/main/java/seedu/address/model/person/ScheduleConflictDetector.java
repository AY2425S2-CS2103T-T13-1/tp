package seedu.address.model.person;

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
     */
    public static ScheduleConflictResult checkScheduleConflict(Person person, Schedule newSchedule) {
        if (newSchedule instanceof RecurringSchedule) {
            RecurringSchedule newRecurringSchedule = (RecurringSchedule) newSchedule;

            // Check against existing recurring schedules
            for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
                if (existingSchedule.getDay().equals(newRecurringSchedule.getDay())) {
                    int existingStartMinutes = convertTimeToMinutes(existingSchedule.getStartTime());
                    int existingEndMinutes = convertTimeToMinutes(existingSchedule.getEndTime());
                    int newStartMinutes = convertTimeToMinutes(newRecurringSchedule.getStartTime());
                    int newEndMinutes = convertTimeToMinutes(newRecurringSchedule.getEndTime());

                    if (hasTimeOverlap(newStartMinutes, newEndMinutes, existingStartMinutes, existingEndMinutes)) {
                        String description = "Recurring schedule conflict on " + existingSchedule.getDay()
                                + " between " + newSchedule.getStartTime() + "-" + newSchedule.getEndTime()
                                + " and " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime();
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }

            // Check against existing one-time schedules
            for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
                java.time.DayOfWeek oneTimeDayOfWeek = existingSchedule.getDate().getDayOfWeek();

                if (oneTimeDayOfWeek.equals(newRecurringSchedule.getDay())) {
                    int existingStartMinutes = convertTimeToMinutes(existingSchedule.getStartTime());
                    int existingEndMinutes = convertTimeToMinutes(existingSchedule.getEndTime());
                    int newStartMinutes = convertTimeToMinutes(newRecurringSchedule.getStartTime());
                    int newEndMinutes = convertTimeToMinutes(newRecurringSchedule.getEndTime());

                    if (hasTimeOverlap(newStartMinutes, newEndMinutes, existingStartMinutes, existingEndMinutes)) {
                        String description = "Recurring schedule conflict with one-time schedule on " + existingSchedule.getDateString()
                                + " between " + newSchedule.getStartTime() + "-" + newSchedule.getEndTime()
                                + " and " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime();
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
        } else if (newSchedule instanceof OneTimeSchedule) {
            OneTimeSchedule newOneTimeSchedule = (OneTimeSchedule) newSchedule;

            // Check against existing one-time schedules
            for (OneTimeSchedule existingSchedule : person.getOneTimeSchedules()) {
                if (existingSchedule.getDate().equals(newOneTimeSchedule.getDate())) {
                    int existingStartMinutes = convertTimeToMinutes(existingSchedule.getStartTime());
                    int existingEndMinutes = convertTimeToMinutes(existingSchedule.getEndTime());
                    int newStartMinutes = convertTimeToMinutes(newOneTimeSchedule.getStartTime());
                    int newEndMinutes = convertTimeToMinutes(newOneTimeSchedule.getEndTime());

                    if (hasTimeOverlap(newStartMinutes, newEndMinutes, existingStartMinutes, existingEndMinutes)) {
                        String description = "One-time schedule conflict on " + existingSchedule.getDateString()
                                + " between " + newSchedule.getStartTime() + "-" + newSchedule.getEndTime()
                                + " and " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime();
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }

            // Check against existing recurring schedules
            java.time.DayOfWeek oneTimeDayOfWeek = newOneTimeSchedule.getDate().getDayOfWeek();

            for (RecurringSchedule existingSchedule : person.getRecurringSchedules()) {
                if (existingSchedule.getDay().equals(oneTimeDayOfWeek)) {
                    int existingStartMinutes = convertTimeToMinutes(existingSchedule.getStartTime());
                    int existingEndMinutes = convertTimeToMinutes(existingSchedule.getEndTime());
                    int newStartMinutes = convertTimeToMinutes(newOneTimeSchedule.getStartTime());
                    int newEndMinutes = convertTimeToMinutes(newOneTimeSchedule.getEndTime());

                    if (hasTimeOverlap(newStartMinutes, newEndMinutes, existingStartMinutes, existingEndMinutes)) {
                        String description = "One-time schedule conflict with recurring schedule on "
                                + existingSchedule.getDay() + " (" + newOneTimeSchedule.getDateString() + ")"
                                + " between " + newSchedule.getStartTime() + "-" + newSchedule.getEndTime()
                                + " and " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime();
                        return new ScheduleConflictResult(description, existingSchedule);
                    }
                }
            }
        }

        return new ScheduleConflictResult();
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
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2));
        return hours * 60 + minutes;
    }
}
