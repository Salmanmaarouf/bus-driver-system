package com.busdrivers;

// driver class for the intelligent bus driver guidance system
public class Driver {

    private String driver_id;
    private String name;
    private int experience_years;
    private String license_type; // the optionas are light or medium or heavy or PublicTransport
    private String address; // format: StreetNumber|StreetName|City|State|Country
    private String birth_date; // date format: dd-mm-yyyy

    // creating a new driver and validating all the fields before storing them
    public Driver(String driver_id, String name, int experience_years,
            String license_type, String address, String birth_date) {

        if (!isValidDriverID(driver_id))
            throw new IllegalArgumentException("D1 violation: invalid driverID: " + driver_id);
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("name cannot be empty.");
        if (experience_years < 0)
            throw new IllegalArgumentException("experience years cannot be negative.");
        if (!isValidLicenseType(license_type))
            throw new IllegalArgumentException("invalid licenseType: " + license_type);
        if (!isValidAddress(address))
            throw new IllegalArgumentException("D2 violation: invalid address format.");
        if (!isValidBirthdate(birth_date))
            throw new IllegalArgumentException("D3 violation: invalid birthdate format.");

        this.driver_id = driver_id;
        this.name = name;
        this.experience_years = experience_years;
        this.license_type = license_type;
        this.address = address;
        this.birth_date = birth_date;
    }

    public String getDriverID() {
        return driver_id;
    }

    public String getName() {
        return name;
    }

    public int getExperienceYears() {
        return experience_years;
    }

    public String getLicenseType() {
        return license_type;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birth_date;
    }

    // updating fields that are allowed to change, D5 and D4 not included here
    public void update(int new_experience_years, String new_license_type,
            String new_address, String new_birth_date) {

        if (new_experience_years < 0)
            throw new IllegalArgumentException("experience years cannot be negative.");
        if (this.experience_years > 10 && !this.license_type.equals(new_license_type))
            throw new IllegalArgumentException(
                    "D4 violation: cannot change license type for a driver with more than 10 years of experience.");
        if (!isValidLicenseType(new_license_type))
            throw new IllegalArgumentException("invalid licenseType: " + new_license_type);
        if (!isValidAddress(new_address))
            throw new IllegalArgumentException("D2 violation: invalid address format.");
        if (!isValidBirthdate(new_birth_date))
            throw new IllegalArgumentException("D3 violation: invalid birthdate format.");

        this.experience_years = new_experience_years;
        this.license_type = new_license_type;
        this.address = new_address;
        this.birth_date = new_birth_date;
    }

    // checks that
    // 1. the driver id is exactly 10 chars, starts with two digits between 2 and 9,
    // 2. has at least 2 special characters between positions 3 and 8,
    // 3. and ends with two uppercase letters
    public static boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10)
            return false;
        if (!Character.isDigit(id.charAt(0)) || id.charAt(0) < '2' || id.charAt(0) > '9')
            return false;
        if (!Character.isDigit(id.charAt(1)) || id.charAt(1) < '2' || id.charAt(1) > '9')
            return false;
        if (!Character.isLetter(id.charAt(8)) || !Character.isUpperCase(id.charAt(8)))
            return false;
        if (!Character.isLetter(id.charAt(9)) || !Character.isUpperCase(id.charAt(9)))
            return false;
        int special_count = 0;
        for (int i = 2; i <= 7; i++) {
            if (!Character.isLetterOrDigit(id.charAt(i)))
                special_count++;
        }
        return special_count >= 2;
    }

    // checks that address follows the format
    public static boolean isValidAddress(String address) {
        if (address == null)
            return false;
        String[] parts = address.split("\\|", -1);
        if (parts.length != 5)
            return false;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].trim().isEmpty())
                return false;
        }
        return true;
    }

    // checking that the birthdate follows the format DD-MM-YYYY with day and month
    // ranges
    public static boolean isValidBirthdate(String birth_date) {
        if (birth_date == null)
            return false;
        if (!birth_date.matches("\\d{2}-\\d{2}-\\d{4}"))
            return false;
        int day = Integer.parseInt(birth_date.substring(0, 2));
        int month = Integer.parseInt(birth_date.substring(3, 5));
        if (day < 1 || day > 31)
            return false;
        if (month < 1 || month > 12)
            return false;
        return true;
    }

    // checks that the licence type is one of the four allowed ones
    public static boolean isValidLicenseType(String license_type) {
        if (license_type == null)
            return false;
        return license_type.equals("Light") || license_type.equals("Medium")
                || license_type.equals("Heavy") || license_type.equals("PublicTransport");
    }
}