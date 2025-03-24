package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.OneTimeSchedule;

/**
 * Jackson-friendly version of {@link OneTimeSchedule}.
 */
class JsonAdaptedOneTimeSchedule {

    private final String oneTimeScheduleDate;

    /**
     * Constructs a {@code JsonAdaptedOneTimeSchedule} with the given {@code OneTimeSchedule}.
     */
    @JsonCreator
    public JsonAdaptedOneTimeSchedule(String oneTimeScheduleDate) {
        this.oneTimeScheduleDate = oneTimeScheduleDate;
    }

    /**
     * Converts a given {@code OneTimeSchedule} into this class for Jackson use.
     */
    public JsonAdaptedOneTimeSchedule(OneTimeSchedule source) {
        oneTimeScheduleDate = source.getDate() + " " + source.getStartTime() + " " + source.getEndTime();
    }

    @JsonValue
    public String getTagName() {
        return oneTimeScheduleDate;
    }

    /**
     * Converts this Jackson-friendly adapted one time schedule object into the model's {@code OneTimeSchedule} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted one time schedule.
     */
    public OneTimeSchedule toModelType() throws IllegalValueException {
        if (!OneTimeSchedule.isValidOneTimeSchedule(oneTimeScheduleDate)) {
            throw new IllegalValueException(OneTimeSchedule.MESSAGE_CONSTRAINTS);
        }
        return new OneTimeSchedule(oneTimeScheduleDate);
    }

}
