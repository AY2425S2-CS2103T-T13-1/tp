package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.person.ScheduleContainsKeywordPredicate;
import seedu.address.model.util.DayOfWeekUtils;
import seedu.address.model.util.LocalDateUtils;

/**
 * Finds and lists all persons in address book who has sessions that matches any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose sessions contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n\n"
            + "Format: "
            + COMMAND_WORD + " DAY/DATE\n\n"
            + "Example: " + COMMAND_WORD + " Monday\n"
            + "Example: " + COMMAND_WORD + " Tue\n"
            + "Example: " + COMMAND_WORD + " 15/06";

    private final ScheduleContainsKeywordPredicate predicate;

    public ViewCommand(ScheduleContainsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        String keyword = predicate.getKeyword();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_SCHEDULES_LISTED, keyword)).append("\n\n");
        if (model.getFilteredPersonList().isEmpty()) {
            sb.append("No clients found!");
            return new CommandResult(sb.toString().trim());
        }
        String searchResult;
        if (DayOfWeekUtils.isDayOfWeek(keyword)) {
            searchResult = resultGivenDay(model);
        } else {
            searchResult = resultGivenDate(model);
        }
        return new CommandResult(sb.append(searchResult).toString().trim());
    }

    private String resultGivenDay(Model model) {
        StringBuilder sb = new StringBuilder();
        DayOfWeek day = predicate.getDayToFind();
        LocalDate todayDate = LocalDate.now();
        int daysUntilTarget = (day.getValue() - todayDate.getDayOfWeek().getValue() + 7) % 7;
        LocalDate targetDate = todayDate.plusDays(daysUntilTarget);
        AtomicInteger index = new AtomicInteger(1);

        model.getFilteredPersonList().forEach(person -> {
            List<String> recurringTimes = findMatchingRecurringSchedule(person, day);
            List<String> oneTimeTimes = findMatchingOneTimeSchedule(person, targetDate);
            List<String> allTimes = new ArrayList<>();
            allTimes.addAll(recurringTimes);
            allTimes.addAll(oneTimeTimes);
            sb.append(index.getAndIncrement()).append(". ").append(person.getName()).append(": ")
                    .append(String.join(", ", allTimes)).append("\n");
        });
        return sb.toString().trim();
    }

    private String resultGivenDate(Model model) {
        StringBuilder sb = new StringBuilder();
        LocalDate normalizedDate = predicate.getDateToFind();
        DayOfWeek targetDayOfWeek = normalizedDate.getDayOfWeek();
        AtomicInteger index = new AtomicInteger(1);

        model.getFilteredPersonList().forEach(person -> {
            List<String> oneTimeTimes = findMatchingOneTimeSchedule(person, normalizedDate);
            List<String> recurringTimes = findMatchingRecurringSchedule(person, targetDayOfWeek);
            List<String> allTimes = new ArrayList<>();
            allTimes.addAll(oneTimeTimes);
            allTimes.addAll(recurringTimes);
            sb.append(index.getAndIncrement()).append(". ").append(person.getName()).append(": ")
                    .append(String.join(", ", allTimes)).append("\n");
        });
        return sb.toString().trim();
    }

    private List<String> findMatchingRecurringSchedule(Person person, DayOfWeek day) {
        String searchDay = day.toString();
        Set<RecurringSchedule> recurringSchedules = person.getRecurringSchedules();
        List<String> matchingTimes = recurringSchedules.stream()
                .filter(schedule -> String.valueOf(schedule.getDay()).equalsIgnoreCase(searchDay))
                .map(schedule -> String.format("%s-%s", schedule.getStartTime(),
                        schedule.getEndTime()))
                .toList();
        return matchingTimes;
    }

    private List<String> findMatchingOneTimeSchedule(Person person, LocalDate date) {
        String searchDate = LocalDateUtils.toString(date);
        Set<OneTimeSchedule> oneTimeSchedules = person.getOneTimeSchedules();
        List<String> matchingTimes = oneTimeSchedules.stream()
                .filter(schedule -> schedule.getDateString().equalsIgnoreCase(searchDate))
                .map(schedule -> String.format("%s-%s", schedule.getStartTime(),
                        schedule.getEndTime()))
                .toList();
        return matchingTimes;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand otherViewCommand = (ViewCommand) other;
        return predicate.equals(otherViewCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
