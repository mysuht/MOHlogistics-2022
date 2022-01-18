package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.GregorianCalendar; 
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import javax.naming.InitialContext;


import moh.logistics.lib.common.UIDSConnection;

import moh.logistics.lib.reports.MainInterface;

import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.graph.Background; 
import oracle.adf.view.faces.bi.component.graph.UIGraph; 
import oracle.dss.dataView.ImageView;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public  class DvtContextCallBack implements ContextCallback,MainInterface {
    String drive = MainInterface.graphPath;
     String tmpFolder = MainInterface.tmpFolder;
    
    public DvtContextCallBack() {
        super();
    }
    
    
    /////////////////print to image//////////////////////
    
    
    
    //method called on each component that matches the search criteria 
    public void invokeContextCallback(FacesContext facesContext, UIComponent uiComponent) {
        String cName = ADFContext.getCurrent().getSessionScope().get("") + "";
        String dname = ADFContext.getCurrent().getSessionScope().get("") + "";
            
        //PRINT //Only care for instances of UIGraph 
        if (uiComponent instanceof UIGraph) { 
            UIGraph dvtgraph = (UIGraph)uiComponent; 
            System.out.println("hiiiiiiiiiiiiiiiiiiiiiii");
        //We can set a different background color. However,    
        //this changes the graph instance and thus we need to //copy and set back the current values
        Background orgBackground = dvtgraph.getBackground();
        //create and set a new background
        Background bg = new Background(); //backgrounds can not only be set to colors but also transparent 
                                          ////with graduated fill
       // bg.setFillColor(Color.WHITE); //explicitly set transparent fill to false for a white //background
    // bg.setFillTransparent(false); 
   // dvtgraph.setBackground(bg); 
        ////this needs to be called to ensure the background color is set. dvtgraph.transferProperties();                                     
        dvtgraph.transferProperties();
        //image view is what we want to export. Also, this is where we //set the exported image size
        ImageView imgView = dvtgraph.getImageView(); 
        //We can set a different image size. However, this changes the //graph instance and thus best is to copy the current
        //values to //set them back after the image is processed 
        Dimension orgDimension = imgView.getImageSize();
        //width, height 
        imgView.setImageSize(new Dimension(800,500)); 
        //Get the OS specific file path separator 
        String slash = File.separator;
        String dSlash = slash + slash; 
        
    //create a unique file name (you may want to change generating the 
    ////file name using a real random so that concurrent access to the
    ////application don't conflict if they are processed just in the 
    ////same fraction of a second
     //   File file1 = new File("C:\\graphs"); // /home/oracle/graphs
        File file1 = new File(drive+"\\"+tmpFolder);
            
                    if (!file1.exists()) {
                            if (file1.mkdir()) {
                                    System.out.println("Directory is created!");
                            } else {
                                    System.out.println("Failed to create directory!");
                            }
                    }
    String filename = GregorianCalendar.getInstance().getTimeInMillis() + "dvt";
 // String drive = "c:"; ///home/oracle
 
    File file = null; FileOutputStream fos;
   
    
   
  //try { file = new File(drive + dSlash + tmpFolder + slash + filename + (ADFContext.getCurrent().getSessionScope().get("ffname")+"")+ ".png");
   try { file = new File("D:/logistics_graphs/"+filename + (ADFContext.getCurrent().getSessionScope().get("ffname")+"")+ ".png");
 
        
        fos = new FileOutputStream(file);
    imgView.exportToPNG(fos);
        fos.close();
       
            ADFContext.getCurrent().getSessionScope().put("downloadedFile",file);
       
//            //Creates a writable workbook with the given file name
//         //       WritableWorkbook workbook = Workbook.createWorkbook(new File("C:/graphs/AddImage.xls"));
//         WritableWorkbook workbook = Workbook.createWorkbook(new File("/home/oracle/AddImage.xls"));
//       // String excelFile = "C:/graphs/AddImage.xls";
//       String excelFile = "/home/oracle/AddImage.xls";
//            String excelFile1 = "AddImage";
//            
//            ADFContext.getCurrent().getSessionScope().put("downloadedFile",excelFile1);
//                
//                WritableSheet sheet = workbook.createSheet("My Sheet", 0);
//                
//                WritableImage image = new WritableImage(
//                    1, 1,   //column, row
//                    18, 22,   //width, height in terms of number of cells
//                   // new File("C:/JXL/google.png")); //Supports only 'png' images
//                   new File(drive + dSlash + tmpFolder + slash + filename + ".png")); //Supports only 'png' images
//               
//                sheet.addImage(image);
//                
//                //Writes out the data held in this workbook in Excel format
//                workbook.write(); 
//                
//                //Close and free allocated memory 
//                workbook.close();  
//       
        } 
    catch (FileNotFoundException e) { 
        //For sample - just show stack trace    
        e.printStackTrace();
        } catch (IOException e) {
    e.printStackTrace(); 
    } 
//    catch (WriteException e) {
//        
//            } 
    finally{ 
        //reset the graph default � IMPORTANT ! � as otherwise the 
        ////web instance of the graph would change with the next refresh 
        dvtgraph.setBackground(orgBackground);
        dvtgraph.transferProperties(); 
        imgView.setImageSize(orgDimension);
        }
        }
        
        
        
        InitialContext ctx;
        try {
           ctx = new InitialContext();
           MBeanServer mBeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
           ObjectName rt;
           rt =
               (ObjectName) mBeanServer.getAttribute(new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean"),
                                                    "ServerRuntime");
           String listenAddress = (String) mBeanServer.getAttribute(rt, "ListenAddress");
           String server = listenAddress.substring(0, listenAddress.indexOf("/"));
           int port = (Integer) mBeanServer.getAttribute(rt, "ListenPort");

            
            
            
                            FacesContext fctx = FacesContext.getCurrentInstance();
                    //String taskflowURL = fctx.getExternalContext().getRequestContextPath() + ProxyPage.PROXY_URL + label;
                    //String taskflowURL = "http://www.google.com";
                    ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
                    StringBuilder script = new StringBuilder();
                    script.append("window.open(\"" + url + "/logistics/excelservlet?fileName="+(ADFContext.getCurrent().getSessionScope().get("downloadedFile")+"") + "\");");
                    erks.addScript(fctx, script.toString());
            
//            ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
//            HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
//        HttpServletResponse request = (HttpServletResponse) ectx.getRequest();
        
//            try {
//                response.sendRedirect(url + ":" + port + "/disReports/excelservlet");
//            } catch (IOException e) {
//            }
//            
        }catch(Exception e){
            e.printStackTrace();
        }
      

        
        
        
       
    }
}
