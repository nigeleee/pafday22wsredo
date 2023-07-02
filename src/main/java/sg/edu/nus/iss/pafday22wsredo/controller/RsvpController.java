package sg.edu.nus.iss.pafday22wsredo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.pafday22wsredo.model.Rsvp;
import sg.edu.nus.iss.pafday22wsredo.repo.RsvpRepo;

@RestController
@RequestMapping("/api")

public class RsvpController {
    
    @Autowired
    RsvpRepo repo;
//get all rsvps
@GetMapping(path="/rsvps", produces="application/json")
public ResponseEntity<?> getAllRsvp(){
    try {
        List<Rsvp> rsvp = repo.getAllRsvp();
        return ResponseEntity.ok().body(rsvp);
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
}    

//find by name
// api/rsvp?q=fullName
// api/rsvp/name?name=am
@GetMapping(path="/rsvp", produces="application/json")
public ResponseEntity<?> findByRSVP(@RequestParam("q") String fullName) {
    try {
        List<Rsvp> rsvp = repo.findByRSVP(fullName);
        return ResponseEntity.ok().body(rsvp);
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
}

// // // update
// @PostMapping(path="/rsvp", consumes="application/json")
// public ResponseEntity<Rsvp> save(@RequestBody Rsvp rsvp, int id) {
    
//     Rsvp existingRsvp = repo.findById(id);
//         if (existingRsvp == null) {
//             rsvp.setId(existingRsvp.getId());

//             repo.save(rsvp);
//             return ResponseEntity.status(HttpStatus.CREATED).build();
//         }
//         //replaces existing Rsvp with rsvp by id
//             return ResponseEntity.notFound().build();


// }
@PostMapping(path = "/rsvp", produces = "application/json")
public ResponseEntity<?> addOrUpdateRsvp(@ModelAttribute Rsvp rsvp) {
    try {
        Boolean isUpdated = repo.save(rsvp);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.CREATED).body(rsvp);
        } else {
            // Handle the case where the RSVP couldn't be saved or updated
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add/update RSVP");
        }
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RSVP not found");
    }
}

 @PutMapping(path = "/rsvp/{email}", produces = "application/json")
    public ResponseEntity<?> updateRsvpByEmail(@PathVariable("email") String email, @ModelAttribute Rsvp rsvp) {
        try {
            Rsvp existingRsvp = repo.findByEmail(email);
            if (existingRsvp == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RSVP not found");
            }
            // Update the email of the existing RSVP
            existingRsvp.setEmail(rsvp.getEmail());
            Boolean isUpdated = repo.update(existingRsvp);
            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.CREATED).body(existingRsvp);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update RSVP");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RSVP not found");
        }
    }

//count
@GetMapping(path="/rsvps/count", produces="application/json")
public ResponseEntity<Integer> getRsvpCount(){
    int result = repo.getCount();

    return ResponseEntity.status(HttpStatus.OK).body(result);
}

}


