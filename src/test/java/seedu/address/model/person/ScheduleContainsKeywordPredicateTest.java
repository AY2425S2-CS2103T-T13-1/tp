package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ScheduleContainsKeywordPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "Monday";
        String secondPredicateKeyword = "01/02";

        ScheduleContainsKeywordPredicate firstPredicate = new ScheduleContainsKeywordPredicate(firstPredicateKeyword);
        ScheduleContainsKeywordPredicate secondPredicate = new ScheduleContainsKeywordPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ScheduleContainsKeywordPredicate firstPredicateCopy =
                new ScheduleContainsKeywordPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_scheduleContainsKeyword_returnsTrue() {
        // Correct day
        ScheduleContainsKeywordPredicate predicate = new ScheduleContainsKeywordPredicate("Tuesday");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice")
                .withRecurringSchedules("Tuesday 1200 1400").build()));

        // Correct date
        predicate = new ScheduleContainsKeywordPredicate("27/04");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice")
                .withOneTimeSchedules("27/04 1200 1400").build()));

        // Abbreviated day
        predicate = new ScheduleContainsKeywordPredicate("mon");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice")
                .withRecurringSchedules("Monday 1200 1400").build()));

        // Abbreviated date
        predicate = new ScheduleContainsKeywordPredicate("2/4");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice")
                .withOneTimeSchedules("02/04 1200 1400").build()));
    }

    @Test
    public void test_scheduleDoesNotContainDay_returnsFalse() {
        // Non-matching day
        ScheduleContainsKeywordPredicate predicate = new ScheduleContainsKeywordPredicate("Tuesday");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice")
                .withRecurringSchedules("Monday 1200 1400").build()));
    }

    @Test
    public void test_scheduleDoesNotContainDate_returnsFalse() {
        // Non-matching date
        ScheduleContainsKeywordPredicate predicate = new ScheduleContainsKeywordPredicate("12/6");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice")
                .withOneTimeSchedules("02/04 1200 1400").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "Wednesday";
        ScheduleContainsKeywordPredicate predicate = new ScheduleContainsKeywordPredicate(keyword);

        String expected = ScheduleContainsKeywordPredicate.class.getCanonicalName() + "{keyword=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
