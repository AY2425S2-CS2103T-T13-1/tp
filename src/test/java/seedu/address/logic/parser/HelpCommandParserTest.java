package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HelpCommand;

public class HelpCommandParserTest {

    private HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_genericCommand_returnsGenericCommand() {
        assertParseSuccess(parser, "", new HelpCommand("", false));
    }

    @Test
    public void parse_validSpecificCommand_returnsSpecificMessageCommand() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " /add", new HelpCommand("add", true));
    }

    @Test
    public void parse_invalidSpecificCommand_returnsInvalidMessageCommand() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " /aslljhv", new HelpCommand("aslljhv", true));
    }
}
