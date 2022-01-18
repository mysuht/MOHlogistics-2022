package ui;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.adf.share.ADFContext;

public class excelServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1256";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**Process the HTTP doGet request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String excelFile = request.getParameter("fileName");
        //excelFile = "C:/graphs/"+excelFile+".xls";
       // excelFile = "/home/oracle/"+excelFile;
     //  excelFile = excelFile;
       
       
       
       // String excelFile = ADFContext.getCurrent().getSessionScope().get("downloadedFile") + "";
        System.out.println("downloaded file is "+excelFile);
        ServletContext ctx = getServletContext();
        String fileName = "downloadedFile.xls";
                      InputStream fis = new FileInputStream(excelFile);
                  //  String mimeType = ctx.getMimeType(file.getAbsolutePath());
                    // response.setContentType(mimeType != null? mimeType:"application/octet-stream");
                   // response.setContentType("application/octet-stream");
                   response.setContentType("image/png");
       
                   //  response.setContentLength((int) file.length());
                    //  response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
       
        
                    String pathToWeb = getServletContext().getRealPath(File.separator);
                                    File f = new File(excelFile);
                                    BufferedImage bi = ImageIO.read(f);
                                    OutputStream out = response.getOutputStream();
                                    ImageIO.write(bi, "png", out);
                                    out.close();
        
        
        
      //  response.setContentType("application/vnd.ms-excel");
      //  response.setHeader("Content-Disposition","attachment; filename=" + "MyReport.xls" );
      //PrintWriter out = response.getWriter();
   
   
   
   
   
   
       
//        ServletOutputStream os       = response.getOutputStream();
//                      byte[] bufferData = new byte[1024];
//                      int read=0;
//                     while((read = fis.read(bufferData))!= -1){
//                         os.write(bufferData, 0, read);
//                     }
//                   os.flush();
//                   os.close();
//                    fis.close();
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                     System.out.println("File downloaded at client successfully");
       
       // out.close();
    }
}
