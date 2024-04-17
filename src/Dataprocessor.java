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

    public static List<Cell> readCSV(String filename) throws IOException {
        List<Cell> cellList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header line
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 12) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim().equals("-") ? null : parts[i].trim(); // Replace "-" with null
                }
                try {
                    Cell cell = new Cell(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9], parts[10], parts[11]);
                    cellList.add(cell);
                } catch (Exception e) {
                    System.out.println("Error creating cell from line: " + line + "; Error: " + e.getMessage());
                }
            }
        }
        return cellList;
    }

    public static void cleanAndTransformData(List<Cell> cellList) {
        for (Cell cell : cellList) {
            cell.setLaunchAnnounced(transformToYear(cell.getLaunchAnnounced()));
            cell.setLaunchStatus(transformToYearOrStatus(cell.getLaunchStatus()));
            cell.setBodyWeight(transformToFloat(cell.getBodyWeight()));
            cell.setDisplaySize(transformToFloat(cell.getDisplaySize()));
        }
    }

    private static String transformToYear(String launchAnnounced) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(launchAnnounced);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    private static String transformToYearOrStatus(String launchStatus) {
        if (launchStatus.matches("\\d{4}")) {
            return launchStatus;
        } else if (launchStatus.equals("Discontinued") || launchStatus.equals("Cancelled")) {
            return launchStatus;
        } else {
            return null;
        }
    }

    private static Float transformToFloat(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return null; // Handling missing or invalid values
        }
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*"); // Regex to extract a floating number
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        }
        return null; // Return null if no numeric data is found
    }

    public static CellStatistics calculateStatistics(List<Cell> cellList) {
        // Implement calculation logic here
        return new CellStatistics(); // Placeholder
    }

    public static List<String> listUniqueValues(List<Cell> cellList, String columnName) {
        List<String> uniqueValues = new ArrayList<>();
        for (Cell cell : cellList) {
            String value = getValueForColumn(cell, columnName);
            if (value != null && !uniqueValues.contains(value)) {
                uniqueValues.add(value);
            }
        }
        return uniqueValues;
    }

    private static String getValueForColumn(Cell cell, String columnName) {
        switch (columnName) {
            case "oem":
                return cell.getOem();
            case "model":
                return cell.getModel();
            case "launch_announced":
                return cell.getLaunchAnnounced();
            default:
                return null;
        }
    }

    public static int countPhonesWithOneSensor(List<Cell> cellList) {
        int count = 0;
        for (Cell cell : cellList) {
            if (cell.getFeaturesSensors() != null && cell.getFeaturesSensors().split(",").length == 1) {
                count++;
            }
        }
        return count;
    }

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

        // Getters and setters as needed
    }

    // Define CellStatistics class
    static class CellStatistics {
        // Define attributes and methods to calculate statistics

        public double getMeanBodyWeight() {
            // Placeholder for actual implementation
            return 0.0;
        }

        public double getMedianBodyWeight() {
            // Placeholder for actual implementation
            return 0.0;
        }
    }
}

