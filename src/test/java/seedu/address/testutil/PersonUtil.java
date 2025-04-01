package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GOALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_HISTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ONETIMESCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.DayOfWeekUtils;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        person.getRecurringSchedules().stream().forEach(
                s -> sb.append(PREFIX_RECURRING_SCHEDULE + String.valueOf(s.getDay())
                        + " " + s.getStartTime() + " " + s.getEndTime() + " ")
        );
        sb.append(PREFIX_GOALS + person.getGoals().value + " ");
        sb.append(PREFIX_MEDICAL_HISTORY + person.getMedicalHistory().value + " ");
        sb.append(PREFIX_LOCATION + person.getLocation().value + " ");
        person.getOneTimeSchedules().stream().forEach(
                s -> sb.append(PREFIX_ONETIMESCHEDULE + s.getDateString()
                       + " " + s.getStartTime() + " " + s.getEndTime() + " ")
        );
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));

        if (descriptor.getRecurringSchedules().isPresent()) {
            Set<RecurringSchedule> recurringSchedules = descriptor.getRecurringSchedules().get();
            if (recurringSchedules.isEmpty()) {
                sb.append(PREFIX_RECURRING_SCHEDULE).append(" ");;
            } else {
                recurringSchedules.forEach(s -> sb.append(PREFIX_RECURRING_SCHEDULE)
                        .append(DayOfWeekUtils.getPascalCaseName(s.getDay())).append(" ")
                        .append(s.getStartTime()).append(" ")
                        .append(s.getEndTime()).append(" ")
                );
            }
        }

        descriptor.getMedicalHistory().ifPresent(medicalHistory -> sb.append(PREFIX_MEDICAL_HISTORY)
                .append(medicalHistory.value).append(" "));
        descriptor.getGoals().ifPresent(goals -> sb.append(PREFIX_GOALS).append(goals.value).append(" "));
        descriptor.getLocation().ifPresent(
                address -> sb.append(PREFIX_LOCATION).append(address.value).append(" "));

        if (descriptor.getOneTimeSchedules().isPresent()) {
            Set<OneTimeSchedule> oneTimeSchedules = descriptor.getOneTimeSchedules().get();
            if (oneTimeSchedules.isEmpty()) {
                sb.append(PREFIX_ONETIMESCHEDULE).append(" ");
            } else {
                oneTimeSchedules.forEach(s -> sb.append(PREFIX_ONETIMESCHEDULE)
                        .append(s.getDateString()).append(" ").append(s.getStartTime()).append(" ")
                        .append(s.getEndTime()).append(" "));
            }
        }

        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
