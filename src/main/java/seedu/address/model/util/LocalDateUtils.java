package seedu.address.model.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Functions to interact with LocalDate.
 */
public class LocalDateUtils {
    public static final String DAY_REGEX = "[0-9]|[0-2][0-9]|3[0-1]";
    public static final String MONTH_REGEX = "[0-9]|0[1-9]|1[0-2]";
    public static final String YEAR_REGEX = "\\d{2}";
    public static final String DATE_REGEX = generateDateRegex();

    /**
     * Formats a {@code String date} and returns a normalized date {@code String}.
     * Single-digit days and months will be padded with a leading zero. Leading and
     * trailing whitespaces will be trimmed if present.
     * Expected input format: {@code "[d]d/[m]m"} or {@code "[d]d/[m]m/yy"}
     *
     * @param date the date {@code String} to be formatted and normalized; must not be {@code null}.
     * @return a normalized date {@code String} in the format {@code "dd/MM/yy"}.
     * @throws NullPointerException if the given {@code date} is {@code null}.
     */
    public static String formatDateString(String date) {
        requireNonNull(date);
        String[] dateComponents = date.split("/");

        String trimmedDay = dateComponents[0].trim();
        String trimmedMonth = dateComponents[1].trim();
        // Pad with leading zeros if necessary
        String normalizedDay = trimmedDay.length() == 1 ? "0" + trimmedDay : trimmedDay;
        String normalizedMonth = trimmedMonth.length() == 1 ? "0" + trimmedMonth : trimmedMonth;

        // Handle optional year part (if it's there)
        String normalizedDate;
        if (dateComponents.length == 3) {
            String trimmedYear = dateComponents[2].trim();
            normalizedDate = normalizedDay + "/" + normalizedMonth + "/" + trimmedYear;
        } else {
            normalizedDate = normalizedDay + "/" + normalizedMonth + "/"
                    + LocalDate.now().getYear() % 100; //Last 2 digits
        }
        return normalizedDate;
    }

    /**
     * Formats a {@code String date} and returns a LocalDate date {@link LocalDate}.
     * It accepts input in the format {@code "[d]d/[m]m/yy"},
     *
     * @param date the date {@code String} to parse; must not be {@code null}.
     * @return a LocalDate date {@link LocalDate} representing the parsed date.
     */
    public static LocalDate localDateParser(String date) {
        String normalizedDate = formatDateString(date);
        List<DateTimeFormatter> inputFormats = List.of(
                DateTimeFormatter.ofPattern("dd/MM/yy")
        );

        for (DateTimeFormatter formatter : inputFormats) {
            try {
                return LocalDate.parse(normalizedDate, formatter);
            } catch (Exception e) {
                // try next format
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + date); // Will never throw
    }

    /**
     * Checks if the given date string matches the expected date format.
     * The expected format is {@code "dd/MM"} or {@code "dd/MM/yy"}, where:
     *   {@code dd} is a one or two-digit day (01-31).
     *   {@code MM} is a one or two-digit month (01-12).
     *   {@code yy} is an optional two-digit year.
     *
     * @param date the date string to validate.
     * @return {@code true} if the date string matches the expected format, {@code false} otherwise.
     */
    public static boolean isValidDateString(String date) {
        requireNonNull(date);
        return date.matches(DATE_REGEX);
    }

    public static String generateDateRegex() {
        return "(" + DAY_REGEX + ")/(" + MONTH_REGEX + ")(/" + YEAR_REGEX + ")?";
    }

    public static String toString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }
}
