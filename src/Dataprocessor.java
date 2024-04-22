import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.*;

public class DataProcessor {

    public static void main(String[] args) {
        String csvFile = "cells.csv"; 
        
        HashMap<Integer, Cell> cellMap = new HashMap<>();
        ArrayList<String> launchAnnouncedList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            //Skip the headers
            br.readLine();
            
            String line;
            int id = 1; // Start ID from 1
            
            //Go through each row of the csv
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                //Take in input of csv into cell object
                Cell cell = new Cell();
                cell.setOEM(cleanStrings(data[0]));
                cell.setModel(cleanStrings(data[1]));

                launchAnnouncedList.add(data[2]);
            
                cell.setLaunchAnnounced(cleanLaunchAnnounced(data[2]));
                cell.setLaunchStatus(cleanLaunchStatus(data[3]));
                cell.setBodyDimensions(cleanStrings(data[4]));
                cell.setBodyWeight(cleanBodyWeight(data[5]));
                cell.setBodySim(cleanBodySim(data[6]));
                cell.setDisplayType(cleanStrings(data[7]));
                cell.setDisplaySize(cleanDisplaySize(data[8]));
                cell.setDisplayResolution(cleanStrings(data[9]));
                cell.setFeaturesSensors(cleanFeatureSensors(data[10]));
                cell.setPlatformOS(cleanPlatformOS(data[11]));
                
                //Store each cell object in hashmap, mapped to its row number.
                cellMap.put(id++, cell);
            }
            displayUniqueData(cellMap);

            boolean userInput = true;
            Scanner scanner = new Scanner(System.in);

            while (userInput) {
                System.out.println("\nOptions:");
                System.out.println("1. Add an object to the cell");
                System.out.println("2. Delete an existing cell by index");
                System.out.println("3. Calculate Mode of Launch Announced Column");
                System.out.println("4. Display unique values of each column");
                System.out.println("5. Print out each column's content");
                System.out.println("6. Print out mean of Body Weight");
                System.out.println("7. Print out median of Display Size");
                System.out.println("8. Find highest average body weight of an oem");
                System.out.println("9. Find the OEM and Models that were announced in one year and released in another");
                System.out.println("10. Return Phones with only one sensor");
                System.out.println("11. Return year greater than 1999 with most phones");
                System.out.println("12. Exit");



    
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
    
                switch (choice) {
                    case 1:
                        String temp = addObject(cellMap, scanner);
                        System.out.print("\n" + temp);
                        break;

                    case 2:
                        System.out.print("Enter index of cell to delete");
                        String s = deleteObject(cellMap, scanner);
                        System.out.print("\n" + s);
                        break;

                    case 3:
                        int mode = modeLaunchAnnounced(cellMap);
                        System.out.print("\nThe mode is: " + mode);
                        break;

                    case 4:
                        displayUniqueData(cellMap);
                        break;

                    case 5: 
                        printContents(cellMap);
                        break;

                    case 6:
                        bodyWeightMean(cellMap);
                        break;
                    
                    case 7: 
                        displaySizeMedian(cellMap);
                        break;

                    case 8: 
                        highestCompanyBodyWeight(cellMap);
                        break;
                    case 9:
                        phonesDifferentYears(cellMap, launchAnnouncedList);
                        break;
                    case 10:
                        phoneOneSensor(cellMap);
                        break;
                    case 11:
                        yearWithMostPhones(cellMap);
                        break;
                    case 12:
                        userInput = false;
                        break;
                        
                    default:
                        System.out.println("\nInvalid choice. Please enter a valid option.");
                }
            }
           //Get unique data for each cell attribute
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the mode (most frequently occurring value) of the launchAnnounced values
     * in the given HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects where the keys are integers.
     * @return The mode of the launchAnnounced values in the given cellMap.
     */
    private static int modeLaunchAnnounced(HashMap<Integer, Cell> cellMap){
        Map<Integer, Integer> countMap = new HashMap<>();
        int mode = 0;
        int max = -1;

        for(Cell cell : cellMap.values()){
            countMap.put(cell.getlaunchAnnounced(), countMap.getOrDefault(cell.getlaunchAnnounced(), 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > max) {
                mode = entry.getKey();
                max = entry.getValue();
            }
        }
        return mode;
    }

    /**
     * Calculates the median of the displaySize values
     * in the given HashMap of Cell objects.
     *
     * @param cellMap A HashMap containing Cell objects where the keys are integers.
     * @return Returns nothing, prints out the median instead of returning it.
     */
    public static void displaySizeMedian(HashMap<Integer, Cell> cellMap){
        ArrayList<Float> displaySizeList = new ArrayList<>();

        for(Cell cell : cellMap.values()){
            if(cell.getDisplaySize() != null){
                displaySizeList.add(cell.getDisplaySize());
            }
        }

        Collections.sort(displaySizeList);
        int size = displaySizeList.size();
        Float median;

        if (size % 2 == 0) {
            int midIndex1 = size / 2 - 1;
            int midIndex2 = size / 2;
            median =  (displaySizeList.get(midIndex1) + displaySizeList.get(midIndex2)) / 2;
        } else {
            int midIndex = size / 2;
            median =  displaySizeList.get(midIndex);
        }

        System.out.println("\n\nThe median of display size is: " + median);
    }

    /**
    * Calculates the average(mean) of body weight values
    * in the given HashMap of Cell objects.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the mean instead of returning it.
    */
    public static void bodyWeightMean(HashMap<Integer, Cell> cellMap){
        Float total = (float) 0;
        Float numberOfValues = (float) 0;
        for(Cell cell : cellMap.values()){
            if(cell.getBodyWeight() != null){
                total += cell.getBodyWeight();
                numberOfValues++;
            }
        }
        Float mean = total/numberOfValues;

        System.out.println("\n\nThe mean of body weight is: " + mean);
    }

    /**
    * Print out the contents of the given HashMap
    * of Cell objects.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the contents instead of returning it.
    */
    public static void printContents(HashMap<Integer, Cell> cellMap){
        for(Cell cell : cellMap.values()){
            System.out.println("\n\nCell " + ": " + cell.getOEM() + ", "
            + cell.getModel() + ", " + (cell.getlaunchAnnounced() != null ? Integer.toString(cell.getlaunchAnnounced()) : "N/A")
            + ", " + cell.getLaunchStatus() + ", " + cell.getBodyDimensions() + ", "
            + (cell.getBodyWeight() != null ? Float.toString(cell.getBodyWeight()) : "N/A") + ", " + cell.getBodySim() + ", "
            + cell.getDisplayType() + ", " + (cell.getDisplaySize() != null ? Float.toString(cell.getDisplaySize()) : "N/A")
            + ", " + cell.getDisplayResolution() + ", " + cell.getFeaturesSensors() 
            + ", " + cell.getPlatformOS()
            );
        }
    }
    
    /**
    * Takes in the input of an integer, if the input cannot be parsed as an integer
    * it throws an exception and reasks the user to re-enter a valid input.
    * @param scanner Variable used to parse the user input.
    * @return Returns the integer parsed
    */
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }

    /**
    * This function and goes through the list of launch dates and launch announcements from the input file,
    * and if it detects that the launch date is greater than the launch announced, it adds it to a list of strings.
    * The strings are then printed at the end of the function, showing the oem and models of the cells where the product
    * was launcehd a year after it was announced.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @param launchAnnounced An ArrayList of strings holding the original launch announced strings before
    * it goes through data cleaning.
    * @return Returns nothing, prints out the model and oem of each cell that fulfills the criteria.
    */
    public static void phonesDifferentYears(HashMap<Integer, Cell> cellMap, ArrayList<String> launchAnnounced){
        ArrayList<String> oemAndModels = new ArrayList<>();
        Pattern pattern = Pattern.compile("Released *(\\d{4})");

        for(int i = 1; i <= cellMap.size(); i++){
            Cell cell = cellMap.get(i);
            if(cell.getlaunchAnnounced() != null){
                Matcher matcher = pattern.matcher(launchAnnounced.get(i-1));
                if (matcher.find()) {
                    int releaseYear = Integer.parseInt(matcher.group(1));
                    
                    if (releaseYear > cell.getlaunchAnnounced()) {
                        String s = "\n\nThe OEM is: " + cell.getOEM() + " and the model is: " + cell.getModel();
                        oemAndModels.add(s);
                    }
                }
            }
        }

        for (int i = 0; i < oemAndModels.size(); i++){
            System.out.println(oemAndModels.get(i));
        }
    }

    /**
    * Calculates the total amount of phones that have one sensor. It does this through checking if the 
    * input string contains a comma, as if it does contain a comma, that means there are multiple sensors.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the total amount of phones with one sensor
    */
    public static void phoneOneSensor(HashMap<Integer, Cell> cellMap){
        int count = 0;
        for(Cell cell: cellMap.values()){
            System.out.println(cell.getFeaturesSensors());
            String s = cell.getFeaturesSensors();
            if(!hasComma(s)){
                count++;
            }
        }

        System.out.println("\n\nTotal number of phones without more than one sensor: " + count);
    }

    /**
    * Finds the year (greater than 1999) with the most phones releaseed.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the corresponding year with the number of phones released.
    */
    public static void yearWithMostPhones(HashMap<Integer, Cell> cellMap){
        HashMap<Integer, Integer> yearAndNumReleases = new HashMap<>();
        for(Cell cell: cellMap.values()){
            if(cell.getlaunchAnnounced() != null && cell.getlaunchAnnounced() > 1999){
                yearAndNumReleases.put(cell.getlaunchAnnounced(), yearAndNumReleases.getOrDefault(cell.getlaunchAnnounced(), 0) + 1);
            }
        }

        String s = "";
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : yearAndNumReleases.entrySet()) {
            int year = entry.getKey();
            int numReleases = entry.getValue();

            if(numReleases > max){
                max = numReleases;
                s = "\n\nYear: " + year + ", Number of Releases: " + max;
            }
        }
        System.out.println(s);
    }

    /**
    * Boolean function that returns true if a string contains a comma
    * @param s Input string that is checked to see if it contains a comma
    * @return Returns true or false based on if a comma is found
    */
    public static boolean hasComma(String s){
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c == ','){
                return true;
            }
        }
        return false;
    }

    /**
    * Finds the company with the highest average body weight. It does this through using two hashmaps, which are 
    * mapped to a key of the company's oem, that keep track of the total weight and number of body weights for that company.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the highest average body weight and company.
    */
    public static void highestCompanyBodyWeight(HashMap <Integer, Cell> cellMap){
        HashMap <String, Float> bwMap = new HashMap<>();
        HashMap <String, Integer> companyCounter = new HashMap<>();

        for(Cell cell : cellMap.values()){
            if(cell.getBodyWeight() != null){
                bwMap.put(cell.getOEM(), bwMap.getOrDefault(cell.getOEM(), (float) 0) + cell.getBodyWeight());
                companyCounter.put(cell.getOEM(), companyCounter.getOrDefault(cell.getOEM(), 0) + 1);
            }
        }
        String highestMeanOEM = null;
        float highestMean = 0f;
        for (String oem : bwMap.keySet()) {
            float totalWeight = bwMap.get(oem);
            int count = companyCounter.get(oem);
            float meanWeight = totalWeight / count;

            if (meanWeight > highestMean) {
                highestMean = meanWeight;
                highestMeanOEM = oem;
            }
        }

        System.out.println("\n\nThe company: " + highestMeanOEM + " has the highest average body weight of: " + highestMean);
    }

    /**
    * Takes in a user input of a float, handles exceptions if user input cannot be parsed as float.
    * @param scanner Used to parse user input
    * @return Returns user input in form of float.
    */
    private static Float getFloatInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextFloat();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); 
            }
        }
    }

    /**
    * Takes in the input of a new user Cell object, and adds it to the cellMap. It prompts the user to enter
    * all user data one input at a time, maps it to a cell object, than adds the object to the end of the cell map
    * with an index 1 greater than the current size of the cellMap.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @param scanner Parses user input
    * @return Returns string indicating success or failure
    */
    public static String addObject(HashMap<Integer, Cell> cellMap, Scanner scanner){
        System.out.print("Enter the OEM: ");
        String oem = scanner.nextLine();

        System.out.print("Enter the model: ");
        String model = scanner.nextLine();
        
        System.out.print("Enter the year launched: ");
        int launch_announced = getIntInput(scanner);

        System.out.print("Enter the launch status: ");
        String launch_status = scanner.nextLine();

        System.out.print("Enter the body dimensions: ");
        String body_dimensions = scanner.nextLine();

        System.out.print("Enter the body weight: ");
        Float body_weight = getFloatInput(scanner);

        System.out.print("Enter the body sim: ");
        String body_sim = scanner.nextLine();

        System.out.print("Enter the display type: ");
        String display_type = scanner.nextLine();

        System.out.print("Enter the display size: ");
        Float display_size = getFloatInput(scanner);

        System.out.print("Enter the display resolution: ");
        String display_resolution = scanner.nextLine();

        System.out.print("Enter the feature sensors: ");
        String feature_sensors = scanner.nextLine();

        System.out.print("Enter the platform os: ");
        String platform_os = scanner.nextLine();

        Cell cell = new Cell();
        cell.setOEM(oem);                
        cell.setModel(model);
        cell.setLaunchAnnounced(launch_announced);
        cell.setLaunchStatus(launch_status);    
        cell.setBodyDimensions(body_dimensions);
        cell.setBodyWeight(body_weight);
        cell.setBodySim(body_sim);
        cell.setDisplayType(display_type);
        cell.setDisplaySize(display_size);
        cell.setDisplayResolution(display_resolution);
        cell.setFeaturesSensors(feature_sensors);
        cell.setPlatformOS(platform_os);

        int newIndex = cellMap.size() + 1;

        cellMap.put(newIndex, cell);

        String s = "Successfully added new cell with index of " + Integer.toString(newIndex);

       return s;
    }

    /**
    * Deletes the user input by given index.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @param scanner Parses user input
    * @return Returns string indicating if the index was not found, or the given object was deleted.
    */
    public static String deleteObject(HashMap<Integer, Cell> cellMap, Scanner scanner){
        System.out.print("\nEnter index of cell to delete: ");
        int indexToDelete = getIntInput(scanner);
        if (cellMap.containsKey(indexToDelete)) {
            cellMap.remove(indexToDelete);
            return "Object deleted from the cell.";
        } else {
            return "Cell Map does not contain that index.";
        }
    }

    /**
    * Finds the unique values of each column by adding each columns data to its own unique arrayList. It then 
    * takes that arrayList, and loops through each index adding the values to a set. Since sets can only have one
    * unique values, we print out the set and that will contain all unique values.
    * @param cellMap A HashMap containing Cell objects where the keys are integers.
    * @return Returns nothing, prints out the unique values of each column.
    */
    public static void displayUniqueData(HashMap<Integer, Cell> cellMap){
        //Initialize unique data function input arrays
        ArrayList<String> oemList = new ArrayList<>();
        ArrayList<String> modelList = new ArrayList<>();
        ArrayList<Integer> launchAnnouncedList = new ArrayList<>();
        ArrayList<String> launchStatusList = new ArrayList<>();
        ArrayList<String> bodyDimensionsList = new ArrayList<>();
        ArrayList<Float> bodyWeightList = new ArrayList<>();
        ArrayList<String> bodySimList = new ArrayList<>();
        ArrayList<String> displayTypeList = new ArrayList<>();
        ArrayList<Float> displaySizeList = new ArrayList<>();
        ArrayList<String> displayResolutionList = new ArrayList<>();
        ArrayList<String> featureSensorsList = new ArrayList<>();
        ArrayList<String> platformOSsList = new ArrayList<>();

        for(Cell cell : cellMap.values()){
            oemList.add(cell.getOEM());
            modelList.add(cell.getModel());
            launchAnnouncedList.add(cell.getlaunchAnnounced());
            launchStatusList.add(cell.getLaunchStatus());
            bodyDimensionsList.add(cell.getBodyDimensions());
            bodyWeightList.add(cell.getBodyWeight());
            bodySimList.add(cell.getBodySim());
            displayTypeList.add(cell.getDisplayType());
            displaySizeList.add(cell.getDisplaySize());
            displayResolutionList.add(cell.getDisplayResolution());
            featureSensorsList.add(cell.getFeaturesSensors());
            platformOSsList.add(cell.getPlatformOS());
        }

        Set<String> uniqueOEM = uniqueDataStrings(oemList);
        Set<String> uniqueModel = uniqueDataStrings(modelList);
        Set<Integer> uniqueLaunchAnnounced = uniqueDataInteger(launchAnnouncedList);
        Set<String> uniqueLaunchStatus = uniqueDataStrings(launchStatusList);
        Set<String> uniqueBodyDimension = uniqueDataStrings(bodyDimensionsList);
        Set<Float> uniqueBodyWeightList = uniqueDataFloats(bodyWeightList);
        Set<String> uniqueBodySim = uniqueDataStrings(bodySimList);
        Set<String> uniqueDisplayType = uniqueDataStrings(displayTypeList);
        Set<Float> uniqueDisplaySize = uniqueDataFloats(displaySizeList);
        Set<String> uniqueDisplayResolution = uniqueDataStrings(displayResolutionList);
        Set<String> uniqueFeatureSensors = uniqueDataStrings(featureSensorsList);
        Set<String> uniquePlatformOS = uniqueDataStrings(platformOSsList);

        System.out.println("Unique items for OEM: " + uniqueOEM);
        System.out.println("\n\nUnique items for Model: " + uniqueModel);
        System.out.println("\n\nUnique items for Launch Announced: " + uniqueLaunchAnnounced);
        System.out.println("\n\nUnique items for Launch Status: " + uniqueLaunchStatus);
        System.out.println("\n\nUnique items for Body Dimension: " + uniqueBodyDimension);
        System.out.println("\n\nUnique items for Body Weight: " + uniqueBodyWeightList);
        System.out.println("\n\nUnique items for Body Sim: " + uniqueBodySim);
        System.out.println("\n\nUnique items for Display Type: " + uniqueDisplayType);
        System.out.println("\n\nUnique items for Display Size: " + uniqueDisplaySize);
        System.out.println("\n\nUnique items for Display Resolution: " + uniqueDisplayResolution);
        System.out.println("\n\nUnique items for Feature Sensors: " + uniqueFeatureSensors);
        System.out.println("\n\nUnique items for Platform OS: " + uniquePlatformOS);
    }

    /**
    * Returns a set of unique strings from an input array list of strings
    * @param input Array list of strings
    * @return Returns set of unique data from the input string array list
    */
    public static Set<String> uniqueDataStrings(ArrayList<String> input){
        Set<String> uniqueValues = new HashSet<>();
        for(String item : input){
            String s = clean(item);
            uniqueValues.add(s);
        }

        return uniqueValues;
    }

    /**
    * Returns a set of unique floats from an input array list of floats
    * @param input Array list of floats
    * @return Returns set of unique data from the input float array list
    */
    public static Set<Float> uniqueDataFloats(ArrayList<Float> input){
        Set<Float> uniqueValues = new HashSet<>();
        for(Float item : input){
            uniqueValues.add(item);
        }
        return uniqueValues;
    }

    /**
    * Returns a set of unique integers from an input array list of integers
    * @param input Array list of integers
    * @return Returns set of unique data from the input integer array list
    */
    public static Set<Integer> uniqueDataInteger(ArrayList<Integer> input){
        Set<Integer> uniqueValues = new HashSet<>();
        for(Integer item : input){
            uniqueValues.add(item);
        }
        return uniqueValues;
    }
    
    /**
    * Trims input string, and also checks if it is null or empty. If it is null or empty, set it to null.
    * in the given HashMap of Cell objects.
    * @param input String that is going to be cleaned
    * @return Returns the input string cleaned and trimmed.
    */
    public static String clean(String input) {
        if (input == null || input.trim().isEmpty() || input.equals("-")) {
            return null;
        }
        input = input.trim();
        return input;
    }

    /**
    * Cleans the PlatformOS input from the csv file, retrieves data up to first comment as per assignment requirements.
    * If the string is empty or contains "-", return null.
    * @param input String that is passed for data cleaning
    * @return Returns cleaned data string.
    */
    public static String cleanPlatformOS(String input) {
        if (input.equals("-") || input.isEmpty()) {
            return null;
        }
        
        input = removeQuotes(input);

        // Regular expression pattern to match everything up to the first comma or end of string
        Pattern pattern = Pattern.compile("^(.*?)(?:,|$)");
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return input.trim();
        }
    }

    /**
    * Used to remove quoatations from an input string, and make sure the given string is not empty. If empty, return null.
    * @param input String that is passed for data cleaning
    * @return Returns cleaned string of data
    */
    public static String cleanStrings(String input){
        input = removeQuotes(input);
        if( input.equals("-") ||  input.isEmpty()){
            return null;
        }
        return input;
    }

    /**
    * Checks featureSensors string to make sure it is not empty, or composed of only numbers.
    * @param input String that is passed for data cleaning
    * @return Returns cleaned string of featureSensros
    */
    public static String cleanFeatureSensors(String input){
        input = removeQuotes(input);
        if(input.equals("-") || input.isEmpty()){
            return null;
        }
        if(onlyNumbers(input)){
            return null;
        }
        return input;
    }

    /**
    * Cleans passed launchAnnounced data string, parses the first year, and makes sure that the given string is not empty.
    * Also removes quotes from givens string.
    * @param input String that is passed for data cleaning
    * @return Returns cleaned launchAnnounced string.
    */
    public static Integer cleanLaunchAnnounced(String input){
        input = removeQuotes(input);

        Integer returnValue;
        if(input.length() < 4){
            return null;
        }
        else if(!isNumeric(input.substring(0,4))){
            return null;
        }
        else{
            String temp = input.substring(0,4);
            returnValue = Integer.parseInt(temp);
        }

        return returnValue;
    }

    /**
    * Removes quotes, and replaces them with an empty space from an input string.
    * @param str Input string to be cleaned of quotations
    * @return Returns input string with quotations removed
    */
    public static String removeQuotes(String str) {
        return str.replace("\"", "");
    }

    /**
    * Parses float of body weight up to the point where a g appears, meaning the body weight is represented in grams
    * @param input Input string to be cleaned and parsed into float
    * @return Returns parsed float from given string
    */
    public static Float cleanBodyWeight(String input) {
        //Regular expression to match the weight in grams
        Pattern pattern = Pattern.compile("^([\\d.]+)\\s*g");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String temp = matcher.group(1);
            return Float.parseFloat(temp);
        } else {
            return null; 
        }
    }

    /**
    * Parses the display size in the form of a float, uses regex to grab all numbers appearing before the word "inches".
    * @param input Input string to be cleaned and parsed into float
    * @return Returns Float of display size parsed from input string
    */
    public static Float cleanDisplaySize(String input) {
        //Regular expression to match the weight in grams
        Pattern pattern = Pattern.compile("(\\d+) inches");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String temp = matcher.group(1);
            return Float.parseFloat(temp);
        } else {
            return null; 
        }
    }

    /**
    * Takes in an input of a launch status string, removes quotes from the string, checks if the string equals "Discontinued"
    * or "Cancelled", and if not it parses the year from the string.
    * @param str Input string to be cleaned
    * @return Returns cleaned data string
    */
    public static String cleanLaunchStatus(String str) {
        //Regular expression to match a 4-digit year
        str = removeQuotes(str);
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(str);
        
        if("Discontinued".equals(str) || "Cancelled".equals(str )){
            return str;
        }
        else if (matcher.find()) {
            return matcher.group();
        } else {
            return null; 
        }
    }

    /**
    * Checks if a given string can be parsed as a integer.
    * @param input Input string used to check if an input string can be parsed as an integer.
    * @return Returns a boolean value based on if a string can be parsed as an integer.
    */
    public static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
    * Checks if a string is composed of only numbers by attempting to parse string into a double.
    * @param input Input string to be checked if it is composed of only numbers.
    * @return Returns a boolean value based on if a string is composed of only numbers.
    */
    public static boolean onlyNumbers(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
    * Takes in the input of a bodySim string, removes quotes, and replaces "yes" and "no" values with null.
    * @param input Input data string of body Sim from .csv file.
    * @return Returns cleaned data string
    */
    public static String cleanBodySim(String input) {
        input = removeQuotes(input);
        if(input.equals("Yes" )|| input.equals("No")){
            return null;
        }
        return input;

    }
}

class Cell {
    private String oem;
    private String model;
    private Integer launch_announced;
    private String launch_status;
    private String body_dimensions;
    private Float body_weight;
    private String body_sim;
    private String display_type;
    private Float display_size;
    private String display_resolution;
    private String features_sensors;
    private String platform_os;
    
    // Getters and setters 
    public String getOEM(){
        return oem;
    }

    public void setOEM(String OEM){
        this.oem = OEM;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }  

    public Integer getlaunchAnnounced(){
        return launch_announced;
    }

    public void setLaunchAnnounced(Integer launchedAnnounced){
        this.launch_announced = launchedAnnounced;
    }

    public String getLaunchStatus(){
        return launch_status;
    }

    public void setLaunchStatus(String launchStatus){
        this.launch_status = launchStatus;
    }

    public String getBodyDimensions(){
        return body_dimensions;
    }

    public void setBodyDimensions(String bodyDimensions){
        this.body_dimensions = bodyDimensions;
    }

    public Float getBodyWeight(){
        return body_weight;
    }

    public void setBodyWeight(Float bodyWeight){
        this.body_weight = bodyWeight;
    }

    public String getBodySim(){
        return body_sim;
    }

    public void setBodySim(String bodySim){
        this.body_sim = bodySim;
    }

    public String getDisplayType(){
        return display_type;
    }

    public void setDisplayType(String displayType){
        this.display_type = displayType;
    }

    public Float getDisplaySize(){
        return display_size;
    }

    public void setDisplaySize(Float displaySize){
        this.display_size = displaySize;
    }

    public String getDisplayResolution(){
        return display_resolution;
    }

    public void setDisplayResolution(String displayResolution){
        this.display_resolution = displayResolution;
    }

    public String getFeaturesSensors(){
        return features_sensors;
    }

    public void setFeaturesSensors(String featureSensor){
        this.features_sensors = featureSensor;
    }

    public String getPlatformOS(){
        return platform_os;
    }

    public void setPlatformOS(String platformOS){
        this.platform_os = platformOS;
    }
}
