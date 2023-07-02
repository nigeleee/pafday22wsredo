package sg.edu.nus.iss.pafday22wsredo.model;

import java.sql.Date;

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
    private Date confirmationDate;
    private String comments;

}
