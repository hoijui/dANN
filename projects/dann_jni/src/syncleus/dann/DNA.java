package syncleus.dann;

public class DNA
{
	static
	{
		System.load("/opt/sun-jre-bin-1.4.2.10/lib/i386/libdann.so");
	}
	
	
	
	private int nativeId = 0;
	private boolean constructed = false;
	
	
	protected int getNativeId()
	{
		return this.nativeId;
	}
	
	
	DNA()
	{
		this.nativeId = this.nativeConstructor();
		this.constructed = true;
	}
	
	DNA(double LearningRateToSet, int MinimumOutgoingToSet, int MaximumIncommingToSet, double MinimumWeightToSet, double InitialMaxWeightToSet, boolean UseMinimumWeightToSet, double IncommingDropFactorToSet, boolean LayeredForwardToSet)
	{
		this.nativeConstructor(LearningRateToSet, MinimumOutgoingToSet, MaximumIncommingToSet, MinimumWeightToSet, InitialMaxWeightToSet, UseMinimumWeightToSet, IncommingDropFactorToSet, LayeredForwardToSet);
		this.constructed = true;
	}
	
	public DNA(int nativeIdToSet)
	{
		this.nativeId = nativeIdToSet;
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
	
	public native int nativeConstructor();
	public native int nativeConstructor(double LearningRateToSet, int MinimumOutgoingToSet, int MaximumIncommingToSet, double MinimumWeightToSet, double InitialMaxWeightToSet, boolean UseMinimumWeightToSet, double IncommingDropFactorToSet, boolean LayeredForwardToSet);
	private native void nativeDestructor();
	public native double getLearningRate();
	public native int getMinimumOutgoing();
	public native int getMaximumIncomming();
	public native double getMinimumWeight();
	public native double getInitialMaxWeight();
	public native boolean getUseMinimumWeight();
	public native double getIncommingDropFactor();
	public native boolean getLayerdForward();
}
