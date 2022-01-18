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
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";
String type = request.getParameter("p_dir");
    String qra="";
    String dat = request.getParameter("dat");
    if(dat.equals("m")){
    qra = request.getParameter("p_mon");
    }if(dat.equals("q")){
    qra = request.getParameter("p_quart"); 
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
   
    String year = request.getParameter("p_year");
    String prod[] = request.getParameterValues("p_prod");
    LogisticsReportsClass db = new LogisticsReportsClass();
    
      DecimalFormat df1 = new DecimalFormat("###,###.#");
    %>
    <div class="main_div" style="clear:both;" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
   CYP Report <br/>
   Report Period:
    <%
    if(qra.equals("0")){
    out.println("Annual ,"+year+"<br/>");
    }else{
    if(dat.equals("m")){
    out.println(qra+", "+year+"<br/>" );
    
    }if(dat.equals("q")){
    out.println(qra+" Quarter, "+year+"<br/>" );
    
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
    ResultSet rs11 = db.getFacTypesMainName(type);
    rs11.next();
    
    if(type.equals("0")){
    out.println("All Facility Types."+"<br/>");
    }else{
    if(Integer.parseInt(type) < 100){
    out.println(rs11.getString(2)+ "<br/>");
    String faces = "";
    String cen[] = request.getParameterValues("p_cen");
//    for(int p=0; p<cen.length;p++){
//    if(p == 0){
//    faces = db.getDirName(cen[p])+"--"+db.getDirCode(cen[p]);
//    }else{
//    faces += ", "+db.getDirName(cen[p])+"--"+db.getDirCode(cen[p]);
//    }
//    }
   // out.println(faces);
    }else{
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    }
    %>
   
    
    </div>
    <br/>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
   
<br/><br/><br/><br/>
 <div style="clear:both">
    Contraceptives 
    
    </div>

<%
if(type.equals("0")){
//if(Integer.parseInt(type) < 100){
%>


<%
for(int p=0; p<prod.length;p++){
ResultSet rs = db.getCypFactorRep(prod[p], qra, year,dat,fY,tY, fM, tM,type, hq);
%>

<% if(p!=0){ %>
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
<%}%>
<br/>
<div style="clear:both;font-weight:bold; font-size:12pt;" align="center">  <%= db.getProdName(prod[p])+"  "+ db.getProdDose(prod[p]) %> </div>

<div>

<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" >
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
</td>
<td align="center">
Dispensed
</td>
<td align="center">
CYP Factor
</td>
<td align="center">
CYP Value
</td>



</tr>
<%
double dispensed = 0;
double cypVal = 0;
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getInt(3) %>
</td>
<td align="center">
<%= df1.format(rs.getDouble(4)) %>
<%
dispensed +=  rs.getInt(4);
%>
</td>
<td align="center">
<%= rs.getString(5) %>
</td>
<td align="center">
<%
if(db.getProdName(prod[p]).equals("IUD") || db.getProdName(prod[p]).equals("Norplant") || db.getProdName(prod[p]).equals("Implanon")){
%>
<%= df1.format(Math.round(Double.valueOf(rs.getDouble(4) * rs.getDouble(5))))   %>
<%
cypVal +=  Math.round(Double.valueOf(rs.getDouble(4) * rs.getDouble(5)));
}else{%>
<%= df1.format(Math.round(Double.valueOf(rs.getDouble(4) / rs.getDouble(5)))) %>
<%
cypVal += Math.round(Double.valueOf(rs.getDouble(4) / rs.getDouble(5))) ;
}%>
</td>
</tr>

<%}%>
<tr class="col_titles">
<td align="center" colspan="2">

</td>
<td align="center">
<%= df1.format(dispensed) %>
</td>
<td>  </td>
<td align="center">
<%= df1.format(Double.valueOf(cypVal))  %>
</td>

</tr>
</table>
</div>
<div class="ba" style="margin-bottom:20pt;" > </div>
<%
} // products loop
//}
} // type 0 -> All
%>







<%
/////////////////////////////////////// for facilities
String cen [] = request.getParameterValues("p_cen");
if(!type.equals("0")){
if(Integer.parseInt(type) < 100 && !type.equals("7")){
int sp = 0;
for(int p=0; p<prod.length;p++){
ResultSet rs = db.getCypFactorRep(prod[p], qra, year,dat,fY,tY, fM, tM,type, hq,cen);
%>





<% if(p!=0){ %>
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
<%}%>




<div style="clear:both;font-weight:bold; font-size:12pt;" align="center">  <%= db.getProdName(prod[p])+"  "+ db.getProdDose(prod[p]) %> </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
</td>
<td align="center">
Dispensed
</td>
<td align="center">
CYP Factor
</td>
<td align="center">
CYP Value
</td>



</tr>
<%
double dispensed = 0;
double cypVal = 0;
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getInt(3) %>
</td>
<td align="center">
<%= df1.format(rs.getDouble(4)) %>
<%
dispensed +=  rs.getInt(4);
%>
</td>
<td align="center">
<%= rs.getString(5) %>
</td>
<td align="center">
<%
if(db.getProdName(prod[p]).equals("IUD") || db.getProdName(prod[p]).equals("Norplant") || db.getProdName(prod[p]).equals("Implanon")){
%>
<%= df1.format( Math.round( Double.valueOf(rs.getDouble(4) * rs.getDouble(5))))   %>
<%
cypVal += Math.round( Double.valueOf(rs.getDouble(4) * rs.getDouble(5)));
}else{%>
<%= df1.format( Math.round( Double.valueOf(rs.getDouble(4) / rs.getDouble(5)))) %>
<%
cypVal += Math.round(Double.valueOf(rs.getDouble(4) / rs.getDouble(5))) ;
}%>
</td>
</tr>

<%}%>
<tr class="col_titles">
<td align="center" colspan="2">

</td>
<td align="center">
<%= df1.format(dispensed) %>
</td>
<td>  </td>
<td align="center">
<%= df1.format(Double.valueOf(cypVal))  %>
</td>

</tr>
</table>
<% if(sp %2 == 0 && sp!=0){%>

<div class="ba" > </div>

<%}sp++;%>
<%
} 
%>
















<%

// products loop
}else if(Integer.parseInt(type) < 100 && type.equals("7")){
for(int p=0; p<prod.length;p++){
%>
<% if(p!=0){ %>
 <div style="clear:both; font-weight:bold; " align="center">
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
<%}%>



<%
//ResultSet rsDirs = db.getDirectoratesD();
for(int cc=0; cc<cen.length; cc++){
ResultSet rs = db.getCypFactorRepByDirs(prod[p], qra, year,dat,fY,tY, fM, tM,type, hq,cen[cc]);

%>





<% if(cc!=0){ %>
 <div style="clear:both; font-weight:bold; " align="center">
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
<%}%>

<div>
<br/>
<div style="clear:both;font-weight:bold; font-size:12pt;" align="center" > <%= db.getDirName(cen[cc]) %>  </div>

<div style="clear:both;font-weight:bold; font-size:12pt;" align="center">  <%= db.getProdName(prod[p])+"  "+ db.getProdDose(prod[p]) %> </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
</td>
<td align="center">
Dispensed
</td>
<td align="center">
CYP Factor
</td>
<td align="center">
CYP Value
</td>



</tr>
<%
double dispensed = 0;
double cypVal = 0;
while(rs.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getInt(3) %>
</td>
<td align="center">
<%= df1.format(rs.getDouble(4)) %>
<%
dispensed +=  rs.getInt(4);
%>
</td>
<td align="center">
<%= rs.getString(5) %>
</td>
<td align="center">
<%
if(db.getProdName(prod[p]).equals("IUD") || db.getProdName(prod[p]).equals("Norplant") || db.getProdName(prod[p]).equals("Implanon")){
%>
<%= df1.format( Math.round( Double.valueOf(rs.getDouble(4) * rs.getDouble(5))))   %>
<%
cypVal += Math.round( Double.valueOf(rs.getDouble(4) * rs.getDouble(5)));
}else{%>
<%= df1.format( Math.round( Double.valueOf(rs.getDouble(4) / rs.getDouble(5)))) %>
<%
cypVal += Math.round(Double.valueOf(rs.getDouble(4) / rs.getDouble(5))) ;
}%>
</td>
</tr>

<%}%>
<tr class="col_titles">
<td align="center" colspan="2">

</td>
<td align="center">
<%= df1.format(dispensed) %>
</td>
<td>  </td>
<td align="center">
<%= df1.format(Double.valueOf(cypVal))  %>
</td>

</tr>
</table>
</div>
<div class="ba"></div>
<%
} 
}
%>
















<%


}else{ /// groups


%>


































<% ///////////////groupTypes
for(int p=0; p<prod.length;p++){
//ResultSet rs = db.getCypFactorRep(prod[p], qra, year,dat,fY,tY, fM, tM,type, hq,cen);
%>

<% if(p!=0){ %>
 <div style="clear:both; font-weight:bold; " align="center">
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
<%}%>



<br/>
<div style="clear:both;font-weight:bold; font-size:12pt;" align="center">  <%= db.getProdName(prod[p])+"  "+ db.getProdDose(prod[p]) %> </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
</td>
<td align="center">
Dispensed
</td>
<td align="center">
CYP Factor
</td>
<td align="center">
CYP Value
</td>



</tr>
<%
int dispensed = 0;
double cypVal = 0;
ResultSet rsTypes = db.getTypesOfGroup((Integer.parseInt(type)-100));
while(rsTypes.next()){
if( (db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,3) == 0.011111 ))
continue; 
%>
<tr class="row_titles">
<td align="center">
<%=      db.getTypeName( rsTypes.getString(2)) %>
</td>
<td align="center">
<%= Math.round(db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,3)) %>
</td>
<td align="center">
<%= Math.round(db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4) ) %>
<%
dispensed +=  db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4);
%>
</td>
<td align="center">
<%
if(db.getProdName(prod[p]).equals("IUD") || db.getProdName(prod[p]).equals("Norplant") || db.getProdName(prod[p]).equals("Implanon")){
%>
<%=  db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5) %>

<%}else{%>
<%= Math.round( db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5)) %>
<%}%>
</td>
<td align="center">
<%
if(db.getProdName(prod[p]).equals("IUD") || db.getProdName(prod[p]).equals("Norplant") || db.getProdName(prod[p]).equals("Implanon")){
%>
<%= df1.format( Math.round( Double.valueOf(db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4) *   db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5)  )))   %>
<%
cypVal += Math.round( Double.valueOf(   db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4)  *    db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5)  ));
}else{%>
<%= df1.format( Math.round( Double.valueOf(   db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4)  /     db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5) ))) %>
<%
cypVal += Math.round(Double.valueOf( db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,4) /   db.getCypFactorRepIs(prod[p], qra, year,dat,fY,tY, fM, tM,rsTypes.getString(2), hq,cen,5)  )) ;
}%>
</td>
</tr>

<%}%>
<tr class="col_titles">
<td align="center" colspan="2">

</td>
<td align="center">
<%= dispensed %>
</td>
<td>  </td>
<td align="center">
<%= df1.format(Double.valueOf(cypVal))  %>
</td>

</tr>
</table>
<div class="ba"></div>
<%
}%>
















<%
}
} // for facilities 
%>











<%
db.s();
%>
</body>
</html>