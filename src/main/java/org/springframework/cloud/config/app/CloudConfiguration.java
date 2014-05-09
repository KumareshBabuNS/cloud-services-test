package org.springframework.cloud.config.app;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CloudConfiguration extends AbstractCloudConfig {
    private Map<String, String> serviceNameMap = new HashMap<String, String>();
    
    public CloudConfiguration() {
        boolean isHeroku = System.getenv("DYNO") != null; 
                
        if (isHeroku) {
            serviceNameMap.put("mysql", "CLEARDB_DATABASE");
            serviceNameMap.put("postgres", "HEROKU_POSTGRESQL_ORANGE");
            serviceNameMap.put("mongolab", "MONGOLAB");
            serviceNameMap.put("mongohq", "MONGOHQ");
            serviceNameMap.put("mongosoup", "MONGOSOUP");
            serviceNameMap.put("cloudamqp", "CLOUDAMQP");
            serviceNameMap.put("redistogo", "REDISTOGO");
            serviceNameMap.put("rediscloud", "REDISCLOUD");
        } else {
            serviceNameMap.put("mysql", "mysql-service");
            serviceNameMap.put("postgres", "postgres-service");
            serviceNameMap.put("mongolab", "mongolab-service");
            serviceNameMap.put("cloudamqp", "cloudamqp-service");
            serviceNameMap.put("rediscloud", "rediscloud-service");
        }
    }
    
    @Bean(name="mysql-service")
    public DataSource mysqlDataSource() {
        try {
            return connectionFactory().dataSource(serviceNameMap.get("mysql"));
        } catch (CloudException ex) {
            return null;
        }
    }
    
    @Bean(name="postgres-service")
    public DataSource postgresDataSource() {
        try {        
            return connectionFactory().dataSource(serviceNameMap.get("postgres"));
        } catch (CloudException ex) {
            return null;
        }
    }    

    @Bean(name="mongolab-service")
    public MongoDbFactory mongoLabDbFactory() {
        try {        
            return connectionFactory().mongoDbFactory(serviceNameMap.get("mongolab"));
        } catch (CloudException ex) {
            return null;
        }
    }    

    @Bean(name="mongohq-service")
    public MongoDbFactory mongoHQDbFactory() {
        try {        
            return connectionFactory().mongoDbFactory(serviceNameMap.get("mongohq"));
        } catch (CloudException ex) {
            return null;
        }
    }    

    @Bean(name="mongosoup-service")
    public MongoDbFactory mongoSoupDbFactory() {
        try {        
            return connectionFactory().mongoDbFactory(serviceNameMap.get("mongosoup"));
        } catch (CloudException ex) {
            return null;
        }
    }    

    @Bean(name="cloudamqp-service")
    public ConnectionFactory rabbitConnectionFactory() {
        try {    
            return connectionFactory().rabbitConnectionFactory(serviceNameMap.get("cloudamqp"));
        } catch (CloudException ex) {
            return null;
        }
    }
    
    @Bean(name="rediscloud-service")
    public RedisConnectionFactory redisCloudConnectionFactory() {
        try {
            return connectionFactory().redisConnectionFactory(serviceNameMap.get("rediscloud"));
        } catch (CloudException ex) {
            return null;
        }
    }

    @Bean(name="redistogo-service")
    public RedisConnectionFactory redisToGoConnectionFactory() {
        try {
            return connectionFactory().redisConnectionFactory(serviceNameMap.get("redistogo"));
        } catch (CloudException ex) {
            return null;
        }
    }

}
