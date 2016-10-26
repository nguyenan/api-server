package com.wut.format.html;

//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.io.Writer;
//
//import javax.xml.transform.Result;
//import javax.xml.transform.Source;
//import javax.xml.transform.SourceLocator;
//import javax.xml.transform.Templates;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.TransformerFactoryConfigurationError;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//
////import org.restlet.data.MediaType;
////
////import com.thoughtworks.xstream.XStream;
////import com.thoughtworks.xstream.io.xml.TraxSource;
//import com.wut.format.Formatter;
//import com.wut.model.Data;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.ErrorHandler;
//import com.wut.support.StreamWriter;

// TODO delete this class and rename the other
public class HTMLFormatter { //implements Formatter {

//	private static StringBuilder XSLT = new StringBuilder();
//
//	static {
//		try {
//			FileInputStream fstream = new FileInputStream("style.xslt");
//			DataInputStream in = new DataInputStream(fstream);
//			while (in.available() != 0) {
//				XSLT.append(in.readLine());
//			}
//			in.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String format(Object result) {
//		try {
//			XStream xstream = new XStream();
//			TraxSource traxSource = new TraxSource(result, xstream);
//			Writer buffer = new StringWriter();
//			StringReader stringReader = new StringReader(XSLT.toString());
//			StreamSource streamSource = new StreamSource(stringReader);
//			TransformerFactory transFactory = TransformerFactory.newInstance();
//			Transformer transformer = transFactory.newTransformer(streamSource);
//			StreamResult streamResult = new StreamResult(buffer);
//			transformer.transform(traxSource, streamResult);
//			return streamResult.getWriter().toString();
//		} catch (TransformerConfigurationException e) {
//			e.printStackTrace();
//		} catch (TransformerFactoryConfigurationError e) {
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//	
//	public MediaType getMediaType() {
//		return MediaType.TEXT_PLAIN;
//	}
//	
//	
//	// This method applies the xslFilename to inFilename and writes
//    // the output to outFilename.
//    public static void xsl(String inFilename, String outFilename, String xslFilename) {
//        try {
//            // Create transformer factory
//            TransformerFactory factory = TransformerFactory.newInstance();
//
//            // Use the factory to create a template containing the xsl file
//            FileInputStream fileInputStream = new FileInputStream("style.xslt");
//			StreamSource streamSource = new StreamSource(
//                fileInputStream);
//			Templates template = factory.newTemplates(streamSource);
//
//            // Use the template to create a transformer
//            Transformer xformer = template.newTransformer();
//
//            // Prepare the input and output files
//            Source source = new StreamSource(new FileInputStream(inFilename));
//            Writer buffer = new StringWriter();
//            Result result = new StreamResult(buffer);
//
//            // Apply the xsl file to the source file and write the result to the output file
//            xformer.transform(source, result);
//            
//            //return buffer.getWriter().toString();
//        } catch (FileNotFoundException e) {
//        } catch (TransformerConfigurationException e) {
//            // An error occurred in the XSL file
//        } catch (TransformerException e) {
//            // An error occurred while applying the XSL file
//            // Get location of error in input file
//            SourceLocator locator = e.getLocator();
//            int col = locator.getColumnNumber();
//            int line = locator.getLineNumber();
//            String publicId = locator.getPublicId();
//            String systemId = locator.getSystemId();
//        }
//    }
//
//    @Override
//	public void format(Data data, StreamWriter stream, WutRequestInterface request) {
//		String html = format(data);
//		try {
//			stream.write(html);
//		} catch (IOException e) {
//			ErrorHandler.systemError("error formatting html:" + html, e);
//		}
//	}
}
