<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="java.util.*, moh.logistics.lib.reports.code.FacilityTemplate ,com.logistics.lib.report.FacilityProductDispensedReport ,moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    <%
    
    DecimalFormat df = new DecimalFormat("###,###.#");
    String dat  = request.getParameter("dat");
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String quart = "";
    String qra = "";
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
    int qr = Integer.parseInt(request.getParameter("p_quart")) ;
    String year = request.getParameter("p_year");
    String products[] = request.getParameterValues("p_prod");
    
    LogisticsReportsClass db = new LogisticsReportsClass();
    //ResultSet rs = db.getProgDisToUserRep(quart+"", year,dat,fY,tY, fM, tM,type, hq);
    switch(qr){
    case 1: qra = "1st";
    break;
    case 2: qra="2nd";
    break;
    case 3: qra="3rd";
    break;
    case 4: qra="4th";
    break;
    }
    %>
    
    
    <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Program Product Dispensed Report <br/>
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
  
   <br/>
  
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/>
    <%
    if(true){
    String [] cen = request.getParameterValues("p_cen");
   String proNames = "";
    %>
    <br/>
    <br/>
    <br/>
    <br/>
    
    <br/>
    <div style="clear:both;font-weight:bold; font-size:14pt;" >
   Contraceptives &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   
   <%
    for(int i=0; i<products.length; i++){
   %>
   <%
   if(i == 0){
   proNames = db.getProdName(products[i]);
   }else{
   proNames += ", "+ db.getProdName(products[i]);
   }
   %>
    <%}
    out.println(proNames); %>
    </div>
<br/><br/>






<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">Facility Name </td>

<td align="center">
Number Of Facilities
</td>
</tr>
<%
int sum = 0;
List<FacilityTemplate> facilityList =
db.getDirectoratesList(cen);
//ResultSet rs1 = db.getDirectoratesD();
int rloop=0;
//while(rs1.next()){
for(FacilityTemplate facility:facilityList){

int counts = FacilityProductDispensedReport.getFacilityDispensedRepAllIntersect(quart, year, products,dat,fY,tY, fM, tM,type, hq, facility.getFacilityId()+"", cen).size();
if(counts == 0)
continue;
%>
<% if(rloop%17==0 && rloop!=0){%>
</table>

<div class="ba"></div>

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
 <div style="clear:both;font-weight:bold; font-size:14pt;" >
   Contraceptives &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= proNames%>
   </div>
   <br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">Facility Name </td>

<td align="center">
Number Of Facilities
</td>
</tr>
<%}rloop++;%>
<tr class="row_titles">
<td align="center">
<%= facility.getFacilityName() %>
</td>

<td align="center">
<%= counts %>
<%
//sum+= rs1.getInt(4);
sum+= counts;
%>
</td>
</tr>


<%}%>
<tr class="col_titles">
<td align="center">
Total For Whole Country
</td>
<td align="center">
<%= sum %>
</td>
</tr>
</table>

<%//}

}//type All%>



<%
/////////////////////////////////////////----------------------

%>







<%
db.s();
%>
</body>
</html>