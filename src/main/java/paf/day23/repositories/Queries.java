package paf.day23.repositories;

public class Queries {
    
    public static final String SQL_GET_STYLES =
        "select distinct(style_name) from styles;";

    public static final String SQL_GET_BREW =
        "select distinct(brew.name) from beers b join styles s on b.style_id = s.id join breweries brew on b.brewery_id = brew.id where style_name = ?;";


}
