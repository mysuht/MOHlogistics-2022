<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
          <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
          <style type="text/css">
 
</style>
    </head>
    <body>
    <%
    LogisticsReportsClass db = new LogisticsReportsClass();
    String [] Directorates = request.getParameterValues("p_cen");
    String dat = request.getParameter("dat");
    String mon = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";
String type = request.getParameter("p_dir");
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
    String prod[] = request.getParameterValues("p_prod");
    DecimalFormat df1 = new DecimalFormat("###,###.###");
    DecimalFormat dfmos = new DecimalFormat("###.#");
    
    %>
    <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
     <%
     String er =request.getParameter("rep");
     if(er.equals("9")){
     %>
    Supply Status Report <br/>
    <%}if(er.equals("8")){%>
    Supply Status Error Report <br/>
    <%}%>
  
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
    out.println("All Facility Types."+"<br/>");
    } if(!type.equals("0") && Integer.parseInt(type) < 100 ){
    out.println(rs12.getString(2));
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
    
  
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/><br/><br/><br/>
  
  
  
   <% // !!!!!!!!!!!!
    if(!type.equals("0")){
    String cen[] = request.getParameterValues("p_cen");
    for(int p=0; p < prod.length; p++){
    ResultSet rsDirs = db.getDirectorates();
      int sumClosed = 0;
      double sumAvg = 0;
      int rloop = 0;
    while(rsDirs.next()){
    
    
    String dr = rsDirs.getString(1);
      
      ResultSet rs = db.getSupplyStatusRepGT(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq,cen);
       if(rsDirs.getString(1).equals("498")){
      while(rs.next()){
      sumAvg += db.getSupplyStatusRepSumGT(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq,cen);
      //////////db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
      }
      }
      
        rs = db.getSupplyStatusRepGT(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq,cen);
        if(!rs.next()){
        continue;
        }
      
    //   ResultSet rsSuppFac = db.getSupplyStatusByFacRepGT(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq,cen);
       ResultSet rsSuppFac = db.getSupplyStatusByFacRep(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
       if(rsSuppFac.next()){
       System.out.println("hiiiiiiiiiiiiiiii");
    //   out.println("<h3 align='center'>"+ db.getProdName(prod[p]) +"  " +db.getProdDose(prod[p])+"</h3>");    
    %>
    <% if(p !=0 || rloop!=0){%>
     
     <br/>
    <div class="ba" style="clear:both;"> 
    </div>
    <Br/>
   
    
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
      
      <%}rloop++;%>
   <br/>
      <div align='center' style="font-weight:bold; font-size:14pt;clear:both;"> <%=  db.getProdName(prod[p]) +"  " +db.getProdDose(prod[p])%>   </div>
      <br/>
    <table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
    <tr class="col_titles">
    <td align="center">
    
    </td>
    <td align="center">
    Type
    </td>
    
    <td align="center">
    Receipts
    </td>
    <td align="center">
    issues / Dispensed 
    </td>
    <td align="center">
    Closing Balance
    </td>
    <td align="center">
    Current MOS
    </td>
    <td align="center">
    Average Monthly Consumption
    </td>
    </tr>
    
     <tr class="col_titles">
     
    <td align="center">
    <%= rsSuppFac.getString(1) %>
    </td>
    <%
     System.out.print("ppppppppppp");
     %>
    <td align="center">
    <%= rsSuppFac.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(4)) %>
    </td>
    <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq )) %>
    </td>
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / sumAvg)) %>
    
    <%}else{
    if( db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) == 0 && (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) == 0 ){
    out.println(0);
    }else{
    %>
    <%= dfmos.format((db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) )) %>
    <%}
    }%>
    </td>
    
    
    
    
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= df1.format(sumAvg) %>
    
    <%}else{%>
   <%= df1.format(Math.round(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
   <%}%>
    </td>
    
    
    </tr>
    
    
    
    
    
    
    
    
    
    
    
    <%
   double mos1 = 0;
    double rec = 0;
    double iss = 0;
    double closeBal = 0;
     double mos = 0;
     double avg = 0;
     rs = db.getSupplyStatusRepGT(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq,cen);
     int rrloop = 0;
    while(rs.next()){ // iterator
    rrloop++;
    System.out.println("hiiiiiiiiman");
    %>
    
    
    
    
    
    
    
    
    
    
     <% if(rrloop !=0 && rrloop%17 ==0){%>
    </table>
    <br/>
    <div class="ba" style="clear:both;"> </div>
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
    <br/>
  
  <div align='center' style="clear:both;font-weight:bold; font-size:14pt;"> <%=  db.getProdName(prod[p]) +"  " +db.getProdDose(prod[p])%>   </div>
  <br/>
     <table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
    <tr class="col_titles">
    <td align="center">
    
    </td>
    <td align="center">
    Type
    </td>
    
    <td align="center">
    Receipts
    </td>
    <td align="center">
    issues / Dispensed 
    </td>
    <td align="center">
    Closing Balance
    </td>
    <td align="center">
    Current MOS
    </td>
    <td align="center">
    Average Monthly Consumption
    </td>
    </tr>
    
     <tr class="col_titles">
     
    <td align="center">
    <%= rsSuppFac.getString(1) %>
    </td>
    <%
     System.out.print("ppppppppppp");
     %>
    <td align="center">
    <%= rsSuppFac.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(4)) %>
    </td>
    <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq )) %>
    </td>
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / sumAvg)) %>
    
    <%}else{
    if( db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) == 0 && (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) == 0 ){
    out.println(0);
    }else{
    %>
    <%= dfmos.format((db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) )) %>
    <%}
    }%>
    </td>
    
    
    
    
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= df1.format(sumAvg) %>
    
    <%}else{%>
   <%= df1.format(Math.round(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
   <%}%>
    </td>
    
    
    </tr>
    
    
    
    
    
    
    
    
      
      
      <%}%>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <tr class="row_titles">
    <td align="center">
    <%= rs.getString(1) %>
    </td>
    <td align="center">
    <%= rs.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rs.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rs.getDouble(4)) %>
    </td>
    <!--
    <td align="center">
    <%=rs.getString(5) %>
    </td> -->
    
    
     <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAllGT(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) ) %>
    </td>
    
    
    
    
    <td align="center">
    <%if(rsDirs.getString(1).equals("498")){%>
    
     <%= db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0.0 ? 99.9 : dfmos.format((rs.getDouble(5) / db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) )) %>
    <%}else{// no.
    
    if( db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) > 0 ){
    out.println(99.9);  
    }else   if( db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0.0 ){
    out.println(0);
    }else{
    %>
    
    <%=  dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)/ db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) ) ) %>
    <%}
    }%>
    </td>
    
    
    
    
    <td align="center">
    <%
     System.out.println("pas" + rsDirs.getString(1));
    if(rsDirs.getString(1).equals("498")){
    System.out.println("pas");
    %>
    
    
     <%= df1.format(Double.valueOf(db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
    
    
    <%}else{//avg11%>
   <%=  df1.format(Double.valueOf(db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) )) %>
    <%}%>
    </td>
    
    
    
    
    </tr>
    
    
    <%
    rec += rs.getDouble(3);
    iss += rs.getDouble(4);
    closeBal += db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
   
     if(rsDirs.getString(1).equals("498")){
     mos +=rs.getDouble(5) / db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
     avg+=  db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
     }else{
      mos += rs.getDouble(6);
    avg += db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
    }
    }%>
    <tr class="col_titles">
    <td align="center">
    Total
    </td>
    <td >
    
    </td>
    <td align="center">
    <%= df1.format(rec) %>
    </td>
    <td align="center">
    <%= df1.format(iss) %>
    </td>
    <td align="center">
    <%= (df1.format(closeBal)) %>
    </td>
    <td align="center">
    <% //avg == 0.0 ? 0 : df1.format(Double.valueOf(closeBal/ avg)) %>
    <% if ( closeBal == 0 && avg == 0){
    out.println(0);
    }else{
    out.println( dfmos.format(closeBal/avg));
    }
    %>
    </td>
    <td align="center">
    <%= df1.format(Math.round(avg)) %>
    </td>
    </tr>
    </table>
    <br/><br/> <br/>
    <%}  } //sp1
    }
    }
    %>
    
    
    
    
    
    
    
    
    
    
    
    
    
     <% // hi all
    if(type.equals("0")){ /////////// all_facilities
    
    for(int p=0; p< prod.length; p++){
    ResultSet rsDirs = db.getDirectorates();
      int sumClosed = 0;
      double sumAvg = 0;
      int rrlooop = 0;
    while(rsDirs.next()){
    rrlooop++;
    
    String dr = rsDirs.getString(1);
      
      ResultSet rs = db.getSupplyStatusRep(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
       if(rsDirs.getString(1).equals("498")){
      while(rs.next()){
      sumAvg += db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
      //////////db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
      }
      }
      %>
     
     <% 
       ResultSet rsSuppFac = db.getSupplyStatusByFacRep(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
       
       int rloop = 0;
       if(rsSuppFac.next()){
       rloop++;
       System.out.println("hiiiiiiiiiiiiiiii");
     
    %>
    
     <% if(p !=0 || rrlooop !=1){%>
     <br/>
      <div class="ba" style="clear:both;"> </div>
     <br/>
    
    
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
      
      <%}%>
   <br/>
      <div align='center' style="font-weight:bold; font-size:14pt;clear:both;"> <%=  db.getProdName(prod[p]) +"  " +db.getProdDose(prod[p])%>   </div>
      <br/>
    <table border="1" cellpadding="2" cellspacing="3" style="clear:both"  align="center">
    <tr class="col_titles">
    <td align="center">
    
    </td>
    <td align="center">
    Type
    </td>
    
    <td align="center">
    Receipts
    </td>
    <td align="center">
    issues / Dispensed 
    </td>
    <td align="center">
    Closing Balance
    </td>
    <td align="center">
    Current MOS
    </td>
    <td align="center">
    Average Monthly Consumption
    </td>
    </tr>
    
     <tr class="col_titles">
     
    <td align="center">
    <%= rsSuppFac.getString(1) %>
    </td>
    <%
     System.out.print("ppppppppppp");
     %>
    <td align="center">
    <%= rsSuppFac.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(4)) %>
    </td>
    <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq )) %>
    </td>
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= sumAvg == 0.0 ? 0 : dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / sumAvg)) %>
    
    <%}else{%>
    <%
    if( db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) > 0 ){
    out.println(99.9);  
    }else   if(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) == 0.0 ){
    out.println(0);
    }else{
    %>
    
    <%=  dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) )) %>
    <%}
    }%>
    </td>
    
    
    
    
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= df1.format(sumAvg) %>
    
    <%}else{%>
   <%= df1.format(Math.round(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
   <%}%>
    </td>
    
    
    </tr>
    
    
    
    
    
    
    
    
    
    
    
    <%
   double mos1 = 0;
    double rec = 0;
    double iss = 0;
    double closeBal = 0;
     double mos = 0;
     double avg = 0;
     rs = db.getSupplyStatusRep(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
     int rrloop = 0;
    while(rs.next()){ // iterator
    rrloop++;
    System.out.println("hiiiiiiiiman");
    %>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <tr class="row_titles">
    <td align="center">
    <%= rs.getString(1) %>
    </td>
    <td align="center">
    <%= rs.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rs.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rs.getDouble(4)) %>
    </td>
    <!--
    <td align="center">
    <%=rs.getString(5) %>
    </td> -->
    
    
     <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) ) %>
    </td>
    
    
    
    
    <td align="center">
    <%if(rsDirs.getString(1).equals("498")){%>
    
     <%= db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0.0 ? 99.9 : dfmos.format((rs.getDouble(5) / db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) )) %>
    <%}else{// no.
    
    if( db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) > 0 ){
    out.println(99.9);  
    }else   if( db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0.0 ){
    out.println(0);
    }else{
    %>
    
    <%=  dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)/ db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) ) ) %>
    <%}
    }%>
    </td>
    
    
    
    
    <td align="center">
    <%
     System.out.println("pas" + rsDirs.getString(1));
    if(rsDirs.getString(1).equals("498")){
    System.out.println("pas");
    %>
    
    
     <%= df1.format(Double.valueOf(db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
    
    
    <%}else{//avg11%>
   <%=  df1.format(Double.valueOf(db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) )) %>
    <%}%>
    </td>
    
    
    
    
    </tr>
    
    
    
    
    
    
    <% if(rrloop !=0 && rrloop%17 ==0){%>
    </table>
    <br/>
    <div class="ba" style="clear:both;"> </div>
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
    <br/>
  
  <div align='center' style="clear:both;font-weight:bold; font-size:14pt;"> <%=  db.getProdName(prod[p]) +"  " +db.getProdDose(prod[p])%>   </div>
  <br/>
     <table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center">
    <tr class="col_titles">
    <td align="center">
    
    </td>
    <td align="center">
    Type
    </td>
    
    <td align="center">
    Receipts
    </td>
    <td align="center">
    issues / Dispensed 
    </td>
    <td align="center">
    Closing Balance
    </td>
    <td align="center">
    Current MOS
    </td>
    <td align="center">
    Average Monthly Consumption
    </td>
    </tr>
    
     <tr class="col_titles">
     
    <td align="center">
    <%= rsSuppFac.getString(1) %>
    </td>
    <%
     System.out.print("ppppppppppp");
     %>
    <td align="center">
    <%= rsSuppFac.getString(2) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(3)) %>
    </td>
    <td align="center">
    <%= df1.format(rsSuppFac.getDouble(4)) %>
    </td>
    <td align="center">
    <%=  df1.format(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq )) %>
    </td>
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= sumAvg == 0.0 ? 0 : dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / sumAvg)) %>
    
    <%}else{%>
    <%
    if( db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) > 0 ){
    out.println(99.9);  
    }else   if(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq) == 0 && db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) == 0.0 ){
    out.println(0);
    }else{
    %>
    
    <%=  dfmos.format(Double.valueOf(db.getSupplyStatusByFacRepClosedBalAll(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq ) / (db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq)) )) %>
    <%}
    }%>
    </td>
    
    
    
    
    
    
    
    <td align="center">
    <%
     if(rsDirs.getString(1).equals("498")){
    %>
    <%= df1.format(sumAvg) %>
    
    <%}else{%>
   <%= df1.format(Math.round(db.getSupplyStatusRepSum(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq))) %>
   <%}%>
    </td>
    
    
    </tr>
    
    
    
    
    
    
      
      
      <%}%>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <%
    rec += rs.getDouble(3);
    iss += rs.getDouble(4);
    closeBal += db.getSupplyStatusByFacRepClosedBalAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
   
     if(rsDirs.getString(1).equals("498")){
     mos +=rs.getDouble(5) / db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
     avg+=  db.getSupplyStatusRepSum(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
     }else{
      mos += rs.getDouble(6);
    avg += db.getSupplyStatusByFacRepAvgAll(rs.getString(8), mon, year, prod[p], dat,fY,tY, fM, tM,type, hq);
    }
    }%>
    <tr class="col_titles">
    <td align="center">
    Total
    </td>
    <td >
    
    </td>
    <td align="center">
    <%= df1.format(rec) %>
    </td>
    <td align="center">
    <%= df1.format(iss) %>
    </td>
    <td align="center">
    <%= (df1.format(closeBal)) %>
    </td>
    <td align="center">
   <%
    if( avg == 0 && closeBal > 0 ){
    out.println(99.9);  
    }else   if(avg == 0 && closeBal == 0.0 ){
    out.println(0);
    }else{
    %>
    <%= dfmos.format(closeBal/avg) 
    
    %><%}%>
    </td>
    <td align="center">
    <%= df1.format(Math.round(avg)) %>
    </td>
    </tr>
    </table>
   
    <br/>
    
    <br/> <br/>
    <%}
    %>
    
    <%
     // db.closeRs(rs);
    } //sp1
    }
    }
    %>
    
    
    
    
    
    
    
    
    
    
    <%
db.s();
%>
    </body>
</html>