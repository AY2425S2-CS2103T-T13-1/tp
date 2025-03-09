package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RecurringScheduleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RecurringSchedule(null));
    }

    @Test
    public void constructor_invalidSchedule_throwsIllegalArgumentException() {
        String invalidSchedule = "";
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule(invalidSchedule));
    }

    @Test
    public void isValidSchedule() {
        // null schedule
        assertThrows(NullPointerException.class, () -> RecurringSchedule.isValidSchedule(null));

        // Invalid schedules
        assertFalse(RecurringSchedule.isValidSchedule("")); // empty string
        assertFalse(RecurringSchedule.isValidSchedule(" ")); // spaces only
        assertFalse(RecurringSchedule.isValidSchedule("^")); // only special characters
        assertFalse(RecurringSchedule.isValidSchedule("Monday")); // missing time
        assertFalse(RecurringSchedule.isValidSchedule("Mon 1000")); // missing end time
        assertFalse(RecurringSchedule.isValidSchedule("Monday 1000 120")); // incorrect time format
        assertFalse(RecurringSchedule.isValidSchedule("Monday 100 1200")); // incorrect time format
        assertFalse(RecurringSchedule.isValidSchedule("Monday 2500 1200")); // invalid hour in time
        assertFalse(RecurringSchedule.isValidSchedule("Tuesday 1200 6100")); // invalid hour in time
        assertFalse(RecurringSchedule.isValidSchedule("Funday 1000 1200")); // invalid day name
        assertFalse(RecurringSchedule.isValidSchedule("monday10001200")); // missing spaces
        assertFalse(RecurringSchedule.isValidSchedule("Monday 1000 1200 ")); // trailing space
        assertFalse(RecurringSchedule.isValidSchedule("   Monday 1000 1200")); // leading space

        // Valid schedules
        assertTrue(RecurringSchedule.isValidSchedule("Monday 0900 1700")); // Full day name
        assertTrue(RecurringSchedule.isValidSchedule("mon 0900 1700")); // Short form, lowercase
        assertTrue(RecurringSchedule.isValidSchedule("Tue 0800 1200")); // Abbreviated day
        assertTrue(RecurringSchedule.isValidSchedule("wednesday 1000 1800")); // Full day, lowercase
        assertTrue(RecurringSchedule.isValidSchedule("FRI 2300 0200")); // Abbreviated uppercase
        assertTrue(RecurringSchedule.isValidSchedule("sunday 0930 1830")); // Case insensitive
    }

    @Test
    public void equals() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 0900 1700");

        // same values -> returns true
        assertTrue(schedule.equals(new RecurringSchedule("Monday 0900 1700")));

        // same object -> returns true
        assertTrue(schedule.equals(schedule));

        // null -> returns false
        assertFalse(schedule.equals(null));

        // different types -> returns false
        assertFalse(schedule.equals(5.0f));

        // different values -> returns false
        assertFalse(schedule.equals(new RecurringSchedule("Tuesday 0900 1700")));
        assertFalse(schedule.equals(new RecurringSchedule("Monday 0800 1700"))); // Different time
        assertFalse(schedule.equals(new RecurringSchedule("Monday 0900 1600"))); // Different end time
    }
}
