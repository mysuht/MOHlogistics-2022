<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=windows-1256"
         import="java.util.* ,moh.logistics.lib.reports.code.FacilityTemplate ,com.logistics.lib.report.FacilityProductDispensedReportV1,moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>

<html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
        function exportToExcel(){
    intable = document.getElementById("dvData");
    this.table = intable.cloneNode(true);
    var cform = document.createElement("form");
    cform.style.display = "none";
    cform.setAttribute("method","POST");
    cform.setAttribute("action","exporttoexcel.jsp");
    cform.setAttribute("name","ExcelForm");
    cform.setAttribute("id","ExcelForm");
    cform.setAttribute("enctype","MULTIPART/FORM-DATA");
    cform.encoding="multipart/form-data";
    var ta = document.createElement("textarea");
    ta.name = "ExcelTable";
    var tabletext = this.table.outerHTML;
    ta.defaultValue = tabletext;
    ta.value = tabletext;
    cform.appendChild(ta);
    intable.parentNode.appendChild(cform);
    cform.submit();
    //clean up
    ta.defaultValue = null;
    ta = null;
    tabletext = null;
    this.table = null;
}       
        </script>
    </head>
    <body>
    <form>
    <%
    int sum = 0;
    
    String dir[];
    DecimalFormat df = new DecimalFormat("###,###.#");
    String qra = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String hq = "";
    int qr = request.getParameter("p_quart")==null?null:Integer.parseInt(request.getParameter("p_quart")) ;
    String quart = "";
    String dat = request.getParameter("dat");
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
    String type = request.getParameter("p_dir");
    String year = request.getParameter("p_year");
    //if(!type.equals("0")){
    
    //}
    String prod[] = request.getParameterValues("p_prod");
    
    LogisticsReportsClass db = new LogisticsReportsClass();
    ResultSet rs = db.getProgDisToUserRep(quart+"", year,dat,fY,tY, fM, tM, hq);
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
    
    
    <div class="main_div" style="width:100%; clear:both" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Facility Product Dispensed Report <br/>
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
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    out.println(rs11.getString(2));
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
    <br/>
    <br/>
    <% //all_fac
    
    if(true){
    int totality = 0;
    int dirNo=0;
    dir = request.getParameterValues("p_cen");
    ResultSet rsCnt = db.getDirectoratesD();
   
    String product = "";
    String productN = "";
    for(int p=0; p<prod.length;p++){
    if(p == 0){
    product = prod[p];
    productN = db.getProdName(prod[p]);
    }else{
    product += ",  "+prod[p];
     productN += ",  "+db.getProdName(prod[p]);
    }
}
int rloop = 0;
List<FacilityTemplate> directorateList = 
FacilityProductDispensedReportV1.getDirectoratesList(dir);
    // while(rsCnt.next()){
    for(FacilityTemplate directorate: directorateList){
        if( (directorate.getFacilityId()+"").equals("498"))
        continue;
        List<FacilityTemplate> facilityList =
        FacilityProductDispensedReportV1.getFacilityDispensedRepAllIntersect(quart, year, prod,dat,fY,tY, fM, tM,type, hq, directorate.getFacilityId()+"", dir);
        if(facilityList.size() == 0 ) continue;
    %>
    <br/>
    <br/>
    <br/>
     <% if(rloop!=0){ %>
  <div class="ba" style="clear:both"> 
    </div>
    <br/>
    <br/>
    <br/>
    <div style="clear:both; font-weight:bold; text-align: center;" align="center">&nbsp;</div>
    <br/>
     <div style="clear:both; font-weight:bold; text-align: center;" align="center">&nbsp;</div>
    <br/>
    <div style="clear:both; font-weight:bold; text-align: center;" align="center">&nbsp;
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
    <div style="clear:both; font-weight:bold;">
    <div style="clear:both; font-weight:bold;" >
   Contraceptives &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= productN %> <br/>
    Supplying Facility: <%= db.getDirCode(directorate.getFacilityId()+"") %> &nbsp;&nbsp;&nbsp;&nbsp; <%= db.getDirNameX(directorate.getFacilityId()+"") %>
    </div>
<br/>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">Facility Code </td>

<td align="center">
Facility Name
</td>
</tr>
<%
//String main = db.getFacProdDispensedRepMain(quart+"", year, product, directorate.getFacilityId()+"",dat,fY,tY, fM, tM,type, hq,prod.length);


 dirNo=0;
 int rrloop = 0;
//while(rs1.next()){
for(FacilityTemplate facility: facilityList){
%>


 <% if(rrloop!=0 && rrloop%20 == 0){ %>
 </table>
 <br/>
  <div class="ba" style="clear:both"> 
    </div>
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
 <div style="clear:both; font-weight:bold;" >
   Contraceptives &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= productN %> <br/>
    Supplying Facility: <%= db.getDirCode(directorate.getFacilityId()+"") %> &nbsp;&nbsp;&nbsp;&nbsp; <%= db.getDirNameX(directorate.getFacilityId()+"") %>
    </div>

<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">Facility Code </td>

<td align="center">
Facility Name
</td>
</tr>
<%}rrloop++;%>


<tr class="row_titles">
<td align="center">
<%= facility.getFacCode()%>
</td>

<td align="center">
<%= facility.getFacilityName() %>
</td>
</tr>


<%dirNo++;
}%>
</table>
</div>
<div class="ba">
<h4>
Total No. of Facilities: <%= dirNo %>
<% totality += dirNo; 
%>
</h4>
</div>

<%



}

//} %>
<br/><br/><br/><br/>
<h3 align="left"> Total for whole Country:  <%= totality%> </h3>
<br/>
<%
}// All Types 
db.s();
%>
</form>
</body>
</html>