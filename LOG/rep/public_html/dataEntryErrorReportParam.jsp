<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, java.sql.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
        <script src="resources/js/logJS.js" type="text/javascript"></script>
          <link href="resources/css/multiple-select.css" rel="stylesheet" type="text/css" />
          <script src="resources/js/jquery.multiple.select.js" type="text/javascript"></script>
        <script type="text/javascript">
        $(document).ready(function(){
        // ---------------------------
        
         if( $('input[name=dat]:checked').val() == "m" ){
            //  alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                  
              }
           
           
           
           
              //alert("hiiiiiii") ;
              $('input:radio').change(
              function(){
             if( $('input[name=dat]:checked').val() == "m" ){
             // alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                  
              }
              
              });
        
        // ---------------------------
       // window.location.replace("dataEntryErrorReportParam.jsp?pd=");
        $('#p_dir option[value='+$("#pd").val()+']').attr('selected','selected');
        
        var dirO = $("#p_dir").val();
        var dirOld = $("#pd").val();
        $("#p_dir").change(function(){
        $("#pd").val($("#p_dir").val());
       // $("#f2").submit();
       window.location.href="dataEntryErrorReportParam.jsp?pd="+$("#pd").val();
       $('#p_dir option[value='+$("#pd").val()+']').attr('selected','selected');
        // *******************
        var dir = $("#p_dir").val();
        var name = [];
        var dir1 = [];
      //  var n = $
          $.ajax( {
                      url : 'dataentryservlet', type : 'post', 

                      //data:{n2:n2,n3:n3,n1:n1,name2:name2,name3:name3,name4:name4},
                      data :  {
                          dir:dir, dir1:dir1, name:name
                      },
                    traditional : true
                    ,
                      // data: form.serialize(),
                      dataType : 'json', 
                      success : function (data) {
                    var obj = jQuery.parseJSON( data.dir1  );  
                  //  alert("the value of Directorate is "+$("#p_dir").val());
                    
    //  alert("sdfsdf");
         var obj1 = jQuery.parseJSON(JSON.stringify(data.name));
      //   alert("sssssssssssss"+ obj1[1]);
                      }, 
                       error : function (request, error) {
                       
                       }
                       
          });
        
        // ******************
        
      //  $("#p_cen").submit();
        // $("#p_dir").val() = dirOld;
        //$("#p_cen").append('<option >'+ $("#p_dir").val() +'</option>');
        });
        
      
        
        });
        
        </script>
    </head>
    <body>
    <form id="f1" action="dataEntryErrorReportResult.jsp" method="POST">
    <%
    LogisticsReportsClass db = new LogisticsReportsClass();
    ResultSet rs = db.getDirectorates();
    String dir = request.getParameter("pd")==null?" ":request.getParameter("pd");
    %>
    <div>
Directorate <select name="p_dir" id="p_dir" >
<option></option>
<% while(rs.next()){ %>

<%
if(dir.equals(rs.getString(1))){
%>

<option value="<%= rs.getString(1) %>" selected="selected" > <%= rs.getString(2) %> </option>
<%}else{%>
<option value="<%= rs.getString(1) %>"  > <%= rs.getString(2) %> </option>
<%}%>
<%}%>

</select>

Centers: <select name="p_cen" id="p_cen" multiple="multiple">
<%
ResultSet rsCnt = db.getDireDispensed(dir);
while(rsCnt.next()){
%>
<option value="<%= rsCnt.getString(1) %>" > <%= rsCnt.getString(2) %> </option>
<%}%>
</select>
<script src="resources/js/jquery.multiple.select.js"></script>
    <script type="text/javascript" >
      $('#p_cen').multipleSelect();
    </script>
<input type="hidden" name="pd" id="pd" />
<input type="submit" value="   View   "  />
 </div>
 <div>
    <div style="width:200pt;float:left;">
    Year: &nbsp; <select name="p_year" class="sel_beh">
    <%
   for(int i=1997; i<=2014; i++){
    %>
    <option value="<%= i %>" > <%= i %> </option>
    
    <%}%>
    </select>
    
   
    </div>
    <div style="width:150pt;float:left" > 
     Month: <input type="radio" value="m" name="dat" checked="checked" id="dat" /> &nbsp; &nbsp;
     Quarter: <input type="radio" value="q" name="dat" id="dat" />
     </div>
    <div style="width:200pt;float:left" id="mon">
    Month: &nbsp; <select name="p_mon" class="sel_beh">
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
     <div style="width:200pt;float:left" id="quart">
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
    </div>

</form>
</body>
</html>