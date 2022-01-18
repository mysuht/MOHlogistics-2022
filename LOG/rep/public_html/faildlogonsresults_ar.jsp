<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass,java.sql.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link type="text/css" rel="stylesheet" href="resources/css/prenatal.css"/>
    </head>
    <body dir="rtl">
      
   
         <hr align="center" width="100%" size="1">
         <form action="faildlogonsparam_ar.jsp" method="POST">
        <table cellspacing="2" cellpadding="3" border="0" width="100%">
        
            <tr>
                <th width="174" class="col_titles_2">
                    اسم المستخدم
                </th>
                <th width="147" class="col_titles_2">
                    كلمة المرور
                </th>
                <th width="305" class="col_titles_2">
                    وقت الدخول
                </th>
                <th width="121" class="col_titles_2">
                    التسلسل
                </th>
                <th width="322" class="col_titles_2">
                    اسم الجهاز
                </th>
                <th width="267" class="col_titles_2">
                    اسم الشبكة
                </th>
                
            </tr>
            </table>
            <hr align="center" width="100%" size="1">
            <table cellspacing="2" cellpadding="3" border="0" width="100%">
            <%
            LogisticsReportsClass db =new LogisticsReportsClass();
           String s_d,t_d;
           s_d = request.getParameter("s_date");
           t_d = request.getParameter("e_date");
           ResultSet rs = db.getFaildLogons(s_d,t_d);
            while(rs.next()){
            %>
            <tr class="row_titles">
                <td width="174" align="center"><%= rs.getString(1)%></td>
                <td width="147"  align="center" ><%= rs.getString(2)%></td>
                <td width="305"  align="center"><%= rs.getString(3)%></td>
                <td width="121"  align="center"><%= rs.getString(4)%></td>
                <td width="322"  align="center"><%= rs.getString(5)%></td>
                <td width="267"  align="center"><%= rs.getString(6)%></td>
            </tr>
            <%}%>
        </table>
    
        <table cellspacing="2" cellpadding="3" border="0" width="100%">
        <tr class="row_titles">
        <td colspan="6" >
                        <input type="submit" value="رجوع"/>
                    </td>
        </tr>
         </table>
     </form>    
    
    </body>
</html>