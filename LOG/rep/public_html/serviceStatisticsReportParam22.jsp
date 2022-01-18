<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*" %>
<html>
    <head>
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
    <form method="post" action="serviceStatisticsReportResult22.jsp" >
    <div>
     <div style="width:325pt;float:left" >
    Directorate: &nbsp; <select name="p_dir" class="sel_beh">
    <%
    while(rsDir.next()){
    %>
    <option value="<%= rsDir.getString(1) %>" > <%= rsDir.getString(2) %> </option>
    
    <%}%>
    </select>
    
    </div>
    <div style="width:200pt;float:left">
    Product: &nbsp; <select name="p_prod" class="sel_beh">
    <%
    while(rsProd.next()){
    %>
    <option value="<%= rsProd.getString(1) %>" > <%= rsProd.getString(2) %> </option>
    
    <%}%>
    </select>
    
    </div>
    
    </div>
    
    <div style="clear: both; margin-top:40px;padding-top:40px;">
     
    <div style="width:200pt;float:left">
    Year: &nbsp; <select name="p_year" class="sel_beh">
    <%
   for(int i=1997; i<=2014; i++){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>
    
    </div>
    <div style="width:150pt;float:left" > 
     Month: <input type="radio" value="m" name="dat" checked="checked" id="dat" class="sel_beh" /> &nbsp; &nbsp;
     Quarter: <input type="radio" value="q" name="dat" id="dat" class="sel_beh"/>
     </div>
    <div style="width:150pt;float:left" id="quart"> 
    Quarter: &nbsp; <select name="p_quart" class="sel_beh">
    <option value="0">All</option>
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
    Month: &nbsp; <select name="p_mon" class="sel_beh">
    <option value="0">All</option>
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
    </div>
    <div style="clear:both; width:525pt; margin-top:30pt; " align="center">
    <input type="submit" value="   View   "
           class="button_behavior"  />
    </div>
    </form>
    </body>
</html>