package sg.edu.nus.iss.pafday22wsredo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import sg.edu.nus.iss.pafday22wsredo.model.Rsvp;
import sg.edu.nus.iss.pafday22wsredo.repo.RsvpRepo;

@RestController
@RequestMapping("/api")
public class RsvpController {

    @Autowired
    RsvpRepo repo;

    // display all rsvps
    @GetMapping(path = "/rsvps", produces = "application/json")
    public ResponseEntity<?> getAllRsvps() {
        try {
            List<Rsvp> rsvps = repo.findAll();
            return ResponseEntity.ok().body(rsvps);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Error\": \"" + e.getMessage() + "\"}");
        }
    }

    // search rsvp by name or parts of
    // /rsvp?q=fred
    // return 404 if cannot find
    @GetMapping(path = "/rsvp", produces = "application/json")
    public ResponseEntity<?> findRsvpByName(@RequestParam("q") String fullName) {
        try {
            List<Rsvp> rsvps = repo.findRsvpByName(fullName);
            return ResponseEntity.ok().body(rsvps);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Error\": \"" + e.getMessage() + "\"}");
        }
    }

    // add new rsvp or overwrite
    // return 201 if successful
    @PostMapping(path = "/rsvp", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<?> addOrUpdateRsvp(@RequestBody Rsvp rsvp) {
        try {
            Rsvp existingRsvp = repo.findById(rsvp.getId());

            if (existingRsvp != null) {
                existingRsvp.setFullName(rsvp.getFullName());
                existingRsvp.setEmail(rsvp.getEmail());
                existingRsvp.setPhone(rsvp.getPhone());
                existingRsvp.setConfirmationDate(rsvp.getConfirmationDate());
                existingRsvp.setComments(rsvp.getComments());

                repo.save(existingRsvp);

                return ResponseEntity.status(HttpStatus.CREATED).build();

            } else {

                repo.save(rsvp);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }

        } catch (DataAccessException e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // update rsvp
    // rsvp/fred@gmail.com
    // return 201 if successful
    @PutMapping(path = "rsvp/{email}", produces = "application/json")
    public ResponseEntity<?> update(@RequestParam("email") String email, @RequestBody Rsvp rsvp) {
        try {
            Rsvp existingRsvp = repo.findByEmail(email);

            if (existingRsvp != null) {
                existingRsvp.setFullName(rsvp.getFullName());
                existingRsvp.setEmail(rsvp.getEmail());
                existingRsvp.setPhone(rsvp.getPhone());
                existingRsvp.setConfirmationDate(rsvp.getConfirmationDate());
                existingRsvp.setComments(rsvp.getComments());

                repo.save(existingRsvp);
                return ResponseEntity.status(HttpStatus.CREATED).build();

            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        }

    }
    // get count
    // return 201 if successful
    @GetMapping(path = "/rsvp/count", produces = "application/json")
    public ResponseEntity<String> findRsvpCount() {

        int count = repo.findRsvpCount();

        String result = "{\"Count\": \"" + count + "\"}";

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

}
