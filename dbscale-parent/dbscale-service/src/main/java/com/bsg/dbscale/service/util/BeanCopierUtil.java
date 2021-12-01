package com.bsg.dbscale.service.util;

import org.springframework.cglib.beans.BeanCopier;

/**
 * 
 * @author HCK
 *
 */
public class BeanCopierUtil {

    public static <T> T create(Object sourceObj, Class<T> targetClass)
            throws InstantiationException, IllegalAccessException {
        BeanCopier copier = BeanCopier.create(sourceObj.getClass(), targetClass, false);
        T targetObj = targetClass.newInstance();
        copier.copy(sourceObj, targetObj, null);
        return targetObj;
    }
}
