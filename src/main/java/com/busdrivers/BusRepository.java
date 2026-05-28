package com.busdrivers;

import java.io.*;
import java.util.*;

// stores and retrieves bus records using a plain TXT file
// one bus per line, format: bus_id|capacity|fuel_level|fuel_type
public class BusRepository {

    private String file_path;

    // creates the repository and the TXT file if it does not already exist
    public BusRepository(String file_path) throws IOException {
        this.file_path = file_path;
        File file = new File(file_path);
        if (file.getParentFile() != null)
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
    }

    // adds a new bus to the TXT file
    // this condition rejects the bus if the same ID already exists
    public void add(Bus bus) throws IOException {
        if (retrieve(bus.getBusID()) != null)
            throw new IllegalArgumentException("B1 Violation: bus ID '" + bus.getBusID() + "' already exists.");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file_path, true));
            writer.write(busToLine(bus));
            writer.newLine();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    // returns the bus with the given ID, or null if no match is found
    public Bus retrieve(String bus_id) throws IOException {
        List<String> lines = readAllLines();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty())
                continue;
            Bus b = lineToBus(line);
            if (b != null && b.getBusID().equals(bus_id))
                return b;
        }
        return null;
    }

    // updates a bus's mutable fields in the TXT file, this condition is enforced
    // inside Bus.update()
    public void update(String bus_id, int new_capacity, double new_fuel_level,
            String new_fuel_type) throws IOException {

        List<String> lines = readAllLines();
        List<String> updated_lines = new ArrayList<String>();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty())
                continue;
            Bus b = lineToBus(line);
            if (b != null && b.getBusID().equals(bus_id)) {
                b.update(new_capacity, new_fuel_level, new_fuel_type);
                updated_lines.add(busToLine(b));
                found = true;
            } else {
                updated_lines.add(line);
            }
        }

        if (!found)
            throw new IllegalArgumentException("bus ID '" + bus_id + "' not found.");

        writeAllLines(updated_lines);
    }

    // returns the total number of buses currently stored in the TXT file
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

    // converts a bus object into a pipe-delimited line for storage in the TXT file
    private String busToLine(Bus b) {
        return b.getBusID() + "|" + b.getCapacity() + "|" + b.getFuelLevel() + "|" + b.getFuelType();
    }

    // parses a pipe-delimited line from the TXT file back into a bus object
    private Bus lineToBus(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 4)
                return null;
            return new Bus(p[0], Integer.parseInt(p[1].trim()),
                    Double.parseDouble(p[2].trim()), p[3]);
        } catch (Exception e) {
            return null;
        }
    }
}