package moh.logistics.lib.reports.code;

public interface AdjSummaryQueriesInterface {
    /*
     * ALL FACILITIES ORDERED BY SUPPLIER
     */

   


    /*
     * ADJUSTMENT SUMMARY REPORT QUERY
     */
    public static String adjSummaryMainQuery =
        "select f.fac_name, adj.type_name, " + " case when sum(ci.adjustments) > 0 then  " +
        " decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )  " +
        " else sum(ci.adjustments) end as adjustments " + " , adj.adj_type_id " +
        " from ctf_item ci, ctf_main cm, facility f, adj_type adj " + " where adj.adj_type_id = ci.adj_type_id " +
        " and f.facility_id=cm.facility_id " + " and ci.ctf_main_id = cm.ctf_main_id ";

    public static final String findAdjustments =
        "select distinct adj.adj_type_id " + " from ctf_item ci, ctf_main cm, facility f, adj_type adj " +
        " where adj.adj_type_id = ci.adj_type_id " + " and f.facility_id=cm.facility_id(+) " +
        " and ci.ctf_main_id = cm.ctf_main_id ";

}
