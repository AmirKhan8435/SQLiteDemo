package com.sqlite;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sqlite.entity.UserEntity;
import com.sqlite.utils.SqliteDbUtils;

@SpringBootApplication
public class SqliteDbConnectionPocApplication implements CommandLineRunner{

	@Autowired
	SqliteDbUtils sqliteDbUtils;
	
	public static void main(String[] args) {
		SpringApplication.run(SqliteDbConnectionPocApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//getting active connection object to path where db file will be created
		Connection conn = sqliteDbUtils.getConnection("C:/sqlite");
		
		//creating database and table in local db file
		sqliteDbUtils.createDbFileAndTable(conn);
		
		//inserting record in local db file
		sqliteDbUtils.insertRecordToSqliteDb(conn, new UserEntity("Adam", 26));
		
		//testing whether record inserted is there in local db file
		List<UserEntity> allRecList = sqliteDbUtils.getAllRecList(conn);
		for (UserEntity userEntity : allRecList) {
			System.out.println(userEntity.toString());
		}
 }

}
