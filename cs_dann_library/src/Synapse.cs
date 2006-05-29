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
	public class Synapse
	{
		#region Attributes




		/// <summary>
		///		Outgoing neuron
		/// </summary>
		public Neuron DestinationNeuron;

		/// <summary>
		///		Incomming neuron
		/// </summary>
		public Neuron SourceNeuron;

		/// <summary>
		///		Current synapses weight.
		/// </summary>
		public double Weight;



		#endregion
    

    
		#region Constructors & Access Methods




		
		/// <summary>
		///		Constructs a new synapse and sets some initial values.
		/// </summary>
		/// <remarks>
		///		InitialWeight should usually be a random number between -1 and 1.
		/// </remarks>
		/// <param name="SourceToSet">Synapse source neuron</param>
		/// <param name="DestinationToSet">Synapse destination neuron</param>
		/// <param name="InitialWeight">Synapses Initial weight</param>
		public Synapse(Neuron SourceToSet, Neuron DestinationToSet, double InitialWeight)
		{
			this.DestinationNeuron = DestinationToSet;
			this.SourceNeuron = SourceToSet;
			this.Weight = InitialWeight;
		}




		#endregion


		#region Propogation & Activation




		/// <summary>
		///		Calculate the output of the synapse.
		/// </summary>
		/// <returns>Synapses output</returns>
		public double CalculateOutput()
		{
			return this.SourceNeuron.Output * this.Weight;
		}




		#endregion

		#region Backpropogation




		/// <summary>
		///		Calculate the new weight based on the destinations DeltaTrain.
		/// </summary>
		public void LearnWeight()
		{
			this.Weight += this.DestinationNeuron.OwnedDNA.LearningRate * this.SourceNeuron.Output * this.DestinationNeuron.DeltaTrain;
			if( this.Weight == 0 )
			{
				this.SourceNeuron.DisconnectDestinationSynapse(this);
			}
		}

		/// <summary>
		///		Calculate the Differential of the destinations DeltaTrain
		/// </summary>
		/// <returns>Destinations Diferetial</returns>
		public double CalculateDifferential()
		{
			return this.Weight * this.DestinationNeuron.DeltaTrain;
		}

		public double GetSignificanceDiviation()
		{
			double AverageSignificance = ((double)1) / ((double)this.DestinationNeuron.SourceSynapses.Count);
			double ThisSignificance = Math.Abs(this.Weight) / this.DestinationNeuron.SourceWeightTotal;

			if( ThisSignificance > AverageSignificance )
				return (ThisSignificance - AverageSignificance) * (((double)1)/(((double)1)-AverageSignificance));
			else if( ThisSignificance < AverageSignificance )
				return (ThisSignificance - AverageSignificance) * (((double)-1)/(((double)0)-AverageSignificance));
			else
				return 0;
		}
/*
		public double GetSignificanceDiviation()
		{
			/*
			double AverageSignificance = ((double)1) / ((double)this.DestinationNeuron.SourceSynapses.Count);
			double ThisSignificance = (this.Weight) / this.DestinationNeuron.SourceWeightTotal;

			if( ThisSignificance > AverageSignificance )
				return (ThisSignificance - AverageSignificance) * (((double)1)/(((double)1)-AverageSignificance));
			else if( ThisSignificance < AverageSignificance )
				return (ThisSignificance - AverageSignificance) * (((double)-1)/(((double)0)-AverageSignificance));
			else
				return 0;
				

			return this.Weight;

			

			double AverageSignificance = ((double)1) / ((double)this.DestinationNeuron.SourceSynapses.Count);
			//double ThisSignificance = (this.Weight) / this.DestinationNeuron.SourceWeightTotal;

			if( this.Weight > 0 )
				return (((double)1)/(((double)1)-AverageSignificance));
			else if( this.Weight < 0 )
				return -1 * (((double)-1)/(((double)0)-AverageSignificance));
			else
				return 0;
				
		}
*/



		#endregion
	}
}
