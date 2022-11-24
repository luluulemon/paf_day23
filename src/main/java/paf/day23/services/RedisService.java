package paf.day23.services;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import paf.day23.models.Breweries;

@Service
public class RedisService {
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    

    public Boolean hasBrew(String style){
        return redisTemplate.hasKey(style);
    }


    public List<String> getBrew(String style){

        Breweries breweries = (Breweries) redisTemplate.opsForValue().get(style);

        return breweries.getBreweryList();
    }


    public void saveBrew(String style, List<String> brewList){
        // create Breweries object
        Breweries breweries = new Breweries();
        breweries.setBreweryList(brewList);
        breweries.setStyle(style);
        
        // save Brew to cache: set time out duration to be ten minutes
        redisTemplate.opsForValue().set(breweries.getStyle(), breweries, Duration.ofMinutes(10));
    }
}
