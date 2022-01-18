package org.suht.moh.test;

import org.suht.moh.service.impl.CTFMainServiceImpl;

public class TestHibernate{
    CTFMainServiceImpl ctfMainService;
    public TestHibernate(){
        ctfMainService = new CTFMainServiceImpl();
    }
    public static void main(String[] args) {
       TestHibernate th = new TestHibernate();
        System.out.println(th.ctfMainService.getMinCtfMainId());
   }
}
