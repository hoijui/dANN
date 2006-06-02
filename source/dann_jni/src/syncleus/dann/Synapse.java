package syncleus.dann;

public class Synapse
{
	static
	{
		System.load("/opt/sun-jre-bin-1.4.2.10/lib/i386/libdann.so");
	}
	
	
	private int nativeId = 0;
	private boolean constructed = false;
	
	
	public Synapse(Neuron Destination, Neuron Source, double InitialWeight)
	{
		this.nativeId = this.nativeConstructor(Destination, Source, InitialWeight);
		this.constructed = true;
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
	
	public native int nativeConstructor(Neuron Destination, Neuron Source, double InitialWeight);
	protected native void nativeDestructor();
	public native double getCurrentWeight();
	public native Neuron getDestinationNeuron();
	public native Neuron getSourceNeuron();
	public native double calculateOutput();
	public native void learnWeight();
	public native double calculateDifferential();
}
