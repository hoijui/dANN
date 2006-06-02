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
	public class InputLayer : Layer
	{
		#region Constructors & Access Methods




		/// <summary>
		///		Creates a new InputLayer and initilizes some values
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the attributes of the layer</param>
		/// <param name="DestinationLayerToSet">Destination layer to connect this layer to</param>
		public InputLayer(Brain OwnedBrainToSet, DNA OwnedDNAToSet, Layer DestinationLayerToSet, bool UseByteResolution) : base(OwnedBrainToSet, OwnedDNAToSet, DestinationLayerToSet, null, UseByteResolution)
		{
			this.SourceLayer = null;
		}

		/// <summary>
		///		Creates a new InputLayer and initilizes some values
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the attributes of the layer</param>
		/// <param name="DestinationLayerToSet">Destination layer to connect this layer to</param>
		public InputLayer(Brain OwnedBrainToSet, bool UseByteResolution, uint UidToSet) : base(OwnedBrainToSet, UseByteResolution, UidToSet)
		{
			this.SourceLayer = null;
		}




		#endregion

		#region Network Interfacing




		/// <summary>
		///		Sets the current input on the neurons in the layer.
		/// </summary>
		/// <param name="InputToSet">Input data to set.</param>
		public void SetInput(double[] InputToSet)
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			int Lcv = 0;
			while( (NeuronEnum.MoveNext()) && (InputToSet.Length > Lcv) )
			{
				if( NeuronEnum.Current is InputNeuron )
				{
					InputNeuron CurrentNeuron = NeuronEnum.Current as InputNeuron;
					CurrentNeuron.SetInputNeuronInput(InputToSet[Lcv]);
				}
				else
					throw new Exception("NeuronsOwned should only contain InputNeurons");

				Lcv++;
			}        
		}




		#endregion

		#region Network Architecture




		/// <summary>
		///		Add InputNeurons to the layer.
		/// </summary>
		/// <param name="CountToAdd">Number of neurons to add</param>
		public override void AddNeurons(int CountToAdd)
		{
			for(int Lcv = 0; Lcv < CountToAdd; Lcv++)
			{
				if( this.ByteResolution == true )
					this.NeuronsOwned.Add(new InputNeuron(this, OwnedDNA, true));
				else
					this.NeuronsOwned.Add(new InputNeuron(this, OwnedDNA, false));
			}        
		}

		public override void AddNeuron(Neuron NeuronToAdd)
		{
			if( NeuronToAdd is InputNeuron )
				this.NeuronsOwned.Add(NeuronToAdd);
			else
				throw new Exception("Only InputNeurons can be added to an InputLayer");
		}




		#endregion
	}
}
