package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ScheduleConflictResultTest {

    @Test
    public void constructor_noParameters_createsNoConflictResult() {
        ScheduleConflictResult result = new ScheduleConflictResult();
        assertFalse(result.hasConflict());
        assertEquals("", result.getConflictDescription());
        assertNull(result.getConflictingSchedule());
    }
    @Test
    public void constructor_withParameters_createsConflictResult() {
        // Create a test schedule and conflict description
        RecurringSchedule schedule = new RecurringSchedule("Monday 1000 1200");
        String description = "Test conflict description";
        ScheduleConflictResult result = new ScheduleConflictResult(description, schedule);
        assertTrue(result.hasConflict());
        assertEquals(description, result.getConflictDescription());
        assertEquals(schedule, result.getConflictingSchedule());
    }
    @Test
    public void getConflictDescription_noConflict_returnsEmptyString() {
        ScheduleConflictResult result = new ScheduleConflictResult();
        assertEquals("", result.getConflictDescription());
    }
    @Test
    public void getConflictDescription_withConflict_returnsDescription() {
        String description = "Conflict with schedule";
        ScheduleConflictResult result = new ScheduleConflictResult(description, null);
        assertEquals(description, result.getConflictDescription());
    }
    @Test
    public void getConflictingSchedule_noConflict_returnsNull() {
        ScheduleConflictResult result = new ScheduleConflictResult();
        assertNull(result.getConflictingSchedule());
    }
    @Test
    public void getConflictingSchedule_withConflict_returnsSchedule() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 1000 1200");
        ScheduleConflictResult result = new ScheduleConflictResult("Description", schedule);
        assertEquals(schedule, result.getConflictingSchedule());
    }
    @Test
    public void constructor_withNullDescription_throwsNullPointerException() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 1000 1200");
        assertThrows(NullPointerException.class, () -> new ScheduleConflictResult(null, schedule));
    }
    @Test
    public void toString_noConflict_returnsCorrectString() {
        ScheduleConflictResult result = new ScheduleConflictResult();
        assertEquals("No conflict", result.toString());
    }
    @Test
    public void toString_withConflict_returnsCorrectString() {
        String description = "Test conflict description";
        RecurringSchedule schedule = new RecurringSchedule("Monday 1000 1200");
        ScheduleConflictResult result = new ScheduleConflictResult(description, schedule);
        assertEquals("Conflict: " + description, result.toString());
    }
}
