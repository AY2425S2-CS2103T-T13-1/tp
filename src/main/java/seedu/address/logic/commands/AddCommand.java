package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_HISTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ONETIMESCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.person.ScheduleConflictDetector;
import seedu.address.model.person.ScheduleConflictResult;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a client to the application for tracking.\n\n"
            + "Format: "
            + COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_RECURRING_SCHEDULE + "[RECURRING SCHEDULE] "
            + PREFIX_ONETIMESCHEDULE + "[ONE TIME SCHEDULE] "
            + PREFIX_GOALS + "[GOALS] "
            + PREFIX_MEDICAL_HISTORY + "[MEDICAL_HISTORY] "
            + PREFIX_LOCATION + "[LOCATION] "
            + "[" + PREFIX_TAG + "TAG]...\n\n"
            + "RECURRING SCHEDULE Format: DAY HHmm HHmm\n"
            + "ONE TIME SCHEDULE Format: DD/MM[/YY] HHmm HHmm\n\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_RECURRING_SCHEDULE + "Mon 1400 1600 "
            + PREFIX_ONETIMESCHEDULE + "23/02/25 1000 1200 "
            + PREFIX_GOALS + "Lose weight, Gain muscle mass "
            + PREFIX_MEDICAL_HISTORY + "Dislocated right shoulder "
            + PREFIX_LOCATION + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_SCHEDULE_CONFLICT = 
            "Note: The person has been added, but there are schedule conflicts: %1$s";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        
        // Check for schedule conflicts with existing persons before adding
        List<String> conflicts = new ArrayList<>();
        for (Person existingPerson : model.getFilteredPersonList()) {
            conflicts.addAll(checkConflictsWithPerson(existingPerson));
        }

        model.addPerson(toAdd);
        
        // If there are conflicts, add them to the success message
        if (!conflicts.isEmpty()) {
            StringBuilder conflictsMsg = new StringBuilder();
            conflictsMsg.append(String.format(MESSAGE_SCHEDULE_CONFLICT, "")).append("\n");
            
            for (int i = 0; i < conflicts.size(); i++) {
                conflictsMsg.append(conflicts.get(i));
                if (i < conflicts.size() - 1) {
                    conflictsMsg.append("\n\n");
                }
            }
            
            conflictsMsg.append("\n\n").append(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
            return new CommandResult(conflictsMsg.toString());
        }
        
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }
    
    /**
     * Checks for schedule conflicts between the person to add and an existing person.
     */
    private List<String> checkConflictsWithPerson(Person existingPerson) {
        List<String> conflicts = new ArrayList<>();
        
        // Check recurring schedules
        for (RecurringSchedule recurringSchedule : toAdd.getRecurringSchedules()) {
            ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(existingPerson, recurringSchedule);
            if (result.hasConflict()) {
                conflicts.add(existingPerson.getName() + ": " + result.getConflictDescription());
            }
        }
        
        // Check one-time schedules
        for (OneTimeSchedule oneTimeSchedule : toAdd.getOneTimeSchedules()) {
            ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(existingPerson, oneTimeSchedule);
            if (result.hasConflict()) {
                conflicts.add(existingPerson.getName() + ": " + result.getConflictDescription());
            }
        }
        
        return conflicts;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
