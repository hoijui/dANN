package com.syncleus.core.dann.examples.nci.ui;
import java.io.File;
import java.io.FileFilter;


public class PngFileFilter implements FileFilter
{
    public boolean accept(File pathname)
    {
        return pathname.getAbsolutePath().toLowerCase().endsWith(".png");
    }
}
