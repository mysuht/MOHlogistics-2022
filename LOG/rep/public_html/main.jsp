<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.Report, moh.logistics.lib.reports.code.FacilityTemplate, moh.logistics.lib.reports.code.Home, java.sql.*, java.util.*" %>
<html>
    <head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>



<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />       
<link href="resources/css/multiple-select.css" rel="stylesheet" type="text/css" />         
<link rel="stylesheet" type="text/css" href="assets/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="assets/jquery.multiselect.filter.css" />
<link rel="stylesheet" type="text/css" href="assets/style.css" />
<link rel="stylesheet" type="text/css" href="assets/prettify.css" />
<link rel="stylesheet" type="text/css" href="assets/jquery_ui1.css" />
<!--<script type="text/javascript" src="assets/ajax1.js"></script>-->
<style type="text/css">
btn {
    
}

</style>

<script src="resources/bootstrap/js/jquery-3.2.1.js" type="text/javascript"></script>
<script src="resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="resources/js/logJS.js" type="text/javascript"></script>
<script src="resources/js/jquery.multiple.select.js" type="text/javascript"></script>
<script src="resources/js/mainScripting.js" type="text/javascript"></script>
<script type="text/javascript" src="assets/prettify.js"></script>
<script type="text/javascript" src="assets/ajax2.js"></script>
<script type="text/javascript" src="assets/jquery.multiselect.js"></script>
<script type="text/javascript" src="assets/jquery.multiselect.filter.js"></script>
<script type="text/javascript" src="assets/prettify.js"></script>
        
    </head>
    <body style="background-color:#94deff">
    <form id="f1" action="processrep" method="POST" target="_blank">
    <div style="width:100%">
    
    <h1 align="center" > LOGISTICS SYSTEM </h1>
    <%
   
    //ResultSet rs = db.getFacTypesMain1();
    List<FacilityTemplate> facilityList = Home.getFacTypesMain1();
    int maxReportedYear = Home.maxReportedYear();
    String dir = request.getParameter("pd")==null?" ":request.getParameter("pd");
    String rep = request.getParameter("re")==null?" ":request.getParameter("re");
    String comt = request.getParameter("comt") == null? " ": request.getParameter("comt");
    %>
    <div >
    
    <div style=" margin-left:10pt;float:left;width:15%">
   <h2 align="center" style="background-color:blue;width:240pt;height:22pt;padding-top:11pt;font-weight:bold; font-size:14pt;"><span style="color:white;font-size:14pt; font-weight:bold;"> Reports Menu </span> </h2>
   <div align="center">
   <select  name="rep" id="rep" size="12" style="width:240pt; font-weight:bold;">
   
   <%
   //ResultSet rsReports = db.getReportsNames();
   List<Report> rsReports =  Home.getReportsNames();
   //while(rsReports.next()){
   for(Report report: rsReports){
   if(rep.trim().equals(report.getReportID() + "")){
   System.out.println("Report is Selected : " + report.getReportID() +  " VS. " + rep );
   %>
   <option value="<%= report.getReportID() %>" selected="selected">  <%= report.getReportName() %> </option>
   <%}else{
   System.out.println("Report is Not Selected : " + report.getReportID() +  " VS. " + rep );
   %>
   <option value="<%= report.getReportID() %>" >  <%= report.getReportName() %> </option>
   <%
   }
   }%>
   
   </select>
   </div>
    </div>
    
    
    
    <!-----------faaaaaaaaaaaaaaaasel -------->
    
    
    <div class="row" style=" float:left; margin-left:110pt;margin-top:12pt;">
    
    <div style=" border-radius:10px;border:solid 0px; padding:5px; width:750px;">
Type: &nbsp; <select name="p_dir" id="p_dir" >
<option value="0" >All </option>
<% 
//while(rs.next()){ 
for(FacilityTemplate facility: facilityList){
%>

<%


if(dir.trim().equals(facility.getFacilityId() + "")){
%>

<option value="<%= facility.getFacilityId() %>" selected="selected" > <%= facility.getFacilityName() %> </option>
<%}else{%>
<option value="<%= facility.getFacilityId() %>"  > <%= facility.getFacilityName() %> </option>
<%}%>
<%}%>

</select> <br/>

Facility : &nbsp; <select name="p_cen"  id="p_cen" multiple="multiple" style="width:250pt;">
 
<%
//ResultSet rsCnt = db.getFacilityByTypeId(dir);
List<FacilityTemplate> rsCnt = Home.getFacilityByTypeId(dir);
//while(rsCnt.next()){
for(FacilityTemplate cnt: rsCnt){

%>

<option value="<%= cnt.getFacilityId() %>" selected="selected"> <%= cnt.getFacilityName() + "--"+cnt.getFacCode() %> </option>
<%}%>
</select>
<!--<script src="resources/js/jquery.multiple.select.js"></script>
    <script type="text/javascript" >
      $('#p_cen').multipleSelect();
    </script>-->
<input type="hidden" name="pd" id="pd" />
 </div>
 <div style="margin-top:20pt;margin-bottom:20pt;clear:both;  height:25px;">
   
    <div style="float:left;border:0px solid; border-radius:10px;padding:5px;" > 

     Month/Annual: <input type="radio" value="m" name="dat" checked="checked" id="dat" /> &nbsp; &nbsp;
     Quarter/Annual: <input type="radio" value="q" name="dat" id="dat" /> &nbsp; &nbsp;
     Halfs Year: <input type="radio" value="hq" name="dat" id="dat" /> &nbsp; &nbsp;
      User Defined: <input type="radio" value="u" name="dat" id="dat" />
     </div>
     
    <br/>
     
     <div style="margin-top:30pt;clear:both;border:0px solid; border-radius:10px; height:25px;" > 
     
    <div style="float:left" class="col-xs-4" id="mon">
    Month: &nbsp; <select name="p_mon">
    <option value="0">All </option>
    <%
    for(int i=1; i<=12 ; i++){
    String mon = "";
    
    switch(i){
    case 1: mon = "January";
    break;
    case 2: mon="February";
    break;
    case 3: mon="March";
    break;
    case 4: mon="April";
    break;
    case 5: mon = "May";
    break;
    case 6: mon = "June";
    break;
    case 7: mon = "July";
    break;
    case 8: mon= "August";
    break;
    case 9: mon = "September";
    break;
    case 10: mon= "October";
    break;
    case 11: mon = "November";
    break;
    case 12: mon= "December";
    break;
    
    }
    %>
    <option value="<%= i<10?("0"+i):i %>" > <%= mon %> </option>
    
    <%}%>
    </select>
    </div>
    
     <div style="float:left" class="col-xs-4" id="quart">
     Quarter: &nbsp; <select name="p_quart" class="sel_beh">
     <option value="0">All </option>
    <%
    for(int i=1; i<=4 ; i++){
    String qra = "";
    
    switch(i){
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
    <option value="<%= i %>" > <%= qra+" Quarter" %> </option>
    
    <%}%>
    </select>
    
    </div>
    
    <div style="float:left" class="col-xs-4" id="p_hq">
     Halfs Year:: &nbsp; <select name="p_hq" class="form-control">
    
    <%
    for(int i=1; i<=2 ; i++){
    String qra = "";
    
    switch(i){
    case 1: qra = "1st Half Year";
    break;
    case 2: qra="2nd Half Year";
    break;
    
    }
    %>
    <option value="<%= i %>" > <%= qra %> </option>
    
    <%}%>
    </select>
    
    </div>
    
     <div style="float:left" class="col-xs-4" id="y" >
   Year: &nbsp; <select name="p_year" class="form-control">
    <%
   for(int i=maxReportedYear; i>=2003; i--){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>
    
    
    </div>
    <div style="float:left" class="row" id="ud">
    <div style="float:left">
    
     From: <select name="p_fmon" class="form-control">
    
    <%
    for(int i=1; i<=12 ; i++){
    String mon = "";
    
    switch(i){
    case 1: mon = "January";
    break;
    case 2: mon="February";
    break;
    case 3: mon="March";
    break;
    case 4: mon="April";
    break;
    case 5: mon = "May";
    break;
    case 6: mon = "June";
    break;
    case 7: mon = "July";
    break;
    case 8: mon= "August";
    break;
    case 9: mon = "September";
    break;
    case 10: mon= "October";
    break;
    case 11: mon = "November";
    break;
    case 12: mon= "December";
    break;
    
    }
    %>
    <option value="<%= i<10?("0"+i):i %>" > <%= mon %> </option>
    
    <%}%>
    </select>   <select name="p_fyear" class="form-control">
    <%
    for(int i=maxReportedYear; i>=2003; i--){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>  &nbsp; &nbsp;    To: &nbsp;
    </div>
    <div style="float:left">
     <select name="p_tmon" class="form-control">
   
    <%
    for(int i=1; i<=12 ; i++){
    String mon = "";
    
    switch(i){
    case 1: mon = "January";
    break;
    case 2: mon="February";
    break;
    case 3: mon="March";
    break;
    case 4: mon="April";
    break;
    case 5: mon = "May";
    break;
    case 6: mon = "June";
    break;
    case 7: mon = "July";
    break;
    case 8: mon= "August";
    break;
    case 9: mon = "September";
    break;
    case 10: mon= "October";
    break;
    case 11: mon = "November";
    break;
    case 12: mon= "December";
    break;
    
    }
    %>
    <option value="<%= i<10?("0"+i):i %>" > <%= mon %> </option>
    
    <%}%>
    </select> 
    <select name="p_tyear" class="form-control">
    <%
    for(int i=maxReportedYear; i>=2003; i--){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>
    
    </div>
    
   
    
    </div>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
    
    </div>
    
    </div>
    <div style="clear:both;border:0px solid; border-radius:10px;padding:5px;width:250pt;">
     Product: &nbsp; <select name="p_prod" class="form-control" id="pProd" multiple="multiple" style="width:150pt;">
    <%
    List<Map<String, Object>>  products = Home.getProductList();
    for (int i=0; i<products.size(); i++) {
    %>
    <option value="<%= products.get(i).get("PROD_ID") %>" selected="selected" > <%= products.get(i).get("PRO_NAME") %> </option>
    
    <%}%>
    </select>
    <script type="text/javascript" src="resources/js/jquery.multiple.select.js"></script>
    <script type="text/javascript" >
      $('#pProd').multipleSelect();
    </script>
    
    </div>
    
    <div class="btn-group">
    <input type="submit" class="btn btn-primary" value="   View   "  />
    </div>
    </div>
    
    
    
    </div>
    <div style="font-weight:bold;clear:both; margin-left:20px;" id="appIn">  
    </div>
<input type="hidden" name="re" id="re" />
<input type="hidden" name="comt" id="comt" />
<input type="hidden" name="me" id="me" />
<input type="hidden" name="ye" id="ye" />

</div>
</form>
<script type="text/javascript">
$('#p_cen').multiselect().multiselectfilter();
</script>
</body>
</html>