package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Input must be an integer value between 1 and "
            + Integer.MAX_VALUE;

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String recurringSchedule} into a {@code RecurringSchedule}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code recurringSchedule} is invalid.
     */
    public static RecurringSchedule parseRecurringSchedule(String recurringSchedule) throws ParseException {
        requireNonNull(recurringSchedule);
        String trimmedRecurringSchedule = recurringSchedule.trim();
        if (!RecurringSchedule.isValidRecurringSchedule(trimmedRecurringSchedule)) {
            throw new ParseException(RecurringSchedule.MESSAGE_CONSTRAINTS);
        }
        if (!RecurringSchedule.isValidTime(trimmedRecurringSchedule)) {
            throw new ParseException(RecurringSchedule.MESSAGE_TIME_CONSTRAINTS);
        }
        return new RecurringSchedule(trimmedRecurringSchedule);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<RecurringSchedule> parseRecurringSchedules(Collection<String> recurringSchedules)
            throws ParseException {
        requireNonNull(recurringSchedules);
        final Set<RecurringSchedule> recurringScheduleSet = new HashSet<>();
        for (String recurringSchedule : recurringSchedules) {
            recurringScheduleSet.add(parseRecurringSchedule(recurringSchedule));
        }
        return recurringScheduleSet;
    }

    /**
     * Parses a {@code String goals} into an a {@code Goals}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code goals} is invalid.
     */
    public static Goals parseGoals(String goals) throws ParseException {
        requireNonNull(goals);
        String trimmedGoals = goals.trim();
        if (!Goals.isValidGoals(trimmedGoals)) {
            throw new ParseException(Goals.MESSAGE_CONSTRAINTS);
        }
        return new Goals(trimmedGoals);
    }

    /**
     * Parses a {@code String location} into an {@code Location}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code location} is invalid.
     */
    public static Location parseLocation(String location) throws ParseException {
        requireNonNull(location);
        String trimmedLocation = location.trim();
        if (!Location.isValidLocation(trimmedLocation)) {
            throw new ParseException(Location.MESSAGE_CONSTRAINTS);
        }
        return new Location(trimmedLocation);
    }

    /**
     * Parses a {@code String oneTimeSchedule} into an {@code OneTimeSchedule}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code oneTimeSchedule} is invalid.
     */
    public static OneTimeSchedule parseOneTimeSchedule(String oneTimeSchedule) throws ParseException {
        requireNonNull(oneTimeSchedule);
        String trimmedOneTimeSchedule = oneTimeSchedule.trim();
        if (!OneTimeSchedule.isValidOneTimeSchedule(trimmedOneTimeSchedule)) {
            throw new ParseException(OneTimeSchedule.MESSAGE_CONSTRAINTS);
        }
        if (!OneTimeSchedule.isValidDate(trimmedOneTimeSchedule)) {
            throw new ParseException(OneTimeSchedule.MESSAGE_DATE_CONSTRAINTS);
        }
        if (!OneTimeSchedule.isValidTime(trimmedOneTimeSchedule)) {
            throw new ParseException(OneTimeSchedule.MESSAGE_TIME_CONSTRAINTS);
        }
        return new OneTimeSchedule(trimmedOneTimeSchedule);
    }

    /**
     * Parses {@code Collection<String> one time schedules} into a {@code Set<OneTimeSchedules>}.
     */
    public static Set<OneTimeSchedule> parseOneTimeSchedules(Collection<String> oneTimeSchedules)
            throws ParseException {
        requireNonNull(oneTimeSchedules);
        final Set<OneTimeSchedule> oneTimeScheduleSet = new HashSet<>();
        for (String oneTimeScheduleDate : oneTimeSchedules) {
            oneTimeScheduleSet.add(parseOneTimeSchedule(oneTimeScheduleDate));
        }
        return oneTimeScheduleSet;
    }

    /**
     * Parses a {@code String medicalHistory} into an {@code medicalHistory}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code medicalHistory} is invalid.
     */
    public static MedicalHistory parseMedicalHistory(String medicalHistory) throws ParseException {
        requireNonNull(medicalHistory);
        String trimmedMedicalHistory = medicalHistory.trim();
        if (!MedicalHistory.isValidMedicalHistory(trimmedMedicalHistory)) {
            throw new ParseException(MedicalHistory.MESSAGE_CONSTRAINTS);
        }
        return new MedicalHistory(trimmedMedicalHistory);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

}
