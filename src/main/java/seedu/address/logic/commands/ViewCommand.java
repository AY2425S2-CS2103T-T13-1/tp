package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
 * Keyword matching is case-insensitive.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients whose sessions contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n\n"
            + "Format: "
            + COMMAND_WORD + " DAY/DATE\n\n"
            + "Example: "
            + COMMAND_WORD + " Monday\n"
            + COMMAND_WORD + " Tue\n"
            + COMMAND_WORD + " 5/6\n"
            + COMMAND_WORD + "15/06";

    private final ScheduleContainsKeywordPredicate predicate;

    public ViewCommand(ScheduleContainsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        String keyword = predicate.getKeyword();
        String messageHeader = buildMessageHeader(keyword);

        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(messageHeader + "No clients found!");
        }

        String searchResult = fetchSearchResult(model, keyword);
        return new CommandResult(messageHeader + searchResult);
    }

    private String buildMessageHeader(String keyword) {
        return String.format(Messages.MESSAGE_SCHEDULES_LISTED, keyword) + "\n\n";
    }

    private String fetchSearchResult(Model model, String keyword) {
        LocalDate date = predicate.getDateToFind();
        return DayOfWeekUtils.isDayOfWeek(keyword)
                ? resultGiven(model, predicate.getDayToFind(), date)
                : resultGiven(model, date.getDayOfWeek(), date);
    }

    private String resultGiven(Model model, DayOfWeek day, LocalDate date) {
        AtomicInteger index = new AtomicInteger(1);

        return model.getFilteredPersonList().stream()
                .map(person -> formatPersonSchedule(person, day, date, index))
                .collect(Collectors.joining(""))
                .trim();
    }

    private String formatPersonSchedule(Person person, DayOfWeek day, LocalDate targetDate, AtomicInteger index) {
        List<String> recurringTimes = findMatchingRecurringSchedule(person, day);
        List<String> oneTimeTimes = findMatchingOneTimeSchedule(person, targetDate);
        List<String> allTimes = new ArrayList<>();
        allTimes.addAll(recurringTimes);
        allTimes.addAll(oneTimeTimes);

        return String.format("%d. %s: %s\n", index.getAndIncrement(), person.getName(),
                String.join(", ", allTimes));
    }

    private List<String> findMatchingRecurringSchedule(Person person, DayOfWeek day) {
        return person.getRecurringSchedules().stream()
                .filter(schedule -> String.valueOf(schedule.getDay())
                                                        .equalsIgnoreCase(day.toString()))
                .map(schedule -> String.format("%s-%s", schedule.getStartTime(),
                        schedule.getEndTime()))
                .toList();
    }

    private List<String> findMatchingOneTimeSchedule(Person person, LocalDate date) {
        return person.getOneTimeSchedules().stream()
                .filter(schedule -> schedule.getDateString()
                                .equalsIgnoreCase(LocalDateUtils.toString(date)))
                .map(schedule -> String.format("%s-%s", schedule.getStartTime(),
                        schedule.getEndTime()))
                .toList();
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
