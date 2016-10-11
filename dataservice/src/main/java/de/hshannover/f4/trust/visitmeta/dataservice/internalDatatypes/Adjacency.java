package de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes;

import de.hshannover.f4.trust.visitmeta.dataservice.rest.RestService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created by tietze on 04.10.16.
 */
public class Adjacency {
    private static final Logger log = Logger.getLogger(Adjacency.class);
    private final int STARTSIZE = 10;

    private HashMap<Identifier, Integer> index = new HashMap<>();
    private double[][] matrix = new double[STARTSIZE][STARTSIZE];
    private int insertPoint = 0;
    private int maxValues = STARTSIZE;

    public void addNeighbors(Identifier from, Identifier to){
        if(! index.containsKey(from)) {
            addIntoMap(from);
        }
        if(! index.containsKey(to) ){
            addIntoMap(to);
        }
        addIntoMatrixSymmetric(index.get(from),index.get(to));

    }

    private void addIntoMap(Identifier ident) {
        this.index.put(ident, insertPoint);
        this.insertPoint++;
    }

    private void resizeMatrix(int value){
        System.out.println("before resize");
        double[][] newMatrix = new double[this.matrix.length + value][this.matrix[0].length + value];
        for (int i = 0; i < this.matrix.length; i++){
            for (int j = 0; j < this.matrix[0].length; j++){
                newMatrix[i][j] = this.matrix[i][j];
            }
        }
        this.matrix = newMatrix;
        maxValues = maxValues + value;
        System.out.println("before resize");
    }

    private void addIntoMatrixSymmetric(int x, int y){
        if( insertPoint >= maxValues) {
            resizeMatrix(STARTSIZE);
        }
        this.matrix[x][y] = 1.0;
        this.matrix[y][x] = 1.0;
    }

    public double[][] getMatrix(){
        return this.matrix;
    }
}
