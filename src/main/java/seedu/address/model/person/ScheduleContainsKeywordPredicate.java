package seedu.address.model.person;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.util.DayOfWeekUtils;
import seedu.address.model.util.LocalDateUtils;

/**
 * Tests that a {@code Person}'s {@code Schedule} matches any of the keywords given.
 */
public class ScheduleContainsKeywordPredicate implements Predicate<Person> {
    private final String keyword;
    private final DayOfWeek dayToFind;
    private final LocalDate dateToFind;

    /**
     * Constructs a {@code ScheduleContainsKeywordPredicate} with the specified keyword.
     * If the keyword is an abbreviated form of a weekday (e.g., "mon", "tue"),
     * it is automatically expanded to the full day name (e.g., "monday", "tuesday").
     * Otherwise, the keyword remains unchanged.
     *
     * @param keyword The keyword to match against a person's schedules.
     */
    public ScheduleContainsKeywordPredicate(String keyword) {
        this.keyword = keyword;
        if (DayOfWeekUtils.isDayOfWeek(keyword)) {
            this.dayToFind = DayOfWeekUtils.fromString(keyword);
            this.dateToFind = calculateNextDateForDay(this.dayToFind);
        } else {
            this.dateToFind = LocalDateUtils.localDateParser(keyword);
            this.dayToFind = this.dateToFind.getDayOfWeek();
        }
    }

    private LocalDate calculateNextDateForDay(DayOfWeek day) {
        LocalDate today = LocalDate.now();
        int daysUntilTarget = (day.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
        return today.plusDays(daysUntilTarget);
    }

    public String getKeyword() {
        return keyword;
    }

    public DayOfWeek getDayToFind() {
        return dayToFind;
    }

    public LocalDate getDateToFind() {
        return dateToFind;
    }

    @Override
    public boolean test(Person person) {
        String searchDate = LocalDateUtils.toString(dateToFind);
        String searchDay = dayToFind.toString();
        boolean oneTimeScheduleMatches = person.getOneTimeSchedules().stream()
                .anyMatch(schedule -> StringUtil.containsWordIgnoreCase(
                        schedule.getDateString(), searchDate));
        boolean recurringScheduleMatches = person.getRecurringSchedules().stream()
                .anyMatch(schedule -> StringUtil.containsWordIgnoreCase(
                        String.valueOf(schedule.getDay()), searchDay));
        return oneTimeScheduleMatches || recurringScheduleMatches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ScheduleContainsKeywordPredicate)) {
            return false;
        }

        ScheduleContainsKeywordPredicate otherScheduleContainsKeywordPredicate =
                (ScheduleContainsKeywordPredicate) other;
        return keyword.equals(otherScheduleContainsKeywordPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
