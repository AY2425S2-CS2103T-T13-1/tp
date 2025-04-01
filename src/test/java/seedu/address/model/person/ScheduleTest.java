package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ScheduleTest {

    @Test
    public void constructor_validInputs_success() {
        TestSchedule schedule = new TestSchedule("1200", "1300");
        assertEquals("1200", schedule.getStartTime());
        assertEquals("1300", schedule.getEndTime());
    }
    @Test
    public void constructor_invalidTimeFormat_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new TestSchedule("12:00", "1300"));
        assertThrows(AssertionError.class, () -> new TestSchedule("1200", "13:00"));
        assertThrows(AssertionError.class, () -> new TestSchedule("abcd", "1300"));
        assertThrows(AssertionError.class, () -> new TestSchedule("1200", "abcd"));
    }

    @Test
    public void isValidTime_validInput_success() {
        assertTrue(Schedule.isValidTime("Monday 1000 1200"));
        assertTrue(Schedule.isValidTime("Monday 0000 2359"));
    }
    @Test
    public void isValidTime_endTimeEarlierThanStartTime_returnsFalse() {
        assertFalse(Schedule.isValidTime("Monday 1300 1200"));
        assertFalse(Schedule.isValidTime("Monday 2359 0000"));
    }
    @Test
    public void isValidTime_edgeCases_correctlyIdentified() {
        assertFalse(Schedule.isValidTime("Monday 1200 1200"));
        assertTrue(Schedule.isValidTime("Monday 1200 1201"));
        assertTrue(Schedule.isValidTime("Monday 0000 0001"));
        assertTrue(Schedule.isValidTime("Monday 2358 2359"));
        assertTrue(Schedule.isValidTime("Monday 1200 1300"));
        assertTrue(Schedule.isValidTime("Monday 1230 1330"));
        assertTrue(Schedule.isValidTime("Monday 1240 1310"));
    }
    @Test
    public void isValidTime_invalidTimeFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday 12:00 13:00"));
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday 1200 25:00"));
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday abcd 1300"));
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday 1200 efgh"));
    }
    @Test
    public void isValidTime_insufficientParts_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday 1200"));
        assertThrows(IllegalArgumentException.class, () ->
            Schedule.isValidTime("Monday"));
    }
    @Test
    public void isValidTime_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Schedule.isValidTime(null));
    }
    /**
     * A concrete Schedule implementation for testing.
     */
    private static class TestSchedule extends Schedule {
        public TestSchedule(String startTime, String endTime) {
            super(startTime, endTime);
        }
        @Override
        public String toString() {
            return "TestSchedule[" + startTime + "-" + endTime + "]";
        }
    }
}
