package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Class to hold schedule conflict information.
 */
public class ScheduleConflictResult {
    private static final Logger logger = LogsCenter.getLogger(ScheduleConflictResult.class);
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
        logger.fine("Created ScheduleConflictResult with no conflict");
    }

    /**
     * Constructs a {@code ScheduleConflictResult} with conflict information.
     *
     * @param conflictDescription Description of the conflict.
     * @param conflictingSchedule The schedule that caused the conflict.
     */
    public ScheduleConflictResult(String conflictDescription, Schedule conflictingSchedule) {
        requireNonNull(conflictDescription);
        this.hasConflict = true;
        this.conflictDescription = conflictDescription;
        this.conflictingSchedule = conflictingSchedule;
        logger.fine("Created ScheduleConflictResult with conflict: " + conflictDescription);
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
        // If there's no conflict, this will return null
        if (!hasConflict) {
            logger.fine("Attempted to get conflicting schedule when there is no conflict");
        }
        return conflictingSchedule;
    }
    @Override
    public String toString() {
        if (hasConflict) {
            return "Conflict: " + conflictDescription;
        } else {
            return "No conflict";
        }
    }
}
