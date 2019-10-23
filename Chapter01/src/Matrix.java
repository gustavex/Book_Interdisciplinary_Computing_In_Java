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
	
	public double[][] ret_inv() throws MyMatrixExceptions {
		double[][] save = new double[M.length][M[0].length];
		double[][] tmp;
		
		for (int i=0; i<M.length; i++)
			for (int j=0; j<M[0].length; j++)
				save[i][j] = M[i][j];
		
		inverse();
		tmp = M;
		M = save;
		return tmp;
	}
	
	public void inverse() throws MyMatrixExceptions {
		int i, j, k;
		int iext=0, jext=0, itemp, jtemp;
		int nmax = M.length;
		int[] ir = new int[nmax];
		int[] ic = new int[nmax];
		double aext, atemp, de;
		
		de=1.0;
		for (j=0;j<nmax;j++) {
			ic[j] = j;
			ir[j] = j;
		}
		for (k=0;k<nmax;k++) {
			aext=0.0;
			for (i=k;i<nmax;i++) 
				for (j=k;j<nmax;j++)
					if (aext<Math.abs(M[i][j])) {
						iext=i;
						jext=j;
						aext=Math.abs(M[i][j]);
					}
			if (aext<0.0)
			throw new MyMatrixExceptions("Error in matrix inversion 1");
			if (k!=iext) {
				de = -de;
				for (j=0;j<nmax;j++) {
					atemp=M[k][j];
					M[k][j]=M[iext][j];
					M[iext][j]=atemp;
				}
				itemp=ic[k];
				ic[k]=ic[iext];
				ic[iext]=itemp;
			}
			if (k!=jext) {
				de = -de;
				for (i=0;i<nmax;i++) {
					atemp=M[i][k];
					M[i][k]=M[i][jext];
					M[i][jext]=atemp;
				}
				itemp=ir[k];
				ir[k]=ir[jext];
				ir[jext]=itemp;
			}
			aext=M[k][k];
			de*=aext;
			M[k][k]=1.0;
			for (j=0;j<nmax;j++) M[k][j]/=aext;
			for (i=0;i<nmax;i++)
				if (k!=i) {
					aext=M[i][k];
					if (aext!=0.0) {
						M[i][k]=0.0;
						for (j=0;j<nmax;j++) M[i][j]-=aext*M[k][j];
					}
				}
			}
			int idim = nmax-1;
			for (k=0;k<idim;k++) {
				int kk=k+1;
				if(k!=ic[k]) {
					for (i=kk;i<nmax;i++) if (k==ic[i]) break;
					if (i == nmax) throw new MyMatrixExceptions("Error in matrix inversion 2");
					for (j=0;j<nmax;j++) {
						atemp=M[j][k];
						M[j][k]=M[j][i];
						M[j][i]=atemp;
					}
					itemp=ic[i];
					ic[i]=ic[k];
					ic[k]=itemp;
				}
				if(k!=ir[k]) {
					for(j=kk;j<nmax;j++) if (k==ir[j]) break;
					if (j == nmax) throw new MyMatrixExceptions("Error in matrix inversion 3");
					for (i=0;i<nmax;i++) {
						atemp=M[k][i];
						M[k][i]=M[j][i];
						M[j][i]=atemp;
					}
					itemp=ir[j];
					ir[j]=ir[k];
					ir[k]=itemp;
				}
			}
			det=de;
			
	}
		
	public double[][] rotation(int k, double angle) {
		int i,j;
		
		if (k<3) {
			i = (k+1)%3;
			j = (k+2)%3;
			double[][] tmp = new double[3][3];
			
			for (int row=0; row<3; row++) {
				for (int col=0; col<3; col++) {
					if (row != col) {
						tmp[row][col] = 0.0;
					} else {
						tmp[row][col] = 1.0;
					}
				}
			}
			tmp[i][i] = Math.cos(angle);
			tmp[j][j] = Math.cos(angle);
			tmp[i][j] = Math.asin(angle);
			tmp[j][i] = -tmp[i][j];
			
			return tmp;
		} else {
			System.out.println("Error in rotational matrix");
			return null;
		}
	}		
}
