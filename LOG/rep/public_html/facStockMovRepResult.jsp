<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*, java.util.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
          <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
    <%
    String type = request.getParameter("p_dir");
    String cen[] = request.getParameterValues("p_cen");
    String dat = request.getParameter("dat");
    String mon ="";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";

    if(dat.equals("m")){
    mon=request.getParameter("p_mon");
    } if(dat.equals("q")){
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
    
    DecimalFormat df = new DecimalFormat("###,###.#");
    DecimalFormat df1 = new DecimalFormat("###,###.#");
    double closeBal = 0;
int maxStock = 0;
double avg = 0;
System.out.println("*******111111111111******");
    %>

 <div class="main_div" >
    <div class="dLeft">
    Jordan Contraceptive Logistics System <br/>
    MOH Familiy Planning Program
    </div>
     <div class="dCenter">
    Facility Stock Movement Report <br/>
     
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
    ResultSet rs = db.getFacTypesMainName(type);
    rs.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    out.println(rs.getString(2));
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
    
    
    </div>
     <div class="dRight">
    Run Date: <%=  db.getDate() %> <br/>
    Run Time: <%= db.getTime() %>
    </div>
    </div>
    <br/><br/><br/><br/>
    
    <% if(!type.equals("0")){ //not_all
    for(int d = 0; d < cen.length;d++){
    if(cen[d].equals("498"))
    continue;
    ResultSet rsPrdStocMov = db.getMovStockRep(cen[d], mon, year,dat,fY,tY, fM, tM,type, hq);    
    System.out.println("*******33333333333******");
//    ResultSet rsDirSup = db.getDireDispensed(cen[d]);
//    rsDirSup.next();
    %>
    <% if(d!=0){ %>
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
<%}%>
    <div style="clear:both">
   <div style="float:left;width:200pt;">
   Supplying facility: <%= db.getDirInfo(db.getDirSupInfo(cen[d],4),2)%>    </div>
   <div style="float:left; width:400pt; text-align:right">
   Suppling Code: <%= db.getDirInfo(db.getDirSupInfo(cen[d],4),3) %>
   </div>
<div style="width:200pt;clear:both;float:left">
<%= db.getDirInfo(cen[d],2)%>
</div>
<div style="float:left; width:200pt; text-align:right">
    <%= db.getDirType(cen[d]) %>
   </div>  
   <div style="float:left; width:200pt; text-align:right">
  Facility Code:  <%= db.getDirInfo(cen[d],3) %>
   </div>  
    </div>
    

<table border="1" cellpadding="2" cellspacing="3" style="clear:both" >
<thead>
<tr class="col_titles">
<th align="center" colspan="2">
Commodities
</th>
<th align="center">
Opening Bal.
</th>
<th align="center">
Reciepts
</th>
<th align="center">
Issues
</th>
<th align="center">
Adjustments
</th>
<th align="center">
Type
</th>

<th align="center">
Closing Bal.
</th>

<th align="center">
Avg. Monthly Cons.
</th>
<th align="center">
Current MOS.
</th>
<th align="center">
Maximum Stock
</th>
<th align="center">
Re-order Amount
</th>
</tr>
</thead>
<%
double mstock = 0;
while(rsPrdStocMov.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rsPrdStocMov.getString(1) %>
</td>
<td align="center">
<%= rsPrdStocMov.getString(2) %>
</td>
<td align="center">
<%= df.format( rsPrdStocMov.getDouble(3) ) %>
</td>
<td align="center">
<%= df.format( rsPrdStocMov.getDouble(4) ) %>
</td>
<td align="center">
<%= df.format( rsPrdStocMov.getDouble(5)) %>
</td>
<td align="center">
<%= rsPrdStocMov.getString(6) %>
</td>
<td align="center">
<%
System.out.println("**************tttttttttttttttttt************");
%>
<%= rsPrdStocMov.getString(7) %>
</td>
<td align="center">
<%= df.format( rsPrdStocMov.getDouble(8) ) %>
</td>
<td align="center">
<% 
System.out.println("**************444444444444444444444444444************");
//rsPrdStocMov.getString(9) 

System.out.println("**************5555555555555555555555555************");
//if(!cen[d].equals("525")){
avg = db.getAVGnonRepFac1(mon, year, cen[d], rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq);
out.println(df.format(avg) );
//}
//if(cen[d].equals("525")){
//avg = db.getAVGnonRepFac( Integer.parseInt(mon), year, cen[d], rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq,rsPrdStocMov.getString(12));
//out.println(df.format(avg));
//}
System.out.println("**************6666666666666666666666666************");
closeBal = rsPrdStocMov.getDouble(8);
%>


</td>
<td align="center">
<%
if(avg == 0 && rsPrdStocMov.getDouble(8) == 0){
out.println(0);
}else{
out.println( avg == 0 && rsPrdStocMov.getDouble(8) > 0 ? 99.9 : df.format(rsPrdStocMov.getDouble(8) / avg)); 
}
%>
</td>
<td align="center">
<%= df.format((avg * 6)) %>
<%
mstock = (avg * 6);
%>
</td>

<td align="center">
<% 
//out.println( ((mstock -  rsPrdStocMov.getDouble(8)) / (db.getProdQty(rsPrdStocMov.getInt(1)) ) ) * db.getProdQty(rsPrdStocMov.getInt(1)) + db.getProdQty(rsPrdStocMov.getInt(1))) ;
if(Math.floor ((mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + db.getProdQty(rsPrdStocMov.getString(12)) < 0 )
{ out.println("0");
}else{
if(rsPrdStocMov.getDouble(8) == 0.0 && avg == 0 ){
out.println("0");
}else{
double constt = db.getProdQty(rsPrdStocMov.getString(12)) == 1 ? 0 : db.getProdQty(rsPrdStocMov.getString(12));


if( ( ((mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) )  ) % 2 == 0 ){
out.println(df.format((mstock - rsPrdStocMov.getDouble(8))));
}else{
out.println( df.format(  Math.floor ((mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + constt  ) );
}


}
}
//out.println( df.format( Math.floor( ((23064)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + db.getProdQty(rsPrdStocMov.getString(12))) );
%>



</td>
</tr>

<%

}
%>
<%
%>

</table>
<h5>Comments:<br/> <%= db.getCommintCtfMain(cen[d], mon, year, dat, fY, tY, fM, tM, type, hq) %></h5>
<br/><br/><br/>






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

<%
}
}// -------------------------------------
%>

  <% if(type.equals("0")){
  ResultSet rsDir = db.getDirectorates();
  
  while(rsDir.next()){
    ResultSet rsPrdStocMov = db.getMovStockRep(rsDir.getString(1), mon, year,dat,fY,tY, fM, tM,type, hq);    
    System.out.println("*******33333333333******");
//    ResultSet rsDirSup = db.getDireDispensed(cen[d]);
//    rsDirSup.next();
if(rsDir.getString(1).equals("498"))
continue;
    %>
    
    <div style="clear:both; width:100%;font-weight:bold;">
   <div style="float:left;width:500pt;">
   Supplying facility: <%= db.getDirInfo(db.getDirSupInfo(rsDir.getString(1),4),2)%> &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; Suppling Code: <%= db.getDirInfo(db.getDirSupInfo(rsDir.getString(1),4),3) %>    </div>
   <div style="float:left; width:320pt; text-align:right">
   
   </div>
<div style="width:500pt;clear:both;float:left">
<%= db.getDirInfo(rsDir.getString(1),2)%>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Facility Code:  <%= db.getDirInfo(rsDir.getString(1),3) %>
</div>

   <div style="float:left; width:200pt; text-align:right">
  
   </div>  
    </div>
    <br/><br/>
    <h5 align="center">
    Maximum Months On Hand:6 Months &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Minimum Months On Hand:1 Month
    </h5>
<table border="1" cellpadding="2" cellspacing="3" style="clear:both" width="100%" >
<thead>
<tr class="col_titles">
<th align="center" colspan="2">
Commodities
</th>
<th align="center">
Opening Bal.
</th>
<th align="center">
Reciepts
</th>
<th align="center">
Issues
</th>
<th align="center">
Adjustments
</th>
<th align="center">
Type
</th>

<th align="center">
Closing Bal.
</th>

<th align="center">
Avg. Monthly Cons.
</th>
<th align="center">
Current MOS.
</th>
<th align="center">
Maximum Stock
</th>
<th align="center">
Re-order Amount
</th>
</tr>
</thead>
<%
double mstock = 0;
while(rsPrdStocMov.next()){
%>
<tr class="row_titles">
<td align="center">
<%= rsPrdStocMov.getString(1) %>
</td>
<td align="center">
<%= rsPrdStocMov.getString(2) %>
</td>
<td align="center">
<%= df1.format(rsPrdStocMov.getDouble(3)) %>
</td>
<td align="center">
<%= df1.format(rsPrdStocMov.getDouble(4)) %>
</td>
<td align="center">
<%= df1.format(rsPrdStocMov.getDouble(5)) %>
</td>
<td align="center">
<%= df1.format(rsPrdStocMov.getDouble(6)) %>
</td>
<td align="center">
<%
System.out.println("**************tttttttttttttttttt************");
%>
<%= rsPrdStocMov.getString(7) %>
</td>
<td align="center">
<%= df1.format(rsPrdStocMov.getDouble(8)) %>
</td>
<td align="center">
<% 
System.out.println("**************444444444444444444444444444************");
//rsPrdStocMov.getString(9) 
out.println(df1.format((db.getAVGnonRepFac(Integer.parseInt(mon), year, rsDir.getString(1), rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq, rsPrdStocMov.getString(12) ))));
System.out.println("**************5555555555555555555555555************");
avg = db.getAVGnonRepFac(Integer.parseInt(mon), year, rsDir.getString(1), rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq, rsPrdStocMov.getString(12));
//if(!rsDir.getString(1).equals("525")){
//avg = db.getAVGnonRepFac1(mon, year, rsDir.getString(1), rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq);
//}
//if(rsDir.getString(1).equals("525")){
//avg = db.getAVGnonRepFac( Integer.parseInt(mon), year, rsDir.getString(1), rsPrdStocMov.getString(12),dat,fY,tY, fM, tM,type, hq,rsPrdStocMov.getString(12));
//}
System.out.println("**************6666666666666666666666666************");
closeBal = rsPrdStocMov.getDouble(8);
%>


</td>
<td align="center">
<%
if(avg == 0 && rsPrdStocMov.getDouble(8) > 0.0){
out.println(99.9);
}else if(avg == 0 && rsPrdStocMov.getDouble(8) == 0.0){
out.println(0);
}


else{
out.println(df.format(rsPrdStocMov.getDouble(8) / avg)); 
}
%>
</td>
<td align="center">
<%= df.format((avg * 6)) %>
<%
mstock = (avg * 6);
%>
</td>

<td align="center">
<% 
if(avg == 0.0 || rsPrdStocMov.getString(8).equals("0")){
out.println(0);
}else{
//out.println( ((mstock -  rsPrdStocMov.getDouble(8)) / (db.getProdQty(rsPrdStocMov.getInt(1)) ) ) * db.getProdQty(rsPrdStocMov.getInt(1)) + db.getProdQty(rsPrdStocMov.getInt(1))) ;
if((mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) < 0){
out.println("0");
}else{
out.println( Math.floor ((mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + db.getProdQty(rsPrdStocMov.getString(12)) < 0 ? 0 : df.format( Math.floor (  (mstock - rsPrdStocMov.getDouble(8)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + db.getProdQty(rsPrdStocMov.getString(12))) );

}
}
//out.println( df.format( Math.floor( ((23064)) / db.getProdQty(rsPrdStocMov.getString(12)) ) * db.getProdQty(rsPrdStocMov.getString(12)) + db.getProdQty(rsPrdStocMov.getString(12))) );
%>



</td>
</tr>

<%

}
%>
<%
%>

</table>
<h5>Comments:<br/> <%= db.getCommintCtfMain(rsDir.getString(1), mon, year, dat, fY, tY, fM, tM, type, hq) %></h5>
<br/><br/><br/>
<div class="ba" > </div>


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


<%
}
}
%>


<%
db.s();
%>

</body>
</html>
