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

public class test {
    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser();
        JSONObject body = null;

        try (FileReader data = new FileReader("storage for testing.json")) {
            Object obj = jsonParser.parse(data);
            body = (JSONObject) obj;
            // System.out.println(body.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(body.get("atmInfo").getClass().getSimpleName());
        JSONObject test = (JSONObject) body.get("atmInfo");
        test.put("location", "Christchurch");
        System.out.println(body);
        
        try (FileWriter newData = new FileWriter("storage for testing.json")) {
            BufferedWriter out = new BufferedWriter(newData);
            out.write(body.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
