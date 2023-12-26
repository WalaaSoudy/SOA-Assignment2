import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@WebServlet(urlPatterns = { "/SearchServlet" })
public class updateservlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try
        {
            String action = request.getParameter("action");

            if("update".equals(action))
            {
                String updateKey = request.getParameter("updateKey");
                String updateType = request.getParameter("updateType");
                List<Element> searchResults = searchStudents(updateKey, updateType);
                displaySearchResults(searchResults, out);
            } 
            else if ("commitUpdate".equals(action)) 
            {
                updateStudent(request);
                out.println("Update successfully");
            } 
            else 
            {
                out.println("Invalid action.");
            }
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

    private List<Element> searchStudents(String searchKey, String searchType)
    {
        List<Element> results = new ArrayList<>();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml"));

            NodeList studentNodes = doc.getElementsByTagName("Student");

            for (int i = 0; i < studentNodes.getLength(); i++) 
            {
                Element studentElement = (Element) studentNodes.item(i);
                String valueToSearch = studentElement.getElementsByTagName(searchType).item(0).getTextContent();

                if (valueToSearch.equalsIgnoreCase(searchKey)) 
                {
                    results.add(studentElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    private void displaySearchResults(List<Element> results, PrintWriter out)
    {
        int numberOfResults = results.size();
        out.println("<center><h2>Update Page</h2></center>");
        if (numberOfResults > 0) 
        {
            for (Element studentElement : results)
            {
                NodeList childNodes = studentElement.getChildNodes();

                out.println("<form action='updateservlet' method='get'>");

                for (int i = 0; i < childNodes.getLength(); i++)
                {
                    if (childNodes.item(i).getNodeType() == Element.ELEMENT_NODE) 
                    {
                        Element childElement = (Element) childNodes.item(i);
                        String tagName = childElement.getTagName();
                        String textContent = childElement.getTextContent();

                        if ("ID".equalsIgnoreCase(tagName)) 
                        {
                            // Display the id as Text
                            out.println("<p>" + tagName + ": " + textContent + "</p>");
                            out.println("<input type='hidden' name='" + tagName + "' value='" + textContent + "'>");
                        } 
                        else 
                        {
                            // Display as Edit
                            out.println("<p>" + tagName + ": <input type='text' name='" + tagName + "' value='" + textContent + "'></p>");
                        }
                    }
                }

                out.println("<input type='hidden' name='action' value='commitUpdate'>");
                   out.println("<br>");
                out.println("<input type='submit' value='update Data' style='background-color:#0096c7' >");
                out.println("</form>");
              
            }
        }
        else 
        {
            out.println("<p>No matching records found.</p>");
        }
    }

    private void updateStudent(HttpServletRequest request) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml"));

            NodeList studentNodes = doc.getElementsByTagName("Student");

            String updated = request.getParameter("ID");

            for (int i = 0; i < studentNodes.getLength(); i++)
            {
                Element studentElement = (Element) studentNodes.item(i);
                String currentId = studentElement.getElementsByTagName("ID").item(0).getTextContent();

                if (currentId.equals(updated)) 
                {
                    NodeList childNode = studentElement.getChildNodes();

                    for (int j = 0; j < childNode.getLength(); j++) 
                    {
                        if (childNode.item(j).getNodeType() == Element.ELEMENT_NODE) 
                        {
                            Element childElement = (Element) childNode.item(j);
                            String tagName = childElement.getTagName();
                            String updatedValue = request.getParameter(tagName);

                            childElement.setTextContent(updatedValue);
                        }
                    }

                    // Save the updated document 
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml"));
                    transformer.transform(source, result);

                    break; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
