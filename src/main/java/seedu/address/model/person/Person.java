package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Set<RecurringSchedule> recurringSchedules = new HashSet<>();
    private final Goals goals;
    private final MedicalHistory medicalHistory;
    private final Location location;
    private final Set<OneTimeSchedule> oneTimeSchedules = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Set<RecurringSchedule> recurringSchedules, Goals goals,
                  MedicalHistory medicalHistory, Location location,
                  Set<OneTimeSchedule> oneTimeSchedule, Set<Tag> tags) {
        requireAllNonNull(name, phone, medicalHistory, location, oneTimeSchedule, tags);
        this.name = name;
        this.phone = phone;
        this.recurringSchedules.addAll(recurringSchedules);
        this.goals = goals;
        this.medicalHistory = medicalHistory;
        this.location = location;
        this.oneTimeSchedules.addAll(oneTimeSchedule);
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns an immutable recurringSchedule set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<RecurringSchedule> getRecurringSchedules() {
        return Collections.unmodifiableSet(recurringSchedules);
    }

    public Goals getGoals() {
        return goals;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public Location getLocation() {
        return location;
    }

    public Set<OneTimeSchedule> getOneTimeSchedules() {
        return Collections.unmodifiableSet(oneTimeSchedules);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same phone.
     * Used to check for duplicate phone numbers.
     */
    public boolean hasSamePhone(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && recurringSchedules.equals(otherPerson.recurringSchedules)
                && goals.equals(otherPerson.goals)
                && medicalHistory.equals(otherPerson.medicalHistory)
                && location.equals(otherPerson.location)
                && oneTimeSchedules.equals(otherPerson.oneTimeSchedules)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, recurringSchedules, goals, medicalHistory, location, oneTimeSchedules, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("recurringSchedules", recurringSchedules)
                .add("goals", goals)
                .add("medical history", medicalHistory)
                .add("location", location)
                .add("oneTimeSchedule", oneTimeSchedules)
                .add("tags", tags)
                .toString();
    }

}
