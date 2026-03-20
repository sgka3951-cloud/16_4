package com.agecalculator;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Comprehensive unit tests for the {@link AgeCalculator} core business logic class.
 *
 * <p>All test methods use the two-parameter overloaded
 * {@link AgeCalculator#calculateAge(LocalDate, LocalDate)} method with hardcoded
 * {@link LocalDate#of(int, int, int)} values, ensuring deterministic and
 * reproducible results regardless of when the tests are executed.</p>
 *
 * <p>Test coverage includes:</p>
 * <ul>
 *   <li>Normal DOB age calculation</li>
 *   <li>Leap year DOB handling (29/02/2000)</li>
 *   <li>Edge case: DOB equal to reference date (age = 0)</li>
 *   <li>Edge case: DOB exactly one year ago</li>
 *   <li>Very old DOB (01/01/1900)</li>
 *   <li>Total months and total days via dedicated methods</li>
 *   <li>Next birthday countdown (birthday upcoming, already passed, and today)</li>
 * </ul>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see AgeCalculator
 * @see AgeResult
 */
public class AgeCalculatorTest {

    /** The calculator instance under test, re-created before each test method. */
    private AgeCalculator calculator;

    /**
     * Creates a fresh {@link AgeCalculator} instance before every test method,
     * guaranteeing complete test isolation with no shared mutable state.
     */
    @BeforeEach
    void setUp() {
        calculator = new AgeCalculator();
    }

    // -----------------------------------------------------------------------
    // Normal DOB
    // -----------------------------------------------------------------------

    /**
     * Verifies correct age calculation for a standard (non-leap-year) date of
     * birth. Uses DOB 15/08/1998 with reference date 01/03/2026.
     *
     * <p>Expected breakdown:
     * <ul>
     *   <li>Aug 15 1998 → Aug 15 2025 = 27 complete years</li>
     *   <li>Aug 15 2025 → Feb 15 2026 = 6 complete months</li>
     *   <li>Feb 15 2026 → Mar 1 2026 = 14 days</li>
     * </ul>
     * Result: 27 years, 6 months, 14 days.</p>
     */
    @Test
    @DisplayName("Normal DOB: 15/08/1998 to reference 01/03/2026")
    void testNormalDob() {
        LocalDate dob = LocalDate.of(1998, 8, 15);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        AgeResult result = calculator.calculateAge(dob, ref);

        assertNotNull(result, "AgeResult should not be null");
        assertEquals(27, result.getYears(), "Years component should be 27");
        assertEquals(6, result.getMonths(), "Months component should be 6");
        assertEquals(14, result.getDays(), "Days component should be 14");
    }

    // -----------------------------------------------------------------------
    // Leap Year DOB
    // -----------------------------------------------------------------------

    /**
     * Verifies that a leap year birthday (29/02/2000) is handled correctly.
     *
     * <p>{@code Period.between(2000-02-29, 2026-03-01)} computes the age by
     * stepping forward 26 full years from Feb 29 2000 to Feb 28 2026 (since
     * 2026 is not a leap year, Java adjusts to Feb 28), then counts the
     * remaining day to reach Mar 1 2026.</p>
     *
     * <p>Expected: 26 years, 0 months, 1 day.</p>
     */
    @Test
    @DisplayName("Leap year DOB: 29/02/2000")
    void testLeapYearDob() {
        LocalDate dob = LocalDate.of(2000, 2, 29);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        AgeResult result = calculator.calculateAge(dob, ref);

        assertNotNull(result, "AgeResult should not be null");
        assertEquals(26, result.getYears(), "Years component should be 26");
        assertEquals(0, result.getMonths(), "Months component should be 0");
        assertEquals(1, result.getDays(), "Days component should be 1");
    }

    // -----------------------------------------------------------------------
    // DOB on Reference Date (Age = 0)
    // -----------------------------------------------------------------------

    /**
     * Edge case: the date of birth is the same as the reference date.
     * All age components — years, months, days, total months, and total days —
     * must be zero.
     */
    @Test
    @DisplayName("DOB on reference date: age should be 0")
    void testDobOnReferenceDate() {
        LocalDate dob = LocalDate.of(2026, 3, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        AgeResult result = calculator.calculateAge(dob, ref);

        assertNotNull(result, "AgeResult should not be null");
        assertEquals(0, result.getYears(), "Years component should be 0");
        assertEquals(0, result.getMonths(), "Months component should be 0");
        assertEquals(0, result.getDays(), "Days component should be 0");
        assertEquals(0L, result.getTotalMonths(), "Total months should be 0");
        assertEquals(0L, result.getTotalDays(), "Total days should be 0");
    }

    // -----------------------------------------------------------------------
    // DOB Exactly One Year Ago
    // -----------------------------------------------------------------------

    /**
     * Verifies that a DOB exactly one year before the reference date yields
     * exactly 1 year with zero months and zero days.
     */
    @Test
    @DisplayName("DOB exactly one year ago")
    void testDobExactlyOneYearAgo() {
        LocalDate dob = LocalDate.of(2025, 3, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        AgeResult result = calculator.calculateAge(dob, ref);

        assertNotNull(result, "AgeResult should not be null");
        assertEquals(1, result.getYears(), "Years component should be 1");
        assertEquals(0, result.getMonths(), "Months component should be 0");
        assertEquals(0, result.getDays(), "Days component should be 0");
    }

    // -----------------------------------------------------------------------
    // Very Old DOB
    // -----------------------------------------------------------------------

    /**
     * Verifies correct calculation for a very old date of birth (01/01/1900).
     *
     * <p>{@code Period.between(1900-01-01, 2026-03-01)} yields:
     * <ul>
     *   <li>Jan 1 1900 → Jan 1 2026 = 126 complete years</li>
     *   <li>Jan 1 2026 → Mar 1 2026 = 2 complete months</li>
     *   <li>Remaining days = 0 (both on the 1st)</li>
     * </ul>
     * Result: 126 years, 2 months, 0 days.</p>
     */
    @Test
    @DisplayName("Very old DOB: 01/01/1900")
    void testVeryOldDob() {
        LocalDate dob = LocalDate.of(1900, 1, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        AgeResult result = calculator.calculateAge(dob, ref);

        assertNotNull(result, "AgeResult should not be null");
        assertEquals(126, result.getYears(), "Years component should be 126");
        assertEquals(2, result.getMonths(), "Months component should be 2");
        assertEquals(0, result.getDays(), "Days component should be 0");
    }

    // -----------------------------------------------------------------------
    // Total Months
    // -----------------------------------------------------------------------

    /**
     * Verifies that {@link AgeCalculator#getTotalMonths(LocalDate, LocalDate)}
     * returns the correct count of complete months between DOB and reference date.
     *
     * <p>From Jan 1 2025 to Mar 1 2026 = 14 complete months.</p>
     */
    @Test
    @DisplayName("getTotalMonths returns correct total months")
    void testGetTotalMonths() {
        LocalDate dob = LocalDate.of(2025, 1, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        long totalMonths = calculator.getTotalMonths(dob, ref);

        assertEquals(14L, totalMonths, "Total months from 2025-01-01 to 2026-03-01 should be 14");
    }

    // -----------------------------------------------------------------------
    // Total Days
    // -----------------------------------------------------------------------

    /**
     * Verifies that {@link AgeCalculator#getTotalDays(LocalDate, LocalDate)}
     * returns the correct count of days between DOB and reference date.
     *
     * <p>From Feb 1 2026 to Mar 1 2026 = 28 days (February 2026 is not a
     * leap year, so it has 28 days).</p>
     */
    @Test
    @DisplayName("getTotalDays returns correct total days")
    void testGetTotalDays() {
        LocalDate dob = LocalDate.of(2026, 2, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        long totalDays = calculator.getTotalDays(dob, ref);

        assertEquals(28L, totalDays, "Total days from 2026-02-01 to 2026-03-01 should be 28");
    }

    // -----------------------------------------------------------------------
    // Next Birthday Countdown — Birthday Not Yet Passed This Year
    // -----------------------------------------------------------------------

    /**
     * Verifies the countdown when the birthday has NOT yet occurred in the
     * reference year. DOB Dec 25 1998, reference Mar 1 2026 — birthday this
     * year is Dec 25 2026, which is 299 days away.
     *
     * <p>Days from Mar 1 to Dec 25 2026:
     * Mar(30) + Apr(30) + May(31) + Jun(30) + Jul(31) + Aug(31) +
     * Sep(30) + Oct(31) + Nov(30) + Dec(25) = 299 days.</p>
     */
    @Test
    @DisplayName("getNextBirthdayCountdown: birthday hasn't passed this year")
    void testNextBirthdayCountdownBirthdayNotPassed() {
        LocalDate dob = LocalDate.of(1998, 12, 25);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        long countdown = calculator.getNextBirthdayCountdown(dob, ref);

        assertEquals(299L, countdown,
                "Days from 2026-03-01 to next birthday 2026-12-25 should be 299");
    }

    // -----------------------------------------------------------------------
    // Next Birthday Countdown — Birthday Already Passed This Year
    // -----------------------------------------------------------------------

    /**
     * Verifies the countdown when the birthday has already occurred in the
     * reference year. DOB Jan 15 1998, reference Mar 1 2026 — birthday this
     * year (Jan 15 2026) has already passed, so the next birthday is
     * Jan 15 2027, which is 320 days away.
     *
     * <p>Days from Mar 1 2026 to Jan 15 2027:
     * Mar(30) + Apr(30) + May(31) + Jun(30) + Jul(31) + Aug(31) +
     * Sep(30) + Oct(31) + Nov(30) + Dec(31) + Jan(15) = 320 days.</p>
     */
    @Test
    @DisplayName("getNextBirthdayCountdown: birthday already passed this year")
    void testNextBirthdayCountdownBirthdayAlreadyPassed() {
        LocalDate dob = LocalDate.of(1998, 1, 15);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        long countdown = calculator.getNextBirthdayCountdown(dob, ref);

        assertEquals(320L, countdown,
                "Days from 2026-03-01 to next birthday 2027-01-15 should be 320");
    }

    // -----------------------------------------------------------------------
    // Next Birthday Countdown — Birthday Is Today
    // -----------------------------------------------------------------------

    /**
     * Verifies the countdown when today IS the birthday. DOB Mar 1 1998,
     * reference Mar 1 2026 — the birthday falls on the reference date, so
     * the next birthday is Mar 1 2027 (365 days, since 2026 is not a leap year).
     */
    @Test
    @DisplayName("getNextBirthdayCountdown: birthday is today")
    void testNextBirthdayCountdownBirthdayIsToday() {
        LocalDate dob = LocalDate.of(1998, 3, 1);
        LocalDate ref = LocalDate.of(2026, 3, 1);

        long countdown = calculator.getNextBirthdayCountdown(dob, ref);

        assertEquals(365L, countdown,
                "Days from 2026-03-01 to next birthday 2027-03-01 should be 365");
    }
}
