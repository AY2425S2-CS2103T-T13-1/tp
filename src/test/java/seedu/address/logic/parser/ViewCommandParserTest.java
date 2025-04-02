package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewCommand;
import seedu.address.model.person.ScheduleContainsKeywordPredicate;

public class ViewCommandParserTest {

    private static final String VALID_DAY = "Monday";
    private static final String VALID_DATE = "01/02";

    private static final String INVALID_DAY_1 = "Funday";
    private static final String INVALID_DAY_2 = "123";

    private static final String INVALID_DATE_1 = "32/01"; // Day out of range
    private static final String INVALID_DATE_2 = "12/13"; // Month out of range
    private static final String INVALID_DATE_3 = "1-2"; // Wrong delimiter
    private static final String INVALID_DATE_4 = "abcd"; // Random string

    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validDay_returnsScheduleCommand() {
        ViewCommand expectedViewCommand =
                new ViewCommand(new ScheduleContainsKeywordPredicate(VALID_DAY));
        assertParseSuccess(parser, VALID_DAY, expectedViewCommand);
    }

    @Test
    public void parse_validDate_returnsScheduleCommand() {
        ViewCommand expectedViewCommand =
                new ViewCommand(new ScheduleContainsKeywordPredicate(VALID_DATE));
        assertParseSuccess(parser, VALID_DATE, expectedViewCommand);
    }

    @Test
    public void parse_invalidDay1_throwParseException() {
        assertParseFailure(parser, INVALID_DAY_1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDay2_throwParseException() {
        assertParseFailure(parser, INVALID_DAY_2,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateMonthOutOfRange_throwParseException() {
        assertParseFailure(parser, INVALID_DATE_1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateDayOutOfRange_throwParseException() {
        assertParseFailure(parser, INVALID_DATE_2,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateInvalidDelimiter_throwParseException() {
        assertParseFailure(parser, INVALID_DATE_3,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_throwParseException() {
        assertParseFailure(parser, INVALID_DATE_4,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

}
