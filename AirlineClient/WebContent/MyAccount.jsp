<!DOCTYPE html>
<html lang="en">
<head>
<title>My Account</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css" media="all">
<link rel="stylesheet" href="css/layout.css" type="text/css" media="all">
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">

<script type="text/javascript" src="js/jquery-1.4.2.js" ></script>
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/cufon-replace.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_italic_600.font.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_italic_400.font.js"></script>
<script type="text/javascript" src="js/Myriad_Pro_400.font.js"></script>

<script type="text/javascript">
function newPopup(url) {
	 var left = (screen.width/2)-(150);
	  var top = (screen.height/2)-(250);
	popupWindow = window.open(
		url,'popUpWindow','height=300,width=500,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=yes,status=no,titlebar=no, left='+left+',top='+top+'');
	popupWindow.focus();
}

function showTickeSelection(){
	$("#ticketSelect").show();
}

function validateTicketNo(){
	reservationID = $("#reservID").val();
	var reg = /^[0-9]$/;
	if(reservationID==""){
		alert("Reservation number should not be empty");
		return false;
	}else if(!reg.test(reservationID)){
		alert("Reservation number should be integer");
		return false;
	}
}
</script>

</head>
<body id="page2">
<%@ page import="beans.Reservation"%>
<!-- START PAGE SOURCE -->

<%
	String fName = (String) session.getAttribute("fName");
	boolean signedIn = false;
	
	if (fName != null)
		signedIn = true;
%>
<div class="body1">
  <div class="main">
    <header>
      <div class="wrapper">
        <h1><a href="index.html" id="logo">AirLines</a><span id="slogan">International Travel</span></h1>
        <div class="right">
          <nav>
            <ul id="top_nav">
              <li><a href="#"><img src="images/img1.gif" alt=""></a></li>
              <li><a href="#"><img src="images/img2.gif" alt=""></a></li>
              <li class="bg_none"><a href="#"><img src="images/img3.gif" alt=""></a></li>
            </ul>
          </nav>
          <nav>
            <ul id="menu">
              <li><a href="Home.jsp">Home</a></li>
  <!-- <li><a href="JavaScript:newPopup('Login.jsp');">Login</a></li> --> 
  			<% if (signedIn == false) {%>
             <li><a href="Login.jsp;">Login</a></li>
             <% } else { %>
              <li id="menu_active"><a href="MyAccount.jsp">My Account</a></li>
              <li><a href="Admin.jsp">Admin</a></li>
              <li><a href="Login.jsp;">SignOut</a></li>
             <%} %>
            </ul>
          </nav>
        </div>
      </div>
    </header>
  </div>
</div>
<div class="main">
  <div id="banner">
    <div class="text1"> COMFORT<span>Guaranteed</span>
    <%
    if (fName != null)
    {
    %>
    	<p>	Welcome <%= fName %> !
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
						<li style="border-top: 0;"><a href="TravelServlet?task=cancel">Cancel Reservation</a></li>
						<li><a href="TravelServlet?task=ticket">Issue Ticket</a></li>

				</ul>
      </div>
    </article>
    <article class="col2 pad_left1">
<!-- 


PUT YOUR MAIN CONTETNT HERE !!!!

<
 -->
 <%if(request.getParameter("task")!=null && "issueTicket".equals(request.getParameter("task"))){ %>
 <div id="ticketSelect">
 	<table>
 	<form action="TravelServlet" method="get" onsubmit="return validateTicketNo()">
 		<tr><td>ENTER YOUR RESERVATION ID: <input type="text" id="reservID" name="reservID"/></td>
 		<input type="hidden" name="task" value="issueTicket"/>
 		<td><input type="submit" value="Issue Ticket"></td></tr>
 	</form>
 	</table>
 </div>
 <%} %>
 <%if(request.getParameter("message")!=null) {%>
 		<p><%=request.getParameter("message")%></p>
 <%} %>
<%if(request.getParameter("task")!=null && "cancel".equals(request.getParameter("task"))){%>
	<table>
	<tr><th>ReservastionNo</th><th>Airline Number</th><th>Airline Name</th><th>Source</th><th>Destination</th><th>Seats Booked</th></tr>
	<%Reservation[] reservations = (Reservation[])session.getAttribute("reservations");
			int rowCnt = 0;
			for(Reservation reservation : reservations){
	%>	
		<form action="TravelServlet" method="post">
		<tr><td><%=reservation.getReservationId() %></td><td><%=reservation.getFlightNumber() %></td><td><%=reservation.getAirlineName()%></td><td><%=reservation.getSource() %></td>
		<td><%=reservation.getDestination() %></td><td><%=reservation.getNumberOfSeats()%></td>
		<input type="hidden" name="reservID" value="<%=reservation.getReservationId()%>"/>
		<input type="hidden" name="task" value="cancelTicket"/>
		<td><input type="submit" value="Cancel Ticket"></td></tr>
		</form>
	<%rowCnt++;} %>
	</table>
<%}else if(request.getParameter("task")!=null && "showTicket".equals(request.getParameter("task"))){
	Reservation ticket = (Reservation)request.getAttribute("ticket");
%>
	<table> 
	<tr><th>RerservationNo</th><th>AIRLINE NO</th><th>AIRLINE NAME</th><th>SOURCE</th><th>DESTINATION</th><th>SEATS BOOKED</th></tr>
	<tr><td><%=ticket.getReservationId()%></td><%=ticket.getFlightNumber()%><td><%=ticket.getAirlineName()%></td><td><%=ticket.getSource()%></td><td><%=ticket.getDestination()%></td><td><%=ticket.getNumberOfSeats()%></td></tr>
	</table>
<%} %>
    </article>
  </section>
</div>
<div class="body2">
  <div class="main">
    <footer>
      <div class="footerlink">
        <div style="clear:both;"></div>
      </div>
    </footer>
  </div>
</div>
<script type="text/javascript"> Cufon.now(); </script>
<!-- END PAGE SOURCE -->
</body>
</html>