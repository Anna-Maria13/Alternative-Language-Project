# Alternative-Language-Project
Repository:

Overview:
This project aims to process data from CSV files containing information about mobile phone specifications. The data is cleaned, transformed, and analyzed using Java programming language.

Why Java?
Java was chosen for this project due to its widespread use, strong support for object-oriented programming, and extensive libraries for handling various tasks including file ingestion, data manipulation, and exception handling. Additionally, Java provides robust tools for unit testing, making it suitable for ensuring the correctness and reliability of the code.

How Java Handles Various Programming Concepts:
1. Object-Oriented Programming (OOP):Java is built as an object-oriented programming language, offering features like classes, objects, inheritance, polymorphism, and encapsulation, facilitating the modeling of real-world entities.
2. File Ingestion: For file handling, Java provides robust support through classes like File, BufferedReader, and FileReader, enabling reading, parsing, and processing of file contents.
3. Conditional Statements: Conditional statements in Java, such as if, else if, and switch, allow for executing different code blocks based on specified conditions, facilitating decision-making within programs.
4. Assignment Statements: Assignment statements in Java allow variables to be assigned values, fundamental for storing and manipulating data within programs.
5. Loops: Java supports various loop types like for, while, and do-while, essential for iterating over collections and executing repetitive tasks, controlling program flow based on conditions.
6. Subprograms (Functions/Methods): Methods or functions can be created within classes in Java, promoting code organization, readability, and reusability by encapsulating specific tasks.
7. Unit Testing: Java's unit testing ecosystem is mature, with widely used frameworks like JUnit, enabling the writing and execution of tests to ensure code correctness and expected behavior under various conditions.
8. Exception Handling: Exception handling in Java, using try, catch, and finally blocks, enables the handling of errors and exceptional conditions during program execution, enhancing program reliability and robustness.

Libraries Used:
1. JUnit: JUnit is a widely used unit testing framework for Java. It is used in this project for writing and executing unit tests to validate the functionality of methods in the DataProcessor class(used in DataprocessorTest.java file).
2. Pattern and Matcher (java.util.regex): These classes are used in the DataProcessor class for data transformation tasks, specifically in methods like transformToYear, transformToYearOrStatus, and transformToFloat. They enable pattern matching and text manipulation, which are crucial for extracting specific information from the input data.
3. The BufferedReader class is indeed used in the DataProcessor class to read data from a CSV file (readCSV method). It helps efficiently read text from the input stream, enhancing the performance of file ingestion in the program.

Answer the following questions (and provide a corresponding screen showing output answering them):
1. What company (oem) has the highest average weight of the phone body?
2. Was there any phones that were announced in one year and released in another? What are they? Give me the oem and models.
3. How many phones have only one feature sensor?
4. What year had the most phones launched in any year later than 1999? 
