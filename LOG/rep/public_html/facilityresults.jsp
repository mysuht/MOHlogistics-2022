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
    LogisticsReportsClass db =new LogisticsReportsClass();
    ResultSet rs = db.getFacilityDir();
    ResultSet    rs4 = db.getTimeDate();
   // rs4.next();
    %>
 
            <table cellspacing="0" cellpadding="3" border="0" width="100%">
            <tr>
            <th>
            Jordan Contraceptive Logistics System<br>MOH & HC Family Planning Program
            </th>
            <th>
            Facility Product Dispensing Report<br>Health Center
            </th>
            <th>
            Run Date: <%= rs4.getString(2)%> <br>Run Time: <%= rs4.getString(1)%>
            </th>
             </tr>
        
            
        </table>

    <br>

            <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <%
            while(rs.next()){
            %>
            <tr class="row_titles">
            <th width="174" class="col_titles_2">
                    Supplying Facility
                </th>
                <td width="145"  align="center" ><%= rs.getString(2)%></td>
                <th width="147" class="col_titles_2">
                    Facility Name
                </th> 
                <td width="305"  align="center"><%= rs.getString(3)%></td>
            </tr>


            <tr>
            <td>
            <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <tr>
             <th width="174" class="col_titles_2">
                    Facility Code
                </th>
                  <th width="147" class="col_titles_2">
                    Facility Name
                </th> 
         
            </tr>
            <%
            ResultSet rs1 = db.getFacilityCenter(rs.getString(1));
            while(rs1.next()){
            %>
            <tr class="row_titles">
                <td width="145"  align="center" ><%= rs1.getString(2)%></td>
                <td width="305"  align="center"><%= rs1.getString(3)%></td>
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