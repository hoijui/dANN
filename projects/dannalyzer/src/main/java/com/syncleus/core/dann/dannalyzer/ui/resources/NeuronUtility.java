package com.syncleus.core.dann.dannalyzer.ui.resources;

import java.util.HashSet;
import java.util.Set;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.*;
import org.w3c.dom.*;
import org.apache.batik.util.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;

public abstract class NeuronUtility
{
    public static boolean checkId(SVGDocument original)
    {
        SVGSVGElement svg = (SVGSVGElement) original.getRootElement();
        
        return (svg.getAttribute("id").compareTo("neuron") == 0);
    }
    
    public static void removeConnections(SVGDocument original)
    {
        if(NeuronUtility.checkId(original) == false)
            return;
        
        Element vectorsElement = (Element) original.getElementsByTagName("g").item(0);
        
        Set<Node> nodesToRemove = new HashSet<Node>();
        NodeList vectors = vectorsElement.getElementsByTagName("*");
        for(int nodeIndex = 0 ; nodeIndex < vectors.getLength(); nodeIndex++)
        {
            Element vector = (Element) vectors.item(nodeIndex);
            
            String currentId = vector.getAttribute("id");
            
            if(currentId.startsWith("connection"))
                nodesToRemove.add(vector);
        }
        
        for(Node nodeToRemove : nodesToRemove)
        {
            vectorsElement.removeChild(nodeToRemove);
        }
    }
}
