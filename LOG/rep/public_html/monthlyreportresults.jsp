<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass,java.sql.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <link type="text/css" rel="stylesheet" href="resources/css/prenatal.css"/>
    </head>
    <body>
          <%
          String ctf = request.getParameter("p_ctf");
    String year = request.getParameter("p_year");
        String month = request.getParameter("p_month");

    LogisticsReportsClass db =new LogisticsReportsClass();
    ResultSet    rs4 = db.getTimeDate();
   // rs4.next();
    %>
 
            <table cellspacing="0" cellpadding="3" border="0" width="100%">
            <tr>
            <th>
            Jordan Contraceptive Logistics System<br>MOH & HC Family Planning Program
            </th>
            <th>
            Monthly Report<br>Health Center
            </th>
            <th>
            Run Date: <%= rs4.getString(2)%> <br>Run Time: <%= rs4.getString(1)%>
            </th>
             </tr>
        
            
        </table>

    <br>

          <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <tr>
             <th width="50" class="col_titles_2">
                    Facility Code
                </th>
                  <th width="147" class="col_titles_2">
                    Facility Name
                </th> 
   
            </tr>
            <%
               ResultSet rs1 = db.getCTFMain(ctf,year,month);
            while(rs1.next()){
            %>
            <tr class="row_titles">
                <td width="50"  align="center"><%= rs1.getString(2)%></td>
                <td width="305"  align="center"><%= rs1.getString(3)%></td>
            </tr>
            
            
            <tr>
            <td>
            <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <tr>
             <th width="174" class="col_titles_2">Commodities</th>
             <th width="147" class="col_titles_2">Dose/Size</th>
             <th width="174" class="col_titles_2">Opening Balance</th>
             <th width="147" class="col_titles_2">Receipts</th> 
             <th width="174" class="col_titles_2">Issues</th>
             <th width="147" class="col_titles_2">Adjustments Type</th> 
             <th width="174" class="col_titles_2">Closing Blance</th>
             <th width="147" class="col_titles_2">Average</th> 
             <th width="174" class="col_titles_2">Required</th>
             <th width="147" class="col_titles_2">Received</th> 
             <th width="174" class="col_titles_2">New Users</th>
             <th width="147" class="col_titles_2">Cont Users</th> 
            </tr>
            <%
               ResultSet rs2 = db.getCTFItem(rs1.getString(1));
            while(rs2.next()){
            %>
            <tr class="row_titles">
                <td width="305"  align="center"><%= rs2.getString(1)%></td>
                <td width="305"  align="center"><%= rs2.getString(2)%></td>
                <td width="305"  align="center"><%= rs2.getString(3)%></td>
                <td width="305"  align="center"><%= rs2.getString(4)%></td>
                <td width="305"  align="center"><%= rs2.getString(5)%></td>
                <td width="305"  align="center"><%= rs2.getString(6)%></td>
                <td width="305"  align="center"><%= rs2.getString(7)%></td>
                <td width="305"  align="center"><%= rs2.getString(8)%></td>
                <td width="305"  align="center"><%= rs2.getString(9)%></td>
                <td width="305"  align="center"><%= rs2.getString(10)%></td>
                <td width="305"  align="center"><%= rs2.getString(11)%></td>
                <td width="305"  align="center"><%= rs2.getString(12)%></td>
            </tr>           
            <%}%>
            </table>
            </td>
            
            
            </tr>
            
            <%}%>
            </table>
        <table cellspacing="2" cellpadding="3" border="0" width="100%">
         </table> 

    </body>
</html>