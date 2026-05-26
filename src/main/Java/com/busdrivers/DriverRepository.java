package com.busdrivers;

import java.io.*;
import java.util.*;

// stores and retrieves driver records using a plain TXT file
// one driver per line, format: driver_id|name|experience_years|license_type|streetNo|streetName|city|state|country|birth_date
public class DriverRepository {

    private String file_path;

    // creates the repository and the TXT file if it does not already exist
    public DriverRepository(String file_path) throws IOException {
        this.file_path = file_path;
        File file = new File(file_path);
        if (file.getParentFile() != null)
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
    }

    // adds a new driver to the TXT file
    // D1: rejects the driver if the same ID already exists
    public void add(Driver driver) throws IOException {
        if (retrieve(driver.getDriverID()) != null)
            throw new IllegalArgumentException(
                    "D1 Violation: driver ID '" + driver.getDriverID() + "' already exists.");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file_path, true));
            writer.write(driverToLine(driver));
            writer.newLine();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    // returns the driver with the given ID, or null if no match is found
    public Driver retrieve(String driver_id) throws IOException {
        List<String> lines = readAllLines();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty())
                continue;
            Driver d = lineToDriver(line);
            if (d != null && d.getDriverID().equals(driver_id))
                return d;
        }
        return null;
    }

    // updates a driver's mutable fields in the TXT file
    // D4 and D5 are enforced inside Driver.update()
    public void update(String driver_id, int new_experience, String new_license_type,
            String new_address, String new_birth_date) throws IOException {

        List<String> lines = readAllLines();
        List<String> updated_lines = new ArrayList<String>();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty())
                continue;
            Driver d = lineToDriver(line);
            if (d != null && d.getDriverID().equals(driver_id)) {
                d.update(new_experience, new_license_type, new_address, new_birth_date);
                updated_lines.add(driverToLine(d));
                found = true;
            } else {
                updated_lines.add(line);
            }
        }

        if (!found)
            throw new IllegalArgumentException("driver ID '" + driver_id + "' not found.");

        writeAllLines(updated_lines);
    }

    // returns the total number of drivers currently stored in the TXT file
    public int count() throws IOException {
        int count = 0;
        List<String> lines = readAllLines();
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).trim().isEmpty())
                count++;
        }
        return count;
    }

    // reads every line from the TXT file and returns them as a list
    private List<String> readAllLines() throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file_path));
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        } finally {
            if (reader != null)
                reader.close();
        }
        return lines;
    }

    // overwrites the TXT file with the updated list of lines
    private void writeAllLines(List<String> lines) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file_path, false));
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    // converts a driver object into a pipe-delimited line for storage in the TXT
    // file
    private String driverToLine(Driver d) {
        return d.getDriverID() + "|" + d.getName() + "|" + d.getExperienceYears() + "|"
                + d.getLicenseType() + "|" + d.getAddress() + "|" + d.getBirthdate();
    }

    // parses a pipe-delimited line from the TXT file back into a driver object
    // the address has 5 parts so there are 10 tokens in total per line
    private Driver lineToDriver(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 10)
                return null;
            String address = p[4] + "|" + p[5] + "|" + p[6] + "|" + p[7] + "|" + p[8];
            return new Driver(p[0], p[1], Integer.parseInt(p[2].trim()), p[3], address, p[9]);
        } catch (Exception e) {
            return null;
        }
    }
}