# 业务管理 规模管理测试套件

import allure
import pytest

from Business.Base_url import url
from Business.login_business import login, site_in
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Common.tools.read_csv import read_csv, read_success_csv
from TestCase.BusinessCase.ScalesCse.test_add_case import case_test_scales_new
from TestCase.BusinessCase.ScalesCse.test_del_case import case_test_scales_del
from TestCase.BusinessCase.ScalesCse.test_disabled_case import case_test_scales_disabled
from TestCase.BusinessCase.ScalesCse.test_enabled_case import case_test_scales_enabled


class Test_Scales_Suite:

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
    @allure.feature('规模管理')
    @allure.story('新增')
    @pytest.mark.parametrize('type, cpu, mem, assert_type', read_csv('TestData/Business/Scales/add.csv'))
    def test_scales_add(self, type, cpu, mem, assert_type):
        if assert_type == '1':
            allure_title = "添加'{}'成功".format(type)
            allure.dynamic.title(allure_title)

        elif assert_type == "2":
            allure_title = "规模名称为空"
            allure.dynamic.title(allure_title)

        elif assert_type == "3":
            allure_title = "规模名称'{}'重复".format(type)
            allure.dynamic.title(allure_title)

        else:
            allure_title = "未知错误"
            allure.dynamic.title(allure_title)
        case_test_scales_new(self, type, cpu, mem, assert_type)

    @allure.epic("业务管理测试")
    @allure.feature('规模管理')
    @allure.story('停用')
    @pytest.mark.parametrize('type, cpu, mem, assert_type',
                             read_success_csv('TestData/Business/Scales/add.csv', '1'))
    def test_scales_disabled(self, type, cpu, mem, assert_type):
        name = cpu + '核' + mem + 'G'
        allure_title = "停用'{}'规模".format(name)
        allure.dynamic.title(allure_title)
        case_test_scales_disabled(self, type, name)

    @allure.epic("业务管理测试")
    @allure.feature('规模管理')
    @allure.story('启用')
    @pytest.mark.parametrize('type, cpu, mem, assert_type',
                             read_success_csv('TestData/Business/Scales/add.csv', '1'))
    def test_scales_enabled(self, type, cpu, mem, assert_type):
        name = cpu + '核' + mem + 'G'
        allure_title = "启用'{}'规模".format(name)
        allure.dynamic.title(allure_title)
        case_test_scales_enabled(self, type, name)

    @allure.epic("业务管理测试")
    @allure.feature('规模管理')
    @allure.story('删除')
    @pytest.mark.parametrize('type, cpu, mem, assert_type',
                             read_success_csv('TestData/Business/Scales/add.csv', '1'))
    def test_scales_del(self, type, cpu, mem, assert_type):
        name = cpu + '核' + mem + 'G'
        case_test_scales_disabled(self, type, name)
        allure_title = "删除'{}'规模".format(name)
        allure.dynamic.title(allure_title)
        case_test_scales_del(self, type, name)
