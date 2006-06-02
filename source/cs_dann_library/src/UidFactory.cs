using System;
using System.Collections;


namespace dANN
{
	class UidFactory
	{
		private bool IncludeZero = true;
		private uint NextValueForArray = 0;
		private ArrayList UidList = new ArrayList();



		public UidFactory(bool IncludeZeroToSet)
		{
			this.IncludeZero = IncludeZeroToSet;

			if( this.IncludeZero == false )
				this.NextValueForArray = 1;
		}

		public uint GetNextUid()
		{
			//go thru the array list and see if any of the values arent in use
			IEnumerator UidEnum = UidList.GetEnumerator();
			while( UidEnum.MoveNext() )
			{
				if( UidEnum.Current is UidListItem )
				{
					UidListItem CurrentUid = UidEnum.Current as UidListItem;
					if( CurrentUid.InUse == false )
					{
						CurrentUid.InUse = true;
						return CurrentUid.Value;
					}
				}
				else
					throw new Exception("UidList should only contain UidListItem's");
			}

			//There are no free id's in the array so make one
			UidListItem NewUid = new UidListItem();
			NewUid.InUse = true;
			NewUid.Value = this.NextValueForArray;
			this.NextValueForArray++;
			this.UidList.Add(NewUid);
			return NewUid.Value;
		}

		public void FreeUid(uint UidToFree)
		{
			bool DeleteUid = false;

			if( UidToFree == this.NextValueForArray - 1)
				DeleteUid = true;

			//Get the Uid item
			IEnumerator UidEnum = UidList.GetEnumerator();
			while( UidEnum.MoveNext() )
			{
				if( UidEnum.Current is UidListItem )
				{
					UidListItem CurrentUid = UidEnum.Current as UidListItem;
					if( CurrentUid.Value == UidToFree )
					{
						if( CurrentUid.InUse == false )
							throw new Exception("The Uid already is free");

						if( DeleteUid )
						{
							this.UidList.Remove(CurrentUid);
							this.NextValueForArray--;
							if( (this.NextValueForArray == 0)&&(this.IncludeZero == false) )
								this.NextValueForArray = 1;

							return;
						}
						else
						{
							CurrentUid.InUse = false;
							return;
						}
					}
				}
				else
					throw new Exception("UidList should only contain UidListItem's");
			}

			throw new Exception("The Uid already is free");
		}
	}

	class UidListItem
	{
		public bool InUse = true;
		public uint Value = 0;
	}
}
