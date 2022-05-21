package com.vincentsit;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        String fileName = "storage.json";
        JSONParser jsonParser = new JSONParser();

        ATM machine = null;
        JSONObject body = null;

        try (FileReader data = new FileReader(fileName)) {
            Object obj = jsonParser.parse(data);
            body = (JSONObject) obj;
            // System.out.println(body.toString());
            machine = initaliseATM((body));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
       
        // database design in the future - each record ID would have an associate name, bank account etc.
        // for now, it will be manually created - but expecting to create object from database (next step)

        Scanner scan = new Scanner(System.in);
        System.out.println("Do you have an existing account? Y/N?");

        String reply = scan.nextLine();

        if (reply.toLowerCase().equals("n")) {
            System.out.println("Would you like to make an account? Y/N");
            String accountMaking = scan.nextLine();
            if (accountMaking.toLowerCase().equals("y")) {
                System.out.println("Please enter your name:");
                String newUsername = scan.nextLine();
                System.out.println("What do you want for a bank account number?");
                String newBankAccountNumber = scan.nextLine();
                while (machine.checkIfUserExists(newBankAccountNumber) >= 0) {
                    System.out.println("There is an existing user with that bank account number, please try another one.");
                    newBankAccountNumber = scan.nextLine();
                }
                System.out.println("What will be your pin?");
                String newBankPin = scan.nextLine();
                try {
                    machine.addNewUser(newUsername, newBankAccountNumber, newBankPin);
                    System.out.println("Thank you. Your account has been created. Please login with your bank account number.");
                    // System.out.println(machine.getUsers().toString());
                    body.put("users", machine.getUsers());
                    writeToFile(body, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Please come back when you have an account.");
                scan.close();
                return;
            }
        }

        System.out.println("Please enter your bank number: ");
        String bankNumberEntry = scan.nextLine();
        
        int recordNumber = machine.checkIfUserExists(bankNumberEntry);

        if (recordNumber < 0) {
            // if it doesn't match - end the program
            System.out.println("No matching bank number found.");
            scan.close();
            return;
        }
        System.out.println("Found matching bank number.\nWelcome " + machine.getUserName(recordNumber));

        int pincodeTries = 5;
        
        String bankPinEntry;

        boolean verified = false;

        while (pincodeTries > 0 && !verified) {
            if (pincodeTries == 5) {
                System.out.println("\nPlease enter your pincode:");
            } else {
                System.out.println("\nPlease try again. You have " + pincodeTries + " tries left.");
            }
            bankPinEntry = scan.nextLine();
            pincodeTries--;
            if (machine.checkUserCode(recordNumber, bankPinEntry)) {
                verified = true;
            }
        }

        if (!verified) {
            System.out.println("You did not enter the right information. Your account has been locked.");
            scan.close();
            return;
        }
        
        boolean quit = false;

        while(!quit) {
            System.out.println("Welcome. What would you like to do today? Press 1 to check balance, 2 to withdraw, 3 to deposit, and 4 to quit");
            int choice = scan.nextInt();    
            switch(choice) {
                case 1:
                    System.out.println("Your bank account has " + machine.checkUserBankBalance(recordNumber));
                    break;
                case 2:
                    System.out.println("How much would you like to withdraw?");
                    System.out.println("Your bank account has $" + machine.updateBankAmount(recordNumber, "withdraw", scan.nextDouble()));
                    break;
                case 3:
                    System.out.println("How much would you like to deposit?");
                    System.out.println("Your bank account has $" + machine.updateBankAmount(recordNumber, "deposit", scan.nextDouble()));
                    break;
                case 4:
                    quit = true;
                    System.out.println("Thank you for using our banking service. We hope to see you next time.");
                    break;
                default:
                    break;
            }
        }
        scan.close();

        writeToFile(body, fileName);
    }
    
    public static ATM initaliseATM(JSONObject data) {
        JSONObject atmData = (JSONObject) data.get("atmInfo");
        JSONArray userData = (JSONArray) data.get("users");
        // System.out.println(userData.get(0).getClass().getSimpleName());
        List<JSONObject> users = new ArrayList<JSONObject>();
        String location = (String) atmData.get("location");
        String currency = (String) atmData.get("currency");

        for (int i=0; i< userData.size(); i++) {
            users.add((JSONObject) userData.get(i));
        }
        
        return new ATM(location, currency, users);
    }
    
    public static void writeToFile(JSONObject body, String fileName) {
        try (FileWriter newData = new FileWriter(fileName)) {
            BufferedWriter out = new BufferedWriter(newData);
            out.write(body.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}