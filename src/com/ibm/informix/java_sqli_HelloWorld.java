package com.ibm.informix;

/**
 * Java Sample Application: Connect to Informix using the SQLI protocol and the Informix JDBC driver
 **/

/**
 * Topics
 * 1 Create table
 * 2 Inserts
 * 2.1 Insert a single document into a table
 * 2.2 Insert multiple documents into a table
 * 3 Queries
 * 3.1 Find one document in a table that matches a query condition
 * 3.2 Find documents in a table that match a query condition
 * 3.3 Find all documents in a table
 * 4 Update documents in a table
 * 5 Delete documents in a table
 * 6 Drop a table
 **/

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.informix.jdbc.IfxDriver;

public class java_sqli_HelloWorld {
	
	// To run locally, set the URL here
	// For example: URL = "jdbc:informix-sqli://localhost:9088/testdb:INFORMIXSERVER=informix;USER=myuser;PASSWORD=mypassword";
	public static String URL = "";
		
	// Service name for if credentials are parsed out of the Bluemix VCAP_SERVICES
	public static String SERVICE_NAME = "timeseriesdatabase";
	public static boolean USE_SSL = false;
	
	public static String tableName = "sqlitest3";
	
	public static List<String> everything = new ArrayList<String>();
	
	
	public static void main(String[] args) {
    	
        doEverything();
        
        for (String s : everything) {
        	System.out.println(s);
        }
        
    }

    public static List<String> doEverything() {
		everything.clear();

        Connection connection = null;
        try {
        	parseVcap();
        	
        	//initialize some variables
        	String sql = "";
            PreparedStatement statement = null;
            Properties prop = new Properties();
            //<------------------------------------->
            
            //connect to database
            connection = new IfxDriver().connect(URL, prop);
            if (connection != null)
                everything.add("Connected to: " + URL);
            //<------------------------------------->
            
            everything.add("\nTopics");
            
            
            // 1 Create table
            everything.add("\n1 Create table");
            
            sql = "create table if not exists " + tableName + " (id varchar(255),  value integer)";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            
            everything.add("\tCreate a table named: " + tableName);
            everything.add("\tCreate Table SQL: " + sql);
            //<------------------------------------->
            
            // 2 Inserts
            everything.add("\n2 Inserts");
            
            
            // 2.1 Insert a single document into a table
            everything.add("2.1 Insert a single document into a table");
            
            DataFormat singleInsert = new DataFormat("TestId000", 4);
            sql = "insert into " + tableName + " values(?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, singleInsert.id);
            statement.setInt(2, singleInsert.value);
            statement.executeUpdate();
            statement.close();
            
            everything.add("\tCreate document -> " + singleInsert.toString());
            everything.add("\tSingle Insert SQL: " + sql);
            //<------------------------------------->
            
            //2.2 Insert multiple documents into a table
            everything.add("\n2.2 Insert multiple documents into a table");
            
            List<DataFormat> listOfData = new ArrayList<DataFormat>();
            listOfData.add(new DataFormat("TestId000", 0));
            listOfData.add(new DataFormat("TestId001", 1));
            listOfData.add(new DataFormat("TestId010", 2));
            listOfData.add(new DataFormat("TestId011", 3));
            sql = "insert into " + tableName + " values(?,?)";
            statement = connection.prepareStatement(sql);
            for (DataFormat data : listOfData) {
                statement.setString(1, data.id);
                statement.setInt(2, data.value);
                statement.addBatch();	}
            statement.executeBatch();
            statement.close();
            
            for (DataFormat dataElement : listOfData)
                everything.add("\tCreate Document -> " + dataElement.toString());
            everything.add("\tMultiple Insert SQL: " + sql + " (executed as batch)");
            //<------------------------------------->
            
            //3 Queries
            everything.add("\n3 Queries");
            
            
            //3.1 Find one document in a table that matches a query condition
            everything.add("3.1 Find one document in a table that matches a query condition");
            
            String nameToFind = "TestId000";
            List<DataFormat> dataWithName = new ArrayList<DataFormat>();
            sql = "select * from " + tableName + " where id LIKE '" + nameToFind.trim() + "'";
            statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                dataWithName.add(new DataFormat(rs.getString(1), rs.getInt(2)));
            DataFormat firstDocument = dataWithName.get(0);
            statement.close();
            
            everything.add("\tFind document with ID: " + nameToFind);
            everything.add("\tFirst document with ID -> " + firstDocument.toString());
            everything.add("\tQuery By ID SQL: " + sql);
            //<------------------------------------->
            
            //3.2 Find documents in a table that match a query condition
            everything.add("\n3.2 Find documents in a table that match a query condition");
            
            String nameToFindAll = "TestId000";
            List<DataFormat> dataWithNameAll = new ArrayList<DataFormat>();
            sql = "select * from " + tableName + " where id LIKE '" + nameToFindAll.trim() + "'";
            statement = connection.prepareStatement(sql);
            ResultSet rsAll = statement.executeQuery();
            while (rsAll.next())
                dataWithNameAll.add(new DataFormat(rsAll.getString(1), rsAll.getInt(2)));
            statement.close();
            
            everything.add("\tFind all documents with ID: " + nameToFindAll);
            for (DataFormat foundData : dataWithNameAll)
                everything.add("\tFound Document -> " + foundData.toString());
            
            everything.add("\tQuery All By ID SQL: select * from " + tableName + " where id LIKE '" + nameToFindAll.trim() + "'");
            //<------------------------------------->
            
            //3.3 Find all documents in a table
            everything.add("\n3.3 Find all documents in a table");
            
            List<DataFormat> everythingInTable = new ArrayList<DataFormat>();
            sql = "select * from " + tableName;
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next())
                everythingInTable.add(new DataFormat(rs.getString(1), rs.getInt("value")));
            statement.close();
            
            everything.add("\tFind all documents in table: " + tableName);
            for (DataFormat elementInTable : everythingInTable)
                everything.add("\tFound document -> " + elementInTable.toString());
            everything.add("\tFind All Documents SQL: " + sql);
            //<------------------------------------->
            
            //4 Update documents in a table
            everything.add("\n4 Update documents in a table");
            
            String nameToUpdate = "TestId010";
            int updatedValue = 6;
            statement.close();
            sql = "update " + tableName + " set value = ? where id  = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, updatedValue);
            statement.setString(2, nameToUpdate);
            statement.executeUpdate();
            statement.close();
            
            everything.add("\tDocument to update: " + nameToUpdate);
            everything.add("\tUpdate By ID SQL: " + sql);
            //<------------------------------------->
            
            //5 Delete documents in a table
            everything.add("\n5 Delete documents in a table");
            
            String nameToDelete = "TestId001";
            sql = "delete from " + tableName + " where id like '" + nameToDelete.trim() + "'";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            
            everything.add("\tDelete documents with ID: " + nameToDelete);
            everything.add("\tDelete By ID SQL: " + sql);
            //<------------------------------------->
            
            //6 Drop a table
            everything.add("\n6 Drop a table");
            
            sql = "drop table " + tableName;
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            
            everything.add("\tDrop table: " + tableName);
            everything.add("\tDrop Table SQL: " + sql);
            //<------------------------------------->
            
            //Complete
            everything.add("\nComplete!");

        } catch (Exception e) {
        	String errMessage = "[ERROR] "
                    + (e instanceof SQLException ? " Error Code : "
                            + ((SQLException) e).getErrorCode() : "")
                    + " Message : " + e.getMessage();
        	everything.add(errMessage);
            System.err.println(errMessage);
            e.printStackTrace();
        } finally {
        	if (connection != null) {
        		try {
        			connection.close();
        		} catch (SQLException e) {
        			
        		}
        	}
        }
        
        return everything;
    }

	public static void parseVcap() throws Exception {

		if (URL != null && !URL.equals("")) {
			// If URL is already set, use it as is
			return;
		}
 
		// Otherwise parse URL and credentials from VCAP_SERVICES
		String serviceName = System.getenv("SERVICE_NAME");
		if(serviceName == null || serviceName.length() == 0) {
			serviceName = SERVICE_NAME;
		}
		String vcapServices = System.getenv("VCAP_SERVICES");
		if (vcapServices == null) {
			throw new Exception("VCAP_SERVICES not found in the environment"); 
		}
		StringReader stringReader = new StringReader(vcapServices);
		JsonReader jsonReader = Json.createReader(stringReader);
		JsonObject vcap = jsonReader.readObject();
		System.out.println("vcap: " + vcap);
		if (vcap.getJsonArray(serviceName) == null) {
			throw new Exception("Service " + serviceName + " not found in VCAP_SERVICES");
		}
		if (USE_SSL) {
			URL = vcap.getJsonArray(serviceName).getJsonObject(0)
					.getJsonObject("credentials").getString("java_jdbc_url_ssl");
		} else {
			URL = vcap.getJsonArray(serviceName).getJsonObject(0)
					.getJsonObject("credentials").getString("java_jdbc_url");
		}
		System.out.println(URL);

	}

}

class DataFormat {
    public final String id;
    public final int value;

    public DataFormat(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public String toString() {
        return "id: " + this.id + " value: " + this.value + " ";
    }
}
 