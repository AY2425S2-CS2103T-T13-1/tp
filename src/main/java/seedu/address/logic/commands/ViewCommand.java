package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
            //search recurringSchedule
            searchResult = resultGivenDay(model);
        } else {
            //search oneTimeSchedule
            searchResult = resultGivenDayOfWeek(model);
        }
        return new CommandResult(sb.append(searchResult).toString().trim());
    }

    private String resultGivenDay(Model model) {
        StringBuilder sb = new StringBuilder();
        String keyword = predicate.getKeyword();
        DayOfWeek day = DayOfWeekUtils.fromString(keyword);
        model.getFilteredPersonList().forEach(person -> {
            List<String> matchingTimes = findMatchingRecurringSchedule(person, day);
            sb.append(person.getName()).append(": ").append(String.join(", ", matchingTimes)).append("\n");
        });
        return sb.toString().trim();
    }

    private String resultGivenDayOfWeek(Model model) {
        StringBuilder sb = new StringBuilder();
        String keyword = predicate.getKeyword();
        LocalDate normalizedDate = LocalDateUtils.localDateParser(keyword);
        model.getFilteredPersonList().forEach(person -> {
            List<String> matchingTimes = findMatchingOneTimeSchedule(person, normalizedDate);
            sb.append(person.getName()).append(": ").append(String.join(", ", matchingTimes)).append("\n");
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
