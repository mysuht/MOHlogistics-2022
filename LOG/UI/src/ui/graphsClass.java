package ui;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import javax.el.ELContext;
import javax.sql.*;

import javax.naming.*;
import javax.el.ExpressionFactory;

import javax.el.MethodExpression;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.graph.UIGraph;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.binding.BindingContainer;

import oracle.jbo.*;
import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import java.awt.Color; 
import java.awt.Dimension; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.GregorianCalendar; 
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import oracle.adf.view.faces.bi.component.graph.Background; 
import oracle.adf.view.faces.bi.component.graph.UIGraph; 
import oracle.dss.dataView.ImageView;

public  class graphsClass  {
  

    public graphsClass() {
    }

  

  

    //String dbURL = "jdbc:oracle:thin:@localhost:1521:orcl";
    String dbURL = "jdbc:oracle:thin:@10.160.12.5:1521:ora10g";

    private Connection getDBConnection() throws SQLException, NamingException {
        Connection con = null;
        DataSource datasource = null;

        Context initialContext;

        initialContext = new InitialContext();

        if (initialContext == null) {
        }

        datasource = (DataSource) initialContext.lookup("java:comp/env/jdbc/LOGDS");

        if (datasource != null) {
            try {
                con = datasource.getConnection();
            } catch (SQLException e) {
            }
        } else {
            System.out.println("Failed to Find JDBC DataSource.");
        }
        return con;
    }


    
    
    
    
 
}
