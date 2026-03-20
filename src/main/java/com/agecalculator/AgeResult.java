package com.agecalculator;

/**
 * Immutable data model class that holds the computed age result.
 *
 * <p>Stores the age decomposed into years, months, and days, along with the
 * total age expressed in months and total age expressed in days. All fields
 * are set once at construction time and cannot be modified afterwards,
 * enforcing full immutability.</p>
 *
 * <p>Provides getter methods for every field and a {@link #toString()} method
 * that returns the formatted age string in the exact format:
 * {@code Your age is X years, Y months, and Z days.}</p>
 *
 * <p>This class has zero dependencies on other project classes and uses only
 * Java standard library types ({@code int}, {@code long}, {@link String}).</p>
 *
 * @author Age Calculator Application
 * @version 1.0
 */
public class AgeResult {

    /** The years component of the calculated age. */
    private final int years;

    /** The months component of the calculated age (0–11). */
    private final int months;

    /** The days component of the calculated age (0–30). */
    private final int days;

    /** The total age expressed entirely in months (from {@code ChronoUnit.MONTHS.between}). */
    private final long totalMonths;

    /** The total age expressed entirely in days (from {@code ChronoUnit.DAYS.between}). */
    private final long totalDays;

    /**
     * Constructs an {@code AgeResult} with the given age components.
     *
     * @param years       the years component of the age (non-negative)
     * @param months      the months component of the age (0–11)
     * @param days        the days component of the age (0–30)
     * @param totalMonths the total age in months
     * @param totalDays   the total age in days
     */
    public AgeResult(int years, int months, int days, long totalMonths, long totalDays) {
        this.years = years;
        this.months = months;
        this.days = days;
        this.totalMonths = totalMonths;
        this.totalDays = totalDays;
    }

    /**
     * Returns the years component of the calculated age.
     *
     * @return the number of complete years in the age
     */
    public int getYears() {
        return years;
    }

    /**
     * Returns the months component of the calculated age.
     *
     * @return the number of months beyond complete years (0–11)
     */
    public int getMonths() {
        return months;
    }

    /**
     * Returns the days component of the calculated age.
     *
     * @return the number of days beyond complete months (0–30)
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the total age expressed entirely in months.
     *
     * <p>This value is computed using {@code ChronoUnit.MONTHS.between(dob, referenceDate)}
     * and represents the complete number of months between the date of birth and
     * the reference date.</p>
     *
     * @return the total age in months
     */
    public long getTotalMonths() {
        return totalMonths;
    }

    /**
     * Returns the total age expressed entirely in days.
     *
     * <p>This value is computed using {@code ChronoUnit.DAYS.between(dob, referenceDate)}
     * and represents the complete number of days between the date of birth and
     * the reference date.</p>
     *
     * @return the total age in days
     */
    public long getTotalDays() {
        return totalDays;
    }

    /**
     * Returns a formatted string representation of the age result.
     *
     * <p>The output format is exactly:
     * {@code Your age is X years, Y months, and Z days.}
     * where X, Y, and Z are the years, months, and days components respectively.</p>
     *
     * <p>Example: {@code Your age is 27 years, 6 months, and 15 days.}</p>
     *
     * @return the formatted age string
     */
    @Override
    public String toString() {
        return String.format("Your age is %d years, %d months, and %d days.", years, months, days);
    }
}
