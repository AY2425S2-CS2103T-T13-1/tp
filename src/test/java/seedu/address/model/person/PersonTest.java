package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GOALS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICAL_HISTORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ONETIMESCHEDULE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RECURRING_SCHEDULE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withGoals(VALID_GOALS_BOB).withMedicalHistory(VALID_MEDICAL_HISTORY_BOB)
                .withLocation(VALID_LOCATION_BOB).withOneTimeSchedules(VALID_ONETIMESCHEDULE_BOB)
                .withRecurringSchedules(VALID_RECURRING_SCHEDULE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        Person editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void hasSamePhone() {
        // same object -> returns true
        assertTrue(ALICE.hasSamePhone(ALICE));

        // null -> returns false
        assertFalse(ALICE.hasSamePhone(null));

        // same phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withGoals(VALID_GOALS_BOB).withMedicalHistory(VALID_MEDICAL_HISTORY_BOB)
                .withLocation(VALID_LOCATION_BOB).withOneTimeSchedules(VALID_ONETIMESCHEDULE_BOB)
                .withRecurringSchedules(VALID_RECURRING_SCHEDULE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.hasSamePhone(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.hasSamePhone(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different goals -> returns false
        editedAlice = new PersonBuilder(ALICE).withGoals(VALID_GOALS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different medical history -> returns false
        editedAlice = new PersonBuilder(ALICE).withMedicalHistory(VALID_MEDICAL_HISTORY_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withLocation(VALID_LOCATION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different one-time schedules -> returns false
        editedAlice = new PersonBuilder(ALICE).withOneTimeSchedules(VALID_ONETIMESCHEDULE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different recurring schedules -> returns false
        editedAlice = new PersonBuilder(ALICE).withRecurringSchedules(VALID_RECURRING_SCHEDULE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCodeMethod() {
        // same values -> has same hashcode
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        // same object -> same hashcode
        assertEquals(ALICE.hashCode(), ALICE.hashCode());

        // different person -> different hashcode
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());

        // different name -> different hashcode
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different phone -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different goals -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withGoals(VALID_GOALS_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different address -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withLocation(VALID_LOCATION_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different one-time schedules -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withOneTimeSchedules(VALID_ONETIMESCHEDULE_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different recurring schedules -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withRecurringSchedules(VALID_RECURRING_SCHEDULE_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // different tags -> different hashcode
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", recurringSchedules=" + ALICE.getRecurringSchedules()
                + ", goals=" + ALICE.getGoals() + ", medical history=" + ALICE.getMedicalHistory()
                + ", location=" + ALICE.getLocation()
                + ", oneTimeSchedule=" + ALICE.getOneTimeSchedules() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
