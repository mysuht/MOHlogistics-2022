<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.AdjSummaryClass,moh.logistics.lib.reports.UtilsClass, java.sql.*, java.text.*, java.util.*" %>
<%@taglib prefix="shared" tagdir="/WEB-INF/tags" %>

    <shared:header></shared:header>
    
    <%
    
    
    
    
    //String products[] = new String [];
    
    
    
      DecimalFormat df = new DecimalFormat("#.#");
      DecimalFormat df1 = new DecimalFormat("#.##");
    String dir [] = request.getParameterValues("p_cen");
    String prod[] = request.getParameterValues("p_prod");
    String dat = request.getParameter("dat") == null ? null : request.getParameter("dat"); 
    String mon = "";
    String type = request.getParameter("p_dir");
String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";
    if(dat.equals("m")){
    mon = request.getParameter("p_mon")==null? null: request.getParameter("p_mon");
    }if(dat.equals("q")){
    mon = request.getParameter("p_quart") == null ? null : request.getParameter("p_quart");
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
    String year = request.getParameter("p_year");
    AdjSummaryClass db = new AdjSummaryClass();
   %>
   
   <!--<div class="main_div" > -->
   <div class="main_div">
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Family Planning Program
    </div>
     <div class="dCenter">
    Adjustments Summary <br/>
   
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
     ResultSet rs11 = db.getFacTypesMainName(type);
     //List<Map<String, Object>> rs11 = db.getFacTypesMainNameListD(type);
   
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
    //for(Map map: rs11){
    if(rs11.next()){
    out.println(rs11.getString(2));
    }
    }
    %>
     </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
   <%
   if(!type.equals("0")){
   System.out.println("sdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
   for(int p=0; p<prod.length;p++){
    %>
    
 
    
    
    
   

    <%
    // myimpls
    System.out.println(" ^^^^^^^^^^^^^^^^^ "+"sdfsdfsdf"+"^^^^^^^^^^^^^") ;
    int pp = 0;
    ResultSet rsDir = db.getDirectorates();
    //for(int i=0; i<dir.length; i++){
    if(db.FacilityLVL(dir[0]) == 2){
    rsDir = db.dirsLVL2(dir, type);
    }else{
    rsDir = db.getDirectorates();
    }
    while(rsDir.next()){
   
   // now
    // List<Map<String,Object>> rsMain = db.getAdjSummaryMainList(prod[p], mon, year, dir[i],dat,fY,tY, fM, tM,type, hq);
     List<Map<String,Object>> rsMain = db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq, dir);

    %>
    
   
    <%
   
  //  if(rsMain.next()){
  int sm = 0;
      for(Map rsMainresults: rsMain ) {
 
    %>
    <% 
    if  (sm == 0 ){
    
    
    if(pp!=0 || p!= 0){
   
    %>
    
    <br/>
    <div class="ba"></div>
    <%}%>
    <br/>
    <div style="width:100%; clear:both;" class="page-header">
    <br/>
    <!-- imps -->
    <!--<h3 align="left"> <%= db.getProdName(prod[p]) +"   " + db.getProdDose(prod[p]) %> </h3>-->
    <h4>Supplier:  <%= db.getFacilityName(rsDir.getString(1)) %>  </h4> 
     <h5>Product:  <%= db.getProdName(prod[p]) %>  </h5> 
     <%
     sm++;
     }
     }%>
    </div>
<table class="table-fluid" border="1" cellpadding="2" cellspacing="3" style="clear:both" width="40%" >
<%
//rsMain = db.getAdjSummaryMain(prod[p], mon, year, dir[i],dat,fY,tY, fM, tM,type, hq);
rsMain = db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq,dir);
 if(rsMain.size() > 0 ){
//if(rsMain.next()){

%>

<thead>
<tr class="col_titles">
<th>
Adjustment Type
</th>
<th>  Facility Name </th>
<th>  Adjusted </th>
</tr>
</thead>

<%
 pp++;
}else{%>

<!--  

<span style="background-color:red; font-style:italic; font-weight:bold; font-size:10pt;" > No Data Found  </span>
-->

<%}%>
<%
//rsMain = db.getAdjSummaryMain(prod[p], mon, year, dir[i],dat,fY,tY, fM, tM,type, hq);
rsMain = db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq, dir);
//while(rsMain.next()){
for(Map rowMain: rsMain){
// dosomething
//ResultSet rs = db.getAdjSummary(prod[p], mon, year, dir[i], rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);
//ResultSet rs = db.getAdjSummary(prod[p], mon, year, dir[i], rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq);
List<Map<String, Object>> rs = db.getAdjSummaryList(prod[p], mon, year, rsDir.getString(1), rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq, dir);

%>

<%
int facCount=0;
int sum =0;
//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);
//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq);

if(rs.size() == 0){
continue;
}

//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq);


//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);
//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq);


//while(rs.next()){
for(Map row: rs) {
facCount++;
%>

<tbody>
<tr class="row_titles" >
<%
// rs.getString(2)
// rs.getString(1)
// rs.getInt(3)
%>
<td align="center"> <%= row.get("type_name").toString() %>  </td>
<td align="center"> <%= row.get("fac_name").toString() %>  </td>
<td align="center"> <%= Integer.parseInt(row.get("adjustments").toString()) %>  </td>
</tr>
<%
//sum += rs.getInt(3);
sum+= Integer.parseInt(row.get("adjustments").toString());
 
 } 

//rs = db.getAdjSummary(prod[p], mon, year, dir[i], rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);
// rs = db.getAdjSummary(prod[p], mon, year, dir[i], rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq);


//if(!rs.next()){
//continue;
//}else{
%>

</tbody>

<tr class="col_titles">
<td  align="center" >
<%= "Total "+"= "+ facCount + " facility" %>
</td> <td></td>
<td  align="center" >
<%=sum %>
</td>


</tr>

<%

//}
sum = 0;


} // rsMain%>
</table>
</div>
<%

}

} 
// products

// For Loop Directorates
}//-----------------------------------------------
%>

 <%
 // all facilities
 int s = 0;
   if(type.equals("0")) {
   
   %>

   
   
   
   <%
   for(int p=0; p<prod.length;p++) {
    %>
    
 
    
    
    <%
    
    System.out.println(" ^^^^^^^^^^^^^^^^^ "+"sdfsdfsdf"+"^^^^^^^^^^^^^") ;
    ResultSet rsDir = db.getDirectorates();
   int pp = 0;
   while(rsDir.next()) {
   
   //  ResultSet rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);
     List<Map<String, Object>> rsMain =
     db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq,dir);

    %>
  
    <% 
    int dp = 0;
    
    int smp = 0;
    
    for(Map rowMain: rsMain){
   if(smp == 0){ 
    %>
      <% if(pp!=0 || p!= 0){
   
    %>
    
    <br/>
    <div class="ba"></div>
    <%}%>
    <br/>
    <br/><br/>
    <%if(s==1)%>
    
   
   
  
    <div style="width:100%; clear:both;" class="page-header">
    
    
    <h4>
     Supplier:  &nbsp;
    <%= db.getFacilityName(rsDir.getString(1)) %>  
    </h4> 
     <h5>
     Product:  <%= db.getProdName(prod[p]) %>  
     </h5> 
    </div>
    <%
  
    }
    
    smp++;
    }s=0;%>

<%
 int rloop=0;

//rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);

rsMain = 
db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq,dir);

int rp = 0;
//if(rsMain.next()){
if(rsMain.size() > 0 ){
rp++;
%>


<table class="table-fluid" border="1" cellpadding="2" cellspacing="3" style="clear:both" width="40%" >
<tr class="col_titles">
<td align="center">
Adjustment Type
</td>
<td align="center">  Facility Name </td>
<td align="center">  Adjusted </td>
</tr>
<%
  pp++;
}else{%>

<!--  

<span style="background-color:red; font-style:italic; font-weight:bold; font-size:10pt;" > No Data Found  </span>
-->

<%}%>
<%
//rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);
rsMain = db.getAdjSummaryMainList(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq,dir);
int su = 0;

// while(rsMain.next()){
for(Map rowMain: rsMain){

//ResultSet rs = db.getAdjSummary(prod[p], mon, year,rsDir.getString(1), rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);

List <Map<String, Object>> rs = 
db.getAdjSummaryList (prod[p], mon, year,rsDir.getString(1), rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,type, hq,dir);

%>

<%
int facCount=0;

//while(rs.next()){
for(Map row: rs){

facCount++;
%>
<tr class="row_titles" >

<td align="center"> <%= row.get("type_name").toString() %>  </td>
<td align="center"> <%= row.get("fac_name").toString() %>  </td>
<td align="center"> <%= Integer.parseInt(row.get("adjustments").toString()) %>  </td>
</tr>
<%
su += Integer.parseInt(row.get("adjustments").toString()) ;
} 
%>

<tr class="col_titles">
<td  align="center" >
<%= "Total "+"= "+ facCount + " facility" %>
</td> <td></td>
<td  align="center" >
<%= su %>
<%
su=0;
%>
</td>


</tr>

<%
//}//rs
} // rsMain%>
</table>





<%

//rr++;




}




// MOH / MCH DATA 

%>

<%
  
   //  ResultSet rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);
     List<Map<String, Object>> rsMain =
     db.getAdjSummaryMainList(prod[p], mon, year, db.getFacilityMaster()+"",dat,fY,tY, fM, tM,"mch", hq,dir);

   
     int dp = 0;
    
    //if(rsMain.next()){ 
    int pop=0;
    for(Map rowMain: rsMain){
    if(pop == 0 ){
    %>
      <% if(pop == 0){
   
    %>
    
    <br/>
    <div class="ba"></div>
    <%}%>
    

    
    <br/>
    <br/><br/>
    <%
    //if(s==1)%>
    
   
   
  
    <div style="width:100%; clear:both;" class="page-header">
    
    
    <h4>Supplier:  MOH/MCH DIRECTORATE  </h4> 
     <h5>Product:  <%= db.getProdName(prod[p]) %>  </h5> 
    </div> <%
  
    }s=0;
    
    pop++;}
    %>

<%
  int rloop=0;

//rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);

rsMain = 
db.getAdjSummaryMainList(prod[p], mon, year,  db.getFacilityMaster()+"" ,dat,fY,tY, fM, tM,"mch", hq, dir);

 int rp = 0;
//if(rsMain.next()){
if(rsMain.size() > 0 ) {
rp++;
%>


<table class="table-fluid" border="1" cellpadding="2" cellspacing="3" style="clear:both" width="40%" >
<tr class="col_titles">
<td align="center">
Adjustment Type
</td>
<td align="center">  Facility Name </td>
<td align="center">  Adjusted </td>
</tr>
<%
  pp++;
}else{%>

<!--  

<span style="background-color:red; font-style:italic; font-weight:bold; font-size:10pt;" > No Data Found  </span>
-->

<%}%>
<%
//rsMain = db.getAdjSummaryMain(prod[p], mon, year, rsDir.getString(1),dat,fY,tY, fM, tM,type, hq);
rsMain = db.getAdjSummaryMainList(prod[p], mon, year,  db.getFacilityMaster()+"",dat,fY,tY, fM, tM,"mch", hq,dir);
 int su = 0;

// while(rsMain.next()){
for(Map rowMain: rsMain){

//ResultSet rs = db.getAdjSummary(prod[p], mon, year,rsDir.getString(1), rsMain.getString(1),dat,fY,tY, fM, tM,type, hq);

List <Map<String, Object>> rs = 
db.getAdjSummaryList (prod[p], mon, year, db.getFacilityMaster()+"", rowMain.get("adj_type_id").toString(),dat,fY,tY, fM, tM,"mch", hq,dir);

%>

<%
int facCount=0;

//while(rs.next()){
for(Map row: rs){

facCount++;
%>
<tr class="row_titles" >

<td align="center"> <%= row.get("type_name").toString() %>  </td>
<td align="center"> <%= row.get("fac_name").toString() %>  </td>
<td align="center"> <%= Integer.parseInt(row.get("adjustments").toString()) %>  </td>
</tr>
<%
su += Integer.parseInt(row.get("adjustments").toString()) ;
} 
%>

<tr class="col_titles">
<td  align="center" >
<%= "Total "+"= "+ facCount + " facility" %>
</td> <td></td>
<td  align="center" >
<%= su %>
<%
su=0;
%>
</td>


</tr>

<%
//}//rs
} // rsMain%>
</table>

<!-- END MOH/MCH -->

<%


} 
%>


<%
// end type 0

// For Loop Directorates
}//-----------------------------------------------
%>



<%
///////////////////////// end All
db.s();
%>


   <shared:footer></shared:footer>