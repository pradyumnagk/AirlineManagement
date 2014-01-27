package airlineSystem;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.misc.BASE64Encoder;
import beans.Employee;
import beans.FlightDetails;
import beans.MessageConstants;
import beans.Person;
import beans.Reservation;
import beans.Traveller;
import connection.EstablishConnection;

public class ModelController
{
	/*
	 * Added by Darshan -- Encrypts the input String (Password)
	 */
	public static synchronized String encrypt(String plaintext) throws Exception
	{
		MessageDigest msgDigest = null;
		String algorithm = "SHA";
		String encoding = "UTF-8";
		String hashValue = null;
		try
		{
			msgDigest = MessageDigest.getInstance(algorithm);
			msgDigest.update(plaintext.getBytes(encoding));
			byte rawByte[] = msgDigest.digest();
			hashValue = (new BASE64Encoder()).encode(rawByte);

		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("No Such Algorithm Exists");
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("The Encoding Is Not Supported");
		}
		return hashValue;
	}

	/*
	 * Added by Darshan -- Returns the list of valid states
	 */
	public String[] fetchStateList()
	{
		String[] stateList = null;
		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);

			String query = "SELECT stateName from state_info";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			resultSet.last();
			int count = resultSet.getRow();
			resultSet.beforeFirst();

			if (count > 0)
			{
				stateList = new String[count];
				int i = 0;

				while (resultSet.next())
				{
					stateList[i++] = resultSet.getString(1);
				}

			}

			return stateList;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return stateList;
	}

	/*
	 * Added by Darshan -- Performs the Login operation
	 */
	public String login(String userName, String password)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);

			String query = "SELECT PASSWORD, ROLEID, FIRSTNAME FROM PERSON WHERE EMAILID = ?";

			PreparedStatement prepStmt = connection.prepareStatement(query);

			prepStmt.setString(1, userName);
			ResultSet result = prepStmt.executeQuery();

			if (!result.next())
			{
				message = "Invalid Login";
				return message;
			}
			else
			{
				String encryptedPassword = encrypt(password);
				String savedPassword = result.getString("password");
				String firstName = result.getString("firstName");
				int roleID = result.getInt("roleID");

				if (encryptedPassword.equals(savedPassword))
				{
					message = "Success," + firstName + "," + roleID;
					connectionClass.endConnection(connection);
					System.out.println("message " + message);
					return message;
				}
				else
				{
					message = "Invalid Login";
					connectionClass.endConnection(connection);
					return message;
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return message;
	}

	/*
	 * Added by Darshan -- Registers a new customer
	 */
	public String registerCustomer(Person person)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);

			String emailID = person.getEmailID();

			String query1 = "Select count(1) from person where emailID = ?";
			PreparedStatement preparedStmt = connection.prepareStatement(query1);

			preparedStmt.setString(1, emailID);
			ResultSet result1 = preparedStmt.executeQuery();

			while (result1.next())
			{
				if (result1.getInt(1) > 0)
				{
					message = "This email ID already exists. ";
					return message;
				}
			}

			String query2 = "Insert into person (firstName, lastName, address, city, zipCode, dateOfBirth, roleID, emailID, password, state) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStat2 = connection.prepareStatement(query2);

			preparedStat2.setString(1, person.getFirstName());
			preparedStat2.setString(2, person.getLastName());
			preparedStat2.setString(3, person.getAddress());
			preparedStat2.setString(4, person.getCity());
			preparedStat2.setString(5, person.getZipCode());
			preparedStat2.setString(6, person.getDateOfBirth());
			preparedStat2.setInt(7, person.getRoleID());

			preparedStat2.setString(8, person.getEmailID());
			preparedStat2.setString(9, encrypt(person.getPassword()));
			preparedStat2.setString(10, person.getState());

			preparedStat2.executeUpdate();
			message = "Successfully Registered!";
			connectionClass.endConnection(connection);
			//to update customer that he/she has been added
//			sendUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			message = "Some error while registering Customer!";
		}

		return message;
	}

	/*
	 * Added by Darshan -- Deletes an existing user
	 */
	public String deleteCustomer(String emailID)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);

			String query = "delete from person a, traveller b where a.uniqueID = b.uniqueID and a.emailID = ?";

			PreparedStatement prepStmt = connection.prepareStatement(query);

			prepStmt.setString(1, emailID);

			int count = prepStmt.executeUpdate();

			if (count > 0)
				message = "Customer Deleted!!";
			else
				message = "Customer could not be deleted. Please try again.";
			connectionClass.endConnection(connection);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return message;
	}

	/*
	 * Code added by Pradyumna Get all reservation of USER OR THE SYSTEM
	 */
	public Reservation[] getAllReservations(String userId)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		Reservation[] tickets = null;

		String CountQuery = "select count(*) from reservation ";
		if (userId != null)
		{
			CountQuery += " where emailId=?;";
		}
		else
		{
			CountQuery += ";";
		}

		String qry = "select * from reservation ";
		if (userId != null)
		{
			qry += " where emailId=?;";
		}
		else
		{
			qry += ";";
		}
		try
		{
			pStmt = con.prepareStatement(CountQuery);
			if (userId != null)
			{
				pStmt.setString(1, userId);
			}
			rs = pStmt.executeQuery();
			if (rs.next())
			{
				tickets = new Reservation[rs.getInt(1)];
				// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
				pStmt = con.prepareStatement(qry);
				pStmt.setString(1, userId);
				rs = pStmt.executeQuery();
				int i = 0;
				while (rs.next())
				{
					Reservation ticket = new Reservation();
					ticket.setReservationId(rs.getInt(1));
					ticket.setFlightNumber(rs.getInt(2));
					ticket.setAirlineName(rs.getString(3));
					ticket.setSource(rs.getString(4));
					ticket.setDestination(rs.getString(5));
					ticket.setNumberOfSeats(rs.getInt(6));
					tickets[i] = ticket;
					i++;
				}
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return tickets;
	}

	/*
	 * Added by Pradyumna-- Reserves the flight ticket
	 */
	public String reserveTicket(FlightDetails jDetails, Traveller travellerInfo)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		
		String updateFlightQry = "update flight_details set noOfSeats=noOfSeats-? where flightNumber=?;";
		String sql = "insert into  reservation(flightNumber,airlineName,source,destination,noOfSeats,emailId) values(?,?,?,?,?,?);";
		String updateTravellerQuery = "update traveller t, person p set t.nationality=?,t.passportId=? where t.uniqueID=p.uniqueID and p.emailid=?;";
		try
		{
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, jDetails.getFlightNumber());
			pStmt.setString(2, jDetails.getAirlineName());
			pStmt.setString(3, jDetails.getSource());
			pStmt.setString(4, jDetails.getDestination());
			pStmt.setInt(5, jDetails.getNumberOfSeats());
			pStmt.setString(6, travellerInfo.getCustomerId());
			pStmt.execute();
			
			pStmt = con.prepareStatement(updateTravellerQuery);
			pStmt.setString(1, travellerInfo.getNationality());
			pStmt.setString(2, travellerInfo.getPassportNo());
			pStmt.setString(3, travellerInfo.getCustomerId());
			pStmt.executeUpdate();
			
			pStmt = con.prepareStatement(updateFlightQry);
			pStmt.setInt(1, jDetails.getNumberOfSeats());
			pStmt.setInt(2, jDetails.getFlightNumber());
			pStmt.executeUpdate();
			con.close();
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return MessageConstants.RESERVED_SUCCESSFULLY;
	}

	/**
	 * @Author : Pradyumna
	 * @param reservationId
	 * @return
	 */
	public String cancelTicket(String UserId, int reservationId)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		String updateFlightQry = "update flight_details set noOfSeats=noOfSeats+? where flightNumber=?;";
		String getSeatsCnt = " select flightNumber,noOfSeats from reservation where where emailid=? and reservationid=?;";
		String query = "delete from reservation where emailid=? and reservationid=?;";
		try
		{	
			pStmt = con.prepareStatement(getSeatsCnt);
			pStmt.setString(1, UserId);
			pStmt.setInt(2, reservationId);
			rs = pStmt.executeQuery();
			if(rs.next()){
				
				pStmt = con.prepareStatement(query);
				pStmt.setString(1, UserId);
				pStmt.setInt(2, reservationId);
				pStmt.executeUpdate();
				
				pStmt = con.prepareStatement(updateFlightQry);
				pStmt.setInt(1, rs.getInt(2));
				pStmt.setInt(2, rs.getInt(1));
				pStmt.executeUpdate();
				
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();

		}

		return MessageConstants.CANCELED_SUCCESSFULLY;
	}
	
	public FlightDetails[] searchFlight(FlightDetails flight){
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		Statement stmt = null;
		ResultSet result = null;
		boolean where = false;
		String countPartOfQry = "Select count(*) ";
		String selectPartOfQry = "Select * ";
		
		String searchFlightQry = "from flight_details ";
		FlightDetails[] resultFlights = null;
		
		if(flight.getFlightNumber()>0){
			searchFlightQry += "where flightNumber="+flight.getFlightNumber();
			where = true;
		}
		if(flight.getAirlineName()!=null){
			if(!where){
				searchFlightQry+="where airlineName='"+flight.getAirlineName()+"'";
				where = true;
			}else{
				searchFlightQry +="and airlineName='"+flight.getAirlineName()+"'";
			}
		}
		
		if(flight.getSource()!=null){
			if(!where){
				searchFlightQry+="where source='"+flight.getSource()+"'";
				where = true;
			}else{
				searchFlightQry +="and source='"+flight.getSource()+"'";
			}
		}
		
		if(flight.getDestination()!=null){
			if(!where){
				searchFlightQry+="where destination='"+flight.getDestination()+"'";
				where = true;
			}else{
				searchFlightQry +="and destination='"+flight.getDestination()+"'";
			}
		}
		
		if(flight.getFlightTime()!=null){
			if(!where){
				searchFlightQry+="where flightTime='"+flight.getFlightTime()+"'";
				where = true;
			}else{
				searchFlightQry +="and flightTime='"+flight.getFlightTime()+"'";
			}
		}
		searchFlightQry +=";";
		
		try{
			//First get the count of the matching flights
			stmt = con.createStatement();
			result = stmt.executeQuery(countPartOfQry+searchFlightQry);
			int i =0;
			if(result.next() && result.getInt(1)>0){
				resultFlights = new FlightDetails[result.getInt(1)];

				//Now get the entire flight search results
				stmt = con.createStatement();
				result = stmt.executeQuery(selectPartOfQry+searchFlightQry);
				while(result.next()){
					FlightDetails newFlight = new FlightDetails();
					newFlight.setFlightNumber(result.getInt(1));
					newFlight.setAirlineName(result.getString(2));
					newFlight.setSource(result.getString(3));
					newFlight.setDestination(result.getString(4));
					newFlight.setNumberOfSeats(result.getInt(5));
					newFlight.setFlightTime(result.getString(6));
					resultFlights[i] = newFlight;
					i++;
				}
				//CREW ID NEED NOT BE SHOWN
			}

		}catch(SQLException se){
			return resultFlights;
		}
		
		return resultFlights;
	}
	/*
	 * Added by Pradyumna -- Retruns the available flights for source and
	 * destination
	 */
	public FlightDetails[] searchFlightForSourceAndDest(String source, String destination,String flightTime)
	{

		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		String flighSearchQry = null;
		String countQuery = null;
		ResultSet result = null;
		FlightDetails[] flights = null;

		if (source == null || destination == null || flightTime==null)
		{
			countQuery = " select count(*) from flight_details;";
			flighSearchQry = "select * from flight_details;";
		}
		else
		{
			countQuery = " select count(*) from flight_details where source=? and destination=? and flightTime=?;";
			flighSearchQry = "select * from flight_details where source=? and destination=? and flightTime=?;";
		}

		try
		{

			pStmt = con.prepareStatement(countQuery);
			if (source != null && destination != null && flightTime!=null)
			{
				pStmt.setString(1, source);
				pStmt.setString(2, destination);
				pStmt.setString(3, flightTime);
			}
			result = pStmt.executeQuery();
			if (result.next())
			{
				flights = new FlightDetails[result.getInt(1)];
				// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4

				pStmt = con.prepareStatement(flighSearchQry);
				if (source != null && destination != null && flightTime!=null)
				{
					pStmt.setString(1, source);
					pStmt.setString(2, destination);
					pStmt.setString(3, flightTime);
				}
				result = pStmt.executeQuery();
				int i = 0;
				while (result.next())
				{
					FlightDetails flight = new FlightDetails();
					flight.setFlightNumber(result.getInt(1));
					flight.setAirlineName(result.getString(2));
					flight.setSource(result.getString(3));
					flight.setDestination(result.getString(4));
					flight.setNumberOfSeats(result.getInt(5));
					flight.setCrewId(result.getInt(6));
					flights[i] = flight;
					i++;
				}
			}

		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return flights;
	}

	/**
	 * @Author : Pradyumna
	 * @param reservationID
	 * @return
	 */
	public Reservation issueTicket(String userId, int reservationID)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		ResultSet result = null;

		Reservation ticket = null;
		String qry = "select * from reservation where emailId=? and reservationID=?";
		try
		{
			pStmt = con.prepareStatement(qry);
			pStmt.setString(1, userId);
			pStmt.setInt(2, reservationID);
			result = pStmt.executeQuery();
			if (result.next())
			{
				ticket = new Reservation();
				ticket.setReservationId(result.getInt(1));
				ticket.setFlightNumber(result.getInt(2));
				ticket.setAirlineName(result.getString(3));
				ticket.setSource(result.getString(4));
				ticket.setDestination(result.getString(5));
				ticket.setNumberOfSeats(result.getInt(6));
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return ticket;
	}

	/*
	 * Added by **** Returns the travellers matching the search criteria
	 */
	public Traveller[] searchTravelers(int travelerID)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		ResultSet result = null;

		Traveller[] travelers = new Traveller[1];
		
		System.out.println("Entering searchquery");
		try
		{
			if (travelerID>0)
			{
				String travelerSearchQry = "SELECT person.firstName, person.lastName, person.state, traveller.passportID, traveller.nationality,traveller.travelerID From (person JOIN traveller ON person.uniqueID=traveller.uniqueID) WHERE travelerID=?;";
				pStmt = con.prepareStatement(travelerSearchQry);
				pStmt.setInt(1, travelerID);
			}
			result = pStmt.executeQuery();
			if (result.last())
			{
				travelers = new Traveller[result.getRow()];
				result.beforeFirst();
			}
			int i=0;
			while (result.next())
			{
				travelers[i] = new Traveller();
				travelers[i].setFirstName(result.getString(1));
				travelers[i].setLastName(result.getString(2));
				travelers[i].setState(result.getString(3));
				travelers[i].setPassportNo(result.getString(4));
				travelers[i].setNationality(result.getString(5));
				travelers[i].setCustomerId(result.getString(6));
				i++;
			}

		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return travelers;
	}

	/*
	 * Added by Abinaya -- Returns the list of employees matching the search
	 * criteria
	 */
	public Employee[] searchEmployeeForID(int empID, String workDesc, String hireDate)
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		Employee[] empArray = new Employee[1];
		String employeeSearchQueryID;
		String employeeSearchQueryDesc;
		String employeeSearchQueryDate;

		try
		{
			if (empID != 0)
			{
				employeeSearchQueryID = "select * from employee where employeeID=?;";
				pStmt = con.prepareStatement(employeeSearchQueryID);
				pStmt.setInt(1, empID);

			}
			else if (null != workDesc)
			{
				employeeSearchQueryDesc = "select * from employee where workDescription=?;";
				pStmt = con.prepareStatement(employeeSearchQueryDesc);
				pStmt.setString(1, workDesc);
			}
			else if (null != hireDate)
			{
				employeeSearchQueryDate = "select * from employee where hireDate=?;";
				pStmt = con.prepareStatement(employeeSearchQueryDate);
				pStmt.setString(1, hireDate);
			}
			ResultSet result = null;

			result = pStmt.executeQuery();

			if (result.last())
			{
				empArray = new Employee[result.getRow()];
				result.beforeFirst();
			}

			int i = 0;
			while (result.next())
			{
				empArray[i] = new Employee();
				empArray[i].setEmployeeID(result.getInt(1));
				empArray[i].setUniqueID(result.getInt(2));
				empArray[i].setWorkDescription(result.getString(3));
				empArray[i].setPosition(result.getString(4));
				empArray[i].setHireDate(result.getString(5));
				i++;
			}

		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return empArray;
	}

	/*
	 * Added by Darshan -- Adds a new employee to the system
	 */
	public String addEmployee(Employee employee)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);

			Person person = new Person();
			person.setFirstName(employee.getFirstName());
			person.setLastName(employee.getLastName());
			person.setAddress(employee.getAddress());
			person.setCity(employee.getCity());
			person.setDateOfBirth(employee.getDateOfBirth());
			person.setEmailID(employee.getEmailID());
			person.setPassword(employee.getPassword());
			person.setState(employee.getState());
			person.setZipCode(employee.getZipCode());
			person.setRoleID(employee.getRoleID());
			String emailID = person.getEmailID();

			String query1 = "Select count(1) from person where emailID = ?";
			PreparedStatement preparedStmt = connection.prepareStatement(query1);

			preparedStmt.setString(1, emailID);
			ResultSet result1 = preparedStmt.executeQuery();

			while (result1.next())
			{
				if (result1.getInt(1) > 0)
				{
					message = "This email ID already exists. ";
					return message;
				}
			}

			String query2 = "Insert into person (firstName, lastName, address, city, zipCode, dateOfBirth, roleID, emailID, password, state) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStat2 = connection.prepareStatement(query2);

			preparedStat2.setString(1, person.getFirstName());
			preparedStat2.setString(2, person.getLastName());
			preparedStat2.setString(3, person.getAddress());
			preparedStat2.setString(4, person.getCity());
			preparedStat2.setString(5, person.getZipCode());
			preparedStat2.setString(6, person.getDateOfBirth());
			preparedStat2.setInt(7, person.getRoleID());

			preparedStat2.setString(8, person.getEmailID());
			preparedStat2.setString(9, encrypt(person.getPassword()));
			preparedStat2.setString(10, person.getState());

			preparedStat2.executeUpdate();

			try
			{
				String query = "update employee , person set workDescription = ? , position = ? , hiredate= ? where emailID = ? and employee.uniqueID = person.uniqueID";

				System.out.println("In MC " + employee.getWorkDescription());
				PreparedStatement prepStmt = connection.prepareStatement(query);
				prepStmt.setString(1, employee.getWorkDescription());
				prepStmt.setString(2, employee.getPosition());

				prepStmt.setString(3, "10/10/10");
				prepStmt.setString(4, employee.getEmailID());

				int count = prepStmt.executeUpdate();
				if (count > 0)
					message = "Employee added successfully to system";
				else
					message = "Error while adding a new employee!";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				connection.rollback();
				message = "Some error while registering Customer!";
			}

			connectionClass.endConnection(connection);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			message = "Some error while registering Customer!";
		}
		return message;
	}
	
	/**
	 * @function: listAllReservations
	 * @description: Lists all the reservations known by the system
	 * @author: Rushabh Jain
	 * @param: None
	 * @return: List of Reservation
	 */
	public Reservation[] listAllReservations()
	{
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		Reservation[] entries = null;

		String CountQuery = "select count(*) from reservation;";

		String qry = "select * from reservation;";
		try
		{
			pStmt = con.prepareStatement(CountQuery);
			rs = pStmt.executeQuery();
			if (rs.next())
			{
				entries = new Reservation[rs.getInt(1)];
				pStmt = con.prepareStatement(qry);
				rs = pStmt.executeQuery();
				int i = 0;
				while (rs.next())
				{
					Reservation entry = new Reservation();
					entry.setReservationId(rs.getInt(1));
					entry.setFlightNumber(rs.getInt(2));
					entry.setAirlineName(rs.getString(3));
					entry.setSource(rs.getString(4));
					entry.setDestination(rs.getString(5));
					entry.setNumberOfSeats(rs.getInt(6));
					entry.setEmail(rs.getString(7));
					entries[i] = entry;
					i++;
				}
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return entries;
	}
	
	
	
	
	public String updateFlightDetails(FlightDetails flightDetails){
		EstablishConnection myConnection = new EstablishConnection();
		Connection con = myConnection.getConnection();
		Statement stmt = null;
		ResultSet rs =null;
		String message= MessageConstants.FLIGHT_NOT_UPDATED;

		String checkFlightQry = "Select airlineName from flight_details where flightNumber="+flightDetails.getFlightNumber();
		boolean flightExists = false;
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery(checkFlightQry);
			if(rs.next()){
				flightExists= true;
			}else{
				message = MessageConstants.FLIGHT_DOESNOT_EXIST;
			}


			String updateQuery = "update flight_details set ";

			if(flightExists){
				if(flightDetails.getAirlineName()!=null){
					updateQuery+="airlineName='"+flightDetails.getAirlineName()+"'";
				}else{
					updateQuery+="airlineName='"+rs.getString(1)+"'";
				}
				if(flightDetails.getSource()!=null && "".equals(flightDetails.getSource())){
					updateQuery+=",source='"+flightDetails.getSource()+"'";
				}
				if(flightDetails.getDestination()!=null && "".equals(flightDetails.getDestination())){
					updateQuery+=",destination='"+flightDetails.getDestination()+"'";
				}
				if(flightDetails.getNumberOfSeats()>0){
					updateQuery+=",noOfSeats="+flightDetails.getNumberOfSeats();
				}
				if(flightDetails.getFlightTime()!=null && !"".equals(flightDetails.getFlightTime())){
					updateQuery+=",flightTime='"+flightDetails.getFlightTime()+"'";
				}
				updateQuery+=" where flightNumber="+flightDetails.getFlightNumber()+";";

				stmt = con.createStatement();
				stmt.executeUpdate(updateQuery);
				message = MessageConstants.FLIGHT_UPDATE_SUCCESS;
			}
		} catch (SQLException e) {
			message = MessageConstants.FLIGHT_UPDATE_FAILURE;
			e.printStackTrace();

		}
		return message;

	}
	/////////////////////listAllReservations//////////////////////
	
	/**
	 * @function: updateEmployeeInfo
	 * @description: Updates the information for the Employee
	 * @author: Rushabh Jain
	 * @param: Employee
	 * @return: String
	 */
	public String updateEmployeeInfo(Employee emp)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);
			Employee updateCand = emp;

			int uniqueId=0;
			int empId = emp.getEmployeeID();
			PreparedStatement preparedStmt1 = null;
			PreparedStatement preparedStmtTemp = null;

			//Check the Employee Table for the Employee
			//The check would be made based on the Employee ID
			String queryP_1 = "Select count(1) from employee where employeeID = ?";
			preparedStmt1 = connection.prepareStatement(queryP_1);
			preparedStmt1.setInt(1, empId);

			String queryTemp = "Select uniqueID,workDescription,position from employee where employeeID = ?";
			preparedStmtTemp = connection.prepareStatement(queryTemp);
			preparedStmtTemp.setInt(1, empId);

			ResultSet result1 = preparedStmt1.executeQuery();
			
			//Ensure that the given employee exists in Employee table
			while (result1.next())
			{
				if (result1.getInt(1) <= 0)
				{
					message = MessageConstants.TRAVELLER_NOT_FOUND;
					return message;
				}
				else	//Extract the Unique ID for the traveler
				{
					ResultSet result2 = preparedStmtTemp.executeQuery();
					while (result2.next())
					{
						uniqueId = result2.getInt(1);
						//Prepare the Updated Traveler Object
						if(emp.getWorkDescription()==null)
							updateCand.setWorkDescription(result2.getString(2));
						if(emp.getPosition()==null)
							updateCand.setPosition(result2.getString(3));
					}
				}
			}

			//Ensure that the employee exists in Person table
			String queryE_1 = "Select count(1) from person where uniqueID = ?";
			PreparedStatement preparedStmt2 = connection.prepareStatement(queryE_1);
			preparedStmt2.setInt(1, uniqueId);
			ResultSet result2 = preparedStmt2.executeQuery();
			
			//Extract the original values from the database
			queryTemp = "Select firstName,lastName,address,city,zipcode,emailID from person where uniqueID= ?";
			preparedStmtTemp = connection.prepareStatement(queryTemp);
			preparedStmtTemp.setInt(1, uniqueId);

			//Ensure that the employee exists in Person table
			while (result2.next())
			{
				if (result2.getInt(1) <= 0)
				{
					message = MessageConstants.EMPLOYEE_NOT_FOUND;
					return message;
				}
				else	//Take the backup if the values are not passed by the caller function
				{
					ResultSet result3 = preparedStmtTemp.executeQuery();
					while (result3.next())
					{
						//Prepare the Updated Traveler Object
						if(emp.getFirstName()==null)
							updateCand.setFirstName(result3.getString(1));
						if(emp.getLastName()==null)
							updateCand.setLastName(result3.getString(2));
						if(emp.getAddress()==null)
							updateCand.setAddress(result3.getString(3));
						if(emp.getCity()==null)
							updateCand.setCity(result3.getString(4));
						if(emp.getZipCode()==null)
							updateCand.setZipCode(result3.getString(5));
						if(emp.getEmailID()==null)
							updateCand.setEmailID(result3.getString(6));
					}
				}
			}
			
			//Update the Person table for the Employee
			String queryP_2 = "update person set firstName=?,lastName=?,address=?,city=?,zipcode=?,emailID=? where uniqueID=?";
			PreparedStatement preparedStmt3 = connection.prepareStatement(queryP_2);

			preparedStmt3.setString(1, updateCand.getFirstName());
			preparedStmt3.setString(2, updateCand.getLastName());
			preparedStmt3.setString(3, updateCand.getAddress());
			preparedStmt3.setString(4, updateCand.getCity());
			preparedStmt3.setString(5, updateCand.getZipCode());
//			preparedStmt3.setString(6, updateCand.getDateOfBirth());
//			preparedStmt3.setInt(7, updateCand.getRoleID());

			preparedStmt3.setString(6, updateCand.getEmailID());
//			preparedStmt3.setString(9, encrypt(updateCand.getPassword()));
//			preparedStmt3.setString(10, updateCand.getState());
			preparedStmt3.setInt(7, uniqueId);

			preparedStmt3.executeUpdate();
			
			//Update the Employee table
			String queryE_2 = "update employee set workDescription=?,position=? where uniqueID=?";
			PreparedStatement preparedStmt4 = connection.prepareStatement(queryE_2);

			preparedStmt4.setString(1, updateCand.getWorkDescription());
			preparedStmt4.setString(2, updateCand.getPosition());
			preparedStmt4.setInt(3, uniqueId);
//			preparedStmt4.setDate(3, emp.getHireDate());

			preparedStmt4.executeUpdate();
			
			message = MessageConstants.EMPLOYEE_INFO_UPDATE_SUCCESS;
			connectionClass.endConnection(connection);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			message = MessageConstants.EMPLOYEE_INFO_UPDATE_FAILURE;
		}

		return message;
	}
	/////////////////////updateEmployeeInfo//////////////////////
	
	
	
	/**
	 * @function: updateTravellerInfo
	 * @description: Updates the information for the Traveller
	 * @author: Rushabh Jain
	 * @param: Traveller
	 * @return: String
	 */
	public String updateTravellerInfo(Traveller cand)
	{
		String message = null;

		EstablishConnection connectionClass = null;
		Connection connection = null;

		try
		{
			connectionClass = new EstablishConnection();
			connection = connectionClass.getConnection();
			connection.setAutoCommit(false);
			Traveller updateCand = cand;

			int uniqueId=0;
			String passportNo = cand.getPassportNo();
			int travellerId = cand.getTravellerId();
			PreparedStatement preparedStmt1 = null;
			PreparedStatement preparedStmtTemp = null;

			//Check the Traveller Table for the Traveller
			//The check would be made based on either Passport Number or TravellerID
			if(passportNo!=null)
			{
				String queryP_1 = "Select count(1) from traveller where passportID = ?";
				preparedStmt1 = connection.prepareStatement(queryP_1);
				preparedStmt1.setString(1, passportNo);
				
				String queryTemp = "Select uniqueID,passportID,nationality from traveller where passportID = ?";
				preparedStmtTemp = connection.prepareStatement(queryTemp);
				preparedStmtTemp.setString(1, passportNo);
			}
			else
			{
				String queryP_1 = "Select count(1) from traveller where travelerID = ?";
				preparedStmt1 = connection.prepareStatement(queryP_1);
				preparedStmt1.setInt(1, travellerId);

				String queryTemp = "Select uniqueID,passportID,nationality from traveller where travelerID = ?";
				preparedStmtTemp = connection.prepareStatement(queryTemp);
				preparedStmtTemp.setInt(1, travellerId);
			}
			ResultSet result1 = preparedStmt1.executeQuery();
			
			//Ensure that the traveler exists in Traveller table
			while (result1.next())
			{
				if (result1.getInt(1) <= 0)
				{
					message = MessageConstants.TRAVELLER_NOT_FOUND;
					return message;
				}
				else	//Extract the Unique ID for the traveler
				{
					ResultSet result2 = preparedStmtTemp.executeQuery();
					while (result2.next())
					{
						uniqueId = result2.getInt(1);
						//Prepare the Updated Traveler Object
						if(cand.getPassportNo()==null)
							updateCand.setPassportNo(result2.getString(2));
						if(cand.getNationality()==null)
							updateCand.setNationality(result2.getString(3));
					}
				}
			}

			//Ensure that the traveler exists in Person table
			String queryE_1 = "Select count(1) from person where uniqueID = ?";
			PreparedStatement preparedStmt2 = connection.prepareStatement(queryE_1);
			preparedStmt2.setInt(1, uniqueId);
			ResultSet result2 = preparedStmt2.executeQuery();
			
			//Extract the original values from the database
			String queryTemp = "Select firstName,lastName,address,city,zipcode,emailID from person where uniqueID= ?";
			preparedStmtTemp = connection.prepareStatement(queryTemp);
			preparedStmtTemp.setInt(1, uniqueId);

			//Ensure that the employee exists in Person table
			while (result2.next())
			{
				if (result2.getInt(1) <= 0)
				{
					message = MessageConstants.TRAVELLER_NOT_FOUND;
					return message;
				}
				else	//Take the backup if the values are not passed by the caller function
				{
					ResultSet result3 = preparedStmtTemp.executeQuery();
					while (result3.next())
					{
						//Prepare the Updated Traveler Object
						if(cand.getFirstName()==null)
							updateCand.setFirstName(result3.getString(1));
						if(cand.getLastName()==null)
							updateCand.setLastName(result3.getString(2));
						if(cand.getAddress()==null)
							updateCand.setAddress(result3.getString(3));
						if(cand.getCity()==null)
							updateCand.setCity(result3.getString(4));
						if(cand.getZipCode()==null)
							updateCand.setZipCode(result3.getString(5));
						if(cand.getEmailID()==null)
							updateCand.setEmailID(result3.getString(6));
					}
				}
			}
			
			//Update the Person table for the Traveller
			String queryP_2 = "update person set firstName=?,lastName=?,address=?,city=?,zipcode=?,emailID=? where uniqueID=?";
			PreparedStatement preparedStmt3 = connection.prepareStatement(queryP_2);

			preparedStmt3.setString(1, updateCand.getFirstName());
			preparedStmt3.setString(2, updateCand.getLastName());
			preparedStmt3.setString(3, updateCand.getAddress());
			preparedStmt3.setString(4, updateCand.getCity());
			preparedStmt3.setString(5, updateCand.getZipCode());
//			preparedStmt3.setString(6, updateCand.getDateOfBirth());
//			preparedStmt3.setInt(7, updateCand.getRoleID());

			preparedStmt3.setString(6, updateCand.getEmailID());
//			preparedStmt3.setString(9, encrypt(updateCand.getPassword()));
//			preparedStmt3.setString(10, updateCand.getState());
			preparedStmt3.setInt(7, uniqueId);

			preparedStmt3.executeUpdate();
			
			//Update the Traveller table for the Traveller
			String queryE_2 = "update traveller set passportID=?,nationality=? where uniqueID=?";
			PreparedStatement preparedStmt4 = connection.prepareStatement(queryE_2);

			preparedStmt4.setString(1, updateCand.getPassportNo());
			preparedStmt4.setString(2, updateCand.getNationality());
			preparedStmt4.setInt(3, uniqueId);
//			preparedStmt4.setDate(3, emp.getHireDate());

			preparedStmt4.executeUpdate();
			
			message = MessageConstants.TRAVELLER_INFO_UPDATE_SUCCESS;
			connectionClass.endConnection(connection);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			message = MessageConstants.TRAVELLER_INFO_UPDATE_FAILURE;
		}

		return message;
	}
	/////////////////////updateTravellerInfo//////////////////////
	
	
//	public void sendUpdate(){
//		javax.jms.Connection connection=null;
//		InitialContext jndi = null;
//		ConnectionFactory conFactory = null;
//		Destination destination = null;
//		Session session = null;
//		MessageProducer producer = null;
//
//
//		try {
//
//			Properties properties = new Properties();
//			properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
//			properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
//			properties.put(Context.PROVIDER_URL, "localhost");
//
//
//			jndi = new InitialContext(properties);
//			conFactory = (ConnectionFactory)jndi.lookup("XAConnectionFactory");
//
//			connection = conFactory.createConnection();
//			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			try{
//				destination = (Queue) jndi.lookup("queue1");
//			}catch(NamingException NE1)
//			{
//				System.out.println("NamingException: "+NE1+ " : Continuing anyway...");
//			}
//
//			if(destination==null){
//				destination = session.createTopic("queue1");
//				jndi.bind("queue1", destination);
//			}
//
//			producer = session.createProducer(destination);
//			connection.start();
//			String message = "CONGRATUALTIONS YOU HAVE BEEN ADDED TO THE SYSTEM";
//			TextMessage TM = session.createTextMessage(message);
//			producer.send(TM);
//		} catch (JMSException e) {
//			e.printStackTrace();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//
//	}
}
