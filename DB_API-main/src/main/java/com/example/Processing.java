package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Exchange;


public class Processing {
	
	private DataSource dataSource;
	   
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void getAirportList(Exchange exchange) throws Exception{
		Map<String,Object>input = exchange.getIn().getBody(Map.class);
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement getBorrowerList = null;
		connection = dataSource.getConnection();
		getBorrowerList = connection.prepareStatement("SELECT * FROM airports;");
		rs = getBorrowerList.executeQuery();
		List<Map<String,String>>resultlist = new ArrayList<Map<String,String>>();
		while (rs.next()) {
			Map<String,String>result = new HashMap<>();
			System.out.println("#####"+rs.getString("Airport"));
			result.put("airport",rs.getString("Airport"));
			result.put("sno",rs.getString("Sno"));
			result.put("initial",rs.getString("Initials"));  
			resultlist.add(result);
	    }
		rs.close();   
		connection.close(); 
		exchange.getIn().setBody(resultlist);
	}



	public void insertFeedback(Exchange exchange) throws Exception{
		// try{
		Map<String,String>input = exchange.getIn().getBody(Map.class);
		Connection connection = null;
		ResultSet rs = null;
		connection = dataSource.getConnection();
		PreparedStatement insertApplication=null;
		insertApplication = connection.prepareStatement("insert into feedback(name,message,email)"+
		"values(?,?,?);");
		String name=input.get("name").toString();
		String message=input.get("message").toString();
		String email= input.get("email").toString();
		insertApplication.setString(1, name.toString());
		insertApplication.setString(2, message.toString());
		insertApplication.setString(3, email.toString());
		// insertApplication.setInt(4, 1);
		connection.setAutoCommit(false);
		int sno=0;
		if (insertApplication.execute()) {
		rs = insertApplication.getResultSet();
		rs.next();
		}else{
			sno = insertApplication.getUpdateCount();
			System.out.println("Sno."+sno);
		}
		Map<String,Object>result = new HashMap<>();	
		result.put("Status","Success");
		insertApplication.close();
		connection.commit();
		connection.close();  
		exchange.getIn().setBody(result);  
	// }
	// catch(Exception e) {
	//   Map<String,String>result = new HashMap<>();	
	//   result.put("Status","Failed");
	//   result.put("Messaage","Unable to insert data");
	//   exchange.getIn().setBody(result);
	// }
	}

	public void searchFlight(Exchange exchange) throws Exception{
		// try{
		Map<String,String>input = exchange.getIn().getBody(Map.class);
		Connection connection = null;
		ResultSet rs = null;
		connection = dataSource.getConnection();
		PreparedStatement insertApplication=null;
		String source=input.get("source").toString();
		// System.out.println("#####"+source);
		String destination=input.get("destination").toString();
		// System.out.println("#####"+destination);
		String date= input.get("date").toString();
		// System.out.println("#####"+date);
		int seat= Integer.parseInt(input.get("seat").toString());
		// System.out.println("#####"+seat);
		insertApplication = connection.prepareStatement("SELECT * FROM flights WHERE source=? AND destination=? AND flightdate=?  AND seats >= ?;");
		insertApplication.setString(1, source.toString());
		insertApplication.setString(2, destination.toString());
		insertApplication.setString(3, date.toString());
		insertApplication.setInt(4, seat);
		// System.out.println("#####");
		rs = insertApplication.executeQuery();
		List<Map<String,Object>>resultlist = new ArrayList<Map<String,Object>>();
		while (rs.next()) {
			Map<String,Object>result = new HashMap<>();
			System.out.println("#####"+rs.getString("source"));
			System.out.println("#####"+rs.getString("destination"));
			System.out.println("#####"+rs.getString("flightdate"));
			System.out.println("#####"+rs.getString("seats"));
			System.out.println("#####"+rs.getString("flightnumber"));
			result.put("source",rs.getString("source"));
			result.put("destination",rs.getString("destination"));
			result.put("date",rs.getString("flightdate"));
			result.put("seat",rs.getInt("seats"));
			result.put("flightNo",rs.getString("flightnumber"));  
			resultlist.add(result);
	    }
		rs.close();   
		connection.close(); 
		exchange.getIn().setBody(resultlist); 
	// }
	// catch(Exception e) {
	//   Map<String,String>result = new HashMap<>();	
	//   result.put("Status","Failed");
	//   result.put("Messaage","Unable to insert data");
	//   exchange.getIn().setBody(result);
	// }
	}

	public void bookTicket(Exchange exchange) throws Exception{
		// try{
		Map<String,String>input = exchange.getIn().getBody(Map.class);
		Connection connection = null;
		ResultSet rs = null;
		connection = dataSource.getConnection();
		PreparedStatement insertApplication=null;
		Connection connection1 = null;
		ResultSet rs1 = null;
		connection1 = dataSource.getConnection();
		PreparedStatement insertApplication1=null;
		insertApplication = connection.prepareStatement("insert into booking(name,age,noofpassenger,date,flightno,source,destination,status)"+
		"values(?,?,?,?,?,?,?,?);");
		String name=input.get("name").toString();
		String age=input.get("age").toString();
		String noofpassenger= input.get("noofpassenger").toString();
		String date= input.get("date").toString();
		String flightno= input.get("flightno").toString();
		String source= input.get("source").toString();
		String destination= input.get("destination").toString();
		insertApplication.setString(1, name.toString());
		insertApplication.setString(2, age.toString());
		insertApplication.setString(3, noofpassenger.toString());
		insertApplication.setString(4, date.toString());
		insertApplication.setString(5, flightno.toString());
		insertApplication.setString(6, source.toString());
		insertApplication.setString(7, destination.toString());
		insertApplication.setString(8, "true");
		connection.setAutoCommit(false);
		if (insertApplication.execute()) {
		rs = insertApplication.getResultSet();
		}
		Map<String,Object>result = new HashMap<>();	
		result.put("Status","Success");
		connection.commit();
		connection.close(); 

		insertApplication1 = connection1.prepareStatement("SELECT MAX( sno ) FROM booking;");
		rs1 = insertApplication1.executeQuery();
            rs1.next();
			// System.out.println("Sno."+rs1.getString(1));
			result.put("TicketNO",rs1.getString(1).toString());
			// System.out.println("Ticket inserted."+result.get("TicketNO"));
			insertApplication1.close();
			// connection1.commit();
			connection1.close();
			insertApplication.close();
		
		exchange.getIn().setBody(result);  
	// }
	// catch(Exception e) {
	//   Map<String,String>result = new HashMap<>();	
	//   result.put("Status","Failed");
	//   result.put("Messaage","Unable to insert data");
	//   exchange.getIn().setBody(result);
	// }
	}

	public void checkStatus(Exchange exchange) throws Exception{
		// try{
		Map<String,String>input = exchange.getIn().getBody(Map.class);
		Connection connection = null;
		ResultSet rs = null;
		connection = dataSource.getConnection();
		PreparedStatement insertApplication=null;
		String ticketno=input.get("ticketNo").toString();
		insertApplication = connection.prepareStatement("SELECT * from booking WHERE sno =?;");
		insertApplication.setString(1, ticketno.toString());
		rs = insertApplication.executeQuery();
		List<Map<String,Object>>resultlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>result = new HashMap<>();
		while (rs.next()) {
			
			result.put("source",rs.getString("source"));
			result.put("destination",rs.getString("destination"));
			result.put("name",rs.getString("name"));
			result.put("noofpassenger",rs.getInt("noofpassenger"));
			result.put("age",rs.getString("age")); 
			result.put("date",rs.getString("date")); 
			result.put("flightno",rs.getString("flightno")); 
			result.put("status",rs.getString("status"));  
			resultlist.add(result);
	    }
		rs.close();   
		connection.close(); 
		exchange.getIn().setBody(result); 
	// }
	// catch(Exception e) {
	//   Map<String,String>result = new HashMap<>();	
	//   result.put("Status","Failed");
	//   result.put("Messaage","Unable to insert data");
	//   exchange.getIn().setBody(result);
	// }
	}
}
