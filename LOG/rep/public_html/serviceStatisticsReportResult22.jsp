<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.FacilityTemplate, moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*,java.util.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    <%
   // System.out.println("sssssssssssssssssssssssssssss");
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
    } if(dat.equals("m")){
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
    String prod[] = request.getParameterValues("p_prod");
    //String dir = request.getParameter("p_cen");
    LogisticsReportsClass db = new LogisticsReportsClass();
    ResultSet rsProd = db.getProducts();
    ResultSet rsDir = db.getDirectorates();
   
    DecimalFormat df = new DecimalFormat("###,###.##");
    DecimalFormat df1 = new DecimalFormat("###,###.##");
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
    Service Statistics Report <br/>
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
    out.println(rs12.getString(2));
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(     db.getGroupName(Integer.parseInt(type)-100+"") );
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
<%
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC xxxxxxxxxxxxxxxxxxxxxxxx ");
String centers[] = request.getParameterValues("p_cen");
List<FacilityTemplate> facilityList = db.getDirectoratesList(centers);
for(int pro=0; pro<prod.length;pro++){
%>

<%
if(pro!=0){
%>
<div class="ba"></div>
<br/><br/>
<%}%>
<%
Vector vdir = new Vector();
Vector vtype = new Vector();
Vector vNew = new Vector();
Vector vNewPer = new Vector();
Vector vCont = new Vector();
Vector vContPer = new Vector();
int pp = 0;
//while (rsDirs.next()){
for(FacilityTemplate directorate: facilityList){


pp++;
//if( (directorate.getFacilityId()+"").equals("498")){
//continue;
//}
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC 0 xxxxxxxxxxxxxxxxxxxxxxxx ");
List<FacilityTemplate> serviceStatisticsReportList =
db.serviceStatisticsReportList(prod[pro], quart, year, dat, fY, tY, fM, tM, hq, centers, directorate.getFacilityId()+"", type ) ;
FacilityTemplate supplier = serviceStatisticsReportList.get(0);
serviceStatisticsReportList.remove(supplier);

if(serviceStatisticsReportList.size() == 0)continue;


//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC 1 xxxxxxxxxxxxxxxxxxxxxxxx ");
double newVisitsCountrySupplier = Double.valueOf(supplier.getNewUsersCountry() == null ? "0" : supplier.getNewUsersCountry());
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC 2 xxxxxxxxxxxxxxxxxxxxxxxx ");
double contVisitsCountrySupplier = Double.valueOf(supplier.getContUsersCountry() == null ? "0" : supplier.getContUsersCountry());
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC 3 xxxxxxxxxxxxxxxxxxxxxxxx ");
double newVisitsDispensedSupplier = Double.valueOf(supplier.getNewUsers() == null ? "0" : supplier.getNewUsers());
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx ABC 4 xxxxxxxxxxxxxxxxxxxxxxxx ");
double contVisitsDispensedSupplier = Double.valueOf(supplier.getContUsers() == null ? "0" : supplier.getContUsers());
//double newVisitsCountry = db.getNewVisitsByDateProduct(prod[pro], quart, year,dat,fY,tY, fM, tM,type, hq);
//double contVisitsCountry =  db.getContVisitsByDateProduct(prod[pro], quart, year,dat,fY,tY, fM, tM,type, hq);
//double newVisitsDispensed = db.getNewVisitsByDateProductD(prod[pro], quart, year,dat,rsDirs.getString(1),fY,tY, fM, tM,type, hq);
//double contVisitsDispensed = db.getContVisitsByDateProductD(prod[pro], quart, year,dat,rsDirs.getString(1),fY,tY, fM, tM,type, hq);


//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 1 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
%>
<br/>
<br/>
<div >
<div align="center" style="clear:both;font-weight:bold; font-size:14pt;">
<%= db.getProdName(prod[pro])+"  "+ db.getProdDose(prod[pro]) %>
</div>


<br/>
 <div style="clear:both">
    Contraceptives <br/>
    New Users For Country: <span style="background-color:yellow"> <%= df.format(newVisitsCountrySupplier) %> </span> &nbsp;&nbsp;&nbsp;&nbsp; Continuing Users For Country <span style="background-color:yellow"> <%= df.format(contVisitsCountrySupplier) %></span>
    </div>
<%
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 1.1 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
//System.out.println(" Supplier Id is : xxxxxxxxxxxxxxxxxxxxx : " + supplier.getFacilityId());
%>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles" >
<td align="center" >
Directorate
</td>
<td align="center">
Type
</td>
<td align="center">
New Users  
</td>
<td align="center">
New Users %
</td>
<td align="center">
Cont. Users
</td>
<td align="center">
Cont. Users %
</td>
</tr>
<tr class="row_titles" style="font-weight:bold">
<td align="center">
<%= db.getDirName(supplier.getFacilityId()+"") %>
<%
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 1.2 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
%>
</td>
<td align="center">
<%= db.getDirType(supplier.getFacilityId()+"") %>
</td>

<td align="center">
<%= df.format(newVisitsDispensedSupplier) %> 
</td>
<td align="center">
<%
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 2 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
out.println(  df.format(newVisitsDispensedSupplier / newVisitsCountrySupplier*100) + "%");
//out.println("0");
%>
</td>
<td align="center">
<%= df.format(contVisitsDispensedSupplier)%> 
</td>
<td align="center">
<% out.println(df.format(contVisitsDispensedSupplier / contVisitsCountrySupplier*100) + "%" );
//out.println(0);
%>
</td>
<%
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 3 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
vdir.addElement(db.getDirName(supplier.getFacilityId()+""));
vtype.addElement(db.getDirType(supplier.getFacilityId()+""));
vNew.addElement(newVisitsDispensedSupplier);
vNewPer.addElement(100);
vCont.addElement(contVisitsDispensedSupplier);
vContPer.addElement(100);
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 4 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
%>

</tr>
<tr><td >Dispensed To: </td></tr>
<%
//ResultSet rsDispensed = db.getDirNameDispensed(supplier.getFacilityId()+"", year, quart, prod[pro], dat,fY,tY, fM, tM,type, hq);
int dd = 0;
for(FacilityTemplate ft: serviceStatisticsReportList){
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 5 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
double newVisitsCountry = Double.valueOf(ft.getNewUsersCountry());
double contVisitsCountry = Double.valueOf(ft.getContUsersCountry());
double newVisitsDispensed =Double.valueOf(ft.getNewUsers());
double contVisitsDispensed =Double.valueOf(ft.getContUsers());
//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXX STAGE : 6 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
//while(rsDispensed.next()){
dd++;
%>

<%if(dd!=0 && dd%17 == 0){%>
</table>
<div class="ba"> </div>
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
    <br/>
    <div align="center" style="clear:both;font-weight:bold; font-size:14pt;">
<%= db.getProdName(prod[pro])+"  "+ db.getProdDose(prod[pro]) %>
</div>
<br/>
<div style="clear:both">
   
    New Users For Country: <span style="background-color:yellow"> <%= df.format(newVisitsCountry) %> </span> &nbsp;&nbsp;&nbsp;&nbsp; Continuing Users For Country <span style="background-color:yellow"> <%= df.format(contVisitsCountrySupplier) %></span>
    </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
 


<tr class="col_titles" >
<td align="center" >
Directorate
</td>
<td align="center">
Type
</td>
<td align="center">
New Users  
</td>
<td align="center">
New Users %
</td>
<td align="center">
Cont. Users
</td>
<td align="center">
Cont. Users %
</td>
</tr>
<tr class="row_titles" style="font-weight:bold">
<td align="center">
<%= db.getDirName(supplier.getFacilityId()+"") %>

</td>
<td align="center">
<%= db.getDirType(supplier.getFacilityId()+"") %>
</td>

<td align="center">
<%= df.format(newVisitsDispensedSupplier) %> 
</td>
<td align="center">
<%
out.println(df.format(newVisitsDispensedSupplier / newVisitsCountrySupplier * 100) + "%");
//out.println(0);
%>
</td>
<td align="center">
<%= df.format(contVisitsDispensedSupplier)%> 
</td>
<td align="center">
<%
 out.println(df.format(contVisitsDispensedSupplier / contVisitsCountrySupplier * 100) + "%");
//out.println(0);
%>
</td>
</tr>
<tr><td >Dispensed To: </td></tr>

<%}%>
<tr class="row_titles">
<td align="center">
<%= ft.getFacilityName() %>
</td>

<td align="center">
<%= db.getDirType(ft.getFacilityId()+"") %>
</td>
<td align="center">
<%= df.format(newVisitsDispensed) %> 
</td>
<td align="center">
<%=  df.format(newVisitsDispensed / newVisitsCountry * 100) + "%" %>
</td>
<td align="center">
<%= df.format(contVisitsDispensed)%> 
</td>
<td align="center">
<%= df.format(contVisitsDispensed / contVisitsCountry * 100) + "%" %>
</td>
</tr>
<%}%>
<tr class="col_titles">
<td align="center" colspan="2">
Total for <%= db.getDirName(supplier.getFacilityId() + "") %>
</td>
<td align="center">
<%= df.format(newVisitsDispensedSupplier) %>
</td>
<td align="center">  <%=100+"%"%></td>
<td align="center">
<%= df.format(contVisitsDispensedSupplier) %>
</td>
<td align="center">   <%=100+"%"%></td>
</tr>
</table>
<%
}
}
%>
</div>
</body>
</html>