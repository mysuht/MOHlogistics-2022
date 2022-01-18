<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="java.util.List, moh.logistics.lib.reports.code.SupplyStatusReport, moh.logistics.lib.reports.code.FacilityTemplate, java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <!--<link href="../../../resources/css/logCss.css" rel="stylesheet" type="text/css"/>-->
        <link href="resources/css/logAbt.css" rel="stylesheet" type="text/css"/>
        <style type="text/css">
            </style>
    </head>
    <body>
        <%
    
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
//    DecimalFormat df1 = new DecimalFormat("###,###.###");
//    DecimalFormat dfmos = new DecimalFormat("###.#");
    
    %>
        <table border="0" cellpadding="2" cellspacing="3" width="100%">
            <thead>
                <tr>
                    <td class="dLeft">
                        Jordan Contraceptive Logistics System 
                        <br/>
                         MOH Family Planning Program
                    </td>
                    <td class="dCenter">
                        <%
     String er =request.getParameter("rep");
     if(er.equals("9")){
     %>
                         Supply Status Report 
                        <br/>
                         
                        <%}if(er.equals("8")){%>
                         Supply Status Error Report 
                        <br/>
                         
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
//    ResultSet rs12 = db.getFacTypesMainName(type);
//    rs12.next();

    
    if(type.equals("0")){
    out.println("All Facility Types."+"<br/>");
    //} if(!type.equals("0") && Integer.parseInt(type) < 100 ){
    }else{
        List<FacilityTemplate> facilityList = SupplyStatusReport.getFacTypesMainName(type);
    FacilityTemplate facility = facilityList.get(0);
    String facilityName = facility.getFacilityName().toString();
    out.println(facilityName);
    //}if(!type.equals("0") && Integer.parseInt(type) > 100){
    //out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
                    </td>
                    <td class="dRight">
                        Run Date: 
                        <%=  SupplyStatusReport.getDate() %>
                         
                        <br/>
                         Run Time: 
                        <%= SupplyStatusReport.getTime() %>
                    </td>
                </tr>
            </thead>
             
            <tbody>
                <tr>
                    <td colspan="3" style="width:100%">
                        <div style="width:100%">
                            <%
    
   
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
    DecimalFormat df1 = new DecimalFormat("###,###.###");
    DecimalFormat dfmos = new DecimalFormat("###.##");
    
    %>
                             
                            <% //hi
    SupplyStatusReport str = new SupplyStatusReport();  
    for(int p=0; p< prod.length; p++){
    List<FacilityTemplate> directorateList = str.getDirectoratesList(Directorates);    
    for(FacilityTemplate directorate : directorateList){
         List<FacilityTemplate> supplyStatusReportList = str.supplyStatusFacilityList(prod[p], mon, year, dat, fY, tY, fM, tM, hq, Directorates, (directorate.getFacilityId()+"") ) ;
         if(supplyStatusReportList.size() == 0) continue;
         FacilityTemplate supplier = supplyStatusReportList.get(0);
   String supplierType = supplier.getFacilityType().toString();
   String supplierName = supplier.getFacilityName().toString();
   String supplierReceipts = supplier.getReceipts().toString();
   String supplierIssues = supplier.getIssues().toString();
   String supplierAdjustments = supplier.getAdjustments().toString();
   double supplierOpeningBalance = null!= supplier.getOpenningBal() ? Double.valueOf( supplier.getOpenningBal().toString()) : 0.0;
   double supplierClosingBalance = null!= supplier.getClosingBal().toString() ? Double.valueOf( supplier.getClosingBal().toString()) : 0.0;
   double supplierAvgMnthlyCons = supplier.getAvg() !=null ? Double.valueOf( supplier.getAvg().toString()) : 0.0;
   double supplierMosMnthlyCons = supplierAvgMnthlyCons  > 0 ?  (supplierClosingBalance / supplierAvgMnthlyCons)  : 0.0;
   
   
   supplyStatusReportList.remove(supplier);
  
//   if(supplyStatusReportList.size() == 0)
//        continue;
    %>
    
    
                            <div align='center' style="font-weight:bold; font-size:14pt;">
                                <%=  str.getProdName(prod[p]) +"  " +str.getProdDose(prod[p])%>
                            </div>
                      
                            <table border="1" cellpadding="2" class="page-break" cellspacing="3"
                                   style=" text-align: center; width:100%; ">
                                <thead>
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
                                    <tr class="col_titles">
                                        <td align="center">
                                            <%= supplierName  %>
                                        </td>
                                        <td align="center">
                                            <%= supplierType  %>
                                        </td>
                                        <td align="center">
                                            <%= df1.format(supplierOpeningBalance ) %>
                                        </td>
                                        <td align="center">
                                            <%=   df1.format(Double.valueOf(supplierReceipts ))%>
                                        </td>
                                        <td align="center">
                                            <%= 
              df1.format(Double.valueOf(supplierIssues ))%>
                                        </td>
                                        <td align="center">
                                            <%= 
              df1.format(Double.valueOf(supplierAdjustments ))%>
                                        </td>
                                        <td align="center">
                                            <%=  df1.format(supplierClosingBalance )%>
                                        </td>
                                        <td align="center">
                                            <% 
                                            if(supplierClosingBalance > 0 && supplierMosMnthlyCons == 0 ) {
                                            out.println( 99.9);
                                            }else{
                                            out.println( dfmos.format(supplierMosMnthlyCons)); 
                                            }
                                            %>
                                        </td>
                                        <td align="center">
                                            <% out.println( df1.format(supplierAvgMnthlyCons )); %>
                                        </td>
                                    </tr>
                                </thead>
                                 
                                <%
    
     
    
    int rloop = 0;
      
        System.out.println(" xxxxxxxxxxxxxxxxx supplier data size : " + supplyStatusReportList.size());
   
      double mos1 = 0;
    double rec = 0;
    double iss = 0;
    double closeBal = 0;
     double openBal = 0;
     double mos = 0;
     double avg = 0;
     double adj = 0;
      for(FacilityTemplate facility : supplyStatusReportList) { 
   String facilityType = facility.getFacilityType().toString();
   String facilityName = facility.getFacilityName().toString();
   String facilityReceipts = facility.getReceipts().toString();
   String facilityIssues = facility.getIssues().toString();
   String facilityAdjustments = facility.getAdjustments().toString();
   double facilityOpeningBalance = null!= facility.getOpenningBal() ? Double.valueOf( facility.getOpenningBal().toString()) : 0.0;
   double facilityClosingBalance = null!= facility.getClosingBal().toString() ? Double.valueOf( facility.getClosingBal().toString()) : 0.0;
   double facilityAvgMnthlyCons = facility.getAvg() !=null ? Double.valueOf( facility.getAvg().toString()) : 0.0;
   double facilityMosMnthlyCons = facilityAvgMnthlyCons  > 0 ?  (facilityClosingBalance / facilityAvgMnthlyCons)  : 0.0;     
    %>
                                 
                                <%
    rec += Double.valueOf(facilityReceipts );
    iss += Double.valueOf(facilityIssues );
    adj += Double.valueOf(facilityAdjustments );
    openBal += facilityOpeningBalance ;
    closeBal += facilityClosingBalance ;
    mos += facilityMosMnthlyCons;
    avg += facilityAvgMnthlyCons;
    %>
                                 
                                <tbody>
                                    <tr class="row_titles">
                                        <td align="center">
                                            <%= facilityName  %>
                                        </td>
                                        <td align="center">
                                            <%= facilityType  %>
                                        </td>
                                        <td align="center">
                                            <%= df1.format(facilityOpeningBalance ) %>
                                        </td>
                                        <td align="center">
                                            <%=   df1.format(Double.valueOf(facilityReceipts ))%>
                                        </td>
                                        <td align="center">
                                            <%= 
              df1.format(Double.valueOf(facilityIssues ))%>
                                        </td>
                                        <td align="center">
                                            <%= 
              df1.format(Double.valueOf(facilityAdjustments ))%>
                                        </td>
                                        <td align="center">
                                            <%=  df1.format(facilityClosingBalance )%>
                                        </td>
                                        <td align="center">
                                            <% 
                                            if(facilityClosingBalance > 0 && facilityAvgMnthlyCons == 0 ){
                                            out.println(99.9);
                                            }else{
                                            out.println( dfmos.format(facilityMosMnthlyCons)); 
                                            }
                                            %>
                                        </td>
                                        <td align="center">
                                            <% 
                                            out.println( df1.format(facilityAvgMnthlyCons )); %>
                                        </td>
                                    </tr>
                                </tbody>
                                 
                                <%
 }
%>
                                 
                                <tfoot>
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
                                </tfoot>
                                 
                                <% }   // While(Dirs) //sp1%>
                            </table>
                            <%
}
    %>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>