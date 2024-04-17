public class Main {
    public static void main(String[] args) {
        // Your application logic goes here
        
        // Example usage of Dataprocessor class
        try {
            // Read data from CSV file
            List<Cell> cellList = DataProcessor.readCSV("cells.csv");

            // Clean and transform data
            DataProcessor.cleanAndTransformData(cellList);

            // Example usage of additional methods
            CellStatistics statistics = DataProcessor.calculateStatistics(cellList);
            System.out.println("Mean body weight: " + statistics.getMeanBodyWeight());
            System.out.println("Median body weight: " + statistics.getMedianBodyWeight());

            List<String> uniqueOems = DataProcessor.listUniqueValues(cellList, "oem");
            System.out.println("Unique OEMs: " + uniqueOems);

            List<String> uniqueYears = DataProcessor.listUniqueValues(cellList, "launch_announced");
            System.out.println("Unique launch years: " + uniqueYears);

            int phonesWithOneSensor = DataProcessor.countPhonesWithOneSensor(cellList);
            System.out.println("Number of phones with only one sensor: " + phonesWithOneSensor);

            int yearWithMostLaunches = DataProcessor.findYearWithMostLaunches(cellList);
            System.out.println("Year with the most launches: " + yearWithMostLaunches);

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }
}
