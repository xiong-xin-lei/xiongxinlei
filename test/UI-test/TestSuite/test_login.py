# 登录测试套件

import allure
import pytest

from Common.selenium_library import SeleniumBase
from Common.tools.read_csv import read_csv
from TestCase.LoginCase.login_case import case_test_login


class Test_Login_Suite:

    def setup_class(self):
        self.driver = SeleniumBase().get_web_driver()

    def teardown_class(self):
        SeleniumBase(self.driver).quit()

    @allure.epic('登录测试')
    @pytest.mark.parametrize('username, password, assert_type', read_csv('TestData/login_user_password.csv'))
    def test_login(self, username, password, assert_type):
        case_test_login(self, username, password, assert_type)
