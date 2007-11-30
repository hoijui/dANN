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
	public class InputNeuron : Neuron
	{

		#region Attributes





		/// <summary>
		///		The current external input on the neuron
		/// </summary>
		protected double InputNeuronInput = 0;




		#endregion
    


		#region Contructors & Access Methods




		/// <summary>
		///		Constructs a new input neuron and sets some inital value
		/// </summary>
		/// <param name="OwningLayerToSet">Layer that owns the new neuron</param>
		/// <param name="OwnedDNAToSet">DNA that sets the neurons properties</param>
		public InputNeuron(Layer OwningLayerToSet, DNA OwnedDNAToSet, bool IsByteResolution):base(OwningLayerToSet, OwnedDNAToSet, IsByteResolution)
		{
			if((OwningLayerToSet is InputLayer) != true)
				throw new Exception("Input neuron can only belong to an InputLayer");
		}

		/// <summary>
		///		Constructs a new input neuron and sets some inital value
		/// </summary>
		/// <param name="OwningLayerToSet">Layer that owns the new neuron</param>
		/// <param name="OwnedDNAToSet">DNA that sets the neurons properties</param>
		public InputNeuron(Layer OwningLayerToSet, bool IsByteResolution, double InitialBiasWeight, uint UidToSet):base(OwningLayerToSet, IsByteResolution, InitialBiasWeight, UidToSet)
		{
			if((OwningLayerToSet is InputLayer) != true)
				throw new Exception("Input neuron can only belong to an InputLayer");
		}




		#endregion


		#region Network interfacing




		/// <summary>
		///		Sets the external input applied to the neuron.
		/// </summary>
		/// <param name="InputToSet">Input value to set.</param>
		public void SetInputNeuronInput(double InputToSet)
		{
			this.InputNeuronInput = InputToSet;
		}




		#endregion

		#region Network Architecture



		/// <summary>
		///		This makes sure that this neuron is never connected to. Calling this
		///		function throws an exception.
		/// </summary>
		/// <param name="ToConnectFrom">Not used</param>
		protected override void ConnectFromSynapse(Synapse ToConnectFrom)
		{
			throw new Exception("InputNeurons shouldnt be connected to");
		}




		#endregion

		#region Propogation & Activation




		/// <summary>
		///		Propogate a new output for this neuron based on the
		///		external input.
		/// </summary>
		public override void Propogate()
		{
			this.Activity = this.InputNeuronInput;
        
			//calculate the activity function and set the result as the output
//			this.Output = this.ActivationFunction();
			if( this.Activity > 1 )
				this.Output = 1;
			else if( this.Activity < -1 )
				this.Output = -1;
			else
				this.Output = this.Activity;
		}




		#endregion

		#region backpropogation




		/// <summary>
		///		Calculate the current DeltaTrain.
		/// </summary>
		public override void BackPropogateWeight()
		{
			this.CalculateDeltaTrain();
		}

		public override void BackpropogateStructure()
		{
			if( this.DestinationSynapses.Count < this.OwnedDNA.MinimumOutgoing )
				this.ConnectDestinationSynapse();
		}




		#endregion
	}
}
