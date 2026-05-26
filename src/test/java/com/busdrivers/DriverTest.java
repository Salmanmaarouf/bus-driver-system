package com.busdrivers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// unit tests for the driver class covering conditions D1 to D5
// each condition has normal, invalid, and edge case tests
public class DriverTest {

    // valid test data used across multiple tests
    String valid_id = "23@#ab12AB";
    String valid_name = "Jane Smith";
    String valid_address = "12|Main Street|Sydney|NSW|Australia";
    String valid_dob = "15-06-1990";
    String valid_license = "Heavy";

    // Driver condition 1 vaild: a correctly formatted ID should be accepted
    @Test
    void testD1_validID() {
        assertTrue(Driver.isValidDriverID("23@#ab12AB"));
    }

    // Driver condition 1 valid: a valid ID with different special characters should
    // also pass
    @Test
    void testD1_validID_differentSpecials() {
        assertTrue(Driver.isValidDriverID("35@!xy34MN"));
    }

    // Driver condition 1 - invalid: an ID shorter than 10 characters should be
    // rejected
    @Test
    void testD1_shortID() {
        assertFalse(Driver.isValidDriverID("23@#abAB"));
    }

    // Driver condition 1 - invalid: a first digit of 1 is outside the allowed range
    // of 2-9
    @Test
    void testD1_firstDigitOne() {
        assertFalse(Driver.isValidDriverID("13@#ab12AB"));
    }

    // Driver condition 1 - invalid: only one special character in positions 3-8 is
    // not enough
    @Test
    void testD1_oneSpecialChar() {
        assertFalse(Driver.isValidDriverID("23@abcd1AB"));
    }

    // Driver condition 1 - invalid: the last two characters must be uppercase so
    // lowercase should
    // fail
    @Test
    void testD1_lowercaseLastTwo() {
        assertFalse(Driver.isValidDriverID("23@#ab12ab"));
    }

    // Driver condition 1 - edge: exactly 2 special characters is the minimum so it
    // should pass
    @Test
    void testD1_exactlyTwoSpecials() {
        assertTrue(Driver.isValidDriverID("23@#abcdAB"));
    }

    // Driver condition 1 - edge: a null ID should return false without throwing an
    // exception
    @Test
    void testD1_nullID() {
        assertFalse(Driver.isValidDriverID(null));
    }

    // Driver condition 2 - normal: a correctly formatted address should be accepted
    @Test
    void testD2_validAddress() {
        assertTrue(Driver.isValidAddress("12|Main Street|Sydney|NSW|Australia"));
    }

    // Driver condition 2 - invalid: an address with only 3 segments should be
    // rejected
    @Test
    void testD2_tooFewSegments() {
        assertFalse(Driver.isValidAddress("12|Main Street|Sydney"));
    }

    // Driver condition 2 - invalid: an empty segment in the address should be
    // rejected
    @Test
    void testD2_emptySegment() {
        assertFalse(Driver.isValidAddress("12|Main Street||NSW|Australia"));
    }

    // Driver condition 2 - edge: 6 segments is too many and should be rejected
    @Test
    void testD2_tooManySegments() {
        assertFalse(Driver.isValidAddress("12|Main Street|Sydney|NSW|Australia|Extra"));
    }

    // Driver condition 3 - normal: a valid DD-MM-YYYY date should be accepted
    @Test
    void testD3_validBirthdate() {
        assertTrue(Driver.isValidBirthdate("01-01-2000"));
    }

    // Driver condition 3 - invalid: using slashes instead of dashes should be
    // rejected
    @Test
    void testD3_wrongSeparator() {
        assertFalse(Driver.isValidBirthdate("15/06/1990"));
    }

    // Driver condition 3 - invalid: month 13 does not exist so it should be
    // rejected
    @Test
    void testD3_invalidMonth() {
        assertFalse(Driver.isValidBirthdate("01-13-1990"));
    }

    // Driver condition 3 - edge: 31-12-1999 is a valid boundary date and should be
    // accepted
    @Test
    void testD3_boundaryDate() {
        assertTrue(Driver.isValidBirthdate("31-12-1999"));
    }

    // Driver condition 4 - normal: a driver with exactly 10 years of experience can
    // still change
    // their license
    @Test
    void testD4_tenYearsCanChangeLicense() {
        Driver d = new Driver(valid_id, valid_name, 10, "Light", valid_address, valid_dob);
        d.update(10, "Medium", valid_address, valid_dob);
        assertEquals("Medium", d.getLicenseType());
    }

    // Driver condition 4 - invalid: a driver with 11 years of experience cannot
    // change their
    // license type
    @Test
    void testD4_elevenYearsCannotChangeLicense() {
        Driver d = new Driver(valid_id, valid_name, 11, "Heavy", valid_address, valid_dob);
        try {
            d.update(11, "Light", valid_address, valid_dob);
            fail("expected IllegalArgumentException for D4 violation");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("D4"));
        }
    }

    // Driver condition 4 - edge: a driver with more than 10 years keeping the same
    // license type
    // should be allowed
    @Test
    void testD4_highExperienceSameLicense() {
        Driver d = new Driver(valid_id, valid_name, 15, "Heavy", valid_address, valid_dob);
        d.update(15, "Heavy", valid_address, valid_dob);
        assertEquals("Heavy", d.getLicenseType());
    }

    // Driver condition 5 - normal: the driver ID should not change after calling
    // update
    @Test
    void testD5_driverIDIsImmutable() {
        Driver d = new Driver(valid_id, valid_name, 5, valid_license, valid_address, valid_dob);
        d.update(6, valid_license, valid_address, valid_dob);
        assertEquals(valid_id, d.getDriverID());
    }

    // Driver condition 5 - normal: the name should not change after calling update
    @Test
    void testD5_nameIsImmutable() {
        Driver d = new Driver(valid_id, valid_name, 5, valid_license, valid_address, valid_dob);
        d.update(6, valid_license, valid_address, valid_dob);
        assertEquals(valid_name, d.getName());
    }

    // Driver condition 5 - edge: passing a null name at construction should throw
    // an exception
    @Test
    void testD5_nullNameRejected() {
        try {
            new Driver(valid_id, null, 5, valid_license, valid_address, valid_dob);
            fail("expected IllegalArgumentException for null name");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
}