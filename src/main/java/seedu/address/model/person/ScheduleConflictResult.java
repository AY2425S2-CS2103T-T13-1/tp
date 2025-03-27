package seedu.address.model.person;

/**
 * Class to hold schedule conflict information.
 */
public class ScheduleConflictResult {
    private final boolean hasConflict;
    private final String conflictDescription;
    private final Schedule conflictingSchedule;

    /**
     * Constructs a {@code ScheduleConflictResult} with no conflict.
     */
    public ScheduleConflictResult() {
        this.hasConflict = false;
        this.conflictDescription = "";
        this.conflictingSchedule = null;
    }

    /**
     * Constructs a {@code ScheduleConflictResult} with conflict information.
     *
     * @param conflictDescription Description of the conflict.
     * @param conflictingSchedule The schedule that caused the conflict.
     */
    public ScheduleConflictResult(String conflictDescription, Schedule conflictingSchedule) {
        this.hasConflict = true;
        this.conflictDescription = conflictDescription;
        this.conflictingSchedule = conflictingSchedule;
    }

    /**
     * Returns whether there is a conflict.
     */
    public boolean hasConflict() {
        return hasConflict;
    }

    /**
     * Returns a description of the conflict.
     */
    public String getConflictDescription() {
        return conflictDescription;
    }

    /**
     * Returns the schedule that caused the conflict.
     */
    public Schedule getConflictingSchedule() {
        return conflictingSchedule;
    }
}
