package com.busdrivers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

//unit tests including conditions B1 to B5 and each condition includes normal, invalid, and edge case tests
public class BusTest {

    //test data
    String ValidBusID = "12345678";
    int ValidCapacity = 40;
    double ValidFuelLevel = 75.0;
    String ValidFuelType = "Diesel";

    String ValidDriverID = "22@#aa22AA";
    String ValidDriverName = "AA";
    String ValidDriverAddress = "2|Reservoir|Melbourne|VIC|Australia";
    String ValidDriverBirth = "11-01-2001";

    //B1 - normal
    //correct format of the BusID should be accept
    @Test
    void testB1ValidBusID() {
        assertTrue(Bus.isValidBusID(ValidBusID));
    }

    //B1 - normal 
    //another valid busID should be accept
    @Test
    void testB1ValidBusIDDifferentOne() {
        assertTrue(Bus.isValidBusID("87654321"));
    }

    //B1 - invalid
    //busID shorter than 8 characters should be reject
    @Test
    void testB1ShortBusID() {
        assertFalse(Bus.isValidBusID("1234567"));
    }

    //B1 - invalid
    //busID longer than 8 characters should be reject
    @Test
    void testB1LongBusID() {
        assertFalse(Bus.isValidBusID("123456789"));
    }

    //B1 - invalid
    //bus ID that contain letters should be reject
    @Test
    void testB1BusIDWithLetters() {
        assertFalse(Bus.isValidBusID("1234567A"));
    }

    //B1 - edge
    //null bus ID should return false without throwing an exception
    @Test
    void testB1NullBusID() {
        assertFalse(Bus.isValidBusID(null));
    }

    //B2 - normal
    //reducing capacity during update should be allow
    @Test
    void testB2CapacityCanDecrease() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
        bus.update(30, ValidFuelLevel, ValidFuelType);
        assertEquals(30, bus.getCapacity());
    }

    //B2 - normal
    //keeping same capacity during update should be allow
    @Test
    void testB2CapacityCanStaySame() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
        bus.update(ValidCapacity, ValidFuelLevel, ValidFuelType);
        assertEquals(ValidCapacity, bus.getCapacity());
    }

    //B2 - invalid
    //iincreasing capacity during update should be reject
    @Test
    void testB2CapacityCantIncrease() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
        try {
            bus.update(50, ValidFuelLevel, ValidFuelType);
            fail("Capacity cannot increase during update");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("B2"));
        }
    }

    //B2 - edge
    //reducing capacity to 0 should be allow
    @Test
    void testB2CapacityCanReduceToZero() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
        bus.update(0, ValidFuelLevel, ValidFuelType);
        assertEquals(0, bus.getCapacity());
    }

    //B3 - normal
    //driver under 50 can drive bus with capacity of 50 or more
    @Test
    void testB3YoungDriverCanDriveLargeBus() {
        Bus bus = new Bus(ValidBusID, 50, ValidFuelLevel, ValidFuelType);
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Heavy", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverAgeValid(driver));
    }

    //B3 - invalid
    //driver older than 50 cannot drive bus with capacity of 50 or more
    @Test
    void testB3OldDriverCantDriveLargeBus() {
        Bus bus = new Bus(ValidBusID, 50, ValidFuelLevel, ValidFuelType);
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Heavy", ValidDriverAddress, "01-01-1960");
        assertFalse(bus.isDriverAgeValid(driver));
    }

    //B3 - edge
    //driver older than 50 can still drive bus with capacity less than 50
    @Test
    void testB3OldDriverCanDriveSmallBus() {
        Bus bus = new Bus(ValidBusID, 49, ValidFuelLevel, ValidFuelType);
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Heavy", ValidDriverAddress, "01-01-1960");
        assertTrue(bus.isDriverAgeValid(driver));
    }

    //B4 - normal
    //driver with 5+ years experience can drive electric bus
    @Test
    void testB4ExperiencedDriverCanDriveElectric() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Electricity");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Heavy", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverExperienceValid(driver));
    }

    //B4 - invalid
    //driver with less than 5 years experience can not drive electric bus
    @Test
    void testB4InexperienceDriverCantDriveElectric() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Electricity");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 3,
            "Heavy", ValidDriverAddress, ValidDriverBirth);
        assertFalse(bus.isDriverExperienceValid(driver));
    }

    //B4 - edge
    //driver with any experience can drive a diesel bus
    @Test
    void testB4AnyDriverCanDriveDiesel() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Diesel");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 1,
            "Light", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverExperienceValid(driver));
    }

    //B5 - normal
    //driver with Heavy licence can drive electric bus
    @Test
    void testB5HeavyLicenceCanDriveElectric() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Electricity");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Heavy", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverLicenceValid(driver));
    }

    //B5 - normal
    //driver with PublicTransport licence can drive hybrid bus
    @Test
    void testB5PublicTransportLicenceCanDriveHybrid() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Hybrid");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "PublicTransport", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverLicenceValid(driver));
    }

    //B5 - invalid
     //driver with Light licence ca not drive electric bus
    @Test
    void testB5LightLicenceCantDriveElectric() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Electricity");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Light", ValidDriverAddress, ValidDriverBirth);
        assertFalse(bus.isDriverLicenceValid(driver));
    }

    //B5 - invalid
    //driver with Medium licence can not drive hybrid bus
    @Test
    void testB5MediumLicenceCantDriveHybrid() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Hybrid");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Medium", ValidDriverAddress, ValidDriverBirth);
        assertFalse(bus.isDriverLicenceValid(driver));
    }

    //B5 - edge
    //driver can drive a diesel bus on any license type
    @Test
    void testB5AnyLicenceCanDriveDiesel() {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, "Diesel");
        Driver driver = new Driver(ValidDriverID, ValidDriverName, 5,
            "Light", ValidDriverAddress, ValidDriverBirth);
        assertTrue(bus.isDriverLicenceValid(driver));
    }
}