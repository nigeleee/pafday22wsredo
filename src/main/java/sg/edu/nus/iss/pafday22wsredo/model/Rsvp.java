package sg.edu.nus.iss.pafday22wsredo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rsvp {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String confirmationDate;
    private String comments;

}
