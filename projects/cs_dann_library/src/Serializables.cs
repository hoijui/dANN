using System;

namespace dANN
{
	[Serializable]
	public class SerializableInt
	{
		public int Value;
	}

	[Serializable]
	public class SerializableDouble
	{
		public double Value;
	}

	public class SerializableSynapse
	{
		public uint ToUid;
		public double Weight;
	}
}
