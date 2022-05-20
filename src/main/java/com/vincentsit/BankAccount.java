package com.vincentsit;

public class BankAccount {
    public String name;
    private double bankAmount;
    public int userID;

    public BankAccount(String name, double bankAmount, int userID) {
        this.name = name;
        this.bankAmount = bankAmount;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public double getBankAmount() {
        return bankAmount;
    }

    public int getUserID() {
        return userID;
    }

    public double withdraw(double amountToWithdraw) {
        bankAmount -= amountToWithdraw;
        return bankAmount;
    }

    public double deposit(double amountToDeposit) {
        bankAmount += amountToDeposit;
        return bankAmount;
    }
}
