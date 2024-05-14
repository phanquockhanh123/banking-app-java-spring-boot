package com.example.bankingapp.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNTS_EXSITS_CODE = "001";
    public static final String ACCOUNTS_EXSITS_MESSAGE = "This user already has an account created";
    public static final String ACCOUNTS_CREATION_SUCCESS = "002";
    public static final String ACCOUNTS_CREATION_MESSAGE = "Account has been created";
    public static final String ACCOUNTS_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNTS_NOT_EXISTS_CODE_MESSAGE = "Account not exists code";
    public static final String ACCOUNTS_FOUND_CODE = "004";
    public static final String ACCOUNTS_FOUND_SUCCESS = "User account found";

    public static final String ACCOUNTS_CREDIT_SUCCESS = "005";
    public static final String ACCOUNTS_CREDIT_MESSAGE = "Account credit success";

    public static final String ACCOUNTS_DEBIT_SUCCESS = "006";
    public static final String ACCOUNTS_DEBIT_MESSAGE = "Account debit success";

    public static final String ACCOUNTS_COMPARE_VALUE = "007";
    public static final String ACCOUNTS_COMPARE_MESSAGE = "Account balance not many";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful!";

    public static String generateAccountNumber() {
        /**
         * 2023 + randomSixDigits
         */
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        // generate random number into 100000 and 999999
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert year current and random number to string

        String year = String.valueOf(currentYear);

        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }

}
