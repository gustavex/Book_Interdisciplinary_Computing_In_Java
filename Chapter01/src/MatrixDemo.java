
class MatrixDemo {
	final int Size = 3;
	Matrix A = new Matrix(Size, Size);
	Matrix B = new Matrix(Size, Size);
	Matrix C = new Matrix(Size, Size);
	Matrix D = new Matrix(Size, Size);
	Matrix E = new Matrix(Size, Size);
	
	public static void main(String args[]) {
		MatrixDemo demo = new MatrixDemo();
		System.out.println("A = ");
		demo.printMatrix(demo.A);
		
		System.out.println("B = ");
		demo.printMatrix(demo.B);
		
		demo.C.M = demo.A.transpose();
		System.out.println("transpose of A = ");
		demo.printMatrix(demo.C);
		
		demo.C.M = demo.A.plus(demo.B.M);
		System.out.println("A + B = ");
		demo.printMatrix(demo.C);
		
		demo.C.M = demo.B.minus(demo.A.M);
		System.out.println("B - A = ");
		demo.printMatrix(demo.C);
		
		try {
			demo.C.M = demo.A.ret_inv();
			System.out.println("inverse of A = ");
			demo.printMatrix(demo.C);
		} catch (MyMatrixExceptions mme) {
			System.out.println(mme.getMessage());
		}
		
		demo.D.M = demo.A.times(demo.C.M);
		System.out.println("A x A_inverse = ");
		demo.printMatrix(demo.D);
		
		try {
			demo.E.M = demo.B.minus(demo.A.times(demo.C.plus(demo.A.ret_inv())));
			System.out.println("B - (A x (A_inverse + A_inverse)) = ");
			demo.printMatrix(demo.E);
		} catch (MyMatrixExceptions mme) {
			System.out.println(mme.getMessage());
		}
	}
	
	public MatrixDemo() {
		A.M[0][0] = 1.; A.M[0][1] = 2.; A.M[0][2] = 5.;
		A.M[1][0] = 2.; A.M[1][1] = 3.; A.M[1][2] = 8.;
		A.M[2][0] = -1.; A.M[2][1] = 1.; A.M[2][2] = 2.;
		
		B.M[0][0] = 1.; B.M[0][1] = 0.; B.M[0][2] = 0.;
		B.M[1][0] = 0.; B.M[1][1] = 1.; B.M[1][2] = 0.;
		B.M[2][0] = 0.; B.M[2][1] = 0.; B.M[2][2] = 1.;
	}
	
	public void printMatrix(Matrix C) {
		for (int i=0; i<Size; i++) {
			for (int j =0; j<Size; j++)
				System.out.print(C.M[i][j]+" ");
			System.out.println("");
		}
		System.out.println("");
	}
}
