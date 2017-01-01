This open source project was built by Tyler J. Syme
Contact me at "tylersyme@gmail.com" if you have any questions regarding this project
--------------------------------------------------------------------------------------------

This is a small Java currency library that can be used to perform basic operations with 
simulated currencies.

The library supports 157 different world currencies and provides the ability to perform math 
and, more importantly, conversion operations upon them. The exchange rates are seemlessly 
kept up to date using a Yahoo!® web service and allows full control over the refresh rate.
Currencies which Yahoo!® may, in the future, no longer recognize will automatically be 
labelled as invalid and should not be used due to a non-existant exchange rate.

This library also protects against gradual conversion accuracy decay. A currency which has
been converted multiple times to new currencies may have a sizeable difference in accuracy
due to accumulating fractional division errors. However, this library prevents this decay
from occuring due to multiple conversions. Note: This does not remove conversion 
inaccuracies entirely given that the first conversion between two currencies may have a
mathematically unavoidable division error. Rather, this prevents further discrepancies
which may result from continued conversions.
