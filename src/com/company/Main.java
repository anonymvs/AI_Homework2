package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> inputStringArray;
        inputStringArray = readingInputFromConsole();

        System.out.print("dfasfds");
    }

    public static ArrayList<String[]> readingInputFromConsole() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String[]> inputstr = new ArrayList<String[]>();
        String line;
        int n = 0;

        line = br.readLine();
        inputstr.add(n, line.split(","));

        boolean b = true;
        while(b) {

            if(line.isEmpty()) {
                b = false;
            } else {
                line = br.readLine();
                inputstr.add(line.split(","));
            }
        }
        return inputstr;
    }
}
