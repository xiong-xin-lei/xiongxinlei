package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.NetworkForm;

@Service
public class NetworkCheck extends BaseCheck {

    public CheckResult checkSave(NetworkForm networkForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(networkForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(networkForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String networkId, NetworkForm networkForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(networkForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(networkId, networkForm);

        return checkResult;
    }

    public CheckResult checkRemove(String networkId) throws Exception {
        CmNetwork cmNetwork = CmApi.getNetwork(networkId);
        if (cmNetwork == null) {
            String msg = "该网段不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmNetwork.getUnschedulable())) {
            String msg = "该网段已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String networkId, boolean enabled) throws Exception {
        CmNetwork cmNetwork = CmApi.getNetwork(networkId);
        if (cmNetwork == null) {
            String msg = "该网段不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(NetworkForm networkForm) {
        if (StringUtils.isBlank(networkForm.getBusinessAreaId())) {
            String msg = "业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(networkForm.getName())) {
            String msg = "网段名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(networkForm.getStartIp())) {
            String msg = "起始IP不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(networkForm.getEndIp())) {
            String msg = "结束IP不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(networkForm.getGateway())) {
            String msg = "网关不能为空。";
            return CheckResult.failure(msg);
        }

        if (networkForm.getNetmask() == null) {
            String msg = "掩码不能为空。";
            return CheckResult.failure(msg);
        }

        if (networkForm.getVlan() == null) {
            String msg = "VLAN不能为空。";
            return CheckResult.failure(msg);
        }

        if (networkForm.getTopologys() == null || networkForm.getTopologys().size() == 0) {
            String msg = "网络拓扑不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(NetworkForm networkForm) throws Exception {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(networkForm.getBusinessAreaId());
        if (businessAreaDO == null) {
            String msg = "业务区不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(businessAreaDO.getEnabled())) {
            String msg = "该业务区已停用。";
            return CheckResult.failure(msg);
        }

        CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
        cmNetworkQuery.setSiteId(businessAreaDO.getSiteId());
        cmNetworkQuery.setName(networkForm.getName());
        List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
        if (cmNetworks.size() != 0) {
            String msg = "网段名已存在。";
            return CheckResult.failure(msg);
        }

        List<String> topologys = networkForm.getTopologys();
        for (String topology : topologys) {
            DictDO topologyDictDO = dictDAO.get(DictTypeConsts.NETWORK_TOPOLOGY, topology);
            if (topologyDictDO == null) {
                String msg = "网络拓扑不存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(NetworkForm networkForm) {
        if (StringUtils.equals(StringUtils.EMPTY, networkForm.getBusinessAreaId())) {
            String msg = "业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, networkForm.getName())) {
            String msg = "网段名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, networkForm.getStartIp())) {
            String msg = "起始IP不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, networkForm.getEndIp())) {
            String msg = "结束IP不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, networkForm.getGateway())) {
            String msg = "网关不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String networkId, NetworkForm networkForm) throws Exception {
        CmNetwork cmNetwork = CmApi.getNetwork(networkId);
        if (cmNetwork == null) {
            String msg = "该网段不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmNetwork.getUnschedulable())) {
            String msg = "该网段已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(networkForm.getBusinessAreaId())) {
            if (!cmNetwork.getZone().equals(networkForm.getBusinessAreaId())) {
                CmNetwork.IpSummary ipSummary = cmNetwork.getIpSummary();
                if (ipSummary != null && ipSummary.getUsed() != null && ipSummary.getUsed().intValue() != 0) {
                    String msg = "该网段已使用，不能编辑业务区。";
                    return CheckResult.failure(msg);
                }

                BusinessAreaDO newBusinessAreaDO = businessAreaDAO.get(networkForm.getBusinessAreaId());
                if (newBusinessAreaDO == null) {
                    String msg = "该业务区不存在。";
                    return CheckResult.failure(msg);
                }

                if (BooleanUtils.isFalse(newBusinessAreaDO.getEnabled())) {
                    String msg = "该业务区已停用。";
                    return CheckResult.failure(msg);
                }
            }
        }

        if (StringUtils.isNotBlank(networkForm.getName()) && !cmNetwork.getName().equals(networkForm.getName())) {
            CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
            cmNetworkQuery.setSiteId(cmNetwork.getSite().getId());
            cmNetworkQuery.setName(networkForm.getName());
            List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
            if (cmNetworks.size() > 0) {
                String msg = "该站点下网段名已存在。";
                return CheckResult.failure(msg);
            }
        }

        List<String> topologys = networkForm.getTopologys();
        if (topologys != null) {
            for (String topology : topologys) {
                DictDO topologyDictDO = dictDAO.get(DictTypeConsts.NETWORK_TOPOLOGY, topology);
                if (topologyDictDO == null) {
                    String msg = "网络拓扑不存在。";
                    return CheckResult.failure(msg);
                }
            }
        }

        return CheckResult.success();
    }
}
