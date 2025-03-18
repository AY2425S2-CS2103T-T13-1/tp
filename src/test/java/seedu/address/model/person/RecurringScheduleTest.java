package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String invalidSchedule = "invalid schedule";
        assertThrows(IllegalArgumentException.class, () -> new RecurringSchedule(invalidSchedule));
    }

    @Test
    public void isValidRecurringSchedule_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> RecurringSchedule.isValidRecurringSchedule(null));
    }

    @Test
    public void isValidSchedule_validRecurringSchedules_returnsTrue() {
        // Valid schedule examples:
        assertTrue(RecurringSchedule.isValidRecurringSchedule("Monday 0900 1700"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("Mon 0000 2359"));
        assertTrue(RecurringSchedule.isValidRecurringSchedule("Tuesday 1230 1330"));
        // Test case-insensitivity
        assertTrue(RecurringSchedule.isValidRecurringSchedule("fri 0800 1200"));
    }

    @Test
    public void isValidRecurringSchedule_invalidSchedules_returnsFalse() {
        // Invalid schedule examples:
        // empty string
        assertFalse(RecurringSchedule.isValidRecurringSchedule(""));
        // missing times
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday"));
        // missing second time
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 0900"));
        // no spaces between parts
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday09001700"));
        // invalid day
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Funday 0900 1700"));
        // time not in four-digit format
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 900 1700"));
        // invalid time: 2400 not allowed
        assertFalse(RecurringSchedule.isValidRecurringSchedule("Monday 2400 0100"));
    }

    @Test
    void testValidAbbreviations() {
        assertEquals("Monday", RecurringSchedule.formatDay("mon"));
        assertEquals("Tuesday", RecurringSchedule.formatDay("tue"));
        assertEquals("Wednesday", RecurringSchedule.formatDay("wed"));
        assertEquals("Thursday", RecurringSchedule.formatDay("thu"));
        assertEquals("Friday", RecurringSchedule.formatDay("fri"));
        assertEquals("Saturday", RecurringSchedule.formatDay("sat"));
        assertEquals("Sunday", RecurringSchedule.formatDay("sun"));
    }

    @Test
    void testValidFullNames() {
        assertEquals("Monday", RecurringSchedule.formatDay("monday"));
        assertEquals("Tuesday", RecurringSchedule.formatDay("tuesday"));
        assertEquals("Wednesday", RecurringSchedule.formatDay("wednesday"));
        assertEquals("Thursday", RecurringSchedule.formatDay("thursday"));
        assertEquals("Friday", RecurringSchedule.formatDay("friday"));
        assertEquals("Saturday", RecurringSchedule.formatDay("saturday"));
        assertEquals("Sunday", RecurringSchedule.formatDay("sunday"));
    }

    @Test
    void testCaseInsensitiveInput() {
        assertEquals("Monday", RecurringSchedule.formatDay("Mon"));
        assertEquals("Tuesday", RecurringSchedule.formatDay("TUE"));
        assertEquals("Wednesday", RecurringSchedule.formatDay("WeD"));
        assertEquals("Thursday", RecurringSchedule.formatDay("thuRSDay"));
        assertEquals("Friday", RecurringSchedule.formatDay("FRIDAY"));
    }

    @Test
    void testInvalidDayThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> RecurringSchedule.formatDay("funday"));
        assertThrows(IllegalArgumentException.class, () -> RecurringSchedule.formatDay("mnday"));
        assertThrows(IllegalArgumentException.class, () -> RecurringSchedule.formatDay("xyz"));
        assertThrows(IllegalArgumentException.class, () -> RecurringSchedule.formatDay(""));
    }

    @Test
    void testNullInputThrowsException() {
        assertThrows(NullPointerException.class, () -> RecurringSchedule.formatDay(null));
    }

    @Test
    public void equals() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 0900 1700");
        // Same values -> returns true
        assertTrue(schedule.equals(new RecurringSchedule("Monday 0900 1700")));
        // Same object -> returns true
        assertTrue(schedule.equals(schedule));
        // Null -> returns false
        assertFalse(schedule.equals(null));
        // Different types -> returns false
        assertFalse(schedule.equals("Monday 0900 1700"));
        // Different schedule -> returns false
        assertFalse(schedule.equals(new RecurringSchedule("Tuesday 0900 1700")));
    }

    @Test
    public void hashCode_sameForEqualSchedules() {
        RecurringSchedule schedule1 = new RecurringSchedule("Monday 0900 1700");
        RecurringSchedule schedule2 = new RecurringSchedule("Monday 0900 1700");
        assertTrue(schedule1.hashCode() == schedule2.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 0900 1700");
        String expected = "[Monday 0900 1700]";
        assertTrue(schedule.toString().equals(expected));
    }

    @Test
    public void isValidTime_validTimes_returnsTrue() {
        // Valid schedule where end time is after start time.
        assertTrue(RecurringSchedule.isValidTime("Monday 0900 1700"));
    }

    @Test
    public void isValidTime_invalidTimes_returnsFalse() {
        // Although the format is valid, the end time is not after the start time.
        assertFalse(RecurringSchedule.isValidTime("Monday 1400 1200"));
    }

    @Test
    public void constructor_abbreviatedDayFormatsToFullDay() {
        // Provide an abbreviated day and check that it is formatted to its full, capitalized form.
        RecurringSchedule schedule = new RecurringSchedule("fri 0800 1200");
        assertEquals("[Friday 0800 1200]", schedule.toString());
    }

    @Test
    public void constructor_edgeCaseTimes_returnsCorrectFormat() {
        // Test the edge valid times: one minute past midnight and near midnight.
        RecurringSchedule schedule1 = new RecurringSchedule("Monday 0000 0001");
        assertEquals("[Monday 0000 0001]", schedule1.toString());

        RecurringSchedule schedule2 = new RecurringSchedule("Monday 2358 2359");
        assertEquals("[Monday 2358 2359]", schedule2.toString());
    }

    @Test
    public void isValidTime_null_throwsNullPointerException() {
        // The method should throw a NullPointerException when provided with null.
        assertThrows(NullPointerException.class, () -> RecurringSchedule.isValidTime(null));
    }

    @Test
    public void constructor_formatsMonday_correctly() {
        // Abbreviated and full form for Monday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("mon 0800 1200");
        assertEquals("[Monday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("monday 0800 1200");
        assertEquals("[Monday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsTuesday_correctly() {
        // Abbreviated and full form for Tuesday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("tue 0800 1200");
        assertEquals("[Tuesday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("tuesday 0800 1200");
        assertEquals("[Tuesday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsWednesday_correctly() {
        // Abbreviated and full form for Wednesday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("wed 0800 1200");
        assertEquals("[Wednesday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("wednesday 0800 1200");
        assertEquals("[Wednesday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsThursday_correctly() {
        // Abbreviated and full form for Thursday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("thu 0800 1200");
        assertEquals("[Thursday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("thursday 0800 1200");
        assertEquals("[Thursday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsFriday_correctly() {
        // Abbreviated and full form for Friday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("fri 0800 1200");
        assertEquals("[Friday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("friday 0800 1200");
        assertEquals("[Friday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsSaturday_correctly() {
        // Abbreviated and full form for Saturday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("sat 0800 1200");
        assertEquals("[Saturday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("saturday 0800 1200");
        assertEquals("[Saturday 0800 1200]", scheduleFull.toString());
    }

    @Test
    public void constructor_formatsSunday_correctly() {
        // Abbreviated and full form for Sunday.
        RecurringSchedule scheduleAbbrev = new RecurringSchedule("sun 0800 1200");
        assertEquals("[Sunday 0800 1200]", scheduleAbbrev.toString());
        RecurringSchedule scheduleFull = new RecurringSchedule("sunday 0800 1200");
        assertEquals("[Sunday 0800 1200]", scheduleFull.toString());
    }


}
