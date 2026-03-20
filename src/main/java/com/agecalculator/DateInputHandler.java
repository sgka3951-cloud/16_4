package com.agecalculator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Handles reading and parsing Date of Birth input from the console.
 *
 * <p>This class is responsible for prompting the user to enter their Date of Birth
 * in {@code DD/MM/YYYY} format, parsing the input string into a {@link LocalDate},
 * and delegating semantic validation (e.g., future date rejection) to
 * {@link DateValidator}.</p>
 *
 * <p>Uses strict date parsing via {@link ResolverStyle#STRICT} to ensure that
 * invalid calendar dates (e.g., {@code 31/02/2020}) are rejected at parse time
 * rather than silently adjusted to a valid date.</p>
 *
 * <p><strong>Design Principles:</strong></p>
 * <ul>
 *   <li>Single Responsibility — handles only input reading and date parsing;
 *       delegates validation to {@link DateValidator} and age calculation to
 *       {@code AgeCalculator}</li>
 *   <li>Dependency Injection — accepts an injected {@link Scanner} for testability</li>
 *   <li>Exception Propagation — parsing errors ({@link DateTimeParseException}) and
 *       validation errors ({@link IllegalArgumentException}) propagate to the caller
 *       for user-facing error display</li>
 * </ul>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * // Console usage (reads from System.in):
 * DateInputHandler handler = new DateInputHandler();
 * LocalDate dob = handler.readDateOfBirth();
 *
 * // Programmatic usage (for testing):
 * LocalDate dob = DateInputHandler.parseDate("15/08/1998");
 * }</pre>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @see DateValidator
 * @see java.time.LocalDate
 * @see java.time.format.DateTimeFormatter
 */
public class DateInputHandler {

    /**
     * Strict date formatter using the pattern {@code dd/MM/uuuu} with
     * {@link ResolverStyle#STRICT} to enforce rigorous calendar date validation.
     *
     * <p><strong>CRITICAL:</strong> The pattern uses {@code uuuu} (proleptic year)
     * instead of {@code yyyy} (year-of-era) because {@code ResolverStyle.STRICT}
     * requires a proleptic year field for correct operation. Using {@code yyyy}
     * with strict mode would cause valid dates to be rejected.</p>
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Regular expression pattern that matches the basic {@code DD/MM/YYYY} structural format.
     *
     * <p>This pattern validates only the structural shape (two digits, slash, two digits,
     * slash, four digits) without checking calendar validity. It is used to distinguish
     * between two distinct error scenarios:</p>
     * <ul>
     *   <li><strong>Format error</strong> — input does not match {@code DD/MM/YYYY} structure
     *       (e.g., {@code "1998-08-15"}, {@code "abc"})</li>
     *   <li><strong>Invalid calendar date</strong> — input matches the format but represents
     *       an impossible date (e.g., {@code "31/02/2020"}, {@code "30/02/2021"})</li>
     * </ul>
     */
    private static final Pattern FORMAT_PATTERN = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

    /** Error message for invalid calendar dates that match the DD/MM/YYYY format structure. */
    private static final String INVALID_DATE_MESSAGE =
            "Invalid date. Please enter a valid date in DD/MM/YYYY format.";

    /** Error message for input strings that do not match the DD/MM/YYYY format structure. */
    private static final String INVALID_FORMAT_MESSAGE =
            "Invalid format. Please enter the date in DD/MM/YYYY format (e.g., 15/08/1998).";

    /**
     * Scanner instance for reading user input from the console.
     * Injected via constructor for testability.
     */
    private final Scanner scanner;

    /**
     * Constructs a {@code DateInputHandler} that reads from standard input
     * ({@code System.in}).
     *
     * <p>This is the default constructor used in production when the application
     * reads directly from the console.</p>
     *
     * <p><strong>Note:</strong> The {@link Scanner} wrapping {@code System.in}
     * is intentionally never closed by this class, as closing it would permanently
     * close {@code System.in} for the entire JVM process.</p>
     */
    public DateInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructs a {@code DateInputHandler} with the specified {@link Scanner}.
     *
     * <p>This overloaded constructor enables dependency injection for unit testing,
     * allowing tests to supply a {@code Scanner} backed by a {@code String} or
     * other input source instead of {@code System.in}.</p>
     *
     * <p><strong>Usage in Tests:</strong></p>
     * <pre>{@code
     * Scanner testScanner = new Scanner("15/08/1998\n");
     * DateInputHandler handler = new DateInputHandler(testScanner);
     * LocalDate dob = handler.readDateOfBirth();
     * }</pre>
     *
     * @param scanner the {@link Scanner} to use for reading input; must not be {@code null}
     * @throws NullPointerException if {@code scanner} is {@code null}
     */
    public DateInputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new NullPointerException("Scanner must not be null.");
        }
        this.scanner = scanner;
    }

    /**
     * Prompts the user for their Date of Birth and returns a parsed, validated
     * {@link LocalDate}.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Prints the prompt {@code "Enter your Date of Birth (DD/MM/YYYY): "}
     *       to the console (without a trailing newline)</li>
     *   <li>Reads a single line of input from the scanner and trims whitespace</li>
     *   <li>Parses the input string using the strict {@code dd/MM/uuuu} formatter</li>
     *   <li>Delegates semantic validation to {@link DateValidator#validate(LocalDate)}
     *       to ensure the date is not in the future</li>
     *   <li>Returns the validated {@code LocalDate}</li>
     * </ol>
     *
     * <p><strong>Error Handling:</strong></p>
     * <ul>
     *   <li>{@link DateTimeParseException} is thrown (with a user-friendly message)
     *       if the input string is malformed, contains an invalid calendar date
     *       (e.g., {@code 31/02/2020}), or uses the wrong format</li>
     *   <li>{@link IllegalArgumentException} is thrown by {@link DateValidator} if
     *       the parsed date is in the future</li>
     * </ul>
     *
     * @return a validated {@link LocalDate} representing the user's date of birth
     * @throws DateTimeParseException if the input cannot be parsed as a valid date
     *         in DD/MM/YYYY format
     * @throws IllegalArgumentException if the parsed date of birth is in the future
     */
    public LocalDate readDateOfBirth() {
        System.out.print("Enter your Date of Birth (DD/MM/YYYY): ");
        String input = scanner.nextLine().trim();

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(input, FORMATTER);
        } catch (DateTimeParseException e) {
            // Differentiate between format errors and invalid calendar dates:
            // If the input matches the DD/MM/YYYY structural pattern but still fails to parse,
            // it is an invalid calendar date (e.g., 31/02/2020). Otherwise, it is a format error.
            String errorMessage = FORMAT_PATTERN.matcher(input).matches()
                    ? INVALID_DATE_MESSAGE
                    : INVALID_FORMAT_MESSAGE;
            throw new DateTimeParseException(
                    errorMessage,
                    e.getParsedString(),
                    e.getErrorIndex(),
                    e
            );
        }

        // Delegate semantic validation (future date check) to DateValidator
        DateValidator.validate(parsedDate);

        return parsedDate;
    }

    /**
     * Parses a date string in {@code DD/MM/YYYY} format and returns a validated
     * {@link LocalDate}.
     *
     * <p>This static method provides programmatic date parsing without requiring
     * a {@link Scanner} or console interaction. It is particularly useful for
     * unit testing the parsing and validation logic independently of I/O.</p>
     *
     * <p>The method uses the same strict {@link DateTimeFormatter} ({@code dd/MM/uuuu}
     * with {@link ResolverStyle#STRICT}) as {@link #readDateOfBirth()}, ensuring
     * consistent parsing behavior across both methods.</p>
     *
     * <p><strong>Error Handling:</strong></p>
     * <ul>
     *   <li>{@link DateTimeParseException} is thrown (with a user-friendly message)
     *       for malformed input strings, invalid calendar dates (e.g., {@code 31/02/2020}),
     *       wrong format strings (e.g., {@code 1998-08-15}), or empty input</li>
     *   <li>{@link IllegalArgumentException} is thrown by {@link DateValidator} if
     *       the parsed date is in the future</li>
     * </ul>
     *
     * <p><strong>Usage Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = DateInputHandler.parseDate("15/08/1998");
     * // dob == 1998-08-15
     *
     * DateInputHandler.parseDate("31/02/2020"); // throws DateTimeParseException
     * DateInputHandler.parseDate("abc");         // throws DateTimeParseException
     * }</pre>
     *
     * @param dateString the date string to parse in DD/MM/YYYY format; must not be
     *                   {@code null}
     * @return a validated {@link LocalDate} parsed from the input string
     * @throws DateTimeParseException if the input string cannot be parsed as a valid
     *         date in DD/MM/YYYY format
     * @throws IllegalArgumentException if the parsed date is in the future
     * @throws NullPointerException if {@code dateString} is {@code null}
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null) {
            throw new DateTimeParseException(
                    INVALID_FORMAT_MESSAGE,
                    "",
                    0
            );
        }

        String trimmed = dateString.trim();

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(trimmed, FORMATTER);
        } catch (DateTimeParseException e) {
            // Differentiate between format errors and invalid calendar dates:
            // If the trimmed input matches the DD/MM/YYYY structural pattern but still fails
            // to parse, it is an invalid calendar date (e.g., 31/02/2020). Otherwise, it is
            // a format error (e.g., "1998-08-15", "abc", empty string).
            String errorMessage = FORMAT_PATTERN.matcher(trimmed).matches()
                    ? INVALID_DATE_MESSAGE
                    : INVALID_FORMAT_MESSAGE;
            throw new DateTimeParseException(
                    errorMessage,
                    e.getParsedString(),
                    e.getErrorIndex(),
                    e
            );
        }

        // Delegate semantic validation (future date check) to DateValidator
        DateValidator.validate(parsedDate);

        return parsedDate;
    }
}
