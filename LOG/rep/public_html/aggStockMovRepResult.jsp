<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="blk"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
          <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    
    <%
      DecimalFormat df = new DecimalFormat("###,###.##");
      DecimalFormat df1 = new DecimalFormat("###,###.###");
      DecimalFormat df11 = new DecimalFormat("###,###.##");
      String type = request.getParameter("p_dir");
    String prod[] = request.getParameterValues("p_prod");
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String hq = "";
    String mon="";
    String dat = request.getParameter("dat");
    if(dat.equals("m")){
    mon = request.getParameter("p_mon");
    }if(dat.equals("q")){
    mon = request.getParameter("p_quart");
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
    
    String year = request.getParameter("p_year");
    LogisticsReportsClass db = new LogisticsReportsClass();
   
    %>

 <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Aggregate Stock Movement Report <br/>
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
    ResultSet rs12 = db.getFacTypesMainName(type);
    rs12.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
    out.println(rs12.getString(2));
    }
    %>
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/><br/><br/><br/>
    <%
    if(type.equals("0")){
    
    for(int p=0; p<prod.length;p++){
    double cBalTotal = db.getSumClosingBalAggStockMovementsByLevel(prod[p], mon, year,dat,fY,tY, fM, tM,null, hq);
     
    //System.out.println(db.getClosingBalAggStockMovements(prod[p], mon, year, dat,fY,tY, fM, tM,type, hq));
  //  double cBal = db.getClosingBalAggStockMovements(prod[p], mon, year, dat,fY,tY, fM, tM,type, hq);
   // double cBal = db.getAggStockMovementsSum(prod[p], mon, year,dat,fY,tY, fM, tM,type, hq);
    
    %>
    
    
    
    
    <% if ( p!=0){%>
 
   <br/>
   <blk:block></blk:block>
<br/>
   
   <%}%>
    
    
    
    
    
    
    
    <h3 align="center" style="clear:both"> <%= db.getProdName(prod[p])+" --- " +db.getProdDose(prod[p]) %> </h3> 
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
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
<!--<td align="center">
Avg Monthly Cons.
</td>-->
<td align="center">
MOS
</td>


<td align="center">
% of Total
</td>
</tr>
<%
double closing_bal = 0;

double closedBal = 0;
int noFacilities = 0;
double openBal = 0;
double avgVal = 0;
double receipts = 0;
double issues = 0;
int adj = 0;
int rloop = 0;

ResultSet rsGetAggStockMovements = db.getAggStockMovements(prod[p], mon, year,dat,fY,tY, fM, tM,type, hq);
while(rsGetAggStockMovements.next()){
%>






   <% if ( rloop%17==0 && rloop != 0) {%>
 </table>
 <div class="ba"></div>
   <br/>
   <blk:block></blk:block>
<br/>
<h3 align="center" style="clear:both"> <%= db.getProdName(prod[p])+" --- " +db.getProdDose(prod[p]) %> </h3> 


<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
  <tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
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
<!--<td align="center">
Avg Monthly Cons.
</td>-->
<td align="center">
MOS
</td>
<td align="center">
% of Total
</td>
</tr>

   <% } rloop++;
   
   
   
   double cBalCell = db.getClosingBalAggStockMovementsRep(prod[p], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(2), hq,1);
   double avg = db.getClosingBalAggStockMovementsRep(prod[p], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(2), hq,2);
 double mos = db.getClosingBalAggStockMovementsRep(prod[p], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(2), hq,3);
 double oBal = db.getOpeningBalAggStockMovementsRep(prod[p], mon, year,dat,fY,tY, fM, tM,rsGetAggStockMovements.getString(2), hq);
   
   
   
   %>
    











<tr class="row_titles">
<td align="center">
<%= rsGetAggStockMovements.getString(1) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(3) %>
</td>
<td align="center">
<%= df1.format(oBal) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(4)) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(5)) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(6) %>
</td>
<td align="center">
<%= df1.format(cBalCell) %>
<%
closing_bal += cBalCell;
%>
</td>
<!--<td align="center">
<%= df1.format(avg) %>
</td>-->
<td align="center">
<%=  df11.format(mos) %>
</td>
<td align="center">
<%
System.out.println("Closing Bal is "+rsGetAggStockMovements.getDouble(7)+" and total CB is "+cBalTotal +" and % is "+ Double.valueOf(  rsGetAggStockMovements.getDouble(7) / cBalTotal) );

%>
<%= df.format(Double.valueOf(  cBalCell / cBalTotal) *100)+"%" %>
</td>
</tr>

<%
noFacilities += rsGetAggStockMovements.getInt(3);
openBal += oBal;
avgVal = avg;
receipts += rsGetAggStockMovements.getInt(4);
issues += rsGetAggStockMovements.getInt(5);
adj += rsGetAggStockMovements.getInt(6);
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
<%= df1.format(openBal) %>
</td>
<td align="center">
<%= df1.format(receipts) %>
</td>
<td align="center">
<%= df1.format(issues) %>
</td>
<td align="center">
<%= adj %>
</td>

<td align="center">
<%= df1.format(closing_bal) %>
</td>

<!--<td align="center">
<%= df1.format(avgVal) %>
</td>-->

</tr>
</table>

<br/>
<div class="ba"></div>
<br/><br/>
<%}

}%>
















<%
/////////////// for facilities ---------------------
%>


<%
    if(!type.equals("0")){
    String cen[] = request.getParameterValues("p_cen");
    for(int p=0; p<prod.length;p++){
     ResultSet rsGetAggStockMovements = db.getAggStockMovements(prod[p], mon, year,dat,fY,tY, fM, tM,type, hq,cen);
    System.out.println(db.getClosingBalAggStockMovements(prod[p], mon, year, dat,fY,tY, fM, tM,type, hq,cen));
    double cBal = db.getClosingBalAggStockMovements(prod[p], mon, year, dat,fY,tY, fM, tM,type, hq,cen);
    
    %>
    <h3 align="center" style="clear:both"> <%= db.getProdName(prod[p])+" --- " +db.getProdDose(prod[p]) %> </h3> 
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">
Facility Type
</td>
<td align="center">
#
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
double closing_bal = 0;
double closedBal = 0;
int noFacilities = 0;
double openBal = 0;
double receipts = 0;
double issues = 0;
int adj = 0;

while(rsGetAggStockMovements.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rsGetAggStockMovements.getString(2) %>
</td>
<td align="center">
<%= db.getFacTypesCount(rsGetAggStockMovements.getString(1)) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(3)) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(4)) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(5)) %>
</td>
<td align="center">
<%= rsGetAggStockMovements.getInt(6) %>
</td>
<td align="center">
<%= df1.format(rsGetAggStockMovements.getDouble(7)) %>
<%
closing_bal += rsGetAggStockMovements.getDouble(7);

%>
</td>
<td align="center">
<%= df.format(rsGetAggStockMovements.getDouble(8)) %>
</td>
<td align="center">
<%
System.out.println("Closing Bal is "+rsGetAggStockMovements.getDouble(7)+" and total CB is "+cBal +" and % is "+ Double.valueOf(  rsGetAggStockMovements.getDouble(7) / cBal) );

%>
<%= df.format(Double.valueOf(  rsGetAggStockMovements.getDouble(7) / cBal) *100)+"%" %>
</td>
</tr>

<%
noFacilities += db.getFacTypesCount(rsGetAggStockMovements.getString(1));
openBal += rsGetAggStockMovements.getInt(3);
receipts += rsGetAggStockMovements.getInt(4);
issues += rsGetAggStockMovements.getInt(5);
adj += rsGetAggStockMovements.getInt(6);
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
<%= df1.format(openBal) %>
</td>
<td align="center">
<%= df1.format(receipts) %>
</td>
<td align="center">
<%= df1.format(issues) %>
</td>
<td align="center">
<%= adj %>
</td>

<td align="center">
<%= df1.format(closing_bal) %>
</td>
</tr>
</table>
<br/><br/><br/>
<%}}%>
















<%
db.s();
%>
</body>
</html>