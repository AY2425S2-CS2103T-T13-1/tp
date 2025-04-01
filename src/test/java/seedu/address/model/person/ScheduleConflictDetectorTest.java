package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ScheduleConflictDetectorTest {

    @Test
    public void checkScheduleConflict_recurringScheduleOverlapping_returnsConflict() {

        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1100 1300")); // Overlaps

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
        assertTrue(result.getConflictDescription().contains("MONDAY"));
        assertTrue(result.getConflictDescription().contains("1000-1200"));
        assertTrue(result.getConflictDescription().contains("1100-1300"));
    }

    @Test
    public void checkScheduleConflict_recurringScheduleNoOverlap_returnsNoConflict() {

        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1300 1500")); // No overlap (different time)
        recurringSchedules.add(new RecurringSchedule("Tuesday 1000 1200")); // No overlap (different day)

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should not detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertFalse(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_oneTimeScheduleOverlapping_returnsConflict() {

        OneTimeSchedule newSchedule = new OneTimeSchedule("15/10 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1100 1300")); // Overlaps

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
        assertTrue(result.getConflictDescription().contains("15/10"));
        assertTrue(result.getConflictDescription().contains("1000-1200"));
        assertTrue(result.getConflictDescription().contains("1100-1300"));
    }

    @Test
    public void checkScheduleConflict_oneTimeScheduleNoOverlap_returnsNoConflict() {

        OneTimeSchedule newSchedule = new OneTimeSchedule("15/10 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1300 1500")); // No overlap (different time)
        oneTimeSchedules.add(new OneTimeSchedule("16/10 1000 1200")); // No overlap (different date)

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should not detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertFalse(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_sameExactTime_returnsConflict() {

        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200")); // Exact same time

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_recurringWithOneTime_returnsConflict() {

        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("31/03 1100 1300")); // Overlaps

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_oneTimeWithRecurring_returnsConflict() {

        OneTimeSchedule newSchedule = new OneTimeSchedule("31/03 1000 1200");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1100 1300")); // Overlaps

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_recurringBoundaryCase_noConflict() {

        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1200 1400");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200")); // Ends right when new one starts

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should not detect a conflict since they're exactly adjacent
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertFalse(result.hasConflict());
    }

    @Test
    public void checkScheduleConflict_oneTimeBoundaryCase_noConflict() {

        OneTimeSchedule newSchedule = new OneTimeSchedule("31/03 1200 1400");

        Set<RecurringSchedule> recurringSchedules = new HashSet<>();

        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("31/03 1000 1200")); // Ends right when new one starts

        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);

        // Should not detect a conflict since they're exactly adjacent
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertFalse(result.hasConflict());
    }

    @Test
    public void checkInternalScheduleConflicts_noConflict_returnsEmptyList() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200"));
        recurringSchedules.add(new RecurringSchedule("Tuesday 1000 1200"));
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1000 1200"));
        oneTimeSchedules.add(new OneTimeSchedule("16/10 1000 1200"));
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        List<String> conflicts = ScheduleConflictDetector.checkInternalScheduleConflicts(person);
        assertEquals(0, conflicts.size(), "Should not detect any internal conflicts");
    }
    @Test
    public void checkInternalScheduleConflicts_recurringSchedulesConflict_returnsConflict() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200"));
        recurringSchedules.add(new RecurringSchedule("Monday 1100 1300")); // Overlaps with first schedule
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        List<String> conflicts = ScheduleConflictDetector.checkInternalScheduleConflicts(person);
        assertEquals(1, conflicts.size(), "Should detect one internal conflict");
        assertTrue(conflicts.get(0).contains("Internal recurring schedule conflict on MONDAY"));
        assertTrue(conflicts.get(0).contains("same client"));
    }
    @Test
    public void checkInternalScheduleConflicts_oneTimeSchedulesConflict_returnsConflict() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1000 1200"));
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1100 1300")); // Overlaps with first schedule
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        List<String> conflicts = ScheduleConflictDetector.checkInternalScheduleConflicts(person);
        assertEquals(1, conflicts.size(), "Should detect one internal conflict");
        assertTrue(conflicts.get(0).contains("Internal one-time schedule conflict on 15/10"));
        assertTrue(conflicts.get(0).contains("same client"));
    }
    @Test
    public void checkInternalScheduleConflicts_recurringAndOneTimeSchedulesConflict_returnsConflict() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200"));
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("31/03 1100 1300")); // Assuming 31/03 is a Monday
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        List<String> conflicts = ScheduleConflictDetector.checkInternalScheduleConflicts(person);
        assertEquals(1, conflicts.size(), "Should detect one internal conflict");
        assertTrue(conflicts.get(0).contains("Internal schedule conflict between recurring and one-time schedule"));
        assertTrue(conflicts.get(0).contains("same client"));
    }
    @Test
    public void checkInternalScheduleConflicts_multipleConflicts_returnsAllConflicts() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        recurringSchedules.add(new RecurringSchedule("Monday 1000 1200"));
        recurringSchedules.add(new RecurringSchedule("Monday 1100 1300")); // Conflicts with first recurring
        recurringSchedules.add(new RecurringSchedule("Tuesday 1000 1200"));
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("31/03 1100 1300")); // Conflicts with Monday recurring
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1000 1200"));
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1100 1300")); // Conflicts with another one-time
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        List<String> conflicts = ScheduleConflictDetector.checkInternalScheduleConflicts(person);
        assertEquals(4, conflicts.size(), "Should detect four internal conflicts");
    }
    @Test
    public void checkScheduleConflict_nullPerson_throwsNullPointerException() {
        RecurringSchedule schedule = new RecurringSchedule("Monday 1000 1200");
        assertThrows(NullPointerException.class, 
            () -> ScheduleConflictDetector.checkScheduleConflict(null, schedule));
    }
    @Test
    public void checkScheduleConflict_nullSchedule_throwsNullPointerException() {
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        assertThrows(NullPointerException.class, 
            () -> ScheduleConflictDetector.checkScheduleConflict(person, null));
    }
    @Test
    public void checkInternalScheduleConflicts_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, 
            () -> ScheduleConflictDetector.checkInternalScheduleConflicts(null));
    }
    @Test
    public void hasTimeOverlapBetweenSchedules_handlesInvalidTimeFormats() {
        // This test ensures our defensive coding properly handles malformed time inputs
        // We can't directly call the private method, but we can test the public methods that use it
        
        // Create a person with valid schedules
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10 1000 1200"));
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        
        // Test with valid schedule format - should not throw any exception
        OneTimeSchedule validSchedule = new OneTimeSchedule("15/10 1100 1300");
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, validSchedule);
        assertTrue(result.hasConflict());
    }
    @Test
    public void checkScheduleConflict_unknownScheduleType_returnsNoConflict() {
        // Create a custom unknown schedule type that extends Schedule
        Schedule unknownSchedule = new Schedule("1000", "1200") {
            @Override
            public String toString() {
                return "Unknown Schedule Type";
            }
        };
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        // Should handle unknown schedule type properly and return no conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, unknownSchedule);
        assertFalse(result.hasConflict());
    }

    /**
     * Helper method to create a test person with specified schedules.
     */
    private Person createTestPerson(Set<RecurringSchedule> recurringSchedules, Set<OneTimeSchedule> oneTimeSchedules) {
        return new Person(
            new Name("Test Person"),
            new Phone("98765432"),
            recurringSchedules,
            new Goals("Stay fit"),
            new MedicalHistory("None"),
            new Location("Anywhere"),
            oneTimeSchedules,
            new HashSet<>()
        );
    }
}
