package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ScheduleConflictDetectorTest {

    @Test
    public void checkScheduleConflict_recurringScheduleOverlapping_returnsConflict() {
        // Create a new recurring schedule
        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");
        
        // Create a person with an overlapping schedule
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
        // Create a new recurring schedule
        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");
        
        // Create a person with non-overlapping schedules
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
        // Create a new one-time schedule (we need to use a date that exists)
        OneTimeSchedule newSchedule = new OneTimeSchedule("15/10/22 1000 1200");
        
        // Create a person with an overlapping schedule
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10/22 1100 1300")); // Overlaps
        
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
        // Create a new one-time schedule
        OneTimeSchedule newSchedule = new OneTimeSchedule("15/10/22 1000 1200");
        
        // Create a person with non-overlapping schedules
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10/22 1300 1500")); // No overlap (different time)
        oneTimeSchedules.add(new OneTimeSchedule("16/10/22 1000 1200")); // No overlap (different date)
        
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        
        // Should not detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertFalse(result.hasConflict());
    }
    
    @Test
    public void checkScheduleConflict_sameExactTime_returnsConflict() {
        // Create a new recurring schedule
        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");
        
        // Create a person with exactly the same schedule time
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
        // Create a new recurring schedule on Monday
        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1000 1200");
        
        // Create a person with a one-time schedule on a Monday (15/08/22 was a Monday)
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/08/22 1100 1300")); // Overlaps
        
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        
        // Should detect a conflict
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
        assertTrue(result.hasConflict());
    }
    
    @Test
    public void checkScheduleConflict_oneTimeWithRecurring_returnsConflict() {
        // Create a new one-time schedule (15/08/22 was a Monday)
        OneTimeSchedule newSchedule = new OneTimeSchedule("15/08/22 1000 1200");
        
        // Create a person with a recurring schedule on Mondays
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
        // Create a new recurring schedule
        RecurringSchedule newSchedule = new RecurringSchedule("Monday 1200 1400");
        
        // Create a person with a schedule that ends exactly when the new one starts
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
        // Create a new one-time schedule
        OneTimeSchedule newSchedule = new OneTimeSchedule("15/10/22 1200 1400");
        
        // Create a person with a one-time schedule that ends exactly when the new one starts
        Set<RecurringSchedule> recurringSchedules = new HashSet<>();
        
        Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
        oneTimeSchedules.add(new OneTimeSchedule("15/10/22 1000 1200")); // Ends right when new one starts
        
        Person person = createTestPerson(recurringSchedules, oneTimeSchedules);
        
        // Should not detect a conflict since they're exactly adjacent
        ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(person, newSchedule);
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