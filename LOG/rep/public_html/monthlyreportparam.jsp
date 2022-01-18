<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <link type="text/css" rel="stylesheet" href="resources/css/prenatal.css"/>
    </head>
    <body>
    
       
        <form action="monthlyreportresults.jsp" method="POST">
        <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <tr class="row_titles">
                <td width="50%" colspan="2">
                <b>Faclility</b> &nbsp;&nbsp;
                <select name="p_ctf">
                <%
                LogisticsReportsClass db = new LogisticsReportsClass();
                ResultSet rs = db.getFacilityCenters();
                while(rs.next()){
                    out.println("<option value="+rs.getString(1)+">"+rs.getString(3)+"</option>");
                }
                %>
                </select>
                
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                  &nbsp;&nbsp;
                <b>Year</b> &nbsp;&nbsp;
                <select name="p_year">
                 <option value="2000">2000</option>
                 <option value="2001">2001</option>
                 <option value="2002">2002</option>
                 <option value="2003">2003</option>
                 <option value="2004">2004</option>
                 <option value="2005">2005</option>
                 <option value="2006">2006</option>
                 <option value="2007">2007</option>
                 <option value="2008">2008</option>
                 <option value="2009">2009</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                  &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                   &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                <b>Month :</b>
                         &nbsp;&nbsp;
                         
                        <select name="p_month">
                            <option value="01">01</option>
                            <option value="02">02</option>
                            <option value="03">03</option>
                            <option value="04">04</option>
                            <option value="05">05</option>
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>
                    </td>
            </tr>
            <tr class="row_titles">
            <td colspan="2">
            <input type="submit" value="SHOW"/>
            
            </td>
            </tr>
        </table>
            
        </form>
    </body>
</html>