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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@WebServlet(urlPatterns = { "/SearchServlet" })
public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if ("search".equals(action)) {
                // If the action is a search, perform the search
                String searchKey = request.getParameter("searchKey");
                String searchType = request.getParameter("searchType");

                // Call the search method
                List<Element> searchResults = searchStudents(searchKey, searchType);

                // Display the search results
                displaySearchResults(searchResults, out);
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

    private List<Element> searchStudents(String searchKey, String searchType) {
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
        }  
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return results;
    }

    private void displaySearchResults(List<Element> results, PrintWriter out) 
    {
        int numberOfResults = results.size();

        out.println("<center><h2>Search Results</h2></center>");
        out.println("<h3>Number of Students Found: " +numberOfResults + "</h3>");

      if (numberOfResults > 0) 
      {
        for (Element studentElement : results)
        {
           
            NodeList childNodes = studentElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) 
            {
                if (childNodes.item(i).getNodeType() == Element.ELEMENT_NODE) 
                {
                    Element childElement = (Element) childNodes.item(i);
                    out.println("<p>" + childElement.getTagName() + ": " + childElement.getTextContent() + "</p>");
                }
            }
            out.println("<hr>");
        }
    } 
    else 
    {
        out.println("<p>No matching records found.</p>");
    }
}
}
