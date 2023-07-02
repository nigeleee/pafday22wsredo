package sg.edu.nus.iss.pafday22wsredo.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.pafday22wsredo.model.Rsvp;

@Repository
public class RsvpRepo {
    @Autowired
    JdbcTemplate template;

    private final String findAllSQL = "select * from rsvp";
    private final String insertSQL = "insert into rsvp (id, full_name, email, phone, confirmation_date, comments) values (?, ?, ?, ?, ?, ?)";
    private final String findByRSVPSQL = "select * from rsvp where full_name like ?";
    private final String findByEmailSQL = "select * from rsvp where email = ?";
    private final String findByIdSQL = "select * from rsvp where id = ?";
    private final String updateSQL = "update rsvp set full_name = ?, email = ?, phone = ?, confirmation_date = ?, comments = ? where id = ?";
    private final String getCountSQL = "select count(*) from rsvp";
    
    public List<Rsvp> getAllRsvp() {
        
        List<Rsvp> rsvp  = template.query(findAllSQL, BeanPropertyRowMapper.newInstance(Rsvp.class));
        if(rsvp.isEmpty()) {
            throw new NoSuchElementException("No Rsvp found");
        }
            return rsvp;
    }

    public Boolean save(Rsvp rsvp) {
        Rsvp existingRsvp = findByEmail(rsvp.getEmail());
        
        if(existingRsvp != null) {
            return update(rsvp);
        } else {
            return insert(rsvp);
        }

    }
    public int[] saveAll(List<Rsvp> rsvps) {
        List<Object[]> params = rsvps
        .stream()
        .map(r -> new Object[] {r.getFullName(), r.getEmail(), r.getPhone(), r.getConfirmationDate(), r.getComments()})
        .collect(Collectors.toList());

        int added[] = template.batchUpdate(insertSQL, params);

        return added;
    }

    // public List<Rsvp> findByRSVP(String fullName) {
    //     String name = "%" + fullName + "%";
    // return template.query(findByRSVPSQL, new PreparedStatementSetter() {

    //     @Override
    //     public void setValues(PreparedStatement ps) throws SQLException {
    //         ps.setString(1, name);
    //     }} , BeanPropertyRowMapper.newInstance(Rsvp.class));
    
    // }

    public List<Rsvp> findByRSVP(String fullName) {
    String name = "%" + fullName + "%";

    try {
        return template.query(findByRSVPSQL, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, name);
            }
        }, BeanPropertyRowMapper.newInstance(Rsvp.class));
    } catch (EmptyResultDataAccessException e) {
        throw new NoSuchElementException("Error executing findByRSVP query", e);
    }
}

    public Rsvp findByEmail (String email) {
        Rsvp rsvp = template.queryForObject(findByEmailSQL, BeanPropertyRowMapper.newInstance(Rsvp.class), email);

        return rsvp;

    }

    public Rsvp findById (int id) {
        Rsvp rsvp = template.queryForObject(findByIdSQL, BeanPropertyRowMapper.newInstance(Rsvp.class), id);

        return rsvp;

    }

     public Boolean insert(Rsvp rsvp) {
        int result = template.update(insertSQL, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComments());
        
        if (result == 0) {
        throw new NoSuchElementException("RSVP cannot be saved");
         }

         return true;
    }

    public Boolean update(Rsvp rsvp) {
        
        int result = template.update(updateSQL, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComments());

        if (result == 0) {
        throw new NoSuchElementException("RSVP cannot be updated");
        }

        return true;

    }

    public int getCount() {
        int count = template.queryForObject(getCountSQL, Integer.class);

        return count;
    }
    
}
