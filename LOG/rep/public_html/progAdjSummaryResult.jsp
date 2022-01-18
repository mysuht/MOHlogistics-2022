<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
          <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    <%
    String products[] = request.getParameterValues("p_prod");
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String mon = "";
    String dat = request.getParameter("dat");
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
    LogisticsReportsClass db = new LogisticsReportsClass();
   
    %>

 <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Family Planning Program
    </div>
     <div class="dCenter">
      Program Adjustments Summary <br/>
  
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
    }else{
    out.println(rs12.getString(2));
    }
    %>
  
   <br/>
   <%
    if(type.equals("0")){
      out.println("All Facilities.");
    }else{
    String dirs = "";
    String pp [] =request.getParameterValues("p_cen");
   
    for(int sp = 0; sp < pp.length; sp++){
     //ResultSet cenName = ;
     //cenName.next();
    if(sp == 0){
    
    dirs = db.getDirName(pp[sp]);
    }else{
    dirs += ", "+db.getDirName(pp[sp]);
    }
    }
    if(db.getTypeLvl(pp[0]).equals("2"))
    out.println(db.getFacilityName(pp[0]));
    }
   %>
   
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/><br/><br/><br/><br/>
    <%
    //Hear
    if(type.equals("0")){
    %>
    
    <table class="table-fluid" border="1" cellpadding="2" cellspacing="3" style="clear:both" >
    
    <%
    for(int i=0; i<products.length; i++){
    ResultSet rs = db.getProgAdjSummary(products[i], mon, year,dat,fY,tY, fM, tM,type, hq);
    %>
    

<!--
<caption style="font-weight:bold; background-color:green"> <%= db.getProdName(products[i]) + "                " +db.getProdDose(products[i]) %>   </caption>
-->
<%
if(rs.next()){
%>
<tr class="col_titles">

<td align="center">
Product
</td>
<td align="center">
Adjustments Type
</td>
<td align="center">
Total Adjusted
</td>

</tr>
<%}%>

<%
 rs = db.getProgAdjSummary(products[i], mon, year,dat,fY,tY, fM, tM,type, hq);
int cc = 0;
while(rs.next()){
%>

<tr class="row_titles">
<td align="center"> <%= cc == 0 ? db.getProdName(products[i]) + "                " +db.getProdDose(products[i]) : " "  %>  </td>
<td align="center"> <%= rs.getString(1) %>  </td>
<td align="center"> <%= rs.getInt(2) %>  </td>
</tr>
<%
cc++; 
}%>
<!-- close table -->
<%}
%>
</table>
<br/><br/><br/>
<%}%>













 <%
    if(!type.equals("0")) {
    String cen[] = request.getParameterValues("p_cen");
    
    
  %>
  <!--
    <div class="ba">
    </div><br/>
    -->
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
    <table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
    <%
    
    for(int i=0; i<products.length; i++) {
    %>
    

<!--
<caption style="font-weight:bold; background-color:green"> <%= db.getProdName(products[i]) + "                " +db.getProdDose(products[i]) %>   </caption>
-->
<%
ResultSet rs = db.getProgAdjSummary(products[i], mon, year,dat,fY,tY, fM, tM,type, hq,cen);
if(rs.next()){
%>
<tr class="col_titles">
<td align="center">
Product
</td>
<td align="center">
Adjustments Type
</td>
<td align="center">
Total Adjusted
</td>

</tr>
<%}%>
<%

 rs = db.getProgAdjSummary(products[i], mon, year,dat,fY,tY, fM, tM,type, hq,cen);

while(rs.next()){
%>

<tr class="row_titles">
<td align="center"> <%=db.getProdName(products[i]) + "                " +db.getProdDose(products[i])%></td>
<td align="center"> <%= rs.getString(1) %>  </td>
<td align="center"> <%= rs.getInt(2) %>  </td>
</tr>
<%}%>


<%}%>
</table>
<br/><br/><br/>
<%}%>















<%
db.s();
%>
</body>
</html>