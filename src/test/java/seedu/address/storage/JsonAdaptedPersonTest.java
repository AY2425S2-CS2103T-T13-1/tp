package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "Réchel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_RECURRING_SCHEDULE = "yay 1400 1600";
    private static final String INVALID_GOALS = " ";
    private static final String INVALID_MEDICAL_HISTORY = "é©";
    private static final String INVALID_LOCATION = " ";
    private static final String INVALID_ONETIMESCHEDULE = "40/01 1000 1200";
    private static final String INVALID_TAG = "friendé";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final List<JsonAdaptedRecurringSchedule> VALID_RECURRING_SCHEDULES = BENSON.getRecurringSchedules()
            .stream()
            .map(JsonAdaptedRecurringSchedule::new)
            .collect(Collectors.toList());
    private static final String VALID_GOALS = BENSON.getGoals().toString();
    private static final String VALID_MEDICAL_HISTORY = BENSON.getMedicalHistory().toString();
    private static final String VALID_LOCATION = BENSON.getLocation().toString();
    private static final List<JsonAdaptedOneTimeSchedule> VALID_ONETIMESCHEDULES = BENSON.getOneTimeSchedules()
            .stream()
            .map(JsonAdaptedOneTimeSchedule::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_RECURRING_SCHEDULES,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_RECURRING_SCHEDULES,
                VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRecurringSchedules_throwsIllegalValueException() {
        List<JsonAdaptedRecurringSchedule> invalidRecurringSchedules = new ArrayList<>(VALID_RECURRING_SCHEDULES);
        invalidRecurringSchedules.add(new JsonAdaptedRecurringSchedule(INVALID_RECURRING_SCHEDULE));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, invalidRecurringSchedules,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidGoals_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                        INVALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = Goals.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullGoals_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                null, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Goals.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidMedicalHistory_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES, VALID_GOALS,
                        INVALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = MedicalHistory.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullMedicalHistory_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                VALID_GOALS, null, VALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, MedicalHistory.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidLocation_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, INVALID_LOCATION, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = Location.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullLocation_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                VALID_GOALS, VALID_MEDICAL_HISTORY, null, VALID_ONETIMESCHEDULES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Location.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidOneTimeSchedules_throwsIllegalValueException() {
        List<JsonAdaptedOneTimeSchedule> invalidOneTimeSchedules = new ArrayList<>(VALID_ONETIMESCHEDULES);
        invalidOneTimeSchedules.add(new JsonAdaptedOneTimeSchedule(INVALID_ONETIMESCHEDULE));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, invalidOneTimeSchedules, VALID_TAGS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_RECURRING_SCHEDULES,
                        VALID_GOALS, VALID_MEDICAL_HISTORY, VALID_LOCATION, VALID_ONETIMESCHEDULES, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
