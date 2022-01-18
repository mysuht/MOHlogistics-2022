<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="com.logistics.lib.report.StockOutFacilityProductService, moh.logistics.lib.reports.code.FacilityTemplate , java.util.* , java.sql.*, java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
        <style  type="text/css" media="print">
        
    @page {
size: Portrait



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
    
    <body class="container">
    <%
    int summ =0;
    String mon = request.getParameter("p_mon");
    String year = request.getParameter("p_year");
    String dir[] = request.getParameterValues("p_cen");
    String products[] = request.getParameterValues("p_prod");
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    String quart = "";
    String dat = request.getParameter("dat");
     int qr = request.getParameter("p_quart")==null?null:Integer.parseInt(request.getParameter("p_quart"));
    if(dat.equals("q")){
   quart = request.getParameter("p_quart");
    } if(dat.equals("m")){
    quart = request.getParameter("p_mon");
    }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
  
      DecimalFormat df1 = new DecimalFormat("##0.0000");
      DecimalFormat df = new DecimalFormat("###,###.###");
      String product= "";
              for(int i=0; i<products.length; i++){
            if(i==0){
                product = StockOutFacilityProductService.getProdName(products[i]);
                }else{
            product += ", " + StockOutFacilityProductService.getProdName(products[i]);
                    }
                    }
                    String qra = "";
                    switch(qr){
                    case 1:qra = "1st Quarter ,";
                    break;
                    case 2: qra = "2nd Quarter ,";
                    break;
                    case 3: qra = "3rd Quarter ,";
                    break;
                    case 4: qra="4th Quarter";
                    break;
                    case 0: qra = "";
                    break;
                    }
    %>
    <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
   Stocked Out Facilities (Product) Report<br/>
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
   

    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
        List<FacilityTemplate> facilityTemplateList = StockOutFacilityProductService.getFacTypesMainName(type); 
    System.out.println(facilityTemplateList.size());
    FacilityTemplate facilityTemplate = facilityTemplateList.get(0);
    out.println(facilityTemplate.getFacilityName());
    }
    
    %>
  <br>
   <%= product %>
    
    
    </div>
     <div class="dRight">
    Run Date: <%=  StockOutFacilityProductService.getDate() %> <br/>
    Run Time: <%= StockOutFacilityProductService.getTime() %>
    </div>
    </div>
   <br/><br/><br/>
<br/><br/>


<%
int s = 0;
int s2 = 0;
int dirCounts = 0;
int count2nd = 0;
int count3rd = 0;
if(true){ //////////////all

%>

        
        
<%        
  

List<FacilityTemplate> directoratesList = StockOutFacilityProductService.getDirectoratesList(dir);
for(FacilityTemplate Directorate: directoratesList ) {
String directorateId = Directorate.getFacilityId()+"".toString();
String directorateName = Directorate.getFacilityName()+"".toString();
String supCode = Directorate.getSupCode()+"".toString(); 
String directorateCode = Directorate.getFacCode()+"".toString();
String directorateLVL = Directorate.getFacilityTypeHierarchyId();


List<FacilityTemplate> facilityList = 
StockOutFacilityProductService.getStockOutFacilityProducts( (quart + "") , year, dat,  fY,  tY,  fM,  tM,  hq,  products, directorateId, dir); 

if(facilityList.size() == 0) continue;

count2nd ++;
%>
<div align="left" style="text-align:left;clear:both;font-weight:bold;" class="hsup" >
 Supplier: <%= directorateName %> &nbsp; &nbsp; <span style="color:pink"><%= directorateCode %> </span></div>
<div >
 
<table border ="1" cellpading="2" cellspacing="3" >
<tr class="col_titles">
<td align="center">
Facility Name
</td>
<td align="center">
Facility Code
</td>
</tr>
<%
int i=0;

%>

<%

                                                            
                                                                                                                      
for(FacilityTemplate Facility: facilityList){     
count3rd++;
String facilityID = Facility.getFacilityId()+"".toString(); 
String facilityName = Facility.getFacilityName()+"".toString(); 
String facilityCode = Facility.getFacCode()+"".toString();
%>


 
 

<tr class="row_titles">
<td align="center">
<%= facilityName %>
</td>
<td align="center">
<%= facilityCode %>
</td>

</tr>

<%
i++;
if(facilityID.equals("498")) {
s = i;

}

%>

<%
//} // condition
} // report query
%>

</table>
<h3 style="height:10pt;" class="ba">Total Facilities In Directorate is: <%= i %> </h3>
<% summ+=i; %>
</div>
<br/><br/><br/>

<%
}// Directorates
%>



<%
s2 = summ - s;
}

%>
<br/>
<br/>
<br/>
<!-- <h3 align="left"> Total for MOH/MCH Directorate is: <%=s%> </h3> -->
<h3 align="left"> Total for Second Level is: <%=count2nd%> </h3>
<h3 align="left"> Total for Third Level is: <%=count3rd%> </h3>
<!--<h3 align="left"> Total for Whole Country is: <%=summ%> </h3> -->
<h3 align="left"> Total for Whole Country is: <%=count2nd+count3rd %> </h3>

    </body>
</html>