package com.techsoft.partselector.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.techsoft.partselector.model.*;
import com.techsoft.partselector.util.knowledgebase.PartLibrary;

/** Reader for assemblies. Has only static methods to read the assembly objects
 * from files.
 * 
 * @author Andrii Dzhyrma */
public class AssemblyReader {

	// String constants
	private static final String ASSEMBLY_STRING = "Assembly";
	private static final String ASSEMBLY_NODE_STRING = "Part";
	private static final String REFERENCE_STRING = "refPt";
	private static final String NAME_STRING = "name";
	private static final String X_STRING = "X";
	private static final String Y_STRING = "Y";
	private static final String Z_STRING = "Z";

	private AssemblyReader() {
	}

	private static Vector<AssemblyNode> readAssemblyNode(Element node) {
		if (node == null)
			return null;
		// checks for the assembly nodes with parts information inside.
		Vector<AssemblyNode> result = new Vector<AssemblyNode>();
		Part part = PartLibrary.getInstance().getPart(node.getAttribute(NAME_STRING));
		if (part == null)
			part = new Part(node.getAttribute(NAME_STRING));
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element childNode = (nodeList.item(i) instanceof Element) ? (Element) nodeList.item(i) : null;
			if (childNode == null)
				continue;
			if (childNode.getNodeName().compareToIgnoreCase(REFERENCE_STRING) == 0 && childNode.hasAttribute(X_STRING) && childNode.hasAttribute(Y_STRING)
			                && childNode.hasAttribute(Z_STRING)) {
				Reference ref =
				    new Reference(Double.valueOf(childNode.getAttribute(X_STRING)), Double.valueOf(childNode.getAttribute(Y_STRING)), Double.valueOf(childNode
				                    .getAttribute(Z_STRING)));
				result.add(new AssemblyNode(part, ref));
			}
		}
		return result;
	}

	private static Assembly readAssembly(Element node) {
		if (node == null || node.getNodeName().compareToIgnoreCase(ASSEMBLY_STRING) != 0)
			return null;
		// traverse the assembly recursively.
		Assembly result = new Assembly(node.getAttribute(NAME_STRING));
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element childNode = (nodeList.item(i) instanceof Element) ? (Element) nodeList.item(i) : null;
			if (childNode == null)
				continue;
			if (childNode.getNodeName().compareToIgnoreCase(ASSEMBLY_NODE_STRING) == 0) {
				Vector<AssemblyNode> assemblyNodes = readAssemblyNode(childNode);
				if (assemblyNodes != null)
					for (AssemblyNode assemblyNode : assemblyNodes)
						if (assemblyNode != null)
							result.addNode(assemblyNode);
			} else {
				Assembly assembly = readAssembly(childNode);
				if (assembly != null)
					result.addAssembly(assembly);
			}
		}
		return result;
	}

	/** Returns an Assembly instance parsed from the file. For any exception
	 * prints the error on the screen (should be changed to maintain the bugs).
	 * 
	 * @param file - file to be parsed.
	 * @return assembly instance. */
	public static Assembly readAssemblyFromXML(File file) {
		if (!file.exists())
			return null;
		Assembly result = null;
		DocumentBuilder dBuilder;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document document = dBuilder.parse(file);
			result = readAssembly(document.getDocumentElement());

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println(e.getMessage());
		}

		return result;
	}

	/** Returns an Assembly instance parsed from the file.
	 * 
	 * @param fileName - name of the file to be parsed.
	 * @return assembly instance. */
	public static Assembly readAssemblyFromXML(String fileName) {
		File file = new File(fileName);
		return readAssemblyFromXML(file);
	}
}