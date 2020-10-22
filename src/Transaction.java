/**
 * Creates instances of transactions made by the customer
 * 
 * COPYRIGHT (C) 2020 Zaki Rangwala. All Rights Reserved.
 * 
 * @author Zaki Rangwala
 * @version 1.01 2020-04-16
 */
public class Transaction {
	private String description;
	private String account;
	private Double opening_balance;
	private Double amount;
	private Double ending_balance;

	/**
	 * Constructor : intializes object with the
	 * description,acount,date,opening_balance,amount,ending_balance
	 * 
	 * @param last_name  Last name of customer
	 * @param first_name First name of customer
	 * @param sin        SIN number of customer
	 * @param year       Birth year of customer
	 * @param month      Birth month of customer
	 * @param day        Birth day of customer
	 */
	public Transaction(String description, String account, Double opening_balance, Double amount,
			Double ending_balance) {
		this.description = description;
		this.account = account;
		this.opening_balance = opening_balance;
		this.amount = amount;
		this.ending_balance = ending_balance;
	}

	/**
	 * Gets the Description of the transaction
	 * 
	 * @return description of the transaction is returned
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Assigns the description of the transaction
	 * 
	 * @param description transaction's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the Type of Account of the Customer
	 * 
	 * @return Account type is returned
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Assigns the type of account of the customer
	 * 
	 * @param account account being used during transaction
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Gets the opening balance of the transaction
	 * 
	 * @return the opening balance
	 */
	public Double getOpeningBalance() {
		return opening_balance;
	}

	/**
	 * Assigns the opening balance of the transaction
	 * 
	 * @param opening_alance opening balance of the transaction
	 */
	public void setOpeningBalance(Double opening_balance) {
		this.opening_balance = opening_balance;
	}

	/**
	 * Gets the amount of the transaction
	 * 
	 * @return amount of the transaction is returned
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of the transaction
	 * 
	 * @param amount amount of the transaction
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * Gets the ending balance of the transaction
	 * 
	 * @return ending balance is returned
	 */
	public Double getEndingBalance() {
		return ending_balance;
	}

	/**
	 * Assigns the ending balance of the transaction
	 * 
	 * @param ending_balance ending balance of the transaction
	 */
	public void setEndingBalance(Double ending_balance) {
		this.ending_balance = ending_balance;
	}

	/**
	 * Writes all the transaction information in the database
	 * 
	 * @return Transaction information is returned
	 */
	public String toString() {
		return "Description : " + getDescription() + "\n" + "Account : " + getAccount() + "\n" + "Opening Balance : "
				+ getOpeningBalance() + "\n" + "Amount of Transaction : " + getAmount() + "\n" + "Ending Balance : "
				+ getEndingBalance();
	}

	/**
	 * Display account activity of the customer
	 * 
	 * @return Account activity is returned
	 */
	public String account_activity() {
		return getDescription() + "\n" + getAccount() + "\n" + getOpeningBalance() + "\n" + getAmount() + "\n"
				+ getEndingBalance();
	}
}