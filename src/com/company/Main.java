package com.company;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> input;
        //input = readingInputFromConsole();
        input = readingInputFromConsoleScanner();

        int I = IFromInput(input);
        int J = JFromInput(input);
        int L = LFromInput(input);
        double beta = BetaFromInput(input);
        double alphaU = 1;
        double alphaV = 1;

        ArrayList<List<Double>> H2dl = heightmapFromInput(input, I, J);

        RealMatrix H = fromMatrixToMatrix(H2dl, I, J);
        RealMatrix U = generateUV(I, L, alphaU);
        RealMatrix V = generateUV(J, L, alphaV);

        ArrayList<RealMatrix> UMatrixList = new ArrayList<>();
        ArrayList<RealMatrix> VMatrixList = new ArrayList<>();
        for(int i = 0; i < 25; i++) {
            U = update("U", I, J, L, V, H, alphaU, beta);
            V = update("V", I, J, L, U, H, alphaV, beta);
            if(i > 10) {
                UMatrixList.add(U);
                VMatrixList.add(V);
            }
        }
        RealMatrix finalU = MatrixUtils.createRealMatrix(L, I);
        for(int i = 0; i < I; i++) {
            finalU.setColumn(i, new ArrayRealVector(L, 0.0).getDataRef());
        }
        RealMatrix finalV = MatrixUtils.createRealMatrix(L, J);
        for(int i = 0; i < I; i++) {
            finalU.setColumn(i, new ArrayRealVector(L, 0.0).getDataRef());
        }
        for(int i = 0; i < 14; i++) {
            finalU = finalU.add(UMatrixList.get(i));
            finalV = finalV.add(VMatrixList.get(i));
        }
        finalU.scalarMultiply(1 / 15);
        finalV.scalarMultiply(1 / 15);

        writeOut(finalU.transpose());
        System.out.println();
        writeOut(finalV.transpose());


        /*
5,6,2,100.0
0.20,0.50,2.51,1.12,6.98,4.43
1.08,0.20,1.87,1.31,1.44,3.96
0.67,4.29,4.48,5.68,6.03,3.49
3.93,1.64,1.84,3.32,2.08,4.63
4.89,7.30,3.20,2.38,3.04,0.73

         */

    }

    public static void writeOut(RealMatrix M) {
        double[][] matrix = M.getData();


        for(int i = 0; i < M.getRowDimension(); i++) {
            for(int j = 0; j < M.getColumnDimension(); j++) {
                if(j != M.getColumnDimension()-1)
                    System.out.print(matrix[i][j] + ",");
            }
            System.out.print(matrix[i][M.getColumnDimension() - 1]);
            System.out.println();
        }

    }

    public static ArrayList<String[]> readingInputFromConsole() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String[]> inputstr = new ArrayList<String[]>();
        String line;

        line = br.readLine();
        inputstr.add(line.split(","));

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

    public static ArrayList<String[]> readingInputFromConsoleScanner() throws IOException {
        ArrayList<String[]> input = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String line;
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            input.add(line.split(","));
        }

        return input;
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

    public static RealMatrix fromMatrixToMatrix(ArrayList<List<Double>> input, int I, int J) {
        RealMatrix ret = MatrixUtils.createRealMatrix(I, J);
        for(int i = 0; i < I; i++) {
            for(int j = 0; j < J; j++) {
                ret.setEntry(i, j, input.get(i).get(j));
            }
        }
        return ret;
    }

    public static RealMatrix generateUV(int size, int L, double alpha) {
        RealMatrix ret = MatrixUtils.createRealMatrix(size, L);

        ArrayRealVector nullVector = new ArrayRealVector(L, 0.0);

        RealMatrix rm = MatrixUtils.createRealIdentityMatrix(L);
        RealMatrix alphaMatrix = MatrixUtils.createRealMatrix(rm.getData());
        alphaMatrix = alphaMatrix.scalarMultiply( 1 / alpha);

        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(nullVector.getDataRef(), alphaMatrix.getData());

        for(int i = 0; i < size; i++) {
            double[] sample = mnd.sample();
            for(int j = 0; j < L; j++) {
                ret.setEntry(i, j, sample[j]);
            }
        }

        return MatrixUtils.createRealMatrix(ret.transpose().getData());
    }

    public static RealMatrix update(String matrix, int argI, int argJ, int L, RealMatrix V, RealMatrix H, double alphaU, double beta ) {
        int I = 0;
        int J = 0;
        if(matrix.equals("U")) {
            I = argI;
            J = argJ;
        } else if(matrix.equals("V")) {
            I = argJ;
            J = argI;
        }

        RealMatrix ret = MatrixUtils.createRealMatrix(L, I);
        ArrayList<RealMatrix> lambdaList = new ArrayList<>();
        ArrayList<ArrayRealVector> psiList = new ArrayList<>();

        for(int i = 0; i < I; i++) {
            lambdaList.add(generateLambdaList(J, L, V, alphaU, beta));
            psiList.add(generatePsiList(J, L, lambdaList.get(i), H, V, beta, i, matrix));
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(psiList.get(i).getDataRef(), lambdaList.get(i).getData());
            ret.setColumn(i, mnd.sample());
        }

        return ret;
    }

    public static RealMatrix generateLambdaList(int J, int L, RealMatrix V, double alphaU, double beta) {
        RealMatrix rm = MatrixUtils.createRealMatrix(L, L);
        for(int i = 0; i < L; i++) {
            rm.setColumn(i, new ArrayRealVector(L, 0.0).getDataRef());
        }
        RealMatrix vj = MatrixUtils.createRealMatrix(L, 1);
        RealMatrix vjT = MatrixUtils.createRealMatrix(1, L);
        for(int i = 0; i < J; i++) {
            vj.setColumn(0,V.getColumn(i));
            vjT.setRow(0,V.getColumn(i));
            RealMatrix temp = MatrixUtils.createRealMatrix(vj.multiply(vjT).getData());
            rm = rm.add(temp);
        }
        rm = rm.scalarMultiply(beta);
        RealMatrix alphaUIdentityMatrix = MatrixUtils.createRealIdentityMatrix(L);
        alphaUIdentityMatrix = alphaUIdentityMatrix.scalarMultiply(alphaU);
        rm = rm.add(alphaUIdentityMatrix);
        return rm;
    }

    private static ArrayRealVector generatePsiList(int J, int L, RealMatrix lambda, RealMatrix H, RealMatrix V, double beta, int i, String matrix) {
        ArrayRealVector ret;

        ArrayRealVector arv = new ArrayRealVector(L, 0.0);
        for(int j = 0; j < J; j++) {
            if(matrix.equals("U"))
                arv = arv.add(V.getColumnVector(j).mapMultiply(H.getColumn(j)[i]));
            if(matrix.equals("V"))
                arv = arv.add(V.getColumnVector(j).mapMultiply(H.getColumn(i)[j]));
        }
        RealMatrix lambdaInverse = MatrixUtils.inverse(lambda);
        ret = new ArrayRealVector(lambdaInverse.preMultiply(arv.mapMultiplyToSelf(beta)));
        return ret;
    }
}
