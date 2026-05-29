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

public class DriverIntegrationTest { 
    //setup
    private static final String TEST_FILE = "test_drivers.txt";
    private DriverRepository repo;


    //create an empty TXT file
    @BeforeEach
    void setUp() throws IOException {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        repo = new DriverRepository(TEST_FILE);
    }

    //delete the TXT file
    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    //test data
    String ValidID = "22@#aa22AA";
    String ValidNAME = "AA";
    int YearOfExperienced = 5;
    String ValidADDRESS = "2|Reservoir|Melbourne|VIC|Australia";
    String ValidBIRTH = "11-01-2001";
    String ValidLICENSE = "Heavy";

    String ValidID2 = "33@#bb33BB";
    String ValidNAME2 = "BB";
    int YearOfExperienced2 = 3;
    String ValidADDRESS2 = "1|Collingwood|Melbourne|VIC|Australia";
    String ValidBIRTH2 = "22-02-2002";
    String ValidLICENSE2 = "Light";



    //test 1 check if a valid driver is save and retrieve correct from the TXT file
    @Test
    void TestIntegration_ValidDriver() throws IOException {
        Driver driver = new Driver(ValidID, ValidNAME, YearOfExperienced, ValidLICENSE, ValidADDRESS, ValidBIRTH);
        repo.add(driver);
        Driver retrieve = repo.retrieve(ValidID);
        assertNotNull(retrieve);
        assertEquals(ValidID, retrieve.getDriverID());
        assertEquals(ValidNAME, retrieve.getName());
        assertEquals(YearOfExperienced, retrieve.getExperienceYears());
        assertEquals(ValidLICENSE, retrieve.getLicenseType());
        assertEquals(ValidADDRESS, retrieve.getAddress());
        assertEquals(ValidBIRTH, retrieve.getBirthdate());
    }

    //test 2 check if an invalid driver ID is reject and nothing is input to the TXT file
    @Test
    void testIntegration_InvalidDriver() throws IOException {
        try {
            //the wrong format of InvalidID should fail D1
            String InvalidID = "INVALID123";
            new Driver(InvalidID, ValidNAME, YearOfExperienced, ValidLICENSE, ValidADDRESS, ValidBIRTH);
            fail("Driver ID format is invalid");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("D1"));
        }

        //check
        //TXT file should be empty since the ID failure
        assertEquals(0, repo.count());
    }

    //test 3 check if the updated data is save correctly to the TXT file
    @Test
    void testIntegration_Update() throws IOException {
        //adding a valid driver
        repo.add(new Driver(ValidID, ValidNAME, YearOfExperienced, ValidLICENSE, ValidADDRESS, ValidBIRTH));

        //update new data
        int New_YearOfExperienced = 8;
        String New_LICENSE = "Medium";
        String New_ADDRESS = "23|Victoria Market|Melbourne|VIC|Australia";
        String New_BIRTH = "23-03-2003";
        repo.update(ValidID, New_YearOfExperienced, New_LICENSE, New_ADDRESS, New_BIRTH);

        //check if updated data saved correctly
        Driver updated = repo.retrieve(ValidID);
        assertNotNull(updated);
        assertEquals(8, updated.getExperienceYears());
        assertEquals("Medium", updated.getLicenseType());
        assertEquals(New_ADDRESS, updated.getAddress());
        assertEquals(New_BIRTH, updated.getBirthdate());

        //check if both ValidID and ValidName are the same
        assertEquals(ValidID, updated.getDriverID());
        assertEquals(ValidNAME, updated.getName());
    }

    //test 4 check that the driver counts adding correctly when driver is added
    @Test
    void testIntegration_CountCheck() throws IOException {

        //check an empty TXT file
        assertEquals(0, repo.count());

        //add the first driver
        repo.add(new Driver(ValidID, ValidNAME, YearOfExperienced, ValidLICENSE, ValidADDRESS, ValidBIRTH));

        assertEquals(1, repo.count());
        //add the second driver
        repo.add(new Driver(ValidID2, ValidNAME2, YearOfExperienced2, ValidLICENSE2, ValidADDRESS2, ValidBIRTH2));

        assertEquals(2, repo.count());
    }
}