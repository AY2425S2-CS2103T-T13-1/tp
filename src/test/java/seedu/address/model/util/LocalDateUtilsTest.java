package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.Year;

import org.junit.jupiter.api.Test;

public class LocalDateUtilsTest {
    @Test
    public void testFormatDateString_validInputsWithoutYear() {
        // Test for "d/m" and "dd/mm" format (should add current year)
        int currentYear = Year.now().getValue() % 100; // Last 2 digits of current year

        String date1 = "1/1";
        String expected1 = "01/01/" + currentYear;
        assertEquals(expected1, LocalDateUtils.formatDateString(date1));

        String date2 = "9/12";
        String expected2 = "09/12/" + currentYear;
        assertEquals(expected2, LocalDateUtils.formatDateString(date2));
    }

    @Test
    public void testFormatDateString_validInputsWithYear() {
        // Test for "d/m/yy" format
        String date1 = "1/1/23";
        String expected1 = "01/01/23";
        assertEquals(expected1, LocalDateUtils.formatDateString(date1));

        String date2 = "15/7/45";
        String expected2 = "15/07/45";
        assertEquals(expected2, LocalDateUtils.formatDateString(date2));
    }

    @Test
    public void testFormatDateString_withWhitespaces() {
        // Test with leading/trailing whitespaces
        int currentYear = Year.now().getValue() % 100;

        String date1 = " 2 / 2 ";
        String expected1 = "02/02/" + currentYear;
        assertEquals(expected1, LocalDateUtils.formatDateString(date1));

        String date2 = "10/ 11 /21 ";
        String expected2 = "10/11/21";
        assertEquals(expected2, LocalDateUtils.formatDateString(date2));
    }

    @Test
    public void testFormatDateString_nullInput() {
        // Test for null input (should throw NullPointerException)
        assertThrows(NullPointerException.class, () -> {
            LocalDateUtils.formatDateString(null);
        });
    }

    @Test
    public void testLocalDateParser_validDates() {
        // Test for "dd/MM/yy" format
        String date1 = "01/01/23"; // Should parse as 1st Jan 2023
        LocalDate expectedDate1 = LocalDate.of(2023, 1, 1);
        assertEquals(expectedDate1, LocalDateUtils.localDateParser(date1));

        String date2 = "25/12/25"; // Should parse as 25th Dec 2025
        LocalDate expectedDate2 = LocalDate.of(2025, 12, 25);
        assertEquals(expectedDate2, LocalDateUtils.localDateParser(date2));
    }

    @Test
    public void testLocalDateParser_invalidDates() {
        // Test invalid date formats (should throw IllegalArgumentException)
        String invalidDate1 = "31-12-2025"; // Wrong separator
        assertThrows(IllegalArgumentException.class, () -> {
            LocalDateUtils.localDateParser(invalidDate1);
        });

        String invalidDate2 = "32/01/23"; // Invalid day
        assertThrows(IllegalArgumentException.class, () -> {
            LocalDateUtils.localDateParser(invalidDate2);
        });

        String invalidDate3 = "00/00/00"; // Invalid date
        assertThrows(IllegalArgumentException.class, () -> {
            LocalDateUtils.localDateParser(invalidDate3);
        });
    }
}
