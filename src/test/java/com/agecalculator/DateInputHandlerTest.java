package com.agecalculator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DateInputHandler} class, focusing on date parsing
 * and format validation via the static {@link DateInputHandler#parseDate(String)} method.
 *
 * <p>This test class covers the following scenarios as specified in the AAP:</p>
 * <ul>
 *   <li><strong>Valid DD/MM/YYYY parsing</strong> — confirms correct {@link LocalDate}
 *       values are returned for well-formed, valid calendar dates including the user's
 *       example input ({@code 15/08/1998}) and leap year dates ({@code 29/02/2000})</li>
 *   <li><strong>Invalid calendar date rejection</strong> — verifies that impossible dates
 *       such as {@code 31/02/2020}, {@code 30/02/2020}, and {@code 32/01/2020} are
 *       rejected with a {@link DateTimeParseException} thanks to
 *       {@link java.time.format.ResolverStyle#STRICT}</li>
 *   <li><strong>Wrong format rejection</strong> — ensures ISO format ({@code YYYY-MM-DD}),
 *       hyphen-separated ({@code DD-MM-YYYY}), American month-first format, and random
 *       text strings are all rejected</li>
 *   <li><strong>Empty and whitespace input</strong> — verifies that empty strings and
 *       whitespace-only strings produce {@link DateTimeParseException}</li>
 *   <li><strong>Null safety</strong> — verifies that {@code null} input is handled
 *       gracefully without a raw {@link NullPointerException} propagating</li>
 *   <li><strong>Future date rejection</strong> — integration test verifying that
 *       {@link DateInputHandler#parseDate(String)} internally delegates to
 *       {@link DateValidator#validate(LocalDate)} and throws
 *       {@link IllegalArgumentException} for future dates</li>
 * </ul>
 *
 * <p><strong>Design Rules:</strong></p>
 * <ul>
 *   <li>All tests call the static {@code parseDate(String)} method — no Scanner or
 *       console interaction is required</li>
 *   <li>All test dates are hardcoded string literals — no {@code LocalDate.now()} usage</li>
 *   <li>Deterministic and reproducible regardless of execution date</li>
 * </ul>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @see DateInputHandler#parseDate(String)
 * @see DateValidator
 */
public class DateInputHandlerTest {

    // =========================================================================
    // Valid DD/MM/YYYY Format Parsing Tests
    // =========================================================================

    /**
     * Tests the happy path with the user's example input: {@code 15/08/1998}.
     * Verifies that the string is parsed into the correct {@link LocalDate} value.
     */
    @Test
    @DisplayName("Valid date 15/08/1998 parses correctly")
    void testValidDate15081998ParsesCorrectly() {
        LocalDate result = assertDoesNotThrow(
                () -> DateInputHandler.parseDate("15/08/1998"),
                "Parsing a valid date '15/08/1998' should not throw any exception"
        );

        assertNotNull(result, "Parsed date should not be null");
        assertEquals(
                LocalDate.of(1998, 8, 15),
                result,
                "15/08/1998 should parse to 1998-08-15"
        );
    }

    /**
     * Tests parsing of the first day of the year 2000: {@code 01/01/2000}.
     * Verifies correct handling of leading zeros in day and month fields.
     */
    @Test
    @DisplayName("Valid date 01/01/2000 parses correctly")
    void testValidDate01012000ParsesCorrectly() {
        LocalDate result = assertDoesNotThrow(
                () -> DateInputHandler.parseDate("01/01/2000"),
                "Parsing a valid date '01/01/2000' should not throw any exception"
        );

        assertNotNull(result, "Parsed date should not be null");
        assertEquals(
                LocalDate.of(2000, 1, 1),
                result,
                "01/01/2000 should parse to 2000-01-01"
        );
    }

    /**
     * Tests that the leap year date {@code 29/02/2000} is correctly parsed.
     * Year 2000 is a leap year (divisible by 400), so February 29 is valid.
     * This is one of the user-specified test cases for leap year handling.
     */
    @Test
    @DisplayName("Valid leap year date 29/02/2000 parses correctly")
    void testValidLeapYearDate29022000ParsesCorrectly() {
        LocalDate result = assertDoesNotThrow(
                () -> DateInputHandler.parseDate("29/02/2000"),
                "Parsing a valid leap year date '29/02/2000' should not throw any exception"
        );

        assertNotNull(result, "Parsed date should not be null");
        assertEquals(
                LocalDate.of(2000, 2, 29),
                result,
                "29/02/2000 should parse to 2000-02-29 (leap year)"
        );
    }

    // =========================================================================
    // Invalid Calendar Date Tests
    // =========================================================================

    /**
     * Tests that February 31 is rejected as an invalid calendar date.
     * This is one of the user-specified test cases: ❌ Invalid date ({@code 31/02/2020}).
     * The {@code ResolverStyle.STRICT} with {@code "dd/MM/uuuu"} pattern ensures
     * this is rejected at parse time rather than silently adjusted.
     */
    @Test
    @DisplayName("Invalid date 31/02/2020 is rejected")
    void testInvalidDate31022020IsRejected() {
        DateTimeParseException exception = assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("31/02/2020"),
                "Parsing '31/02/2020' should throw DateTimeParseException — February never has 31 days"
        );

        assertNotNull(exception.getMessage(),
                "Exception message should not be null");
    }

    /**
     * Tests that February 30 is rejected as an invalid calendar date.
     * February never has 30 days in any year.
     */
    @Test
    @DisplayName("Invalid date 30/02/2020 is rejected")
    void testInvalidDate30022020IsRejected() {
        DateTimeParseException exception = assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("30/02/2020"),
                "Parsing '30/02/2020' should throw DateTimeParseException — February never has 30 days"
        );

        assertNotNull(exception.getMessage(),
                "Exception message should not be null");
    }

    /**
     * Tests that day 32 is rejected as an invalid calendar date.
     * No month has 32 days.
     */
    @Test
    @DisplayName("Invalid date 32/01/2020 is rejected")
    void testInvalidDate32012020IsRejected() {
        DateTimeParseException exception = assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("32/01/2020"),
                "Parsing '32/01/2020' should throw DateTimeParseException — day 32 is always invalid"
        );

        assertNotNull(exception.getMessage(),
                "Exception message should not be null");
    }

    // =========================================================================
    // Wrong Format Input Tests
    // =========================================================================

    /**
     * Tests that ISO format ({@code YYYY-MM-DD}) is rejected.
     * Only {@code DD/MM/YYYY} with forward slashes is accepted.
     */
    @Test
    @DisplayName("Wrong format: ISO format YYYY-MM-DD is rejected")
    void testWrongFormatIsoFormatIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("1998-08-15"),
                "Parsing ISO format '1998-08-15' should throw DateTimeParseException"
        );
    }

    /**
     * Tests that hyphen-separated format ({@code DD-MM-YYYY}) is rejected.
     * The parser requires forward slashes as delimiters, not hyphens.
     */
    @Test
    @DisplayName("Wrong format: DD-MM-YYYY with hyphens is rejected")
    void testWrongFormatHyphensIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("15-08-1998"),
                "Parsing '15-08-1998' (hyphens) should throw DateTimeParseException"
        );
    }

    /**
     * Tests that American month-first format is rejected when interpreted as DD/MM/YYYY.
     * The input {@code "12/25/1998"} would be valid as MM/DD/YYYY (December 25, 1998),
     * but when parsed as DD/MM/YYYY, month=25 is invalid (no 25th month exists).
     * This demonstrates that American-format dates are correctly rejected.
     */
    @Test
    @DisplayName("Wrong format: MM/DD/YYYY American format is rejected or parsed differently")
    void testWrongFormatAmericanFormatIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("12/25/1998"),
                "Parsing '12/25/1998' (American MM/DD/YYYY) should throw DateTimeParseException — "
                        + "month 25 is invalid when interpreted as DD/MM/YYYY"
        );
    }

    /**
     * Tests that completely random, non-date text is rejected.
     * Non-date strings must produce a {@link DateTimeParseException}.
     */
    @Test
    @DisplayName("Wrong format: random text input is rejected")
    void testWrongFormatRandomTextIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("not-a-date"),
                "Parsing random text 'not-a-date' should throw DateTimeParseException"
        );
    }

    // =========================================================================
    // Empty and Whitespace Input Tests
    // =========================================================================

    /**
     * Tests that an empty string is rejected.
     * An empty string cannot be parsed as a valid date in any format.
     */
    @Test
    @DisplayName("Empty string input is rejected")
    void testEmptyStringInputIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate(""),
                "Parsing an empty string should throw DateTimeParseException"
        );
    }

    /**
     * Tests that whitespace-only input is rejected.
     * The {@link DateInputHandler#parseDate(String)} method trims input before parsing,
     * so whitespace-only strings become empty strings after trimming and fail to parse.
     */
    @Test
    @DisplayName("Whitespace-only input is rejected")
    void testWhitespaceOnlyInputIsRejected() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate("   "),
                "Parsing whitespace-only input '   ' should throw DateTimeParseException"
        );
    }

    // =========================================================================
    // Null Safety Test
    // =========================================================================

    /**
     * Tests that {@code null} input is handled gracefully.
     * The {@link DateInputHandler#parseDate(String)} implementation explicitly checks
     * for {@code null} and throws a {@link DateTimeParseException} with a descriptive
     * message rather than allowing a raw {@link NullPointerException} to propagate.
     */
    @Test
    @DisplayName("Null input throws NullPointerException")
    void testNullInputThrowsException() {
        assertThrows(
                DateTimeParseException.class,
                () -> DateInputHandler.parseDate(null),
                "Parsing null input should throw DateTimeParseException — implementation "
                        + "explicitly catches null and wraps it in DateTimeParseException"
        );
    }

    // =========================================================================
    // Future Date Rejection Test (Integration with DateValidator)
    // =========================================================================

    /**
     * Integration test verifying that {@link DateInputHandler#parseDate(String)}
     * internally delegates to {@link DateValidator#validate(LocalDate)} and
     * propagates the {@link IllegalArgumentException} for future dates.
     *
     * <p>Uses the far-future date {@code 01/01/2099} which is a valid calendar
     * date in DD/MM/YYYY format but should be rejected because it is after
     * today's date. The error message must match the DateValidator's exact
     * output: {@code "Date of Birth cannot be a future date."}</p>
     */
    @Test
    @DisplayName("Future date is rejected via DateValidator integration")
    void testFutureDateIsRejectedViaDateValidatorIntegration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateInputHandler.parseDate("01/01/2099"),
                "Parsing a future date '01/01/2099' should throw IllegalArgumentException "
                        + "via DateValidator integration"
        );

        assertEquals(
                "Date of Birth cannot be a future date.",
                exception.getMessage(),
                "The error message should exactly match DateValidator's output"
        );
    }
}
