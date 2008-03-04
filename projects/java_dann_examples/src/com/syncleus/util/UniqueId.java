package com.syncleus.util;


import java.util.Random;
import java.io.Serializable;

public class UniqueId implements Comparable<UniqueId>, Serializable
{
    private static final Random rand = new Random();
    private byte[] uniqueData = null;



    public UniqueId(int size)
    {
        this.uniqueData = new byte[size];
        rand.nextBytes(this.uniqueData);
    }



    public int hashCode()
    {
        int dataIndex = 0;
        int hash = 0;
        while(dataIndex < this.uniqueData.length)
        {
            int intBlock = 0;
            for(int blockIndex = 0;blockIndex < 4;blockIndex++)
            {
                if(dataIndex < this.uniqueData.length)
                    intBlock += this.uniqueData[dataIndex] << blockIndex;
                else
                    break;

                dataIndex++;
            }

            hash ^= intBlock;
        }

        return hash;
    }



    public int compareTo(UniqueId compareWith)
    {
        return this.toString().compareTo(compareWith.toString());
    }



    public boolean equals(Object compareWithObj)
    {
        if(!(compareWithObj instanceof UniqueId))
            return false;

        UniqueId compareWith = (UniqueId)compareWithObj;

        if(this.compareTo(compareWith) != 0)
            return false;

        return true;
    }



    public String toString()
    {
        String dataString = new String();

        for(int dataIndex = (this.uniqueData.length - 1);dataIndex >= 0;dataIndex--)
        {
            long currentData = convertByteToUnsignedLong(this.uniqueData[dataIndex]);
            String newHex = Long.toHexString(currentData);
            while(newHex.length() < 2)
                newHex = "0" + newHex;
            dataString += newHex;
        }

        return dataString.toUpperCase();
    }

    /*
     * A better solution to the convert* methods needs to be found.
     */


    private static long convertByteToUnsignedLong(byte signedByte)
    {
        long unsignedLong = signedByte;
        if(unsignedLong < 0)
            unsignedLong += 256;

        return unsignedLong;
    }
}
