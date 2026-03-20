package com.agecalculator;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Core business logic class that encapsulates age calculation using
 * {@link java.time.Period} and {@link java.time.temporal.ChronoUnit}.
 *
 * <p>Provides methods for:</p>
 * <ul>
 *   <li>Exact age breakdown into years, months, and days</li>
 *   <li>Total age expressed in months</li>
 *   <li>Total age expressed in days</li>
 *   <li>Countdown (in days) to the next birthday</li>
 * </ul>
 *
 * <p>This class follows the Object-Oriented Programming principle of
 * <em>single responsibility</em>: it encapsulates <strong>all</strong> age
 * calculation business logic and has no console I/O or validation
 * responsibilities. Input validation is handled by {@code DateValidator},
 * and console interaction is managed by {@code Main} and
 * {@code DateInputHandler}.</p>
 *
 * <p><strong>Testability:</strong> Every method that depends on the current
 * date exposes an overloaded variant that accepts a {@code referenceDate}
 * parameter, allowing unit tests to inject a deterministic date instead of
 * relying on the system clock.</p>
 *
 * <p>Uses exclusively Java standard library classes — no third-party date
 * libraries are used.</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 * @see AgeResult
 */
public class AgeCalculator {

    /**
     * Calculates the exact age from the given date of birth using the
     * current system date ({@link LocalDate#now()}).
     *
     * <p>This convenience method delegates to
     * {@link #calculateAge(LocalDate, LocalDate)} with
     * {@code LocalDate.now()} as the reference date. For deterministic
     * unit testing, prefer the two-parameter overload.</p>
     *
     * @param dob the date of birth; must not be {@code null} and should
     *            represent a date in the past or today
     * @return an {@link AgeResult} containing the decomposed age and totals
     * @see #calculateAge(LocalDate, LocalDate)
     */
    public AgeResult calculateAge(LocalDate dob) {
        return calculateAge(dob, LocalDate.now());
    }

    /**
     * Calculates the exact age from the given date of birth to the specified
     * reference date.
     *
     * <p>This overload supports testability by allowing injection of a
     * deterministic reference date instead of relying on
     * {@link LocalDate#now()}. Unit tests should always use this method
     * to ensure reproducible, clock-independent results.</p>
     *
     * <p>The method computes:</p>
     * <ul>
     *   <li>Years, months, and days via {@link Period#between(LocalDate, LocalDate)}</li>
     *   <li>Total months via {@link ChronoUnit#MONTHS}</li>
     *   <li>Total days via {@link ChronoUnit#DAYS}</li>
     * </ul>
     *
     * <p><strong>Leap year handling:</strong> {@code Period.between()} handles
     * leap year birthdays natively and correctly. For example, if DOB is
     * {@code 2000-02-29} and the reference date is {@code 2024-03-01}, the
     * calculation correctly accounts for the leap day.</p>
     *
     * @param dob           the date of birth; must not be {@code null}
     * @param referenceDate the date to calculate the age relative to;
     *                      must not be {@code null}
     * @return an {@link AgeResult} containing the decomposed age (years,
     *         months, days) and the total age in months and days
     */
    public AgeResult calculateAge(LocalDate dob, LocalDate referenceDate) {
        Period period = Period.between(dob, referenceDate);

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        long totalMonths = ChronoUnit.MONTHS.between(dob, referenceDate);
        long totalDays = ChronoUnit.DAYS.between(dob, referenceDate);

        return new AgeResult(years, months, days, totalMonths, totalDays);
    }

    /**
     * Returns the total age in months between the date of birth and the
     * specified reference date.
     *
     * <p>Calculated using {@link ChronoUnit#MONTHS}{@code .between(dob, referenceDate)}.
     * A partial month at the end of the period is <em>not</em> counted.</p>
     *
     * @param dob           the date of birth; must not be {@code null}
     * @param referenceDate the date to measure to; must not be {@code null}
     * @return the total number of complete months between the two dates
     */
    public long getTotalMonths(LocalDate dob, LocalDate referenceDate) {
        return ChronoUnit.MONTHS.between(dob, referenceDate);
    }

    /**
     * Returns the total age in days between the date of birth and the
     * specified reference date.
     *
     * <p>Calculated using {@link ChronoUnit#DAYS}{@code .between(dob, referenceDate)}.</p>
     *
     * @param dob           the date of birth; must not be {@code null}
     * @param referenceDate the date to measure to; must not be {@code null}
     * @return the total number of days between the two dates
     */
    public long getTotalDays(LocalDate dob, LocalDate referenceDate) {
        return ChronoUnit.DAYS.between(dob, referenceDate);
    }

    /**
     * Calculates the number of days remaining until the next birthday from
     * the given reference date.
     *
     * <p>The algorithm:</p>
     * <ol>
     *   <li>Projects the birthday onto the reference year using
     *       {@link LocalDate#withYear(int)}.</li>
     *   <li>If that projected birthday has already passed (or falls on the
     *       reference date itself), it advances to the following year using
     *       {@link LocalDate#plusYears(long)}.</li>
     *   <li>Returns the number of days between the reference date and the
     *       next birthday via {@link ChronoUnit#DAYS}.</li>
     * </ol>
     *
     * <p><strong>Leap year edge case:</strong> If the date of birth is
     * February 29 and the target year is <em>not</em> a leap year,
     * {@code LocalDate.withYear()} automatically adjusts the date to
     * February 28. This is the documented behaviour of
     * {@link LocalDate#withYear(int)} in the Java standard library.</p>
     *
     * @param dob           the date of birth; must not be {@code null}
     * @param referenceDate the date from which to count days until the next
     *                      birthday; must not be {@code null}
     * @return the number of days until the next birthday (always &gt; 0)
     */
    public long getNextBirthdayCountdown(LocalDate dob, LocalDate referenceDate) {
        // Project the birthday onto the current (reference) year.
        // For Feb 29 DOBs in non-leap years, withYear() gracefully adjusts to Feb 28.
        LocalDate nextBirthday = dob.withYear(referenceDate.getYear());

        // If the birthday this year has already passed or is today, move to next year.
        if (!nextBirthday.isAfter(referenceDate)) {
            nextBirthday = dob.withYear(referenceDate.getYear() + 1);
        }

        return ChronoUnit.DAYS.between(referenceDate, nextBirthday);
    }
}
