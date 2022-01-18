<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*" %>


  
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
    String mon = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String dat = request.getParameter("dat");
    if(dat.equals("m")){
     mon = request.getParameter("p_mon");
    }else{
    mon = request.getParameter("p_quart");
    }
    String year = request.getParameter("p_year");
    String center[] = request.getParameterValues("p_cen");
    String dir = request.getParameter("p_dir");
    LogisticsReportsClass db = new LogisticsReportsClass();
   
      DecimalFormat df1 = new DecimalFormat("###.##");
    %>
    <div class="main_div" style="width:100%" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
   Data Entry Error Report <br/>
   <%
   if(mon.equals("0")){
   out.println("Report Period :"+year+" <br/>");
   }if(dat.equals("m") && !mon.equals("0")){
   out.println("Report Period : "+mon+"  , "+year+" <br/>");
   }
   if(dat.equals("q") && !mon.equals("0")){
   out.println("Report Period : "+mon+" Quarter  , "+year+ " <br/>");
   }
   %>
    
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
   
<br/><br/>

<%
if(!type.equals("0")){%>
<div id="dvData">
<%
String dateEntry = "";
String entryBy = "";
String changeOn = "";
String changedBy = "";
int ip = 0;
for(int i=0; i<center.length; i++){
%>

<%




if(db.getDataEntryErrorCheck(mon, year, center[i],dat)){
//out.println(ip+"\n");
ip++;
%>
 <div style="clear:both">
   <div style="float:left;width:200pt;">
   Supplying facility: <%= db.getDirInfo(db.getDirSupInfo(center[i],4),2)%>    </div>
   <div style="float:left; width:400pt; text-align:right">
   Suppling Code: <%= db.getDirInfo(db.getDirSupInfo(center[i],4),3) %>
   </div>
<div style="width:200pt;clear:both;float:left">
<%= db.getDirInfo(center[i],2)%>
</div>
<div style="float:left; width:200pt; text-align:right">
    <%= db.getDirType(dir) %>
   </div>  
   <div style="float:left; width:200pt; text-align:right">
  Facility Code:  <%= db.getDirInfo(center[i],3) %>
   </div>  
    </div>

<table border="1" cellpadding="2" cellspacing="3" style="clear:both"  >
<tr class="col_titles">
<td align="center">
Product
</td>
<td align="center">
Opening
</td>
<td align="center">
Receipts
</td>
<td align="center">
Issues
</td>
<td align="center">
Adjustments
</td>
<td align="center">
Type
</td>
<td align="center">
Closing Bal.
</td>
<td align="center">
Avg con.
</td>
<td align="center">
Required
</td>
<td align="center">
Receipts
</td>
<td align="center">
New Users
</td>
<td align="center">
Con. Users
</td>
</tr>
<%

ResultSet rs = db.getDataEntryError(mon, year, center[i],dat);
while(rs.next()){
if( rs.getInt(5)< 0){
%>
<tr class="row_titles" style="font-weight:bold; font-style:italic"; >
<%}if( rs.getInt(5) >= 0){%>
<tr class="row_titles">
<%}%>
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getInt(2) %>
</td>
<td align="center">
<%= rs.getInt(3) %>
</td>
<td align="center">
<%= rs.getInt(4) %>
</td>
<td align="center">
<%= rs.getInt(5) %>
</td>
<td align="center">

<%= rs.getString(16) %>
</td>
</td>
<td align="center">
<%= rs.getInt(6) %>
</td>
<td align="center">
<%= rs.getInt(7) %>
</td>
<td align="center">
<%= rs.getInt(8) %>
</td>
<td align="center">
<%= rs.getInt(9) %>
</td>
<td align="center">
<%= rs.getInt(10) %>
</td>
<td align="center">
<%= rs.getInt(11) %>
</td>
</tr>
<%
dateEntry = rs.getString(12);
entryBy = rs.getString(13);
changeOn = rs.getString(14);
changedBy = rs.getString(15);
}%>
</table>
<br/>
<h5>Comments:<br/> <%= db.getCommintCtfMain(center[i], mon, year, dat, fY, tY, fM, tM, type, hq) %></h5>
</div>

<div style="margin-bottom:20pt;" class="ba">
<div style="float:left;width:230pt;">
Date Entry &nbsp;&nbsp;&nbsp;&nbsp; Entry By <br/>
<%= dateEntry %>&nbsp;&nbsp;&nbsp;&nbsp;<%= entryBy %> 
</div>
<div style="float:left;width:230pt;">
Changed On &nbsp;&nbsp;&nbsp;&nbsp; Changed By <br/>
<%= changeOn %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=changedBy %>
</div>


<br/><br/> <br/>
<%}
%>
</div>

<%}}///////////////specific Directorates%>


<%
System.out.println("hello");
if(type.equals("0")){ ///alllllllllllllll
String dateEntry = "";
String entryBy = "";
String changeOn = "";
String changedBy = "";
ResultSet rsDirs = db.getAllFac();

while(rsDirs.next()){

ResultSet rs = db.getDataEntryError(mon, year, rsDirs.getString(1),dat);
while(rs.next()){
System.out.println("Type name is"+rs.getString(16)+" and type id is"+rs.getString(17));
if( (rs.getInt(17) !=  1 ) || (rs.getInt(17) !=  2 )){
continue;
}
}



if(db.getDataEntryErrorCheck(mon, year, rsDirs.getString(1),dat)){

System.out.println("hello1");
%>
<br/>
 <div style="clear:both; font-weight:bold;">
   <div style="float:left;width:200pt;">
   Supplying facility: <%= db.getDirInfo(db.getDirSupInfo(rsDirs.getString(1),4),2)%>    </div>
   <div style="float:left; width:400pt; text-align:right">
   Suppling Code: <%= db.getDirInfo(db.getDirSupInfo(rsDirs.getString(1),4),3) %>
   </div>
<div style="width:200pt;clear:both;float:left">
<%= db.getDirInfo(rsDirs.getString(1),2)%>
</div>
<div style="float:left; width:200pt; text-align:right">
        &nbsp;
   </div>  
   <div style="float:left; width:200pt; text-align:right">
  Facility Code: <%= db.getDirInfo(rsDirs.getString(1),3) %>
   </div>  
    </div>

<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<tr class="col_titles">
<td align="center">
Product
</td>
<td align="center">
Opening
</td>
<td align="center">
Receipts
</td>
<td align="center">
Issues
</td>
<td align="center">
Adjustments
</td>
<td align="center">
Type
</td>
<td align="center">
Closing Bal.
</td>
<td align="center">
Avg con.
</td>
<td align="center">
Required
</td>
<td align="center">
Receipts
</td>
<td align="center">
New Users
</td>
<td align="center">
Con. Users
</td>
</tr>
<%

 rs = db.getDataEntryError(mon, year, rsDirs.getString(1),dat);
while(rs.next()){


System.out.println("Type name is"+rs.getString(16)+" and type id is"+rs.getString(17));
 
if( (rs.getInt(17) ==  1 ) || (rs.getInt(17) ==  2 )){
%>
<tr class="row_titles" style="font-weight:bold; font-style:italic"; >
<%}else{%>


<tr class="row_titles">
<%}%>
<td align="center">
<%= rs.getString(1) %>
</td>
<td align="center">
<%= rs.getInt(2) %>
</td>
<td align="center">
<%= rs.getInt(3) %>
</td>
<td align="center">
<%= rs.getInt(4) %>
</td>
<td align="center">
<%= rs.getInt(5) %>
</td>
<td align="center">

<%= rs.getString(16) %>
</td>
</td>
<td align="center">
<%= rs.getInt(6) %>
</td>
<td align="center">
<%= rs.getInt(7) %>
</td>
<td align="center">
<%= rs.getInt(8) %>
</td>
<td align="center">
<%= rs.getInt(9) %>
</td>
<td align="center">
<%= rs.getInt(10) %>
</td>
<td align="center">
<%= rs.getInt(11) %>
</td>
</tr>
<%
dateEntry = rs.getString(12);
entryBy = rs.getString(13);
changeOn = rs.getString(14);
changedBy = rs.getString(15);
}%>
</table>

<h5>Comments:<br/> <%= db.getCommintCtfMain(rsDirs.getString(1), mon, year, dat, fY, tY, fM, tM, type, hq) %></h5>



<div style="margin-bottom:20pt;" class="ba">
<div style="float:left;width:230pt;">
Date Entry &nbsp;&nbsp;&nbsp;&nbsp; Entry By <br/>
<%= dateEntry %>&nbsp;&nbsp;&nbsp;&nbsp;<%= entryBy %> 
</div>
<div style="float:left;width:230pt;">
Changed On &nbsp;&nbsp;&nbsp;&nbsp; Changed By <br/>
<%= changeOn %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=changedBy %>
</div>

</div>
<br/><br/> <br/>
<%}}}///////////////specific Directorates%>




<!--
<input type="button" class="submitButton" onclick="write_to_excel();" id="btnExport" value="Download To Excel"/> 
-->








<%
db.s();
%>
</form>
</body>
</html>