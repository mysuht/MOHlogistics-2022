<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass,java.sql.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <link type="text/css" rel="stylesheet" href="resources/css/prenatal.css"/>
    </head>
    <body>
    
       
        <form action="faildlogonsresults_en.jsp" method="POST">
        <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <tr class="row_titles">
                <td width="50%" colspan="2"><b>From :</b> &nbsp;&nbsp;
                <input type="text" name="s_date"/>&nbsp;&nbsp;&nbsp;&nbsp;(dd/mm/yyyy)
                 &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                  &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                   &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                <b>To :</b>
                         &nbsp;&nbsp;
                         
                        <input type="text" name="e_date"/>&nbsp;&nbsp;&nbsp;&nbsp;(dd/mm/yyyy)
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