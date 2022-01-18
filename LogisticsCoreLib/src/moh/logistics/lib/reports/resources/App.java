package moh.logistics.lib.reports.resources;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class App {
   public static void main(String[] args) {
       System.out.println("Hibernate one to many (XML Mapping)");
               Session session = HibernateUtil.getSessionFactory().openSession();

               session.beginTransaction();
       List<CTFMain> ctfmains  = (List<CTFMain>) session.createQuery("from CTFMain where facility_id=465 and  to_char(p_date,'yyyy')='2016' ").list();
       
       
       for(int i=0; i<ctfmains.size(); i++){
           System.out.println(ctfmains.get(i).getFacilityId()+" **** "+ctfmains.get(i).getPdate()+" **** "
                               );
           Iterator itr = ctfmains.get(i).getCtfItems().iterator();
         
               for(CTFItems id : ctfmains.get(i).getCtfItems()){
                   System.out.println("mainid "+ctfmains.get(i).getCtfMainId()+"date : "+ctfmains.get(i).getPdate()+"kkkkkkkkkkkk "+id.getOpenBal());
               }
               
//           String sql = "select  f.fac_name as facname, ft.fac_name,  nvl(sum(ci.receipts),0), \n" + 
//           " nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), \n" + 
//           " decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), \n" + 
//           " nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id \n" + 
//           " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n" + 
//           " where ci.ctf_main_id = cm.ctf_main_id(+) \n" + 
//           " and cm.facility_id = f.facility_id(+) \n" + 
//           " and f.fac_type_id= ft.fac_type_id(+) \n" + 
//           " and ci.prod_id=4 \n" + 
//           " and f.facility_id in (select ff.facility_id from facility ff  where sup_code=287) \n" + 
//           " and to_char(cm.p_date,'mm/yyyy')='01/2015' \n" + 
//           " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n" + 
//           " order by 1 \n";
           
           
           String sql = 
               "select  distinct sum(ci.closing_bal) cbal, sum(avg_mnthly_cons) as avg,  decode ( sum(avg_mnthly_cons), 0 , 0 , sum(ci.closing_bal)/ sum(avg_mnthly_cons) ) as MOS , cm.p_date  \n" + 
               "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
               "where ci.ctf_main_id = cm.ctf_main_id \n" + 
               "and ff.facility_id = cm.facility_id \n" + 
               "and fft.fac_type_id = ff.fac_type_id \n" + 
               "and ff.facility_id = 693 \n" + 
               " and ci.prod_id = 4 \n" + 
               "-- and ff.active = 1 \n" + 
               " and to_char(cm.p_date,'mm/yyyy')='01/2015' group by cm.p_date \n" + 
               "order by cm.p_date desc";
           SQLQuery query = session.createSQLQuery(sql);
          
           System.out.println(query.list().size());
           
           query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
           List<Map<String,Object>> a=query.list();
           //List<String> a = query.list();
          // for(int k=0; k<a.size(); k++){
           for(Map k : a) {
               System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx "+k.get("CBAL")+" xxxx "+
                                  k.get("AVG")+
                                  " xxx "+k.get("MOS"));
           }
           //query.setParameter("roll", a);
           //    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
         

       }

   }
   
   
    CTFItems findIfPresent(CTFItems source, HashSet<CTFItems> set)
    {
       if (set.contains(source)) {
          for (CTFItems obj : set) {
            if (obj.equals(source)) 
              return obj;
          } 
       }

      return null;
    }
}
