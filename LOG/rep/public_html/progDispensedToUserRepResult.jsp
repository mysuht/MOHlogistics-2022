<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    <%
    DecimalFormat df = new DecimalFormat("###,###.#");
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String qra = "";
    String dat = request.getParameter("dat");
    String quart = "";
    if(dat.equals("q")){
    quart = request.getParameter("p_quart");
    }if(dat.equals("m")){
    quart = request.getParameter("p_mon");
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
    
    int qr = request.getParameter("p_quart")==null?null:Integer.parseInt(request.getParameter("p_quart")) ;
    String year = request.getParameter("p_year");
    
    LogisticsReportsClass db = new LogisticsReportsClass();
    
    switch(qr){
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
    
    
    <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Program Dispensed To User Report <br/>
     Report Period:
    <%
    if(quart.equals("0")){
    out.println("Annual ,"+year+"<br/>");
    }else{
    if(dat.equals("m")){
    out.println(quart+", "+year+"<br/>" );
    }if(dat.equals("q")){
    out.println(quart+" Quarter, "+year+"<br/>" );
    }
    if(dat.equals("hq")){
    out.println(hq+"Half Year, "+year+"<br/>" );    
    }    
    if(dat.equals("u")){
    out.println(fM+"/"+fY+"   -   "+tM+"/"+tY +"<br/>");
    }
    }
    %>
   <%
    ResultSet rs12 = db.getFacTypesMainName(type);
    rs12.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }if(!type.equals("0") && Integer.parseInt(type) < 100){
    String m[] = request.getParameterValues("p_cen");
    if(m.length >=1 || m.length <= 4){ 
    out.println(rs12.getString(2));
//    out.println("<br/>");
//    String x = "";
//    for(int b=0; b < (m.length <= 4 ? m.length : 3); b++){
//    x += db.getDirName(m[b])+"  "+db.getDirCode(m[b]) + ", ";
//    }
//    out.println(x);
    } 
    else{
    out.println(rs12.getString(2)+"<br/>"+"All");
    }
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+"" )  +" </br>");
    String m[] = request.getParameterValues("p_cen");
    if(m.length >=1 || m.length <= 4) { 
//    String x = "";
//    for(int b=0; b < (m.length <= 4 ? m.length : 3); b++){
//    x += db.getDirName(m[b])+"  "+db.getDirCode(m[b]) + ", ";
//    }
//    out.println(x);
    
    } 
    
    }
    %>
  
   <br/>

    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <div style="clear:both">
    All Programs 
    
    </div>
    <br/>
<%

if(true){
String centers[] = request.getParameterValues("p_cen");
ResultSet rs = db.getProgDisToUserRep(quart, year,dat,fY,tY, fM, tM, hq);
%>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
Dispensed
</td>
</tr>
<%
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString("pro_name") %>
</td>
<td align="center">
<%= rs.getString("pro_dose") %>
</td>
<td align="center">

<% 
//edittt
//if(!dat.equals("hq")){
//out.println( df.format(rs.getDouble("dispensed")) );
out.println(
 df.format(
db.Dispensed(quart, year, dat,  rs.getString("prod_id"), fY, tY, fM, tM, hq, centers, type)
)
);
//}else{
//out.println(df.format(db.getDispensedToUserRepCountWholeCountry(quart, year, db.getprodId(rs.getString(1)), dat, fY, tY, fM, tM, type, hq)));
//}
%>
</td>
</tr>


<%}%>
</table>
<%
}
%>



<%
db.s();
%>
</body>
</html>