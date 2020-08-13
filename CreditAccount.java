/**
 * Creates an instance of a credit account from class account
 * 
 * COPYRIGHT (C) 2020 Zaki Rangwala. All Rights Reserved.
 * 
 * @author Zaki Rangwala
 * @version 1.01 2020-04-16
 * 
 */

public class CreditAccount extends Account {
	/**
	 * Constructor : intializes object with an opening balance
	 * 
	 * @param balance bank balance of account
	 */
	public CreditAccount() {
	}

	/**
	 * Pays off debt in the credit account, getting the credit balance to 0
	 * 
	 */
	public void paid() {
		double debt = getOpeningBalance();
		double credit = debt - debt;
		super.setOpeningBalance(credit);
	}

	/**
	 * Does not change the balance of credit card account
	 * 
	 * @param number not used in the method
	 */
	@Override
	public void deposit(double number) {
		setOpeningBalance((super.getOpeningBalance()));
	}

	/**
	 * To String method that provides name of account
	 * 
	 * @return type of account being used
	 */
	public String toString() {
		return "Credit Account";
	}

}