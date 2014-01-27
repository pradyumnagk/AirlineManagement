<!DOCTYPE html>
<html lang="en">
<head>
<title>Admin</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css" media="all">
<link rel="stylesheet" href="css/layout.css" type="text/css" media="all">
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">

<%@ page import="beans.Employee"%>
<%@ page import="beans.Traveller"%>
<%@ page import="beans.Reservation"%>
<%@ page import="beans.FlightDetails"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@page import="servlets.LoginServlet"%>

<%@ page session="true"%>

<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/cufon-replace.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_italic_600.font.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_italic_400.font.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_400.font.js"></script>

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>

<style type="text/css">
	table{
		border: 1px;
		width: 50%;
		cellspacing : 5px;
		cellpadding : 8px;
	}
	table th{
		bgcolor : #005fbf;
	}
	table td{
		bgcolor : #003f7f;
	}
</style>
<script type="text/javascript">

function validateUpdateTraveler(){
	var passportId=document.getElementById("passport");
	var travelerId=document.getElementById("travelerId");

	if(passportId ==null && travelerId==null)
	{
		alert("Either Passport ID or Traveler ID is required for updation.");
		return false;
	}
}


function validateUpdateEmployee(){
	var empId=document.getElementById('empId');

	if(empId =="")
	{
		alert("Employee ID is required for updation.");
		return false;
	}
}

function validateSearchFlight(){
	var flghtNo = $("#flightNo").val();
	var flightTime = $("#flightTime").val();
	var seats = $("#seats").val();
	var reg = /^[0-9]$/;
	var timeReg = /^[01][0-9]|[2][0-3]:[0-5][0-9]$/;
	
	if(flghtNo){
		if(!reg.test(flghtNo)){
			alert('Flight number should not be a number');
			return false;
		}
	}else if(flightTime){
		if(!timeReg.test(flightTime)){
			alert('Invalid time format. (00:00 to 23:59)');
			return false;
		}
	}else if(seats){
		if(!reg.test(seats)){
			alert('seats should be number');
			return false;
		}
	}else if(source || destination){
		if(source=="" || destination==""){
			alert('Source or destination is invalid name');
		}
	}
	
}

function validateUpdateFlight(){
	var flghtNo = $("#flightNo").val();
	var flightTime = $("#flightTime").val();
	var seats = $("#seats").val();
	var source = $("#source").val();
	var destination = $("#destination").val();
	
	var reg = /^[0-9]$/;
	var timeReg = /^[01][0-9]|[2][0-3]:[0-5][0-9]$/;
	
	if(!flightNo){
		alert('Flight number should  be a blank');
		return false;
	}else if(flghtNo){
		if(!reg.test(flghtNo)){
			alert('Flight number should  be a number');
			return false;
		}
	}else if(flightTime){
		if(!timeReg.test(flightTime)){
			alert('Invalid time format. (00:00 to 23:59)');
			return false;
		}
	}else if(seats){
		if(!reg.test(seats)){
			alert('seats should be number');
			return false;
		}
	}else if(source || destination){
		if(source=="" || destination==""){
			alert('Source or destination is invalid name');
		}
	}
}
function hideOther(){
	$('div#div1').hide();
	$('div#div2').hide();
	$('div#div10').hide();
}
	$(function() {

		$('a#createEmp').click(function() {
			$('div#div1').show();
			$('div#div2').hide();
			$('div#div10').hide();
			var ajaxDisplay = document.getElementById('searchDispEmp');
			ajaxDisplay.innerHTML = '';
			return false;
		});

		$('a#delEmp').click(function() {
			$('div#div2').show();
			$('div#div1').hide();
			$('div#div10').hide();
			var ajaxDisplay = document.getElementById('searchDispEmp');
			ajaxDisplay.innerHTML = '';
			return false;
		});

		$('a#searchEmployee').click(function() {
			$('div#div10').show();
			$('div#div1').hide();
			$('div#div2').hide();
			var ajaxDisplay = document.getElementById('searchDispEmp');
			ajaxDisplay.innerHTML = '';
			return false;
		});

	});
</script>

</head>
<body id="page2" onload="hideOther()">
	<!-- START PAGE SOURCE -->

	<%
		String fName = (String) session.getAttribute("fName");
		boolean signedIn = false;

		if (fName != null)
			signedIn = true;
		else
			response.sendRedirect("Home.jsp");
	%>
	<div class="body1">
		<div class="main">
			<header>
				<div class="wrapper">
					<h1>
						<a href="index.html" id="logo">AirLines</a><span id="slogan">International
							Travel</span>
					</h1>
					<div class="right">
						<nav>
							<ul id="top_nav">
								<li><a href="#"><img src="images/img1.gif" alt=""></a></li>
								<li><a href="#"><img src="images/img2.gif" alt=""></a></li>
								<li class="bg_none"><a href="#"><img
										src="images/img3.gif" alt=""></a></li>
							</ul>
						</nav>
						<nav>
							<ul id="menu">
								<li><a href="Home.jsp">Home</a></li>
						
								<!-- <li><a href="JavaScript:newPopup('Login.jsp');">Login</a></li> -->
								<%
									if (signedIn == false)
									{
								%>
								<li><a href="Login.jsp;">Login</a></li>
								<%
									}
									else
									{
								%>
								<li><a href="MyAccount.jsp">My Account</a></li>
								<li id="menu_active"><a href="Admin.jsp">Admin</a></li>
								<li><a href="Login.jsp;">SignOut</a></li>
								<%
									}
								%>
							</ul>
						</nav>
					</div>
				</div>
			</header>
		</div>
	</div>
	<div class="main">
		<div id="banner">
			<div class="text1">
				COMFORT<span>Guaranteed</span>
				<%
					if (fName != null)
					{
				%>
				<p>
					Welcome
					<%=fName%>
					!
					<%
					}
				%>
				</p>
			</div>
		</div>
	</div>

	<div class="main">
		<section id="content">
			<article class="col1">
				<div class="pad_1">
					<h2>Your Flight Planner</h2>

					<ul id="categories">
						<li style="border-top: 0;"><a href="#" id="createEmp">Create
								New Employee</a></li>
						<li><a href="#" id="delEmp">Delete Employee</a></li>
						<li><a href="#">Delete Customer</a></li>
						<li><a href="#">List All Employees</a></li>
						<li><a href="#">List All Customers</a></li>
						<li><a href="EmployeeServlet?task=6">List All Reservations</a></li>
						<li><a href="EmployeeServlet?task=7">List All Flights</a></li>
						<li><a href="Admin.jsp?task=searchFlight">Search Flight</a></li>
						<li><a href="EmployeeServlet?task=10">Update Flight Info</a></li>
						<li><a href="EmployeeServlet?task=8">Update Customer Info</a></li>
						<li><a href="EmployeeServlet?task=9">Update Employee Info</a></li>
						<li><a href="#" id="searchEmployee">Search Employee</a></li>
						<li><a href="TravelServlet?task=searchTravelleer">search Traveller</a></li>
						

					</ul>
				</div>
			</article>
			<article class="col2 pad_left1">
				<%String message = request.getParameter("message");
					if(message!=null){%>
						<p><%=message %></p>
				<%	}
				%>
				<div id="div1">
					<%
						airlineSystem.AirlineServerProxy proxy = new airlineSystem.AirlineServerProxy();
						//proxy.setEndpoint("http://localhost:8080/Airline_Management/services/AirlineServer?wsdl");

						String[] stateList = proxy.fetchStateList();
					%>
					
					<h2>Contact Form</h2>
					<form id="ContactForm" method="post" action="LoginServlet"
						name="form1">
						<div>
							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="firstName" />
								</div>
								First Name:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="lastName" />
								</div>
								Last Name:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="email" class="input" name="email" />
								</div>
								Your E-mail:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="password" class="input" name="password" />
								</div>
								Password:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="address" />
								</div>
								Address:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type=text class="input" name="city" />
								</div>
								City:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="date" class="input" name="DOB" />
								</div>
								Date Of Birth:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="position" />
								</div>
								Designation:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="workDescription" />
								</div>
								Work Description:<br />
							</div>


							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="zipCode" />
								</div>
								Zip Code:<br />
							</div>

							<div class="wrapper">
								<div class="bg">
									<span style="display: block; float: left;"> <select
										name="selectedState" id="selectedState"
										style="margin-top: 10px;">
											<%
												for (int i = 0; i < stateList.length; i++)
												{
											%>
											<option value=<%=stateList[i]%>><%=stateList[i]%></option>
											<%
												}
											%>
									</select>
									</span>
								</div>
								State:<br />
							</div>

							<br> <input type="submit" name="Login" class="button1"
								value="Add Employee" style="margin-bottom: 20px;"><br>
						</div>
					</form>

				</div>

				<div id="div2" hidden="yes">
					<form id="ContactForm" method="post" action="LoginServlet"
						name="form2">
						<h2>Enter email ID to delete User!</h2>
						<div>
							<div class="wrapper">
								<div class="bg">
									<input type="email" class="input" name="emailID" />
								</div>
								Email ID:<br />
							</div>

							<br> <input type="submit" name="Login" class="button1"
								value="Delete Employee" style="margin-bottom: 20px;"><br>
						</div>
					</form>
				</div>


				<!-- Abinaya's changes for Search Employee starts here-->
				<div id="div10" hidden="yes">
					<form id="ContactForm" action="LoginServlet" method="POST"
						name="form10">
						<h2>Search Employee</h2>
						<div>
							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="empID" id="empID" />
								</div>
								Employee ID:<br />
							</div>
							<div class="wrapper">
								<div class="bg">
									<input type="text" class="input" name="empDesc"
										id="description" />
								</div>
								Work Description:<br />
							</div>
							<div class="wrapper">
								<div class="bg">
									<input type="date" class="input" name="hireDate" id="hireDate" />
								</div>
								Hired Date:<br />
							</div>

							<br> <input type="button" name="Login" class="button1"
								value="Search Employee" onclick="searchEmp()"
								style="margin-bottom: 20px;"><br> <br>
						</div>
					</form>
				</div>
				<div class="bg" id="searchDispEmp"></div>
				<script>
					function searchEmp() {
						var xmlHttpReq;
						// Mozilla/Safari
						if (window.XMLHttpRequest) {
							xmlHttpReq = new XMLHttpRequest();
						}
						// IE
						else if (window.ActiveXObject) {
							xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
						}
						xmlHttpReq.onreadystatechange = function() {
							if (xmlHttpReq.readyState == 4) {
								var ajaxDisplay = document
										.getElementById('searchDispEmp');
								ajaxDisplay.innerHTML = xmlHttpReq.responseText;
							}
						}
						var param = "empID="
								+ document.getElementById('empID').value
								+ "&description="
								+ document.getElementById('description').value
								+ "&hireDate="
								+ document.getElementById('hireDate').value
								+ "&Login=Search Employee";
						xmlHttpReq.open("POST", "LoginServlet?" + param, true);
						xmlHttpReq.setRequestHeader("Content-length",
								param.length);
						xmlHttpReq.send(null);

					}
				</script>
				<!-- End of changes by Abinaya -->
				
				
				<!-- SHRUTI CHANGES -->
				<%if(request.getParameter("task")!=null && "searchTraveller".equals(request.getParameter("task"))){ %>
				<form action="TravelServlet" method="get">
						<table>
								<tr>
									<td>Search traveler by ID</td>
									<td><input type="text" name="travelerID" id="travelerID" value="" /></td>
								</tr>
								<input type="hidden" name="task" value="travellerInfo"/>
								<tr>
									<td><input type="submit" value="Search Traveller"/>
								</tr>
						</table>
				</form>
				<%}else if(request.getParameter("task")!=null && "travellerInfo".equals(request.getParameter("task"))){
					Traveller traveller = (Traveller)request.getAttribute("traveller");
					if(traveller!=null){%>
					<table>
					<tr><th>FIRST NAME</th><th>LAST NAME</th><th>STATE</th><th>NATIONALITY</th><th>PASSPORT</th></tr>
					<tr><td><%=traveller.getFirstName() %></td><td><%=traveller.getLastName()%></td><td><%=traveller.getState() %></td><td><%=traveller.getNationality()%></td>
						<td><%=traveller.getPassportNo()%></td></tr>
					</table>
					
				<%}} %>
				<!-- End of SHRUTI CHANGES -->
				
				<!-- Pradyumna changes -->
				<!-- Search flight results -->
				<%if(request.getParameter("task")!=null && "searchFlightForAttrib".equals(request.getParameter("task"))){
					FlightDetails[] flights = (FlightDetails[])request.getAttribute("sflights");
					if(flights!=null){%>
					<table border=1px width=50% cellspacing=5px cellpadding=8px>
					   <tr bgcolor=#005fbf><th>Flight Number</th><th>Airline Name</th><th>Source</th><th>Destination</th><th>Seats Available</th><th>Flight Time</th></tr>
							<%
					   for(FlightDetails flight : flights){%>
					   
					   <tr bgcolor=#003f7f><td><%=flight.getFlightNumber()%></td><td><%=flight.getAirlineName() %></td><td><%=flight.getSource()%></td>
					   <td><%=flight.getDestination()%></td><td><%=flight.getNumberOfSeats()%></td><td><%=flight.getFlightTime()%></td></tr>
						<%}%>
						</table>
					<%}else{%>
					<p>NO FLIGHTS FOUND FOR THE GIVEN PARAMETERS</p>
				<%
					   }} %>
				<!-- search flight ends -->
				
				<!-- SEARCH FLIGHT BASED ON ATTRIBUTES -->
				<%if(request.getParameter("task")!=null && "searchFlight".equals(request.getParameter("task"))){%>
					<form  id="ContactForm" action="EmployeeServlet" method="post" onsubmit="return validateSearchFlight()">
								<h2>UPDATE FLIGHT DETAILS</h2>
									<div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightNo" id="flightNo" />
											</div>
											FLIGHT NUMBER:<br />
										</div> ,
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightName" id="flightName" />
											</div>
											AIRLINE NAME:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="source" id="source" />
											</div>
											SOURCE:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="destination" id="destination" />
											</div>
											DESTINATION*:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightTime" id="flightTime" />
											</div>
											FLIGHT TIME:<br />
										</div>
										<input type="hidden" name="task" value="11"/>
										<div class="wrapper">
											<div class="bg">
												<input type="submit" class="input"  value="Search" />
												<input type="reset" class="input"  value="Clear" />
											</div>
										</div>
									</div>	
								</form>
				<%} %>
				
				
				<!-- UPDATE FLIGHT DETAILS  -->
				<%if(request.getParameter("task")!=null && "10".equals(request.getParameter("task"))){ %>
				
								<form  id="ContactForm" action="EmployeeServlet" method="post" onsubmit="return validateUpdateFlight()">
								<h2>UPDATE FLIGHT DETAILS</h2>
									<div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightNo" id="flightNo" />
											</div>
											FLIGHT NUMBER*:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightName" id="flightName" />
											</div>
											AIRLINE NAME:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="source" id="source" />
											</div>
											SOURCE:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="destination" id="destination" />
											</div>
											DESTINATION*:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="flightTime" id="flightTime" />
											</div>
											FLIGHT TIME:<br />
										</div>
										<div class="wrapper">
											<div class="bg">
												<input type="text" class="input" name="seats" id="seats" />
											</div>
											NUMBER OF SEATS:<br />
										</div>
										<input type="hidden" name="task" value="10"/>
										<div class="wrapper">
											<div class="bg">
												<input type="submit" class="input"  value="Update" />
												<input type="reset" class="input"  value="Clear" />
											</div>
										</div>
									</div>	
								</form>
				<%} %>
				<!-- UPDATE FLIGHT DETAILS ENDS:PRADYUMNA -->
				
				<!-- UPDATE TRAVELLER INFO : RUSHAB CHANGES -->
				<%if(request.getParameter("task")!=null && "8".equals(request.getParameter("task"))){ %>
						<div class="update customer">
						 	<table>
						 	<form action="EmployeeServlet" method="post" onsubmit="return validateUpdateTraveler()">
						   		<tr><td>TRAVELER ID *: <input type="text" id="travelerId" name="travelerId"/></td>	
						  		<tr><td>PASSPORT ID *: <input type="text" id="passport" name="passport"/></td>
						 		<tr><td>FIRST NAME: <input type="text" id="fname" name="fname"/></td>
						  		<tr><td>LAST NAME: <input type="text" id="lname" name="lname"/></td>
						  		<tr><td>ADDRESS: <input type="text" id="address" name="address"/></td>
						  		<tr><td>CITY: <input type="text" id="city" name="city"/></td>
						  		<tr><td>ZIPCODE: <input type="text" id="zip" name="zip"/></td>
						  		<tr><td>EMAIL ID: <input type="text" id="email" name="email"/></td>
						  		<tr><td>NATIONALITY: <input type="text" id="nationality" name="nationality"/></td>
						 		<input type="hidden" name="task" value="8"/>
						 		<tr><td><input type="submit" value="UPDATE"></td></tr>
						 	</form>
						 	</table>
						 </div>
			   <%} %>
			   <!--UPDATE EMPLOYEE INFO-->
						<%if(request.getParameter("task")!=null && "9".equals(request.getParameter("task"))){ %>
						<div class="update employee">
						 	<table>
						 	<form action="EmployeeServlet" method="post" onsubmit="return validateUpdateEmployee()">
						 		<tr><td>EMPLOYEE ID: <input type="text" id="empId" name="empId"/></td>
						 		<tr><td>FIRST NAME: <input type="text" id="fname" name="fname"/></td>
						  		<tr><td>LAST NAME: <input type="text" id="lname" name="lname"/></td>
								<tr><td>WORK DESCRIPTION: <input type="text" id="workDes" name="workDes"/></td>
						 		<tr><td>POSITION: <input type="text" id="position" name="position"/></td>
						  		<tr><td>ADDRESS: <input type="text" id="address" name="address"/></td>
						  		<tr><td>CITY: <input type="text" id="city" name="city"/></td>
						  		<tr><td>ZIPCODE: <input type="text" id="zip" name="zip"/></td>
						  		<tr><td>EMAIL ID: <input type="text" id="email" name="email"/></td>
						 		<input type="hidden" name="task" value="9"/>
						 		<tr><td><input type="submit" value="UPDATE"></td></tr>
						 	</form>
						 	</table>
						 </div>
						 <%} %>
						<!--LIST ALL RESERVATIONS-->
							<%if(request.getParameter("task")!=null && "6".equals(request.getParameter("task"))){%>
							<div class="list reservations">
							<table border="1" cellspacing="10"  cellpadding="10">
								<tr>
								<th>ID</th>
								<th>Flight No.</th>
								<th>Airlines</th>
								<th>Source</th>
								<th>Destination</th>
								<th>Seats</th>
								<th>Email</th>
								</tr>
							
								<%Reservation[] reservations = (Reservation[])session.getAttribute("reservations");
										int rowCnt = 0;
										for(Reservation reservation : reservations){
								%>	
									<form action="EmployeeServlet" method="post">
										<tr>
										<td><%=reservation.getReservationId()%></td>
										<td><%=reservation.getFlightNumber()%></td>
										<td><%=reservation.getAirlineName()%></td>
										<td><%=reservation.getSource()%></td>
										<td><%=reservation.getDestination()%></td>
										<td><%=reservation.getNumberOfSeats()%></td>
										<td><%=reservation.getEmail()%></td>
									</form>
								<%rowCnt++;} %>
							</table>
							</div>
							<%} %>
							<!--LIST ALL RESERVATIONS-->
							
							
							<!--LIST ALL FLIGHTS-->
							<%if(request.getParameter("task")!=null && "7".equals(request.getParameter("task"))){%>
							<div class="list flights">
							<table border="1" cellspacing="10"  cellpadding="10">
								<tr>
								<th>Flight No.</th>
								<th>Airlines</th>
								<th>Source</th>
								<th>Destination</th>
								<th>Seats</th>
								<th>Crew ID</th>
								</tr>
							
								<%FlightDetails[] flights  = (FlightDetails[])request.getAttribute("flights");
									int rowCnt = 0;
									for(FlightDetails flight : flights){
								%>	
									<form action="EmployeeServlet" method="post">
										<tr>
										<td><%=flight.getFlightNumber()%></td>
										<td><%=flight.getAirlineName()%></td>
										<td><%=flight.getSource()%></td>
										<td><%=flight.getDestination()%></td>
										<td><%=flight.getNumberOfSeats()%></td>
										<td><%=flight.getCrewId()%></td>
									</form>
								<%rowCnt++;} %>
							</table>
							</div>
							<%} %>
							<!--LIST ALL FLIGHTS-->
				<!-- RUSHAB CHANGES -->
			</article>
		</section>
	</div>
	<div class="body2">
		<div class="main">
			<footer>
				<div class="footerlink">
					<div style="clear: both;"></div>
				</div>
			</footer>
		</div>
	</div>
	<script type="text/javascript">
		Cufon.now();
	</script>
	<!-- END PAGE SOURCE -->
</body>
</html>