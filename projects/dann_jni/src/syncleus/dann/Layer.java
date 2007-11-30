package syncleus.dann;

public class Layer
{
	static
	{
		System.load("/opt/sun-jre-bin-1.4.2.10/lib/i386/libdann.so");
	}
	
	
	
	private int nativeId = 0;
	private boolean constructed = false;
	
	
	public Layer(NeuralNet OwnedNeuralNetToSet, DNA OwnedDNAToSet, Layer DestinationLayerToSet, Layer SourceLayerToSet)
	{
		this.nativeId = this.nativeConstructor(OwnedNeuralNetToSet, OwnedDNAToSet, DestinationLayerToSet, SourceLayerToSet);
		this.constructed = true;
	}
	
	public Layer(int nativeIdToSet)
	{
		this.nativeId = nativeIdToSet;
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
	
	public native Layer getSourceLayer();
	public native Layer getDestinationLayer();
	public native int nativeConstructor(NeuralNet OwnedNeuralNetToSet, DNA OwnedDNAToSet, Layer DestinationLayerToSet, Layer SourceLayerToSet);
	protected native void nativeDestructor();
	public native NeuralNet GetNeuralNet();
	public native void AddNeurons(int CountToAdd);
	public native void AddNeuron(Neuron NeuronToAdd);
	public native void ConnectAllToLayer(Layer LayerToConnectTo);
	public native void ConnectAllToNextLayer();
	public native void ConnectAllToForwardLayers(Layer LastLayerToConnectTo);
	public native Neuron GetRandomNeuron();
	public native Neuron GetNeuronByUid(int UidToSearch);
	public native boolean ContainsNeuronByUid(int UidToSearch);
	public native int NeuronCount();
	public native int OutgoingConnectionCount();
	public native int IncommingConnectionCount();
	public native int MaximumOutgoingConnectionCount();
	public native int MinimumOutgoingConnectionCount();
	public native int MaximumIncommingConnectionCount();
	public native int MinimumIncommingConnectionCount();
	public native double[] GetOutput();
	public native void PropogateAll();
	public native void SetInput(double[] InputToSet);
	public native void SetTrainData(double[] TrainToSet);
	public native void BackPropogateWeightAll();
	public native void BackPropogateStructureAll();
}
