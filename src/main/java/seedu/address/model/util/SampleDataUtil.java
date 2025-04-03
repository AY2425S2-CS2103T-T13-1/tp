package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                    getRecurringScheduleSet("Mon 1400 1600", "Wed 1600 1800"), new Goals("Get fitter"),
                    new MedicalHistory("Lower back injury"), new Location("Jurong West ActiveSG"),
                    getOneTimeScheduleSet("25/02 1000 1200", "01/03 1000 1200"), getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                    getRecurringScheduleSet("Mon 1600 1800"), new Goals("Lose weight"),
                    new MedicalHistory("Upper back injury"), new Location("Nanyang CC Anytime Fitness"),
                    getOneTimeScheduleSet("25/02 1200 1300"), getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    getRecurringScheduleSet("Tue 1400 1600"), new Goals("Gain muscle mass"),
                    new MedicalHistory("Right shoulder dislocated"),
                    new Location("Jurong West ActiveSG"),
                    getOneTimeScheduleSet("27/02 1000 1200"), getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"),
                    getRecurringScheduleSet("Tue 1800 2000"), new Goals("Get stronger"),
                    new MedicalHistory("Left shoulder dislocated"),
                    new Location("Boon Lay Anytime Fitness"),
                    getOneTimeScheduleSet("28/03 1000 1200"), getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                    getRecurringScheduleSet("Wed 1400 1600"), new Goals("Work on stamina"),
                    new MedicalHistory("Fractured right hand"), new Location("NUS"),
                    getOneTimeScheduleSet("16/04 1000 1200"), getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                    getRecurringScheduleSet("Thu 1400 1600"), new Goals("Calisthenics improvement"),
                    new MedicalHistory("Fractured left hand"), new Location("NUS"),
                    getOneTimeScheduleSet("08/09 1000 1200"), getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a recurringSchedule set containing the list of strings given.
     */
    public static Set<RecurringSchedule> getRecurringScheduleSet(String... strings) {
        return Arrays.stream(strings)
                .map(RecurringSchedule::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a one time schedule set containing the list of strings given.
     */
    public static Set<OneTimeSchedule> getOneTimeScheduleSet(String... strings) {
        return Arrays.stream(strings)
                .map(OneTimeSchedule::new)
                .collect(Collectors.toSet());
    }

}
