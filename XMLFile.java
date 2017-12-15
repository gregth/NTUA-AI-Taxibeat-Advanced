import org.w3c.dom.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import java.util.*;

public class XMLFile {
    private Document doc;
    private Transformer transformer;
    private DOMSource source;
    private StreamResult consoleResult;
    private StreamResult result;
    public XMLFile(String filename) {
        try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();

			// Used to write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			source = new DOMSource(doc);
			result = new StreamResult(new File(filename));

            System.out.println("\nOutput file at: " + filename);
			// Output to console for testing
			consoleResult = new StreamResult(System.out);
        } catch (Exception e) {
            System.out.println("Error creating doc");
        }
    }


    private Element createStyleElement(String styleName, String color) {
        Element style = doc.createElement("Style");
        Attr attrType = doc.createAttribute("id");
        attrType.setValue(styleName);
        style.setAttributeNode(attrType);
        Element lineStyle = doc.createElement("LineStyle");
        style.appendChild(lineStyle);

        Element colorElement = doc.createElement("color");
        colorElement.appendChild(doc.createTextNode(color));
        lineStyle.appendChild(colorElement);

        Element widthElement = doc.createElement("width");
        widthElement.appendChild(doc.createTextNode("4"));
        lineStyle.appendChild(widthElement);

        return style;
    }

    private Element createPlacemarkElement(String name,
            String style, ArrayList<String> coordinates) {
        Element placemarkElement = doc.createElement("Placemark");
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(name));
        placemarkElement.appendChild(nameElement);

        Element styleElement = doc.createElement("styleUrl");
        styleElement.appendChild(doc.createTextNode("#" + style));
        placemarkElement.appendChild(styleElement);

        Element lineStringElement = doc.createElement("LineString");
        placemarkElement.appendChild(lineStringElement);

        Element altElement = doc.createElement("altitudeMode");
        altElement.appendChild(doc.createTextNode("relative"));
        lineStringElement.appendChild(altElement);

        Element coordElement = doc.createElement("coordinates");
        lineStringElement.appendChild(coordElement);

        for (String item: coordinates) {
            String[] parts = item.split(" ");
            coordElement.appendChild(doc.createTextNode(
                parts[0] + "," + parts[1]  + ",0" + "\n"
            ));
        };
        return placemarkElement;
    }


    public void write(SortedSet<Route> routes) {
        try {
            // <kml>
            Element rootElement = doc.createElement("kml");
            Attr attrType = doc.createAttribute("xmlns");
            attrType.setValue("http://earth.google.com/kml/2.1");
            rootElement.setAttributeNode(attrType);
            doc.appendChild(rootElement);

            // <Document>
            Element docElement = doc.createElement("Document");
            rootElement.appendChild(docElement);

            // <name>
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode("Taxi Routes"));
            docElement.appendChild(name);

            docElement.appendChild(createStyleElement("green", "ff009900"));
            docElement.appendChild(createStyleElement("red", "ff0000ff"));

            Iterator<Route> iterator = routes.iterator();
            iterator.next(); // add the shortest path last
            Route route;
            while (iterator.hasNext()) {
                route = iterator.next();
                docElement.appendChild(createPlacemarkElement("Taxi " + route.getDriver().getId(), "red", route.getNodesString()));
            }
            docElement.appendChild(createPlacemarkElement("Taxi " + routes.first().getDriver().getId(), "green", routes.first().getNodesString()));
            routes.remove(routes.first());

			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (Exception pce) {
            System.out.println("Error while writing file...");
			pce.printStackTrace();
		}
    }
}
