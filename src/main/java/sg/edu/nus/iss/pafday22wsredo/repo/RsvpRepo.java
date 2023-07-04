package sg.edu.nus.iss.pafday22wsredo.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.pafday22wsredo.model.Rsvp;

@Repository
public class RsvpRepo {

    @Autowired
    JdbcTemplate template;

    private final String findAllSQL = "select * from Rsvp";
    private final String findByIdSQL = "select * from Rsvp where id = ?";
    private final String findByEmailSQL = "select * from Rsvp where email = ?";
    private final String findRsvpByNameSQL = "select * from Rsvp where full_name like ?";
    private final String findRsvpCountSQL = "select count(*) from Rsvp";
    private final String updateSQL = "update Rsvp set full_name, email, phone, confirmation_date, comments values where id = ?";
    private final String insertSQL = "insert into Rsvp (full_name, email, phone, confirmation_date, comments) values = ?,?,?,?,?";

    //get Rsvps
    public List<Rsvp> findAll() {
        List<Rsvp> rsvp = template.query(findAllSQL, BeanPropertyRowMapper.newInstance(Rsvp.class));

        if (rsvp.isEmpty()) {
            throw new NoSuchElementException("Rsvp List is empty");
        }

        return rsvp;
    }

    //find Rsvp by id
    public Rsvp findById(int id) {
        try {
            return template.queryForObject(findByIdSQL, BeanPropertyRowMapper.newInstance(Rsvp.class), id);    
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Rsvp with id " + id + " not found");
        }

    }

    //find Rsvp by email
    public Rsvp findByEmail(String email) {
        try {
            return template.queryForObject(findByEmailSQL, BeanPropertyRowMapper.newInstance(Rsvp.class), email);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Rsvp with email " + email + " not found");
        }
    }

    //create custom String name for query
    public List <Rsvp> findRsvpByName(String fullName) {
        
        String name = "%" + fullName + "%";
        
        try {
            return template.query(findRsvpByNameSQL, new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, name);
                }    

            }, BeanPropertyRowMapper.newInstance(Rsvp.class));

        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Rsvp with name " + name + " not found");
        }

    }
    
    //get count
    public int findRsvpCount() {

        int count = template.queryForObject(findRsvpCountSQL, BeanPropertyRowMapper.newInstance(Integer.class));
        
        return count;
    }

    //update Rsvp
    public Boolean update(Rsvp rsvp) {
        int result = template.update(updateSQL, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComments(), rsvp.getId());

        return result>0 ? true : false;
    }

    //insert Rsvp
    public Integer insert(Rsvp rsvp) {

        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator psc = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, new String[] {"id"});
                ps.setString(1, rsvp.getFullName());
                ps.setString(2, rsvp.getEmail());
                ps.setString(3, rsvp.getPhone());
                ps.setString(4, rsvp.getConfirmationDate());
                ps.setString(5, rsvp.getComments());
                return ps;
            }
            
        };

        template.update(psc, key);
        return key.getKey().intValue();
    }

    public Boolean save(Rsvp rsvp) {
        Rsvp existingRsvp = findById(rsvp.getId());

        if(existingRsvp != null) {
            return update(rsvp);
        } else {
            Integer newId = insert(rsvp);
            return newId != null && newId > 0;
        }
    }

}
