package org.suht.moh.dao;

import org.suht.moh.model.CTFMain;

public interface CTFMainDao extends GenericDao<CTFMain> {
    Long getMinCtfMainId();
}
