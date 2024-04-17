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

            System.out.println("Mean Body Weight: " + CellStatistics.getMeanBodyWeight(cellList));
            System.out.println("Median Body Weight: " + CellStatistics.getMedianBodyWeight(cellList));

            System.out.println("Unique OEMs: " + listUniqueValues(cellList, "oem"));
            System.out.println("Unique launch years: " + listUniqueValues(cellList, "launch_announced"));
            System.out.println("Number of phones with only one sensor: " + countPhonesWithOneSensor(cellList));
            System.out.println("Year with the most launches: " + findYearWithMostLaunches(cellList));

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
                if (parts.length != 7) {  // Adjust the number based on your CSV structure
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                Cell cell = new Cell(parts[0], parts[1], parts[2], parts[3], Float.parseFloat(parts[4]), Float.parseFloat(parts[5]), parts[6]);
                cellList.add(cell);
            }
        }
        return cellList;
    }

    public static void cleanAndTransformData(List<Cell> cellList) {
        for (Cell cell : cellList) {
            cell.setLaunchAnnounced(transformToYear(cell.getLaunchAnnounced()));
            cell.setLaunchStatus(transformToYearOrStatus(cell.getLaunchStatus()));
            cell.setBodyWeight(Float.parseFloat(transformToFloat(String.valueOf(cell.getBodyWeight()))));
            cell.setDisplaySize(Float.parseFloat(transformToFloat(String.valueOf(cell.getDisplaySize()))));
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

    private static String transformToFloat(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return "0"; // Default to "0" if the input is invalid
        }
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group();
        }
        return "0"; // Default to "0" if no digits are found
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
        HashMap<String, Integer> yearCounts = new HashMap<>();
        for (Cell cell : cellList) {
            String year = cell.getLaunchAnnounced();
            yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
        }
        int maxCount = 0;
        String yearWithMostLaunches = "";
        for (Map.Entry<String, Integer> entry : yearCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                yearWithMostLaunches = entry.getKey();
            }
        }
        return yearWithMostLaunches.isEmpty() ? 0 : Integer.parseInt(yearWithMostLaunches);
    }

    static class CellStatistics {
        public static double getMeanBodyWeight(List<Cell> cells) {
            double sum = 0;
            for (Cell cell : cells) {
                sum += cell.getBodyWeight();
            }
            return cells.size() > 0 ? sum / cells.size() : 0;
        }

        public static double getMedianBodyWeight(List<Cell> cells) {
            List<Double> weights = new ArrayList<>();
            for (Cell cell : cells) {
                if (cell.getBodyWeight() != 0) {
                    weights.add((double) cell.getBodyWeight());
                }
            }
            weights.sort(Double::compare);
            int middle = weights.size() / 2;
            if (weights.size() % 2 == 1) {
                return weights.get(middle);
            } else {
                return (weights.get(middle - 1) + weights.get(middle)) / 2.0;
            }
        }
    }

    static class Cell {
        private String oem;
        private String model;
        private String launchAnnounced;
        private String launchStatus;
        private float bodyWeight;
        private float displaySize;
        private String featuresSensors;

        public Cell(String oem, String model, String launchAnnounced, String launchStatus, float bodyWeight, float displaySize, String featuresSensors) {
            this.oem = oem;
            this.model = model;
            this.launchAnnounced = launchAnnounced;
            this.launchStatus = launchStatus;
            this.bodyWeight = bodyWeight;
            this.displaySize = displaySize;
            this.featuresSensors = featuresSensors;
        }

        // Getters and setters
        public String getOem() { return oem; }
        public String getModel() { return model; }
        public String getLaunchAnnounced() { return launchAnnounced; }
        public String getLaunchStatus() { return launchStatus; }
        public float getBodyWeight() { return bodyWeight; }
        public float getDisplaySize() { return displaySize; }
        public String getFeaturesSensors() { return featuresSensors; }

        public void setLaunchAnnounced(String launchAnnounced) { this.launchAnnounced = launchAnnounced; }
        public void setLaunchStatus(String launchStatus) { this.launchStatus = launchStatus; }
        public void setBodyWeight(float bodyWeight) { this.bodyWeight = bodyWeight; }
        public void setDisplaySize(float displaySize) { this.displaySize = displaySize; }
    }
}

