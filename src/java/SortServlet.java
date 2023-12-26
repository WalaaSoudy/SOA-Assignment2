import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class SortServlet extends HttpServlet 
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Parse the XML file
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml"));
            String sortBy = request.getParameter("SortBY");
            String sortType = request.getParameter("Sorttype");

            sortStudents(doc, sortBy, sortType);
            saveDocument(doc, "C:\\Users\\ALkamel\\Downloads\\WebApplication2\\WebApplication2\\src\\java\\university.xml");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Sorted Students</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Sorted Students</h1>");

           NodeList studentNodes = doc.getElementsByTagName("Student");
           for (int i = 0; i < studentNodes.getLength(); i++) 
           {
               Node studentNode = studentNodes.item(i);
    
              if (studentNode.getNodeType() == Node.ELEMENT_NODE) 
              {
                   Element studentElement = (Element) studentNode;
                        
                   NodeList childNodes = studentElement.getChildNodes();
                   for (int j = 0; j < childNodes.getLength(); j++) 
                   {
                       
                       if (childNodes.item(j).getNodeType()== Node.ELEMENT_NODE) 
                       {
                             Element childElement = (Element) childNodes.item(j);
                             out.println("<p>" + childElement.getTagName() + ": " + childElement.getTextContent() + "</p>");
                       }
                   }
                     out.println("<hr>");
               }
           }

         out.println("</body>");
         out.println("</html>");

                                   

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            out.close();
        }
    }

    private void sortStudents(Document doc, String sortBy, String sortType)
    {
        NodeList studentNodes = doc.getElementsByTagName("Student");
        List<Node> studentList = new ArrayList<>();
        for (int i = 0; i < studentNodes.getLength(); i++) 
        {
            studentList.add(studentNodes.item(i));
        }

        Collections.sort(studentList, new Compare(sortBy, sortType));

        // Remove existing student nodes from the document
        while (doc.getDocumentElement().hasChildNodes()) 
        {
            doc.getDocumentElement().removeChild(doc.getDocumentElement().getFirstChild());
        }

        // Add the sorted student nodes back to the document
        for (Node studentNode : studentList) 
        {
            doc.getDocumentElement().appendChild(studentNode);
        }
    }

    private void saveDocument(Document doc, String filePath) throws TransformerException 
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

      // Comparator for sorting students based on user input
    private static class Compare implements Comparator<Node> 
    {
        private String sortBy;
        private String sortType;

        public Compare(String sortBy, String sortType)
        {
            this.sortBy = sortBy;
            this.sortType = sortType;
        }

        @Override
        public int compare(Node node1, Node node2)
        {
            String value1 = getNodeValue(node1, sortBy).toLowerCase();
            String value2 = getNodeValue(node2, sortBy).toLowerCase();

            //  compare as numbers
            try 
            {
                double doubleValue1 = Double.parseDouble(value1);
                double doubleValue2 = Double.parseDouble(value2);
                 //1 is desending and -1 is ascending
                if (doubleValue1 < doubleValue2)
                {
                    return (sortType.equalsIgnoreCase("Ascending")) ? -1 : 1;
                } 
                else if (doubleValue1 > doubleValue2) 
                {
                    return (sortType.equalsIgnoreCase("Ascending")) ? 1 : -1;
                } 
                else
                {
                    return 0;
                }
            } 
            catch (NumberFormatException e)
            {
                // string comparison
                int Result = value1.compareTo(value2);
                //-Result is decending
                return (sortType.equalsIgnoreCase("Ascending")) ? Result : -Result;
            }
        }

        private String getNodeValue(Node node, String tagName)
        {
            
            NodeList ChildList = node.getChildNodes();
            //for loop to child of node to get tag name 
            for (int i = 0; i < ChildList.getLength(); i++) {
                Node childNode = ChildList.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(tagName))
                {
                    return childNode.getTextContent();
                }
            }
            return "";
        }
    }
}
