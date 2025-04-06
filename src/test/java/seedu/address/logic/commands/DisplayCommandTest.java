package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DisplayCommand}.
 */
public class DisplayCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDisplay = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DisplayCommand displayCommand = new DisplayCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DisplayCommand.MESSAGE_DISPLAY_PERSON_SUCCESS,
                Messages.format(personToDisplay));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(displayCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DisplayCommand displayCommand = new DisplayCommand(outOfBoundIndex);

        assertCommandFailure(displayCommand, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + "\n\n"
                        + DisplayCommand.MESSAGE_USAGE);
    }


    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DisplayCommand displayCommand = new DisplayCommand(outOfBoundIndex);

        assertCommandFailure(displayCommand, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + "\n\n"
                        + DisplayCommand.MESSAGE_USAGE);
    }

    @Test
    public void equals() {
        DisplayCommand displayFirstCommand = new DisplayCommand(INDEX_FIRST_PERSON);
        DisplayCommand displaySecondCommand = new DisplayCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(displayFirstCommand.equals(displayFirstCommand));

        // same values -> returns true
        DisplayCommand displayFirstCommandCopy = new DisplayCommand(INDEX_FIRST_PERSON);
        assertTrue(displayFirstCommand.equals(displayFirstCommandCopy));

        // different types -> returns false
        assertFalse(displayFirstCommand.equals(1));

        // null -> returns false
        assertFalse(displayFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(displayFirstCommand.equals(displaySecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DisplayCommand displayCommand = new DisplayCommand(targetIndex);
        String expected = DisplayCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, displayCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
