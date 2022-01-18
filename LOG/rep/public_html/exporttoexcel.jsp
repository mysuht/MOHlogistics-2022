<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*" %> 
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
    </head>
    <body>
    
    <%
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=" + "MyReport.xls" );
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
Iterator pit = null;
DiskFileItem dfi = null;
String line = ""; 
if(isMultipart){
    //      Create a factory for disk-based file items
    FileItemFactory factory = new DiskFileItemFactory();

    //      Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);

    //      Parse the request
    File f = null;
    dfi = null;
    List items = null;
    items = upload.parseRequest(request);
    pit = items.iterator();

 }
 if(isMultipart){
    while(pit.hasNext()){
        dfi = (DiskFileItem)pit.next();
        String name = dfi.getFieldName();
        if (name.equalsIgnoreCase("ExcelTable")){
            InputStream is = dfi.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine()) != null){
                out.println(line);
            }                                   
        }
     }
  }else{
    Enumeration params = request.getParameterNames();
    while(params.hasMoreElements()){
        String par = (String)params.nextElement();
        out.println(par+"<br>");
        out.println(request.getParameter(par));
    }
  }
%>

    
    </body>
</html>