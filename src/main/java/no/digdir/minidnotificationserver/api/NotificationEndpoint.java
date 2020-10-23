package no.digdir.minidnotificationserver.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationEndpoint {

    @GetMapping("/send")
    @PreAuthorize("isAuthenticated()") // must require admin auth
    public ResponseEntity<String> send() {
        // call service layer and send message to given person_identifier
        return ResponseEntity.ok("{\"status\": \"Great success!\"}");
    }

}