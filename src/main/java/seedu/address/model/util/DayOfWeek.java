package seedu.address.model.util;

import static java.util.Objects.requireNonNull;

/**
 * Represents the days of the week.
 */
public enum DayOfWeek {
    MONDAY("Mon", "Monday"),
    TUESDAY("Tue", "Tuesday"),
    WEDNESDAY("Wed", "Wednesday"),
    THURSDAY("Thu", "Thursday"),
    FRIDAY("Fri", "Friday"),
    SATURDAY("Sat", "Saturday"),
    SUNDAY("Sun", "Sunday");

    public static final String DAY_OF_WEEK_REGEX = generateDayOfWeekRegex();

    private final String abbreviation;
    private final String pascalCaseName;
    /**
     * Constructs a {@code DayOfWeek} with the given abbreviation.
     *
     * @param abbreviation The short form of the day (e.g., "Mon" for Monday).
     * @param pascalCaseName The PascalCase form of the day (e.g., "Monday").
     */
    DayOfWeek(String abbreviation, String pascalCaseName) {
        this.abbreviation = abbreviation;
        this.pascalCaseName = pascalCaseName;
    }

    /**
     * Gets the abbreviated name of the day.
     *
     * @return The abbreviation of the day (e.g., "Mon", "Tue").
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Gets the PascalCase name of the day.
     *
     * @return The PascalCase name of the day (e.g., "Monday").
     */
    public String getPascalCaseName() {
        return pascalCaseName;
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
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.name().equalsIgnoreCase(normalizedDay)
                    || dayOfWeek.getAbbreviation().equalsIgnoreCase(normalizedDay)) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Invalid day: " + day);
    }

    /**
     * Checks if the given string is a day of the week.
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
            regex.append(day.getPascalCaseName()); // Full name (e.g., "Monday")
            regex.append("|").append(day.getAbbreviation()); // Short name (e.g., "Mon")
        }
        return regex.toString();
    }

    /**
     * Returns the DayOfWeek as a String in PascalCase (e.g. "Monday")
     */
    @Override
    public String toString() {
        return pascalCaseName;
    }
}
