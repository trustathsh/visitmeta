package de.hshannover.f4.trust.visitmeta.graphCalculator;


import Jama.Matrix;
import com.jujutsu.tsne.*;
import org.apache.log4j.Logger;

import java.util.Random;

public class ComputeMDSImpl implements ComputeMDS {

    private static final Logger LOGGER = Logger.getLogger(ComputeMDSImpl.class);

    private static double perplexity = 5.0;
    private int iters = 500;

    public double[][] computeMDS(double[][] inputMatrix) {
        //LOGGER.trace(MatrixOps.doubleArrayToPrintString(inputMatrix, ", ", 50,10));

        double[][] centerMatrix = MatrixOps.centerAndScale(inputMatrix);
        //LOGGER.trace(MatrixOps.doubleArrayToPrintString(centerMatrix, ", ", 50,10));

        //double[][] eigenvec = E.getV().getArray();


        TSne tsne = new FastTSne();
        double[][] Y = tsne.tsne(inputMatrix, 2, inputMatrix[0].length, perplexity, iters);
        //double[][] Y = tsne.tsne(generateRandomMatrix(inputMatrix.length, inputMatrix[0].length), 2, inputMatrix.length, perplexity, iters);
        LOGGER.trace(MatrixOps.doubleArrayToPrintString(Y, ", ", 50, 10));


        return Y;
    }

    private double[][] generateRandomMatrix(int dimensionX, int dimensionY){
        double[][] matrix = new double[dimensionX][dimensionY];
        Random rand = new Random();
        for (int i = 0; i < dimensionY; i++) {
            for (int j = 0; j < dimensionX; j++) {
                if(rand.nextBoolean()) matrix[j][i] = 1.0;
                else matrix[j][i] = 0.0;
            }
        }
        return matrix;
    }
}