package com.agecalculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link AgeResult} immutable data model class.
 *
 * <p>Tests construction via the 5-parameter constructor, verifies that all five
 * getter methods ({@code getYears()}, {@code getMonths()}, {@code getDays()},
 * {@code getTotalMonths()}, {@code getTotalDays()}) return the correct values,
 * and validates that the {@code toString()} method produces the exact formatted
 * output: {@code "Your age is X years, Y months, and Z days."}</p>
 *
 * <p>No java.time imports are required because {@link AgeResult} uses only
 * primitive types ({@code int}, {@code long}) and {@link String}. All test
 * values are hardcoded constants, making every test fully deterministic with
 * no clock dependency.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeResultTest {

    // -----------------------------------------------------------------------
    // Construction Tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that the 5-parameter constructor successfully creates a non-null
     * {@link AgeResult} instance when supplied with typical valid values
     * representing an age of 27 years, 6 months, and 15 days.
     */
    @Test
    @DisplayName("Construction with valid values creates non-null result")
    void testConstructionWithValidValues() {
        // Arrange & Act — construct with representative valid values
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Assert — the instance must be non-null
        assertNotNull(result, "AgeResult constructed with valid values must not be null");
    }

    /**
     * Verifies that the constructor correctly handles the edge case where all
     * components are zero, representing a person whose date of birth is today
     * (age = 0 years, 0 months, 0 days).
     */
    @Test
    @DisplayName("Construction with zero values creates valid result")
    void testConstructionWithZeroValues() {
        // Arrange & Act — construct with all-zero values (DOB = today)
        AgeResult result = new AgeResult(0, 0, 0, 0L, 0L);

        // Assert — the instance must be non-null even with zero age
        assertNotNull(result, "AgeResult constructed with zero values must not be null");
    }

    // -----------------------------------------------------------------------
    // Getter Method Tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that {@link AgeResult#getYears()} returns the exact years value
     * that was supplied to the constructor.
     */
    @Test
    @DisplayName("getYears returns correct years value")
    void testGetYearsReturnsCorrectValue() {
        // Arrange — create an AgeResult with years = 27
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act & Assert — getYears() must return the exact value passed to the constructor
        assertEquals(27, result.getYears(),
                "getYears() must return the years value supplied at construction time");
    }

    /**
     * Verifies that {@link AgeResult#getMonths()} returns the exact months value
     * that was supplied to the constructor.
     */
    @Test
    @DisplayName("getMonths returns correct months value")
    void testGetMonthsReturnsCorrectValue() {
        // Arrange — create an AgeResult with months = 6
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act & Assert — getMonths() must return the exact value passed to the constructor
        assertEquals(6, result.getMonths(),
                "getMonths() must return the months value supplied at construction time");
    }

    /**
     * Verifies that {@link AgeResult#getDays()} returns the exact days value
     * that was supplied to the constructor.
     */
    @Test
    @DisplayName("getDays returns correct days value")
    void testGetDaysReturnsCorrectValue() {
        // Arrange — create an AgeResult with days = 15
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act & Assert — getDays() must return the exact value passed to the constructor
        assertEquals(15, result.getDays(),
                "getDays() must return the days value supplied at construction time");
    }

    /**
     * Verifies that {@link AgeResult#getTotalMonths()} returns the exact total
     * months value (as a {@code long}) that was supplied to the constructor.
     */
    @Test
    @DisplayName("getTotalMonths returns correct total months value")
    void testGetTotalMonthsReturnsCorrectValue() {
        // Arrange — create an AgeResult with totalMonths = 330
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act & Assert — getTotalMonths() must return the exact long value passed to the constructor
        assertEquals(330L, result.getTotalMonths(),
                "getTotalMonths() must return the totalMonths value supplied at construction time");
    }

    /**
     * Verifies that {@link AgeResult#getTotalDays()} returns the exact total
     * days value (as a {@code long}) that was supplied to the constructor.
     */
    @Test
    @DisplayName("getTotalDays returns correct total days value")
    void testGetTotalDaysReturnsCorrectValue() {
        // Arrange — create an AgeResult with totalDays = 10044
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act & Assert — getTotalDays() must return the exact long value passed to the constructor
        assertEquals(10044L, result.getTotalDays(),
                "getTotalDays() must return the totalDays value supplied at construction time");
    }

    // -----------------------------------------------------------------------
    // toString() Format Verification Tests
    // -----------------------------------------------------------------------

    /**
     * Verifies that {@link AgeResult#toString()} returns the EXACT formatted
     * string as specified by the user and the AAP:
     * {@code "Your age is 27 years, 6 months, and 15 days."}
     *
     * <p>The format requirements are:
     * <ul>
     *   <li>Prefix: {@code "Your age is "}</li>
     *   <li>Comma after years: {@code "X years, "}</li>
     *   <li>Comma after months: {@code "Y months, "}</li>
     *   <li>{@code "and"} before days: {@code "and Z days."}</li>
     *   <li>Trailing period: {@code "."}</li>
     * </ul>
     * </p>
     */
    @Test
    @DisplayName("toString returns exact formatted string")
    void testToStringReturnsExactFormattedString() {
        // Arrange — create an AgeResult matching the user's example output
        AgeResult result = new AgeResult(27, 6, 15, 330L, 10044L);

        // Act
        String actual = result.toString();

        // Assert — the output must match the exact format from the AAP specification
        assertEquals("Your age is 27 years, 6 months, and 15 days.", actual,
                "toString() must produce the exact format: 'Your age is X years, Y months, and Z days.'");
    }

    /**
     * Verifies that {@link AgeResult#toString()} correctly formats the output
     * when all age components are zero (edge case: DOB is today).
     * The same format must be produced regardless of the numeric values.
     */
    @Test
    @DisplayName("toString with zero values formats correctly")
    void testToStringWithZeroValues() {
        // Arrange — create an AgeResult with all zeros (DOB = today)
        AgeResult result = new AgeResult(0, 0, 0, 0L, 0L);

        // Act
        String actual = result.toString();

        // Assert — zeros must still follow the exact format
        assertEquals("Your age is 0 years, 0 months, and 0 days.", actual,
                "toString() with all zero values must produce 'Your age is 0 years, 0 months, and 0 days.'");
    }

    /**
     * Verifies that {@link AgeResult#toString()} correctly formats the output
     * when all age components are single-digit values. The format does NOT use
     * pluralization logic (no "1 year" vs "2 years") — it always uses the
     * plural form as specified in the AAP.
     */
    @Test
    @DisplayName("toString with single digit values formats correctly")
    void testToStringWithSingleDigitValues() {
        // Arrange — create an AgeResult with single-digit years, months, days
        AgeResult result = new AgeResult(1, 1, 1, 13L, 366L);

        // Act
        String actual = result.toString();

        // Assert — single-digit values must follow the same plural format (no special singularization)
        assertEquals("Your age is 1 years, 1 months, and 1 days.", actual,
                "toString() with single-digit values must use the same plural format without singularization");
    }
}
