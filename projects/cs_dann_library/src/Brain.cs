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
using System.Runtime.Serialization;
using System.Collections;

namespace dANN
{
	[Serializable]
	public class Brain : ISerializable
	{
		#region Attributes





		/// <summary>
		///		Layer that receives the input for the netowrk.
		/// </summary>
		public InputLayer InLayer;

		/// <summary>
		///		Layer that contains the output for the network. Also receives the
		///		training data doe backpropogation.
		/// </summary>
		public OutputLayer OutLayer;

		/// <summary>
		///		The DNA which dictates the properties of the brain and its members.
		/// </summary>
		public DNA OwnedDNA;

		public int LayerCount = 2;

		private UidFactory LayerUidFactory = new UidFactory(true);
		private UidFactory NeuronUidFacotry = new UidFactory(true);




		#endregion



		#region Constructors & Access Methods



		/// <summary>
		///		Constructs a new Brain object and creates the input and output layer.
		/// </summary>
		/// <param name="OwnedDNAToSet">Set the DNA properties of the brain</param>
		/// <param name="InputCount">The number of inputs into the network</param>
		/// <param name="OutputCount">The number of outputs out of the the network</param>
		public Brain(DNA OwnedDNAToSet, int InputCount, int OutputCount) 
		{
			this.OwnedDNA = OwnedDNAToSet;
			InLayer = new InputLayer(this, OwnedDNAToSet, null, false);
			OutLayer = new OutputLayer(this, OwnedDNAToSet, null, false);
			InLayer.DestinationLayer = OutLayer;
			OutLayer.SourceLayer = InLayer;
        
			InLayer.AddNeurons(InputCount);
			OutLayer.AddNeurons(OutputCount);
		}

		public void SetDNA(DNA OwnedDNAToSet)
		{
			this.OwnedDNA = OwnedDNAToSet;

			Layer CurrentLayer = this.InLayer;
			while( CurrentLayer != null )
			{
				CurrentLayer.OwnedDNA = this.OwnedDNA;

				IEnumerator NeuronEnum = CurrentLayer.NeuronsOwned.GetEnumerator();
				while( NeuronEnum.MoveNext() )
				{
					if( NeuronEnum.Current is Neuron )
					{
						Neuron CurrentNeuron = NeuronEnum.Current as Neuron;

						CurrentNeuron.OwnedDNA = this.OwnedDNA;
					}
					else
						throw new Exception("NeuronsOwned should only contain neurons");
				}

				CurrentLayer = CurrentLayer.DestinationLayer;
			}
		}




		#endregion


		#region Network Architecture




		/// <summary>
		///		Adds a layer directly after the input layer.
		/// </summary>
		/// <param name="NeuronCount">The number of neurons to create this layer with</param>
		public Layer AddLayerAfterInput(int NeuronCount, bool ByteResolution)
		{
			return this.AddLayerAfter(NeuronCount, this.InLayer, ByteResolution);
		}
    
		/// <summary>
		///		Adds a layer directly before the output layer.
		/// </summary>
		/// <param name="NeuronCount">Number of neurons to create in this layer</param>
		public Layer AddLayerBeforeOutput(int NeuronCount, bool ByteResolution)
		{
			return this.AddLayerBefore(NeuronCount, this.OutLayer, ByteResolution);
		}
    
		/// <summary>
		///		Add a layer just before the specified layer.
		/// </summary>
		/// <param name="NeuronCount">Number of neurons to create in the new layer</param>
		/// <param name="LayerToAddBefore">Layer to add the new layer before.</param>
		public Layer AddLayerBefore(int NeuronCount, Layer LayerToAddBefore, bool ByteResolution)
		{
			//cant add a layer before the input layer
			if( LayerToAddBefore is InputLayer )
				throw new Exception("Cant add a layer before a input layer");

			Layer LayerToAdd = new Layer(this, this.OwnedDNA, LayerToAddBefore, LayerToAddBefore.SourceLayer, ByteResolution);
			LayerToAddBefore.SourceLayer.DestinationLayer = LayerToAdd;
			LayerToAddBefore.SourceLayer = LayerToAdd;
			LayerToAdd.AddNeurons(NeuronCount);     
			this.LayerCount++;

			return LayerToAdd;
		}
    
		/// <summary>
		///		Adds a layer just after the specified layer.
		/// </summary>
		/// <param name="NeuronCount">Number of neurons to create in the new layer</param>
		/// <param name="LayerToAddAfter">Layer to add the new layer after.</param>
		public Layer AddLayerAfter(int NeuronCount, Layer LayerToAddAfter, bool ByteResolution)
		{
			//cant add a layer after an output layer
			if( LayerToAddAfter is OutputLayer )
				throw new Exception("Cant add a layer after an output layer");

			Layer LayerToAdd = new Layer(this, this.OwnedDNA, LayerToAddAfter.DestinationLayer, LayerToAddAfter, ByteResolution);
			LayerToAddAfter.DestinationLayer.SourceLayer = LayerToAdd;
			LayerToAddAfter.DestinationLayer = LayerToAdd;
			LayerToAdd.AddNeurons(NeuronCount);       
			this.LayerCount++;

			return LayerToAdd;
		}
    
		/// <summary>
		///		Connects every neuron to every neuron in a layer above it.
		/// </summary>
		public void ConnectAllFeedForward()
		{
			Layer CurrentLayer = this.InLayer;
			while((CurrentLayer != null)&&((CurrentLayer is OutputLayer) == false))
			{
				Layer CurrentToLayer = CurrentLayer.DestinationLayer;
				while(CurrentToLayer != null)
				{
					CurrentLayer.ConnectAllToLayer(CurrentToLayer);

					CurrentToLayer = CurrentToLayer.DestinationLayer;
				}                        
				CurrentLayer = CurrentLayer.DestinationLayer;
			}
		}
    
		/// <summary>
		///		Connects every neuron to every other neuron in the layer just after it.
		/// </summary>
		public void ConnectLayeredFeedForward()
		{
			Layer CurrentLayer = this.InLayer;
			while((CurrentLayer != null)&&((CurrentLayer is OutputLayer) == false))
			{
				CurrentLayer.ConnectAllToNextLayer();
            
				CurrentLayer = CurrentLayer.DestinationLayer;
			}        
		}

		public Neuron GetRandomForwardNeuron(Neuron StartNeuron)
		{
			if( StartNeuron.OwningLayer is OutputLayer )
				throw new Exception("OutputLayer neurons have no forward neurons");

			int LayerCountToUse = (int) Math.Ceiling(this.OwnedDNA.RandomGenerator.NextDouble() * this.LayerCount);
			Layer LayerToUse = StartNeuron.OwningLayer;
			for(int Lcv = 0; Lcv < LayerCountToUse; Lcv++)
			{
				LayerToUse = LayerToUse.DestinationLayer;
				if( LayerToUse == null )
					LayerToUse = StartNeuron.OwningLayer.DestinationLayer;
			}

			return LayerToUse.GetRandomNeuron();
		}

		public uint GetNextNeuronId()
		{
			return this.NeuronUidFacotry.GetNextUid();
		}

		public void FreeNeuronId(uint IdToFree)
		{
			this.NeuronUidFacotry.FreeUid(IdToFree);
		}

		public uint GetNextLayerId()
		{
			return this.LayerUidFactory.GetNextUid();
		}

		public void FreeLayerId(uint IdToFree)
		{
			this.LayerUidFactory.FreeUid(IdToFree);
		}

		//returns an array wich contains an array for each layer,
		//wich in turn contains an array for each neuron in that layer,
		//wich in turn contains an int representing the weight for
		//each synapse in that neurons, Inlayer is index 0;
		public ArrayList GetCurrentWeights()
		{
			ArrayList RetVal = new ArrayList();

			//Iterate thru all the layers starting with the Inptu Layer
			Layer CurrentLayer = this.InLayer;
			while( CurrentLayer != null )
			{
				RetVal.Add(CurrentLayer.GetNeuronWeightArray());

				CurrentLayer = CurrentLayer.DestinationLayer;
			}

			return RetVal;
		}




		#endregion

		#region Network Interfacing




		/// <summary>
		///		Gets the current output of the network.
		/// </summary>
		/// <returns>The Current output.</returns>
		public double[] GetCurrentOutput()
		{
			return this.OutLayer.GetOutput();
		}
    
		/// <summary>
		///		Sets teh current traing data for the network.
		/// </summary>
		/// <remarks>
		///		The Traing data should be in the same order as the output it corresponds to.
		/// </remarks>
		/// <param name="TrainingToSet">Current Traing data.</param>
		public void SetCurrentTraining(double[] TrainingToSet)
		{
			this.OutLayer.SetTraining(TrainingToSet);
		}
    
		/// <summary>
		///		Sets the current input for the network.
		/// </summary>
		/// <remarks>
		///		Hardwire a perticular input to always be in the same position in the input data.
		/// </remarks>
		/// <param name="InputToSet">Array of the inputs to set.</param>
		public void SetCurrentInput(double[] InputToSet)
		{
			this.InLayer.SetInput(InputToSet);
		}




		#endregion

		#region Propogation & Activation




		/// <summary>
		///		Propogates each of the layers in order from the input layer to the 
		///		output layer.
		/// </summary>
		public void PropogateOutput()
		{
			Layer CurrentLayer = this.InLayer;
			while(CurrentLayer != null)
			{
				CurrentLayer.PropogateAll();
            
				CurrentLayer = CurrentLayer.DestinationLayer;
			}        
		}




		#endregion

		#region Backpropogation




		/// <summary>
		///		Backpropogates  each of the layers in order from the output layer
		///		to the input layer to improve weights.
		/// </summary>
		public void BackPropogateWeightTraining()
		{
			Layer CurrentLayer = this.OutLayer;
			while(CurrentLayer != null)
			{
				CurrentLayer.BackPropogateWeightAll();
            
				CurrentLayer = CurrentLayer.SourceLayer;
			}          
		}


		/// <summary>
		///		Backpropogates each layer to improve network structure.
		/// </summary>
		public void BackPropogateStructureTraining()
		{
			Layer CurrentLayer = this.OutLayer;
			while(CurrentLayer != null)
			{
				CurrentLayer.BackPropogateStructureAll();
            
				CurrentLayer = CurrentLayer.SourceLayer;
			} 
		}




		#endregion

		#region Serialization




		protected Brain(SerializationInfo info, StreamingContext context)
		{
			//Get brains attributes
			this.LayerCount = info.GetInt32("LayerCount");
			// Input layer and output layer must be set later

			//Get all the layers
			Layer LastLayer = null;
			for( int LayerLcv = this.LayerCount - 1; LayerLcv >= 0; LayerLcv--)
			{
				string LayerIdName = "Layer" + LayerLcv;

				uint LayerUid = info.GetUInt32(LayerIdName + "Uid");
				bool UseByteRes = info.GetBoolean(LayerIdName + "UseByteRes");
				bool IsInput = info.GetBoolean(LayerIdName + "IsInput");
				bool IsOutput = info.GetBoolean(LayerIdName + "IsOutput");
				int NeuronCount = info.GetInt32(LayerIdName + "NeuronCount");
				if( IsInput )
				{
					Layer NewLayer = new InputLayer(this, UseByteRes, LayerUid);
					this.InLayer = (NewLayer as InputLayer);
					NewLayer.DestinationLayer = LastLayer;
					LastLayer.SourceLayer = NewLayer;
					LastLayer = NewLayer;

				}
				else if( IsOutput )
				{
					LastLayer = new OutputLayer(this, UseByteRes, LayerUid);
					this.OutLayer = (LastLayer as OutputLayer);
				}
				else
				{
					Layer NewLayer = new Layer(this, UseByteRes, LayerUid);
					NewLayer.DestinationLayer = LastLayer;
					LastLayer.SourceLayer = NewLayer;
					LastLayer = NewLayer;
				}

				//Get all of this layers neurons
				for( int NeuronLcv = 0; NeuronLcv < NeuronCount; NeuronLcv++)
				{
					string NeuronIdName = LayerIdName + "Neuron" + NeuronLcv;

					double BiasWeight = info.GetDouble(NeuronIdName + "BiasWeight");
					bool UseByte = info.GetBoolean(NeuronIdName + "UseByteResolution");
					int OutConCount = info.GetInt32(NeuronIdName + "OutConCount");
					uint NeuronUid = info.GetUInt32(NeuronIdName + "Uid");

					Neuron NewNeuron = null;
					if( IsInput )
						NewNeuron = new InputNeuron(LastLayer, UseByte, BiasWeight, NeuronUid);
					else if( IsOutput )
						NewNeuron = new OutputNeuron(LastLayer, UseByte, BiasWeight, NeuronUid);
					else
						NewNeuron = new Neuron(LastLayer, UseByte, BiasWeight, NeuronUid);

					LastLayer.AddNeuron(NewNeuron);

					//Connect the new neuron to all its destinations
					for( int SynapseLcv = 0; SynapseLcv < OutConCount; SynapseLcv++)
					{
						string SynapseIdName = NeuronIdName + "Synapse" + SynapseLcv;

						uint ToUid = info.GetUInt32(SynapseIdName + "ToUid");
						double Weight = info.GetDouble(SynapseIdName + "Weight");

						Neuron ToConnectTo = this.FindNeuron(LastLayer, ToUid);

						NewNeuron.ConnectToNeuron(ToConnectTo, Weight);
					}
				}
			}
		}

		public void GetObjectData(SerializationInfo info, StreamingContext context)
		{
			//Add brains attributes
			info.AddValue("LayerCount", this.LayerCount);
			info.AddValue("InputLayerUid", this.InLayer.Uid);
			info.AddValue("OutputLayerUid", this.OutLayer.Uid);

			//Add each layer and the neurons it contains
			Layer CurrentLayer = this.InLayer;
			int LayerId = 0;
			while( CurrentLayer != null )
			{
				string LayerIdName = "Layer" + LayerId;

				//Add layers attributes
				info.AddValue(LayerIdName + "Uid", CurrentLayer.Uid);
				info.AddValue(LayerIdName + "UseByteRes", CurrentLayer.ByteResolution);
				info.AddValue(LayerIdName + "NeuronCount", CurrentLayer.NeuronsOwned.Count);

				if( CurrentLayer is InputLayer )
					info.AddValue(LayerIdName + "IsInput", true);
				else
				{
					info.AddValue(LayerIdName + "IsInput", false);
					info.AddValue(LayerIdName + "SourceLayerUid", CurrentLayer.SourceLayer.Uid);
				}

				if( CurrentLayer is OutputLayer )
					info.AddValue(LayerIdName + "IsOutput", true);
				else
				{
					info.AddValue(LayerIdName + "IsOutput", false);
					info.AddValue(LayerIdName + "DestLayerUid", CurrentLayer.DestinationLayer.Uid);
				}

				//Add all the layers neurons
				IEnumerator NeuronEnum = CurrentLayer.NeuronsOwned.GetEnumerator();
				int NeuronId = 0;
				while( NeuronEnum.MoveNext() )
				{
					if( NeuronEnum.Current is Neuron )
					{
						Neuron CurrentNeuron = NeuronEnum.Current as Neuron;

						string NeuronIdName = LayerIdName + "Neuron" + NeuronId;

						//add neurons attributes
						info.AddValue(NeuronIdName + "BiasWeight", CurrentNeuron.BiasWeight);
						info.AddValue(NeuronIdName + "OwningLayerUid", CurrentNeuron.OwningLayer.Uid);
						info.AddValue(NeuronIdName + "UseByteResolution", CurrentNeuron.ByteResolution);
						info.AddValue(NeuronIdName + "OutConCount", CurrentNeuron.DestinationSynapses.Count);
						info.AddValue(NeuronIdName + "Uid", CurrentNeuron.Uid);

						//Add all the neurons outgoing connection
						IEnumerator SynapseEnum = CurrentNeuron.DestinationSynapses.GetEnumerator();
						int SynapseId = 0;
						while( SynapseEnum.MoveNext() )
						{
							if( SynapseEnum.Current is Synapse )
							{
								Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
								string SynapseIdName = NeuronIdName + "Synapse" + SynapseId;

								info.AddValue(SynapseIdName + "ToUid", CurrentSynapse.DestinationNeuron.Uid);
								info.AddValue(SynapseIdName + "Weight", CurrentSynapse.Weight);

								SynapseId++;
							}
							else
								throw new Exception("DestinationSynapses should only contain synapses");
						}

						NeuronId++;
					}
					else
						throw new Exception("NeuronsOwned should only contain neurons");
				}

				LayerId++;
				CurrentLayer = CurrentLayer.DestinationLayer;
			}
		}

		private Neuron FindNeuron(Layer StartLayer, uint UidToSearch)
		{
			Layer CurrentLayer = StartLayer;
			while( CurrentLayer != null )
			{
				if( CurrentLayer.ContainsNeuronByUid(UidToSearch) == true )
					return CurrentLayer.GetNeuronByUid(UidToSearch);

				CurrentLayer = CurrentLayer.DestinationLayer;
			}

			throw new Exception("Neuron doesnt exist");
		}



		#endregion
	}
}
