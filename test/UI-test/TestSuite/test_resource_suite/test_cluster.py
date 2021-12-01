# 资源管理 集群管理测试套件

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
from TestCase.ResourceCase.ClustersCase.test_edit_case import case_test_clusters_edit
from TestCase.ResourceCase.ClustersCase.test_enabled_case import case_test_clusters_enabled


class Test_Clusters_Suite:

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
    @allure.feature('集群管理')
    @allure.story('新增')
    @pytest.mark.parametrize('name, description, assert_type', read_success_csv('TestData/Resource/Business_areas/add.csv','1'))
    def test_business_areas_add(self, name, description, assert_type):
        allure_title = "新增'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_new(self, name, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('新增')
    @pytest.mark.parametrize('business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type',
                             read_csv('TestData/Resource/Clusters/add.csv'))
    def test_clusters_add(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):

        if assert_type == '1':
            allure_title = "添加'{}'成功".format(name)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "集群名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "集群名称'{}'重复".format(name)
            allure.dynamic.title(allure_title)

        elif assert_type == '4':
            allure_title = "'{}'输入格式有误".format(nfsIp)
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_clusters_new(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('停用')
    @pytest.mark.parametrize('business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/add.csv', '1'))
    def test_clusters_disabled(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):
        allure_title = "停用'{}'集群".format(name)
        allure.dynamic.title(allure_title)
        case_test_clusters_disabled(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('启用')
    @pytest.mark.parametrize('business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/add.csv', '1'))
    def test_clusters_enabled(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):
        allure_title = "启用'{}'集群".format(name)
        allure.dynamic.title(allure_title)
        case_test_clusters_enabled(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('编辑')
    @pytest.mark.parametrize('key, business_area, name, haTag,defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/edit.csv', '1'))
    def test_clusters_edit(self, key, business_area, name, haTag, defServs, description, assert_type):
        # 编辑前先停用
        case_test_clusters_disabled(self, key)
        allure_title = "编辑'{}'集群".format(key)
        allure.dynamic.title(allure_title)
        case_test_clusters_edit(self, key, business_area, name, haTag, defServs, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('删除')
    @pytest.mark.parametrize('key, business_area, name, haTag,defServs, description, assert_type',
                             read_success_csv('TestData/Resource/Clusters/edit.csv', '1'))
    def test_cluster_del(self, key, business_area, name, haTag, defServs, description, assert_type):
        allure_title = "删除'{}'集群".format(name)
        allure.dynamic.title(allure_title)
        case_test_clusters_del(self, name)


    @allure.epic("资源管理测试")
    @allure.feature('集群管理')
    @allure.story('删除')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/add.csv', '1'))
    def test_business_areas_disabled(self, name, description, assert_type):
        allure_title = "停用并删除'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_disabled(self, name)
        case_test_business_areas_del(self, name)