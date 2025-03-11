package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Email;
import seedu.address.model.person.Location;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_RECURRING_SCHEDULE = "Mon 1400 1600";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_LOCATION = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private RecurringSchedule recurringSchedule;
    private Email email;
    private Location location;
    private Set<OneTimeSchedule> oneTimeSchedules;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        recurringSchedule = new RecurringSchedule(DEFAULT_RECURRING_SCHEDULE);
        email = new Email(DEFAULT_EMAIL);
        location = new Location(DEFAULT_LOCATION);
        oneTimeSchedules = new HashSet<>();
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        recurringSchedule = personToCopy.getRecurringSchedule();
        email = personToCopy.getEmail();
        location = personToCopy.getLocation();
        oneTimeSchedules = new HashSet<>(personToCopy.getOneTimeSchedules());
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Location} of the {@code Person} that we are building.
     */
    public PersonBuilder withLocation(String address) {
        this.location = new Location(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code RecurringSchedule} of the {@code Person} that we are building.
     */
    public PersonBuilder withRecurringSchedule(String recurringSchedule) {
        this.recurringSchedule = new RecurringSchedule(recurringSchedule);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Parses the {@code oneTimeSchedules} into a {@code Set<OneTimeSchedule>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withOneTimeSchedules(String ... oneTimeSchedules) {
        this.oneTimeSchedules = SampleDataUtil.getOneTimeScheduleSet(oneTimeSchedules);
        return this;
    }

    public Person build() {
        return new Person(name, phone, recurringSchedule, email, location, oneTimeSchedules, tags);
    }

}
