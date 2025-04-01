package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class OneTimeScheduleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OneTimeSchedule(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidOneTimeSchedule = "invalid schedule";
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule(invalidOneTimeSchedule));
    }

    @Test
    public void isValidOneTimeSchedule() {
        // null one time schedule
        assertThrows(NullPointerException.class, () -> seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule(null));

        // invalid one time schedule
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("")); // empty string
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule(" ")); // spaces only
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("^")); // only non-alphanumeric characters
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("peter*")); // contains non-alphanumeric characters
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("2/2 1000 12")); // wrong time
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("3/2 1000 1290")); // wrong date
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("34/10 1000 1200")); // wrong date

        // valid one time schedule
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("12/12 1000 1200"));
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("31/3 1000 1100"));
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("1/1 1000 1100"));
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("31/3/25 1000 1100"));
    }
    @Test
    public void constructor_initializesDateCorrectly() {
        OneTimeSchedule schedule = new OneTimeSchedule("15/10 1000 1200");
        LocalDate expectedDate = LocalDate.of(LocalDate.now().getYear(), 10, 15);
        assertEquals(expectedDate, schedule.getDate());
    }

    @Test
    public void getDateString_returnsCorrectFormat() {
        OneTimeSchedule schedule = new OneTimeSchedule("15/10 1000 1200");
        assertEquals("15/10/25", schedule.getDateString());
    }

    @Test
    public void extractDate_invalidInput_throwsException() {
        // Testing the defensive coding for extractDate method indirectly through constructor
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule("XX/YY 0900 1700"));
    }
    @Test
    public void extractStartTime_invalidInput_throwsException() {
        // A valid date but invalid start time format
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule("15/10 ABCD 1700"));
    }
    @Test
    public void extractEndTime_invalidInput_throwsException() {
        // A valid date and start time but invalid end time format
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule("15/10 0900 WXYZ"));
    }
    @Test
    public void malformedInput_throwsException() {
        // Testing that improperly formatted inputs throw exceptions as expected
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule("15/10/20231000 1200"));
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule("15/10 0900"));
        assertThrows(IllegalArgumentException.class, () -> new OneTimeSchedule(" "));
    }
    @Test
    public void toString_formatsCorrectly() {
        OneTimeSchedule schedule = new OneTimeSchedule("15/10 1000 1200");
        assertEquals("[15/10/25 1000 1200]", schedule.toString());
    }
    @Test
    public void equals_sameValues_returnsTrue() {
        OneTimeSchedule schedule1 = new OneTimeSchedule("15/10 1000 1200");
        OneTimeSchedule schedule2 = new OneTimeSchedule("15/10 1000 1200");
        assertTrue(schedule1.equals(schedule2));
    }
    @Test
    public void equals_differentValues_returnsFalse() {
        OneTimeSchedule schedule1 = new OneTimeSchedule("15/10 1000 1200");
        // Different date
        OneTimeSchedule schedule2 = new OneTimeSchedule("16/10 1000 1200");
        assertFalse(schedule1.equals(schedule2));
        // Different start time
        OneTimeSchedule schedule3 = new OneTimeSchedule("15/10 1100 1200");
        assertFalse(schedule1.equals(schedule3));
        // Different end time
        OneTimeSchedule schedule4 = new OneTimeSchedule("15/10 1000 1300");
        assertFalse(schedule1.equals(schedule4));
    }
}
