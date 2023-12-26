import org.w3c.dom.Node;
import java.io.File;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@WebServlet(urlPatterns = {"/NewServlet"})
public class NewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
 {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {
       
        String action = request.getParameter("action");

        String numStudentsParam = request.getParameter("numStudents");

        if (numStudentsParam != null && !numStudentsParam.isEmpty()) {
            int numStudents = Integer.parseInt(numStudentsParam);

            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;
            File file = new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml");
            if (file.exists()) {
                // Parse the existing XML file
                doc = docBuilder.parse(file);
            } else {
                // Create a new XML document
                doc = docBuilder.newDocument();
                Element universityElement = doc.createElement("University");
                doc.appendChild(universityElement);
            }

            // Take student data from the user and store it in the XML document
            for (int i = 0; i < numStudents; i++) {
                String studentID = request.getParameter("id" + i);
                String firstName = request.getParameter("firstName" + i);
                String lastName = request.getParameter("lastName" + i);
                String gender = request.getParameter("gender" + i);
                String gpa = request.getParameter("gpa" + i);
                String level = request.getParameter("level" + i);
                String address = request.getParameter("address" + i);

                if (isNullOrEmpty(studentID) || isNullOrEmpty(firstName) || isNullOrEmpty(lastName)|| isNullOrEmpty(gender) || isNullOrEmpty(gpa) || isNullOrEmpty(level)|| isNullOrEmpty(address)) 
                 {
                    out.println("<h2 style='color: red;'>Error: All attributes must not be null or empty </h2>");
                   return;
                }

                if (!ISString(firstName) || !ISString(lastName) || !ISString(address)) 
                {
                  out.println("<h2 style='color: red;'>Error: First name, last name, and address must contain only characters</h2>");
                  return;
                }

                if (!isNumeric(gpa) || Double.parseDouble(gpa) < 0 || Double.parseDouble(gpa) > 4) 
                {
                  out.println("<h2 style='color: red;'>Error: GPA must be a numeric value between 0 and 4</h2>");
                  return;
                }

     
               if (isDuplicateID(doc, studentID)) 
               {
                  out.println("<h2 style='color: red;'>Error: Duplicate student ID found</h2>");
                  return;
                }
                Element studentElement = doc.createElement("Student");
                doc.getDocumentElement().appendChild(studentElement);
                createElementAndAppendText(doc, studentElement, "ID", studentID);
                createElementAndAppendText(doc, studentElement, "FirstName", firstName);
                createElementAndAppendText(doc, studentElement, "LastName", lastName);
                createElementAndAppendText(doc, studentElement, "Gender", gender);
                createElementAndAppendText(doc, studentElement, "GPA", gpa);
                createElementAndAppendText(doc, studentElement, "Level", level);
                createElementAndAppendText(doc, studentElement, "Address", address);
            }

            
            saveXmlDocument(doc, "C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml");
            out.println("<h2 style='color: green;'>XML document created and data stored successfully!</h2>");
        }
    }
    catch (ParserConfigurationException | NumberFormatException e) 
    {
        e.printStackTrace();
        out.println("Error occurred: " + e.getMessage());
    } 
    catch (Exception e) 
    {
        e.printStackTrace();
        out.println("Error occurred: " + e.getMessage());
    } 
    finally 
    {
        out.close();
    }
}

    private boolean isNullOrEmpty(String value)
    {
       return value == null || value.trim().isEmpty();
    }

    private boolean ISString(String value) 
    {
       return value.matches("[a-zA-Z]+");
    }

    private boolean isNumeric(String value) 
    {
      return value.matches("\\d+(\\.\\d+)?");
    }
   private boolean isDuplicateID(Document doc, String studentID)
   {
      NodeList studentList = doc.getElementsByTagName("Student");
      for (int i = 0; i < studentList.getLength(); i++) 
      {
        Node studentNode = studentList.item(i);
        if (studentNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element studentElement = (Element) studentNode;
            Element ElementID = (Element) studentElement.getElementsByTagName("ID").item(0);
            if (ElementID != null && ElementID.getTextContent().equals(studentID)) 
            {
                return true;
            }
        }
      }

      return false;
  }



    private void createElementAndAppendText(Document doc, Element parent, String elementName, String textContent) 
    {
        Element element = doc.createElement(elementName);
        element.appendChild(doc.createTextNode(textContent));
        parent.appendChild(element);
    }


    private void saveXmlDocument(Document doc, String fileName) 
    {
       try 
       {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
       }
       catch (Exception e) 
       {
          e.printStackTrace();
       }
    }
    
}