package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions."
            + "Parameters: "
            + "[COMMAND]"
            + "Example: "
            + COMMAND_WORD + "add";

    public static final String GENERIC_HELP_MESSAGE = "Here are a list of basic commands you can use in FitFlow."
            + " For more information on each command, type one of the following.\n"
            + "\nhelp " + PREFIX_COMMAND + AddCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + ListCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + EditCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + FindCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + DisplayCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + ViewCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + DeleteCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + ClearCommand.COMMAND_WORD
            + "\nhelp " + PREFIX_COMMAND + ExitCommand.COMMAND_WORD;

    private final String commandRequested;

    /**
     * Creates a HelpCommand with the specified commandRequested
     */
    public HelpCommand(String commandRequested) {
        this.commandRequested = commandRequested;
    }

    @Override
    public CommandResult execute(Model model) {
        String helpMessage;

        switch (commandRequested.toLowerCase()) {
        case AddCommand.COMMAND_WORD:
            helpMessage = AddCommand.MESSAGE_USAGE;
            break;
        case ClearCommand.COMMAND_WORD:
            helpMessage = ClearCommand.MESSAGE_USAGE;
            break;
        case DeleteCommand.COMMAND_WORD:
            helpMessage = DeleteCommand.MESSAGE_USAGE;
            break;
        case DisplayCommand.COMMAND_WORD:
            helpMessage = DisplayCommand.MESSAGE_USAGE;
            break;
        case EditCommand.COMMAND_WORD:
            helpMessage = EditCommand.MESSAGE_USAGE;
            break;
        case ExitCommand.COMMAND_WORD:
            helpMessage = ExitCommand.MESSAGE_USAGE;
            break;
        case FindCommand.COMMAND_WORD:
            helpMessage = FindCommand.MESSAGE_USAGE;
            break;
        case ListCommand.COMMAND_WORD:
            helpMessage = ListCommand.MESSAGE_USAGE;
            break;
        case ViewCommand.COMMAND_WORD:
            helpMessage = ViewCommand.MESSAGE_USAGE;
            break;
        default:
            helpMessage = GENERIC_HELP_MESSAGE;
            break;
        }

        return new CommandResult(helpMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HelpCommand)) {
            return false;
        }

        HelpCommand otherAddCommand = (HelpCommand) other;
        return commandRequested.equals(otherAddCommand.commandRequested);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("commandRequested", commandRequested)
                .toString();
    }
}
