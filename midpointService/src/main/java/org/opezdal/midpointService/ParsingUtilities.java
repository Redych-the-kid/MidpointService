package org.opezdal.midpointService;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ParsingUtilities {
    public static boolean checkConflicts(Document document){
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
            return false;
        }
    }
    private static boolean hasConflict(ArrayList<String> roles) {
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
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
