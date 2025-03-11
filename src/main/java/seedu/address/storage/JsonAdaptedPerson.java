package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Location;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String recurringSchedule;
    private final String email;
    private final String location;
    private final List<JsonAdaptedOneTimeSchedule> oneTimeSchedules = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("recurringSchedule") String recurringSchedule,
                             @JsonProperty("email") String email, @JsonProperty("location") String location,
                             @JsonProperty("oneTimeSchedule") List<JsonAdaptedOneTimeSchedule> oneTimeSchedules,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.recurringSchedule = recurringSchedule;
        this.email = email;
        this.location = location;
        if (oneTimeSchedules != null) {
            this.oneTimeSchedules.addAll(oneTimeSchedules);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        recurringSchedule = source.getRecurringSchedule().value;
        email = source.getEmail().value;
        location = source.getLocation().value;
        oneTimeSchedules.addAll(source.getOneTimeSchedules().stream()
                .map(JsonAdaptedOneTimeSchedule::new)
                .collect(Collectors.toList()));
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<OneTimeSchedule> personOneTimeSchedules = new ArrayList<>();
        for (JsonAdaptedOneTimeSchedule oneTimeSchedule : oneTimeSchedules) {
            personOneTimeSchedules.add(oneTimeSchedule.toModelType());
        }

        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (recurringSchedule == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    RecurringSchedule.class.getSimpleName()));
        }
        if (!RecurringSchedule.isValidSchedule(recurringSchedule)) {
            throw new IllegalValueException(RecurringSchedule.MESSAGE_CONSTRAINTS);
        }
        final RecurringSchedule modelRecurringSchedule = new RecurringSchedule(recurringSchedule);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (location == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Location.class.getSimpleName()));
        }
        if (!Location.isValidLocation(location)) {
            throw new IllegalValueException(Location.MESSAGE_CONSTRAINTS);
        }
        final Location modelLocation = new Location(location);

        final Set<OneTimeSchedule> modelOneTimeSchedules = new HashSet<>(personOneTimeSchedules);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelRecurringSchedule, modelEmail, modelLocation, modelOneTimeSchedules, modelTags);
    }

}
