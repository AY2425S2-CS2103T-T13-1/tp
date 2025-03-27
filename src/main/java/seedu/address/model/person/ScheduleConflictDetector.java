package seedu.address.model.person;

import java.time.DayOfWeek;

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
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
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
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
                        String description = "Recurring schedule conflict with one-time schedule on "
                                + existingSchedule.getDateString()
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
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
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
                    if (hasTimeOverlapBetweenSchedules(newSchedule, existingSchedule)) {
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
     * Checks if there is a time overlap between two schedules.
     *
     * @param schedule1 The first schedule.
     * @param schedule2 The second schedule.
     * @return true if there is an overlap, false otherwise.
     */
    private static boolean hasTimeOverlapBetweenSchedules(Schedule schedule1, Schedule schedule2) {
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
