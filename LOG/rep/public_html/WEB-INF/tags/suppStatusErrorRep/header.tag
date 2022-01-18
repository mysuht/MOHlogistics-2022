<%@ tag pageEncoding="windows-1256"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ tag import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
        <style type="text/css">
            </style>
    </head>
    <body>
        <%
    LogisticsReportsClass db = new LogisticsReportsClass();
    String [] Directorates = request.getParameterValues("p_cen");
    String dat = request.getParameter("dat");
    String mon = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
String hq = "";
String type = request.getParameter("p_dir");
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
    String prod[] = request.getParameterValues("p_prod");
    DecimalFormat df1 = new DecimalFormat("###,###.###");
    DecimalFormat dfmos = new DecimalFormat("###.#");
    
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Family Planning Program
            </div>
            <div class="dCenter">
                <%
     String er =request.getParameter("rep");
     if(er.equals("9")){
     %>
                 Supply Status Report 
                <br/>
                 
                <%}if(er.equals("8")){%>
                 Supply Status Error Report 
                <br/>
                 
                <%}%>
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
    out.println("All Facility Types."+"<br/>");
    } if(!type.equals("0") && Integer.parseInt(type) < 100 ){
    out.println(rs12.getString(2));
    }if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
            </div>
            <div class="dRight" style="">
                 Run Date: 
                <%=  db.getDate() %>
                 
                <br/>
                 Run Time: 
                <%= db.getTime() %>
            </div>
        </div>
        <br/><br/><br/><br/>
    </body>
</html>