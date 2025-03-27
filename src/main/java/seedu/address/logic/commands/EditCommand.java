package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_HISTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ONETIMESCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.person.ScheduleConflictDetector;
import seedu.address.model.person.ScheduleConflictResult;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Update the details of a client in the client list, "
            + "by the index number used in the displayed client list. "
            + "Existing values will be overwritten by the input values.\n\n"
            + "Format: "
            + COMMAND_WORD + " INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_RECURRING_SCHEDULE + "RECURRING SCHEDULE] "
            + "[" + PREFIX_ONETIMESCHEDULE + "ONE TIME SCHEDULE] "
            + "[" + PREFIX_GOALS + "GOALS]"
            + "[" + PREFIX_MEDICAL_HISTORY + "MEDICAL_HISTORY] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_TAG + "TAG]...\n\n"
            + "RECURRING SCHEDULE Format: DAY HHmm HHmm\n"
            + "ONE TIME SCHEDULE Format: DD/MM[/YY] HHmm HHmm\n\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_SCHEDULE_CONFLICT =
            "Note: The person has been edited, but there are schedule conflicts:\n\n";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // Check for schedule conflicts with existing persons
        List<String> conflicts = new ArrayList<>();
        for (Person existingPerson : model.getAddressBook().getPersonList()) {
            // Skip the person being edited
            if (existingPerson.equals(personToEdit)) {
                continue;
            }
            conflicts.addAll(checkConflictsWithPerson(existingPerson, editedPerson));
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // If there are conflicts, add them to the success message
        if (!conflicts.isEmpty()) {
            StringBuilder conflictsMsg = new StringBuilder();
            conflictsMsg.append(MESSAGE_SCHEDULE_CONFLICT);

            for (String conflict : conflicts) {
                conflictsMsg.append(conflict).append("\n\n");
            }

            conflictsMsg.append(String.format(MESSAGE_EDIT_PERSON_SUCCESS,
                    Messages.format(editedPerson)));
            return new CommandResult(conflictsMsg.toString());
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Checks for schedule conflicts between the editedPerson and an existing person.
     */
    private List<String> checkConflictsWithPerson(Person existingPerson, Person editedPerson) {
        List<String> conflicts = new ArrayList<>();

        // Check each recurring schedule
        for (RecurringSchedule schedule : editedPerson.getRecurringSchedules()) {
            ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(existingPerson, schedule);
            if (result.hasConflict()) {
                conflicts.add(String.format("%s with %s", result.getConflictDescription(), 
                        existingPerson.getName()));
            }
        }

        // Check each one-time schedule
        for (OneTimeSchedule schedule : editedPerson.getOneTimeSchedules()) {
            ScheduleConflictResult result = ScheduleConflictDetector.checkScheduleConflict(existingPerson, schedule);
            if (result.hasConflict()) {
                conflicts.add(String.format("%s with %s", result.getConflictDescription(), 
                        existingPerson.getName()));
            }
        }

        return conflicts;
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Set<RecurringSchedule> updatedRecurringSchedules = editPersonDescriptor.getRecurringSchedules()
                .orElse(personToEdit.getRecurringSchedules());
        Goals updatedGoals = editPersonDescriptor.getGoals().orElse(personToEdit.getGoals());
        MedicalHistory updatedMedicalHistory = editPersonDescriptor.getMedicalHistory()
                .orElse(personToEdit.getMedicalHistory());
        Location updatedLocation = editPersonDescriptor.getLocation().orElse(personToEdit.getLocation());
        Set<OneTimeSchedule> updatedOneTimeSchedules = editPersonDescriptor.getOneTimeSchedules()
                .orElse(personToEdit.getOneTimeSchedules());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedRecurringSchedules, updatedGoals,
                updatedMedicalHistory, updatedLocation, updatedOneTimeSchedules, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Set<RecurringSchedule> recurringSchedules;
        private Goals goals;
        private MedicalHistory medicalHistory;
        private Location location;
        private Set<OneTimeSchedule> oneTimeSchedules;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setRecurringSchedules(toCopy.recurringSchedules);
            setGoals(toCopy.goals);
            setMedicalHistory(toCopy.medicalHistory);
            setLocation(toCopy.location);
            setOneTimeSchedules(toCopy.oneTimeSchedules);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, recurringSchedules, goals, medicalHistory, location,
                    oneTimeSchedules, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        /**
         * Sets {@code recurringSchedules} to this object's {@code recurringSchedules}.
         * A defensive copy of {@code recurringSchedules} is used internally.
         */
        public void setRecurringSchedules(Set<RecurringSchedule> recurringSchedules) {
            this.recurringSchedules = (recurringSchedules != null) ? new HashSet<>(recurringSchedules) : null;
        }

        /**
         * Returns an unmodifiable recurringSchedule set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code recurringSchedules} is null.
         */
        public Optional<Set<RecurringSchedule>> getRecurringSchedules() {
            return (recurringSchedules != null)
                    ? Optional.of(Collections.unmodifiableSet(recurringSchedules))
                    : Optional.empty();
        }

        public void setGoals(Goals goals) {
            this.goals = goals;
        }

        public Optional<Goals> getGoals() {
            return Optional.ofNullable(goals);
        }

        public void setMedicalHistory(MedicalHistory medicalHistory) {
            this.medicalHistory = medicalHistory;
        }

        public Optional<MedicalHistory> getMedicalHistory() {
            return Optional.ofNullable(medicalHistory);
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Optional<Location> getLocation() {
            return Optional.ofNullable(location);
        }

        public void setOneTimeSchedules(Set<OneTimeSchedule> oneTimeSchedules) {
            this.oneTimeSchedules = (oneTimeSchedules != null) ? new HashSet<>(oneTimeSchedules) : null;
        }

        public Optional<Set<OneTimeSchedule>> getOneTimeSchedules() {
            return (oneTimeSchedules != null)
                    ? Optional.of(Collections.unmodifiableSet(oneTimeSchedules)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(recurringSchedules, otherEditPersonDescriptor.recurringSchedules)
                    && Objects.equals(location, otherEditPersonDescriptor.location)
                    && Objects.equals(oneTimeSchedules, otherEditPersonDescriptor.oneTimeSchedules)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("recurringSchedules", recurringSchedules)
                    .add("goals", goals)
                    .add("medicalHistory", medicalHistory)
                    .add("location", location)
                    .add("oneTimeSchedule", oneTimeSchedules)
                    .add("tags", tags)
                    .toString();
        }
    }
}
