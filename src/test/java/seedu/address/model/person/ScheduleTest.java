package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Schedule abstract base class and its shared functionality.
 */
public class ScheduleTest {

    private static class TestSchedule extends Schedule {
        /**
         * A concrete implementation of Schedule for testing purposes.
         */
        public TestSchedule(String startTime, String endTime) {
            super(startTime, endTime);
        }

        @Override
        public String toString() {
            return "[Test " + startTime + " " + endTime + "]";
        }
    }

    @Test
    public void constructor_nullStartTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestSchedule(null, "1400"));
    }

    @Test
    public void constructor_nullEndTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestSchedule("1000", null));
    }

    @Test
    public void constructor_validTimes_success() {
        Schedule schedule = new TestSchedule("1000", "1200");
        assertEquals("1000", schedule.getStartTime());
        assertEquals("1200", schedule.getEndTime());
    }

    @Test
    public void getStartTime_returnsCorrectTime() {
        Schedule schedule = new TestSchedule("0830", "1700");
        assertEquals("0830", schedule.getStartTime());
    }

    @Test
    public void getEndTime_returnsCorrectTime() {
        Schedule schedule = new TestSchedule("0830", "1700");
        assertEquals("1700", schedule.getEndTime());
    }

    @Test
    public void isValidTime_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Schedule.isValidTime(null));
    }

    @Test
    public void isValidTime_invalidFormat_returnsFalse() {
        // Incomplete time string
        assertFalse(Schedule.isValidTime("Monday 1000"));
        // Wrong format 
        assertFalse(Schedule.isValidTime("This is not a schedule"));
        // Invalid time format
        assertFalse(Schedule.isValidTime("Monday 1x00 1200"));
    }

    @Test
    public void isValidTime_validFormatButEndTimeBeforeStartTime_returnsFalse() {
        assertFalse(Schedule.isValidTime("Monday 1200 1000"));
        assertFalse(Schedule.isValidTime("16/5 2300 2200"));
    }

    @Test
    public void isValidTime_validFormatAndEndTimeAfterStartTime_returnsTrue() {
        assertTrue(Schedule.isValidTime("Monday 1000 1200"));
        assertTrue(Schedule.isValidTime("16/5 0900 1700"));
    }

    @Test
    public void isValidTime_validFormatSameStartAndEndTime_returnsFalse() {
        assertFalse(Schedule.isValidTime("Monday 1000 1000"));
    }
} 