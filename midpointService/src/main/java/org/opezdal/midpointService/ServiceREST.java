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
    boolean userConflict(@PathVariable String id) throws IOException, ParserConfigurationException, SAXException {
        // Загрузка XML документа
        Document document = ParsingUtilities.getUser(id);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return ParsingUtilities.checkConflicts(document);
    }
}
