package com.excite.atm.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Basic jaxb xml loader and writer class.
 * 
 * @author James David
 *
 */
public class XMLLoader {
	private static Logger log = Logger.getLogger(XMLLoader.class.getName());
	
    public static <T> Object jaxbXMLToObject(Class<T> clz, String fileName) throws Exception {
        try {
            JAXBContext context = JAXBContext.newInstance(clz);
            Unmarshaller un = context.createUnmarshaller();      
            Object retVal = un.unmarshal(new File(fileName));
            return retVal;
        } catch (JAXBException e) {
            log.log(Level.SEVERE, "Unable to load XMLfrom file " + fileName, e);
            throw e;
        }        
    }
 
 
    public static void jaxbObjectToXML(Object obj, String fileName) throws Exception {
 
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 
            m.marshal(obj, new File(fileName));
        } catch (JAXBException e) {
        	log.log(Level.SEVERE, "Unable to write XML file " + fileName, e);
        	throw e;
        }
    }
}
