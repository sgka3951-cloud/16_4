package com.agecalculator;

import java.time.LocalDate;

/**
 * Stateless validation utility class for date-related business rules.
 *
 * <p>Provides static methods to validate parsed dates against semantic constraints
 * such as ensuring the date of birth is not in the future. This class follows the
 * utility class pattern with a private constructor and exclusively static methods.</p>
 *
 * <p><strong>Design Principles:</strong></p>
 * <ul>
 *   <li>Stateless — no instance fields, no instance methods (except private constructor)</li>
 *   <li>Single Responsibility — handles only date validation rules; does not parse dates
 *       or calculate ages</li>
 *   <li>Exception-based validation — uses {@link IllegalArgumentException} to signal
 *       validation failures, following Java conventions for invalid argument detection</li>
 *   <li>No console I/O — no Scanner, no System.out, no System.err</li>
 * </ul>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * LocalDate dob = LocalDate.of(1998, 8, 15);
 * DateValidator.validate(dob); // passes silently — valid past date
 *
 * LocalDate futureDate = LocalDate.now().plusDays(1);
 * DateValidator.validate(futureDate); // throws IllegalArgumentException
 * }</pre>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @see java.time.LocalDate
 */
public class DateValidator {

    // Utility class — prevent instantiation
    private DateValidator() {
        // No instances allowed; all methods are static
    }

    /**
     * Validates that the given date of birth is not in the future.
     *
     * <p>This method checks the provided date against the current system date
     * obtained via {@link LocalDate#now()}. If the date of birth is strictly
     * after today's date, an {@link IllegalArgumentException} is thrown with
     * a descriptive error message.</p>
     *
     * <p><strong>Edge Case — Today's Date:</strong> A date of birth equal to
     * today's date is considered valid (resulting in an age of 0 years, 0 months,
     * and 0 days). The check uses {@code isAfter()} exclusively, so today is
     * always accepted.</p>
     *
     * <p><strong>Note:</strong> {@code LocalDate.now()} is called fresh on each
     * invocation to ensure the comparison always uses the current system date,
     * even if the application runs across a midnight boundary.</p>
     *
     * @param dob the parsed date of birth to validate; must not be {@code null}
     * @throws IllegalArgumentException if the date of birth is in the future
     *         (i.e., after today's date)
     * @throws NullPointerException if {@code dob} is {@code null}
     */
    public static void validate(LocalDate dob) {
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of Birth cannot be a future date.");
        }
    }
}
