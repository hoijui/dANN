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
using System.Collections;

namespace dANN
{
	public class OutputLayer : Layer
	{
		#region Constructors & Access Methods




		/// <summary>
		///		Constructs a new output layer and sets some initial values.
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the properties of the layer</param>
		/// <param name="SourceLayerToSet">Source Layer</param>
		public OutputLayer(Brain OwnedBrainToSet, DNA OwnedDNAToSet, Layer SourceLayerToSet, bool UseByteResolution) : base(OwnedBrainToSet, OwnedDNAToSet, null, SourceLayerToSet, UseByteResolution)
		{
			this.DestinationLayer = null;
		}

		/// <summary>
		///		Constructs a new output layer and sets some initial values.
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the properties of the layer</param>
		/// <param name="SourceLayerToSet">Source Layer</param>
		public OutputLayer(Brain OwnedBrainToSet, bool UseByteResolution, uint UidToSet) : base(OwnedBrainToSet, UseByteResolution, UidToSet)
		{
			this.DestinationLayer = null;
		}




		#endregion

		#region Network Interfacing




		/// <summary>
		///		Set the networks current training data
		/// </summary>
		/// <remarks>
		///		Teh traing data should have the same order as teh output data it corilates to.
		/// </remarks>
		/// <param name="TrainingToSet">Trainging data</param>
		public void SetTraining(double[] TrainingToSet)
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			int Lcv = 0;
			while( (NeuronEnum.MoveNext()) && (TrainingToSet.Length > Lcv) )
			{
				if( NeuronEnum.Current is OutputNeuron )
				{
					OutputNeuron CurrentNeuron = NeuronEnum.Current as OutputNeuron;
					CurrentNeuron.SetTrainingData(TrainingToSet[Lcv]);
				}
				else
					throw new Exception("NeuronsOwned should only contain OutputNeuron");

				Lcv++;
			}          
		} 




		#endregion

		#region Network Architecture




		/// <summary>
		///		Creates and adds OutputNeurons to the layer
		/// </summary>
		/// <param name="CountToAdd">Number of OutputNeurons to add</param>
		public override void AddNeurons(int CountToAdd)
		{
			for(int Lcv = 0; Lcv < CountToAdd; Lcv++)
			{
				if( this.ByteResolution == true )
					this.NeuronsOwned.Add(new OutputNeuron(this, OwnedDNA, true));
				else
					this.NeuronsOwned.Add(new OutputNeuron(this, OwnedDNA, false));
			}        
		}

		public override void AddNeuron(Neuron NeuronToAdd)
		{
			if( NeuronToAdd is OutputNeuron )
				this.NeuronsOwned.Add(NeuronToAdd);
			else
				throw new Exception("Only OutputNeurons can be added to an OutputLayer");
		}

		/// <summary>
		///		This method should not be called in an OutputLayer. Throws an error if called.
		/// </summary>
		/// <param name="LayerToConnectTo">Not used</param>
		public override void ConnectAllToLayer(Layer LayerToConnectTo)
		{
			throw new Exception("An output layer cannot connect to other layers");
		}




		#endregion

	}
}
