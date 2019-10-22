import java.lang.*;

public class Matrix {
	double[][] M;
	double det;
	
	public Matrix(int row, int col) {
		M = new double[row][col];
		det = 0.0;
	}
	
	public double[][] plus(double[][] N){
		if ((M.length == N.length) && (M[0].length == N[0].length)) {
			double[][] tmp = new double[M.length][M[0].length];
			for (int i=0; i<M.length; i++) {
				for (int j=0; j<M[0].length; j++) {
					tmp[i][j] = M[i][j] + N[i][j];
				}
			}
			return tmp;
		} else {
			System.out.println("Error in matrix addition");
			return null;
		}
	}
	
	public double[][] minus(double[][] N) {
		if((M.length == N.length) && (M[0].length == N[0].length)) {
			double[][] tmp = new double[M.length][M[0].length];
			for (int i=0; i<M.length; i++) {
				for (int j=0; j<M[0].length; j++) {
					tmp[i][j] = M[i][j] - N[i][j];
				}
			}
			return tmp;
		} else {
			System.out.println("Error in matrix substraction");
			return null;
		}
	}
	
	public double[][] times(double[][] N){
		if(M.length == N.length) {
			double[][] tmp = new double[M.length][N[0].length];
			for (int i=0; i<M.length; i++) {
				for (int j=0; j<N[0].length; j++) {
					tmp[i][j] = 0.0;
					for (int k=0; k<M[0].length; k++) {
						tmp[i][j] += M[i][k] * N[k][j];
					}
				}
			}
			return tmp;
		} else {
			System.out.println("Error in matrix multiplication");
			return null;
		}
	}
	
	public double[][] transpose() {
		double[][] tmp = new double[M[0].length][M.length];
		for (int i=0; i<M[0].length; i++) {
			for (int j=0; j<M.length; j++) {
				tmp[i][j] = M[j][i];
			}
		}
		return tmp;
	}
}
