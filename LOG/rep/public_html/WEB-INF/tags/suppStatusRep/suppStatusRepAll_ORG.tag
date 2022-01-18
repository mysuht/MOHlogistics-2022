<%@ tag pageEncoding="windows-1256"%>
<%@ tag import="moh.logistics.lib.reports.code.SuppStatusRepAll, moh.logistics.lib.reports.code.SupplyStatusReport, java.util.*, java.sql.*, java.text.*, java.util.*, java.io.*"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="blk"%>


<%
    
    // moh.logistics.lib.reports.code.LogisticsReportsClass db = new moh.logistics.lib.reports.code.LogisticsReportsClass();
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
<% //hi
//SuppStatusRepAll stp = new SuppStatusRepAll();
SupplyStatusReport str = new SupplyStatusReport();
   
    if(true){ /////////// all_facilities
    
    for(int p=0; p< prod.length; p++){
   
    //ResultSet rsDirs = stp.getDirectorates(type, Directorates);
    List<Map<String,Object>> directorateList = str.getDirectorates(type,Directorates);
      int sumClosed = 0;
      double sumAvg = 0;
      int rrlooop = 0;
      
      int dirCount = directorateList.size(); //0;
//        while(rsDirs.next()){
//        dirCount ++;
//        }

      
      int rowIndex = 0;
    //  rsDirs = stp.getDirectorates(type, Directorates);
    for(Map<String,Object> directorate : directorateList){
   String suppFacilityId = directorate.get("FACILITY_ID") + "";


    
   
      %>
<% 
      //ResultSet rsSuppFac  = stp.getSupplyStatusRepBySupplierRS(suppFacilityId, mon, year, prod[p], dat,fY,tY, fM, tM,"indv", hq,Directorates, type);
      List<Map<String,Object>> rsSuppFac = str.getSupplyStatusRepBySupplierRS(suppFacilityId, mon, year, prod[p], dat,fY,tY, fM, tM,"indv", hq,Directorates, type);
     
     
      for(Map<String,Object> suppData : rsSuppFac){
         String suppFacilityType = suppData.get("factype") + "";
   String suppFacilityName = suppData.get("facname") + "";
   String suppReceipts = suppData.get("receipts") + "";
   String suppIssues = suppData.get("issues") + "";
   String suppAdjustments = suppData.get("adj") + "";
      
      
       int rloop = 0;
      //if(rsSuppFac.next()){
      if(rsSuppFac.size() > 0 ){
      //int ksize = stp.getSupplyStatusRepBySupplier(suppFacilityId, mon, year, prod[p], dat,fY,tY, fM, tM,"supp", hq,Directorates, type).size();
      int ksize = str.getSupplyStatusRepBySupplierRS(suppFacilityId, mon, year, prod[p], dat,fY,tY, fM, tM,"supp", hq,Directorates, type).size();
      if(ksize == 0 )
        continue;
     
         rrlooop++;
       rloop++;
   
     
    %>
<% if(p !=0 || rrlooop !=1){%>
<br/>
<div class="ba" style="clear:both;"></div>
<br/>
<br/>
<blk:block></blk:block>
<br/>
<%}%>
<br/>
<div align='center' style="font-weight:bold; font-size:14pt;clear:both;">
    <%=  str.getProdName(prod[p]) +"  " +str.getProdDose(prod[p])%>
</div>
<br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" width="100%">
    <tr class="col_titles">
        <td align="center"></td>
        <td align="center">Type</td>
         <td align="center">Opening Balance</td>
        <td align="center">Receipts</td>
        <td align="center">issues / Dispensed</td>
        <td align="center">Adjustments</td>
        <td align="center">Closing Balance</td>
        <td align="center">Current MOS</td>
        <td align="center">Average Monthly Consumption</td>
       
    </tr>
     
    <%
    double closeBal0 =   Double.valueOf(str.closeBal(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));//rsSuppFac.getDouble("closingBal");
    double mos0 = Double.valueOf(str.mos(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    double avg0 = Double.valueOf(str.avgMnthlyCons(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    double openBal0 = Double.valueOf(str.openBal(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;

     %>
     
    <tr class="col_titles">
        <td align="center">

             
            <%= suppFacilityName %>
        </td>
        <td align="center">
            <%= suppFacilityType %>
        </td>
        <td align="center">
        <%= df1.format(openBal0) %>
        </td>
        <td align="center">
            <%=   df1.format(Double.valueOf(suppReceipts))%>
        </td>
        <td align="center">
            <%= 
              df1.format(Double.valueOf(suppIssues))%>
        </td>
        <td align="center">
            <%= 
              df1.format(Double.valueOf(suppAdjustments))%>
        </td>
        <td align="center">
            <%=  df1.format(closeBal0)%>
        </td>
        </td>
        <td align="center">
        
            <%
           
     if(suppFacilityId.equals("498")) {
     out.println( dfmos.format(mos0));
     }else{
     if( avg0 == 0 && closeBal0 > 0 ){
    out.println(99.9);  
    }else   if( avg0 == 0 && closeBal0 == 0.0 ){
    out.println(0);
    }else{
   out.println( dfmos.format( mos0    ));
    }
    }
    
    
    %>
        </td>
        <td align="center">
            <%
     if(suppFacilityId.equals("498")){
    %>
             
            <%= df1.format(avg0) %>
             
            <%}else{%>
             
            <%= df1.format(avg0) %>
             
            <%}%>
        </td>
        
        
        </tr>
    </tr>
     
    <%
   double mos1 = 0;
    double rec = 0;
    double iss = 0;
    double closeBal = 0;
     double openBal = 0;
     double mos = 0;
     double avg = 0;
     double adj = 0;
     
     
     
    List<Map<String,Object>> facilityDataList = str.getSupplyStatusRepBySupplierRS(suppFacilityId, mon, year, prod[p], dat,fY,tY, fM, tM,"supp", hq,Directorates, type); 
    //ResultSet rs = stp.getSupplyStatusRepBySupplierRS(rsDirs.getString(1), mon, year, prod[p], dat,fY,tY, fM, tM,"supp", hq, Directorates, type);
     int rrloop = 0;
   // while(rs.next()){ // iterator
     for(Map<String, Object> facilityData : facilityDataList) {
     String facilityId = facilityData.get("FACILITY_ID") + "";
      String facilityName = facilityData.get("facname") + "";
      String facilityType = facilityData.get("factype") + "";
      String receipts = facilityData.get("receipts") + "";
      String issues = facilityData.get("issues") + "";
      String adjustments = facilityData.get("adj") + "";
     
    rrloop++;
    closeBal0 =   Double.valueOf(str.closeBal(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));//rsSuppFac.getDouble("closingBal");
    mos0 = Double.valueOf(str.mos(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    avg0 = Double.valueOf(str.avgMnthlyCons(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    openBal0 = Double.valueOf(str.openBal(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;


    
    
    
    
    rec += Double.valueOf(receipts);
    iss += Double.valueOf(issues);
    adj += Double.valueOf(adjustments);
    openBal += openBal0;
    closeBal += closeBal0;
   
   
      mos += mos0;
    avg += avg0;
    
    if( !(rrloop !=0 && rrloop%17 ==0) ) {
    %>
     
    <tr class="row_titles">
        <td align="center">
            <%
        //rsSuppFac.getString(1)
        //rsSuppFac.getString(2)
        //df1.format(rsSuppFac.getDouble(3))
        //df1.format(rsSuppFac.getDouble(4))
        %>
             
            <%= facilityName %>
        </td>
        <%
     //System.out.print("ppppppppppp");
     %>
        <td align="center">
            <%= facilityType %>
        </td>
        <td align="center">
        <%= df1.format(openBal0) %>
        </td>
        <td align="center">
            <%=   df1.format(Double.valueOf(receipts))%>
        </td>
        <td align="center">
            <%= 
              df1.format(Double.valueOf(issues))%>
        </td>
        
        <td align="center">
            <%= 
              df1.format(Double.valueOf(adjustments))%>
        </td>
        
        <td align="center">
            <%=  df1.format(closeBal0)%>
        </td>
        <td align="center">
            <%
            
     if(suppFacilityId.equals("498")){
     out.println(avg0 == 0.0 ? 0 : dfmos.format(mos0));
     }else{
     if( avg0 == 0 && closeBal0 > 0 ){
    out.println(99.9);  
    }else   if( avg0 == 0 && closeBal0 == 0.0 ){
    out.println(0);
    }else{
    out.println( dfmos.format( mos0    ));
    }
    }
    
    
    %>
        </td>
        <td align="center">
            <%
     if(suppFacilityId.equals("498")){
    %>
             
            <%= df1.format(avg0) %>
             
            <%}else{%>
             
            <%= df1.format(avg0) %>
             
            <%}%>
        </td>
        
        
    </tr>
     
    <% }else {
    %>
</table>

<br/>
<div class="ba" style="clear:both;"></div>
<br/>
<blk:block></blk:block>
<br/>
<br/>
<div align='center' style="clear:both;font-weight:bold; font-size:14pt;">
    <%=  str.getProdName(prod[p]) +"  " +str.getProdDose(prod[p])%>
</div>
<br/>



<table border="1" cellpadding="2" cellspacing="3" style="clear:both" align="center" width="100%">

    <tr class="col_titles">
        <td align="center">
           
        </td>
        <td align="center">Type</td>
        <td align="center">Opening Balance</td>
        <td align="center">Receipts</td>
        <td align="center">issues / Dispensed</td>
        <td align="center">Adjustments</td>
        <td align="center">Closing Balance</td>
        <td align="center">Current MOS</td>
        <td align="center">Average Monthly Consumption</td>
       
    </tr>
     <tr class="col_titles">
        <td align="center">
            <%
        closeBal0 =   Double.valueOf(str.closeBal(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));//rsSuppFac.getDouble("closingBal");
         mos0 = Double.valueOf(str.mos(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
         avg0 = Double.valueOf(str.avgMnthlyCons(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
         openBal0 = Double.valueOf(str.openBal(suppFacilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;

            
//     closeBal0 = rsSuppFac.getDouble("closingBal");
//     mos0 = rsSuppFac.getDouble("mos");
//     avg0 = rsSuppFac.getDouble("avg");
//     openBal0 = rsSuppFac.getDouble("openingBal");
       
        %>
             
            <%= suppFacilityName %>
        </td>
        <%
     //System.out.print("ppppppppppp");
     %>
        <td align="center">
            <%= suppFacilityType %>
        </td>
        <td align="center">
        <%= df1.format(openBal0) %>
        </td>
        <td align="center">
            <%=   df1.format(Double.valueOf(suppReceipts))%>
        </td>
        <td align="center">
            <%= 
              df1.format(Double.valueOf(suppIssues))%>
        </td>
        <td align="center">
            <%=   df1.format(Double.valueOf(suppAdjustments))%>
        </td>
        <td align="center">
            <%=  df1.format(closeBal0)%>
        </td>
        <td align="center">
        
            <%
           
     if(suppFacilityId.equals("498")) {
     out.println( dfmos.format(mos0));
     }else{
     if( avg0 == 0 && closeBal0 > 0 ){
    out.println(99.9);  
    }else   if( avg0 == 0 && closeBal0 == 0.0 ){
    out.println(0);
    }else{
   out.println( dfmos.format( mos0    ));
    }
    }
    
    
    %>
        </td>
        <td align="center">      
            <%= df1.format(avg0) %>    
        </td>
        
        
        </tr>
    <tr class="row_titles">
        <td align="center">
            <%
    closeBal0 =   Double.valueOf(str.closeBal(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));//rsSuppFac.getDouble("closingBal");
    mos0 = Double.valueOf(str.mos(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    avg0 = Double.valueOf(str.avgMnthlyCons(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;
    openBal0 = Double.valueOf(str.openBal(facilityId, mon, year, prod[p], dat, fY, tY, fM, tM, type, hq));;

//    closeBal0 = rs.getDouble("closingBal");
//    avg0 = rs.getDouble("avg");
//    mos0 = rs.getDouble("mos");
//    openBal0 = rs.getDouble("openingBal"); 
    
    %>
             
            <%= facilityName %>
        </td>
        <%
     //System.out.print("ppppppppppp");
     %>
        <td align="center">
            <%= facilityType %>
        </td>
        <td align="center">
        <%= df1.format(openBal0) %>
        </td>
        <td align="center">
            <%=   df1.format(Double.valueOf(receipts))%>
        </td>
        <td align="center">
            <%= 
              df1.format(Double.valueOf(issues))%>
        </td>
         <td align="center">
            <%= 
              df1.format(Double.valueOf(adjustments))%>
        </td>
        <td align="center">
            <%=  df1.format(closeBal0)%>
        </td>
        <td align="center">
            <%
            
     if(suppFacilityId.equals("498")){
     out.println(avg0 == 0.0 ? 0 : dfmos.format(mos0));
     }else{
     if( avg0 == 0 && closeBal0 > 0 ){
    out.println(99.9);  
    }else   if( avg0 == 0 && closeBal0 == 0.0 ){
    out.println(0);
    }else{
    out.println(dfmos.format(mos0));
    }
    }
    
    
    %>
        </td>
        <td align="center">
            <%
     if(suppFacilityId.equals("498")){
    %>
             
            <%= df1.format(avg0) %>
             
            <%}else{%>
             
            <%= df1.format(avg0) %>
             
            <%}%>
        </td>
        
        
    </tr>
     
    <%}%>
     
    <%
  
   
    } // end main loop%>
     
    <tr class="col_titles">
        <td align="center">Total</td>
        <td></td>
        <td align="center">
            <%= df1.format(openBal) %>
        </td>
        <td align="center">
            <%= df1.format(rec) %>
        </td>
        <td align="center">
            <%= df1.format(iss) %>
        </td>
        
         <td align="center">
            <%= df1.format(adj) %>
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
    
    %><%}
    
    
    %>
        </td>
        <td align="center">
            <%= df1.format(Math.round(avg)) %>
        </td>
        
    </tr>
</table>
<br/>
<br/>
<br/>
<%


// db.closeRs(rs);
// db.closeRs(rsSuppFac);

}
    %>
   
<%
rowIndex ++;

} // for supplier

}   // While(Dirs) //sp1
//db.closeRs(rsDirs);

    }
    }
    %>
<%
//db.s();
%>