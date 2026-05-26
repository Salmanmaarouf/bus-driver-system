package com.busdrivers;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//integration tests for BusRepository
public class BusIntegrationTest {

    //setup
    private static final String TEST_FILE = "test_buses.txt";
    private BusRepository repo;

    //create an empty TXT file
    @BeforeEach
    void setUp() throws IOException {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        repo = new BusRepository(TEST_FILE);
    }

    //delete the TXT file
    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    //test data
    String ValidBusID = "12345678";
    int ValidCapacity = 40;
    double ValidFuelLevel = 75.0;
    String ValidFuelType = "Diesel";

    String ValidBusID2 = "87654321";
    int ValidCapacity2 = 30;
    double ValidFuelLevel2 = 50.0;
    String ValidFuelType2 = "Hybrid";

    //test 1 check if a valid bus is saved and retrieved correctly from the TXT file
    @Test
    void TestIntegration_ValidBus() throws IOException {
        Bus bus = new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
        repo.add(bus);

        Bus retrieved = repo.retrieve(ValidBusID);

        assertNotNull(retrieved);
        assertEquals(ValidBusID, retrieved.getBusID());
        assertEquals(ValidCapacity, retrieved.getCapacity());
        assertEquals(ValidFuelLevel, retrieved.getFuelLevel());
        assertEquals(ValidFuelType, retrieved.getFuelType());
    }

    //test 2 check if an invalid bus ID is reject and nothing is input to the TXT file
    @Test
    void TestIntegration_InvalidBus() throws IOException {
        try {
            //the wrong format of InvalidBusID should fail B1
            String InvalidBusID = "INVALID1";
            new Bus(InvalidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType);
            fail("Bus ID format is invalid");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("B1"));
        }

        //TXT file should be empty since the ID failure
        assertEquals(0, repo.count());
    }

    //test 3 check if the updated data is save correctly to the TXT file
    @Test
    void TestIntegration_Update() throws IOException {
        //adding a valid bus
        repo.add(new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType));


        //update new data
        int New_Capacity = 30;
        double New_FuelLevel = 50.0;
        String New_FuelType = "Hybrid";
        repo.update(ValidBusID, New_Capacity, New_FuelLevel, New_FuelType);

        //check if updated data saved correctly
        Bus updated = repo.retrieve(ValidBusID);
        assertNotNull(updated);
        assertEquals(New_Capacity, updated.getCapacity());
        assertEquals(New_FuelLevel, updated.getFuelLevel());
        assertEquals(New_FuelType, updated.getFuelType());
        //check if ValidBusID is unchanged
        assertEquals(ValidBusID, updated.getBusID());
    }

    //test 4 check that the bus count increases correctly when buses are added
    @Test
    void TestIntegration_CountCheck() throws IOException {
        //check an empty TXT file
        assertEquals(0, repo.count());

        //add the first bus
        repo.add(new Bus(ValidBusID, ValidCapacity, ValidFuelLevel, ValidFuelType));
        assertEquals(1, repo.count());


        //add the second bus
        repo.add(new Bus(ValidBusID2, ValidCapacity2, ValidFuelLevel2, ValidFuelType2));
        assertEquals(2, repo.count());
    }
}
