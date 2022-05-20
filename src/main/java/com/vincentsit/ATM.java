package com.vincentsit;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

public class ATM {
    public String location;
    public String currency;
    private List<JSONObject> users;
    public boolean login = false;

    public ATM(String location, String currency, List<JSONObject> users) {
        this.location = location;
        this.currency = currency;
        this.users = users;
    }

    public String getLocation() {
        return location;
    }

    public String getCurrency() {
        return currency;
    }

    public List<JSONObject> getUsers() {
        return users;
    }

    public boolean getLogin() {
        return login;
    }

    public String getRegisteredUsers() {
        StringBuilder userDetails = new StringBuilder();
        for (int i=0; i<users.size(); i++) {
            JSONObject particularUser = users.get(i);
            String userName = particularUser.get("name").toString();
            String bankAccountNumber = particularUser.get("bankAccountNumber").toString();
            userDetails.append("\nName: " + userName + "\nAccount Number: " + bankAccountNumber + "\n");
        }
        return userDetails.toString();
    }

    public int checkIfUserExists(String accountNumber) {
        // get all usernames into a list
        int index;
        for (int i=0; i<users.size(); i++) {
            String storedAccountNumber = users.get(i).get("bankAccountNumber").toString();
            if (storedAccountNumber.equals(accountNumber)) {
                index = i;
                return index;
            }
        }
        return -1;
    }

    public boolean checkUserCode(String accountNumber, String pincodeEntry) {
        return checkIfUserExists(accountNumber) >= 0 && pincodeEntry == users.get(checkIfUserExists(accountNumber)).get("bankPin");
    }

    public String getUserName(int index) {
        return users.get(index).get("name").toString();
    }

    // get original user, update the cash amount and overwrite the current json
    // two step process:
    // step 1 - update JSONObject with put method
    // Step 2 - write it back to the file
    // when do we update the file? At the end of the session?
    // Should a user profile be created and closed when the session finishes?
    // User is created, get the amount of money, updates that amount of money
    // save the update into a JSONObject, replace the JSONObject with this
    // write to file

    // public double updateBankAmount(users) {

    //     return 
    // }
    
    @Override
    public String toString() {
        return location + " branch ATM";
    }
}
