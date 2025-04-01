package seedu.address.model.person;

import java.util.Objects;

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
     * @throws NullPointerException if conflictDescription is null
     */
    public ScheduleConflictResult(String conflictDescription, Schedule conflictingSchedule) {
        Objects.requireNonNull(conflictDescription, "Conflict description cannot be null");
        this.hasConflict = true;
        this.conflictDescription = conflictDescription;
        this.conflictingSchedule = conflictingSchedule;
        // Verify that if we have a conflict, we have a description
        assert !this.conflictDescription.isEmpty() : "Conflict description should not be empty when a conflict exists";
    }

    /**
     * Returns whether there is a conflict.
     *
     * @return true if a conflict exists, false otherwise.
     */
    public boolean hasConflict() {
        return hasConflict;
    }

    /**
     * Returns a description of the conflict.
     *
     * @return The conflict description string, or empty string if no conflict.
     */
    public String getConflictDescription() {
        return conflictDescription;
    }

    /**
     * Returns the schedule that caused the conflict.
     *
     * @return The conflicting schedule, or null if no conflict.
     */
    public Schedule getConflictingSchedule() {
        return conflictingSchedule;
    }
    /**
     * Returns a string representation of the conflict result.
     *
     * @return A string describing the conflict or a "No conflict" message.
     */
    @Override
    public String toString() {
        return hasConflict ? "Conflict: " + conflictDescription : "No conflict";
    }
}
