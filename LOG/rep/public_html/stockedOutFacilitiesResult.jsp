<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.util.*, java.text.*, moh.logistics.lib.reports.code.StockOutFacility"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%
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
    String cen[] = request.getParameterValues("p_cen");
    String products[] = request.getParameterValues("p_prod");
    LogisticsReportsClass db = new LogisticsReportsClass();
  
      DecimalFormat df1 = new DecimalFormat("##0.0000");
      DecimalFormat df = new DecimalFormat("###,###.###");
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Familiy Planning Program
            </div>
            <div class="dCenter">
                Stocked Out Facilities 
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
    }else{
    out.println(rs12.getString(2));
    }
    %>
                 
                <br/><br/>
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
if(!type.equals("0")) {
%>
         
        <%
int pLop = 0;
String productss = "";
        for(int ip=0; ip<products.length; ip++){
            if(ip==0){
                productss = products[ip];
            }else{
            productss += ", "+products[ip];
            }
        }
int rloop = 0;


%>
         
        <%
int i=0;
int count = 0;
int is=0;
int px = 0;
for(int p=0; p<products.length; p++){
//px++;
px=0;
is=0;


StockOutFacility sofc = new StockOutFacility();
List<Map<String, Object>> facilityList = sofc.AllFacilities(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           cen);

 count = 0;
 %>
         
        <%
  pLop = 0;
for(Map<String, Object> row : facilityList){
String facilityId = row.get("FACILITY_ID").toString();
String lvl = row.get("lvl").toString();

pLop++;




List<Map<String, Object>> StockOutFacilityList = sofc.GetStockOutFacility(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           facilityId);
for(Map<String, Object> stockOutFacility : StockOutFacilityList){                                                           
 double avg = Double.valueOf(stockOutFacility.get("AVG").toString());
 double closeBal = Double.valueOf(stockOutFacility.get("CLOSE_BAL").toString());
 double adjustments = Double.valueOf(stockOutFacility.get("ADJUSTMENTS").toString());
 double adjType = Double.valueOf(stockOutFacility.get("ADJ_TYPE_ID").toString());
boolean condition = false;

if(stockOutFacility.get("ADJ_TYPE_ID").toString().equals("12") 
|| stockOutFacility.get("ADJ_TYPE_ID").toString().equals("17") 
|| stockOutFacility.get("ADJ_TYPE_ID").toString().equals("19") 
) 
continue;

if((avg > 0.0 && closeBal == 0.0))
condition = true;




if(! condition ) {




continue;


}

count ++;


String code = row.get("Code") + "";
String facilityName = row.get("FacilityName") + "";
String contact = row.get("Contact") + "";
String facilityType = row.get("FacilityType") + "";
String phone = row.get("Phone") + "";

System.out.println(" &&&&&&&&&&&&&&&&&&&& FacilityId is : " + facilityId + " and CODE IS : " +  code + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& name IS : " + facilityName + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& contact IS : " + contact + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& facilityType IS : " + facilityType + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& phone IS : " + phone + " &&&&&&&&&&&&&&&&&&&&");

%>
         
        <% if(rloop % 5 ==0 && rloop!=0){%>
        <div class="ba"></div>
        <br/><br/>
         
        <br/><br/>
         
        <%}rloop++;
 %>
        <div align="left" style="text-align:left;clear:both;font-weight:bold">
            Supplier: 
            <%= db.getSupplierName(facilityId) %>
             &nbsp; &nbsp; 
            <span style="color:pink">
                <%= db.getSupplierCode(facilityId) %></span>
        </div>
        <h3 style="text-align:left; clear:both;">
            <% 
//if(pLop==1){
%>
            Product:
            <%= db.getProdName(products[p]) %>
            <%//}%>
        </h3>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="float:left; width:400pt;text-align:left;">
                <span style="background-color:yellow">Code: </span>
                 
                <%= code %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Name: </span><%= facilityName  %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Contact: </span><%= contact  %>
                 
                <br/>
            </div>
            <div style="float:left; width:200pt;text-align:left;">
                <span style="background-color:yellow">Type: </span>
                 
                <%= facilityType %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Phone: </span>
                 
                <%= phone %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Avg_Monthly_Cons.: </span>
                 
                <%= avg %>
                 
                <br/>
                 
                <br/>
            </div>
        </div>
        <div style="height:10pt;"></div>
        <%
i++;
%>
        <div style="clear:both"></div>
        <hr/>
        <br/>
         
        <%

} // StockOutFacility


} // if rs.next
//db.closeRs(rsAVG);
} // while Loop Facilities
%>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="float:left; width:400pt;text-align:left;">&nbsp;</div>
            <div style="float:left; width:200pt;text-align:left;">
                <%if(px==0 && count !=0){


%>
                 
                <span style="font-weight:bold">Total No. of Sites: 
                    <%= count %></span>
                 
                <%px++;}%>
            </div>
        </div>
        <%
//continue;

%>
         
        <%

//is=0;
%>
         
        <%
}
%>
         
        <%
// --------------> All Facilities
%>
         
        <%
if(type.equals("0")){ // All
%>
         
        <%
int pLop = 0;
String productss = "";
        for(int ip=0; ip<products.length; ip++){
            if(ip==0){
                productss = products[ip];
            }else{
            productss += ", "+products[ip];
            }
        }

int rloop = 0;


%>
         
        <%
int i=0;
int count = 0;
int is=0;
int px = 0;
for(int p=0; p<products.length; p++){
//px++;
px=0;
is=0;


StockOutFacility sofc = new StockOutFacility();
List<Map<String, Object>> facilityList = sofc.AllFacilities(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           null);

 count = 0;
 %>
         
        <%
  pLop = 0;

for(Map<String, Object> row : facilityList){
String facilityId = row.get("FACILITY_ID").toString();

pLop++;




List<Map<String, Object>> StockOutFacilityList = sofc.GetStockOutFacility(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           facilityId);
for(Map<String, Object> stockOutFacility : StockOutFacilityList){                                                           
if(stockOutFacility.get("ADJ_TYPE_ID").toString().equals("12") 
|| stockOutFacility.get("ADJ_TYPE_ID").toString().equals("17") 
|| stockOutFacility.get("ADJ_TYPE_ID").toString().equals("19") 
) 
continue;

 double avg = Double.valueOf(stockOutFacility.get("AVG").toString());
 double closeBal = Double.valueOf(stockOutFacility.get("CLOSE_BAL").toString());
 double adjustments = Double.valueOf(stockOutFacility.get("ADJUSTMENTS").toString());
 double adjType = Double.valueOf(stockOutFacility.get("ADJ_TYPE_ID").toString());
boolean condition = false;

if((avg > 0.0 && closeBal == 0.0))
condition = true;



if(!condition ) {




continue;


}

count ++;

String code = row.get("Code") + "";
String facilityName = row.get("FacilityName") + "";
String contact = row.get("Contact") + "";
String facilityType = row.get("FacilityType") + "";
String phone = row.get("Phone") + "";

System.out.println(" &&&&&&&&&&&&&&&&&&&& CODE IS : " +  code + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& name IS : " + facilityName + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& contact IS : " + contact + " &&&&&&&&&&&&&&&&&&&& ");
System.out.println(" &&&&&&&&&&&&&&&&&&&& facilityType IS : " + facilityType + " &&&&&&&&&&&&&&&&&&&&");
System.out.println(" &&&&&&&&&&&&&&&&&&&& phone IS : " + phone + " &&&&&&&&&&&&&&&&&&&&");

%>
         
        <% if(rloop % 5 ==0 && rloop!=0){%>
        <div class="ba"></div>
        <br/><br/>
         
        <br/><br/>
         
        <%}rloop++;
 //out.println(rloop+" ddddddddddddd");
 %>
        <div align="left" style="text-align:left;clear:both;font-weight:bold">
            Supplier: 
            <%= db.getSupplierName(facilityId) %>
             &nbsp; &nbsp; 
            <span style="color:pink">
                <%= db.getSupplierCode(facilityId) %></span>
        </div>
        <h3 style="text-align:left; clear:both;">
            <%// if(pLop==1){%>
            Product:
            <%= db.getProdName(products[p]) %>
            <%//}%>
        </h3>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="float:left; width:400pt;text-align:left;">
                <span style="background-color:yellow">Code: </span>
                 
                <%= code %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Name: </span><%= facilityName  %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Contact: </span><%= contact  %>
                 
                <br/>
            </div>
            <div style="float:left; width:200pt;text-align:left;">
                <span style="background-color:yellow">Type: </span>
                 
                <%= facilityType %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Phone: </span>
                 
                <%= phone %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Avg_Monthly_Cons.: </span>
                 
                <%= avg %>
                 
                <br/>
                 
                <br/>
            </div>
        </div>
        <div style="height:10pt;"></div>
        <%
i++;
%>
        <div style="clear:both"></div>
        <hr/>
        <br/>
         
        <%

} // StockOutFacility


} // if rs.next
//db.closeRs(rsAVG);
} // while Loop Products
%>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="float:left; width:400pt;text-align:left;">&nbsp;</div>
            <div style="float:left; width:200pt;text-align:left;">
                <%if(px==0 && count !=0){


%>
                 
                <span style="font-weight:bold">Total No. of Sites: 
                    <%= count %></span>
                 
                <%px++;}%>
            </div>
        </div>
        <%
//continue;

%>
         
        <%

//is=0;
%>
         
        <%
}

%>
         
        <%
db.s();
%>
    </body>
</html>