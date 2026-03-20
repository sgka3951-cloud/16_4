package com.agecalculator;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DateValidator} validation utility class.
 *
 * <p>This test class verifies the semantic validation rules applied to parsed
 * date-of-birth values. Specifically, it validates:</p>
 * <ul>
 *   <li>Future dates are rejected with the correct error message</li>
 *   <li>Valid past dates are accepted without exception</li>
 *   <li>Very old dates (e.g., 01/01/1900) are accepted</li>
 *   <li>Leap year dates (e.g., 29/02/2000) are accepted</li>
 *   <li>Today's date is accepted as a valid DOB (age = 0 boundary case)</li>
 * </ul>
 *
 * <p><strong>Test Design Notes:</strong></p>
 * <ul>
 *   <li>{@code DateValidator.validate()} is a static method — called directly
 *       without class instantiation.</li>
 *   <li>Most tests use hardcoded {@link LocalDate#of(int, int, int)} values for
 *       determinism. The only exception is the today-boundary test which uses
 *       {@link LocalDate#now()} to verify the edge case.</li>
 *   <li>Future date tests use far-future years (2099) to avoid test brittleness.</li>
 * </ul>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @see DateValidator
 */
public class DateValidatorTest {

    /**
     * Verifies that a far-future date (2099-01-01) is rejected by the validator
     * with an {@link IllegalArgumentException} containing the exact error message
     * {@code "Date of Birth cannot be a future date."}.
     */
    @Test
    @DisplayName("Future date is rejected with correct error message")
    void testFutureDateRejectedWithCorrectErrorMessage() {
        LocalDate futureDate = LocalDate.of(2099, 1, 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateValidator.validate(futureDate)
        );

        assertEquals("Date of Birth cannot be a future date.", exception.getMessage());
    }

    /**
     * Verifies that another far-future date (2099-06-15) is also rejected,
     * confirming the validator consistently rejects all dates after today.
     * Uses a hardcoded far-future date rather than {@code LocalDate.now().plusDays(1)}
     * to maintain test determinism.
     */
    @Test
    @DisplayName("Tomorrow's date is rejected as future")
    void testTomorrowRejectedAsFuture() {
        LocalDate tomorrow = LocalDate.of(2099, 6, 15);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateValidator.validate(tomorrow)
        );

        assertEquals("Date of Birth cannot be a future date.", exception.getMessage());
    }

    /**
     * Verifies that a typical valid past date (15/08/1998) is accepted
     * without throwing any exception.
     */
    @Test
    @DisplayName("Valid past date 15/08/1998 is accepted")
    void testValidPastDateAccepted() {
        LocalDate pastDate = LocalDate.of(1998, 8, 15);

        assertDoesNotThrow(() -> DateValidator.validate(pastDate));
    }

    /**
     * Verifies that a very old date (01/01/1900) is accepted without
     * throwing any exception, ensuring no lower-bound date restriction exists.
     */
    @Test
    @DisplayName("Valid old date 01/01/1900 is accepted")
    void testValidOldDateAccepted() {
        LocalDate oldDate = LocalDate.of(1900, 1, 1);

        assertDoesNotThrow(() -> DateValidator.validate(oldDate));
    }

    /**
     * Verifies that a leap year date (29/02/2000) passes semantic validation.
     * This confirms the validator does not introduce any additional calendar
     * restrictions beyond the future-date check.
     */
    @Test
    @DisplayName("Valid leap year date 29/02/2000 is accepted")
    void testValidLeapYearDateAccepted() {
        LocalDate leapDate = LocalDate.of(2000, 2, 29);

        assertDoesNotThrow(() -> DateValidator.validate(leapDate));
    }

    /**
     * Verifies that today's date is accepted as a valid date of birth.
     * This is an important boundary condition: a DOB of today results in
     * an age of 0 years, 0 months, and 0 days, which is semantically valid.
     *
     * <p>The {@link DateValidator#validate(LocalDate)} method uses
     * {@code dob.isAfter(LocalDate.now())} — since today is NOT "after" today,
     * this test must pass without exception.</p>
     *
     * <p><strong>Note:</strong> This is one of the rare cases where
     * {@code LocalDate.now()} is used in a test, because the boundary
     * condition being tested is specifically "today is valid, tomorrow is not."</p>
     */
    @Test
    @DisplayName("Today's date is accepted as valid DOB (age = 0)")
    void testTodayDateAcceptedAsValidDOB() {
        LocalDate today = LocalDate.now();

        assertDoesNotThrow(() -> DateValidator.validate(today));
    }
}
