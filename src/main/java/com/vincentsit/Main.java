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
        // System.out.println("Do you have an existing account? Y/N?");
        // boolean hasAccount = false;

        // while (!hasAccount) {

        // }
        // switch(scan.nextLine().toLowerCase()) {
        //     case "y":

        //         break;
        //     case "n":
        //         break;
        //     default:
        //         break;
        // }
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

        System.out.println("Please enter your pincode: ");
        int pincodeTries = 5;
        
        String bankPinEntry;

        boolean verified = false;

        // TODO: fix this logic here
        while (pincodeTries > 0 && !verified) {
            // if (pincodeTries == 5) {
            //     System.out.println("\nPlease enter your pincode:");
            // } else {
            //     System.out.println("\nPlease try again. You have " + pincodeTries + " tries left.");
            // }
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
                    // System.out.println("Your bank account has " + vincent.getBankAmount());
                    break;
                case 2:
                    System.out.println("How much would you like to withdraw?");
                    System.out.println("Your bank account has $" + machine.updateBankAmount(recordNumber, "withdraw", scan.nextInt()));
                    break;
                case 3:
                    System.out.println("How much would you like to deposit?");
                    System.out.println("Your bank account has $" + machine.updateBankAmount(recordNumber, "deposit", scan.nextInt()));
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