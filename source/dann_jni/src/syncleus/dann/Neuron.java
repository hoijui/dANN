package syncleus.dann;


public class Neuron
{
	static
	{
		System.load("/opt/sun-jre-bin-1.4.2.10/lib/i386/libdann.so");
	}
	
	private int nativeId = 0;
	private boolean constructed = false;
	
	
	public Neuron(int nativeIdToSet)
	{
		this.nativeId = nativeIdToSet;
	}
	
	public Neuron(Layer OwningLayerToSet, DNA DNAToSet, double InitialBiasWeight)
	{
		this.constructed = true;
		this.nativeId = this.nativeConstructor(OwningLayerToSet, DNAToSet, InitialBiasWeight);
	}
	
	protected int getNativeId()
	{
		return this.nativeId;
	}
	
	protected void finalize() throws Throwable
	{
		try
		{
			if( this.constructed == true )
			{
				this.nativeDestructor();
				this.nativeId = 0;
			}
		}
		finally
		{
			super.finalize();
		}
	}

	public native int nativeConstructor(Layer OwningLayerToSet, DNA DNAToSet, double InitialBiasWeight);
	protected native void nativeDestructor();
	public native double GetOutput();
	public native DNA GetDNA();
	public native double GetDeltaTrain();
	public native Layer GetParentLayer();
	public native boolean ConnectToNeuron(Neuron ToConnectTo, double InitialWeight);
	public native void DisconnectAllDestinationSynapses();
	public native void DisconnectAllSourceSynapses();
	public native void DisconnectAllSynapses();
	public native boolean DisconnectSourceSynapse(Synapse ToDisconnectFrom);
	public native boolean DisconnectDestinationSynapse(Synapse ToDisconnectFrom);
	public native int GetOutgoingConnectionCount();
	public native int GetIncommingConnectionCount();
	public native double Propogate();
	public native void SetNeuronInput(double InputToSet);
	public native void ResetNeuronInput();
	public native void BackPropogateWeight();
	public native void BackPropogateStructure();
	public native void SetTrainingData(double TrainingData);
	
}
