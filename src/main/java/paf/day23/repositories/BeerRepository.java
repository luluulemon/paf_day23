package paf.day23.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static paf.day23.repositories.Queries.*;

@Repository 
public class BeerRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getStyles(){

        List<String> styles = new LinkedList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_STYLES);
        
        while(rs.next()){
            styles.add(rs.getString("style_name"));
        }

        return styles;
    }


    public List<String> getBreweries(String style){

        List<String> breweries = new LinkedList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_BREW, style);

        while(rs.next()){
            breweries.add(rs.getString("name"));
        }

        return breweries;
    }
}
