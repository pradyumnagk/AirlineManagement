<!DOCTYPE html>
<%@page import="beans.FlightDetails"%>
<html lang="en">
<head>
<title>AirLines | Aircrafts</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css" media="all">
<link rel="stylesheet" href="css/layout.css" type="text/css" media="all">
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<link rel="stylesheet" href="css/table.css">

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

function validateFlightSearch(){
	var source = $("#source").val();
	var destination = $("#destination").val();
	var noOfSeats = $("#noOfSeats").val();
	var timeReg = /^[01][0-9]|[2][0-3]:[0-5][0-9]$/;
	var reg = /^[0-9]$/;
	var timeVal = $("#time").val();
	
	alert(timeVal);
	if(source==""){
		alert("Source not provided");
		return false;
	}else if(destination==""){
		alert("Source not provided");
		return false;
	}else if(!reg.test(noOfSeats)){
		alert('Seats should be a number');
		return false;
	}else if(timeVal==""){
		alert('Mention flight departure time');
	}else if(!timeReg.test(timeVal)){
		alert('Invalid time format. (00:00 to 23:59)');
		return false;
		
	}
	
}

</script>

</head>
<body id="page2">
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
              <li id="menu_active"><a href="Home.jsp">Home</a></li>
  			<% if (signedIn == false) {%>
             <li><a href="Login.jsp;">Login</a></li>
             <% } else { %>
              <li><a href="MyAccount.jsp">My Account</a></li>
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
      <%String message = request.getParameter("retry");
      	if(message!=null){%>
      		<p><%=message %></p>
      <%}
      %>
        <h2>Your Flight Planner</h2>
        <form id="form_1" action="TravelServlet" method="post" onsubmit="return validateFlightSearch()">
         <!-- <div class="wrapper pad_bot1">
     		<div class="radio marg_right1">
              <input type="radio" name="name1">
              Round Trip<br>
              <input type="radio" name="name1">
              One Way </div>
            <div class="radio">
              <input type="radio" name="name1">
              Empty-Leg<br>
              <input type="radio" name="name1">
              Multi-Leg </div>
          </div> --> 
          <div class="wrapper"> Leaving From:
            <div class="bg">
              <input type="text" class="input input1" id="source" name="source" placeholder="Enter City or Airport Code" onBlur="if(this.value=='') this.value='Enter City or Airport Code'" onFocus="if(this.value =='Enter City or Airport Code' ) this.value=''">
            </div>
          </div>
          <div class="wrapper"> Going To:
            <div class="bg">
              <input type="text" class="input input1" id="destination" name="destination" placeholder="Enter City or Airport Code" onBlur="if(this.value=='') this.value='Enter City or Airport Code'" onFocus="if(this.value =='Enter City or Airport Code' ) this.value=''">
            </div>
          </div>
          <div class="wrapper"> Departure Time:
            <div class="wrapper">
              <!-- <div class="bg left">
                <input type="text" class="input input2" value="mm/dd/yyyy " onBlur="if(this.value=='') this.value='mm/dd/yyyy '" onFocus="if(this.value =='mm/dd/yyyy ' ) this.value=''">
              </div>-->
              <div class="bg right">
                <input type="text" name="time" id="time" class="input input2" placeholder="12:00am" onBlur="if(this.value=='') this.value='12:00am'" onFocus="if(this.value =='12:00am' ) this.value=''">
              </div>
            </div>
          </div>
         <!--  <div class="wrapper"> Return Date and Time:
            <div class="wrapper">
              <div class="bg left">
                <input type="text" class="input input2" value="mm/dd/yyyy " onBlur="if(this.value=='') this.value='mm/dd/yyyy '" onFocus="if(this.value =='mm/dd/yyyy ' ) this.value=''">
              </div>
              <div class="bg right">
                <input type="text" class="input input2" value="12:00am" onBlur="if(this.value=='') this.value='12:00am'" onFocus="if(this.value =='12:00am' ) this.value=''">
              </div>
            </div>
          </div> -->
          <div class="wrapper">
            <p>Passenger(s):</p>
            <div class="bg left">
              <input type="text" name="noOfSeats" id="noOfSeats" class="input input2" placeholder="# passengers" onBlur="if(this.value=='') this.value='# passengers'" onFocus="if(this.value =='# passengers' ) this.value=''">
            </div>
            <input type="hidden" name="task" value="search"/>
            <input type="submit" class="button2" value="Search">
            <!-- <a href="#" class="button2">go!</a> --></div>
        </form>
        <h2>Did You Know?</h2>
        <p><strong>Lorem ipsum dolor</strong> sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo. </p>
        <div class="wrapper pad_bot2"><a href="#" class="button2">Read More</a></div>
      </div>
    </article>
    <article class="col2 pad_left1">
    <%if(request.getParameter("task")==null){ %>
    <form method="post" action="LoginServlet" name="form1">
      <h2>Welcome to AirLine</h2>
      <div class="wrapper">
        <figure class="left marg_right1"><img src="images/page2_img1.jpg" alt=""></figure>
        <p><strong>Sed ut perspiciatis</strong> unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae.</p>
      </div>
      <p>Vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem:</p>
      <input type="submit" name="Login" class="button1" value = "Remove">
      <input type="submit" name="Login" class="button1" value = "Add Employee">
      <h2>Aircraft Management</h2>
      <div class="wrapper">
        <figure class="right marg_left1"><img src="images/page2_img2.jpg" alt=""></figure>
        <p><strong>At vero eos et accusamus</strong> et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excep- turi sint occaecati cupiditate.</p>
        <p><strong>Non provident</strong>, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus omnis.</p>
      </div>
      </form>
      <%}else if(request.getParameter("task")!=null && "search".equals(request.getParameter("task"))){%>
      		<% if(session.getAttribute("flights")==null){
      		 	%>
      		 		<P>THERE ARE NO FLIGHTS AVAILABLE BETWEEN THIS SOURCE AND DESTINATION</P>
      		 	<%}else{
      		 			FlightDetails[] flights = (FlightDetails[])session.getAttribute("flights");
          		 		int rowNo = 0;
      					for(FlightDetails flight : flights){
      					//Display all flights here
			 %>			
					 <table class="CSSTableGenerator">
		      		<tr><th>FLIGHT NO</th><th>FLIGHT NAME</th><th>SOURCE</th><th>DESTINATION</th><th>SEATS AVAILABLE</th></tr>
			      		<form action="TravelServlet" method="post">
			      		<tr><td><%=flight.getFlightNumber() %></td><td><%=flight.getAirlineName() %></td><td><%=flight.getSource()%></td><td><%=flight.getDestination()%></td><td><%=flight.getNumberOfSeats()%></td>
			      		<input type="hidden" name="task" value="reserve"/>
			      		<input type="hidden" name="flightCnt" value="<%=rowNo%>"/>
			      		<td><input type="submit" value="Reserve"/></td></tr>
			      		</form>
      		<%rowNo++;}} %>
      				</table>
      <%}else if(request.getParameter("task")!=null && "confirmReservaton".equals(request.getParameter("task"))){%>
      		
      		<form action="TravelServlet" method="post">
      		<table>
      		<tr><td><label>NATIONALITY:</label></td><td> <input type="text" name="nationality"></td></tr>
      		<tr><td><label>PASSPORT NUMBER:</label></td><td> <input type="text" name="passport"></td></tr>
      		<input type="hidden" name="task" value="confirmReservation"/>
      		<tr><td><input type="submit" value="Confirm"/></td></tr>
      		</table>
      		</form>
      <%} %>
    </article>
  </section>
</div>
<div class="body2">
  <div class="main">
    <footer>
      <div class="footerlink">
      </div>
    </footer>
  </div>
</div>
<script type="text/javascript"> Cufon.now(); </script>
<!-- END PAGE SOURCE -->
</body>
</html>