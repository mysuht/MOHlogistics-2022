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
    
    String dir = request.getParameter("p_dir");
    String dispensedFac[] = request.getParameterValues("p_cen");
    String prod = request.getParameter("p_prod");
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
    int qr = request.getParameter("p_quart")==null?null:Integer.parseInt(request.getParameter("p_quart"));
    String year = request.getParameter("p_year");
   
    LogisticsReportsClass db = new LogisticsReportsClass();

    String qra = "";
    switch(qr){
    case 1: qra = "1st";
    break;
    case 2: qra = "2nd";
    break;
    case 3: qra = "3rd";
    break;
    case 4: qra = "4th";
    }
    DecimalFormat df = new DecimalFormat("###,###.##");
    DecimalFormat df1 = new DecimalFormat("###,###.##");

    %>

 <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Facililty Services Statistics Report <br/>
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
    ResultSet rs11 = db.getFacTypesMainName(type);
    rs11.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    out.println(rs11.getString(2));
    }
    if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
     
   
    
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
    <% // for specific
    if(!type.equals("0")){
    
    for(int i=0; i< dispensedFac.length; i++){
       double sumNew = db.getFacilityServicesStatisticsRepMainSum(dispensedFac[i], quart, year,1,dat,fY,tY, fM, tM,type, hq);
       double sumCont = db.getFacilityServicesStatisticsRepMainSum(dispensedFac[i], quart, year,2,dat,fY,tY, fM, tM,type, hq);
    ResultSet rs = db.getFacServiceStatisticsRep(dispensedFac[i], quart, year,dat,fY,tY, fM, tM,type, hq);
    if(db.type_hierarchyDir(dispensedFac[i]).equals("3")){
     rs = db.getFacServiceStatisticsRep3(dispensedFac[i], quart, year,dat,fY,tY, fM, tM,type, hq);
    }
    
        %>
        <br/>
        <br/>
        
        
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
        
        <%}%>
        
        
    <div style="clear:both;font-size:11pt;font-weight:bold" align="center">
   <%= db.getDirName(dispensedFac[i]) + " -- " + db.getDirCode(dispensedFac[i])%> </div>
   Supplier Facility: <%= db.getDirName(db.getDirSupInfo(dispensedFac[i],4)) %>
   <br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" class="ba">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>
<td align="center">
New Users
</td>
<td align="center">
New Users For Higher Level
</td>
<td align="center">
% New Users</td>

<td align="center">
Cont. Users
</td>
<td align="center">
Cont. Users For Higher Level
</td>
<td align="center">
% Cont. Users
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
<%= db.getProdDose(rs.getString(3)) %>
</td>

<td align="center">
<%= df.format(rs.getDouble(2)) %>
</td>
<td align="center">
<% if(db.type_hierarchyDir(dispensedFac[i]).equals("2")){%>
<%= Math.round(db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq)) %>
<%}else{%>
<%= df.format(db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),2, dat,fY,tY, fM, tM,type, hq)) %>
<%}%>

</td>
<td align="center">
<% if(db.type_hierarchyDir(dispensedFac[i]).equals("2")){%>
<%= db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(2)/ db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) )*100)+"%" %>
<%}else{%>
<%= db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),2, dat,fY,tY, fM, tM,type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(2)/ db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),2, dat,fY,tY, fM, tM,type, hq) )*100)+"%" %>
<%}%>
</td>
<td align="center">
<%= df.format(rs.getDouble(4)) %>
</td>
<td align="center">
<% if(db.type_hierarchyDir(dispensedFac[i]).equals("2")){%>
<%= Math.round(db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq)) %>
<%}else{%>
<%= df.format(db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),3,dat,fY,tY, fM, tM,type, hq)) %>
<%}%>
</td>
<td align="center">
<% if(db.type_hierarchyDir(dispensedFac[i]).equals("2")){%>
<%= db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(4)/ db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq))*100)+"%" %>
<%}else{%>

<%= db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),3,dat,fY,tY, fM, tM,type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(4)/ db.getFacServiceStatisticsRepMain(db.getDirSupInfo(dispensedFac[i],4), quart, year,rs.getString(3),3,dat,fY,tY, fM, tM,type, hq))*100)+"%" %>


<%}%>
</td>


</tr>



 <%
 //System.out.println(db.getFacServiceStatisticsRepMain(dir, quart, year,rs.getString(3),2,dat));
 //System.out.println("bast is "+rs.getDouble(2)+ " maqam is "+sum);
 } // while%>
</table>
 
  <%
  }// for dispensed Facilities
  } //specific types
  // ------------------------------------separation
  %>  
  
  
  
   <%
    if(type.equals("0")){
    ResultSet rsDir = db.getDirectorates();
    int rloop = 0;
    while(rsDir.next()){
    if(rsDir.getString(1).equals("498")){
    continue;
    }
    
    
       double sumNew = db.getFacilityServicesStatisticsRepMainSum(rsDir.getString(1), quart, year,1,dat,fY,tY, fM, tM,type, hq);
       double sumCont = db.getFacilityServicesStatisticsRepMainSum(rsDir.getString(1), quart, year,2,dat,fY,tY, fM, tM,type, hq);
    ResultSet rs = db.getFacServiceStatisticsRep(rsDir.getString(1), quart, year,dat,fY,tY, fM, tM,type, hq);
    
        %>
       
        <% if (rloop !=0){%>
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
        
        <%}rloop++;%>
        <br/>
    <div style="clear:both;font-size:11pt;font-weight:bold" align="center">
   <%= db.getDirName(rsDir.getString(1)) + " -- " + db.getDirCode(rsDir.getString(1))%> </div>
   Supplier Facility: <%= db.getDirName(db.getDirSupInfo(rsDir.getString(1),4)) %>
   <br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" class="ba">
<tr class="col_titles">
<td align="center" colspan="2">
Commodities
</td>
<td align="center">
New Users
</td>
<td align="center">
New Users For Higher Level
</td>
<td align="center">
% New Users</td>

<td align="center">
Cont. Users
</td>
<td align="center">
Cont. Users For Higher Level
</td>
<td align="center">
% Cont. Users
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
<%= db.getProdDose(rs.getString(3)) %>
</td>

<td align="center">
<%= df.format(rs.getDouble(2)) %>
</td>
<td align="center">

<%= Math.round(db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq)) %>

</td>
<td align="center">
<%= db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(2)/ db.getNewVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) )*100)+"%" %>
</td>
<td align="center">
<%= df.format(rs.getDouble(4)) %>
</td>
<td align="center">
<% if(db.type_hierarchyDir(rsDir.getString(1)).equals("2")){%>
<%= Math.round(db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq)) %>
<%}else{%>
<%= df.format(db.getFacServiceStatisticsRepMain(db.getDirSupInfo(rsDir.getString(1),4), quart, year,rs.getString(3),3,dat,fY,tY, fM, tM,type, hq)) %>
<%}%>
</td>
<td align="center">
<%= db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq) == 0.0 ? 0 : df1.format((rs.getDouble(4)/ db.getContVisitsByDateProduct(rs.getString(3), quart, year, dat, fY, tY, fM, tM, type, hq))*100)+"%" %>
</td>


</tr>



 <%
 //System.out.println(db.getFacServiceStatisticsRepMain(dir, quart, year,rs.getString(3),2,dat));
 //System.out.println("bast is "+rs.getDouble(2)+ " maqam is "+sum);
 } // while%>
</table>
 
  <%
  }// for dispensed Facilities
  } //specific types
  %>  
  
  
    <%
db.s();
%>
    </body>
</html>