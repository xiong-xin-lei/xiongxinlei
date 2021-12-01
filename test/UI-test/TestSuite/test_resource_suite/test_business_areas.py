# 资源管理 业务区管理测试套件

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
from TestCase.ResourceCase.Business_areasCase.test_edit_case import case_test_business_areas_edit
from TestCase.ResourceCase.Business_areasCase.test_enabled_case import case_test_business_areas_enabled


class Test_Business_Areas_Suite:

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
    @allure.feature('业务区管理')
    @allure.story('新增')
    @pytest.mark.parametrize('name, description, assert_type', read_csv('TestData/Resource/Business_areas/add.csv'))
    def test_business_areas_add(self, name, description, assert_type):
        if assert_type == '1':
            allure_title = "添加'{}'成功".format(name)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "业务区名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "业务区名称'{}'重复".format(name)
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_business_areas_new(self, name, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('业务区管理')
    @allure.story('停用')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/add.csv', '1'))
    def test_business_areas_disabled(self, name, description, assert_type):
        allure_title = "停用'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_disabled(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('业务区管理')
    @allure.story('启用')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/add.csv', '1'))
    def test_business_areas_enabled(self, name, description, assert_type):
        allure_title = "启用'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_enabled(self, name)

    @allure.epic("资源管理测试")
    @allure.feature('业务区管理')
    @allure.story('编辑')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_csv('TestData/Resource/Business_areas/edit.csv'))
    def test_business_areas_edit(self, key, name, description, assert_type):
        case_test_business_areas_disabled(self, key)
        allure_title = "编辑'{}'业务区".format(key)
        allure.dynamic.title(allure_title)
        case_test_business_areas_edit(self, key, name, description, assert_type)

    @allure.epic("资源管理测试")
    @allure.feature('业务区管理')
    @allure.story('删除')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_success_csv('TestData/Resource/Business_areas/edit.csv', '1'))
    def test_business_areas_del(self, key, name, description, assert_type):
        # case_test_business_areas_disabled(self, key)
        allure_title = "删除'{}'业务区".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_areas_del(self, name)
