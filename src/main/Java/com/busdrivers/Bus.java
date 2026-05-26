package com.busdrivers;

// bus class for the intelligent bus driver guidance system
public class Bus {

    private String bus_id;
    private int capacity;
    private double fuel_level;
    private String fuel_type; // diesel or hybrid or electricity

    // creates a new bus and validates all the fields before storing them
    public Bus(String bus_id, int capacity, double fuel_level, String fuel_type) {

        if (!isValidBusID(bus_id))
            throw new IllegalArgumentException("B1 Violation: invalid busID: " + bus_id);
        if (capacity < 0)
            throw new IllegalArgumentException("capacity cannot be negative.");
        if (fuel_level < 0 || fuel_level > 100)
            throw new IllegalArgumentException("fuel level must be between 0 and 100.");
        if (!isValidFuelType(fuel_type))
            throw new IllegalArgumentException("invalid fuelType: " + fuel_type);

        this.bus_id = bus_id;
        this.capacity = capacity;
        this.fuel_level = fuel_level;
        this.fuel_type = fuel_type;
    }

    public String getBusID() {
        return bus_id;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuel_level;
    }

    public String getFuelType() {
        return fuel_type;
    }

    // updates mutable fields only
    // bus condition 2 capacity cannot increase during an update, it can only
    // decrease or stay
    // the same
    public void update(int new_capacity, double new_fuel_level, String new_fuel_type) {

        if (new_capacity > this.capacity)
            throw new IllegalArgumentException("B2 Violation: capacity cannot increase during an update.");
        if (new_capacity < 0)
            throw new IllegalArgumentException("capacity cannot be negative.");
        if (new_fuel_level < 0 || new_fuel_level > 100)
            throw new IllegalArgumentException("fuel level must be between 0 and 100.");
        if (!isValidFuelType(new_fuel_type))
            throw new IllegalArgumentException("invalid fuelType: " + new_fuel_type);

        this.capacity = new_capacity;
        this.fuel_level = new_fuel_level;
        this.fuel_type = new_fuel_type;
    }

    // bus condition 3 hecks that a driver is not older than 50 if the bus capacity
    // is 50 or more
    // and returns true if the driver is allowed to drive this bus
    public boolean isDriverAgeValid(Driver driver) {
        int age = calculateAge(driver.getBirthdate());
        if (age > 50 && this.capacity >= 50)
            return false;
        return true;
    }

    // bus condition 4 checks that the driver has at least 5 years of experience for
    // electric
    // buses and returns true if the driver is allowed to drive this bus
    public boolean isDriverExperienceValid(Driver driver) {
        if (this.fuel_type.equals("Electricity") && driver.getExperienceYears() < 5)
            return false;
        return true;
    }

    // bus condition 5 hecks that the driver holds a Heavy or PublicTransport
    // licence for
    // electric or hybrid buses and returns true if the driver is allowed to drive
    // this bus
    public boolean isDriverLicenceValid(Driver driver) {
        if (this.fuel_type.equals("Electricity") || this.fuel_type.equals("Hybrid")) {
            if (!driver.getLicenseType().equals("Heavy") && !driver.getLicenseType().equals("PublicTransport"))
                return false;
        }
        return true;
    }

    // busess condition 1 checks that the bus ID is exactly 8 digits
    public static boolean isValidBusID(String id) {
        if (id == null || id.length() != 8)
            return false;
        for (int i = 0; i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i)))
                return false;
        }
        return true;
    }

    // checking that the fuel type is one of the three allowed values
    public static boolean isValidFuelType(String fuel_type) {
        if (fuel_type == null)
            return false;
        return fuel_type.equals("Diesel") || fuel_type.equals("Hybrid") || fuel_type.equals("Electricity");
    }

    // calculating the age in years from a dd-mm-yyyy birthdate string
    private int calculateAge(String birth_date) {
        int birth_year = Integer.parseInt(birth_date.substring(6, 10));
        int birth_month = Integer.parseInt(birth_date.substring(3, 5));
        int birth_day = Integer.parseInt(birth_date.substring(0, 2));
        java.time.LocalDate today = java.time.LocalDate.now();
        int age = today.getYear() - birth_year;
        if (today.getMonthValue() < birth_month ||
                (today.getMonthValue() == birth_month && today.getDayOfMonth() < birth_day))
            age--;
        return age;
    }
}