package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LocationTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Location(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Location(invalidAddress));
    }

    @Test
    public void isValidLocation() {
        // null address
        assertThrows(NullPointerException.class, () -> Location.isValidLocation(null));

        // invalid addresses
        assertFalse(Location.isValidLocation("")); // empty string
        assertFalse(Location.isValidLocation(" ")); // spaces only

        // valid addresses
        assertTrue(Location.isValidLocation("Blk 456, Den Road, #01-355"));
        assertTrue(Location.isValidLocation("-")); // one character
        assertTrue(Location.isValidLocation("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void equals() {
        Location location = new Location("Valid Location");

        // same values -> returns true
        assertTrue(location.equals(new Location("Valid Location")));

        // same object -> returns true
        assertTrue(location.equals(location));

        // null -> returns false
        assertFalse(location.equals(null));

        // different types -> returns false
        assertFalse(location.equals(5.0f));

        // different values -> returns false
        assertFalse(location.equals(new Location("Other Valid Location")));
    }
}
