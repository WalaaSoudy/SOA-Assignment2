import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            String deleteID = request.getParameter("deleteID");

            // Check if the deleteID is not null or empty
            if (deleteID != null && !deleteID.isEmpty()) {
                // Delete the student with the specified ID
                boolean deleteResult = deleteStudent(deleteID);

                if (deleteResult) {
                    out.println("Student with ID " + deleteID + " deleted successfully.");
                } else {
                    out.println("No student found with ID " + deleteID + ".");
                }
            } else {
                out.println("Please provide a valid student ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error occurred: " + e.getMessage());
        } finally {
            out.close();
        }
    }

    private boolean deleteStudent(String deleteID) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml"));

            NodeList studentNodes = doc.getElementsByTagName("Student");

            for (int i = 0; i < studentNodes.getLength(); i++) {
                Element studentElement = (Element) studentNodes.item(i);
                Element studentID = (Element) studentElement.getElementsByTagName("ID").item(0);

                if (studentID.getTextContent().equalsIgnoreCase(deleteID)) 
                {
                    // Remove the student node from the document
                    doc.getDocumentElement().removeChild(studentElement);

                    // Save the updated XML document
                    saveXmlDocument(doc, "C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml");

                    return true; // Deletion successful
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Deletion failed (student not found)
    }

    private void saveXmlDocument(Document doc, String fileName) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fileName);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
