package moh.logistics.lib.common;

import java.io.FileInputStream;
import java.io.IOException;

import java.sql.*;
import java.sql.SQLException;

import java.util.Properties;

import javax.sql.DataSource;

import java.io.InputStream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import oracle.adf.share.logging.ADFLogger;


public class DBUtil implements Serializable {
    @SuppressWarnings("compatibility:5821103541661137729")
    private static final long serialVersionUID = 1L;
    private static ADFLogger LOGGER = ADFLogger.createADFLogger(DBUtil.class);

//    private static final String DB_USERNAME = "db.username";
//    private static final String DB_PASSWORD = "db.password";
//    private static final String DB_URL = "db.url";
//    private static final String DB_DRIVER_CLASS = "driver.class.name";

    private static DataSource dataSource;

    public DBUtil() {
    }


    public static DataSource getDataSource() {
        Context initContext;
        try {
            initContext = new InitialContext();
            if (dataSource == null) {
                LOGGER.info("... Jndi Found Successfully go and deal with DS ...");
                dataSource = (DataSource) initContext.lookup("jdbc/LOGConnDS");
            } else {
                LOGGER.info("dataSource already exists .... ");
            }

        } catch (NamingException ne) {
            LOGGER.info("... Jndi Not Found ...");
            ne.printStackTrace();
        }
        return dataSource;
    }

    static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info(" connection is now Cleaned ...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }

    static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
                LOGGER.info(" resultSet is now Cleaned ...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                resultSet = null;
            }
        }
    }

    static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                LOGGER.info(" preparedStatement is now Cleaned ...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                preparedStatement = null;
            }
        }
    }

    static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
                LOGGER.info(" statement is now Cleaned ...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                statement = null;
            }
        }
    }

    static void closeStatement(CallableStatement callableStatement) {
        if (callableStatement != null) {
            try {
                callableStatement.close();
                LOGGER.info(" callableStatement is now Cleaned ...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                callableStatement = null;
            }
        }
    }

    public static void cleanDS(PreparedStatement preparedStatement, ResultSet resultSet, Connection connection) {
        closePreparedStatement(preparedStatement);
        closeResultSet(resultSet);
        closeConnection(connection);
    }

    public static void cleanDS(Statement statement, ResultSet resultSet, Connection connection) {
        closeStatement(statement);
        closeResultSet(resultSet);
        closeConnection(connection);
        LOGGER.info(" dataSource is now Cleaned ...");
    }
    
    public static void cleanDS(Statement statement, Connection connection) {
        closeStatement(statement);
        closeConnection(connection);
        LOGGER.info(" dataSource is now Cleaned ...");
    }

    public static void cleanDS(Statement statement, ResultSet resultSet) {
        closeStatement(statement);
        closeResultSet(resultSet);
        LOGGER.info(" dataSource is now Cleaned ...");
    }

    public void cleanDS(CallableStatement callableStatement, Connection connection) {
        closeStatement(callableStatement);
        closeConnection(connection);
        LOGGER.info(" dataSource is now Cleaned ...");
    }

    // public static DBUtil getInstance() {
    // return new DBUtil();
    // }
    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                String columnName = md.getColumnName(i).toUpperCase();
                Object columnValue = rs.getObject(i);
                row.put(columnName, columnValue);
                LOGGER.info(" column_name :: " + columnName);
                LOGGER.info(" column_value :: " + columnValue);
            }
            LOGGER.info(" row added successfully...");
            rows.add(row);
        }

        // // 1. way
        // for (Map<String, Object> row:dataList) {
        // for (Map.Entry<String, Object> rowEntry : row.entrySet()) {
        // System.out.print(rowEntry.getKey() + " = " + rowEntry.getValue() + ", ");
        // }
        // }
        //
        // //2 second way -
        // for (int i=0; i<dataList.size(); i++) {
        // System.out.print(" " + dataList.get(i).get("REPORT_SECTION"));
        // }
        return rows;
    }
}


