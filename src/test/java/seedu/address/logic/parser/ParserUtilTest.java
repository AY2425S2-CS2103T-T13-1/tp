package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "Réchel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_RECURRINGSCHEDULE_1 = "invalid schedule";
    private static final String INVALID_RECURRINGSCHEDULE_2 = "Monday 1400 1200"; //invalid time
    private static final String INVALID_GOALS = " ";
    private static final String INVALID_LOCATION = " ";
    private static final String INVALID_ONETIMESCHEDULE_1 = "33/1 1000 1200"; //out of range date
    private static final String INVALID_ONETIMESCHEDULE_2 = "2/3 1400 1200"; //invalid time
    private static final String INVALID_ONETIMESCHEDULE_3 = "31/6 1000 1200"; //invalid date
    private static final String INVALID_TAG = "friendé";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "88888888";
    private static final String VALID_RECURRINGSCHEDULE_1 = "Tuesday 0900 1700";
    private static final String VALID_RECURRINGSCHEDULE_2 = "fri 0800 1200";
    private static final String VALID_GOALS = "Get fitter";
    private static final String VALID_LOCATION = "123 Main Street #0505";
    private static final String VALID_ONETIMESCHEDULE_1 = "01/12 1000 1200";
    private static final String VALID_ONETIMESCHEDULE_2 = "1/1 1000 1200";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseRecurringSchedule_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseRecurringSchedule(null));
    }

    @Test
    public void parseRecurringSchedule_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRecurringSchedule(INVALID_RECURRINGSCHEDULE_1));
    }

    @Test
    public void parseRecurringSchedule_invalidTimeOrder_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRecurringSchedule(INVALID_RECURRINGSCHEDULE_2));
    }

    @Test
    public void parseRecurringSchedule_validValueWithoutWhitespace_returnsRecurringSchedule() throws Exception {
        RecurringSchedule expected = new RecurringSchedule(VALID_RECURRINGSCHEDULE_1);
        assertEquals(expected, ParserUtil.parseRecurringSchedule(VALID_RECURRINGSCHEDULE_1));
    }

    @Test
    public void parseRecurringSchedule_validValueWithWhitespaceAndAbbreviatedDay_returnsRecurringSchedule() throws
            Exception {
        String abbrevDayWithWhiteppace = WHITESPACE + VALID_RECURRINGSCHEDULE_2 + WHITESPACE;
        // The constructor converts "fri" to "Friday", so the expected schedule should be "Friday 0800 1200"
        RecurringSchedule expected = new RecurringSchedule("fri 0800 1200");
        assertEquals(expected, ParserUtil.parseRecurringSchedule(abbrevDayWithWhiteppace));
    }


    @Test
    public void parseGoals_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseGoals((String) null));
    }

    @Test
    public void parseGoals_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGoals(INVALID_GOALS));
    }

    @Test
    public void parseGoals_validValueWithoutWhitespace_returnsGoals() throws Exception {
        Goals expectedGoals = new Goals(VALID_GOALS);
        assertEquals(expectedGoals, ParserUtil.parseGoals(VALID_GOALS));
    }

    @Test
    public void parseGoals_validValueWithWhitespace_returnsTrimmedGoals() throws Exception {
        String goalsWithWhitespace = WHITESPACE + VALID_GOALS + WHITESPACE;
        Goals expectedGoals = new Goals(VALID_GOALS);
        assertEquals(expectedGoals, ParserUtil.parseGoals(goalsWithWhitespace));
    }

    @Test
    public void parseLocation_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLocation((String) null));
    }

    @Test
    public void parseLocation_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLocation(INVALID_LOCATION));
    }

    @Test
    public void parseLocation_validValueWithoutWhitespace_returnsLocation() throws Exception {
        Location expectedLocation = new Location(VALID_LOCATION);
        assertEquals(expectedLocation, ParserUtil.parseLocation(VALID_LOCATION));
    }

    @Test
    public void parseLocation_validValueWithWhitespace_returnsTrimmedLocation() throws Exception {
        String locationWithWhitespace = WHITESPACE + VALID_LOCATION + WHITESPACE;
        Location expectedLocation = new Location(VALID_LOCATION);
        assertEquals(expectedLocation, ParserUtil.parseLocation(locationWithWhitespace));
    }

    @Test
    public void parseOneTimeSchedules_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseOneTimeSchedule((String) null));
    }

    @Test
    public void parseOneTimeSchedule_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseOneTimeSchedule(INVALID_ONETIMESCHEDULE_1));
    }

    @Test
    public void parseOneTimeSchedule_invalidTimeOrder_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseOneTimeSchedule(INVALID_ONETIMESCHEDULE_2));
    }

    @Test
    public void parseOneTimeSchedule_invalidDate_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseOneTimeSchedule(INVALID_ONETIMESCHEDULE_3));
    }

    @Test
    public void parseOneTimeSchedule_validValueWithoutWhitespace_returnsOneTimeSchedule() throws Exception {
        OneTimeSchedule expectedOneTimeSchedule = new OneTimeSchedule(VALID_ONETIMESCHEDULE_1);
        assertEquals(expectedOneTimeSchedule, ParserUtil.parseOneTimeSchedule(VALID_ONETIMESCHEDULE_1));
    }

    @Test
    public void parseOneTimeSchedule_validValueWithWhitespace_returnsTrimmedOneTimeSchedule() throws Exception {
        String oneTimeScheduleWithWhitespace = WHITESPACE + VALID_ONETIMESCHEDULE_1 + WHITESPACE;
        OneTimeSchedule expectedOneTimeSchedule = new OneTimeSchedule(VALID_ONETIMESCHEDULE_1);
        assertEquals(expectedOneTimeSchedule, ParserUtil.parseOneTimeSchedule(oneTimeScheduleWithWhitespace));
    }

    @Test
    public void parseOneTimeSchedules_collectionWithInvalidOneTimeSchedules_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseOneTimeSchedules(
                Arrays.asList(VALID_ONETIMESCHEDULE_1, INVALID_ONETIMESCHEDULE_1)));
    }

    @Test
    public void parseOneTimeSchedules_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseOneTimeSchedules(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseOneTimeSchedules_collectionWithValidOneTimeSchedules_returnsOneTimeScheduleSetSet()
            throws Exception {
        Set<OneTimeSchedule> actualOneTimeScheduleSet = ParserUtil.parseOneTimeSchedules(
                Arrays.asList(VALID_ONETIMESCHEDULE_1, VALID_ONETIMESCHEDULE_2));
        Set<OneTimeSchedule> expectedOneTimeScheduleSet = new HashSet<OneTimeSchedule>(
                Arrays.asList(new OneTimeSchedule(VALID_ONETIMESCHEDULE_1),
                        new OneTimeSchedule(VALID_ONETIMESCHEDULE_2)));

        assertEquals(expectedOneTimeScheduleSet, actualOneTimeScheduleSet);
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
