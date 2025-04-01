package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;

import org.junit.jupiter.api.Test;

public class RecurringScheduleTest {

    @Test
    public void constructor_nullSchedule_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RecurringSchedule(null));
    }

    @Test
    public void constructor_invalidSchedule_throwsIllegalArgumentException() {
        // invalid day
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule("NoSuchDay 1200 1300"));
        
        // invalid time format
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule("Monday 25:00 1300"));
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule("Monday 1200 2500"));
        
        // invalid schedule format (missing parts)
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule("Monday 1200"));
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule("Monday"));
    }

    @Test
    public void isValidRecurringSchedule() {
        // null schedule
        assertFalse(RecurringSchedule.isValidRecurringSchedule(null));

        // invalid schedules
        assertFalse(RecurringSchedule.isValidRecurringSchedule(""));
        assertFalse(RecurringSchedule.isValidRecurringSchedule(" "));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday"));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 1200"));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("1200 1300"));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 1200 1300 extra"));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 2500 1300"));
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 1200 2500"));

        // valid schedules - different day formats
        assertTrue(RecurringSchedule.isValidRecurringSchedule("Monday 1200 1300"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("MON 1200 1300"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("mon 1200 1300"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("Tuesday 0000 2359"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("WED 0900 1700"));
    }

    @Test
    public void equals() {
        RecurringSchedule mondaySchedule = new RecurringSchedule("Monday 1200 1300");
        // same object -> returns true
        assertTrue(mondaySchedule.equals(mondaySchedule));
        // same values -> returns true
        RecurringSchedule mondayScheduleCopy = new RecurringSchedule("Monday 1200 1300");
        assertTrue(mondaySchedule.equals(mondayScheduleCopy));
        // different types -> returns false
        assertFalse(mondaySchedule.equals(1));
        // null -> returns false
        assertFalse(mondaySchedule.equals(null));
        // different day -> returns false
        RecurringSchedule tuesdaySchedule = new RecurringSchedule("Tuesday 1200 1300");
        assertFalse(mondaySchedule.equals(tuesdaySchedule));
        // different start time -> returns false
        RecurringSchedule differentStartTime = new RecurringSchedule("Monday 1300 1400");
        assertFalse(mondaySchedule.equals(differentStartTime));
        // different end time -> returns false
        RecurringSchedule differentEndTime = new RecurringSchedule("Monday 1200 1400");
        assertFalse(mondaySchedule.equals(differentEndTime));
    }

    @Test
    public void hashCode_test() {
        RecurringSchedule mondaySchedule = new RecurringSchedule("Monday 1200 1300");
        RecurringSchedule sameMondaySchedule = new RecurringSchedule("Monday 1200 1300");
        RecurringSchedule differentSchedule = new RecurringSchedule("Tuesday 1200 1300");
        // same values -> same hash code
        assertEquals(mondaySchedule.hashCode(), sameMondaySchedule.hashCode());
        // different values -> different hash codes
        assertNotEquals(mondaySchedule.hashCode(), differentSchedule.hashCode());
    }

    @Test
    public void toString_test() {
        RecurringSchedule mondaySchedule = new RecurringSchedule("Monday 1200 1300");
        assertEquals("[Monday 1200 1300]", mondaySchedule.toString());
        RecurringSchedule tuesdaySchedule = new RecurringSchedule("TUE 0900 1700");
        assertEquals("[Tuesday 0900 1700]", tuesdaySchedule.toString());
    }

    @Test
    public void getDay_test() {
        RecurringSchedule mondaySchedule = new RecurringSchedule("Monday 1200 1300");
        assertEquals(DayOfWeek.MONDAY, mondaySchedule.getDay());
        RecurringSchedule tuesdaySchedule = new RecurringSchedule("tue 0900 1700");
        assertEquals(DayOfWeek.TUESDAY, tuesdaySchedule.getDay());
    }
}
