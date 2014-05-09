package org.springframework.cloud.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.Cloud;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(headers="Accept=application/json", value="/tests")
public class TestController {

	@Autowired(required=false) @Qualifier("postgres-service") private DataSource postgresDataSource;

	@Autowired(required=false) @Qualifier("mysql-service") private DataSource mysqlDataSource;
	
	@Autowired(required=false) @Qualifier("cloudamqp-service") private ConnectionFactory connectionFactory;
	
	@Autowired(required=false) @Qualifier("mongolab-service") private MongoDbFactory mongoLabDbFactory;
	@Autowired(required=false) @Qualifier("mongohq-service") private MongoDbFactory mongoHqDbFactory;
	@Autowired(required=false) @Qualifier("mongosoup-service") private MongoDbFactory mongoSoupDbFactory;
	
	@Autowired(required=false) @Qualifier("rediscloud-service") private RedisConnectionFactory redisCloudConnectionFactory;
	@Autowired(required=false) @Qualifier("redistogo-service") private RedisConnectionFactory redisToGoConnectionFactory;
	
	@Autowired Cloud cloud;
	
	private static TestResult result(boolean success) {
		return new TestResult(success);
	}
	
	private static TestResult failure(Throwable ex) {
		ex.printStackTrace(System.out);
		return new TestResult(ex);
	}
	
	@RequestMapping("/postgres")
	@ResponseBody
	public TestResult testPostgresConnectivity() {
		cloud.getServiceInfos();
		try {
			Connection connection = postgresDataSource.getConnection();
			return result(connection != null);
		} catch (Exception e) {
			return failure(e);
		}
	}
	
	@RequestMapping("/mysql")
	@ResponseBody
	public TestResult testMysqlConnectivity() {
		try {
			Connection connection = mysqlDataSource.getConnection();
			return result(connection != null);
		} catch (Exception e) {
			return failure(e);
		}
	}

    @RequestMapping("/cloudamqp")
    @ResponseBody
    public TestResult testAmqpConnectivity() {
        try {
            org.springframework.amqp.rabbit.connection.Connection connection = connectionFactory.createConnection();
            return result(connection != null);
        } catch (Exception e) {
            return failure(e);
        }
    }
	
	@RequestMapping("/mongolab")
	@ResponseBody
	public TestResult testMongoLabConnectivity() {
		try {
			Set<String> collectionNames = mongoLabDbFactory.getDb().getCollectionNames();
			return result(collectionNames != null);
		} catch (Exception e) {
			return failure(e);
		}
	}

    @RequestMapping("/mongohq")
    @ResponseBody
    public TestResult testMongoHqConnectivity() {
        try {
            Set<String> collectionNames = mongoHqDbFactory.getDb().getCollectionNames();
            return result(collectionNames != null);
        } catch (Exception e) {
            return failure(e);
        }
    }

    @RequestMapping("/mongosoup")
    @ResponseBody
    public TestResult testMongoSoupConnectivity() {
        try {
            Set<String> collectionNames = mongoSoupDbFactory.getDb().getCollectionNames();
            return result(collectionNames != null);
        } catch (Exception e) {
            return failure(e);
        }
    }

    @RequestMapping("/rediscloud")
    @ResponseBody
    public TestResult testRedisCloudConnectivity() {
        try {
            RedisConnection connection = redisCloudConnectionFactory.getConnection();
            return result(connection != null);
        } catch (Exception e) {
            return failure(e);
        }
    }
    
    @RequestMapping("/redistogo")
    @ResponseBody
    public TestResult testRedisToGoConnectivity() {
        try {
            RedisConnection connection = redisToGoConnectionFactory.getConnection();
            return result(connection != null);
        } catch (Exception e) {
            return failure(e);
        }
    }
    
	
	static class TestResult {
		public boolean success;
		public String message;
		public String failureDetails;
		
		public TestResult(boolean success) {
			this.success = success;
		}
		
		public TestResult(Throwable ex) {
			this.message = ex.getMessage();
			StringWriter writer = new StringWriter();
			ex.printStackTrace(new PrintWriter(writer));
			this.failureDetails = writer.toString();
			this.success = false;
		}
	}
}
