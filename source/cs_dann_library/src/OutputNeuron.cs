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
	/// <summary>
	/// Summary description for OutputNeuron.
	/// </summary>
	public class OutputNeuron : Neuron
	{
		#region Attributes




		/// <summary>
		///		This neurons current training desired output.
		/// </summary>
		protected double Desired = 0;



		#endregion



		#region Constructors & Access Methods




		/// <summary>
		///		Constructs a new OutputNeuron and sets some intial data.
		/// </summary>
		/// <param name="OwningLayerToSet">Layer that owns the neuron</param>
		/// <param name="OwnedDNAToSet">DNA that dictates the neurons properties</param>
		public OutputNeuron(Layer OwningLayerToSet, DNA OwnedDNAToSet, bool IsByteResolution) : base(OwningLayerToSet, OwnedDNAToSet, IsByteResolution)
		{
			if((OwningLayerToSet is OutputLayer) != true)
				throw new Exception("OutputNeurons can only belong to OutputLayers");
		}

		/// <summary>
		///		Constructs a new OutputNeuron and sets some intial data.
		/// </summary>
		/// <param name="OwningLayerToSet">Layer that owns the neuron</param>
		/// <param name="OwnedDNAToSet">DNA that dictates the neurons properties</param>
		public OutputNeuron(Layer OwningLayerToSet, bool IsByteResolution, double InitialBiasWeight, uint UidToSet) : base(OwningLayerToSet, IsByteResolution, InitialBiasWeight, UidToSet)
		{
			if((OwningLayerToSet is OutputLayer) != true)
				throw new Exception("OutputNeurons can only belong to OutputLayers");
		}




		#endregion


		#region Network Interfacing




		/// <summary>
		///		Sets the current training data doe the neuron
		/// </summary>
		/// <param name="TrainingToSet">Traing data</param>
		public void SetTrainingData(double TrainingToSet)
		{
			this.Desired = TrainingToSet;
		}




		#endregion

		#region Netowrk Architecture




		/// <summary>
		///		This function should not be called. OutputNeuron's do not connect to
		///		other neurons. Throws an error if called.
		/// </summary>
		/// <param name="NeuronToConnectTo">Not Used</param>
		public override void ConnectToNeuron(Neuron NeuronToConnectTo)
		{
			throw new Exception("Output neurons shouldnt connect to neurons");
		}




		#endregion

		#region Backpropogation




		/// <summary>
		///		Calculates the DeltaTrain of an OutputNeuron. It is based
		///		on the training input.
		/// </summary>
		public override void CalculateDeltaTrain()
		{
			this.DeltaTrain = this.ActivationFunctionDerivitive() * (this.Desired - this.Output);
		}

		public override void BackpropogateStructure()
		{
			if( this.SourceSynapses.Count > this.OwnedDNA.MaximumIncomming )
				this.DropSourceSynapse();
		}




		#endregion
	}
}
