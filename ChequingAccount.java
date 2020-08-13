/**
 * Creates an instance of a chequing account from class account that can
 * manipulate monetary transactions
 * 
 * COPYRIGHT (C) 2020 Zaki Rangwala. All Rights Reserved.
 * 
 * @author Zaki Rangwala
 * @version 1.01 2020-04-16
 */
public class ChequingAccount extends Account {
	/**
	 * Constructor : intializes object with an opening balance
	 * 
	 * @param balance bank balance of account
	 */
	public ChequingAccount() {
	}

	/**
	 * To String method that provides name of account
	 * 
	 * @return type of account being used
	 */
	public String toString() {
		return "Chequing Account";
	}
}