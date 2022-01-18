<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.BelowEmergencyOrderPointV2, moh.logistics.lib.reports.code.FacilityTemplate, java.util.*,  java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
         <style  type="text/css" media="print">
        
    @page {
size: Portrait;



} 
  BODY {
   
    width:90%;
   
   
  /* 
   -webkit-transform: rotate(-90deg) scale(.68,.68); 
  -moz-transform: scale(.90,.90); */
  
    zoom:90 ;
   -moz-transform: scale(90%);
    -ms-transform: scale(90%);
}

        
        
 
    </style>
    </head>
    
    <body>
        <%
    String dat = request.getParameter("dat");
    String mon = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
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
    String dir[] = request.getParameterValues("p_cen");
    String products[] = request.getParameterValues("p_prod");
  
      DecimalFormat df1 = new DecimalFormat("###,###.##");
      DecimalFormat df = new DecimalFormat("###,###.##");
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Familiy Planning Program
            </div>
            <div class="dCenter">
                Below Emergency Order Point 
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
    ResultSet rs12 = BelowEmergencyOrderPointV2.getFacTypesMainName(type);
    rs12.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
    out.println(rs12.getString(2));
    }
    %>
                 
                <br/>
            </div>
            <div class="dRight">
                Run Date: 
                <%=  BelowEmergencyOrderPointV2.getDate() %>
                 
                <br/>
                 Run Time: 
                <%= BelowEmergencyOrderPointV2.getTime() %>
            </div>
        </div>
        <br/><br/><br/><br/>
         
   
         
        <%
        BelowEmergencyOrderPointV2 osr = new BelowEmergencyOrderPointV2();
        
if(true){ //all

int i=0;
ResultSet rsProducts = BelowEmergencyOrderPointV2.getProducts();
for(int p=0; p<products.length; p++){
    List<FacilityTemplate> directorateList = BelowEmergencyOrderPointV2.getDirectoratesList(dir);    
    for(FacilityTemplate directorate : directorateList){
//int dispensed = BelowEmergencyOrderPointV2.dispensedDirectorate
//(products[p], mon, year, dat, fY, tY, fM, tM, hq, directorate.getFacilityId() + "", directorate.getFacilityId() + "") ;                                                         
//if(dispensed == 0 ) continue;
List<FacilityTemplate> facilityList = 
BelowEmergencyOrderPointV2.belowEmergencyOrderPointList
(products[p], mon, year, dat, fY, tY, fM, tM, hq, dir, directorate.getFacilityId() + "") ;                                                         
if(facilityList.size() == 0 ) continue;
int rloop = 0;
%>

        <div style="text-align:left; clear:both; font-weight:bold">
            Product: 
            <%= BelowEmergencyOrderPointV2.getProdName(products[p]) %>
        </div>
         
        <%if( (rloop%4 ==0 && rloop != 0 )  ){
 %>
        <div class="ba" style="clear:both"></div>
        <br/>
         
        <br/><br/><br/><br/>
         
        <%}rloop++;%>
         

 <table border="1" cellpadding="2" cellspacing="3" width="100%">
        <tr>
        <td>Code</td>
        <td>Name</td>
        <td>Type</td>
        <td>MOS</td>
        <td>Closing Balance</td>
        <td>Average Monthly Consumption.</td>
        <td>Contact</td>
        <td>Phone</td>
        </tr>        
        
 
<%
for(FacilityTemplate facility : facilityList){
String facilityId = facility.getFacility().getFacilityId() + "";
String facilityName = facility.getFacility().getFacilityName() + "";
String facilityType = facility.getFacilityType() + "";
String lvl = facility.getFacility().getFacilityTypeHierarchyId();
double mos = facility.getMonthOfSupplier();

//boolean condition = (lvl.equals("3") && mos < 0.55 ) || (lvl.equals("2") && mos <= 1.0);

String code = facility.getFacility().getFacCode() + "";
String contact = facility.getFacility().getContact() + "";
String phone = facility.getFacility().getPhone() + "";

//if( ! condition )
//continue;
int pC = 0;

double closeBalance = Double.valueOf(facility.getClosingBal());         
double avg = Double.valueOf(facility.getAvg());

  pC = 0;
 
 %>





<tr>
<td>
<%= code %>
</td>
<td>
<%= facilityName  %>
</td>
<td>
<%= facilityType %>
</td>
<td>
<%=mos%>
</td>
<td>
<%= df.format(closeBalance) %>
</td>
<td>
<%= avg %>
</td>
<td>
<%= phone %>
</td>
<td>
<%= contact %>
</td>
</tr>                 
        <%
i++;
%>
         
<%
} // Products Looping
%>

      </table>

<%
}
}
}
%>

    </body>
</html>