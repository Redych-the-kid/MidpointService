package org.opezdal.midpointService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
public class ServiceREST {
    @GetMapping("users/{id}")
    ResponseEntity<String> userConflict(@PathVariable String id) throws IOException, ParserConfigurationException, SAXException {
        // Загрузка XML документа
        Document document = ParsingUtilities.getUser(id);
        String result = ParsingUtilities.checkConflicts(document);
        if(result.equals("None")){
            String responceMessage = String.format("User %s has no role conflicts!", id);
            return new ResponseEntity<>(responceMessage, HttpStatus.OK);
        } else if(!result.equals("Error")){
            String responceMessage = String.format("User %s has role conflicts: %s", id, result);
            return new ResponseEntity<>(responceMessage, HttpStatus.OK);
        } else{
            String responceMessage = "Error has occured while parsing role!";
            return new ResponseEntity<>(responceMessage, HttpStatus.OK);
        }
    }
}
