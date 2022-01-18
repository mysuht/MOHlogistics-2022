<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%
    String []cen;
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String hq = "";
    String type = request.getParameter("p_dir");
    String dat = request.getParameter("dat");
    String mon = "";
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
        // moh.logistics.lib.reports.code.LogisticsReportsClass.DBClass D = new moh.logistics.lib.reports.code.LogisticsReportsClass.DBClass ();
   
       LogisticsReportsClass db = new LogisticsReportsClass();
       ResultSet rs = db.getNonRepFac(mon, year,dat,fY,tY,fM,tM,type,hq);
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Familiy Planning Program
            </div>
            <div class="dCenter">
                Non Reporting Facility Report 
                <br/>
                 Program: Contraceptives. 
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
    }if(!type.equals("0") && Integer.parseInt(type) <100){
    out.println(rs12.getString(2));
    }
    if(!type.equals("0") && Integer.parseInt(type) > 100){
    out.println(db.getGroupName((Integer.parseInt(type)-100)+""));
    }
    %>
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
if(type.equals("0") ) {
cen = request.getParameterValues("p_cen");
//if( db.getDirType(type).equals("7")){

int i=0;
rs = db.getNonRepFac(mon, year,dat,fY,tY,fM,tM,type,hq);
while(rs.next()){
if (i != 0){

%>
         
        <%}

if(i%6 == 0 && i!=0){



%>
        <div class="ba"></div>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="clear:both; font-weight:bold; text-align: center;" align="center">
                <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                     &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                     &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
            </div>
            <%
i=0;}else{%>
            <div style=" width:600pt;clear:both; margin:10pt;">
                <%}%>
                <div style="float:left; width:400pt;text-align:left;">
                    <%= 
db.getNonRepFacInfo(rs.getString(1), 1)  
%>
                     
                    <br/>
                     
                    <br/>
                     
                    <br/>
                     
                    <%= 
db.getNonRepFacInfo(rs.getString(1),7)  %>
                     &nbsp; &nbsp; &nbsp; 
                    <%= db.getNonRepFacInfo(rs.getString(1),8)  %>
                     
                    <br/>
                     &nbsp; 
                    <span style="background-color:yellow"> CEP:</span>
                </div>
                <div style="float:left; width:200pt;text-align:left;">
                    <span style="background-color:yellow">Code: </span>
                     
                    <%= db.getNonRepFacInfo(rs.getString(1),2) %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Type: </span>
                     
                    <%= db.getNonRepFacInfo(rs.getString(1),3) %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Contact: </span>
                     
                    <%= db.getNonRepFacInfo(rs.getString(1),4) %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Phone: </span>
                     
                    <%= db.getNonRepFacInfo(rs.getString(1),5) %>
                     
                    <br/>
                     
                    <span style="background-color:yellow"> Fax: </span>
                     
                    <%= db.getNonRepFacInfo(rs.getString(1),6) %>
                     
                    <br/>
                     
                    <br/>
                     
                    <br/>
                </div>
            </div>
            <div style="clear:both">
                <hr/>
            </div>
            <%
i++;
%>
             
            <%
} // end while


System.out.println("Directorates");

//}else{ ////////////////////////////////////////
/////////////////////////nested
%>
             
            <%
//} ////////////////////////// end nested






}
%>
             
            <%
if(!type.equals("0") && type.equals("7")){
System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiii heare");
cen = request.getParameterValues("p_cen");
for(int kl = 0; kl < cen.length; kl++){

int i=0;
int l = 0;
rs = db.getNonRepFacType(mon, year,dat,fY,tY,fM,tM,type,hq,cen[kl]);
while(rs.next()){
if (l>0) break;
if(i == 7){
%>
            <div style=" width:600pt;clear:both; margin:10pt;" class="ba">
                <%
i=0;}else{%>
                <div style=" width:600pt;clear:both; margin:10pt;">
                    <%}%>
                    <div style="float:left; width:400pt;text-align:left;">
                        <%= db.getNonRepFacInfo(rs.getString(1), 1)  %>
                         
                        <br/>
                         
                        <br/>
                         
                        <br/>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),7)  %>
                         &nbsp; &nbsp; &nbsp; 
                        <%= db.getNonRepFacInfo(rs.getString(1),8)  %>
                         
                        <br/>
                         &nbsp; 
                        <span style="background-color:yellow"> CEP:</span>
                    </div>
                    <div style="float:left; width:200pt;text-align:left;">
                        <span style="background-color:yellow">Code: </span>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),2) %>
                         
                        <br/>
                         
                        <span style="background-color:yellow"> Type: </span>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),3) %>
                         
                        <br/>
                         
                        <span style="background-color:yellow"> Contact: </span>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),4) %>
                         
                        <br/>
                         
                        <span style="background-color:yellow"> Phone: </span>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),5) %>
                         
                        <br/>
                         
                        <span style="background-color:yellow"> Fax: </span>
                         
                        <%= db.getNonRepFacInfo(rs.getString(1),6) %>
                         
                        <br/>
                         
                        <br/>
                         
                        <br/>
                    </div>
                </div>
                <div style="clear:both">
                    <hr/>
                </div>
                <%l++;%>
                 
                <!------------------------------>
                 
                <%
//for(int sp=0; sp<cen.length; sp++){



//}}
//}





%>
                 
                <!-------------------------->
                 
                <%
i++;
%>
                 
                <%
}  // end while
%>
                 
                <h3>
                    <%= db.getDirName(cen[kl]) %>
                </h3>
                 
                <%
int ii=0;
int ll = 0;
//ResultSet rs1 = db.getNonRepFacType(  mon, year,dat,fY,tY,fM,tM,type,hq, db.getFacTypesCount1(cen[kl])    );
ResultSet rs1 = db.getNonRepFacTypeChilds(  mon, year,dat,fY,tY,fM,tM,type,hq, cen[kl]    );
while(rs1.next()){
//if (ll>0) break;
if(ii %6==0 && ii!=0){
%>
                <div class="ba"></div>
                <div style=" width:600pt;clear:both; margin:10pt;">
                    <div style="clear:both; font-weight:bold; text-align: center;" align="center">
                        <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                             &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                             &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                               &nbsp; &nbsp; &nbsp; &nbsp;</div>
                    </div>
                    <%
ii=0;}else{%>
                    <div style=" width:600pt;clear:both; margin:10pt;">
                        <%}%>
                        <div style="float:left; width:400pt;text-align:left;">
                            <%= db.getNonRepFacInfo(rs1.getString(1), 1)  %>
                             
                            <br/>
                             
                            <br/>
                             
                            <br/>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),7)  %>
                             &nbsp; &nbsp; &nbsp; 
                            <%= db.getNonRepFacInfo(rs1.getString(1),8)  %>
                             
                            <br/>
                             &nbsp; 
                            <span style="background-color:yellow"> CEP:</span>
                        </div>
                        <div style="float:left; width:200pt;text-align:left;">
                            <span style="background-color:yellow">Code: </span>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),2) %>
                             
                            <br/>
                             
                            <span style="background-color:yellow"> Type: </span>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),3) %>
                             
                            <br/>
                             
                            <span style="background-color:yellow"> Contact: </span>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),4) %>
                             
                            <br/>
                             
                            <span style="background-color:yellow"> Phone: </span>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),5) %>
                             
                            <br/>
                             
                            <span style="background-color:yellow"> Fax: </span>
                             
                            <%= db.getNonRepFacInfo(rs1.getString(1),6) %>
                             
                            <br/>
                             
                            <br/>
                             
                            <br/>
                        </div>
                    </div>
                    <div style="clear:both">
                        <hr/>
                    </div>
                    <%ll++;%>
                     
                    <!------------------------------>
                     
                    <%
//for(int sp=0; sp<cen.length; sp++){



//}}
//}





%>
                     
                    <%
ii++;
}
%>
                     
                    <%






/////out.println("hiiiiiiiiiiiiiiiiiiiiiiii");


}

}


%>
                     
                    <%
if(!type.equals("0") && !type.equals("7")){

cen = request.getParameterValues("p_cen");
rs = db.getNonRepFacType(mon, year,dat,fY,tY,fM,tM,type,hq,cen);
int s=0;
while(rs.next()){
if(s %6==0 && s!= 0 ){
%>
                    <div class="ba"></div>
                    <div style=" width:600pt;clear:both; margin:10pt;">
                        <div style="clear:both; font-weight:bold; text-align: center;" align="center">
                            <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                            <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="float:left;width:200pt;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="float:left; width:400pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="width:200pt;clear:both;float:left">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                           &nbsp; &nbsp;</div>
                            <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                            <div style="float:left; width:200pt; text-align:right">&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                                                                   &nbsp; &nbsp; &nbsp; &nbsp;</div>
                        </div>
                        <%
s=0;}else{%>
                        <div style=" width:600pt;clear:both; margin:10pt;">
                            <%}%>
                            <div style="float:left; width:400pt;text-align:left;">
                                <%= db.getNonRepFacInfo(rs.getString(1), 1)  %>
                                 
                                <br/>
                                 
                                <br/>
                                 
                                <br/>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),7)  %>
                                 &nbsp; &nbsp; &nbsp; 
                                <%= db.getNonRepFacInfo(rs.getString(1),8)  %>
                                 
                                <br/>
                                 &nbsp; 
                                <span style="background-color:yellow"> CEP:</span>
                            </div>
                            <div style="float:left; width:200pt;text-align:left;">
                                <span style="background-color:yellow">Code: </span>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),2) %>
                                 
                                <br/>
                                 
                                <span style="background-color:yellow"> Type: </span>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),3) %>
                                 
                                <br/>
                                 
                                <span style="background-color:yellow"> Contact: </span>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),4) %>
                                 
                                <br/>
                                 
                                <span style="background-color:yellow"> Phone: </span>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),5) %>
                                 
                                <br/>
                                 
                                <span style="background-color:yellow"> Fax: </span>
                                 
                                <%= db.getNonRepFacInfo(rs.getString(1),6) %>
                                 
                                <br/>
                                 
                                <br/>
                                 
                                <br/>
                            </div>
                        </div>
                        <div style="clear:both">
                            <hr/>
                        </div>
                        <%
s++;
%>
                         
                        <%
}
}
%>
                         
                        <%
db.s();
%>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>