# 业务管理 业务系统测试套件

import allure
import pytest

from Business.Base_url import url
from Business.login_business import login, site_in
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Common.tools.read_csv import read_csv, read_success_csv
from TestCase.BusinessCase.Business_subsystemsCase.test_add_case import case_test_business_subsystems_new
from TestCase.BusinessCase.Business_subsystemsCase.test_del_case import case_test_business_subsystems_del
from TestCase.BusinessCase.Business_subsystemsCase.test_disabled_case import case_test_business_subsystems_disabled
from TestCase.BusinessCase.Business_subsystemsCase.test_edit_case import case_test_business_subsystems_edit
from TestCase.BusinessCase.Business_subsystemsCase.test_enabled_case import case_test_business_subsystems_enabled
from TestCase.BusinessCase.Business_systemsCase.test_add_case import case_test_business_system_new
from TestCase.BusinessCase.Business_systemsCase.test_del_case import case_test_business_system_del
from TestCase.BusinessCase.Business_systemsCase.test_disabled_case import case_test_business_system_disabled
from TestCase.BusinessCase.Business_systemsCase.test_edit_case import case_test_business_system_edit
from TestCase.BusinessCase.Business_systemsCase.test_enabled_case import case_test_business_system_enabled


class Test_Business_System_Suite:

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

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('新增')
    @pytest.mark.parametrize('name, description, assert_type', read_csv('TestData/Business/Business_system/add.csv'))
    def test_business_system_add(self, name, description, assert_type):
        if assert_type == '1':
            allure_title = "添加'{}'成功".format(name)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "业务系统名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "业务系统名称'{}'重复".format(name)
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_business_system_new(self, name, description, assert_type)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('子系统新增')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_csv('TestData/Business/Business_subsystems/add.csv'))
    def test_business_subsystems_add(self, key, name, description, assert_type):
        if assert_type == '1':
            allure_title = "添加'{}'成功".format(name)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "业务子系统名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "业务子系统名称'{}'重复".format(name)
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_business_subsystems_new(self, key, name, description, assert_type)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('子系统停用')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_success_csv('TestData/Business/Business_subsystems/add.csv', '1'))
    def test_business_subsystems_disabled(self, key, name, description, assert_type):
        allure_title = "停用'{}'业务子系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_subsystems_disabled(self, key, name)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('子系统启用')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_success_csv('TestData/Business/Business_subsystems/add.csv', '1'))
    def test_business_subsystems_enabled(self, key, name, description, assert_type):
        allure_title = "启用'{}'业务子系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_subsystems_enabled(self, key, name)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('子系统编辑')
    @pytest.mark.parametrize('system, key, name, description, assert_type',
                             read_csv('TestData/Business/Business_subsystems/edit.csv'))
    def test_business_subsystems_edit(self, system, key, name, description, assert_type):
        case_test_business_subsystems_disabled(self, system, key)
        allure_title = "编辑'{}'业务子系统".format(key)
        allure.dynamic.title(allure_title)
        case_test_business_subsystems_edit(self, system, key, name, description, assert_type)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('子系统删除')
    @pytest.mark.parametrize('system, key, name, description, assert_type',
                             read_success_csv('TestData/Business/Business_subsystems/edit.csv', '1'))
    def test_business_subsystems_del(self, system, key, name, description, assert_type):
        # case_test_business_system_disabled(self, key)
        allure_title = "删除'{}'业务子系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_subsystems_del(self, system, name)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('停用')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Business/Business_system/add.csv', '1'))
    def test_business_system_disabled(self, name, description, assert_type):
        allure_title = "停用'{}'业务系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_system_disabled(self, name)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('启用')
    @pytest.mark.parametrize('name, description, assert_type',
                             read_success_csv('TestData/Business/Business_system/add.csv', '1'))
    def test_business_system_enabled(self, name, description, assert_type):
        allure_title = "启用'{}'业务系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_system_enabled(self, name)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('编辑')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_csv('TestData/Business/Business_system/edit.csv'))
    def test_business_system_edit(self, key, name, description, assert_type):
        case_test_business_system_disabled(self, key)
        allure_title = "编辑'{}'业务系统".format(key)
        allure.dynamic.title(allure_title)
        case_test_business_system_edit(self, key, name, description, assert_type)

    @allure.epic("业务管理测试")
    @allure.feature('业务系统管理')
    @allure.story('删除')
    @pytest.mark.parametrize('key, name, description, assert_type',
                             read_success_csv('TestData/Business/Business_system/edit.csv', '1'))
    def test_business_system_del(self, key, name, description, assert_type):
        # case_test_business_system_disabled(self, key)
        allure_title = "删除'{}'业务系统".format(name)
        allure.dynamic.title(allure_title)
        case_test_business_system_del(self, name)
