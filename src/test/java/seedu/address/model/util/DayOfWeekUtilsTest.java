package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

public class DayOfWeekUtilsTest {
    @Test
    void testFromString_validFullName() {
        assertEquals(DayOfWeek.MONDAY, DayOfWeekUtils.fromString("Monday"));
        assertEquals(DayOfWeek.TUESDAY, DayOfWeekUtils.fromString("Tuesday"));
        assertEquals(DayOfWeek.WEDNESDAY, DayOfWeekUtils.fromString("Wednesday"));
        assertEquals(DayOfWeek.THURSDAY, DayOfWeekUtils.fromString("Thursday"));
        assertEquals(DayOfWeek.FRIDAY, DayOfWeekUtils.fromString("Friday"));
        assertEquals(DayOfWeek.SATURDAY, DayOfWeekUtils.fromString("Saturday"));
        assertEquals(DayOfWeek.SUNDAY, DayOfWeekUtils.fromString("Sunday"));
    }

    @Test
    void testFromString_validAbbreviations() {
        assertEquals(DayOfWeek.MONDAY, DayOfWeekUtils.fromString("Mon"));
        assertEquals(DayOfWeek.TUESDAY, DayOfWeekUtils.fromString("Tue"));
        assertEquals(DayOfWeek.WEDNESDAY, DayOfWeekUtils.fromString("Wed"));
        assertEquals(DayOfWeek.THURSDAY, DayOfWeekUtils.fromString("Thu"));
        assertEquals(DayOfWeek.FRIDAY, DayOfWeekUtils.fromString("Fri"));
        assertEquals(DayOfWeek.SATURDAY, DayOfWeekUtils.fromString("Sat"));
        assertEquals(DayOfWeek.SUNDAY, DayOfWeekUtils.fromString("Sun"));
    }

    @Test
    void testFromString_caseInsensitive() {
        assertEquals(DayOfWeek.MONDAY, DayOfWeekUtils.fromString("monday"));
        assertEquals(DayOfWeek.TUESDAY, DayOfWeekUtils.fromString("tueSDAY"));
        assertEquals(DayOfWeek.WEDNESDAY, DayOfWeekUtils.fromString("WeD"));
        assertEquals(DayOfWeek.THURSDAY, DayOfWeekUtils.fromString("thu"));
        assertEquals(DayOfWeek.FRIDAY, DayOfWeekUtils.fromString("fRI"));
    }

    @Test
    void testFromString_invalidValues() {
        assertThrows(IllegalArgumentException.class, () -> DayOfWeekUtils.fromString("Notaday"));
        assertThrows(IllegalArgumentException.class, () -> DayOfWeekUtils.fromString("Mond"));
        assertThrows(IllegalArgumentException.class, () -> DayOfWeekUtils.fromString("Mo"));
        assertThrows(IllegalArgumentException.class, () -> DayOfWeekUtils.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> DayOfWeekUtils.fromString("123"));
    }

    @Test
    void testIsDayOfWeek_validInputs() {
        assertTrue(DayOfWeekUtils.isDayOfWeek("Monday"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("mon"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("TUESDAY"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("wed"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("Thu"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("fri"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("saturday"));
        assertTrue(DayOfWeekUtils.isDayOfWeek("Sun"));
    }

    @Test
    void testIsDayOfWeek_invalidInputs() {
        assertFalse(DayOfWeekUtils.isDayOfWeek("Mo"));
        assertFalse(DayOfWeekUtils.isDayOfWeek("Mond"));
        assertFalse(DayOfWeekUtils.isDayOfWeek("Notaday"));
        assertFalse(DayOfWeekUtils.isDayOfWeek("123"));
        assertFalse(DayOfWeekUtils.isDayOfWeek(""));
        assertFalse(DayOfWeekUtils.isDayOfWeek("mon1"));
    }

    @Test
    void testGetAbbreviation() {
        assertEquals("MON", DayOfWeekUtils.getAbbreviation(DayOfWeek.MONDAY));
        assertEquals("TUE", DayOfWeekUtils.getAbbreviation(DayOfWeek.TUESDAY));
        assertEquals("WED", DayOfWeekUtils.getAbbreviation(DayOfWeek.WEDNESDAY));
        assertEquals("THU", DayOfWeekUtils.getAbbreviation(DayOfWeek.THURSDAY));
        assertEquals("FRI", DayOfWeekUtils.getAbbreviation(DayOfWeek.FRIDAY));
        assertEquals("SAT", DayOfWeekUtils.getAbbreviation(DayOfWeek.SATURDAY));
        assertEquals("SUN", DayOfWeekUtils.getAbbreviation(DayOfWeek.SUNDAY));
    }

    @Test
    void testGetPascalCaseName() {
        assertEquals("Monday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.MONDAY));
        assertEquals("Tuesday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.TUESDAY));
        assertEquals("Wednesday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.WEDNESDAY));
        assertEquals("Thursday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.THURSDAY));
        assertEquals("Friday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.FRIDAY));
        assertEquals("Saturday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.SATURDAY));
        assertEquals("Sunday", DayOfWeekUtils.getPascalCaseName(DayOfWeek.SUNDAY));
    }

    @Test
    void testValueOf() {
        assertEquals(1, DayOfWeekUtils.valueOf(DayOfWeek.MONDAY));
        assertEquals(2, DayOfWeekUtils.valueOf(DayOfWeek.TUESDAY));
        assertEquals(3, DayOfWeekUtils.valueOf(DayOfWeek.WEDNESDAY));
        assertEquals(4, DayOfWeekUtils.valueOf(DayOfWeek.THURSDAY));
        assertEquals(5, DayOfWeekUtils.valueOf(DayOfWeek.FRIDAY));
        assertEquals(6, DayOfWeekUtils.valueOf(DayOfWeek.SATURDAY));
        assertEquals(7, DayOfWeekUtils.valueOf(DayOfWeek.SUNDAY));
    }
}
