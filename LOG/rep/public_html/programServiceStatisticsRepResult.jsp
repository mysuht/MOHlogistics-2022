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
    String cen1 [] = request.getParameterValues("p_cen");
    DecimalFormat df = new DecimalFormat("###,###.#");
    String qra = "";
    String quart = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String dat = request.getParameter("dat");
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
    Program Service Statistics Report <br/>
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
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    
    out.println(rs12.getString(2)+"<br/>");
    if(cen1.length==1)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0]));
    if(cen1.length==2)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1]));
    if(cen1.length==3)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1])+", "+db.getDirName(cen1[2])+"   "+db.getDirCode(cen1[2]));
     if(cen1.length==4)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1])+", "+db.getDirName(cen1[2])+"   "+db.getDirCode(cen1[2])+", "+db.getDirName(cen1[3])+"   "+db.getDirCode(cen1[3]));
    
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+"")+"<br/>");
    if(cen1.length==1)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0]));
    if(cen1.length==2)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1]));
    if(cen1.length==3)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1])+", "+db.getDirName(cen1[2])+"   "+db.getDirCode(cen1[2]));
     if(cen1.length==4)
    out.println(db.getDirName(cen1[0])+"   "+db.getDirCode(cen1[0])+", "+db.getDirName(cen1[1])+"   "+db.getDirCode(cen1[1])+", "+db.getDirName(cen1[2])+"   "+db.getDirCode(cen1[2])+", "+db.getDirName(cen1[3])+"   "+db.getDirCode(cen1[3]));
    
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
    <div style="clear:both">
    All Programs 
    
    </div>
<br/><br/><br/><br/>

<%
if(type.equals("0")){
 //ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM, hq);
%>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" class="tps">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
New Users
</td>
<td align="center">
Cont. Users.
</td>
</tr>
<%
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getString(2) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(3)) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(4)) %>
</td>

</tr>


<%}%>
</table>
<br/>
<br/>
<br/>
<br/>








<%

////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
ResultSet rsDirs = db.getDirectorates2();
int i = 0;
while(rsDirs.next()){
//ResultSet  rsall = db.getProgramServicesStatisticsRepG(quart, year,dat,fY,tY, fM, tM,type, hq, cen, rsDirs.getString(1));
ResultSet  rsall = db.getProgramServicesStatisticsRepGall(quart, year,dat,fY,tY, fM, tM,type, hq, rsDirs.getString(1));

// if(db.type_hierarchyDir(cen[0]).equals("2")){
//           rsall = db.getProgramServicesStatisticsRepGT(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
// }
 if(!rsall.next()){
continue;
}
// ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM, hq);
%>


 <% if (i !=0){%>
        <div style="clear:both; font-weight:bold; text-align: center;" align="center">
   <div style="float:left;width:200pt;">
   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   
  </div>
   <div style="float:left; width:400pt; text-align:right">
    &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>
<div style="width:200pt;clear:both;float:left">
   &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
</div>
<div style="float:left; width:200pt; text-align:right">
        &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>  
   <div style="float:left; width:200pt; text-align:right">
    &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>  
   
   
    <div style="float:left;width:200pt;">
   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   
  </div>
   <div style="float:left; width:400pt; text-align:right">
    &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>
<div style="width:200pt;clear:both;float:left">
   &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
</div>
<div style="float:left; width:200pt; text-align:right">
        &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>  
   <div style="float:left; width:200pt; text-align:right">
    &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
   </div>  
   
   
   
    </div>
        
        <%}i++;%>

<br/>
<div align="center" style="clear:both; font-weight:bold;"> <%= db.getDirName(rsDirs.getString(1)) %> </div>
<br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" class="tps">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
New Users
</td>
<td align="center">
Cont. Users.
</td>
</tr>
<%
// rsall = db.getProgramServicesStatisticsRepG(quart, year,dat,fY,tY, fM, tM,type, hq, cen, rsDirs.getString(1));
 rsall = db.getProgramServicesStatisticsRepGall(quart, year,dat,fY,tY, fM, tM,type, hq, rsDirs.getString(1));
while(rsall.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rsall.getString(1) %>
</td>
<td align="center">
<%= rsall.getString(2) %>
</td>
<td align="center">
<%= df.format(rsall.getDouble(3)) %>
</td>
<td align="center">
<%= df.format(rsall.getDouble(4)) %>
</td>

</tr>


<%}%>
</table>
<%}%>



<%}%>















<%
if(!type.equals("0") && Integer.parseInt(type) <100 ){
String cen [] = request.getParameterValues("p_cen");
 ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 if(db.type_hierarchyDir(cen[0]).equals("2")){
 rs = db.getProgramServicesStatisticsRepGT(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 }
// ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM, hq);
%>
<br/>


<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" class="tps">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
New Users
</td>
<td align="center">
Cont. Users.
</td>
</tr>
<%
int rloop=0;
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getString(2) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(3)) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(4)) %>
</td>

</tr>


<%}%>
</table>
<%}%>







































<%
if(!type.equals("0") && Integer.parseInt(type) >100 ){
ResultSet rsDirs = db.getDirectorates2();
String cen [] = request.getParameterValues("p_cen");
//////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
 ResultSet rsp = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 
 if(db.type_hierarchyDir(cen[0]).equals("2")){
 rsp = db.getProgramServicesStatisticsRepGT(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 }

// ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM, hq);
%>
<br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" class="tps">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
New Users
</td>
<td align="center">
Cont. Users.
</td>
</tr>
<%
 rsp = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
while(rsp.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rsp.getString(1) %>
</td>
<td align="center">
<%= rsp.getString(2) %>
</td>
<td align="center">
<%= df.format(rsp.getDouble(3)) %>
</td>
<td align="center">
<%= df.format(rsp.getDouble(4)) %>
</td>

</tr>


<%}%>
</table>




<br/>
<br/>
<br/>


























<%

////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
while(rsDirs.next()){
ResultSet  rs = db.getProgramServicesStatisticsRepG(quart, year,dat,fY,tY, fM, tM,type, hq, cen, rsDirs.getString(1));

 if(db.type_hierarchyDir(cen[0]).equals("2")){
 rs = db.getProgramServicesStatisticsRepGT(quart, year,dat,fY,tY, fM, tM,type, hq, cen);
 }
 if(!rs.next()){
continue;
}
// ResultSet rs = db.getProgramServicesStatisticsRep(quart, year,dat,fY,tY, fM, tM, hq);
%>
<br/>
<br/>
<br/>
<h3 align="center"> <%= db.getDirName(rsDirs.getString(1)) %> </h3>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" class="tps">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>

<td align="center">
New Users
</td>
<td align="center">
Cont. Users.
</td>
</tr>
<%
 rs = db.getProgramServicesStatisticsRepG(quart, year,dat,fY,tY, fM, tM,type, hq, cen, rsDirs.getString(1));
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getString(2) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(3)) %>
</td>
<td align="center">
<%= df.format(rs.getDouble(4)) %>
</td>

</tr>


<%}%>
</table>
<%}%>



<%}%>


<%
db.s();
%>
</body>
</html>