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
    
    
    
    
    //String products[] = new String [];
    
    
    
      DecimalFormat df = new DecimalFormat("#.#");
      DecimalFormat df1 = new DecimalFormat("###,###.##");
    String prod [] = request.getParameterValues("p_prod");
  String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String cen[];
    String type = request.getParameter("p_dir");
//    if(type.equals("0")){
//    cen = request.getParameterValues("p_cen");
//    }
    String year = request.getParameter("p_year")==null?null:request.getParameter("p_year");
    String dat = request.getParameter("dat")==null ? null:request.getParameter("dat") ;
     String mon = "";
     String hq = "";
    if(dat.equals("m")){
    mon = request.getParameter("p_mon")==null? null: request.getParameter("p_mon");
    }if(dat.equals("q")){
    mon = request.getParameter("p_quart") == null ? null : request.getParameter("p_quart");
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
    LogisticsReportsClass db = new LogisticsReportsClass();
   
   
   
    %>
    
 <div class="main_div"  >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Aggregate Stock Movement Report - By Level <br/>
    Report Period:
    <%
    if(mon.equals("0")){
    out.println("Annual ,"+year+"<br/>");
    }else{
    if(dat.equals("m")){
    out.println(mon+", "+year+"<br/>" );
    
    }if(dat.equals("q")){
    out.println(mon+" Quarter, "+year+"<br/>" );
    
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
    ResultSet rs = db.getFacTypesMainName(type);
    rs.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
    out.println(rs.getString(2));
    }
    %>
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/> <br/><br/> <br/>
    <%
    // Hear
    if(type.equals("0")){
    int rloop = 0;
    for(int i=0; i<prod.length; i++){
     double cBalTotal = db.getSumClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,null, hq);
    rloop++;
    %>
   <% if ( rloop%3 ==0){%>
   <div class="ba"></div>
   <br/>
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
   
   <%}
   
      %>
    <div align="center" style="width:100%; clear:both; margin-top:10pt;">
    <h3 align="center"> <u> <%= db.getProdName(prod[i])+" --- "+db.getProdDose(prod[i]) %> </u>  </h3> 
    </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center" height="23">
Level
</td>
<td align="center" height="23">
#
</td>
<td align="center" height="23">
Opening Bal.
</td>
<td align="center" height="23">
Receipts
</td>
<td align="center" height="23">
issues
</td>
<td align="center" height="23">
Adjustments
</td>
<td align="center" height="23">
Closing Bal.
</td>
<!--<td>Avg Monthly Cons. </td>-->
<td align="center" height="23">
MOS
</td>
<td align="center" height="23">
% of Total
</td>
</tr>
<%
double closing_bal = 0;
double avgVal = 0;
double closedBal = 0;
int noFacilities = 0;
double openBal = 0;
double receipts = 0;
double issues = 0;
double adj = 0;
 ResultSet rsGetAggStockMovements = db.getAggStockMovByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,type, hq);
while(rsGetAggStockMovements.next()){

 double cBal = db.getClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(1), hq,1);
 double mos = db.getClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(1), hq,3);
 double oBal = db.getOpeningBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(1), hq);
 double avg = db.getClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(1), hq,2);

%>
<tr class="row_titles">
<td align="center">
<%= rsGetAggStockMovements.getString(1) %>
</td>
<td align="center">
<% // db.getFacTypesCountByLevel(rsGetAggStockMovements.getString(1),mon,year,dat,fY,tY, fM, tM,type, hq) 
%>
<%= rsGetAggStockMovements.getString(2) %>
</td>
<td align="center">
<%= df1.format( oBal  ) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(3)) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(4)) %>
</td>
<td align="center">
<%= df1.format( rsGetAggStockMovements.getDouble(5)) %>
</td>
<td align="center">
<%= df1.format( cBal) %>
<%
closing_bal += cBal;
avgVal = avg;     
%>
</td>
<!--<td align="center">
<%= df1.format( avg) %>
</td>-->
<td align="center">
<%= df.format(mos) %>
</td>
<td align="center">
    <%
//System.out.println("Closing Bal is "+rsGetAggStockMovements.getDouble(6)+" and total CB is "+cBal +" and % is "+ Double.valueOf(  rsGetAggStockMovements.getDouble(6) / cBal) );
if(cBal == 0){
out.println(0); 
}else{

    %>
    
<%= df1.format(Double.valueOf(   cBal/cBalTotal) *100)+"%" %>

    <%}%>
</td>
</tr>
    
<%
noFacilities += rsGetAggStockMovements.getInt(2);
openBal += oBal;
receipts += rsGetAggStockMovements.getInt(3);
issues += rsGetAggStockMovements.getInt(4);
adj += rsGetAggStockMovements.getInt(5);
//closing_bal += rsGetAggStockMovements.getInt(7);
   
}
%>
<tr class="row_titles">
<td align="center">
Total
</td>
<td align="center">
<%= noFacilities %>
</td>
<td align="center">
<%= df1.format( openBal) %>
</td>
<td align="center">
<%= df1.format( receipts) %>
</td>
<td align="center">
<%= df1.format(issues) %>
</td>
<td align="center">
<%= df1.format(adj) %>
</td>
    
<td align="center">
<%= df1.format(closing_bal) %>
</td>
<!--<td align="center">
<%= df1.format(avgVal) %>
</td>-->
</tr>
</table>
<hr>
<%}

%>


 <%
   } else{
    cen = request.getParameterValues("p_cen");
   out.println("dsfdsfdsf");
    for(int i=0; i<prod.length; i++){
    
    %>
   
    <div align="center" style="width:100%; clear:both; margin-top:10pt;">
    <h3 align="center"> <u> <%= db.getProdName(prod[i])+" --- "+db.getProdDose(prod[i]) %> </u>  </h3> 
    </div>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">
Facility
</td>

<td align="center">
Opening Bal.
</td>
<td align="center">
Receipts
</td>
<td align="center">
issues
</td>
<td align="center">
Adjustments
</td>
<td align="center">
Closing Bal.
</td>
<td align="center">
MOS
</td>
<td align="center">
% of Total
</td>
</tr>
<%
int closing_bal = 0;
int closedBal = 0;
int noFacilities = 0;
int openBal = 0;
int receipts = 0;
int issues = 0;
int adj = 0;
 ResultSet rsGetAggStockMovements = db.getAggStockMovByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,type, hq,cen);
while(rsGetAggStockMovements.next()){
 double cBal = db.getClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat,fY,tY, fM, tM,type, hq,cen);
 // System.out.println(db.getClosingBalAggStockMovementsByLevel(prod[i], mon, year,dat));
%>
<tr class="row_titles">
<td align="center">
<%= rsGetAggStockMovements.getString(1) %>
</td>

<td align="center">
<%= rsGetAggStockMovements.getInt(2) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(3) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(4) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(5) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(6) %>
<%
closing_bal += rsGetAggStockMovements.getInt(6);
     
%>
</td>
<td align="center">
<%= df.format(rsGetAggStockMovements.getDouble(7)) %>
</td>
<td align="center">
    <%
System.out.println("Closing Bal is "+rsGetAggStockMovements.getDouble(6)+" and total CB is "+cBal +" and % is "+ Double.valueOf(  rsGetAggStockMovements.getDouble(6) / cBal) );
if(cBal == 0){
out.println(0); 
}else{

    %>
    
<%= df1.format(Double.valueOf(  rsGetAggStockMovements.getDouble(6) / cBal) *100)+"%" %>

    <%}%>
</td>
</tr>
    
<%
//noFacilities += db.getFacTypesCountByLevel(rsGetAggStockMovements.getString(1), mon, year,dat,fY,tY, fM, tM,type, hq);
openBal += rsGetAggStockMovements.getInt(2);
receipts += rsGetAggStockMovements.getInt(3);
issues += rsGetAggStockMovements.getInt(4);
adj += rsGetAggStockMovements.getInt(5);
//closing_bal += rsGetAggStockMovements.getInt(7);
   
}
%>
<tr class="row_titles">
<td align="center">
Total
</td>

<td align="center">
<%= openBal %>
</td>
<td align="center">
<%= receipts %>
</td>
<td align="center">
<%= issues %>
</td>
<td align="center">
<%= adj %>
</td>
    
<td align="center">
<%= closing_bal %>
</td>
</tr>
</table>
<hr>
<%}

}%>













<%

db.s();
%>
    </body>
</html>