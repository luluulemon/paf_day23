package paf.day23.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import paf.day23.models.Breweries;


@Configuration  
public class RedisConfig {
    
    //Take values to configure redis from application.properties

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    @Scope("singleton")
    public RedisTemplate<String, Object> redisTemplate(){
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort.get());
        config.setPassword(redisPassword);//set online DB address configurations for which database to link to

        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient); //get the address and socket creator together
        jedisFac.afterPropertiesSet(); //validate configurations and finalize properties to create template
        // logger.info("redis port > {redisHost} {redisPort}", redisHost, redisPort);
        RedisTemplate<String, Object> template = new RedisTemplate<>(); //instantiate the empty redis template or local database
        template.setConnectionFactory(jedisFac); //populate the data from the redis database online
        template.setKeySerializer(new StringRedisSerializer()); //set keys as string, allows for serialization as string

        Jackson2JsonRedisSerializer<Breweries> jackson2JsonJsonSerializer = new Jackson2JsonRedisSerializer<Breweries>(Breweries.class);
        //RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(getClass().getClassLoader()); //choose type of serialization as object to allow for all objects
        template.setValueSerializer(jackson2JsonJsonSerializer);//set the template's serialization as above
        return template;
    }
}

