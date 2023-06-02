package org.opezdal.midpointService;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class ParsingUtilities {
    public static String checkConflicts(Document document){
        try {
            // Создание объекта XPath
            XPath xpath = XPathFactory.newInstance().newXPath();

            // Компиляция Xpath запроса
            XPathExpression expression = xpath.compile("//roleMembershipRef[@type='c:RoleType']/@oid");

            // Выполнение запроса и получение результата
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

            ArrayList<String> roles = new ArrayList<>();
            // Итерация по результату и вывод найденных строк
            for (int i = 0; i < nodeList.getLength(); i++) {
                String text = nodeList.item(i).getNodeValue();
                roles.add(text);
            }
            return hasConflict(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    private static String hasConflict(ArrayList<String> roles) {
        String message = null;
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/conflicts.properties"));
            for(String role: roles){
                String check = properties.getProperty(role);
                if(check != null){
                    String[] conflicts = check.split("#");
                    for(String conflict: conflicts){
                        for(String checkRole: roles){
                            if(checkRole.equals(conflict)){
                                if(message == null){
                                    message = String.format("Role %s has conflicts with %s", role, conflict);
                                }
                                else{
                                    message = message + ", " + conflict;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
        if(message != null){
            return message;
        }
        return "None";

    }
    public static Document getUser(String oid) throws IOException, ParserConfigurationException, SAXException {
        String unpw = "administrator:5ecr3t";
        String url = "http://localhost:8080/midpoint/ws/rest/users/" + oid;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        //Probably is GET by default but idc
        con.setRequestMethod("GET");
        //No idea what this is but it supposedly works to send auth data
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(unpw.getBytes()));
        con.setRequestProperty("Authorization", basicAuth);
        System.out.println(con.getInputStream());
        InputStream stream = con.getInputStream();
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        InputSource is = new InputSource(new StringReader(result));
        return factory.newDocumentBuilder().parse(is);
    }
}
