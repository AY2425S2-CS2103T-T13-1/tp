package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.person.OneTimeSchedule.formatDate;

import java.util.List;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.person.ScheduleContainsKeywordPredicate;
import seedu.address.model.util.DayOfWeek;

/**
 * Finds and lists all persons in address book who has sessions that matches any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose sessions contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: DAY/DATE \n"
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
        if (DayOfWeek.isDayOfWeek(keyword)) {
            //search recurringSchedule
            model.getFilteredPersonList().forEach(person -> {
                List<String> matchingTimes = findMatchingRecurringSchedule(person, keyword);
                sb.append(person.getName()).append(": ").append(String.join(", ", matchingTimes)).append("\n");
            });
        } else {
            //search oneTimeSchedule
            String normalizedDate = formatDate(keyword);
            model.getFilteredPersonList().forEach(person -> {
                List<String> matchingTimes = findMatchingOneTimeSchedule(person, normalizedDate);
                sb.append(person.getName()).append(": ").append(String.join(", ", matchingTimes)).append("\n");
            });
        }
        return new CommandResult(sb.toString().trim());
    }

    private List<String> findMatchingRecurringSchedule(Person person, String keyword) {
        Set<RecurringSchedule> recurringSchedules = person.getRecurringSchedules();
        List<String> matchingTimes = recurringSchedules.stream()
                .filter(schedule -> String.valueOf(schedule.getDay()).equalsIgnoreCase(keyword))
                .map(schedule -> String.format("%s-%s", schedule.getStartTime(),
                        schedule.getEndTime()))
                .toList();
        return matchingTimes;
    }

    private List<String> findMatchingOneTimeSchedule(Person person, String keyword) {
        Set<OneTimeSchedule> oneTimeSchedules = person.getOneTimeSchedules();
        List<String> matchingTimes = oneTimeSchedules.stream()
                .filter(schedule -> schedule.getDate().equalsIgnoreCase(keyword))
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
