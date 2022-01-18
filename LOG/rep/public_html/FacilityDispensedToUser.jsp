<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.FacilityDispensedToUserReport, moh.logistics.lib.reports.code.FacilityTemplate, moh.logistics.lib.reports.code.FacilityDispensed,  java.util.*, moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*,java.util.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
        <style type="text/css">
                table {
                    page-break-inside: auto
                }

                tr {
                    page-break-inside: avoid;
                    page-break-after: auto
                }

                thead {
                    display: table-header-group
                }

                tfoot {
                    display: table-footer-group
                }
            </style>
    </head>
    <body>
        <%
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";
String type = request.getParameter("p_dir");
    String cen[] = request.getParameterValues("p_cen");
    String prod[] = request.getParameterValues("p_prod");
    String dat = request.getParameter("dat");
    int qr = request.getParameter("p_quart") == null?null:Integer.parseInt(request.getParameter("p_quart"));
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
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Family Planning Program
            </div>
            <div class="dCenter">
                Facility Dispensed To User Report 
                <br/>
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
    } if(!type.equals("0") && Integer.parseInt(type) < 100 ){
    out.println(rs11.getString(2));
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
            </div>
            <div class="dRight">
                Run Date: 
                <%=  db.getDate() %>
                 
                <br/>
                 Run Time: 
                <%= db.getTime() %>
            </div>
        </div>
        <br/>
         
        <br/>
         
        <%

///////// ------------------------- all12

    if(true){ //all1
    List<FacilityTemplate> directorateList =  FacilityDispensedToUserReport.getDirectoratesList(cen);
int sloop = 0;
  for(FacilityTemplate directorate: directorateList){

   double dispensed = 0.0;
  List<FacilityDispensed> facilityDispensedList = FacilityDispensedToUserReport.facilityDispensedToUserReport(quart, year, dat,
                                                                     fY, tY, fM, tM,
                                                                      hq, (directorate.getFacilityId()+""), type);
 int facilitiesOfSupplierSize = facilityDispensedList.size();
 
    if(facilitiesOfSupplierSize > 0){  
    int sum=0;
    int sumD = 0;
    if(sloop == 0){
    %>
         
        <%}else{%>
         
        <%}%>
         
        <br/>
         
        <br/>
         
        <br/>
        <div class="contentpage">
            <div style="clear:both">
                <%

String directorateID = directorate.getFacilityId()+"";
%>
                <div style="clear:both;font-size:11pt;font-weight:bold">
                        Supplier Name: 
                        <%= db.getSupplierName(directorateID) + " -- " + db.getSupplierCode(directorateID)%>
                    </div>
                    <div style="clear:both;font-size:11pt;font-weight:bold">
                    Facility Name:
                        <%= db.getFacilityName(directorateID ) + " -- " + db.getFacilityCode( directorateID) %>
                </div>
                <br/>
                <table border="1" cellpadding="2" cellspacing="3" style="clear:both;table-layout:fixed;" width="70%"
                       id="DataList1">
                    <thead>
                        <tr class="col_titles">
                            <th align="center" colspan="2">Commodities</th>
                            <th align="center">Dispensed</th>
                            <th align="center">Dispensed To Higher Level.</th>
                            <th align="center">Percent %</th>
                        </tr>
                    </thead>
                     
                    <%
int rloop=0;
for(FacilityDispensed facility : facilityDispensedList) {
%>
                     
                    <%
if(rloop % 17 == 0 && rloop != 0){
%>
                </table>
                <div class="ba"></div>
                <div style="clear:both; font-weight:bold; text-align: center;" align="center">
                    <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                         &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                    <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                    <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                    <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                    <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                    <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                         &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                    <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                    <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                    <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                    <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                </div>
                <br/>
                <div style="clear:both">
                    <table border="1" cellpadding="2" cellspacing="3" style="clear:both;table-layout:fixed;" width="70%"
                           id="DataList1">
                        <tr class="col_titles">
                            <th align="center" colspan="2">Commodities</th>
                            <th align="center">Dispensed</th>
                            <th align="center">Dispensed To Higher Level.</th>
                            <th align="center">Percent %</th>
                        </tr>
                         
                        <%}rloop++;%>
                         
                        <tr class="row_titles">
                            <td align="center">
                                <%= facility.getProductName()  %>
                            </td>
                            <td align="center">
                                <%= facility.getProductDose() %>
                            </td>
                            <td align="center">
                                <%out.println(
df.format(Double.valueOf(facility.getFacilityDispensed()))
);%>
                            </td>
                            <td align="center">
                                <%out.println(
df.format(Double.valueOf(facility.getSupplierDispensed()))
);%>
                            </td>
                            <td align="center">
                                <%= facility.getFacBySuppDispensed() %>
                            </td>
                        </tr>
                         
                        <%}%>
                    </table>
                    <br/><br/><br/>
                </div>
                <br/>
                <div class="ba"></div>
                <%
}%>
                <div class="ba"></div>
                <%
} // directorates



//} // products
} //type <> 0
%>
            </div>
        </div>
    </body>
</html>