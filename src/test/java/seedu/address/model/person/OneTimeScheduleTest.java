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
        assertFalse(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("3/13 1000 1200")); // wrong date

        // valid one time date
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("2/2 1000 1200")); // d/m HHmm HHmm
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("02/2 1000 1200")); // dd/m HHmm HHmm
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("2/02 1000 1200")); // d/mm HHmm HHmm
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("02/02 1000 1200")); // dd/mm HHmm HHmm
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("2/2/25 1000 1200")); // d/m/yy HHmm HHmm
        assertTrue(seedu.address.model.person
                .OneTimeSchedule.isValidOneTimeSchedule("02/02/25 1000 1200")); // dd/mm/yy HHmm HHmm
    }

    @Test
    void formatDate_validInputs_shouldReturnNormalizedDate() {
        // Test without year
        assertEquals("01/01/25", OneTimeSchedule.formatDate("1/1"));
        assertEquals("09/12/25", OneTimeSchedule.formatDate("9/12"));
        assertEquals("31/10/25", OneTimeSchedule.formatDate("31/10"));

        // Test with year
        assertEquals("01/01/23", OneTimeSchedule.formatDate("1/1/23"));
        assertEquals("09/12/99", OneTimeSchedule.formatDate("9/12/99"));
        assertEquals("31/10/20", OneTimeSchedule.formatDate("31/10/20"));

        // Test with already normalized input
        assertEquals("01/01/25", OneTimeSchedule.formatDate("01/01"));
        assertEquals("31/12/23", OneTimeSchedule.formatDate("31/12/23"));
    }

    @Test
    void formatDate_nullInput_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> OneTimeSchedule.formatDate(null));
    }

    @Test
    public void equals() {
        OneTimeSchedule oneTimeSchedule = new OneTimeSchedule("2/2 1000 1200");

        // same values -> returns true
        assertTrue(oneTimeSchedule.equals(new OneTimeSchedule("2/2 1000 1200")));

        // same object -> returns true
        assertTrue(oneTimeSchedule.equals(oneTimeSchedule));

        // null -> returns false
        assertFalse(oneTimeSchedule.equals(null));

        // different types -> returns false
        assertFalse(oneTimeSchedule.equals(5.0f));

        // different date -> returns false
        assertFalse(oneTimeSchedule.equals(new OneTimeSchedule("3/2 1000 1200"))); // Date differs

        // different start time -> returns false
        assertFalse(oneTimeSchedule.equals(new OneTimeSchedule("2/2 0900 1200"))); // Start time differs

        // different end time -> returns false
        assertFalse(oneTimeSchedule.equals(new OneTimeSchedule("2/2 1000 1300"))); // End time differs
    }

    @Test
    public void testLocalDateParser_validDates() {
        // Test for "dd/MM/yy" format
        String date2 = "25/12/25"; // Should parse as 25th Dec 2025
        LocalDate expectedDate2 = LocalDate.of(2025, 12, 25);
        assertEquals(expectedDate2, OneTimeSchedule.localDatePaser(date2));
    }

    @Test
    public void testLocalDateParser_invalidDate() {
        String invalidDate = "31-12-2025"; // Invalid format
        assertThrows(IllegalArgumentException.class, () -> {
            OneTimeSchedule.localDatePaser(invalidDate);
        });
    }
}
