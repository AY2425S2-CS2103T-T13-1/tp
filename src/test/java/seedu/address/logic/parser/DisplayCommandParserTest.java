package seedu.address.logic.parser;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.DisplayCommand;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DisplayCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DisplayCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DisplayCommandParserTest {

    private DisplayCommandParser parser = new DisplayCommandParser();

    @Test
    public void parse_validArgs_returnsDisplayCommand() {
        assertParseSuccess(parser, "1", new DisplayCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DisplayCommand.MESSAGE_USAGE));
    }
}
