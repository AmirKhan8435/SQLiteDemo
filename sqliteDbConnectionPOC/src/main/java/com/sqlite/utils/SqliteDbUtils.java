package com.sqlite.utils;

import java.io.File;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.sqlite.entity.UserEntity;

@Configuration
public class SqliteDbUtils {

	private static Logger logger = LoggerFactory.getLogger(SqliteDbUtils.class);

	@Value("${sqlite.dbFileName}")
	private String sqliteDatabaseFileName;
	@Value("${sqlite.defaultDbPath}")
	private String defaultDbPath;

	@Value("${sqlite.query.createTable}")
	private String createTableQuery;

	@Value("${sqlite.query.selectAllRecords}")
	private String selectAllRecordsQuery;

	@Value("${sqlite.query.insertRecord}")
	private String insertRecordQuery;

	/**
	 * 
	 * @param aPath - path where sqlite db file will be created eg."C:/",
	 *              "C:/sqlite", "D:/" etc
	 * @return Open connection object for db file created at given aPath
	 * @throws SQLException
	 */
	public Connection getConnection(String aPath) {
		try {
			logger.info("Started Process getConnection()");
			String path = (null != aPath && !aPath.isEmpty()) ? aPath : defaultDbPath;
			File f1 = new File(path);
			boolean mkdirStat = f1.mkdir();
			if (mkdirStat) {
				logger.info("{} path is created successfully", path);
			} else {
				logger.info("{} path already exists", path);
			}
			String url = "jdbc:sqlite:" + path + "/" + sqliteDatabaseFileName;
			logger.info("Connection to {} successfully created", path);
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			logger.error("Error in getConnection() : {}",e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param conn Open Connection object as a parameter/should be not be null!!!
	 * @throws SQLException
	 * @throws ConnectException
	 */
	private void createNewDatabase(Connection conn) {
		if (conn == null) {
			logger.error("Error in createNewDatabase()");	// we can also throw exception (custom or from exception class which is suitable)
			throw new NullPointerException();
		}
		try {
			DatabaseMetaData meta = conn.getMetaData();
			logger.info("Created Connection : {}", meta.getDriverName());
		} catch (SQLException e) {
			logger.error("Error in createNewDatabase() : {}",e.getMessage());
		}
	}

	/**
	 * 
	 * @param conn
	 * @throws SQLException
	 * @throws ConnectException
	 */
	public void createDbFileAndTable(Connection conn){
		if (conn == null) {
			logger.error("Error in createDbFileAndTable()");	// we can also throw exception (custom or from exception class which is suitable)
			throw new NullPointerException();
		}
		try (Statement stmt = conn.createStatement()) {
			createNewDatabase(conn);
			int status = stmt.executeUpdate(createTableQuery);
			if (status == 0)
				logger.info("Table created successfully if not exists already");
			else
				logger.info("Unable to create Table");
		} catch (SQLException | NullPointerException e) {
			logger.error("Error in createDbAndTable() : {}",e.getMessage());
		}
	}

	/**
	 * 
	 * @param conn
	 * @return List of existing all records from sqlite db file
	 * @throws SQLException
	 * @throws ConnectException
	 */
	public List<UserEntity> getAllRecList(Connection conn){
		if (conn == null) {
			logger.error("Error in getAllRecList()");	// we can also throw exception (custom or from exception class which is suitable)
			throw new NullPointerException();
		}
		try (Statement stmt = conn.createStatement()) {
			logger.info("Started Process getAllRecList()");
			List<UserEntity> fileNameList = new ArrayList<>();
			ResultSet resultSet = stmt.executeQuery(selectAllRecordsQuery);
			while (resultSet.next()) {
				UserEntity obj = new UserEntity();
				obj.setId(resultSet.getInt("id"));
				obj.setFirstName(resultSet.getString("firstName"));
				obj.setAge(resultSet.getInt("age"));
				fileNameList.add(obj);
			}
			logger.info("Completed Process getAllRecList()");
			return fileNameList;
		} catch (SQLException e) {
			logger.error("Error in getAllRecList() : {}",e.getMessage());
		}
		return Collections.emptyList();
	}

	public void insertRecordToSqliteDb(Connection conn, UserEntity user){
		if (conn == null) {
			logger.error("Error in insertRecordToSqliteDb()");	// we can also throw exception (custom or from exception class which is suitable)
			throw new NullPointerException();
		}
		try (PreparedStatement pstmt = conn.prepareStatement(insertRecordQuery)) {
			pstmt.setString(1, user.getFirstName());
			pstmt.setInt(2, user.getAge());
			pstmt.executeUpdate();
			logger.info(" record inserted successfully.");
		} catch (SQLException e) {
			logger.error("Error in insertRecordToSqliteDb() : {}",e.getMessage());
		}
	}
}
