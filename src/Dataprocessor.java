import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessor {

    public static void main(String[] args) {
        List<Cell> cellList = readCSV("cells.csv");
        cleanAndTransformData(cellList);
        // Additional processing or output here

        // Example usage of additional methods
        CellStatistics statistics = calculateStatistics(cellList);
        System.out.println("Mean body weight: " + statistics.getMeanBodyWeight());
        System.out.println("Median body weight: " + statistics.getMedianBodyWeight());

        List<String> uniqueValues = listUniqueValues(cellList, "oem");
        System.out.println("Unique OEMs: " + uniqueValues);

        // Example usage of exception handling
        try {
            List<Cell> emptyList = readCSV("empty_file.csv");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Step 1: Read the CSV file with exception handling for input validation
    private static List<Cell> readCSV(String filename) throws IOException {
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
    private static void cleanAndTransformData(List<Cell> cellList) {
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
    private static CellStatistics calculateStatistics(List<Cell> cellList) {
        // Implement calculation logic here
        return new CellStatistics(); // Placeholder, replace with actual implementation
    }

    // Additional method to list unique values for a specific column
    private static List<String> listUniqueValues(List<Cell> cellList, String columnName) {
        // Implement logic to extract unique values from the specified column
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
        // Implement logic to get the value of the specified column from the cell
        switch (columnName) {
            case "oem":
                return cell.getOem();
            case "model":
                return cell.getModel();
            // Add cases for other columns as needed
            default:
                return null;
        }
    }

    // Define Cell class
    static class Cell {
        // Define attributes and constructor
    }

    // Define CellStatistic class
    static class CellStatistics {
        // Define attributes and methods to calculate statistics
    }
}



