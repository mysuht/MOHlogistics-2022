<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.PrintedItemsClass, java.sql.*, java.text.*" %>
<html>
   <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    
    <form name ="f1" action="" target="_blank">
    <%
    String main = request.getParameter("main");
    //String main = "107167";
    ResultSet rsMain = PrintedItemsClass.getPrintedRepMain(main);
    rsMain.next();
    %>
    
    <h1 align="center" style="font-weight:bold;">
    Monthly Report
    </h1>
    <br/>
    <div align="center" style="width:100%;text-align:left;font-size:14pt;font-weight:bold;">
    <div style="float:left; width:50%">
    Facility Code: <%= rsMain.getString(12) %> <br/>
    Facility Name: <%= rsMain.getString(15) %>  <br/>
    Facility Type: <%= rsMain.getString(17) %>  <br/>
    Month/Year: <%= rsMain.getString(3) %>
    </div>
    <div style="float:left;text-align:left; width:50%">
    Run Date: <%= PrintedItemsClass.getDate() %>  <br/>
    Entered By: &nbsp;&nbsp;&nbsp; <%= rsMain.getString(5)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+rsMain.getString(4) %>  </br>
    Last Changed By: <%= rsMain.getString(7)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+rsMain.getString(6) %>    
    </div>
    
    
    </div>
    <span style="clear:both;"> </span>
    <br style="clear:both" /> 
    <br/>
    <hr style="clear:both">
    <br/>
    <table border="0" width="100%" style="clear:both;">
     <tr class="col_titles">
     <th align="center" style="text-decoration:underline ;">
      Commodities 
     </th>
     <th align="center" style="text-decoration:underline ;">
     Dose/Size
     </th>
     <th align="center">
      Opening <br/>
     <span   style="text-decoration:underline ;"> Balance </span>
     </th>
     <th align="center" style="text-decoration:underline ;">
     Receipts
     </th>
     <th align="center" style="text-decoration:underline ;">
     Issues
     </th>
     <th align="center" style="text-decoration:underline ;">
     Adjustments
     </th>
     <th align="center" style="text-decoration:underline ;">
     Type
     </th>
     <th align="center">
      Closing <br/>
     <span   style="text-decoration:underline ;"> Balance </span>
     </th>
     <th align="center" style="text-decoration:underline ;">
     Average
     </th>
     <th align="center" style="text-decoration:underline ;">
     Required
     </th>
     <th align="center" style="text-decoration:underline ;">
     Received
     </th>
      <th align="center">
      New <br/>
     <span   style="text-decoration:underline ;"> Users </span>
     </th>
      <th align="center">
      Cont <br/>
     <span   style="text-decoration:underline ;"> Users </span>
     </th>
     </tr>
    <%
    ResultSet rsItems = PrintedItemsClass.getPrintedRepItems(main);
    while(rsItems.next()){
    %>
    <tr class="row_titles">
    <th>
    <%= rsItems.getString(21) %>
    </th>
    <th>
    <%= rsItems.getString(23) %>
    </th>
    <th>
    <%= rsItems.getString(2) %>
    </th>
    <th>
    <%= rsItems.getString(3) %>
    </th>
    <th>
    <%= rsItems.getString(4) %>
    </th>
    <th>
    <%= rsItems.getString(5) %>
    </th>
    <th>
    <%= rsItems.getString(19) %>
    </th>
    <th>
    <%= rsItems.getString(7) %>
    </th>
    <th>
    <%= rsItems.getString(16) %>
    </th>
    <th>
    <%= rsItems.getString(17) %>
    </th>
    <th>
    <%= rsItems.getString(18) %>
    </th>
    <th>
    <%= rsItems.getString(11) %>
    </th>
    <th>
    <%= rsItems.getString(12) %>
    </th>
    
    </tr>
    
    <%}%>
    </table>
    <br/>
    <hr/>
    <span style="text-decoration:underline ; font-weight:bold;">
    
    Comments: 
    </span> <%= rsMain.getString(9) %>
   
    </form>
    
    </body>
</html>