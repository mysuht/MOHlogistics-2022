<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, 
         com.logistics.lib.report.NonReportingFacilityReport, 
         moh.logistics.lib.reports.code.FacilityTemplate,
         java.sql.*, java.text.*, java.util.List"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%
    String []cen;
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String hq = "";
    String type = request.getParameter("p_dir");
    String dat = request.getParameter("dat");
    String mon = "";
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
        // moh.logistics.lib.reports.code.LogisticsReportsClass.DBClass D = new moh.logistics.lib.reports.code.LogisticsReportsClass.DBClass ();
   
       LogisticsReportsClass db = new LogisticsReportsClass();

    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Familiy Planning Program
            </div>
            <div class="dCenter">
                Non Reporting Facility Report 
                <br/>
                 Program: Contraceptives. 
                <br/>
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
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    out.println(rs12.getString(2));
    }
    if(!type.equals("0") && Integer.parseInt(type) > 100){
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
        <br/><br/><br/><br/>
         
        <%
cen = request.getParameterValues("p_cen");
//if( db.getDirType(type).equals("7")){
NonReportingFacilityReport
nrf = new NonReportingFacilityReport();
List<FacilityTemplate> directorateList = nrf.getDirectoratesList(cen);
int i=0;
for(FacilityTemplate directorate : directorateList) {
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx 22222222222 xxxxxxxxxxxxxxxxxxx");
List<FacilityTemplate> facilityList 
= nrf.facilityList(mon, year, dat, fY, tY, fM, tM, hq, directorate.getFacilityId()+"", cen);
if(nrf.facilityList(mon, year, dat, fY, tY, fM, tM, hq, directorate.getFacilityId()+"", cen).size() == 0)
%>
                    <div style=" width:600pt;clear:both; color: red; ">
<h4>
        Supplier : <%= directorate.getFacilityName() %>
</h4>
</div>
<%
for(FacilityTemplate facility : facilityList){
if (i != 0){

%>
         
        <%}

if(i%6 == 0 && i!=0){



%>
        <div class="ba"></div>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="clear:both; font-weight:bold; text-align: center;" align="center">
                <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                     &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                     &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
            </div>
            <%
i=0;}else{%>

            <div style=" width:600pt;clear:both; margin:10pt;">
                <%}%>
                
                    <div style=" width:600pt;clear:both; ">
<h4>
        Supplier : <%= directorate.getFacilityName() %>
</h4>
</div>
                
                <div style="float:left; width:400pt;text-align:left;">
                    <%= 
facility.getFacilityDTO().getFacilityName()  
%>
                     
                    <br/>
                     
                    <br/>
                     
                    <br/>
                     
                    <%= 
facility.getFacilityDTO().getFacilityCity()  %>
                     &nbsp; &nbsp; &nbsp; 
                    <%= facility.getFacilityDTO().getFacilityState() %>
                     
                    <br/>
                     &nbsp; 
                    <span style="background-color:yellow"> CEP:</span>
                </div>
                <div style="float:left; width:200pt;text-align:left;">
                    <span style="background-color:yellow">Code: </span>
                     
                    <%= facility.getFacilityDTO().getFacilityCode() %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Type: </span>
                     
                    <%= nrf.facilityType(facility.getFacilityDTO().getFacilityTypeId()).getFacilityTypeName() %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Contact: </span>
                     
                    <%= facility.getFacilityDTO().getFacilityContact() %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Phone: </span>
                     
                    <%= facility.getFacilityDTO().getFacilityPhone() %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Fax: </span>
                     
                    <%= facility.getFacilityDTO().getFacilityFax() %>
                     
                    <br/>
                     
                    <br/>
                     
                    <br/>
                </div>
            </div>
            <div style="clear:both">
                <hr/>
            </div>
            <%
i++;
%>
             
            <%
} // end while


//System.out.println("Directorates");

//}else{ ////////////////////////////////////////
/////////////////////////nested
%>
             
            <%
//} ////////////////////////// end nested



}// directorates


%>
        </div>
    </body>
</html>