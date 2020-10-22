/**
 * Can create any instance of bank account(Credit,Chequing,Saving) that can
 * manipulate monetary transactions
 * 
 * COPYRIGHT (C) 2020 Zaki Rangwala. All Rights Reserved.
 * 
 * @author Zaki Rangwala
 * @version final 2020-04-18
 * 
 * 
 */
public abstract class Account {
	private int id;
	private Double balance;

	/**
	 * Constructor : intializes object with an opening balance
	 * 
	 * @param balance bank balance of account
	 */
	public Account() {
		balance = null;
	}

	/**
	 * Gets the Balance of the customer's account
	 * 
	 * @return balance of the account is returned
	 */
	public Double getOpeningBalance() {
		return balance;
	}

	/**
	 * Sets the Balance of the customer's account
	 * 
	 * @param balance manipulates current balance with new balance
	 */
	public void setOpeningBalance(Double balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Withdraws a sum of money from the customer's preffered bank account
	 * 
	 * @param number manipulates the current bank balance with the amount of money
	 *               the customer chooses to withdraw
	 */
	public void withdraw(double number) {
		double start_balance = getOpeningBalance();
		double end_balance = start_balance - number;
		setOpeningBalance(end_balance);
	}

	/**
	 * Deposits a sum of money to the customer's preffered bank account
	 * 
	 * @param number manipulates the current bank balance with the amount of money
	 *               the customer chooses to deposit
	 */
	public void deposit(double number) {
		double start_balance = getOpeningBalance();
		double end_balance = start_balance + number;
		setOpeningBalance(end_balance);
	}

	/**
	 * To String abstract method, allows child classes to return type of account
	 *
	 */
	public abstract String toString();
}