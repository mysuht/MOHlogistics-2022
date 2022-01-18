<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*" %>
<html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
         <link href="resources/css/multiple-select.css" rel="stylesheet" type="text/css" />
          <script src="resources/js/jquery.multiple.select.js" type="text/javascript"></script>
           <script src="resources/js/logJS.js" type="text/javascript"></script>
           <script type="text/javascript" >
           $(document).ready(function(){
           if( $('input[name=dat]:checked').val() == "m" ){
            //  alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                  
              }
           
           
           
           
              //alert("hiiiiiii") ;
              $('input:radio').change(
              function(){
             if( $('input[name=dat]:checked').val() == "m" ){
             // alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                  
              }
              
              });
           });
           </script>
    </head>
    <body>
     <%
    LogisticsReportsClass db = new LogisticsReportsClass();
    ResultSet rsProd = db.getProducts();
    ResultSet rsDir = db.getDirectorates();
    
    %>
    <form action="processreports" method="GET">
    <div style="margin-top:20%; margin-left:20%; width:600pt; " >
    
         <div style="width:200pt;">
     Product: &nbsp; <select name="p_prod" class="sel_beh" id="pProd" multiple="multiple">
    <%
    while(rsProd.next()){
    %>
    <option value="<%= rsProd.getString(1) %>" > <%= rsProd.getString(2) %> </option>
    
    <%}%>
    </select>
    <script src="resources/js/jquery.multiple.select.js"></script>
    <script type="text/javascript" >
      $('#pProd').multipleSelect();
    </script>
    </div>
   <div style="width:150pt;float:left"> 
     Month: <input type="radio" value="m" name="dat" checked="checked" id="dat" /> &nbsp; &nbsp;
     Quarter: <input type="radio" value="q" name="dat" id="dat" />
     </div>
    <div style="width:150pt;float:left; display:none" id="quart">
     Quarter: &nbsp; <select name="p_quart" class="sel_beh">
    <%
    for(int i=1; i<=4 ; i++){
    String qra = "";
    
    switch(i){
    case 1: qra = "1st";
    break;
    case 2: qra="2nd";
    break;
    case 3: qra="3rd";
    break;
    case 4: qra="4th";
    break;
    }
    %>
    <option value="<%= i %>" > <%= qra+" Quarter" %> </option>
    
    <%}%>
    </select>
    
    </div>
    
     
     <div style="width:200pt;float:left" id="mon">
    Month: &nbsp; <select name="p_mon" class="sel_beh" >
    <%
    for(int i=1; i<=12 ; i++){
    String mon = "";
    
    switch(i){
    case 1: mon = "January";
    break;
    case 2: mon="February";
    break;
    case 3: mon="March";
    break;
    case 4: mon="April";
    break;
    case 5: mon = "May";
    break;
    case 6: mon = "June";
    break;
    case 7: mon = "July";
    break;
    case 8: mon= "August";
    break;
    case 9: mon = "September";
    break;
    case 10: mon= "October";
    break;
    case 11: mon = "November";
    break;
    case 12: mon= "December";
    break;
    
    }
    %>
    <option value="<%= i<10?("0"+i):i %>" > <%= mon %> </option>
    
    <%}%>
    </select>
    
    </div>
    <div style="width:200pt;">
    Year: &nbsp; <select name="p_year" class="sel_beh">
    <%
   for(int i=1997; i<=2014; i++){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>
    
   
    </div>
    <div style="width:500pt;">
    Report: <select name="rep" >
    <option value="p1">Rep1</option>
    <option value="p2">Rep2</option>
    
    </select>
    </div>
    <div >
    <input type="submit" value="call" />
    </div>
    </div>
    
    <%
db.s();
%>
    </form>
    </body>
</html>