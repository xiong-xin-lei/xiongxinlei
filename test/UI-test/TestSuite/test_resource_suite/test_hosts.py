# 资源管理 主机管理测试套件
import time

import allure
import pytest

from Business.Base_url import url
from Business.login_business import login, site_in
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Common.tools.read_csv import read_csv, read_success_csv
from TestCase.ResourceCase.Business_areasCase.test_add_case import case_test_business_areas_new
from TestCase.ResourceCase.Business_areasCase.test_del_case import case_test_business_areas_del
from TestCase.ResourceCase.Business_areasCase.test_disabled_case import case_test_business_areas_disabled
from TestCase.ResourceCase.ClustersCase.test_add_case import case_test_clusters_new
from TestCase.ResourceCase.ClustersCase.test_del_case import case_test_clusters_del
from TestCase.ResourceCase.ClustersCase.test_disabled_case import case_test_clusters_disabled
from TestCase.ResourceCase.HostsCase.test_add_case import case_test_hosts_new
from TestCase.ResourceCase.HostsCase.test_del_case import case_test_hosts_del
from TestCase.ResourceCase.HostsCase.test_btnIn_case import case_test_hosts_btnIn
from TestCase.ResourceCase.HostsCase.test_btnOut_case import case_test_hosts_btnOut
from TestCase.ResourceCase.HostsCase.test_disabled_case import case_test_hosts_disabled
from TestCase.ResourceCase.HostsCase.test_before_edit_case import case_test_hosts_before_edit
from TestCase.ResourceCase.HostsCase.test_after_edit_case import case_test_hosts_after_edit
from TestCase.ResourceCase.HostsCase.test_enabled_case import case_test_hosts_enabled
from TestCase.ResourceCase.HostsCase.test_monitoring_case import case_test_hosts_monitoring
from TestCase.ResourceCase.NetworkCase.test_add_case import case_test_network_new
from TestCase.ResourceCase.NetworkCase.test_del_case import case_test_network_del
from TestCase.ResourceCase.NetworkCase.test_disabled_case import case_test_network_disabled


class Test_Hosts_Suite:

    def setup_class(self):
        self.k, self.user = acquire_user("TestData/user.csv")
        self.driver = SeleniumBase().get_web_driver()
        username, password = self.user
        se = SeleniumBase(self.driver)
        se.get(url)
        login(self.driver, username, password)
        site_in(self.driver)

    def teardown_class(self):
        release_user(self.k)
        SeleniumBase(self.driver).quit()

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('新增')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/add.csv', '1'))
    def test_business_areas_add(self, name, description, assert_type):
        allure_title = "新增'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_new(self, name, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('新增')
    @pytest.mark.parametrize('business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/add.csv', '1'))
    def test_clusters_add(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):
        allure_title = "新增'{}'集群".format(name)
        allure.dynamic.title(allure_title)
        case_test_clusters_new(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('新增')
    @pytest.mark.parametrize(
        'name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description, assert_type',
        read_success_csv('TestData/Resource/Network/add.csv', '1'))
    def test_network_add(self, name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description,
                         assert_type):
        allure_title = "新增'{}'网段".format(name)
        allure.dynamic.title(allure_title)
        case_test_network_new(self, name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description,
                              assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('新增')
    @pytest.mark.parametrize(
        'business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type',
        read_csv('TestData/Resource/Hosts/add.csv'))
    def test_hosts_add(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId,
                       maxUnitCnt, hotSpare, description, assert_type):

        if assert_type == '1':
            allure_title = "添加'{}'成功".format(hostIp)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "主机名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "主机IP'{}'重复".format(hostIp)
            allure.dynamic.title(allure_title)

        elif assert_type == '4':
            allure_title = "输入格式有误"
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_hosts_new(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId,
                            maxUnitCnt, hotSpare, description, assert_type)


    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('入库前编辑')
    @pytest.mark.parametrize(
        'key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type',
        read_csv('TestData/Resource/Hosts/beforeEdit.csv'))
    def test_hosts_before_edit(self, key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths,
                               remoteStorageId, maxUnitCnt, hotSpare, description, assert_type):
        allure_title = "编辑入库前主机'{}'".format(key)
        allure.dynamic.title(allure_title)
        case_test_hosts_before_edit(self, key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths,
                                    remoteStorageId, maxUnitCnt, hotSpare, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('入库')
    @pytest.mark.parametrize(
        'key, port, userName, password, assert_type',
        read_csv('TestData/Resource/Hosts/btnIn.csv'))
    def test_hosts_btnIn(self, key, port, userName, password, assert_type):
        # if assert_type == '1':
        #     allure_title = "添加'{}'成功".format(hostIp)
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == "2":
        #     allure_title = "主机名称为空"
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == "3":
        #     allure_title = "主机IP'{}'重复".format(hostIp)
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == '4':
        #     allure_title = "输入格式有误"
        #     allure.dynamic.title(allure_title)
        #
        # else:
        #     allure_title = "未知错误"
        #     allure.dynamic.title(allure_title)
        case_test_hosts_btnIn(self, key, port, userName, password)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('查看监控')
    @pytest.mark.parametrize('key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type',
                             read_success_csv('TestData/Resource/Hosts/beforeEdit.csv', '1'))
    def test_hosts_monitoring(self, key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type):
        allure_title = "查看'{}'主机监控".format(key)
        allure.dynamic.title(allure_title)
        case_test_hosts_monitoring(self, key)

    # @allure.epic("资源管理测试")
    # @allure.feature('主机管理')
    # @allure.story('查看单元')
    # @pytest.mark.parametrize('key, hotSpare, description, assert_type',
    #                          read_success_csv('TestData/Resource/Hosts/beforeEdit.csv', '1'))
    # def test_hosts_del(self, key, hotSpare, description, assert_type):
    #     allure_title = "查看'{}'主机单元".format(key)
    #     allure.dynamic.title(allure_title)
    #     case_test_hosts_units(self, key)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('停用')
    @pytest.mark.parametrize(
        'business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type',
        read_success_csv('TestData/Resource/Hosts/add.csv', '1'))
    def test_hosts_disabled(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId,
                            maxUnitCnt, hotSpare, description, assert_type):
        allure_title = "停用'{}'主机".format(hostIp)
        allure.dynamic.title(allure_title)
        case_test_hosts_disabled(self, hostIp)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('启用')
    @pytest.mark.parametrize(
        'business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,hotSpare, description, assert_type',
        read_success_csv('TestData/Resource/Hosts/add.csv', '1'))
    def test_hosts_enabled(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId,
                           maxUnitCnt, hotSpare, description, assert_type):
        allure_title = "启用'{}'主机".format(hostIp)
        allure.dynamic.title(allure_title)
        case_test_hosts_enabled(self, hostIp)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('入库后编辑')
    @pytest.mark.parametrize('key, hotSpare, description, assert_type',
                             read_csv('TestData/Resource/Hosts/afterEdit.csv'))
    def test_hosts_after_edit(self, key, hotSpare, description, assert_type):
        case_test_hosts_disabled(self, key)
        allure_title = "编辑入库后主机'{}'".format(key)
        allure.dynamic.title(allure_title)
        case_test_hosts_after_edit(self, key, hotSpare, description, assert_type)


    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('出库')
    @pytest.mark.parametrize(
        'key, port, userName, password, assert_type',
        read_csv('TestData/Resource/Hosts/btnOut.csv'))
    def test_hosts_btnOut(self, key, port, userName, password, assert_type):
        # if assert_type == '1':
        #     allure_title = "添加'{}'成功".format(hostIp)
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == "2":
        #     allure_title = "主机名称为空"
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == "3":
        #     allure_title = "主机IP'{}'重复".format(hostIp)
        #     allure.dynamic.title(allure_title)
        #
        # elif assert_type == '4':
        #     allure_title = "输入格式有误"
        #     allure.dynamic.title(allure_title)
        #
        # else:
        #     allure_title = "未知错误"
        #     allure.dynamic.title(allure_title)
        case_test_hosts_btnOut(self, key, port, userName, password)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('删除')
    @pytest.mark.parametrize('key, hotSpare, description, assert_type',
                             read_success_csv('TestData/Resource/Hosts/afterEdit.csv', '1'))
    def test_hosts_del(self, key, hotSpare, description, assert_type):
        allure_title = "删除'{}'主机".format(key)
        allure.dynamic.title(allure_title)
        case_test_hosts_del(self, key)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('网段停用并删除')
    @pytest.mark.parametrize(
        'name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description, assert_type',
        read_success_csv('TestData/Resource/Network/add.csv', '1'))
    def test_network_disabled(self, name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description,
                              assert_type):
        allure_title = "停用并删除'{}'网段".format(name)
        allure.dynamic.title(allure_title)
        case_test_network_disabled(self, name)
        case_test_network_del(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('集群停用并删除')
    @pytest.mark.parametrize('business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/add.csv', '1'))
    def test_clusters_disabled(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):
        allure_title = "停用并删除'{}'集群".format(name)
        allure.dynamic.title(allure_title)
        case_test_clusters_disabled(self, name)
        case_test_clusters_del(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('主机管理')
    @allure.story('业务区停用并删除')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/add.csv', '1'))
    def test_business_areas_disabled(self, name, description, assert_type):
        allure_title = "停用并删除'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_disabled(self, name)
        case_test_business_areas_del(self, name)
