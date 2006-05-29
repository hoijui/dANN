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
using System.Runtime.Serialization;

namespace dANN
{
	/// <summary>
	///		Acts as a node in a neural network and the input and outputs to the real world.
	/// </summary>
	public class Neuron
	{
		#region Attributes




		/// <summary>
		///		Current activity of all the source synapses.
		/// </summary>
		protected double Activity = 0;

		/// <summary>
		///		Current output of the neuron.
		/// </summary>
		public double Output = 0;

		/// <summary>
		///		The current Bias's Weight
		/// </summary>
		public double BiasWeight = 0;

		/// <summary>
		///		All the destination synapses
		/// </summary>
		public ArrayList DestinationSynapses = new ArrayList();

		/// <summary>
		///		All the source synapses
		/// </summary>
		public ArrayList SourceSynapses = new ArrayList();

		/// <summary>
		///		Layer that owns this neuron
		/// </summary>
		public Layer OwningLayer;

		/// <summary>
		///		DNA that dictates this neurons properties
		/// </summary>
		public DNA OwnedDNA;

		/// <summary>
		///		This neurons current DeltaTran
		/// </summary>
		public double DeltaTrain = 0;

		public bool ByteResolution = false;

		public uint Uid;

		public double SourceWeightTotal = 0;



		#endregion
    


		#region Constructors & Access Methods




		/// <summary>
		///		Constructs a new neuron.
		/// </summary>
		/// <remarks>
		///		Usually new neurons will be created using the layer class.
		/// </remarks>
		/// <param name="OwningLayerToSet">The layer this neuron belongs to.</param>
		/// <param name="OwnedDNAToSet">The DNA containing the properties for this neuron.</param>
		public Neuron(Layer OwningLayerToSet, DNA OwnedDNAToSet, bool ByteResolutionToSet)
		{
			this.ByteResolution = ByteResolutionToSet;
			this.OwnedDNA = OwnedDNAToSet;
			this.OwningLayer = OwningLayerToSet;
			this.BiasWeight = (this.OwnedDNA.RandomGenerator.NextDouble() *2)-1;

			this.Uid = this.OwningLayer.OwnedBrain.GetNextNeuronId();
		}

		/// <summary>
		///		Constructs a new neuron.
		/// </summary>
		/// <remarks>
		///		Usually new neurons will be created using the layer class.
		/// </remarks>
		/// <param name="OwningLayerToSet">The layer this neuron belongs to.</param>
		/// <param name="OwnedDNAToSet">The DNA containing the properties for this neuron.</param>
		public Neuron(Layer OwningLayerToSet, bool ByteResolutionToSet, double InitialBiasWeight, uint UidToSet)
		{
			this.ByteResolution = ByteResolutionToSet;
			this.OwningLayer = OwningLayerToSet;
			this.BiasWeight = InitialBiasWeight;

			this.Uid = UidToSet;
		}



		#endregion


		#region Network Architecture




		/// <summary>
		///		Call this method to cause this neuron to start the process of connecting
		///		to another neuron.
		/// </summary>
		/// <param name="ToConnectTo">The neuron to connect to.</param>
		public virtual void ConnectToNeuron(Neuron ToConnectTo)
		{
			//make sure you arent already connected to the neuron
			if( ToConnectTo == null)
				throw new Exception("Cant connect to a null pointer");
        
			//connect to the neuron
			//double NewWeight = (this.OwnedDNA.RandomGenerator.NextDouble() *  2 - 1) * ToConnectTo.GetLeastSignificantSourceWeight() * 0.1;
			//double NewWeight = (this.OwnedDNA.RandomGenerator.NextDouble() *  2 - 1) * this.OwnedDNA.MinimumWeight;
			//ToConnectTo.CalculateSourceWeightTotal();
			double NewWeight = 0;
			if( (ToConnectTo.SourceWeightTotal != 0)&&(ToConnectTo.SourceSynapses.Count > 0) )
				NewWeight = ToConnectTo.SourceWeightTotal * (((double)1) / ((double)ToConnectTo.SourceSynapses.Count)) * this.OwnedDNA.ConnectPercentageAverage * (this.OwnedDNA.RandomGenerator.NextDouble() *  2 - 1);
			else
				NewWeight = this.OwnedDNA.ConnectPercentageAverage * (this.OwnedDNA.RandomGenerator.NextDouble() *  2 - 1);


			if( NewWeight == 0 )
				NewWeight = Double.Epsilon;

			Synapse NewSynapse = new Synapse(this, ToConnectTo, NewWeight);
			this.DestinationSynapses.Add(NewSynapse);
			ToConnectTo.ConnectFromSynapse(NewSynapse);
		}

		/// <summary>
		///		Call this method to cause this neuron to start the process of connecting
		///		to another neuron.
		/// </summary>
		/// <param name="ToConnectTo">The neuron to connect to.</param>
		public virtual void ConnectToNeuron(Neuron ToConnectTo, double InitialWeight)
		{
			//make sure you arent already connected to the neuron
			if( ToConnectTo == null)
				throw new Exception("Cant connect to a null pointer");
        
			//connect to the neuron
			Synapse NewSynapse = new Synapse(this, ToConnectTo, InitialWeight);
			this.DestinationSynapses.Add(NewSynapse);
			ToConnectTo.ConnectFromSynapse(NewSynapse);
		}
    
		/// <summary>
		///		Called from the ConnectToNeuron of the source neuron to add the synapse as a source.
		/// </summary>
		/// <param name="ToConnectFrom">The new synapse to add as a source.</param>
		protected virtual void ConnectFromSynapse(Synapse ToConnectFrom)
		{
			//make sure you arent already connected fromt his neuron
        
			//add the synapse to the source list
			this.SourceSynapses.Add(ToConnectFrom);
		}
    
		/// <summary>
		///		Disconnects all outgoing synapses.
		/// </summary>
		public void DisconnectAllDestinationSynapses()
		{
			IEnumerator MyEnum = this.DestinationSynapses.GetEnumerator();
			while( MyEnum.MoveNext() )
			{
				if( MyEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = MyEnum.Current as Synapse;
					this.DisconnectDestinationSynapse(CurrentSynapse);
				}
				else
					throw new Exception("This collection should only contain synapses");
			}
		}
    
		/// <summary>
		///		Disconnects all incomming synapses
		/// </summary>
		public void DisconnectAllSourceSynpases()
		{
			IEnumerator MyEnum = this.SourceSynapses.GetEnumerator();
			while( MyEnum.MoveNext() )
			{
				if( MyEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = MyEnum.Current as Synapse;
					this.DisconnectSourceSynapse(CurrentSynapse);
				}
				else
					throw new Exception("This collection should only contain synapses");
			}       
		}
    
		/// <summary>
		///		Disconnects all synapses.
		/// </summary>
		public void DisconnectAllSynapses()
		{
			this.DisconnectAllDestinationSynapses();
			this.DisconnectAllSourceSynpases();
		}
    
		/// <summary>
		///		Disconnects a outgoing synapse
		/// </summary>
		/// <param name="ToDisconnect">Synapse to disconnect</param>
		public void DisconnectDestinationSynapse(Synapse ToDisconnect)
		{
			//make sure we are currently connected to the synapse
			if( this.DestinationSynapses.Contains(ToDisconnect) == false )
				throw new Exception("Not currently connected to that synapse");
            
			this.DestinationSynapses.Remove(ToDisconnect);
			if(  ToDisconnect.DestinationNeuron != null )
				ToDisconnect.DestinationNeuron.RemoveSourceSynapse(ToDisconnect);
		}

		/// <summary>
		///		Disconnects a Source synapse.
		/// </summary>
		/// <param name="ToDisconnect">Synapse to disconnect</param>
		public void DisconnectSourceSynapse(Synapse ToDisconnect)
		{
			//checks to make sure it is a source synapse
			if( this.SourceSynapses.Contains(ToDisconnect) == false )
				throw new Exception("Not a source synapse");
        
			this.SourceSynapses.Remove(ToDisconnect);
        
			if( ToDisconnect.SourceNeuron != null )
				ToDisconnect.SourceNeuron.RemoveDestinationSynapse(ToDisconnect);
		}
    
		/// <summary>
		///		removes the synapse from the list. called from the DisconnectSourceSynapse
		///		of the neuron sharing the synapse
		/// </summary>
		/// <param name="ToDisconnect">Synapse to remove from destination list.</param>
		private void RemoveDestinationSynapse(Synapse ToDisconnect)
		{
			if( this.DestinationSynapses.Contains(ToDisconnect) == false )
				throw new Exception("The specified synapse is not a destination synapse");
            
			this.DestinationSynapses.Remove(ToDisconnect);
		}
    
		/// <summary>
		///		removes the synapse from the list. called from the DisconnectDestinationSynapse
		///		of the neuron sharing the synapse
		/// </summary>
		/// <param name="ToDisconnect">Synapse to remove from source list.</param>
		private void RemoveSourceSynapse(Synapse ToDisconnect)
		{
			if( this.SourceSynapses.Contains(ToDisconnect) == false )
				throw new Exception("Synapse is not a source synapse");

			this.SourceSynapses.Remove(ToDisconnect);
		}

		public double[] GetSourceWeightValues()
		{
			double[] RetVal = new double[this.SourceSynapses.Count + 1];

			//first add the bias weight
			int RetValIndex = 0;
			RetVal[0] = this.BiasWeight;
			RetValIndex++;

			//update SourceWeightTotal
			this.CalculateSourceWeightTotal();

			//iterate thru and add source synapse weights
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
					//RetVal[RetValIndex] = CurrentSynapse.Weight;
					RetVal[RetValIndex] = CurrentSynapse.GetSignificanceDiviation();
					RetValIndex++;
				}
				else throw new Exception("Only synapses should be int he sourceSynapses array");
			}

			return RetVal;
		}




		#endregion

		#region Propogation & Activation




		/// <summary>
		///		Used to calculate the output according to the current activity
		/// </summary>
		/// <remarks>
		///		Does not change this.Output. that needs to be done seperatly.
		/// </remarks>
		/// <returns>The output according to the current activity</returns>
		protected double ActivationFunction()
		{
			//replace this with the tanh function in 1.5.0
			return Math.Tanh(this.Activity);
		}
    
		/// <summary>
		///		The derivitive of the ActivationFunction. Used in training.
		/// </summary>
		/// <returns>The output of the derivitive according to the current activity</returns>
		protected double ActivationFunctionDerivitive()
		{
			return 1 - Math.Pow(this.ActivationFunction(), 2);
		}

		/// <summary>
		///		Calculates the current activity, and then sets the result acording to
		///		the ActivationFunction.
		/// </summary>
		public virtual void Propogate()
		{
			//calculate the current input activity
			this.Activity = 0;
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
					this.Activity += CurrentSynapse.CalculateOutput();
				}
				else
					throw new Exception("SourceSynapses should only contain synapses");
			}

			//Add the bias to the activity
			this.Activity += this.BiasWeight;
        
			//calculate the activity function and set the result as the output
			this.Output = this.ActivationFunction();

			//check if it needs to be restrivted to byte resolution
			if( this.ByteResolution == true )
				this.Output = (Math.Floor((this.Output+1)*(((double)256)/((double)2)))/(((double)256)/((double)2)))-1;
		}




		#endregion

		#region Backpropogation




		/// <summary>
		///		Calculates the DeltaTrain for the neuron, and then modifies the weights
		///		of all source synapses and bias acording to the new delta.
		/// </summary>
		public virtual void BackPropogateWeight()
		{
			this.CalculateDeltaTrain();
        
			//step thru source synapses and make them learn their new weight.
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
					CurrentSynapse.LearnWeight();
				}
				else
					throw new Exception("Source Synapses should only contain synapses");
			}
           
			//learn the biases new weight
			this.BiasWeight += this.OwnedDNA.LearningRate * this.DeltaTrain;
		}

		/// <summary>
		///		Calculates the current DeltaTrain from the destination neurons.
		/// </summary>
		public virtual void CalculateDeltaTrain()
		{
			this.DeltaTrain = 0;
			IEnumerator DestSynapseEnum = this.DestinationSynapses.GetEnumerator();
			while( DestSynapseEnum.MoveNext() )
			{
				if( DestSynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = DestSynapseEnum.Current as Synapse;
					this.DeltaTrain += CurrentSynapse.CalculateDifferential();
				}
				else
					throw new Exception("DestinationSynapses should only contain synapses");
			}

			this.DeltaTrain *= this.ActivationFunctionDerivitive();
        
		}

		public virtual void BackpropogateStructure()
		{
			//if( this.SourceSynapses.Count > this.OwnedDNA.MaximumIncomming )
			while( this.DropSourceSynapse() );
			//if( this.DestinationSynapses.Count < this.OwnedDNA.MinimumOutgoing )
			this.ConnectDestinationSynapse();
		}

		protected bool DropSourceSynapse()
		{
			if( this.OwnedDNA.UseMinimumWeight == true )
			{
				IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
				while( SynapseEnum.MoveNext() )
				{
					if( SynapseEnum.Current is Synapse )
					{
						Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
						if( CurrentSynapse.Weight < this.OwnedDNA.MinimumWeight )
						{
							this.DisconnectSourceSynapse(CurrentSynapse);
							return true;
						}
					}
					else
						throw new Exception("SourceSynapses should only contain synapses");
				}

				return false;

			}
			else if ( (this.OwnedDNA.UseCountDrop == true)&&(this.SourceSynapses.Count > this.OwnedDNA.MaximumIncomming) )
			{
				//0 most common, 1 least likely
				double PercentageOfTop = 1-Math.Tanh(this.OwnedDNA.RandomGenerator.NextDouble() * this.OwnedDNA.IncommingDropFactor);
				double IdealDropWeight = PercentageOfTop * this.CalculateHighestWeight();
				Synapse SynapseToDrop = this.FindClosestSynapseWithWeight(IdealDropWeight);
				this.DisconnectSourceSynapse(SynapseToDrop);

				return false;
			}
			else if( this.OwnedDNA.UseSignificanceDrop == true )
			{
				this.CalculateSourceWeightTotal();

				//iterate thru all the source synapses
				for( int SynapseLcv = 0; SynapseLcv < this.SourceSynapses.Count; SynapseLcv++ )
				{
					if( this.SourceSynapses[SynapseLcv] is Synapse )
					{
						Synapse CurrentSynapse = this.SourceSynapses[SynapseLcv] as Synapse;
						if( CurrentSynapse.GetSignificanceDiviation() <= this.OwnedDNA.DropBelowSignificanceDiviation )
						{
							this.DisconnectSourceSynapse(CurrentSynapse);
							
							SynapseLcv--;
						}
					}
					else
						throw new Exception("SourceSynapses can only contain synapses");
				}

				return false;
			}
			else if( this.OwnedDNA.DropBelowWeight == true )
			{
				//iterate thru all the source synapses
				for( int SynapseLcv = 0; SynapseLcv < this.SourceSynapses.Count; SynapseLcv++ )
				{
					if( this.SourceSynapses[SynapseLcv] is Synapse )
					{
						Synapse CurrentSynapse = this.SourceSynapses[SynapseLcv] as Synapse;
						if( CurrentSynapse.Weight <= this.OwnedDNA.DropBelow )
						{
							this.DisconnectSourceSynapse(CurrentSynapse);
							
							SynapseLcv--;
						}
					}
					else
						throw new Exception("SourceSynapses can only contain synapses");
				}

				return false;
			}
			else
				throw new Exception("One of the structure learning methods must be used");
		}

		protected void ConnectDestinationSynapse()
		{
			if( this.DestinationSynapses.Count < this.OwnedDNA.MinimumOutgoing )
			{
				Neuron NeuronToConnectTo = null;
				if( this.OwnedDNA.LayerdForward != true )
					NeuronToConnectTo = this.OwningLayer.OwnedBrain.GetRandomForwardNeuron(this);
				else
					NeuronToConnectTo = this.OwningLayer.DestinationLayer.GetRandomNeuron();
				if( this.IsConnectedTo(NeuronToConnectTo) != true )
					this.ConnectToNeuron(NeuronToConnectTo);
			}
		}

		private bool IsConnectedTo(Neuron NeuronToCheck)
		{
			IEnumerator SynapseEnum = this.DestinationSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
					if( CurrentSynapse.DestinationNeuron == NeuronToCheck )
						return true;
				}
				else
					throw new Exception("Destination synapses should only contain synapses");
			}

			return false;
		}

		private double CalculateHighestWeight()
		{
			double CurrentHighestSourceWeight = 0;
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;
					if( (Math.Abs(CurrentHighestSourceWeight)) < (Math.Abs(CurrentSynapse.Weight)) )
						CurrentHighestSourceWeight = CurrentSynapse.Weight;
				}
				else
					throw new Exception("Source Synapses should only contain synapses");
			}

			return CurrentHighestSourceWeight;
		}

		private Synapse FindClosestSynapseWithWeight(double WeightToCheck)
		{
			Synapse CurrentClosestSynapse = null;
			double CurrentClosestWeightDelta = 0;
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			if( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					CurrentClosestSynapse = SynapseEnum.Current as Synapse;
					CurrentClosestWeightDelta = WeightToCheck - CurrentClosestSynapse.Weight;
				}
				else
					throw new Exception("Source synapses can only contain synapses");
			}
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;

					double CurrentWeightDelta = WeightToCheck - CurrentSynapse.Weight;
					if( Math.Abs(CurrentWeightDelta) < Math.Abs(CurrentClosestWeightDelta))
					{
						CurrentClosestSynapse = CurrentSynapse;
						CurrentClosestWeightDelta = CurrentWeightDelta;
					}
				}
				else
					throw new Exception("Source synapses can only contain synapses");
			}

			return CurrentClosestSynapse;
		}

		protected double GetLeastSignificantSourceWeight()
		{
			double RetVal = this.BiasWeight;
			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse )
				{
					Synapse SynapseCurrent = SynapseEnum.Current as Synapse;
					if( Math.Abs(SynapseCurrent.Weight) < Math.Abs(RetVal))
					{
						if( SynapseCurrent.Weight != 0 )
							RetVal = SynapseCurrent.Weight;
						else
							this.DisconnectSourceSynapse(SynapseCurrent);
					}
				}
				else
					throw new Exception("Source Synapses should only contain synapses");
			}

			if( RetVal != 0 )
				return RetVal;
			else
				throw new Exception("None of the synapses should have a weight of 0");
		}

		public void CalculateSourceWeightTotal()
		{
			this.SourceWeightTotal = 0;

			IEnumerator SynapseEnum = this.SourceSynapses.GetEnumerator();
			while( SynapseEnum.MoveNext() )
			{
				if( SynapseEnum.Current is Synapse ) 
				{
					Synapse CurrentSynapse = SynapseEnum.Current as Synapse;

					this.SourceWeightTotal += Math.Abs(CurrentSynapse.Weight);
				}
				else
					throw new Exception("The array list contains an item it shouldnt");
			}
		}




		#endregion
	}
}
