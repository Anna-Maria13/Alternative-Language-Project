import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class DataProcessorTest {

    private List<DataProcessor.Cell> testCellList;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test
        testCellList = TestData.generateTestData(); // Generate test data or initialize it manually
    }

    @Test
    public void testReadCSV() throws IOException {
        List<DataProcessor.Cell> cellList = DataProcessor.readCSV("test_data.csv");
        assertNotNull(cellList);
        assertFalse(cellList.isEmpty());
        assertEquals(testCellList.size(), cellList.size());
        // Add more assertions if needed
    }

    @Test
    public void testCleanAndTransformData() {
        DataProcessor.cleanAndTransformData(testCellList);
        // Add assertions to verify data cleaning and transformation
    }

    @Test
    public void testTransformToYear() {
        String year = DataProcessor.transformToYear("2023-05-15");
        assertEquals("2023", year);
        // Add more assertions if needed
    }

    @Test
    public void testTransformToYearOrStatus() {
        String statusYear = DataProcessor.transformToYearOrStatus("2023");
        assertEquals("2023", statusYear);

        String status = DataProcessor.transformToYearOrStatus("Discontinued");
        assertEquals("Discontinued", status);
        // Add more assertions if needed
    }

    @Test
    public void testTransformToFloat() {
        Float floatValue = DataProcessor.transformToFloat("5.2");
        assertEquals(5.2f, floatValue);

        floatValue = DataProcessor.transformToFloat("10");
        assertEquals(10f, floatValue);
        // Add more assertions if needed
    }

    @Test
    public void testCalculateStatistics() {
        DataProcessor.CellStatistics statistics = DataProcessor.calculateStatistics(testCellList);
        // Add assertions to verify calculated statistics
    }

    @Test
    public void testListUniqueValues() {
        List<String> uniqueValues = DataProcessor.listUniqueValues(testCellList, "oem");
        // Add assertions to verify unique values
    }

    @Test
    public void testGetValueForColumn() {
        DataProcessor.Cell cell = testCellList.get(0);
        String oem = DataProcessor.getValueForColumn(cell, "oem");
        assertEquals("Apple", oem);
        // Add more assertions if needed
    }

    @Test
    public void testEmptyFileReadCSV() {
        assertThrows(IOException.class, () -> DataProcessor.readCSV("empty_file.csv"));
    }

    @Test
    public void testEmptyFileReadCSVEmptyList() throws IOException {
        List<DataProcessor.Cell> cellList = DataProcessor.readCSV("empty_file.csv");
        assertNotNull(cellList);
        assertTrue(cellList.isEmpty());
    }

    // Add more test cases as needed for edge cases, etc.
}