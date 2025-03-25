package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ScheduleContainsKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code ViewCommand}.
 */
public class ViewCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ScheduleContainsKeywordPredicate firstPredicate =
                new ScheduleContainsKeywordPredicate("monday");
        ScheduleContainsKeywordPredicate secondPredicate =
                new ScheduleContainsKeywordPredicate("tuesday");

        ViewCommand findFirstCommand = new ViewCommand(firstPredicate);
        ViewCommand findSecondCommand = new ViewCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        ViewCommand findFirstCommandCopy = new ViewCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_dayKeyword_noPersonsFound() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_SCHEDULES_LISTED, "Sunday")).append("\n\n");
        sb.append("No clients found!");
        String expectedMessage = sb.toString().trim();
        ScheduleContainsKeywordPredicate predicate = preparePredicate("Sunday");
        ViewCommand command = new ViewCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_dayKeyword_multiplePersonsFound() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_SCHEDULES_LISTED, "Monday")).append("\n\n");
        sb.append("Alice Pauline: 1400-1600\n").append("Benson Meier: 1400-1600\n").append("Carl Kurz: 1400-1600\n");
        String expectedMessage = sb.toString().trim();
        ScheduleContainsKeywordPredicate predicate = preparePredicate("Monday");
        ViewCommand command = new ViewCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_dayTruncatedKeyword_multiplePersonsFound() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_SCHEDULES_LISTED, "mon")).append("\n\n");
        sb.append("Alice Pauline: 1400-1600\n").append("Benson Meier: 1400-1600\n").append("Carl Kurz: 1400-1600\n");
        String expectedMessage = sb.toString().trim();
        ScheduleContainsKeywordPredicate predicate = preparePredicate("mon");
        ViewCommand command = new ViewCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_dateKeyword_multiplePersonsFound() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_SCHEDULES_LISTED, "2/2")).append("\n\n");
        sb.append("Benson Meier: 1200-1400\n").append("Daniel Meier: 1000-1200\n");
        String expectedMessage = sb.toString().trim();
        ScheduleContainsKeywordPredicate predicate = preparePredicate("2/2");
        ViewCommand command = new ViewCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        ScheduleContainsKeywordPredicate predicate = new ScheduleContainsKeywordPredicate("02/02");
        ViewCommand viewCommand = new ViewCommand(predicate);
        String expected = ViewCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, viewCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code ScheduleContainsKeywordPredicate}.
     */
    private ScheduleContainsKeywordPredicate preparePredicate(String userInput) {
        return new ScheduleContainsKeywordPredicate(userInput);
    }
}
