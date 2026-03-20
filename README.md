# Age Calculator — Java Console Application

A Java console application that calculates a user's exact age from their Date of Birth (DOB). It accepts DOB in **DD/MM/YYYY** format, computes the precise age in years, months, and days using Java's `java.time` API, and displays the result in a formatted output.

The application also provides optional enhancements including total age in months and days, and a countdown to the user's next birthday.

## Features

- **Exact age calculation** in years, months, and days using `java.time.Period`
- **Total age display** in months and days using `java.time.temporal.ChronoUnit`
- **Countdown to next birthday** — shows the number of days remaining until your next birthday
- **Strict input validation** — rejects future dates, invalid calendar dates (e.g., `31/02/2020`), and wrong format input with meaningful error messages
- **Leap year handling** for all valid years (e.g., `29/02/2000` is correctly accepted and calculated)
- **Clean OOP design** with separated concerns — input handling, validation, calculation, and data model in dedicated classes
- **Comprehensive test suite** with JUnit 6 (Jupiter) covering normal dates, leap years, invalid dates, future dates, and wrong formats

## Prerequisites

- **Java Development Kit (JDK) 21** or later
- **Apache Maven 3.8** or later

Verify your installations:

```bash
java -version
mvn -version
```

## Project Structure

```
.
├── pom.xml
├── .gitignore
├── README.md
└── src
    ├── main/java/com/agecalculator
    │   ├── Main.java
    │   ├── AgeCalculator.java
    │   ├── AgeResult.java
    │   ├── DateInputHandler.java
    │   └── DateValidator.java
    └── test/java/com/agecalculator
        ├── AgeCalculatorTest.java
        ├── AgeResultTest.java
        ├── DateInputHandlerTest.java
        └── DateValidatorTest.java
```

| Class | Purpose |
|---|---|
| `Main` | Application entry point — orchestrates user interaction via the console |
| `AgeCalculator` | Core business logic — computes age breakdown, total months/days, and next birthday countdown |
| `AgeResult` | Immutable data model — holds the computed age result (years, months, days, total months, total days) |
| `DateInputHandler` | Input handling — reads DOB from the console, parses it with strict `DateTimeFormatter` |
| `DateValidator` | Validation logic — ensures the parsed date is not in the future |

## Build Instructions

**Full build (compile, test, and package):**

```bash
mvn clean package
```

**Compile only (no tests):**

```bash
mvn compile
```

## Run Instructions

After building the project, run the application using:

```bash
java -cp target/classes com.agecalculator.Main
```

Alternatively, you can compile and run in a single step using Maven:

```bash
mvn -q compile exec:java -Dexec.mainClass="com.agecalculator.Main"
```

> **Note:** The `exec:java` command requires the `exec-maven-plugin`. If not configured, use the `java -cp` approach above.

## Test Execution

Run the full test suite:

```bash
mvn test
```

The test suite covers the following scenarios:

| Test Category | Examples |
|---|---|
| ✅ Normal DOB | `15/08/1998` — standard age calculation |
| ✅ Leap year DOB | `29/02/2000` — leap year birthday handling |
| ✅ Boundary cases | DOB today (age = 0), DOB exactly one year ago, very old DOB (`01/01/1900`) |
| ✅ Total calculations | Total months and total days verification |
| ✅ Next birthday countdown | Days remaining until the next birthday |
| ❌ Invalid date | `31/02/2020` — rejected with error message |
| ❌ Future date | Any date after today — rejected with error message |
| ❌ Wrong format | `1998-08-15`, `15-08-1998`, empty string — rejected with error message |

## Usage Examples

### Basic Age Calculation

```
Enter your Date of Birth (DD/MM/YYYY): 15/08/1998

Your age is 27 years, 6 months, and 15 days.
Total age: 330 months and 10088 days.
Days until next birthday: 148 days.
```

### Leap Year Birthday

```
Enter your Date of Birth (DD/MM/YYYY): 29/02/2000

Your age is 26 years, 0 months, and 19 days.
Total age: 312 months and 9517 days.
Days until next birthday: 346 days.
```

### Invalid Date Error

```
Enter your Date of Birth (DD/MM/YYYY): 31/02/2020

Invalid date. Please enter a valid date in DD/MM/YYYY format.
```

### Future Date Error

```
Enter your Date of Birth (DD/MM/YYYY): 25/12/2030

Date of Birth cannot be a future date.
```

### Wrong Format Error

```
Enter your Date of Birth (DD/MM/YYYY): 1998-08-15

Invalid format. Please enter the date in DD/MM/YYYY format (e.g., 15/08/1998).
```

## Technologies Used

- **Java 21** — `java.time` API (`LocalDate`, `Period`, `DateTimeFormatter`, `ChronoUnit`, `ResolverStyle`)
- **Maven** — build tool and dependency management (`pom.xml` with compiler and surefire plugins)
- **JUnit 6.0.3** (Jupiter) — testing framework for comprehensive unit tests

## License

This project is provided as-is for educational and demonstration purposes.
