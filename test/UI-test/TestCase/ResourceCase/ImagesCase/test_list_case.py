# 镜像管理测试用例
import time

import allure
import pytest
from Business.Base_url import url
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Business.login_business import login, site_in
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Images_Page.list_page import list_Page


@allure.epic("资源管理测试")
@allure.feature('镜像管理')
class Test_login:

    def setup_class(self):
        self.k, self.user = acquire_user("../../../TestData/user.csv")
        self.driver = SeleniumBase().get_web_driver()
        username, password = self.user
        se = SeleniumBase(self.driver)
        se.get(url)
        login(self.driver, username, password)
        site_in(self.driver)

    def teardown_class(self):
        release_user(self.k)
        SeleniumBase(self.driver).quit()

    def setup_method(self):
        se = SeleniumBase(self.driver)
        se.get(url)
        index = Index_Page(self.driver)
        index.click_image()
        images = list_Page(self.driver)
        images.list_in()

    # @allure.story('停用')
    # def test_network_disabled(self):
    #     network = list_Page(self.driver)
    #     # isDisabled = network.is_button_disabled()
    #     network.click_row_disabled_button()
    #     time.sleep(3)
    #     network.operation_confirm()
    #     assert True, "停用成功"
    #     time.sleep(3)

    # @allure.story('启用')
    # def test_network_enabled(self):
    #     network = list_Page(self.driver)
    #     network.click_row_enabled_button()
    #     time.sleep(3)
    #     network.operation_confirm()
    #     assert True, '启用成功'
    #     time.sleep(3)

    # @allure.story('搜索')
    # def test_network_search(self):
    #     allure_title = "搜索"
    #     allure.dynamic.title(allure_title)
    #     network = list_Page(self.driver)
    #     network.send_keys(network.loc_search_input, '123')
    #     network.click_search_button()
    #     time.sleep(5)
    #     print(network.get_cell_text(network.table_loc, 0))
    #     assert '123' in network.get_cell_text(network.table_loc, 0), "搜索无误"

    @allure.story('刷新')
    def test_network_refresh(self):
        allure_title = "刷新"
        allure.dynamic.title(allure_title)
        network = list_Page(self.driver)
        network.click_refresh_button()


if __name__ == '__main__':
    # pytest.main(["-s", "login_case.py", "--html=tmp/html/test.html", "--alluredir=tmp/log"])
    pytest.main(["-s", "test_list_case.py"])
