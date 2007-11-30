#region Copright 2004 Jeffrey Phillips Freeman

/********************************************************************************/
/*                                                                              */
/*                   dANN: Dynamic Artifical Neural Network                     */
/*               (C) Copyright 2004 - * Jeffrey Phillips Freeman                */
/*                                                                              */
/*               Copyright History:                                             */
/*                  Created: July 28, 2004, Jeffrey Phillips Freeman            */
/*                                                                              */
/********************************************************************************/

#endregion

using System;

namespace dANN
{
	public class DNA
	{
		/*
		 *  Attributes
		 */

		#region Non-mutatable




		/// <summary>
		///		Used to obtain any random numbers needed.
		/// </summary>
		public Random RandomGenerator = new Random();




		#endregion

		#region Mutation Values




		/// <summary>
		///		A neurons learning rate used in Backpropogation
		/// </summary>
		public double LearningRate = 0.001;

		public int MinimumOutgoing = 100000;
		public double ConnectPercentageAverage = 0.01;
		public bool LayerdForward = true;

		public bool UseCountDrop = false;
		public int MaximumIncomming = 16;
		public double IncommingDropFactor = 20;

		public bool UseMinimumWeight = false;
		public double MinimumWeight = .0001;

		public bool UseSignificanceDrop = true;
		public double DropBelowSignificanceDiviation = -0.40;
		//public double DropBelowSignificanceDiviation = -0.40;
		//public double DropBelowSignificanceDiviation = -.10;

		public bool DropBelowWeight = false;
		//public double DropBelow = 0.002955;
		//public double DropBelow = 0.0009851;
		//public double DropBelow = -0.0009851;
		//public double DropBelow = -0.002955;
		public double DropBelow = 0;




		#endregion

		#region Mutation Factors




		/// <summary>
		///		Mutation factor for the LearningRate mutation value
		/// </summary>
		public double LearningRateMutationFactor = 1;




		#endregion
    
    
    
		/*
		 * Methods
		 */

		#region Constructors & Access Methods




		/// <summary>
		///		Creates the DNA and defines some initial values.
		/// </summary>
		public DNA()
		{
		}




		#endregion

		#region Mutation Functions






		/// <summary>
		///		Tanh Mutation Function, results in a number between -1 and 1
		///		using the Tanh mathmatical function.
		/// </summary>
		/// <param name="RandomNumber">a random number between 0 and 1</param>
		/// <param name="SteepnessFactor">determines the smoothness of the 
		///			function, higher means closer to digital.</param>
		/// <returns>A number between -1 and +1 according to Tanh</returns>
		private double TanhMutationFunction(double RandomNumber, double SteepnessFactor)
		{
			//convert the this to a number between -1 and 1 for the equation.
			RandomNumber = (RandomNumber * 2) - 1;
        
			//replace this with the tanh function in 1.5.0
			return Math.Tanh(RandomNumber * SteepnessFactor);
		}
    
		/// <summary>
		///		Results in -infinity at 0 and +infinity at 1, 0 at 0.5. this
		///		is the most common function used for mutation.
		/// </summary>
		/// <param name="RandomNumber">random number between 0 and 1</param>
		/// <param name="SteepnessFactor">determines smoothness of the function,
		///			higher means closer to a digital reaction.</param>
		/// <returns>Unbound number approaching infinity at the extremes</returns>
		private double GenericMutationFunction(double RandomNumber, double SteepnessFactor)
		{
			return (((2*RandomNumber)-1)/((RandomNumber*RandomNumber)-RandomNumber)) * SteepnessFactor;
		}




		#endregion

		#region Mutation Activation




		/// <summary>
		///		Perform a mutation of the LearningRate.
		/// </summary>
		public void MutateLearningRate()
		{
			this.LearningRate += this.GenericMutationFunction(this.RandomGenerator.NextDouble(),  this.LearningRateMutationFactor);
		}
    
    
		/// <summary>
		///		Performs a mutation on the LearningRate's mutation factor.
		/// </summary>
		public void MutateLearningRateMutationFactor()
		{
		}




		#endregion
	}
}
