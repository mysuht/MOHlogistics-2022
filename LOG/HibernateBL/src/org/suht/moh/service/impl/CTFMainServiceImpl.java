package org.suht.moh.service.impl;

import javax.transaction.Transactional;

import org.suht.moh.dao.impl.CTFMainDaoImpl;
import org.suht.moh.service.CTFMainService;

@Transactional
public class CTFMainServiceImpl implements CTFMainService {
    CTFMainDaoImpl ctfMainDao;
    public CTFMainServiceImpl() {
        super();
     ctfMainDao = new CTFMainDaoImpl();
    }

    @Override
    public Long getMinCtfMainId() {
        return ctfMainDao.getMinCtfMainId();
    }
}
