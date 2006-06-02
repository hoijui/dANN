package syncleus.dann;

public class NeuralNet
{
	static
	{
		System.load("/opt/sun-jre-bin-1.4.2.10/lib/i386/libdann.so");
	}
	
	
	
	private int nativeId = 0;
	private boolean constructed = false;
	
	public NeuralNet(DNA OwnedDNAToSet, int InputCount, int OutputCount)
	{
		this.nativeId = this.nativeConstructor(OwnedDNAToSet, InputCount, OutputCount);
		this.constructed = true;
	}
	
	public NeuralNet(int nativeIdToSet)
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
	
	
	public native int nativeConstructor(DNA OwnedDNAToSet, int InputCount, int OutputCount);
	private native void nativeDestructor();
	public native Layer AddLayerAfterInput(int NeuronCount);
	public native Layer AddLayerBeforeOutput(int NeuronCount);
	public native Layer AddLayerBefore(int NeuronCount, Layer LayerToAddBefore);
	public native Layer AddLayerAfter(int NeuronCount, Layer LayerToAddAfter);
	public native void ConnectAllFeedForward();
	public native void ConnectLayeredFeedForward();
	public native Neuron GetRandomForwardNeuron(Neuron StartNeuron);
	public native int GetNeuronCount();
	public native int GetOutgoingConnectionCount();
	public native int GetIncommingConnectionCount();
	public native int GetMaximumOutgoingConnectionCount();
	public native int GetMinimumOutgoingConnectionCount();
	public native int GetMaximumIncommingConnectionCount();
	public native int GetMinimumIncommingConnectionCount();
	public native double[] GetCurrentOutput();
	public native void SetCurrentInput(double[] InputToSet);
	public native void PropogateOutput();
	public native void SetCurrentTraining(double[] TrainingToSet);
	public native void BackPropogateWeightTraining();
	public native void BackPropogateStructureTraining();
}
