package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.util.DayOfWeekUtils.isDayOfWeek;
import static seedu.address.model.util.LocalDateUtils.isValidDateString;

import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ScheduleContainsKeywordPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        String trimmedArg = args.trim();
        if (trimmedArg.isEmpty() || !(isDayOfWeek(trimmedArg) || isValidDateString(trimmedArg))) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }
        return new ViewCommand(new ScheduleContainsKeywordPredicate(trimmedArg));
    }

}
