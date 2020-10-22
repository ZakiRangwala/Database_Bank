/**
 * Creates an instance of a customer
 * 
 * COPYRIGHT (C) 2020 Zaki Rangwala. All Rights Reserved.
 * 
 * @author Zaki Rangwala
 * @version 1.01 2020-04-16
 *
 */
public class Customer {
	private int id;
	private String last_name;
	private String first_name;
	private String sin;
	private String year;
	private String month;
	private String day;
	private SavingsAccount savingsAccount;
	private ChequingAccount chequingAccount;
	private CreditAccount creditAccount;

	/**
	 * Constructor : intializes object with the name,sin number,birth year,birth
	 * month and birth day
	 * 
	 * @param last_name  Last name of customer
	 * @param first_name First name of customer
	 * @param sin        SIN number of customer
	 * @param year       Birth year of customer
	 * @param month      Birth month of customer
	 * @param day        Birth day of customer
	 */
	public Customer(int id, String last_name, String first_name, String sin, String year, String month, String day) {
		this.id = id;
		this.last_name = last_name;
		this.first_name = first_name;
		this.sin = sin;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	/**
	 * Constructor : intializes object with the name,sin number,birth year,birth
	 * month and birth day
	 * 
	 * @param last_name       Last name of customer
	 * @param first_name      First name of customer
	 * @param sin             SIN number of customer
	 * @param year            Birth year of customer
	 * @param month           Birth month of customer
	 * @param day             Birth day of customer
	 * @param savingsAccount  instance of SavingsAccount
	 * @param chequingAccount instance of ChequingAccount
	 * @param creditAccount   instance of creditAccount
	 * 
	 */
	public Customer(int id, String last_name, String first_name, String sin, String year, String month, String day,
			SavingsAccount savingsAccount, ChequingAccount chequingAccount, CreditAccount creditAccount) {
		this.id = id;
		this.last_name = last_name;
		this.first_name = first_name;
		this.sin = sin;
		this.year = year;
		this.month = month;
		this.day = day;
		this.savingsAccount = savingsAccount;
		this.chequingAccount = chequingAccount;
		this.creditAccount = creditAccount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the First Name of the Customer
	 * 
	 * @return first name of customer is returned
	 */
	public String getFirstName() {
		return first_name;
	}

	/**
	 * Assigns the first name of the Customer
	 * 
	 * @param first_name first name of the customer
	 */
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * Gets the Last Name of the Customer
	 * 
	 * @return last name of the customer is returned
	 */
	public String getLastName() {
		return last_name;
	}

	/**
	 * Assigns the last name of the Customer
	 * 
	 * @param last_name last name of the customer
	 */
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * Gets the Full Name of the Customer
	 * 
	 * @return full name of the customer is returned
	 */
	public String getName() {
		return getLastName() + "," + getFirstName();

	}

	/**
	 * Combines the last and first name of the Customer to produce the full name
	 * 
	 * @param last_name  last name of the customer
	 * @param first_name first name of the customer
	 */
	public void setName(String last_name, String first_name) {
		this.last_name = last_name;
		this.first_name = first_name;
	}

	/**
	 * Gets the SIN number of the Customer
	 * 
	 * @return SIN number of customer is returned
	 */
	public String getSin() {
		return sin;
	}

	/**
	 * Assigns the SIN of the Customer
	 * 
	 * @param sin SIN number of the customer
	 */
	public void setSin(String sin) {
		this.sin = sin;
	}

	/**
	 * Gets the Birth Year of the Customer
	 * 
	 * @return birth year of customer is returned
	 */
	public String getBirthYear() {
		return year;
	}

	/**
	 * Assigns the birth year of the Customer
	 * 
	 * @param year birth year of the customer
	 */
	public void setBirthYear(String year) {
		this.year = year;
	}

	/**
	 * Gets the Birth Month of the Customer
	 * 
	 * @return birth month of customer is returned
	 */
	public String getBirthMonth() {
		return month;
	}

	/**
	 * Assigns the birth month of the Customer
	 * 
	 * @param month birth month of the customer
	 */
	public void setBirthMonth(String month) {
		this.month = month;
	}

	/**
	 * Gets the Birth Day of the Customer
	 * 
	 * @return birth day of customer is returned
	 */
	public String getBirthDay() {
		return day;
	}

	/**
	 * Assigns the birth day of the Customer
	 * 
	 * @param day birth day of the customer
	 */
	public void setBirthDay(String day) {
		this.day = day;
	}

	/**
	 * Gets the ChequingAccount of the Customer
	 * 
	 * @return ChequingAccount of customer is returned
	 */
	public ChequingAccount getChequingAccount() {
		return chequingAccount;
	}

	/**
	 * Gets the SavingsAccount of the Customer
	 * 
	 * @return SavingsAccount of customer is returned
	 */
	public SavingsAccount getSavingsAccount() {
		return savingsAccount;
	}

	/**
	 * Gets the CreditAccount of the Customer
	 * 
	 * @return CreditAccount of customer is returned
	 */
	public CreditAccount getCreditAccount() {
		return creditAccount;
	}

	/**
	 * Gets the Chequing Account Balance of the Customer
	 * 
	 * @return Chequing account balance of customer is returned
	 */
	public Double getChequingAccountBalance() {
		return chequingAccount.getOpeningBalance();
	}

	/**
	 * Gets the Saving Account Balance of the Customer
	 * 
	 * @return Savings account balance of customer is returned
	 */
	public Double getSavingsAccountBalance() {
		return savingsAccount.getOpeningBalance();
	}

	/**
	 * Gets the Credit Account Balance of the Customer
	 * 
	 * @return Credit account balance of customer if returned
	 */
	public Double getCreditAccountBalance() {
		return creditAccount.getOpeningBalance();
	}

	/**
	 * Writes all the customer information in the database
	 * 
	 * @return Customer information is returned
	 */
	public String toString() {
		return getLastName() + "\n" + getFirstName() + "\n" + getSin() + "\n" + getBirthYear() + "\n" + getBirthMonth()
				+ "\n" + getBirthDay() + "\n" + getSavingsAccountBalance() + "\n" + getChequingAccountBalance() + "\n"
				+ getCreditAccountBalance();
	}

	/**
	 * Displays Customer information
	 * 
	 * @return Customer name and sin is returned
	 */
	public String display() {
		return getId() + ". " + getLastName() + " " + getFirstName() + " " + getSin();
	}
}