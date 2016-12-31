package com.gmail.tylersyme.currency;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * <p>
 * Stores basic information on all versions of currency found throughout the
 * world including four standard X currencies (precious metals). Each type 
 * is identified by its corresponding ISO 4217:2015 currency code. Visit the
 * <a href="http://www.iso.org/iso/home/standards/currency_codes.htm">
 * ISO Website</a> for more information.
 * </p>
 * Each entry contains the following information:
 * <ul>
 * <li>Currency Code/Abbreviation</li>
 * <li>Currency Name</li>
 * <li>Currency Symbol</li>
 * <li>Exchange Rate (Quoted against the USD)</li>
 * </ul>
 * <p>
 * Some symbols require UTF-8 encoding in order to be 
 * output properly. Logically, however, they will be evaluated the same.<br>
 * <b>Warning:</b> Many currencies share the exact same symbol even if they have
 * no correlation with each other. Additionally, many currency's may have many
 * symbols. This list prefers the native language symbol over the Latin version
 * unless the native symbol is not available in UTF-8 encoding.
 * </p>
 * <p>
 * Every currency's exchange rate is quoted against the USD. For 
 * example, a euro's exchange rate to the dollar is approximately 1.1115 
 * <b>(1 euro = 1.1115 dollars)</b> at the time of this documentation.<br>
 * Using this USD based exchange rate, all currency types may be compared to
 * each other through their shared relationship to the USD.
 * </p>
 * <p>
 * Exchange rates are constantly changing between currencies. This API uses a 
 * Yahoo!® web service to calculate the exchange rates between any two 
 * currencies. These exchange rates are updated on an hourly basis but only if
 * there is a need for them to be updated.<br>
 * As a result, no update will occur unless a call to 
 * {@link CurrencyType#getExchangeRate} is performed on any enum type. This 
 * update is global and will affect all CurrencyTypes on a separate thread.
 * </p>
 */
public enum CurrencyType
{
	/*
	 * Currencies with no symbol will substitute their abbreviation to demark
	 * currencies.
	 */
	
	// Common currency codes
	AUD("AUD", "australian dollar", "$"),
	GBP("GBP", "british pound", "£"),
	EUR("EUR", "euro", "€"),
	JPY("JPY", "japanese yen", "¥"),
	CHF("CHF", "swiss franc"), // No symbol
	CAD("CAD", "canadian dollar", "$"),
	USD("USD", "us dollar", "$"),
	
	// Precious metal currency codes
	XAU("XAU", "gold ounce", "Au"),
	XAG("XAG", "silver ounce", "Ag"),
	XPT("XPT", "platinum ounce", "Pt"),
	XPD("XPD", "palladium ounce", "Pd"),
	
	// Less common currency codes
	AFN("AFN", "afghanistan afghani", "؋"),			
	ALL("ALL", "albanian lek", "L"),
	DZD("DZD", "algerian dinar", "دج"),
	AOA("AOA", "angolan kwanza", "Kz"),
	ARS("ARS", "argentine peso", "$"),
	AMD("AMD", "armenian dram"),
	AWG("AWG", "aruban florin", "ƒ"),
	AZN("AZN", "azerbaijan new manat", "₼"),
	BSD("BSD", "bahamian dollar", "$"),
	BHD("BHD", "bahraini dinar", "$"),
	BDT("BDT", "bangladeshi taka", "৳"),
	BBD("BBD", "barbados dollar", "$"),
	BYR("BYR", "belarusian ruble", "Br"),
	BZD("BZD", "belize dollar", "BZ$"),
	BMD("BMD", "bermudian dollar", "$"),
	BTN("BTN", "bhutan ngultrum", "Nu."),
	BOB("BOB", "bolivian boliviano", "Bs."),
	BAM("BAM", "bosnian mark", "KM"),
	BWP("BWP", "botswana pula", "P"),
	BRL("BRL", "brazilian real", "R$"),
	BND("BND", "brunei dollar", "B$"),
	BGN("BGN", "bulgarian lev", "лв."),
	BIF("BIF", "burundi franc", "FBu"),
	XOF("XOF", "CFA franc BCEAO", "CFA"),
	XAF("XAF", "CFA franc BEAC", "FCFA"),
	XPF("XPF", "CFP franc", "F"),
	KHR("KHR", "cambodian riel", "៛"),
	CVE("CVE", "cape verde escudo", "Esc"),
	KYD("KYD", "cayman islands dollar", "$"),
	CLP("CLP", "chilean peso", "$"),
	CNY("CNY", "chinese yuan/renminbi", "¥"),
	COP("COP", "colombian peso", "$"),
	KMF("KMF", "comoros franc", "CF"),
	CDF("CDF", "congolese franc", "FC"),
	CRC("CRC", "costa rican colon", "₡"),
	HRK("HRK", "croatian kuna", "kn"),
	CUC("CUC", "cuban convertible peso", "CUC"),
	CUP("CUP", "cuban peso", "$MN"),
	CYP("CYP", "cyprus pound", "	£"),
	CZK("CZK", "czech koruna", "Kč"),
	DKK("DKK", "danish krone", "	kr."),
	DJF("DJF", "djibouti franc", "Fdj"),
	DOP("DOP", "dominican peso", "RD$"),
	XCD("XCD", "east caribbean dollar", "$"),
	EGP("EGP", "egyptian pound", "ج.م"),
	SVC("SVC", "el salvador colon", "₡"),
	ETB("ETB", "ethiopian birr", "ብር"),
	FKP("FKP", "falkland islands pound", "£"),
	FJD("FJD", "fiji dollar", "FJ$"),
	GMD("GMD", "gambian dalasi", "D"),
	GEL("GEL", "georgian lari", "₾"),
	GHS("GHS", "ghanaian new cedi", "GH₵"),
	GIP("GIP", "gibraltar pound", "£"),
	GTQ("GTQ", "guatemalan quetzal", "Q"),
	GNF("GNF", "guinea franc", "FG"),
	GYD("GYD", "guyanese dollar", "G$"),
	HTG("HTG", "haitian gourde", "G"),
	HNL("HNL", "honduran lempira", "L"),
	HKD("HKD", "hong kong dollar", "HK$"),
	HUF("HUF", "hungarian forint", "Ft"),
	ISK("ISK", "iceland krona", "kr"),
	INR("INR", "indian rupee", "₹"),
	IDR("IDR", "indonesian rupiah", "Rp"),
	IRR("IRR", "iranian rial", "﷼"),
	IQD("IQD", "iraqi dinar", "	ع.د"),
	ILS("ILS", "israeli new shekel", "₪"),
	JMD("JMD", "jamaican dollar", "$"),
	JOD("JOD", "jordanian dinar", "JOD"),
	KZT("KZT", "kazakhstan tenge", "₸"),
	KES("KES", "kenyan shilling", "KSh"),
	KWD("KWD", "kuwaiti dinar", "د.ك"),
	KGS("KGS", "kyrgyzstani som", "сом"),
	LAK("LAK", "lao kip", "₭"),
	LVL("LVL", "latvian lats", "Ls"),
	LBP("LBP", "lebanese pound", "ل.ل"),
	LSL("LSL", "lesotho loti", "L"),
	LRD("LRD", "liberian dollar", "L$"),
	LYD("LYD", "libyan dinar", "ل.د"),
	LTL("LTL", "lithuanian litas", "Lt"),
	MOP("MOP", "macau pataca", "MOP$"),
	MKD("MKD", "macedonian denar", "ден"),
	MGA("MGA", "malagasy ariary"), // No symbol
	MWK("MWK", "malawi kwacha", "MK"),
	MYR("MYR", "malaysian ringgit", "RM"),
	MVR("MVR", "maldive rufiyaa", "ރ"),
	MRO("MRO", "mauritanian ouguiya", "UM"),
	MUR("MUR", "mauritius rupee", "Rs"),
	MXN("MXN", "mexican peso", "Mex$"),
	MDL("MDL", "moldovan leu"), // No symbol
	MNT("MNT", "mongolian tugrik", "₮"),
	MAD("MAD", "moroccan dirham", "MAD"),
	MZN("MZN", "mozambique new metical", "MT"),
	MMK("MMK", "myanmar kyat", "K"),
	ANG("ANG", "netherlands antillian guilder", "ƒ"),
	NAD("NAD", "namibia dollar", "N$"),
	NPR("NPR", "nepalese rupee", "रू"),
	NZD("NZD", "new zealand dollar", "$"),
	NIO("NIO", "nicaraguan cordoba oro", "C$"),
	NGN("NGN", "nigerian naira", "₦"),
	KPW("KPW", "north korean won", "₩"),
	NOK("NOK", "norwegian kroner", "kr"),
	OMR("OMR", "omani rial", "ر.ع."),
	PKR("PKR", "pakistan rupee", "Rs"),
	PAB("PAB", "panamanian balboa", "B/."),
	PGK("PGK", "papua new guinea kina", "K"),
	PYG("PYG", "paraguay guarani", "₲"),
	PEN("PEN", "peruvian nuevo sol", "S/"),
	PHP("PHP", "philippine peso", "₱"),
	PLN("PLN", "polish zloty", "zł"),
	QAR("QAR", "qatari rial", "ر.ق"),
	RON("RON", "romanian new lei"), // No symbol
	RUB("RUB", "russian rouble", "₽"),
	RWF("RWF", "rwandan franc", "FRw"),
	WST("WST", "samoan tala", "WS$"),
	STD("STD", "sao tome/principe dobra", "	Db"),
	SAR("SAR", "saudi riyal", "ر.س"),
	RSD("RSD", "serbian dinar", "РСД"),
	SCR("SCR", "seychelles rupee", "SR"),
	SLL("SLL", "sierra leonean leone", "Le"),
	SGD("SGD", "singapore dollar", "S$"),
	SIT("SIT", "slovenian tolar", "€"),
	SBD("SBD", "solomon islands dollar", "SI$"),
	SOS("SOS", "somali shilling", "Sh.So."),
	ZAR("ZAR", "south african rand", "R"),
	KRW("KRW", "south korean won", "₩"),
	LKR("LKR", "sri lanka rupee", "රු"),
	SHP("SHP", "st helena pound", "£"),
	SDG("SDG", "sudanese pound", "ج.س."),
	SRD("SRD", "suriname dollar", "$"),
	SZL("SZL", "swaziland lilangeni", "L"),
	SEK("SEK", "swedish krona", "kr"),
	SYP("SYP", "syrian pound", "£S"),
	TWD("TWD", "taiwan new dollar", "NT$"),
	TZS("TZS", "tanzanian shilling", "TSh"),
	THB("THB", "thai baht", "฿"),
	TOP("TOP", "tonga pa'anga", "T$"),
	TTD("TTD", "trinidad/tobago dollar", "TT$"),
	TND("TND", "tunisian dinar", "د.ت"),
	TRY("TRY", "turkish new lira", "YTL"),
	UGX("UGX", "uganda shilling", "USh"),
	UAH("UAH", "ukraine hryvnia", "₴"),
	UYU("UYU", "uruguayan peso", "$U"),
	AED("AED", "united arab emirates dirham", "د.إ"),
	VUV("VUV", "vanuatu vatu", "VT"),
	VND("VND", "vietnamese dong", "₫"),
	UZS("UZS", "uzbekistan som", "som"),
	YER("YER", "yemeni rial", "﷼");
	
// -----------------------------------------------------------------------------
	
	/**
	 * The rate at which exchange rates are able to be updated.<br>
	 * Default value is 3,600,000 ms or 1 hour.
	 */
	public static long updateRateMillis = 3_600_000; // Default is one hour
	
	/**
	 * This field represents a future point in time. When any call to 
	 * {@link CurrencyType#getExchangeRate} is made, all exchange rates for
	 * each currency type will be updated and a new update point will be set.
	 */
	private static long updateExchangeRatesAfter = System.currentTimeMillis();
	
	/** Whether the exchange rates are currently being processed and updated. */
	private static boolean isUpdating = false;
	
	/**
	 * Returns whether exchange rates are in the process of being updated.
	 */
	public static boolean isUpdating()
	{
		return isUpdating;
	}
	
// -----------------------------------------------------------------------------
	
	private String abbreviation;
	private String name;
	private String symbol;
	private float exchangeRate = 0.0f; // The exchange rate to the USD
	private boolean isValid = true; // Whether this currency is in circulation
	
	private boolean isUpdated = false;
	
	private CurrencyType(
			 String abbreviation, 
			 String name, 
			 String symbol)
	{
		this.abbreviation = abbreviation;
		this.name = name;
		this.symbol = symbol;
	}
	
	/**
	 * In a few cases, the abbreviation may be the same as its symbol.
	 * 
	 * @param abbreviation The {@code Currency's} abbreviation and symbol
	 * @param name The full name of the {@code Currency}
	 */
	private CurrencyType(
			 String abbreviation, 
			 String name)
	{
		this.abbreviation = abbreviation;
		this.name = name;
		this.symbol = abbreviation;
	}
	
	/**
	 * Returns whether this currency type has an assigned symbol.<br>
	 * Certain currencies do not have these particular symbols and instead use
	 * their abbreviation to denote their currency on paper.
	 * 
	 * @return Whether there is a symbol associated with this currency
	 */
	public boolean hasSymbol()
	{
		return this.symbol.equals("");
	}

// -----------------------------------------------------------------------------
// Static Exchange Rate Update Methods
// -----------------------------------------------------------------------------	

	public static void updateExchangeRate(CurrencyType type)
	{
		HttpURLConnection connection = null;
		
		try
		{
			URL url = new URL(
					"http://download.finance.yahoo.com/d/quotes.csv?s=" + 
					type.getAbbreviation() + 
					"USD=X&f=l1&e=.cs");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty(
					"Content-Type",
					"application/x-www-form-urlencoded");
	
			connection.setRequestProperty(
					"Content-Length", "");
			connection.setRequestProperty("Content-Language", "en-US");
	
			connection.setUseCaches(false);
			connection.setDoOutput(true);
	
			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes("");
			wr.close();
	
			// Get exchange rate response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); 
			
			String line;
			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}

			rd.close();
			try 
			{
				type.exchangeRate = Float.parseFloat(response.toString());
				type.isUpdated = true;
			} catch (NumberFormatException e) // The value was not a float
			{
				// Assumes that this currency is no longer supported by Yahoo!®
				type.exchangeRate = 0.0f;
				type.isValid = false;
			}
		} catch (UnknownHostException e) // No connection
		{
			System.err.println("Error: Connection to Yahoo!® finance services "
					+ "could not be established.");
		} catch (Exception e)
		{
			
		} finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
	}
	
	/**
	 * Globally updates every currency's exchange rate on a separate thread.
	 */
	public synchronized static void updateExchangeRates()
	{
		// Set all types to an "un-updated" status
		// This is required to avoid overlapping updates if a currency type
		// is being updated explicitly
		// Overlapping updates can cause inconsistencies when converting
		// currencies during a full scale update
		for (CurrencyType type : CurrencyType.values())
		{
			type.isUpdated = false;
		}
		
		isUpdating = true;
		
		// Update exchange rates within this thread
		Thread updateRatesThread = new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				for (int idx = 0; idx < CurrencyType.values().length; idx++)
				{
					CurrencyType type = CurrencyType.values()[idx];
					if (type.isUpdated == false) // May already be updated
					{
						updateExchangeRate(type);
					}
				}
				isUpdating = false;
			}
		});
		updateRatesThread.start();
	}

	/**
	 * Returns whether exchange rates need to be updated based upon whether
	 * the current point in time has passed
	 * {@link CurrencyType#updateExchangeRatesAfter}.
	 * 
	 * @return Whether currency types should have their exchange rates updated
	 */
	private static boolean needsExchangeRateUpdate()
	{
		return System.currentTimeMillis() >= updateExchangeRatesAfter;
	}
	
	/**
	 * Causes the next exchange rate update to occur 
	 * {@link CurrencyType#updateRateMillis} milliseconds in the future.
	 */
	private static void resetUpdatePoint()
	{
		updateExchangeRatesAfter = 
				System.currentTimeMillis() + updateRateMillis;
	}
	
// -----------------------------------------------------------------------------
// Getters (No Setters)
// -----------------------------------------------------------------------------
	
	public String getAbbreviation()
	{
		return this.abbreviation;
	}
	public String getName()
	{
		return name;
	}
	public String getSymbol()
	{
		return symbol;
	}
	
	/**
	 * Returns the exchange rate of this currency by comparison to the USD.<br>
	 * For example, as of 07/15/2016 (mm/dd/yyyy) the exchange rate of the EUR
	 * to the USD is 1.1115. In other words, a single euro is worth 1.1115
	 * dollars.
	 * 
	 * @see CurrencyType#getExchangeRate(CurrencyType comparison)
	 * 
	 * @return The exchange rate between this currency and the USD
	 */
	public float getExchangeRate()
	{
		// May update before proceeding
		if (needsExchangeRateUpdate())
		{
			resetUpdatePoint();
			
			// Updates all exchange rates (on a separate thread)
			updateExchangeRates();
		} 
		if (isUpdating == true && this.isUpdated == false)
		{
			// Quickly updates this specific exchange rate
			updateExchangeRate(this);
		}
		
		return this.exchangeRate;
	}
	
	/**
	 * Returns the exchange rate of this currency by comparison to the other
	 * given currency type.<br>
	 * For example, as of 07/15/2016 (mm/dd/yyyy) the exchange rate of the EUR
	 * to the USD is 1.1115. In other words, a single euro is worth 1.1115
	 * dollars.
	 * 
	 * @see CurrencyType#getExchangeRate()
	 * 
	 * @param comparison The currency to be compared with
	 * @return The exchange rate between this currency and the given currency
	 */
	public float getExchangeRate(CurrencyType comparison)
	{
		if (comparison != this)
		{
			float usdExchangeRate = this.getExchangeRate();
			
			return usdExchangeRate / comparison.getExchangeRate();
		} else {
			return 1.0f;
		}
	}

	/**
	 * <p>
	 * Currencies may go out of circulation, in which case the currency no 
	 * longer holds an exchange rate. This will return false if the currency is 
	 * no longer supported by the world market.
	 * </p>
	 * <p>
	 * An invalid currency will have an exchange rate of 0.0
	 * </p>
	 * 
	 * @return Whether this currency is supported by the world market
	 */
	public boolean isValid()
	{
		return isValid;
	}
	
}















