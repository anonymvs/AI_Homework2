package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> input;
        input = readingInputFromConsole();

        int I = IFromInput(input);
        int J = JFromInput(input);
        int L = LFromInput(input);
        double Beta = BetaFromInput(input);

        ArrayList<List<Double>> H = heightmapFromInput(input, I, J);

        System.out.println(H);

        /*
5,6,2,100.0
0.20,0.50,2.51,1.12,6.98,4.43
1.08,0.20,1.87,1.31,1.44,3.96
0.67,4.29,4.48,5.68,6.03,3.49
3.93,1.64,1.84,3.32,2.08,4.63
4.89,7.30,3.20,2.38,3.04,0.73

         */

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

    public static int IFromInput(ArrayList<String[]> input) {
        return Integer.parseInt(input.get(0)[0]);
    }

    public static int JFromInput(ArrayList<String[]> input) {
        return Integer.parseInt(input.get(0)[1]);
    }

    public static int LFromInput(ArrayList<String[]> input) {
        return Integer.parseInt(input.get(0)[2]);
    }

    public static double BetaFromInput(ArrayList<String[]> input) {
        return Double.parseDouble(input.get(0)[3]);
    }

    public static ArrayList<List<Double>> heightmapFromInput(ArrayList<String[]> input, int I, int J) {
        ArrayList<List<Double>> ret = new ArrayList<>();
        for(int i = 0; i < I; i++) {
            List<Double> row = new ArrayList<>();
            for(int j = 0; j < J; j++) {
                row.add(Double.parseDouble(input.get(i+1)[j]));
            }
            ret.add(row);
        }
        return ret;
    }
}
