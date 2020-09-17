import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Bank {
    private static Scanner scan = new Scanner(System.in);
    private static Transaction transactions = new Transaction("", "", 0.0, 0.0, 0.0);
    private static SavingsAccount savings_info = new SavingsAccount();
    private static ChequingAccount chequing_info = new ChequingAccount();
    private static CreditAccount credit_info = new CreditAccount();
    private static Customer customer_info = new Customer(0, " ", " ", " ", " ", " ", " ", savings_info, chequing_info,
            credit_info);
    private static String url = "jdbc:sqlserver://localhost;databaseName=Bank;integratedSecurity=true;";
    private static String[] sins;

    /**
     * Adds a customer to the database and adds an instance of it to the array of
     * customers, transaction is made when account is opened
     * 
     * @exception IOException if a stream to file cannot be written or closed
     */
    public static void addCustomer() {
        SavingsAccount savingsAccount = new SavingsAccount();
        ChequingAccount chequingAccount = new ChequingAccount();
        CreditAccount creditAccount = new CreditAccount();
        Customer customer = new Customer(0, " ", " ", " ", " ", " ", " ", savingsAccount, chequingAccount,
                creditAccount);
        String user_input = "";
        System.out.println("\nPlease enter the following information : ");
        System.out.print("1. Last Name(Doe) : ");
        user_input = scan.nextLine();
        customer.setLastName(user_input);
        System.out.print("2. First Name(John) : ");
        user_input = scan.nextLine();
        customer.setFirstName(user_input);
        System.out.print("3. SIN(123456789) : ");
        user_input = scan.nextLine();
        boolean validate = isNumber(user_input);
        while (!validate) {
            System.out.print("Please enter a 9-digit number : ");
            user_input = scan.nextLine();
            validate = isNumber(user_input);
        }
        boolean present = search_sins(Long.parseLong(user_input));
        while (present) {
            System.out.print("Customer is already present in the database.\nPlease try again : ");
            user_input = scan.nextLine();
            validate = isNumber(user_input);
            while (!validate) {
                System.out.print("Please enter a 9-digit number : ");
                user_input = scan.nextLine();
                validate = isNumber(user_input);
            }
            present = search_sins(Long.parseLong(user_input));
        }
        customer.setSin(user_input);
        System.out.print("4. Birth Year(YYYY): ");
        while (!scan.hasNextInt()) {
            System.out.print("Please enter an integer : ");
            user_input = scan.nextLine();
        }
        user_input = scan.nextLine();
        customer.setBirthYear(user_input);
        System.out.print("5. Birth Month(MM): ");
        while (!scan.hasNextInt()) {
            System.out.print("Please enter an integer : ");
            user_input = scan.nextLine();
        }
        user_input = scan.nextLine();
        customer.setBirthMonth(user_input);
        System.out.print("6. Birth Day(DD): ");
        while (!scan.hasNextInt()) {
            System.out.print("Please enter an integer : ");
            user_input = scan.nextLine();
        }
        user_input = scan.nextLine();
        customer.setBirthDay(user_input);
        int check = checkAge(Integer.parseInt(customer.getBirthYear()), Integer.parseInt(customer.getBirthMonth()),
                Integer.parseInt(customer.getBirthDay()));
        if (check == 0) {
            System.out.println("\nYou are only eligible for the Savings Account as you are not of age");
            System.out.print("Please enter the opening balance $(2000.56) : ");
            while (!scan.hasNextDouble()) {
                System.out.print("Please enter a number : ");
                user_input = scan.nextLine();
            }
            user_input = scan.nextLine();
            savingsAccount.setOpeningBalance(Double.parseDouble(user_input));
            System.out.println("\nSavings Account succesfully opened!");
            transactions = new Transaction("Open an account", savingsAccount.toString(),
                    customer.getSavingsAccountBalance(), 0.0, customer.getSavingsAccountBalance());
        }
        if (check == 1) {
            System.out.println("\nYou are eligible for the Chequing Account,Savings Account and Credit Card");
            System.out.print(
                    "\nWhich account(s) would you like to open :\n1.Savings Account\n2.Chequing Account\n3.Credit Account\nPlease enter an option : ");
            user_input = scan.nextLine();
            if (user_input.equals("1") && savingsAccount.getOpeningBalance() == null) {
                System.out.print("\nPlease enter the opening balance : ");
                while (!scan.hasNextDouble()) {
                    System.out.print("Please enter an number : ");
                    user_input = scan.nextLine();
                }
                user_input = scan.nextLine();
                savingsAccount.setOpeningBalance(Double.parseDouble(user_input));
                System.out.println("\nSavings Account succesfully opened!");
                transactions = new Transaction("Open an account", savingsAccount.toString(),
                        customer.getSavingsAccountBalance(), 0.0, customer.getSavingsAccountBalance());
            } else if (user_input.equals("2") && chequingAccount.getOpeningBalance() == null) {
                System.out.print("\nPlease enter the opening balance : ");
                while (!scan.hasNextDouble()) {
                    System.out.print("Please enter an number : ");
                    user_input = scan.nextLine();
                }
                user_input = scan.nextLine();
                chequingAccount.setOpeningBalance(Double.parseDouble(user_input));
                System.out.println("\nChequing Account succesfully opened!");
                transactions = new Transaction("Open an account", chequingAccount.toString(),
                        customer.getChequingAccountBalance(), 0.0, customer.getChequingAccountBalance());
            } else if (user_input.equals("3") && creditAccount.getOpeningBalance() == null) {
                creditAccount.setOpeningBalance(0.0);
                System.out.println("\nCredit Card Account succesfully opened!");
                transactions = new Transaction("Open an account", creditAccount.toString(),
                        customer.getCreditAccountBalance(), 0.0, customer.getCreditAccountBalance());
            } else {
                System.out.print("\nThis account has already been issued!");
            }
        }
        write(customer);
        write_transaction(customer);
    }

    public static boolean isNumber(String word) {
        if (word == null) {
            return false;
        }
        int length = word.length();
        if (length != 9) {
            return false;
        }
        int i = 0;
        if (word.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = word.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static void deleteCustomer() {
        Long sin = (long) 0;
        String name = "";
        String user_input = "";
        boolean validation = true;
        if (scan.hasNextLong()) {
            sin = Long.parseLong(scan.nextLine());
            validation = search_sins(sin);
            if (!validation) {
                System.out.println("\nThe customer is not present in the database");
            } else {
                System.out.println("\nThe customer is present in the database");
                System.out.print("\nAre you sure you want to remove " + customer_info.getFirstName()
                        + " from the database?\n(Y) or (N) : ");
                user_input = scan.nextLine();
                if (user_input.equalsIgnoreCase("Y")) {
                    delete();
                    System.out.print("\nCustomer deleted successfully!!");
                }
            }
        } else {
            name = scan.nextLine();
            while (!name.contains(",")) {
                System.out.print("Please seperate your first and last name with a comma : ");
                name = scan.nextLine();
            }
            int validate = search_names(name);
            if (validate == -1) {
                System.out.println("\nThe customer is not present in the database");
            } else if (validate == 0) {
                System.out.println("\nThe customer is present in the database");
                System.out.print("\nAre you sure you want to remove " + customer_info.getFirstName()
                        + " from the database?\n(Y) or (N) : ");
                user_input = scan.nextLine();
                if (user_input.equalsIgnoreCase("Y")) {
                    delete();
                    System.out.print("\nCustomer deleted successfully!!");
                }
            } else {
                System.out.print("\nThere are " + validate + " people with the name : " + name
                        + "\nTo confirm, please enter the SIN number of the customer you wish to delete : ");
                user_input = scan.nextLine();
                boolean check = isNumber(user_input);
                while (!check) {
                    System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                    user_input = scan.nextLine();
                    check = isNumber(user_input);
                }
                int present = present(user_input);
                while (present != 0) {
                    System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                    user_input = scan.nextLine();
                    check = isNumber(user_input);
                    while (!check) {
                        System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                        user_input = scan.nextLine();
                        check = isNumber(user_input);
                    }
                    present = present(user_input);
                }
                System.out.print("\nAre you sure you want to remove " + customer_info.getFirstName()
                        + " from the database?\n(Y) or (N) : ");
                user_input = scan.nextLine();
                if (user_input.equalsIgnoreCase("Y")) {
                    delete();
                    System.out.print("\nCustomer deleted successfully!!");
                }
            }
        }
    }

    public static int present(String sin) {
        int return_value = -1;
        for (int i = 0; i < sins.length; i++) {
            if (sins[i].equalsIgnoreCase(sin)) {
                return_value = 0;
            }
        }
        return return_value;
    }

    public static void delete() {
        try {
            Connection connection = DriverManager.getConnection(url);
            // Account
            String sql = "DELETE FROM Customer WHERE Customer.sin_number = " + customer_info.getSin()
                    + "SELECT * FROM Customer GO";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();
            // Transactions
            sql = "DELETE FROM Transactions WHERE Transactions.customer = " + customer_info.getSin()
                    + "SELECT * FROM Transactions GO";
            statement = connection.prepareStatement(sql);
            statement.executeQuery();
            // SavingsAccount
            sql = "DELETE FROM Savings_Account WHERE Savings_Account.id = " + savings_info.getId()
                    + "SELECT * FROM Savings_Account GO";
            statement = connection.prepareStatement(sql);
            statement.executeQuery();
            // ChequingAccount
            sql = "DELETE FROM Chequings_Account WHERE Chequings_Account.id = " + chequing_info.getId()
                    + "SELECT * FROM Chequings_Account GO";
            statement = connection.prepareStatement(sql);
            statement.executeQuery();
            // CreditAccount
            sql = "DELETE FROM Credit_Account WHERE Credit_Account.id = " + credit_info.getId()
                    + "SELECT * FROM Credit_Account GO";
            statement = connection.prepareStatement(sql);
            statement.executeQuery();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static int search_names(String name) {
        String last = name.substring(0, name.indexOf(","));
        String first = name.substring(name.indexOf(",") + 1);
        int counter = 0;
        String sin_append = "";
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT * From Customer WHERE Customer.first_name = ? AND Customer.last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, first);
            statement.setString(2, last);
            ResultSet result = statement.executeQuery();
            if (result.next() == false) {
                return -1;
            } else {
                do {
                    customer_info.setId(result.getInt("id"));
                    customer_info.setLastName(result.getString("last_name"));
                    customer_info.setFirstName(result.getString("first_name"));
                    customer_info.setSin(result.getString("sin_number"));
                    String birthday = result.getString("birthday");
                    customer_info.setBirthDay(birthday.substring(birthday.lastIndexOf('-') + 1));
                    customer_info
                            .setBirthMonth(birthday.substring(birthday.indexOf('-') + 1, birthday.lastIndexOf('-')));
                    customer_info.setBirthYear(birthday.substring(0, birthday.indexOf('-')));
                    savings_info.setId(result.getInt("savings_account"));
                    chequing_info.setId(result.getInt("chequings_account"));
                    credit_info.setId(result.getInt("credit_account"));
                    counter++;
                    sin_append += customer_info.getSin() + " ";
                } while (result.next());
            }
            if (counter > 1) {
                sins = sin_append.split(" ");
                return counter;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean search_sins(Long sin) {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT * FROM Customer WHERE Customer.sin_number = " + sin;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if (result.next() == false) {
                return false;
            } else {
                do {
                    customer_info.setId(result.getInt("id"));
                    customer_info.setLastName(result.getString("last_name"));
                    customer_info.setFirstName(result.getString("first_name"));
                    customer_info.setSin(result.getString("sin_number"));
                    String birthday = result.getString("birthday");
                    customer_info.setBirthDay(birthday.substring(birthday.lastIndexOf('-') + 1));
                    customer_info
                            .setBirthMonth(birthday.substring(birthday.indexOf('-') + 1, birthday.lastIndexOf('-')));
                    customer_info.setBirthYear(birthday.substring(0, birthday.indexOf('-')));
                    savings_info.setId(result.getInt("savings_account"));
                    chequing_info.setId(result.getInt("chequings_account"));
                    credit_info.setId(result.getInt("credit_account"));
                } while (result.next());
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
        return true;
    }

    public static void write(Customer customer) {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "INSERT INTO Customer ([id],[Date_],[first_name], [last_name],[sin_number],[birthday],[savings_account],[chequings_account],[credit_account])"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" + "SELECT * FROM Customer GO";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, connect("Customer"));
            statement.setDate(2, getDate());
            statement.setString(3, customer.getFirstName());
            statement.setString(4, customer.getLastName());
            statement.setString(5, customer.getSin());
            statement.setString(6,
                    customer.getBirthYear() + "-" + customer.getBirthMonth() + "-" + customer.getBirthDay());
            statement.setInt(7, connect("Savings_Account"));
            statement.setInt(8, connect("Chequings_Account"));
            statement.setInt(9, connect("Credit_Account"));
            statement.executeQuery();
            // savings
            String account = "Savings_Account";
            sql = "INSERT INTO " + account + "([id], [opening_balance], [Date_])" + "VALUES(?, ?, ?)" + "SELECT * FROM "
                    + account;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, connect(account));
            if (customer.getSavingsAccountBalance() == null) {
                statement.setNull(2, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(2, customer.getSavingsAccountBalance());
            }
            statement.setDate(3, getDate());
            statement.executeQuery();
            // chequings
            account = "Chequings_Account";
            sql = "INSERT INTO " + account + "([id], [opening_balance], [Date_])" + "VALUES(?, ?, ?)" + "SELECT * FROM "
                    + account;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, connect(account));
            if (customer.getChequingAccountBalance() == null) {
                statement.setNull(2, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(2, customer.getChequingAccountBalance());
            }
            statement.setDate(3, getDate());
            statement.executeQuery();
            // credit
            account = "Credit_Account";
            sql = "INSERT INTO " + account + "([id], [opening_balance], [Date_])" + "VALUES(?, ?, ?)" + "SELECT * FROM "
                    + account;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, connect(account));
            if (customer.getCreditAccountBalance() == null) {
                statement.setNull(2, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(2, customer.getCreditAccountBalance());
            }
            statement.setDate(3, getDate());
            statement.executeQuery();
            connection.close();
        } catch (SQLException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public static int connect(String Table_name) {
        int id = 0;
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT TOP 1 id FROM " + Table_name + " ORDER BY id DESC";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                id = result.getInt("id");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
        return id + 1;
    }

    public static void display() {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT * FROM Customer";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            String[] info = new String[3];
            while (result.next()) {
                info[0] = result.getString("first_name");
                info[1] = result.getString("last_name");
                info[2] = result.getString("sin_number");
                for (int i = 0; i < info.length; i++) {
                    System.out.print(info[i] + " ");
                }
                System.out.println();
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static void sort_name() {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT * FROM Customer " + "ORDER BY Customer.last_name";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            String[] info = new String[3];
            while (result.next()) {
                info[0] = result.getString("first_name");
                info[1] = result.getString("last_name");
                info[2] = result.getString("sin_number");
                for (int i = 0; i < info.length; i++) {
                    System.out.print(info[i] + " ");
                }
                System.out.println();
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static void sort_num() {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT * FROM Customer " + "ORDER BY Customer.sin_number";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            String[] info = new String[3];
            while (result.next()) {
                info[0] = result.getString("first_name");
                info[1] = result.getString("last_name");
                info[2] = result.getString("sin_number");
                for (int i = 0; i < info.length; i++) {
                    System.out.print(info[i] + " ");
                }
                System.out.println();
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static void write_transaction(Customer customer) {
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "INSERT INTO Transactions ([id], [description_], [account],[opening_balance],[amount],[ending_balance], [customer], [Date_])"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" + "SELECT * FROM Transactions GO";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, connect("Transactions"));
            statement.setString(2, transactions.getDescription());
            statement.setString(3, transactions.getAccount());
            if (transactions.getOpeningBalance() == null) {
                statement.setNull(4, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(4, transactions.getOpeningBalance());
            }
            if (transactions.getAmount() == null) {
                statement.setNull(5, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(5, transactions.getAmount());
            }
            if (transactions.getEndingBalance() == null) {
                statement.setNull(6, java.sql.Types.FLOAT);
            } else {
                statement.setDouble(6, transactions.getEndingBalance());
            }
            statement.setString(7, customer.getSin());
            statement.setDate(8, getDate());
            statement.executeQuery();
            connection.close();
        } catch (SQLException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public static Date getDate() {
        Date current_date = new Date(System.currentTimeMillis());
        return current_date;
    }

    public static int checkAge(int year, int month, int day) {
        DateFormat df = new SimpleDateFormat("dd");
        DateFormat mf = new SimpleDateFormat("MM");
        DateFormat yf = new SimpleDateFormat("yyyy");
        java.util.Date dateobj = new java.util.Date();
        int current_day = Integer.parseInt(df.format(dateobj));
        int current_month = Integer.parseInt(mf.format(dateobj));
        int current_year = Integer.parseInt(yf.format(dateobj));
        int age = current_year - year;
        int return_type = 0;
        if (age > 18) {
            return_type = 1;
        } else if (age < 18) {
            return_type = 0;
        } else if (age == 18) {
            int month_diff = current_month - month;
            if (month_diff > 0) {
                return_type = 1;
            } else if (month_diff < 0) {
                return_type = 0;
            } else if (month_diff == 0) {
                int day_diff = current_day - day;
                if (day_diff < 0) {
                    return_type = 0;
                } else if (day_diff > 0) {
                    return_type = 1;
                } else if (day_diff == 0) {
                    return_type = 1;
                }
            }
        }
        return return_type;
    }

    public static void getBalances() {
        try {
            Connection connection = DriverManager.getConnection(url);
            // Savings Account
            String sql = "SELECT * FROM Savings_Account WHERE Savings_Account.id = " + savings_info.getId();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                result.getDouble("opening_balance");
                if (result.wasNull()) {
                    savings_info.setOpeningBalance(null);
                } else if (!result.wasNull()) {
                    savings_info.setOpeningBalance(result.getDouble("opening_balance"));
                }
            }
            // Chequing Account
            sql = "SELECT * FROM Chequings_Account WHERE Chequings_Account.id = " + chequing_info.getId();
            statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                result.getDouble("opening_balance");
                if (result.wasNull()) {
                    chequing_info.setOpeningBalance(null);
                } else if (!result.wasNull()) {
                    chequing_info.setOpeningBalance(result.getDouble("opening_balance"));
                }
            }
            // Credit Account
            sql = "SELECT * FROM Credit_Account WHERE Credit_Account.id = " + credit_info.getId();
            statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                result.getDouble("opening_balance");
                if (result.wasNull()) {
                    credit_info.setOpeningBalance(null);
                } else if (!result.wasNull()) {
                    credit_info.setOpeningBalance(result.getDouble("opening_balance"));
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static void read_transactions() {
        try {
            // counting number of transactions
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT Count(*) AS \"Number\" FROM Transactions WHERE Transactions.customer = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer_info.getSin());
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("Number");
            }
            if (count >= 5) {
                // retaining ids
                sql = "SELECT id FROM Transactions WHERE Transactions.customer = ? " + " ORDER BY Transactions.id";
                statement = connection.prepareStatement(sql);
                statement.setString(1, customer_info.getSin());
                result = statement.executeQuery();
                int[] trans_key = new int[count];
                count = 0;
                while (result.next()) {
                    trans_key[count] = result.getInt("id");
                    count++;
                }
                // reading the 5 recent transactions
                int MAX_TRANSACTIONS = 5;
                sql = "SELECT * FROM Transactions WHERE Transactions.customer = ? AND Transactions.id BETWEEN "
                        + trans_key[count - MAX_TRANSACTIONS] + " AND " + trans_key[count - 1]
                        + "ORDER BY Transactions.id";
                statement = connection.prepareStatement(sql);
                statement.setString(1, customer_info.getSin());
                result = statement.executeQuery();
                while (result.next()) {
                    transactions.setDescription(result.getString("description_"));
                    transactions.setAccount(result.getString("account"));
                    transactions.setOpeningBalance(result.getDouble("opening_balance"));
                    transactions.setAmount(result.getDouble("amount"));
                    transactions.setEndingBalance(result.getDouble("ending_balance"));
                    Date date_time = result.getDate("Date_");
                    System.out.println("\n" + transactions + "\nDate: " + date_time);
                }
                connection.close();
            } else {
                Connection connect = DriverManager.getConnection(url);
                String sql_statement = "SELECT * FROM Transactions WHERE Transactions.customer = ? "
                        + " ORDER BY Transactions.id";
                PreparedStatement statement_exec = connect.prepareStatement(sql_statement);
                statement_exec.setString(1, customer_info.getSin());
                ResultSet res = statement_exec.executeQuery();
                while (res.next()) {
                    transactions.setDescription(res.getString("description_"));
                    transactions.setAccount(res.getString("account"));
                    transactions.setOpeningBalance(res.getDouble("opening_balance"));
                    transactions.setAmount(res.getDouble("amount"));
                    transactions.setEndingBalance(res.getDouble("ending_balance"));
                    Date date_time = res.getDate("Date_");
                    System.out.println("\n" + transactions + "\nDate: " + date_time);
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    public static void account() {
        String user_input = "";
        int age = checkAge(Integer.parseInt(customer_info.getBirthYear()),
                Integer.parseInt(customer_info.getBirthMonth()), Integer.parseInt(customer_info.getBirthDay()));
        getBalances();
        System.out.println("\n" + customer_info.getName() + "\n" + customer_info.getSin());
        System.out.println("Account Balances");
        // Savings
        if (savings_info.getOpeningBalance() == null) {
            System.out.println("Savings : Account not activated");
        } else {
            System.out.println("Savings : " + savings_info.getOpeningBalance());
        }
        // Chequings
        if (chequing_info.getOpeningBalance() == null) {
            System.out.println("Chequings : Account not activated");
        } else {
            System.out.println("Chequings : " + chequing_info.getOpeningBalance());
        }
        // Credit
        if (credit_info.getOpeningBalance() == null) {
            System.out.println("Credit : Account not activated");
        } else {
            System.out.println("Credit : " + credit_info.getOpeningBalance());
        }
        do {
            int check = 0;
            System.out.println(
                    "\nPROFILE MENU\n-----------------------\n1: View account activity\n2: Deposit\n3: Withdraw\n4: Process cheque\n5: Process purchase\n6: Pay bill\n7: Transfer funds\n8: Open account or issue card\n9: Cancel account or card\n10: Return to main menu");
            System.out.print("\nWhat would you like to do ? ");
            user_input = scan.nextLine();
            if (user_input.equals("1")) {
                read_transactions();
                System.out.println();
            } else if (user_input.equals("2")) {
                System.out.print("\nWhich account would you like to deposit to ? ");
                System.out.print("\nAccount Information");
                if (savings_info.getOpeningBalance() != null) {
                    System.out.print("\n1 : Savings Account : " + savings_info.getOpeningBalance());
                }
                if (chequing_info.getOpeningBalance() != null) {
                    System.out.print("\n2 : Chequing Account : " + chequing_info.getOpeningBalance());
                }
                System.out.print("\nPlease choose an option : ");
                user_input = scan.nextLine();
                if (user_input.equals("1")) {
                    double opening_balance = customer_info.getSavingsAccountBalance();
                    System.out.print("How much would you like to deposit : ");
                    while (!scan.hasNextDouble()) {
                        System.out.print("Please enter a number : ");
                        user_input = scan.nextLine();
                    }
                    user_input = scan.nextLine();
                    savings_info.deposit(Double.parseDouble(user_input));
                    System.out.println("\nSucessfully deposited!");
                    transactions = new Transaction("Deposit", savings_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getSavingsAccountBalance());
                    check = 1;
                }
                if (user_input.equals("2")) {
                    double opening_balance = customer_info.getChequingAccountBalance();
                    System.out.print("\nHow much would you like to deposit : ");
                    user_input = scan.nextLine();
                    chequing_info.deposit(Double.parseDouble(user_input));
                    System.out.println("\nSucessfully deposited!");
                    transactions = new Transaction("Deposit", chequing_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getChequingAccountBalance());
                    check = 1;
                }
            } else if (user_input.equals("3")) {
                System.out.print("\nWhich account would you like to withdraw funds from ? ");
                System.out.print("\nAccount Information");
                if (savings_info.getOpeningBalance() != null) {
                    System.out.print("\n1 : Savings Account : " + savings_info.getOpeningBalance());
                }
                if (chequing_info.getOpeningBalance() != null) {
                    System.out.print("\n2 : Chequing Account : " + chequing_info.getOpeningBalance());
                }
                if (credit_info.getOpeningBalance() != null) {
                    System.out.print("\n3 : Credit Account : " + credit_info.getOpeningBalance());
                }
                System.out.print("\nPlease choose an option : ");
                user_input = scan.nextLine();
                if (user_input.equals("1")) {
                    double opening_balance = customer_info.getSavingsAccountBalance();
                    System.out.print("How much would you like to withdraw? : ");
                    user_input = scan.nextLine();
                    savings_info.withdraw(Double.parseDouble(user_input));
                    System.out.println("\nSucessfull withdrawal!");
                    transactions = new Transaction("Withdrawal", savings_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getSavingsAccountBalance());
                    check = 1;
                }
                if (user_input.equals("2")) {
                    double opening_balance = customer_info.getChequingAccountBalance();
                    System.out.print("\nHow much would you like to withdraw? : ");
                    user_input = scan.nextLine();
                    chequing_info.withdraw(Double.parseDouble(user_input));
                    System.out.println("\nSucessfull withdrawal!");
                    transactions = new Transaction("Withdrawal", chequing_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getChequingAccountBalance());
                    check = 1;
                }
                if (user_input.equals("3")) {
                    double opening_balance = customer_info.getCreditAccountBalance();
                    System.out.print("\nHow much would you like to withdraw? : ");
                    user_input = scan.nextLine();
                    credit_info.withdraw(Double.parseDouble(user_input));
                    System.out.println("\nSucessfull withdrawal!");
                    transactions = new Transaction("Withdrawal", credit_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getCreditAccountBalance());
                    check = 1;
                }
            } else if (user_input.equals("4")) {
                final int MINIMUM_AMOUNT = 1000;
                final int TRANSACTION_FEE = 1;
                if (chequing_info.getOpeningBalance() != null) {
                    System.out.print("\nChequing Account : " + chequing_info.getOpeningBalance());
                    System.out.print("\nProcess cheque into chequing account");
                    double opening_balance = customer_info.getChequingAccountBalance();
                    if (opening_balance < MINIMUM_AMOUNT) {
                        chequing_info.withdraw(TRANSACTION_FEE);
                        transactions = new Transaction("Transaction fee", chequing_info.toString(), opening_balance,
                                1.00, customer_info.getChequingAccountBalance());
                        write_transaction(customer_info);
                    }
                    opening_balance = customer_info.getChequingAccountBalance();
                    System.out.print("\nHow much would you like to deposit : ");
                    user_input = scan.nextLine();
                    chequing_info.deposit(Double.parseDouble(user_input));
                    System.out.println("\nSucessfully deposited!");
                    transactions = new Transaction("Deposit a cheque", chequing_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getChequingAccountBalance());
                    check = 1;
                } else {
                    System.out.println("\nYou don't have a chequing account issued to your name!");
                }
            } else if (user_input.equals("5")) {
                if (credit_info.getOpeningBalance() != null) {
                    double opening_balance = customer_info.getCreditAccountBalance();
                    String description;
                    System.out.print("\nEnter the description of the purchase : ");
                    description = scan.nextLine();
                    System.out.print("\nHow much is the purchase ? : ");
                    user_input = scan.nextLine();
                    credit_info.withdraw(Double.parseDouble(user_input));
                    System.out.println("\nPurchase Successfull!");
                    transactions = new Transaction(description, credit_info.toString(), opening_balance,
                            Double.parseDouble(user_input), customer_info.getCreditAccountBalance());
                    check = 1;
                } else {
                    System.out.println("\nYou don't have a credit account issued to your name!");
                }
            } else if (user_input.equals("6")) {
                if ((savings_info.getOpeningBalance() != null) || chequing_info.getOpeningBalance() != null) {
                    if (credit_info.getOpeningBalance() != null) {
                        double credit_balance = credit_info.getOpeningBalance();
                        System.out
                                .print("\nWhich account would you like to use to pay the bill ? \nOutstanding Credit : "
                                        + credit_balance);
                        System.out.print("\nAccount Information");
                        System.out.print("\n1 : Savings Account : " + savings_info.getOpeningBalance());
                        System.out.print("\n2 : Chequing Account : " + chequing_info.getOpeningBalance());
                        System.out.print("\nPlease choose an option : ");
                        user_input = scan.nextLine();
                        if (user_input.equals("1")) {
                            double opening_balance = customer_info.getSavingsAccountBalance();
                            savings_info.withdraw(Math.abs(credit_balance));
                            System.out.println("\nBill sucessfully paid!");
                            transactions = new Transaction("Credit Bill", savings_info.toString(), opening_balance,
                                    credit_balance, customer_info.getSavingsAccountBalance());
                            check = 1;
                            write_transaction(customer_info);
                        }
                        if (user_input.equals("2")) {
                            double opening_balance = customer_info.getChequingAccountBalance();
                            chequing_info.withdraw(Math.abs(credit_balance));
                            System.out.print("\nBill sucessfully paid!");
                            transactions = new Transaction("Credit Bill", chequing_info.toString(), opening_balance,
                                    credit_balance, customer_info.getChequingAccountBalance());
                            check = 1;
                            write_transaction(customer_info);
                        }
                        credit_info.paid();
                        transactions = new Transaction("Credit Bill Paid", credit_info.toString(), credit_balance,
                                Math.abs(credit_balance), customer_info.getCreditAccountBalance());
                    } else {
                        System.out.println("\nYou don't have a credit account issued to your name!");
                    }
                } else {
                    System.out.println("Please open a savings or chequings account!!");
                }
            } else if (user_input.equals("7")) {
                if (chequing_info.getOpeningBalance() == null || savings_info.getOpeningBalance() == null) {
                    System.out.println("\nYou don't have a chequings or savings account issued to your name!");
                } else {
                    System.out.print("\nAccount Information");
                    System.out.print("\nSavings Account : " + savings_info.getOpeningBalance());
                    System.out.print("\nChequing Account : " + chequing_info.getOpeningBalance());
                    System.out.print(
                            "\nWould you like to transfer funds from\n1.Saving-->Chequing\n2.Chequing-->Saving : ");
                    System.out.print("\nPlease choose an option : ");
                    user_input = scan.nextLine();
                    if (user_input.equals("1")) {
                        double saving_balance = customer_info.getSavingsAccountBalance();
                        double chequing_balance = customer_info.getChequingAccountBalance();
                        System.out.print("How much would you like to transfer ? ");
                        user_input = scan.nextLine();
                        savings_info.withdraw(Double.parseDouble(user_input));
                        transactions = new Transaction("Fund Transfer", savings_info.toString(), saving_balance,
                                Double.parseDouble(user_input), customer_info.getSavingsAccountBalance());
                        write_transaction(customer_info);
                        chequing_info.deposit(Double.parseDouble(user_input));
                        transactions = new Transaction("Fund Transfer", chequing_info.toString(), chequing_balance,
                                Double.parseDouble(user_input), customer_info.getChequingAccountBalance());
                        System.out.println("\nFund Transfer Sucessfull!");
                        check = 1;
                    }
                    if (user_input.equals("2")) {
                        double saving_balance = customer_info.getSavingsAccountBalance();
                        double chequing_balance = customer_info.getChequingAccountBalance();
                        System.out.print("How much would you like to transfer ? ");
                        user_input = scan.nextLine();
                        chequing_info.withdraw(Double.parseDouble(user_input));
                        transactions = new Transaction("Fund Transfer", chequing_info.toString(), chequing_balance,
                                Double.parseDouble(user_input), customer_info.getChequingAccountBalance());
                        write_transaction(customer_info);
                        savings_info.deposit(Double.parseDouble(user_input));
                        transactions = new Transaction("Fund Transfer", savings_info.toString(), saving_balance,
                                Double.parseDouble(user_input), customer_info.getSavingsAccountBalance());
                        System.out.println("\nFund Transfer Sucessfull!");
                        check = 1;
                    }
                }
            } else if (user_input.equals("8")) {
                if (age == 1) {
                    System.out.print(
                            "\nWhich account(s) would you like to open :\n1.Savings Account\n2.Chequing Account\n3.Credit Account\nPlease enter an option : ");
                    user_input = scan.nextLine();
                    if (user_input.equals("1") && savings_info.getOpeningBalance() == null) {
                        System.out.print("\nPlease enter the opening balance : ");
                        user_input = scan.nextLine();
                        savings_info.setOpeningBalance(Double.parseDouble(user_input));
                        System.out.println("\nSavings Account succesfully opened!");
                        transactions = new Transaction("Open an account", savings_info.toString(),
                                customer_info.getSavingsAccountBalance(), 0.0,
                                customer_info.getSavingsAccountBalance());
                        check = 1;
                    } else if (user_input.equals("2") && chequing_info.getOpeningBalance() == null) {
                        System.out.print("\nPlease enter the opening balance : ");
                        user_input = scan.nextLine();
                        chequing_info.setOpeningBalance(Double.parseDouble(user_input));
                        System.out.println("\nChequing Account succesfully opened!");
                        transactions = new Transaction("Open an account", chequing_info.toString(),
                                customer_info.getChequingAccountBalance(), 0.0,
                                customer_info.getChequingAccountBalance());
                        check = 1;
                    } else if (user_input.equals("3") && credit_info.getOpeningBalance() == null) {
                        credit_info.setOpeningBalance(0.0);
                        System.out.println("\nCredit Card Account succesfully opened!");
                        transactions = new Transaction("Open an account", credit_info.toString(),
                                customer_info.getCreditAccountBalance(), 0.0, customer_info.getCreditAccountBalance());
                        check = 1;
                    } else {
                        System.out.println("\nThis account has already been issued!");
                    }
                } else {
                    System.out.println("\nAs you are not of age you can't issue any more accounts!");
                }
            } else if (user_input.equals("9")) {
                String decision;
                System.out.print("\n(1).Delete Card");
                System.out.print("\n(2).Delete Acccount");
                System.out.print("\nSelect an option : ");
                user_input = scan.nextLine();
                if (user_input.equals("1")) {
                    System.out.print("\nAccount Information");
                    System.out.print("\n1 : Savings Account : " + savings_info.getOpeningBalance());
                    System.out.print("\n2 : Chequing Account : " + chequing_info.getOpeningBalance());
                    System.out.print("\n3 : Credit Account : " + credit_info.getOpeningBalance());
                    System.out.print("\nWhich card would you like to cancel ? ");
                    decision = scan.nextLine();
                    if (decision.equals("1") && savings_info.getOpeningBalance() != null) {
                        savings_info.setOpeningBalance(null);
                        System.out.println(" Savings Account successfully deleted!");
                        transactions = new Transaction("Delete an account", savings_info.toString(),
                                customer_info.getSavingsAccountBalance(), 0.0,
                                customer_info.getSavingsAccountBalance());
                        write_transaction(customer_info);
                    } else if (decision.equals("2") && chequing_info.getOpeningBalance() != null) {
                        chequing_info.setOpeningBalance(null);
                        System.out.println("Chequing Account successfully deleted!");
                        transactions = new Transaction("Delete an account", chequing_info.toString(),
                                customer_info.getSavingsAccountBalance(), 0.0,
                                customer_info.getChequingAccountBalance());
                        write_transaction(customer_info);
                    } else if (decision.equals("3")) {
                        if (credit_info.getOpeningBalance() < 0) {
                            System.out.println("Outstanding balance to be paid");
                        } else {
                            credit_info.setOpeningBalance(null);
                            System.out.println(" Credit Account successfully deleted!");
                            transactions = new Transaction("Delete an account", credit_info.toString(),
                                    customer_info.getSavingsAccountBalance(), 0.0,
                                    customer_info.getCreditAccountBalance());
                            write_transaction(customer_info);
                        }
                    } else {
                        System.out.print("The card you have selected has not been issued!");
                    }
                    if (savings_info.getOpeningBalance() == null && chequing_info.getOpeningBalance() == null
                            && credit_info.getOpeningBalance() == null) {
                        delete();
                        System.out.print("The customer has been deleted");
                        break;
                    }
                } else if (user_input.equals("2")) {
                    if (credit_info.getOpeningBalance() == null || credit_info.getOpeningBalance() == 0.0) {
                        delete();
                        System.out.print("The customer has been deleted");
                        break;
                    } else {
                        System.out.print("\nPlease pay the outstanding credit bill balance : "
                                + credit_info.getOpeningBalance());
                    }
                }
            } else {
                break;
            }
            if (check == 1) {
                write_transaction(customer_info);
            }
            update();
        } while (true);
    }

    public static void update() {
        try {
            // Savings Account
            Connection connection = DriverManager.getConnection(url);
            String sql = "UPDATE Savings_Account SET opening_balance = " + savings_info.getOpeningBalance()
                    + ", Date_ = ? WHERE Savings_account.id = " + savings_info.getId();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, getDate());
            statement.executeUpdate();
            // Chequing Account
            sql = "UPDATE Chequings_Account SET opening_balance = " + chequing_info.getOpeningBalance()
                    + ", Date_ = ? WHERE Chequings_Account.id = " + chequing_info.getId();
            statement = connection.prepareStatement(sql);
            statement.setDate(1, getDate());
            statement.executeUpdate();
            // Credit Account
            sql = "UPDATE Credit_Account SET opening_balance = " + credit_info.getOpeningBalance()
                    + ", Date_ = ? WHERE Credit_Account.id = " + credit_info.getId();
            statement = connection.prepareStatement(sql);
            statement.setDate(1, getDate());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Oopsie!");
            e.printStackTrace();
        }
    }

    public static void explore() {
        String user_input = "";
        do {
            System.out.println(
                    "\n\nPlease choose an action from the following:\n1: Rank Clients through net worth\n2: Average Money in bank\n3: Most Transactions \n4: Group Consumers By Account\n5: Group by Name\n- Back to Main Menu\n----------------------- ");
            System.out.print("\nProceed with an action : ");
            user_input = scan.nextLine();
            if (user_input.equals("2")) {
                double savings[] = average("Savings_Account");
                double chequings[] = average("Chequings_Account");
                double credit[] = average("Credit_Account");
                double avg = 0.0;
                for (int i = 0; i < savings.length; i++) {
                    avg += savings[i] + chequings[i] + credit[i];
                }
                avg /= savings.length;
                System.out.println("Average : " + avg);
            } else if (user_input.equals("1")) {
                double savings[] = average("Savings_Account");
                double chequings[] = average("Chequings_Account");
                double credit[] = average("Credit_Account");
                double avg[] = new double[savings.length];
                for (int i = 0; i < avg.length; i++) {
                    avg[i] = savings[i] + chequings[i] + credit[i];
                    avg[i] /= 3;
                }
                int[] key = search();
                String person[] = new String[key.length];
                try {
                    for (int i = 0; i < key.length; i++) {
                        Connection connection = DriverManager.getConnection(url);
                        String sql = "SELECT * FROM Customer WHERE savings_account = " + key[i];
                        PreparedStatement statement = connection.prepareStatement(sql);
                        ResultSet result = statement.executeQuery();
                        while (result.next()) {
                            person[i] = result.getString("sin_number");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < avg.length; i++) {
                    search_sins(Long.parseLong(person[i]));
                    System.out.println(customer_info.getName() + " : " + avg[i]);
                }
            } else if (user_input.equals("3")) {
                try {
                    Connection connection = DriverManager.getConnection(url);
                    String sql = "SELECT customer,Count(*) AS \"Count\" FROM Transactions GROUP BY customer HAVING COUNT(*) > 1 ORDER BY Count desc";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        search_sins(Long.parseLong(rs.getString("customer")));
                        System.out.println(customer_info.getName() + " : " + rs.getInt("Count"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (user_input.equals("5")) {
                System.out.print("Enter name of the customer : ");
                String name = scan.nextLine();
                try {
                    Connection conn = DriverManager.getConnection(url);
                    String sql = "SELECT Customer.first_name,Customer.last_name,Savings_Account.opening_balance AS \"Saving\",Chequings_Account.opening_balance AS \"Chequing\",Credit_Account.opening_balance AS \"Credit\" FROM Customer,Savings_Account,Chequings_Account,Credit_Account WHERE Customer.first_name = ? AND Customer.savings_account = Savings_Account.id AND Customer.chequings_account = chequings_account.id AND Customer.credit_account = credit_account.id";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, name);
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        System.out.println(result.getString("last_name") + "," + result.getString("first_name")
                                + " Savings : " + result.getString("Saving") + " Chequings : "
                                + result.getString("Chequing") + " Credit : " + result.getString("Credit"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (user_input.equals("4")) {
                System.out.println("1.Savings");
                System.out.println("2.Chequings");
                System.out.println("3.Credit");
                System.out.print("Select an option : ");
                user_input = scan.nextLine();
                if (user_input.equals("1")) {
                    conclude("Savings_Account");
                } else if (user_input.equals("2")) {
                    conclude("Chequings_Account");
                } else if (user_input.equals("3")) {
                    conclude("Credit_Account");
                }
            } else {
                break;
            }
        } while (true);

    }

    public static void conclude(String name) {
        int[] another = last_dance(name);
        try {
            System.out.println();
            for (int i = 0; i < another.length; i++) {
                Connection connection = DriverManager.getConnection(url);
                String sql = "SELECT * FROM Customer WHERE savings_account = " + another[i];
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    System.out.println(result.getString("last_name") + "," + result.getString("first_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] last_dance(String name) {
        int[] keys = new int[0];
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT id FROM " + name + " WHERE opening_balance >= 0";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            String read = "";
            while (result.next()) {
                result.getInt("id");
                if (result.wasNull()) {

                } else if (!result.wasNull()) {
                    read += result.getInt("id") + " ";
                }
            }
            String[] array = read.split(" ");
            keys = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                keys[i] = Integer.parseInt(array[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public static int[] search() {
        int[] key = new int[0];
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT id,AVG(opening_balance) AS \"Count\" FROM Savings_Account GROUP BY id ORDER BY Count desc";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            String read = "";
            while (result.next()) {
                read += result.getInt("id") + " ";
            }
            String[] array = read.split(" ");
            key = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                key[i] = Integer.parseInt(array[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static double[] average(String name) {
        double[] money = new double[0];
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT AVG(opening_balance) AS \"Average\" FROM " + name
                    + " GROUP BY id ORDER BY Average desc";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            String read = "";
            while (result.next()) {
                result.getDouble("Average");
                if (result.wasNull()) {
                    read += 0 + " ";
                } else if (!result.wasNull()) {
                    read += result.getDouble("Average") + " ";
                }
            }
            String[] array = read.split(" ");
            money = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                money[i] = Double.parseDouble(array[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }

    public static void main(String[] args) {
        int flag = -1;
        String user_input = "";
        do {
            System.out.println(
                    "\n\nWelcome to the VP bank.\n-----------------------\nPlease choose an action from the following:\n1: Add a customer \n2: Delete a customer\n3: Sort customers by last name, first name\n4: Sort customers by SIN\n5: Display customer summary (name, SIN)\n6: Find profile by last name, first name\n7: Find profile by SIN\n8: Explore\n- Quit\n----------------------- ");
            System.out.print("\nProceed with an action : ");
            user_input = scan.nextLine();
            if (user_input.equals("1")) {
                addCustomer();
            } else if (user_input.equals("2")) {
                System.out.print(
                        "Please enter the name (i.e : Doe,John) or 9-digit SIN (i.e : 696123910) \nof the customer you wish to delete : ");
                deleteCustomer();
            } else if (user_input.equals("3")) {
                flag = 0;
                System.out.println("\nSuccessfully Sorted!");
            } else if (user_input.equals("4")) {
                flag = 1;
                System.out.println("\nSuccessfully Sorted!");
            } else if (user_input.equals("5")) {
                if (flag == 0) {
                    sort_name();
                } else if (flag == 1) {
                    sort_num();
                } else {
                    display();
                }
                flag = -1;
            } else if (user_input.equals("6")) {
                System.out.print("Please enter the name of the user (i.e : Doe,John) : ");
                String name = scan.nextLine();
                while (!name.contains(",")) {
                    System.out.print("Please seperate your first and last name with a comma : ");
                    name = scan.nextLine();
                }
                int validate = search_names(name);
                if (validate == -1) {
                    System.out.println("\nThe customer is not present in the database");
                } else if (validate == 0) {
                    account();
                } else {
                    System.out.print("\nThere are " + validate + " people with the name : " + name
                            + "\nTo confirm, please enter the SIN number of the customer you wish to delete : ");
                    user_input = scan.nextLine();
                    boolean check = isNumber(user_input);
                    while (!check) {
                        System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                        user_input = scan.nextLine();
                        check = isNumber(user_input);
                    }
                    int present = present(user_input);
                    while (present != 0) {
                        System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                        user_input = scan.nextLine();
                        check = isNumber(user_input);
                        while (!check) {
                            System.out.print("Please enter the 9-digit sin number associated with " + name + " : ");
                            user_input = scan.nextLine();
                            check = isNumber(user_input);
                        }
                        present = present(user_input);
                    }
                    account();
                }
            } else if (user_input.equals("7")) {
                System.out.print("Please enter the 9-digit SIN of the user (i.e : 123456789) : ");
                user_input = scan.nextLine();
                boolean validate = isNumber(user_input);
                while (!validate) {
                    System.out.print("Please enter a 9-digit number : ");
                    user_input = scan.nextLine();
                    validate = isNumber(user_input);
                }
                boolean present = search_sins(Long.parseLong(user_input));
                while (!present) {
                    System.out.print("Customer is not present in the database.\nPlease try again : ");
                    user_input = scan.nextLine();
                    validate = isNumber(user_input);
                    while (!validate) {
                        System.out.print("Please enter a 9-digit number : ");
                        user_input = scan.nextLine();
                        validate = isNumber(user_input);
                    }
                    present = search_sins(Long.parseLong(user_input));
                }
                account();
            } else if (user_input.equals("8")) {
                explore();
            } else {
                break;
            }
        } while (true);
        System.out.print("Thanks for using VP Bank!");
        scan.close();
    }
}