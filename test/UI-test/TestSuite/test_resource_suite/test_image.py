# 资源管理 镜像管理测试套件

import allure
import pytest

from Business.Base_url import url
from Business.login_business import login, site_in
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Common.tools.read_csv import read_csv
from TestCase.ResourceCase.ImagesCase.test_config_case import case_test_images_config
from TestCase.ResourceCase.ImagesCase.test_disabled_case import case_test_images_disabled
from TestCase.ResourceCase.ImagesCase.test_enabled_case import case_test_images_enabled


class Test_Images_Suite:

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
    @allure.feature('镜像管理')
    @allure.story('停用')
    @pytest.mark.parametrize('type , version', read_csv('TestData/Resource/Image/action.csv'))
    def test_network_disabled(self, type, version):
        allure_title = "停用'{}'镜像".format(type)
        allure.dynamic.title(allure_title)
        case_test_images_disabled(self, type, version)

    @allure.epic("资源管理测试")
    @allure.feature('镜像管理')
    @allure.story('启用')
    @pytest.mark.parametrize('type, version', read_csv('TestData/Resource/Image/action.csv'))
    def test_network_enabled(self, type, version):
        allure_title = "启用'{}'镜像".format(type)
        allure.dynamic.title(allure_title)
        case_test_images_enabled(self, type, version)

        # @allure.epic("资源管理测试")
        # @allure.feature('镜像管理')
        # @allure.story('编辑')
        # @pytest.mark.parametrize(
        #     'type,version, key,value, defaultValue, range, canSet, mustRestart, description, assert_type',
        #     read_csv('TestData/Resource/Image/edit.csv'))
        # def test_network_edit(self,type, version, key, value, defaultValue, range, canSet, mustRestart, description, assert_type):
        #     allure_title = "编辑'{}'镜像".format(type)
        #     allure.dynamic.title(allure_title)
        #     case_test_images_config(self,type, version, key, value, defaultValue, range, canSet, mustRestart, description, assert_type)
