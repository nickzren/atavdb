package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.SampleManager;
import org.atavdb.model.SearchFilter;
import org.atavdb.util.SessionManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nick
 */
@RestController
@RequestMapping("/api")
public class RestSampleCountController {

    @GetMapping("/sample-count")
    public ResponseEntity<String> index(HttpSession session) throws Exception {
        SessionManager.clearSession4Search(session);

        SearchFilter filter = new SearchFilter(session);
        SampleManager.init(filter, session);

        int sampleCount = (Integer) session.getAttribute("sampleCount");
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sampleCount\":\"" + String.valueOf(sampleCount) + "\"}");
    }
}
