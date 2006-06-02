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
	/// <summary>
	/// Summary description for Layer.
	/// </summary>
	public class Layer
	{
		#region Attributes





		/// <summary>
		///		Neurons in this layer
		/// </summary>
		public ArrayList NeuronsOwned = new ArrayList();

		/// <summary>
		///		Source layer
		/// </summary>
		public Layer SourceLayer;

		/// <summary>
		///		Destination layer
		/// </summary>
		public Layer DestinationLayer;

		/// <summary>
		///		DNA which dictates the properties of the layer
		/// </summary>
		public DNA OwnedDNA;

		public Brain OwnedBrain;

		public bool ByteResolution = false;

		public uint Uid;



		#endregion
    

   
		#region Constructors & Access Methods




		/// <summary>
		///		Constructs a new layer, and sets some initial values
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the properties of the layer</param>
		/// <param name="DestinationLayerToSet">Initial Destination layer.</param>
		/// <param name="SourceLayerToSet">Initial source layer</param>
		public Layer(Brain OwnedBrainToSet, DNA OwnedDNAToSet, Layer DestinationLayerToSet, Layer SourceLayerToSet, bool UseByteResolution) 
		{
			this.ByteResolution =UseByteResolution;
			this.OwnedBrain = OwnedBrainToSet;
			this.SourceLayer = SourceLayerToSet;
			this.DestinationLayer = DestinationLayerToSet;
			this.OwnedDNA = OwnedDNAToSet;
			this.Uid = this.OwnedBrain.GetNextLayerId();
		}

		/// <summary>
		///		Constructs a new layer, and sets some initial values
		/// </summary>
		/// <param name="OwnedDNAToSet">DNA that dictates the properties of the layer</param>
		/// <param name="DestinationLayerToSet">Initial Destination layer.</param>
		/// <param name="SourceLayerToSet">Initial source layer</param>
		public Layer(Brain OwnedBrainToSet, bool UseByteResolution, uint UidToSet) 
		{
			this.ByteResolution =UseByteResolution;
			this.OwnedBrain = OwnedBrainToSet;
			this.Uid = UidToSet;
		}




		#endregion


		#region Network Architecture




		/// <summary>
		///		Creates the given neurons and adds them to the layer.
		/// </summary>
		/// <param name="CountToAdd">Number of neurons to create</param>
		public virtual void AddNeurons(int CountToAdd)
		{
			for(int Lcv = 0; Lcv < CountToAdd; Lcv++)
			{
				if( this.ByteResolution == true )
					this.NeuronsOwned.Add(new Neuron(this, OwnedDNA, true));
				else
					this.NeuronsOwned.Add(new Neuron(this, OwnedDNA, false));
			}        
		}

		public virtual void AddNeuron(Neuron NeuronToAdd)
		{
			this.NeuronsOwned.Add(NeuronToAdd);
		}
    
		/// <summary>
		///		Connect all the neurons in the layer to the neurons int he specified layer
		/// </summary>
		/// <param name="LayerToConnectTo">Layer to connect to</param>
		public virtual void ConnectAllToLayer(Layer LayerToConnectTo)
		{
			if( LayerToConnectTo is InputLayer )
				throw new Exception("Cannot connect to an input layer");

			IEnumerator FromNeuronsEnum = this.NeuronsOwned.GetEnumerator();
			while( FromNeuronsEnum.MoveNext() )
			{
				if( FromNeuronsEnum.Current is Neuron )
				{
					Neuron FromNeuron = FromNeuronsEnum.Current as Neuron;
					IEnumerator ToNeuronsEnum = LayerToConnectTo.NeuronsOwned.GetEnumerator();
					while( ToNeuronsEnum.MoveNext() )
					{
						if(ToNeuronsEnum.Current is Neuron)
						{
							Neuron ToNeuron = ToNeuronsEnum.Current as Neuron;
							FromNeuron.ConnectToNeuron(ToNeuron);
						}
						else
							throw new Exception("Layer owns non neurons classes");
					}
				}
				else
					throw new Exception("NeuronsOwned can only contain neurons");
			}
		}
    
		/// <summary>
		///		Connect all the neurons in this layer to the next layer.
		/// </summary>
		public void ConnectAllToNextLayer()
		{
			this.ConnectAllToLayer(this.DestinationLayer);
		}

		public void ConnectAllToForwardLayers(Layer LastLayerToConnectTo)
		{
			Layer CurrentToLayer = this.DestinationLayer;
			while((CurrentToLayer != null)&&(CurrentToLayer != LastLayerToConnectTo))
			{
				this.ConnectAllToLayer(CurrentToLayer);

				CurrentToLayer = CurrentToLayer.DestinationLayer;
			}       
            
			if( LastLayerToConnectTo != null )
				this.ConnectAllToLayer(LastLayerToConnectTo);
		}

		public Neuron GetRandomNeuron()
		{
			int NeuronIndex = (int) Math.Floor(this.NeuronsOwned.Count * this.OwnedDNA.RandomGenerator.NextDouble());
			if( this.NeuronsOwned[NeuronIndex] is Neuron )
				return (this.NeuronsOwned[NeuronIndex] as Neuron);
			else
				throw new Exception("NeuronsOwned should only contain Neurons");
		}

		public Neuron GetNeuronByUid(uint UidToSearch)
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					if( CurrentNeuron.Uid == UidToSearch )
						return CurrentNeuron;
				}
				else
					throw new Exception("NeuronsOwned should only contain neurons");
			}

			throw new Exception("Neuron not found");
		}

		public bool ContainsNeuronByUid(uint UidToSearch)
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					if( CurrentNeuron.Uid == UidToSearch )
						return true;
				}
				else
					throw new Exception("NeuronsOwned should only contain neurons");
			}

			return false;
		}

		//returns an arrayList containing an array list for every neuron,
		//wich in turn contains all that neurons weights as ints
		public ArrayList GetNeuronWeightArray()
		{
			ArrayList RetVal = new ArrayList();

			//if there are no neurons return an empty array list
			if( this.NeuronsOwned.Count <= 0 )
				return RetVal;

			//iterate thru allt he neurons owned and add their array to the array list
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;

					RetVal.Add(CurrentNeuron.GetSourceWeightValues());
				}
				else
					throw new Exception("Only neurons should be in the NeuronsOwned array");
			}

			return RetVal;
		}




		#endregion

		#region Network Interfacing




		/// <summary>
		///		Gets the current output of the layer
		/// </summary>
		/// <returns>The current network output</returns>
		public double[] GetOutput()
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			double[] RetVal = new double[this.NeuronsOwned.Count];
			int Lcv = 0;
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					RetVal[Lcv] = CurrentNeuron.Output;
				}
				else
					throw new Exception("NeuronsOwned should only contian OutputNeurons");

				Lcv++;
			}

			return RetVal;
		}




		#endregion

		#region Propogation & Activation




		/// <summary>
		///		Propogate all the neurons in this layer
		/// </summary>
		public void PropogateAll()
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					CurrentNeuron.Propogate();
				}
				else
					throw new Exception("NeuronsOwned can only contain Neurons");
			}       
		}




		#endregion

		#region Backpropogation




		/// <summary>
		///		Backpropogate all the neurons in this layer.
		/// </summary>
		public void BackPropogateWeightAll()
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					CurrentNeuron.BackPropogateWeight();
				}
				else
					throw new Exception("NeuronsOwned can only contain neurons");
			}      
		} 

		public void BackPropogateStructureAll()
		{
			IEnumerator NeuronEnum = this.NeuronsOwned.GetEnumerator();
			while( NeuronEnum.MoveNext() )
			{
				if( NeuronEnum.Current is Neuron )
				{
					Neuron CurrentNeuron = NeuronEnum.Current as Neuron;
					CurrentNeuron.BackpropogateStructure();
				}
				else
					throw new Exception("NeuronsOwned can only contain neurons");
			}      
		} 




		#endregion
	}
}
