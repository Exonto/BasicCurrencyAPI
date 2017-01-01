This open source project was built by Tyler J. Syme
Contact me at "tylersyme@gmail.com" if you have any questions regarding this project
--------------------------------------------------------------------------------------------

This is a small Java currency library that can be used to perform basic operations with 
simulated currencies.

The library supports 157 different world currencies and provides the ability to perform math 
and, more importantly, conversion operations upon them. The exchange rates are seemlessly 
kept up to date using a Yahoo!® web service and allows full control over the refresh rate.
These refreshes can take anywhere from 15 to 30 seconds to complete and should not be
done any more than necessary. The refresh operation is performed on a separate thread
and should not noticeably interfere during runtime. 
Currencies which Yahoo!® may, in the future, no longer recognize will automatically be 
labelled as invalid and should not be used due to a non-existant exchange rate.

This library also protects against gradual conversion accuracy decay. A currency which has
been converted multiple times to new currencies may have a sizeable difference in accuracy
due to accumulating fractional division errors. However, this library prevents this decay
from occuring due to multiple conversions. Note: This does not remove conversion 
inaccuracies entirely given that the first conversion between two currencies may have a
mathematically unavoidable division error. Rather, this prevents further discrepancies
which may result from continued conversions.
Further details may be found in the documentation.

Technical Notes/Example Usages
--------------------------------------------------------------------------------------------

Creating a new Currency using this library is very simple:
Currency c = new Currency(CurrencyType.USD, 6.50); // Creates a new US Dollar Currency worth $6.50

To convert this currency to Euros, for example, call the .convert() method:
Currency c = c.convert(CurrencyType.EUR); // Converts $6.50 into Euros
System.out.println(c); // Will output: €6.18 as of 12/29/2016 (Of course this number changes constantly)

These are just two of the available 157 currencies.

It is important to keep in mind that the Currency class is immutable and therefore thread safe.
