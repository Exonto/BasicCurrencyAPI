package com.gmail.tylersyme.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>
 * This represents a form of real world currency with both an attached amount
 * and affiliated {@code CurrencyType}.
 * </p>
 * <p>
 * Internally, this class uses {@code BigDecimal} to manage precision and
 * rounding. This allows calculations to be made safely without the slow decay
 * which can result from floating point math. The maximum precision for any
 * currency is 9 digits following the decimal point. The round mode is set to
 * {@code RoundingMode.HALF_EVEN}, also known as 
 * "<a href="http://c2.com/cgi/wiki?BankersRounding">Banker's Rounding</a>".
 * </p>
 * <p>
 * Special attention is dedicated towards preserving a {@code Currency}'s true
 * value. Even when using {@code BigDecimal} to ensure consistency between
 * precision and rounding, there will always be a small amount of error. This 
 * is due to the fact that often times two currencies cannot be perfectly 
 * converted to each other.<br>
 * This class solves the degrading accuracy problem. A {@code Currency} may be
 * safely converted and modified as much as is needed without any effect on the
 * accuracy.<br>
 * <b>Warning:</b> This does not mean that no small amount of error exists. 
 * Instead, this class protects this error from gradually increasing as more
 * conversions and modifications are made.
 * </p>
 * <p>
 * This class is immutable and thread safe.
 * </p>
 * 
 * @see CurrencyType
 * @see BigDecimal
 * @see RoundingMode
 */

public final class Currency
{	
	// These represent the default BigDecimal#setScale parameters
	private static final RoundingMode roundMode = RoundingMode.HALF_EVEN;
	private static final int MAX_PRECISION = 9;
	
// -----------------------------------------------------------------------------
	
	private final CurrencyType type; // This currency's associated type
	private final BigDecimal amount; // The decimal amount of this currency
	
	/*
	 * (Near) Perfect Conversions - Explanation
	 * -------------------------------------------------------------------------
	 * 
	 * If many calculations are performed on a currency, it will eventually and
	 * gradually grow more and more inaccurate. This class, and specifically 
	 * this field, solve this issue.
	 * The source represents where this currency originally came from.
	 * Using this information, some assumptions can be used to determine
	 * perfectly accurate results.
	 * 
	 * If the currency was originally of type USD and is then converted to EUR, 
	 * there would likely be some marginal division imperfection. This is
	 * entirely unavoidable. However, what is avoidable is a buildup of this 
	 * error. The first conversion will indeed have a small amount of error.
	 * Now lets convert back to USD. Because of the small division error, the 
	 * conversion back to USD would be slightly incorrect. This is where source
	 * comes to fix this issue.
	 * 
	 * In the background, source would have saved this currency's original 
	 * value, which was USD. So even though the currency was converted with some
	 * division error to EUR, it has saved its old USD value which is still
	 * accurate. If a conversion is made back to USD from EUR, the source would
	 * be used to convert back to the exact original value.
	 * 
	 * Not only does this work when converting back to the original value, it 
	 * also works when converting to other CurrencyTypes. Such as:
	 * 
	 * Currency c = new Currency(CurrencyType.USD, 5.0);
	 * c.convert(CurrencyType.EUR) // €4.5400890
	 *  .convert(CurrencyType.RUB) // ₽318.4713555
	 *  .convert(CurrencyType.JPY) // ¥531.9149015
	 *  .convert(CurrencyType.EUR) // Still €4.5400890 (No compounding error)
	 *  .convert(CurrencyType.RUB) // Still ₽318.4713555
	 *  .convert(CurrencyType.JPY) // Still ¥531.9149015
	 *  .convert(CurrencyType.USD) // Still $5.0
	 *  
	 * This is possible because source remains the same even when conversions
	 * change the currency. As a result, when a conversion is made from EUR to
	 * RUB (Russian Ruble), all math is being performed with the source currency 
	 * rather than the current currency. 
	 * 
	 * This method has one slight drawback. The relationship between the source
	 * and the actual currency will likely have that small division error
	 * (unless they happen to convert perfectly to each other). This means that
	 * if a currency is converted to itself, the currency amount may change
	 * slightly because of the source vs actual currency imperfection. This is
	 * simply the result of two currencies (the source and the actual currency)
	 * being imperfectly converted to each other.
	 */
	private final Currency source;
	
	/**
	 * Sets currency type to USD and amount to 0.0.
	 */
	public Currency()
	{
		this.type = CurrencyType.USD;
		this.amount = new BigDecimal(0.0).setScale(MAX_PRECISION, roundMode);
		this.source = this;
	}
	
	/**
	 * Default currency type is USD.
	 */
	public Currency(double amount)
	{
		this.type = CurrencyType.USD;
		this.amount = new BigDecimal(amount).setScale(MAX_PRECISION, roundMode);
		this.source = this;
	}
	
	public Currency(CurrencyType type, double amount)
	{
		this.type = type;
		this.amount = new BigDecimal(amount).setScale(MAX_PRECISION, roundMode);
		this.source = this;
	}
	
	public Currency(Currency toCopy)
	{
		this.type = toCopy.type;
		this.amount = toCopy.amount;
		this.source = toCopy.source;
	}
	
// -----------------------------------------------------------------------------
// Private Constructor(s)
// -----------------------------------------------------------------------------
	
	private Currency(Currency source, CurrencyType type, double amount)
	{
		this.type = type;
		this.amount = new BigDecimal(amount).setScale(MAX_PRECISION, roundMode);
		this.source = source;
	}
	
// -----------------------------------------------------------------------------
// Metric Methods (Advanced Getters)
// -----------------------------------------------------------------------------
	
	/**
	 * Returns whether this currency represents debt (has a negative amount).
	 */
	public boolean isDebt()
	{
		return this.amount.doubleValue() < 0;
	}
	
	/**
	 * Returns whether this currency represents profit (has a positive amount 
	 * including 0).
	 */
	public boolean isProfit()
	{
		return this.amount.doubleValue() >= 0;
	}
	
	/**
	 * Returns whether the two {@code Currency} {@code amount}s are exactly
	 * equivalent. A conversion is made before the comparison is made.
	 * 
	 * @param other The {@code Currency} this is being compared to
	 */
	public boolean isSameAmount(Currency other)
	{
		return (this.source.convert(this.type).amount == 
				other.source.convert(this.type).amount);
	}
	
// -----------------------------------------------------------------------------
// Modifier Methods (Immutable Style)
// -----------------------------------------------------------------------------
	
	/**
	 * Returns a new {@code Currency} resulting from changing the current amount
	 * by the given amount. This assumes that the given amount is of the same
	 * {@code CurrencyType} as this {@code Currency}'s {@code CurrencyType}.
	 * 
	 * @param amount The amount to change by
	 * @return The {@code Currency} with modified amount
	 * 
	 * @see Currency#changeBy(Currency)
	 */
	public Currency changeBy(double amount)
	{
		return this.changeBy(new Currency(this.type, amount));
	}
	
	/**
	 * Returns a new {@code Currency} resulting from changing the current amount
	 * by the given amount. The given {@code CurrencyType} is converted to this
	 * {@code Currency}'s {@code CurrencyType}.
	 * 
	 * @param amount The amount to change by
	 * @param type The {@code CurrencyType} of the {@code amount} given
	 * @return The {@code Currency} with modified amount
	 * 
	 * @see Currency#changeBy(Currency)
	 */
	public Currency changeBy(double amount, CurrencyType type)
	{
		return this.changeBy(new Currency(type, amount));
	}
	
	/**
	 * <p>
	 * Returns a new {@code Currency} resulting from changing the current amount
	 * by the other Currency's converted amount.
	 * </p>
	 * <p>
	 * The given Currency is implicitly converted to this Currency's 
	 * {@code CurrencyType} before the amount is modified.
	 * </p>
	 * 
	 * @param toChangeBy The other {@code Currency} modifying this 
	 * 					 {@code Currency}
	 * @return The {@code Currency} with modified amount
	 */
	public Currency changeBy(Currency toChangeBy)
	{
		Currency newSource = new Currency(
				this.source.type, 
				
				this.source.amount.add(
				toChangeBy.
				convert(this.source.type). // Convert other currency
				getAmount()). // Get the converted amount
				setScale(MAX_PRECISION, roundMode). // Apply scale/rounding
				doubleValue());
		
		return new Currency(
				newSource,
				this.type, 
				newSource.convertToAmount(this.type));
	}
	
	/**
	 * <p>
	 * Returns a new {@code Currency} resulting from setting the amount to
	 * given amount.
	 * </p>
	 * 
	 * @param amount The amount of the new currency
	 * @return The {@code Currency} with modified amount
	 */
	public Currency changeTo(double amount)
	{
		// Abandons the current source because it not longer applies when 
		// directly setting the amount
		return new Currency(this.type, amount);
	}
	
	/**
	 * <p>
	 * Returns the resulting conversion between this {@code CurrencyType} and 
	 * the given {@code CurrencyType}.
	 * </p>
	 * 
	 * @param type The type to be converted to
	 * @return A new instance of Currency with converted amount
	 */
	public Currency convert(CurrencyType type)
	{
		BigDecimal wrapper = new BigDecimal(this.source.getExchangeRate(type))
			.setScale(MAX_PRECISION, roundMode);
		
		return new Currency(
				this.source, // Source
				type, // Currency Type
				wrapper. // Amount
				multiply(this.source.amount).
				setScale(MAX_PRECISION, roundMode).
				doubleValue());
	}
	
	/**
	 * Returns the amount which results from converting this 
	 * {@code CurrencyType} to the given {@code CurrencyType}.
	 * 
	 * @param type The type to be converted to
	 * @return The converted currency amount
	 */
	public double convertToAmount(CurrencyType type)
	{
		// Get the exchange rate of the source compared to the currency type
		BigDecimal wrapper = new BigDecimal(this.source.getExchangeRate(type));

		// Based on this currency's source, calculates the value 
		return wrapper.
			   multiply(this.source.amount).
			   setScale(MAX_PRECISION, roundMode).
			   doubleValue();
	}
	
	/**
	 * Returns the negative value of this {@code Currency}.<br>
	 * If this {@code Currency} is already, negative no change is made.
	 */
	public Currency toDebt()
	{
		if (this.isDebt())
		{
			return this;
		} else {
			Currency newSource = new Currency(
					this.source.type, 
					this.source.amount.negate().doubleValue());
			
			return new Currency(
					newSource, 
					this.type, 
					this.amount.negate().doubleValue());
		}
	}
	
	/**
	 * Returns the positive (absolute) value of this {@code Currency}.<br>
	 * If this {@code Currency} is already positive, no change is made.
	 */
	public Currency toProfit()
	{
		if (this.isProfit())
		{
			return this;
		} else {
			Currency newSource = new Currency(
					this.source.type, 
					this.source.amount.abs().doubleValue());
			
			return new Currency(
					newSource, 
					this.type, 
					this.amount.abs().doubleValue());
		}
	}
	
// -----------------------------------------------------------------------------
// String Representations
// -----------------------------------------------------------------------------
	
	/**<p>
	 * Will return this {@code Currency} as a string; with its symbol followed
	 * by its decimal {@code amount}. This decimal value will be rounded to the
	 * hundreds place unless the {@code amount} is smaller than .01, in which
	 * case the unrounded decimal value will be shown.
	 * </p>
	 * <p>
	 * See {@link Currency#toStringUnrounded()} If you wish to always display an 
	 * unrounded value.
	 * </p>
	 */
	@Override
	public String toString()
	{
		// Rounds up to the hundreds if large enough
		if (this.amount.abs().doubleValue() >= .01)
		{
			DecimalFormat formatter = new DecimalFormat("##.##");
			return this.type.getSymbol() + formatter.format(this.amount);
		} else { // If amount is very small, will display the decimal value
			return this.type.getSymbol() + this.amount;
		}
		
	}
	
	/**
	 * Will return this {@code Currency}'s full decimal value as opposed to
	 * {@link Currency#toString()} which may shrink the decimal precision down 
	 * to the hundreds place: <b>(##.##)</b> for the sake of readability.
	 * 
	 * @return This {@code Currency}'s symbol followed by its full decimal 
	 * 		   value.
	 */
	public String toStringUnrounded()
	{
		return this.type.getSymbol() + 
			   this.amount.setScale(MAX_PRECISION, roundMode);
	}
	
	/**
	 * Will return this {@code Currency}'s information in its entirety.<br>
	 * <b>Example:</b> {@code "us dollar (USD) - $4.55"}
	 */
	public String toStringFullName()
	{
		return this.type.getName() + 
				" (" + this.type.getAbbreviation() + 
				") = " + 
				this.type.getSymbol() +
				this.getAmount().setScale(MAX_PRECISION, roundMode);
	}
	
// -----------------------------------------------------------------------------
// Equals and Hashcode Overrides
// -----------------------------------------------------------------------------
	
	/**
	 * <p>
	 * Returns whether both the {@code CurrencyType} and {@code amount} are the
	 * same.<br>
	 * <b>No conversion between currencies will occur.</b>
	 * </p>
	 * <p>
	 * See {@link Currency#isSameAmount(Currency)} to specifically compare two
	 * {@code Currency}'s {@code amount}s.
	 * </p>
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Currency))
			return false;
		Currency other = (Currency) obj;
		if (amount == null)
		{
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
// -----------------------------------------------------------------------------
// Getters (No Setters)
// -----------------------------------------------------------------------------

	public CurrencyType getType()
	{
		return type;
	}
	public BigDecimal getAmount()
	{
		return amount;
	}
	public float getExchangeRate()
	{
		return this.type.getExchangeRate();
	}
	public float getExchangeRate(CurrencyType type)
	{
		return this.type.getExchangeRate(type);
	}
	
}
