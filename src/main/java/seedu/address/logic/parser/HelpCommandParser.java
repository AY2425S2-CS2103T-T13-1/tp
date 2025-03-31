package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a HelpCommand object
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_COMMAND);
        if (argMultimap.getValue(PREFIX_COMMAND).isEmpty()) {
            return new HelpCommand("", false);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COMMAND);
        String commandRequested = argMultimap.getValue(PREFIX_COMMAND).orElse("");

        return new HelpCommand(commandRequested, true);
    }
}
