package moh.logistics.lib.reports;

import java.io.IOException;
import java.io.InputStream;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import javax.naming.InitialContext;

import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public interface MainInterface {


    public static final String datasource = "java:comp/env/jdbc/LOGConnDS";
    public static final String datasourceName = "LOGConnDS";

    public static final String datasourceURL = "java:comp/env/jdbc/";
    public static final String datasourceLookup = datasourceURL + datasourceName;
    public static final String graphPath = "D:";
    //public static final String graphPath = "/home/oracle";
    public static final String tmpFolder = "logistics_graphs";
    // String drive = "c:"; ///home/oracle
    //  String drive = "/home/oracle";
    public static final String ds = URLInfo.getLOGConfigFile().getProperty("LOG.DS");
    public static final String username = URLInfo.getLOGConfigFile().getProperty("jdbc.username");
    public static final String password = URLInfo.getLOGConfigFile().getProperty("jdbc.password");
    public static final String url = URLInfo.getLOGConfigFile().getProperty("browser.url");
    public static final String thinConn = URLInfo.getLOGConfigFile().getProperty("jdbc.thinConn");


    public static final String amDef = URLInfo.getLOGConfigFile().getProperty("amDef");
    public static final String config = URLInfo.getLOGConfigFile().getProperty("amConfig");

    class URLInfo {


        public static Properties getLOGConfigFile() {
            Properties prop = new Properties();
            InputStream input = null;
            input = URLInfo.class.getClassLoader().getResourceAsStream("database.properties");
            try {
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prop;
        }

        public static int port() {
            InitialContext ctx;
            int port = 0;
            try {
                ctx = new InitialContext();
                MBeanServer mBeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
                ObjectName rt;
                rt =
                    (ObjectName) mBeanServer.getAttribute(new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean"),
                                                          "ServerRuntime");
                String listenAddress = (String) mBeanServer.getAttribute(rt, "ListenAddress");
                String server = listenAddress.substring(0, listenAddress.indexOf("/"));
                port = (Integer) mBeanServer.getAttribute(rt, "ListenPort");


                FacesContext fctx = FacesContext.getCurrentInstance();
                //String taskflowURL = fctx.getExternalContext().getRequestContextPath() + ProxyPage.PROXY_URL + label;
                //String taskflowURL = "http://www.google.com";
                ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
                StringBuilder script = new StringBuilder();
                script.append("window.open(\"" + url + "/log/excelservlet?fileName=" +
                              (ADFContext.getCurrent().getSessionScope().get("downloadedFile") + "") + "\");");
                erks.addScript(fctx, script.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return port;
        }


        public static String address() {
            InetAddress ip;
            String address = null;
            String hostname;

            try {
                ip = InetAddress.getLocalHost();
                address = InetAddress.getLocalHost().getHostAddress();
                hostname = ip.getHostName();
                System.out.println(" xxxxxxxxxxxx ADDRESS XXXXXXXXXXX " + address);
            } catch (UnknownHostException e) {

            }


            return address;


        }

    }


}
