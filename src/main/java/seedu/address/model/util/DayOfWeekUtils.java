package seedu.address.model.util;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.util.Arrays;

/**
 * Functions to interact with DayOfWeek.
 */
public class DayOfWeekUtils {
    public static final String DAY_OF_WEEK_REGEX = generateDayOfWeekRegex();

    /**
     * Gets the abbreviated name of the day.
     *
     * @param day The DayOfWeek enum.
     * @return The abbreviation of the day (e.g., "Mon", "Tue").
     */
    public static String getAbbreviation(DayOfWeek day) {
        return day.name().substring(0, 3).toUpperCase();
    }

    /**
     * Gets the PascalCase name of the day.
     *
     * @param day The DayOfWeek enum.
     * @return The PascalCase name of the day (e.g., "Monday").
     */
    public static String getPascalCaseName(DayOfWeek day) {
        String lowercase = day.name().toLowerCase();
        return Character.toUpperCase(lowercase.charAt(0)) + lowercase.substring(1);
    }

    /**
     * Converts a string to a {@code DayOfWeek} enum.
     * The input string is case-insensitive.
     *
     * @param day The name or abbreviation of the day (e.g., "Monday", "mon", "Mon").
     * @return The corresponding {@code DayOfWeek} enum.
     * @throws IllegalArgumentException if the input does not match any day.
     */
    public static DayOfWeek fromString(String day) {
        String normalizedDay = day.trim().toLowerCase();
        return Arrays.stream(DayOfWeek.values())
                .filter(d -> d.name().equalsIgnoreCase(normalizedDay)
                        || getAbbreviation(d).equalsIgnoreCase(normalizedDay))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid day: " + day));
    }

    /**
     * Checks if the given string is a day of the week.
     *
     * @param day The input string.
     * @return True if string is a day of the week, False otherwise.
     */
    public static boolean isDayOfWeek(String day) {
        requireNonNull(day);
        return day.matches("(?i)(" + DAY_OF_WEEK_REGEX + ")");
    }

    /**
     * Generates a regex pattern that matches all day names and abbreviations from the DayOfWeek enum.
     * Example output: "Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|Mon|Tue|Wed|Thu|Fri|Sat|Sun"
     *
     * @return A regex pattern string matching all valid day names.
     */
    private static String generateDayOfWeekRegex() {
        StringBuilder regex = new StringBuilder();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (regex.length() > 0) {
                regex.append("|");
            }
            regex.append(getPascalCaseName(day)); // Full name (e.g., "Monday")
            regex.append("|").append(getAbbreviation(day)); // Short name (e.g., "Mon")
        }
        return regex.toString();
    }

    /**
     * Converts a DayOfWeek instance to its integer value.
     *
     * @param day The DayOfWeek instance.
     * @return The numeric representation (Monday = 1, ..., Sunday = 7).
     */
    public static int valueOf(DayOfWeek day) {
        return day.ordinal() + 1;
    }
}
