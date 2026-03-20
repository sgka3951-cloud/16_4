package com.agecalculator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Application entry point for the Age Calculator console application.
 *
 * <p>This class serves as the sole orchestrator that ties together the input
 * handling, validation, age calculation, and output display components. It
 * follows the Object-Oriented Programming principle of <em>separation of
 * concerns</em> — {@code Main} contains <strong>no business logic</strong> and
 * delegates all processing to specialized classes:</p>
 *
 * <ul>
 *   <li>{@link DateInputHandler} — reads and parses the Date of Birth from
 *       the console, enforcing strict {@code DD/MM/YYYY} format validation</li>
 *   <li>{@link AgeCalculator} — computes the exact age decomposed into years,
 *       months, and days, as well as total months, total days, and next
 *       birthday countdown</li>
 *   <li>{@link AgeResult} — immutable data model holding the computed age
 *       result with a formatted {@link AgeResult#toString()} output</li>
 * </ul>
 *
 * <p><strong>Execution Flow:</strong></p>
 * <ol>
 *   <li>Prompts the user for their Date of Birth via {@link DateInputHandler}</li>
 *   <li>Computes the exact age via {@link AgeCalculator#calculateAge(LocalDate)}</li>
 *   <li>Displays the primary formatted result via {@link AgeResult#toString()}</li>
 *   <li>Displays optional enhancement outputs: total age in months, total age
 *       in days, and countdown to the next birthday</li>
 * </ol>
 *
 * <p><strong>Error Handling:</strong> The entire execution flow is wrapped in
 * structured {@code try-catch} blocks to ensure graceful error handling. All
 * exceptions are caught and translated to user-friendly error messages — the
 * application never exposes raw stack traces to the end user.</p>
 *
 * <p><strong>Sample Interaction:</strong></p>
 * <pre>{@code
 * Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
 * Your age is 27 years, 6 months, and 15 days.
 * Total age in months: 330 months
 * Total age in days: 10046 days
 * Days until next birthday: 148 days
 * }</pre>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see DateInputHandler
 * @see AgeCalculator
 * @see AgeResult
 */
public class Main {

    /**
     * Application entry point. Orchestrates the complete age calculation
     * workflow: input acquisition, age computation, and result display.
     *
     * <p>The method performs the following steps in order:</p>
     * <ol>
     *   <li><strong>Input Acquisition:</strong> Instantiates a
     *       {@link DateInputHandler} to prompt the user for their Date of
     *       Birth in {@code DD/MM/YYYY} format and returns a validated
     *       {@link LocalDate}.</li>
     *   <li><strong>Age Calculation:</strong> Instantiates an
     *       {@link AgeCalculator} and invokes
     *       {@link AgeCalculator#calculateAge(LocalDate)} to compute the exact
     *       age, receiving an {@link AgeResult} containing years, months, days,
     *       total months, and total days.</li>
     *   <li><strong>Primary Output:</strong> Prints the formatted age string
     *       via {@link AgeResult#toString()} in the exact format:
     *       {@code Your age is X years, Y months, and Z days.}</li>
     *   <li><strong>Enhancement Outputs:</strong> Displays total age in months,
     *       total age in days, and days until the next birthday.</li>
     * </ol>
     *
     * <p><strong>Error Handling:</strong> The entire flow is wrapped in
     * structured {@code try-catch} blocks catching three exception types in
     * order of specificity:</p>
     * <ul>
     *   <li>{@link DateTimeParseException} — malformed input string, invalid
     *       calendar date (e.g., {@code 31/02/2020}), or wrong format</li>
     *   <li>{@link IllegalArgumentException} — Date of Birth is a future date
     *       (thrown by {@code DateValidator} via {@code DateInputHandler})</li>
     *   <li>{@link Exception} — any unexpected runtime error as a final
     *       fallback safety net</li>
     * </ul>
     *
     * @param args command-line arguments (not used by this application)
     */
    public static void main(String[] args) {
        try {
            // Step 1: Input Acquisition — delegate DOB reading and parsing
            // to DateInputHandler which prompts: "Enter your Date of Birth (DD/MM/YYYY): "
            DateInputHandler inputHandler = new DateInputHandler();
            LocalDate dob = inputHandler.readDateOfBirth();

            // Step 2: Age Calculation — delegate computation to AgeCalculator
            AgeCalculator calculator = new AgeCalculator();
            AgeResult result = calculator.calculateAge(dob);

            // Step 3: Primary Output — display formatted age breakdown
            // AgeResult.toString() returns: "Your age is X years, Y months, and Z days."
            System.out.println(result.toString());

            // Step 4: Optional Enhancement — total age in months and days
            System.out.println("Total age in months: " + result.getTotalMonths() + " months");
            System.out.println("Total age in days: " + result.getTotalDays() + " days");

            // Step 5: Optional Enhancement — countdown to next birthday
            long daysUntilBirthday = calculator.getNextBirthdayCountdown(dob, LocalDate.now());
            System.out.println("Days until next birthday: " + daysUntilBirthday + " days");

        } catch (DateTimeParseException e) {
            // Invalid date format or impossible calendar date
            // e.getMessage() already contains the user-friendly message set by DateInputHandler:
            // - "Invalid format. Please enter the date in DD/MM/YYYY format (e.g., 15/08/1998)."
            // - "Invalid date. Please enter a valid date in DD/MM/YYYY format."
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            // Future date of birth — thrown by DateValidator via DateInputHandler
            // e.getMessage() returns: "Date of Birth cannot be a future date."
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {
            // Unexpected runtime error — final fallback safety net
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
