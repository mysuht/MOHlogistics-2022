package moh.logistics.lib.common;

import com.logistics.lib.util.DatabaseConnectionManager;

import java.math.BigDecimal;

import java.sql.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import oracle.jdbc.OracleDriver;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor; 

public class TestDatabase {
	public static Connection getConnection() throws SQLException {
        String username = "log5";
        String password = "log5";
        String thinConn = "jdbc:oracle:thin:@localhost:1521:ORCL";
        DriverManager.registerDriver(new OracleDriver());
        Connection conn = DriverManager.getConnection(thinConn, username, password);
        conn.setAutoCommit(false);
        return conn;
    }
	public static void passArray()
	{
		try{
			DatabaseConnectionManager dcm 
                            = new DatabaseConnectionManager("localhost"
                                                            , "ORCL"
                                                            , "log5"
                                                            , "log5");
			Connection connection = dcm.getConnection();
			
			String array[] = {"4", "5", "6","7"}; 			
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("STRING_ARRAY", connection);
			ARRAY array_to_pass = new ARRAY(descriptor,connection,array);
			
			CallableStatement st = connection.prepareCall("call LOG5.TEST_ARRAY(?,?,?)");

			// Passing an array to the procedure - 
			st.setArray(1, array_to_pass);

			st.registerOutParameter(2, Types.INTEGER);
			st.registerOutParameter(3,OracleTypes.ARRAY,"INT_ARRAY");
			st.execute();
			
			System.out.println("size : "+st.getInt(2));

			// Retrieving array from the resultset of the procedure after execution -
			ARRAY arr = ((OracleCallableStatement)st).getARRAY(3);
			 BigDecimal[] recievedArray = (BigDecimal[])(arr.getArray());

			for(int i=0;i<recievedArray.length;i++)
				System.out.println("element" + i + ":" + recievedArray[i] + "\n");
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String args[]){
		passArray();
	}
}