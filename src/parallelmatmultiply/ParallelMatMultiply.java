/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parallelmatmultiply;

/**
 *
 * @author Muhammad Husain F
 */
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ParallelMatMultiply {
    public static int[][] A;
    public static int[][] B;
    public static int[][] C;
    public static int N;
    public static threadForMultiplyingMatrix[] threads;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {

        System.out.println("Masukkan jumlah kolom dan baris : ");
        Scanner s = new Scanner(System.in);
        N = s.nextInt();
        System.out.println("Masukkan nilai matriks untuk A : \n");
        A = getMatrix();
        System.out.println("Masukkan nilai matriks untuk B : \n");
        B = getMatrix();
        C = new int [N][N];
        threads = new threadForMultiplyingMatrix[N];
        long t1 = System.currentTimeMillis();
        //untuk parallel
        Step1();
        Step2();

        //nonParallelMultiply(); //untuk non parallel
        long t2 = System.currentTimeMillis();

        printMatrix(C);
        System.out.println("\nThe time is "+(t2-t1));


   }
    public static int[][] getMatrix(){

        Random rand = new Random();
        Scanner s = new Scanner(System.in);
        int mat[][] = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mat[i][j] = rand.nextInt(10) + 1;
            }
        }
        return mat;
    }
     public static void printMatrix(int matrix[][]){

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] +"    ");
            }
            System.out.print("\n");
        }
    }
     public static void nonParallelMultiply()
     {
         int sum = 0;
            for ( int i = 0 ; i < N ; i++ )
            {
              for ( int j = 0 ; j < N ; j++ )
              {
                for ( int k = 0 ; k < N ; k++ )
                {
                  sum = sum + A[i][k]*B[k][j];
                }

                C[i][j] = sum;
                sum = 0;
              }
            }
     }


    public static class threadForMultiplyingMatrix extends Thread {

        int upperBoundary;
        int bottomBoundary;
        int leftBoundary;
        int rightBoundary;

        public threadForMultiplyingMatrix(int upperBoundary, int bottomBoundary, int leftBoundary, int rightBoundary){
            this.upperBoundary = upperBoundary;
            this.bottomBoundary = bottomBoundary;
            this.leftBoundary = leftBoundary;
            this.rightBoundary = rightBoundary;
        }

        private threadForMultiplyingMatrix() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void run() {

            for (int i = upperBoundary; i <= bottomBoundary; i++) {
                for (int j = leftBoundary; j <= rightBoundary; j++) {
                    for (int k = 0; k <= Math.sqrt(N)-1; k++) {
                        C[i][j] = C[i][j] + A[i][leftBoundary+k] * B[upperBoundary+k][j];
                    }
                }
            }
        }
    }

    public static void multiplyMatrix() throws Exception{
        int count = 0 ;
        for( int i = 0; i < Math.sqrt(N)*Math.sqrt(N)-1 ; i+=Math.sqrt(N))
        {
            for( int j = 0; j < Math.sqrt(N)*Math.sqrt(N)-1 ; j+=Math.sqrt(N))
            {
                threadForMultiplyingMatrix thread = new threadForMultiplyingMatrix(i,(int)(i+Math.sqrt(N)-1),j,(int)(j+Math.sqrt(N)-1));
                threads[count] = thread;
                thread.start();

                count++;
            }

        }

        for (threadForMultiplyingMatrix thread : threads) {
            thread.join();
        }

    }

        public static void Step1() throws Exception{

            for(int i = 1; i < Math.sqrt(N) ;i++)
            {

                for(int j = (int)Math.sqrt(N)*i;j<=(Math.sqrt(N)*(i+1))-1;j++)
                {
                    swipeLeft(A,j,i);
                    swipeTop(B,j,i);
                }
            }
            multiplyMatrix();

        }

    public static void Step2() throws Exception{

        for(int i=0;i<Math.sqrt(N)-1;i++){
            for(int j=0;j<Math.sqrt(N)*Math.sqrt(N);j++)
            {
                swipeLeft(A,j,1);
                swipeTop(B,j,1);
            }
            multiplyMatrix();

        }
    }

    public static void swipeLeft(int[][] matrix, int rows, int numberOfSwipes){
        int[] temp1 = new int[(int)(Math.sqrt(N)*Math.sqrt(N))];
        int temp2;

        for(int i=0; i<Math.sqrt(N)*numberOfSwipes; i++){
            System.arraycopy(matrix[rows], 0, temp1, 0, (int)(Math.sqrt(N)*Math.sqrt(N)));
            for(int j=0; j<(Math.sqrt(N)*Math.sqrt(N))-1; j++){
                temp2 = temp1[j];
                temp1[j] = temp1[j+1];
                temp1[j+1] = temp2;
            }
            System.arraycopy(temp1, 0, matrix[rows], 0, (int)(Math.sqrt(N)*Math.sqrt(N)));
        }
    }

    public static void swipeTop(int[][] matrix, int column, int numberOfSwipes){
        int[] temp1 = new int[(int)(Math.sqrt(N)*Math.sqrt(N))];
        int temp2;

        for(int i=0; i<Math.sqrt(N)*numberOfSwipes; i++){
            for(int k=0;k<(int)(Math.sqrt(N)*Math.sqrt(N));k++){
                temp1[k] = matrix[k][column];
            }
            for(int j=0; j<(int)(Math.sqrt(N)*Math.sqrt(N))-1; j++){
                temp2 = temp1[j];
                temp1[j] = temp1[j+1];
                temp1[j+1] = temp2;
            }
            for(int k=0;k<(int)(Math.sqrt(N)*Math.sqrt(N));k++){
                matrix[k][column] = temp1[k];
            }
        }
    }
}