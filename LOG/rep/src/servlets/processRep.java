package servlets;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;

import atg.taglib.json.util.JSONObject;

import com.logistics.lib.dto.Facility;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import moh.logistics.lib.reports.code.FacilityTemplate;
import moh.logistics.lib.reports.code.Home;

public class processRep extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1256";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        response.setContentType(CONTENT_TYPE);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        //get the PrintWriter object to write the html page
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");
        System.out.println(" TYPE IS : " + type);
        //        List<FacilityTemplate> facilityList
        //            = Home.getFacilityByTypeId(type);
        String jsonData = "";
        try {
            jsonData = getJSONData(type);
            out.print(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //facilityList.getJ

    }

    public String getJSONData(String type) throws JSONException {
        String jsonData = "";

        JSONObject facilityDetails;
        JSONObject jSonDataObj;

        JSONArray facilityDetailsArr;

        List<FacilityTemplate> facilityList = Home.getFacilityByTypeId(type);
        ;

        facilityDetailsArr = new JSONArray();
        for (FacilityTemplate facility : facilityList) {

            facilityDetails = new JSONObject();
            facilityDetails.put("id", facility.getFacilityId());
            facilityDetails.put("name", facility.getFacilityName());
            facilityDetailsArr.put(facilityDetails);
        }
        jSonDataObj = new JSONObject();
        jSonDataObj.put("JsonData", facilityDetailsArr);
        jsonData = jSonDataObj.toString();
        return jsonData;
    }

    public Map<String,Object> requestedDates(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                   String hq) {
        Map<String,Object> map = new HashMap<String, Object>();
        String dateFromStr = null;
        String dateToStr = null;
        switch (dat) {
        case "m":
            if (mon.equals("0")) {
                dateFromStr = "01/" + year;
                dateToStr = "12/" + year;
            } else {
                dateFromStr = mon + "/" + year;
                dateToStr = mon + "/" + year;
            }
            break;
        case "q":
            if (mon.equals("0")) {
                dateFromStr = "01/" + year;
                dateToStr = "12/" + year;
            } else {
                switch (Integer.valueOf(mon)) {
                case 0:
                    dateFromStr = "01/" + year;
                    dateToStr = "12/" + year;
                    break;
                case 1:
                    dateFromStr = "01/" + year;
                    dateToStr = "03/" + year;
                    break;
                case 2:
                    dateFromStr = "04/" + year;
                    dateToStr = "06/" + year;
                    break;
                case 3:
                    dateFromStr = "07/" + year;
                    dateToStr = "09/" + year;
                    break;
                case 4:
                    dateFromStr = "10/" + year;
                    dateToStr = "12/" + year;
                    break;
                }
            }
            break;
        case "u":
            dateFromStr = fM + "/" + fY;
            dateToStr = tM + "/" + tY;
            break;
        case "hq":
            if (Integer.valueOf(hq) == 1) {
                dateFromStr = "01/" + year;
                dateToStr = "06/" + year;
            } else if (Integer.valueOf(hq) == 2) {
                dateFromStr = "07/" + year;
                dateToStr = "12/" + year;
            }
            break;
        }
        map.put("dateFromStr", dateFromStr);
        map.put("dateToStr", dateToStr);
        return map;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
//
//        String[] cen;
//        String fY = "";
//        String tY = "";
//        String fM = "";
//        String tM = "";
//        String hq = "";
//        String type = request.getParameter("p_dir");
//        String dat = request.getParameter("dat");
//        String mon = "";
//        if (dat.equals("m")) {
//            mon = request.getParameter("p_mon");
//        }
//        if (dat.equals("q")) {
//            mon = request.getParameter("p_quart");
//        }
//        if (dat.equals("u")) {
//            fY = request.getParameter("p_fyear");
//            tY = request.getParameter("p_tyear");
//            fM = request.getParameter("p_fmon");
//            tM = request.getParameter("p_tmon");
//        }
//        if (dat.equals("hq")) {
//            hq = request.getParameter("p_hq");
//        }
//        String year = request.getParameter("p_year");
        
//        Map<String,Object> dates = requestedDates(mon, year, dat, fY, tY, fM, tM, hq);
//        System.out.println("dateFromStr :: " + dates.get("dateFromStr"));
//        System.out.println("dateToStr :: " + dates.get("dateToStr"));
        int rep = Integer.parseInt(request.getParameter("rep"));

        switch (rep) {
        case 1:
            request.getRequestDispatcher("nonRepFacResultV2.jsp").forward(request, response);
            break;
        case 2:
            request.getRequestDispatcher("adjSummaryResult.jsp").forward(request, response);
            break;
        case 3:
            request.getRequestDispatcher("progAdjSummaryResult.jsp").forward(request, response);
            break;
        case 4:
            request.getRequestDispatcher("aggStockMovByLevelRepResult.jsp").forward(request, response);
            break;
        case 5:
            request.getRequestDispatcher("aggStockMovRepResult.jsp").forward(request, response);
            break;
        case 6:
            request.getRequestDispatcher("cypRepResult.jsp").forward(request, response);
            break;
        case 7:
            request.getRequestDispatcher("facStockMovRepResult.jsp").forward(request, response);
            break;
        case 8:
            request.getRequestDispatcher("suppStatusErrorRep.jsp").forward(request, response);
            break;
        case 9:
            request.getRequestDispatcher("supStatusRepResult.jsp").forward(request, response);
            break;
        case 10:
            request.getRequestDispatcher("dispensedToUserRepResult.jsp").forward(request, response);
            break;
        case 11:
            request.getRequestDispatcher("FacilityDispensedToUser.jsp").forward(request, response);
            break;
        case 12:
            request.getRequestDispatcher("facilityProductDispensedRepResult.jsp").forward(request, response);
            break;
        case 13:
            request.getRequestDispatcher("progProductDispensedRepResult.jsp").forward(request, response);
            break;
        case 14:
            request.getRequestDispatcher("progDispensedToUserRepResult.jsp").forward(request, response);
            break;
        case 15:
            request.getRequestDispatcher("serviceStatisticsReportResult22.jsp").forward(request, response);
            break;
        case 16:
            request.getRequestDispatcher("facilityServiceStatisticsRepResult.jsp").forward(request, response);
            break;
        case 17:
            request.getRequestDispatcher("programServiceStatisticsRepResult.jsp").forward(request, response);
            break;
        case 18:
            request.getRequestDispatcher("stockedOutFacilitiesResult.jsp").forward(request, response);
            break;
        case 19:
            request.getRequestDispatcher("stockedOutFacilitiesProductsResult.jsp").forward(request, response);
            break;
        case 20:
            request.getRequestDispatcher("overStockedFacilityResult.jsp").forward(request, response);
            break;
        case 21:
            request.getRequestDispatcher("belowEmergencyOrderPointResult.jsp").forward(request, response);
            break;
        case 22:
            request.getRequestDispatcher("dataEntryErrorReportResult.jsp").forward(request, response);
            break;
        case 23:
            request.getRequestDispatcher("BelowEmergencyOrderPointV2.jsp").forward(request, response);
            break;
        case 24:
            request.getRequestDispatcher("stockedOutFacilitiesProductsV2.jsp").forward(request, response);
            break;
        case 25:
            request.getRequestDispatcher("facilityProductDispensedV2.jsp").forward(request, response);
            break;
        case 26:
            request.getRequestDispatcher("progProductDispensedV2.jsp").forward(request, response);
            break;


        }
        //request.getRequestDispatcher("aggStockMovByLevelRepResult.jsp").forward(request, response);
        out.close();
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                          IOException {
    }
}
