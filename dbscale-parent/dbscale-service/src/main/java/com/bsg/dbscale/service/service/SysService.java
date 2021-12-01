package com.bsg.dbscale.service.service;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmVersion;

@Service
public class SysService extends BaseService {

    public CmVersion getVersion() throws Exception {
        CmVersion cmVersion = CmApi.getVersion();
        return cmVersion;
    }
}
