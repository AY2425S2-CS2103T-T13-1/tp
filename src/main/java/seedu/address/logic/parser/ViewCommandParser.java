package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.util.DayOfWeekUtils.isDayOfWeek;

import java.util.regex.Pattern;

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
        if (trimmedArg.isEmpty() || !(isDayOfWeek(trimmedArg) || isValidDate(trimmedArg))) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }
        return new ViewCommand(new ScheduleContainsKeywordPredicate(trimmedArg));
    }
    /**
     * Checks if a given date string follows the format "DD/MM" or "DD/MM/YY".
     *
     * - DD: 01-31 (valid day range)
     * - MM: 01-12 (valid month range)
     * - YY (optional): Any two-digit year
     *
     * @param date The date string to validate.
     * @return {@code true} if the date follows the expected format, otherwise {@code false}.
     */
    private static boolean isValidDate(String date) {
        requireNonNull(date);

        // Regular expression to match "DD/MM" or "DD/MM/YY" format
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])(\\/\\d{2})?$";

        // Compile the pattern and match it against the input date
        return Pattern.matches(regex, date);
    }

}
