package airlineSystem;

import javax.jws.WebService;

import beans.Employee;
import beans.FlightDetails;
import beans.Person;
import beans.Reservation;
import beans.Traveller;

@WebService
public class AirlineServer
{
	public String[] fetchStateList()
	{
		String[] stateArray = null;
		ModelController controller = new ModelController();
		stateArray = controller.fetchStateList();

		return stateArray;
	}

	public String registerCustomer(Person person)
	{
		String message = null;
		ModelController controller = new ModelController();
		message = controller.registerCustomer(person);

		return message;
	}

	public String deleteCustomer(String emailID)
	{
		String message = null;
		ModelController controller = new ModelController();
		message = controller.deleteCustomer(emailID);
		return message;
	}

	public String login(String userName, String password)
	{
		String message = null;
		ModelController controller = new ModelController();
		message = controller.login(userName, password);

		return message;
	}

	public String addEmployee(Employee employee)
	{
		String message = null;
		ModelController controller = new ModelController();
		message = controller.addEmployee(employee);

		return message;
	}

	public String deleteEmployee(String emailID)
	{
		String message = null;

		return message;
	}

	// Added by Pradyumna
	public String createNewReservation(FlightDetails journeyDetails, Traveller travellerInfo)
	{
		ModelController controller = new ModelController();
		String message = controller.reserveTicket(journeyDetails, travellerInfo);
		return message;
	}

	public String cancelReservation(String userID, int reservationID)
	{
		String message = null;
		ModelController controller = new ModelController();
		return controller.cancelTicket(userID, reservationID);
	}

	public Reservation issueTicket(String userId, int reservationID)
	{
		ModelController controller = new ModelController();
		return controller.issueTicket(userId, reservationID);
	}

	public Reservation[] getBookedTickets(String userID)
	{
		ModelController controller = new ModelController();
		Reservation[] jDetials = controller.getAllReservations(userID);
		;
		return jDetials;
	}

	// End of code added by Pradyumna

	public String processPayement(String cardID)
	{
		String message = null;

		return message;
	}

	public Employee[] listAllEmployees()
	{
		Employee[] employeeArray = null;

		return employeeArray;
	}

	// GEt 1 or many traveller information
	public Traveller[] listAllCustomers(int travelerID)
	{
		ModelController controller = new ModelController();
		Traveller[] travellerArray = controller.searchTravelers(travelerID);

		return travellerArray;
	}

	public Reservation[] listAllReservation()
	{
		ModelController controller = new ModelController();
		return controller.listAllReservations();
	}

	// Start: Code added by Pradyumna
	public FlightDetails[] listAllFlights(String source, String destination,String flightTime)
	{
		ModelController controller = new ModelController();
		FlightDetails[] flightArray = controller.searchFlightForSourceAndDest(source, destination,flightTime);

		return flightArray;
	}
	// End of code added by Pradyumna
	
	
	
	/**
	 * UPDATE TRAVELLER INFO: RUSHAB
	 * @param traveller
	 * @return
	 */
	public String updateTravellerInfo(Traveller traveller)
	{
		ModelController controller = new ModelController();
		return controller.updateTravellerInfo(traveller);
	}

	public String updateEmployeeInfo(Employee emp)
	{
		ModelController controller = new ModelController();
		return controller.updateEmployeeInfo(emp);
	}

	/**
	 * UPDATE FLIGHT DETAILS: PRADYUMNA
	 * @param flight 
	 * @return
	 */
	public String updateFlightDetails(FlightDetails flight)
	{	
		ModelController controller = new ModelController();
		String message = controller.updateFlightDetails(flight);
		return message;
	}

	public Employee[] searchEmployeeForID(int empID, String workDesc, String hireDate)
	{
		Employee[] employeeArray = null;

		ModelController controller = new ModelController();
		employeeArray = controller.searchEmployeeForID(empID, workDesc, hireDate);
		return employeeArray;
	}

	public Employee findEmployee(Employee emp)
	{
		Employee employee = null;
		return employee;
	}

	public FlightDetails[] findFlights(FlightDetails flight)
	{	
		ModelController controller = new ModelController();
		FlightDetails[] flightDetails = controller.searchFlight(flight);
		return flightDetails;
	}

	public Traveller[] findPassengersOnBoard(FlightDetails flight)
	{
		Traveller[] travellerArray = null;

		return travellerArray;
	}
	
	public Traveller[] searchTravelers(int travelerID)
	{
		System.out.println("Entering Airline Server" +travelerID);
		Traveller[] travellerArray = null;

		ModelController controller = new ModelController();
		travellerArray = controller.searchTravelers(travelerID);
		System.out.println("returning" + travellerArray);
		return travellerArray;
	}
}
