/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/

/*
** Derived from Public-Domain source as indicated at
** http://math.nist.gov/javanumerics/jama/ as of 9/13/2009.
*/
package com.syncleus.dann.math.linear.decomposition;

import java.util.*;
import com.syncleus.dann.math.OrderedAlgebraic;
import com.syncleus.dann.math.linear.Matrix;

/**
 * matrixToDecomposeElements Decomposition.
 * <p/>
 * For an height-by-width matrix matrixToDecompose with height >= width, the
 * matrixToDecomposeElements decomposition is an height-by-width unit lower
 * triangular matrix lowerTriangularFactor, an width-by-width upper triangular
 * matrix U, and a permutation vector pivot of length height so that
 * matrixToDecompose(pivot,:) = lowerTriangularFactor*U. If height < width, then
 * lowerTriangularFactor is height-by-height and U is height-by-width.
 * <p/>
 * The matrixToDecomposeElements decompostion with pivoting always exists, even
 * if the matrix is singular, so the constructor will never fail.  The primary
 * use of the matrixToDecomposeElements decomposition is in the solution of
 * square systems of simultaneous linear equations.  This will fail if
 * isNonsingular() returns false.
 */
public class DoolittleLuDecomposition<M extends Matrix<M, F>, F extends OrderedAlgebraic<F>> implements java.io.Serializable, LuDecomposition<M, F>
{
	private static final long serialVersionUID = -7096672949387816785L;
	/**
	 * Array for internal storage of decomposition.
	 */
	private M matrix;
	/**
	 * Row and column dimensions, and pivot sign.
	 */
	private int pivotSign;
	/**
	 * Internal storage of pivot vector.
	 */
	private final int[] pivot;

	/**
	 * matrixToDecompose Elements Decomposition. Structure to access
	 * lowerTriangularFactor, U and pivot.
	 *
	 * @param matrixToDecompose Rectangular matrix
	 */
	public DoolittleLuDecomposition(final M matrixToDecompose)
	{
		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.
		this.matrix = matrixToDecompose;
		final int height = matrixToDecompose.getHeight();
		final int width = matrixToDecompose.getWidth();

		this.pivot = new int[height];
		for(int i = 0; i < height; i++)
			this.pivot[i] = i;
		this.pivotSign = 1;

		final List<F> matrixColumn = new ArrayList<F>(height);
		matrixColumn.addAll(Collections.nCopies(height, matrixToDecompose.getElementField().getZero()));

		// Outer loop.
		for(int j = 0; j < width; j++)
		{
			// Make a copy of the j-th column to localize references.
			for(int i = 0; i < height; i++)
				matrixColumn.set(i, this.matrix.get(i, j));

			// Apply previous transformations.
			for(int i = 0; i < height; i++)
			{
				// Most of the time is spent in the following dot product.
				final int kmax = Math.min(i, j);
				F s = matrixToDecompose.getElementField().getZero();
				for(int k = 0; k < kmax; k++)
					s = s.add(this.matrix.get(i, k).multiply(matrixColumn.get(k)));

				matrixColumn.set(i, matrixColumn.get(i).subtract(s));
				this.matrix = this.matrix.set(i, j, matrixColumn.get(i));
			}

			// Find pivot and exchange if necessary.
			int p = j;
			for(int i = j + 1; i < height; i++)
				if( matrixColumn.get(i).abs().compareTo(matrixColumn.get(p).abs()) > 0 )
					p = i;
			if( p != j )
			{
				for(int k = 0; k < width; k++)
				{
					final F t = this.matrix.get(p, k);
					this.matrix = this.matrix.set(p, k, this.matrix.get(j, k));
					this.matrix = this.matrix.set(j, k, t);
				}
				final int k = this.pivot[p];
				this.pivot[p] = this.pivot[j];
				this.pivot[j] = k;
				this.pivotSign = -this.pivotSign;
			}

			// Compute multipliers.
			if( (j < height) && (!this.matrix.get(j, j).equals(this.matrix.getElementField().getZero())) )
				for(int i = j + 1; i < height; i++)
					this.matrix = this.matrix.set(i, j, this.matrix.get(i, j).divide(this.matrix.get(j, j)));
		}
	}

	public M getMatrix()
	{
		return this.matrix;
	}

	public int getHeight()
	{
		return this.matrix.getHeight();
	}

	public int getWidth()
	{
		return this.matrix.getWidth();
	}

	/**
	 * Is the matrix nonsingular?
	 *
	 * @return true if U, and hence matrixToDecompose, is nonsingular.
	 */
	public boolean isNonsingular()
	{
		for(int j = 0; j < this.getWidth(); j++)
			if( this.matrix.get(j, j).equals(this.matrix.getElementField().getZero()) )
				return false;
		return true;
	}

	/**
	 * Return lower triangular factor
	 *
	 * @return lowerTriangularFactor
	 */
	public M getLowerTriangularFactor()
	{
		M lowerTriangularFactor = this.matrix;
		for(int i = 0; i < this.getHeight(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if( i == j )
					lowerTriangularFactor = lowerTriangularFactor.set(i, j, lowerTriangularFactor.getElementField().getOne());
				else if( i <= j )
					lowerTriangularFactor = lowerTriangularFactor.set(i, j, lowerTriangularFactor.getElementField().getZero());
		return lowerTriangularFactor;
	}

	/**
	 * Return upper triangular factor
	 *
	 * @return U
	 */
	public M getUpperTriangularFactor()
	{
		M U = this.matrix;
		for(int i = 0; i < this.getWidth(); i++)
			for(int j = 0; j < this.getWidth(); j++)
				if( i > j )
					U = U.set(i, j, U.getElementField().getZero());
		return U;
	}

	/**
	 * Return pivot permutation vector
	 *
	 * @return pivot
	 */
	public int[] getPivot()
	{
		final int[] p = new int[this.getHeight()];
		System.arraycopy(this.pivot, 0, p, 0, this.getHeight());
		return p;
	}

	/**
	 * Determinant
	 *
	 * @return getDeterminant(matrixToDecompose)
	 * @throws IllegalArgumentException SimpleRealMatrix must be square
	 */
	public F getDeterminant()
	{
		if( this.getHeight() != this.getWidth() )
			throw new ArithmeticException("Matrix must be square.");
		F determinant;
		if( this.pivotSign > 0 )
			determinant = this.matrix.getElementField().getOne();
		else if( this.pivotSign < 0 )
			determinant = this.matrix.getElementField().getOne().negate();
		else
			determinant = this.matrix.getElementField().getZero();
		for(int j = 0; j < this.getWidth(); j++)
			determinant = determinant.multiply(this.matrix.get(j, j));
		return determinant;
	}

	/**
	 * Solve matrixToDecompose*X = solutionMatrix
	 *
	 * @param solutionMatrix matrixToDecompose SimpleRealMatrix with as many rows
	 * as matrixToDecompose and any number of columns.
	 * @return X so that lowerTriangularFactor*U*X = solutionMatrix(pivot,:)
	 * @throws IllegalArgumentException SimpleRealMatrix row dimensions must
	 * agree.
	 * @throws RuntimeException SimpleRealMatrix is singular.
	 */
	public M solve(final M solutionMatrix)
	{
		if( solutionMatrix.getHeight() != this.getHeight() )
			throw new IllegalArgumentException("solutionMatrix row dimensions must agree.");
		if( !this.isNonsingular() )
			throw new ArithmeticException("Matrix is singular.");
		// Copy right hand side with pivoting
		final int nx = solutionMatrix.getWidth();
		M Xmat = solutionMatrix.getSubmatrix(this.pivot, 0, nx - 1);
		// Solve lowerTriangularFactor*Y = solutionMatrix(pivot,:)
		for(int k = 0; k < this.getWidth(); k++)
			for(int i = k + 1; i < this.getWidth(); i++)
				for(int j = 0; j < nx; j++)
					Xmat = Xmat.set(i, j, Xmat.get(i, j).subtract(Xmat.get(k, j).multiply(this.matrix.get(i, k))));
		// Solve U*X = Y;
		for(int k = this.getWidth() - 1; k >= 0; k--)
		{
			for(int j = 0; j < nx; j++)
				Xmat = Xmat.set(k, j, Xmat.get(k, j).divide(this.matrix.get(k, k)));
			for(int i = 0; i < k; i++)
				for(int j = 0; j < nx; j++)
					Xmat = Xmat.set(i, j, Xmat.get(i, j).subtract(Xmat.get(k, j).multiply(this.matrix.get(i, k))));
		}
		return Xmat;
	}
}
