package org.opezdal.midpointService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
public class ServiceREST {
    @GetMapping("users/{id}")
    boolean userConflict(@PathVariable String id){
        // Загрузка XML документа
        Document document = null    ;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            document = factory.newDocumentBuilder().parse(id + ".xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(document != null){
            return ParsingUtilities.checkConflicts(document);
        }
        return true;
    }
}
