package com.syncleus.core.dann.examples.test;


import org.freehep.j3d.plot.*;
import javax.vecmath.*;
import java.awt.Color;
import java.security.InvalidParameterException;

public class MathFunction3dDataBinder implements Binned2DData
{
    private MathFunction function = null;
    private int functionXIndex;
    private int functionYIndex;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private float zMin;
    private float zMax;
    private int resolution;



    public MathFunction3dDataBinder(MathFunction function,
                                     String functionXParam,
                                     String functionYParam,
                                     float xMin,
                                     float xMax,
                                     float yMin,
                                     float yMax,
                                     int resolution)
    {
        if( resolution <= 0 )
            throw new InvalidParameterException("resolution must be greater than 0");
        
        this.function = function;
        this.functionXIndex = this.function.getParameterNameIndex(functionXParam);
        this.functionYIndex = this.function.getParameterNameIndex(functionYParam);
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.resolution = resolution;

        boolean zMaxSet = false;
        boolean zMinSet = false;
        zMin = -1.0f;
        zMax = 1.0f;
        for(int xIndex = 0;xIndex < this.xBins();xIndex++)
        {
            this.setX(this.convertFromXIndex(xIndex));
            for(int yIndex = 0;yIndex < this.yBins();yIndex++)
            {
                this.setY(this.convertFromYIndex(yIndex));
                float currentZ = (float)this.calculateZ();

                if(Float.isNaN(currentZ) == false)
                {

                    if((this.zMax < currentZ) || (zMaxSet == false))
                    {
                        this.zMax = currentZ;
                        zMaxSet = true;
                    }

                    if((this.zMin > currentZ) || (zMinSet == false))
                    {
                        this.zMin = currentZ;
                        zMinSet = true;
                    }
                }
            }
        }
        
        if(zMax == zMin)
        {
            zMax += 1.0;
            zMin += -1.0;
        }
        
        if( Float.isNaN(zMax) || Float.isNaN(zMin))
            throw new InvalidParameterException("z does not deviate, nothing to plot!");
    }



private float convertFromXIndex(int x)
    {
        float xSize = this.xMax - this.xMin;

        return (((float)x) / ((float)this.xBins())) * xSize + this.xMin;
    }



    private float convertFromYIndex(int y)
    {
        float ySize = this.yMax - this.yMin;
        
        y = this.yBins() - y;

        return (((float)y) / ((float)this.yBins())) * ySize + this.yMin;
    }



    public MathFunction getFunction()
    {
        return this.function;
    }



    private void setX(double x)
    {
        this.function.setParameter(this.functionXIndex, x);
    }



    private void setY(double y)
    {
        this.function.setParameter(this.functionYIndex, y);
    }



    private double calculateZ()
    {
        return this.function.calculate();
    }



    public Color3b colorAt(int xIndex, int yIndex)
    {
        byte blueBytes = Byte.MIN_VALUE;

        float x = this.convertFromXIndex(xIndex);
        float y = this.convertFromYIndex(yIndex);

        this.setX(x);
        this.setY(y);
        double z = this.calculateZ();


        if(z > this.zMax)
            return new Color3b(new Color(0.0f, 0.0f, 0.0f));
        else if(z < this.zMin)
            return new Color3b(new Color(0.0f, 0.0f, 0.0f));
        else
        {
            float redValue = (float)(z - this.zMin) / (this.zMax - this.zMin);
            float blueValue = 1.0f - redValue;
            float greenValue = 0.0f;

            return new Color3b(new Color(redValue, greenValue, blueValue));
        }
    }



    public int xBins()
    {
        return this.resolution;
    }



    public float xMax()
    {
        return this.xMax;
    }



    public float xMin()
    {
        return this.xMin;
    }



    public int yBins()
    {
        return this.resolution;
    }



    public float yMax()
    {
        return this.yMax;
    }



    public float yMin()
    {
        return this.yMin;
    }



    public float zAt(int xIndex, int yIndex)
    {
        float x = this.convertFromXIndex(xIndex);
        float y = this.convertFromYIndex(yIndex);

        this.setX(x);
        this.setY(y);
        float z = (float)this.calculateZ();


        if(z < this.zMin)
            return this.zMin;
        else if(z > this.zMax)
            return this.zMax;
        else if(Float.isNaN(z))
            return 0.0f;
        else
            return z;
    }



    public float zMax()
    {
        return this.zMax;
    }



    public float zMin()
    {
        return this.zMin;
    }
}