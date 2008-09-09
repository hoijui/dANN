package com.syncleus.core.dann.dannalyzer.ui.resources;

import org.w3c.dom.svg.*;
import org.w3c.dom.*;
import org.apache.batik.util.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Dimension;


public abstract class SvgUtility
{
    private static final Pattern translatePattern = Pattern.compile("translate\\(([\\-\\.1234567890]*)[,\\s]*([\\-\\.1234567890]*)\\)");
    
    private static Dimension naturalDimensions(SVGDocument doc)
    {
        GVTBuilder builder = new GVTBuilder();
        BridgeContext context = new BridgeContext(new UserAgentAdapter());
        GraphicsNode gvtRoot = builder.build(context, doc);

        float oldWidth = (float) gvtRoot.getSensitiveBounds().getWidth();
        float oldHeight = (float) gvtRoot.getSensitiveBounds().getHeight();
        
        Dimension dimension = new Dimension();
        dimension.setSize(oldWidth, oldHeight);
        return dimension;
    }
    
    public static Dimension scale(SVGDocument original, int width, int height)
    {
        Dimension naturalDimension = SvgUtility.naturalDimensions(original);
        
        float widthScale = ((float) width) / ((float)naturalDimension.getWidth());
        float heightScale = ((float) height) /((float) naturalDimension.getHeight());
        float newScale = (widthScale < heightScale ? widthScale : heightScale);

        return SvgUtility.scale(original, newScale);
    }



    public static Dimension scale(SVGDocument original, float scale)
    {
        SVGSVGElement svg = (SVGSVGElement) original.getRootElement();
        svg.removeAttribute("width");
        svg.removeAttribute("height");
        svg.removeAttribute("viewbox");

        Element vectorsElement = (Element) original.getElementsByTagName("g").item(0);
        Matcher translateMatch = translatePattern.matcher(vectorsElement.getAttribute("transform"));
        vectorsElement.removeAttribute("transform");


        Dimension naturalDimension = SvgUtility.naturalDimensions(original);

        float oldWidth = (float) naturalDimension.getWidth();
        float oldHeight = (float) naturalDimension.getHeight();

        float newWidth = oldWidth * scale;
        float newHeight = oldHeight * scale;


        double translateX = 0.0;
        double translateY = 0.0;

        if (translateMatch.find())
        {
            String translateXText = translateMatch.group(1);
            String translateYText = translateMatch.group(2);

            translateX = Double.parseDouble(translateXText) * scale;
            translateY = Double.parseDouble(translateYText) * scale;
        }
        vectorsElement.setAttribute("transform", "translate(" + translateX + ", " + translateY + ") scale(" + scale + ")");

        svg.setAttribute("width", newWidth + "in");
        svg.setAttribute("height", newHeight + "in");
        svg.setAttribute("viewBox", "0 0 " + newWidth + " " + newHeight);
        
        Dimension dimension = new Dimension();
        dimension.setSize(newWidth, newHeight);
        return dimension;
    }
}
