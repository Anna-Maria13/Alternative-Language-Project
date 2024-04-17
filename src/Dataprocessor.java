import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessor {
    private static final String CSV_FILE = "cells.csv";

    public static void main(String[] args) {
        try {
            List<Cell> cellList = readCSV(CSV_FILE);
            cleanAndTransformData(cellList);

            // Example usage of additional methods
            CellStatistics statistics = calculateStatistics(cellList);
            System.out.println("Mean body weight: " + statistics.getMeanBodyWeight());
            System.out.println("Median body weight: " + statistics.getMedianBodyWeight());

            List<String> uniqueOems = listUniqueValues(cellList, "oem");
            System.out.println("Unique OEMs: " + uniqueOems);

            List<String> uniqueYears = listUniqueValues(cellList, "launch_announced");
            System.out.println("Unique launch years: " + uniqueYears);

            int phonesWithOneSensor = countPhonesWithOneSensor(cellList);
            System.out.println("Number of phones with only one sensor: " + phonesWithOneSensor);

            int yearWithMostLaunches = findYearWithMostLaunches(cellList);
            System.out.println("Year with the most launches: " + yearWithMostLaunches);

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // Step 1: Read the CSV file with exception handling for input validation
    public static List<Cell> readCSV(String filename) throws IOException {
        List<Cell> cellList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 12) {
                    throw new IllegalArgumentException("Invalid number of columns in CSV file");
                }
                Cell cell = new Cell(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9], parts[10], parts[11]);
                cellList.add(cell);
            }
        }
        return cellList;
    }

    // Step 2: Perform data cleaning and transformation
    public static void cleanAndTransformData(List<Cell> cellList) {
        for (Cell cell : cellList) {
            // Clean and transform attributes as needed
            cell.setLaunchAnnounced(transformToYear(cell.getLaunchAnnounced()));
            cell.setLaunchStatus(transformToYearOrStatus(cell.getLaunchStatus()));
            cell.setBodyWeight(transformToFloat(cell.getBodyWeight()));
            cell.setDisplaySize(transformToFloat(cell.getDisplaySize()));
            // Additional transformations for other attributes
        }
    }

    // Helper method to transform launch_announced to year
    private static String transformToYear(String launchAnnounced) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(launchAnnounced);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    // Helper method to transform launch_status to year or status
    private static String transformToYearOrStatus(String launchStatus) {
        if (launchStatus.matches("\\d{4}")) {
            return launchStatus;
        } else if (launchStatus.equals("Discontinued") || launchStatus.equals("Cancelled")) {
            return launchStatus;
        } else {
            return null;
        }
    }

    // Helper method to transform body_weight and display_size to float
    private static Float transformToFloat(String value) {
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        } else {
            return null;
        }
    }

    // Additional method to calculate statistics on cell attributes
    public static CellStatistics calculateStatistics(List<Cell> cellList) {
        // Implement calculation logic here
        return new CellStatistics(); // Placeholder, replace with actual implementation
    }

    // Additional method to list unique values for a specific column
    public static List<String> listUniqueValues(List<Cell> cellList, String columnName) {
        List<String> uniqueValues = new ArrayList<>();
        for (Cell cell : cellList) {
            String value = getValueForColumn(cell, columnName);
            if (!uniqueValues.contains(value)) {
                uniqueValues.add(value);
            }
        }
        return uniqueValues;
    }

    // Additional method to get the value of a specific column for a cell
    private static String getValueForColumn(Cell cell, String columnName) {
        switch (columnName) {
            case "oem":
                return cell.getOem();
            case "model":
                return cell.getModel();
            case "launch_announced":
                return cell.getLaunchAnnounced();
            // Add cases for other columns as needed
            default:
                return null;
        }
    }

    // Additional method to count phones with only one feature sensor
    public static int countPhonesWithOneSensor(List<Cell> cellList) {
        int count = 0;
        for (Cell cell : cellList) {
            if (cell.getFeaturesSensors() != null && cell.getFeaturesSensors().split(",").length == 1) {
                count++;
            }
        }
        return count;
    }

    // Additional method to find the year with the most launches
    public static int findYearWithMostLaunches(List<Cell> cellList) {
        Map<String, Integer> yearCounts = new HashMap<>();
        for (Cell cell : cellList) {
            String year = cell.getLaunchAnnounced();
            yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
        }
        int maxCount = 0;
        int yearWithMostLaunches = 0;
        for (Map.Entry<String, Integer> entry : yearCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                yearWithMostLaunches = Integer.parseInt(entry.getKey());
            }
        }
        return yearWithMostLaunches;
    }

    // Define Cell class
    static class Cell {
        private String oem;
        private String model;
        private String launchAnnounced;
        private String launchStatus;
        private String bodyWeight;
        private String displaySize;
        private String featuresSensors;

        public Cell(String oem, String model, String launchAnnounced, String launchStatus, String bodyWeight, String displaySize, String featuresSensors) {
            this.oem = oem;
            this.model = model;
            this.launchAnnounced = launchAnnounced;
            this.launchStatus = launchStatus;
            this.bodyWeight = bodyWeight;
            this.displaySize = displaySize;
            this.featuresSensors = featuresSensors;
        }

        public String getOem() {
            return oem;
        }

        public String getModel() {
            return model;
        }

        public String getLaunchAnnounced() {
            return launchAnnounced;
        }

        public String getLaunchStatus() {
            return launchStatus;
        }

        public String getBodyWeight() {
            return bodyWeight;
        }

        public String getDisplaySize() {
            return displaySize;
        }

        public String getFeaturesSensors() {
            return featuresSensors;
        }
    }

    // Define CellStatistics class (if needed)
    static class CellStatistics {
        // Define attributes and methods to calculate statistics
    }
} 

