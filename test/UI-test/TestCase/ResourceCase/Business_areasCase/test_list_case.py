# 业务区管理测试用例
import time

import allure
import pytest
from Business.Base_url import url
from Business.sync_user_data import acquire_user, release_user
from Common.selenium_library import SeleniumBase
from Business.login_business import login, site_in
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Business_areas_Page.list_page import list_Page


@allure.epic("资源管理测试")
@allure.feature('业务区管理')
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
        index.click_business_areas()
        business_areas = list_Page(self.driver)
        business_areas.list_in()

    # @allure.story('搜索')
    # def test_business_areas_search(self):
    #     allure_title = "搜索"
    #     allure.dynamic.title(allure_title)
    #     business_areas = list_Page(self.driver)
    #     business_areas.send_keys(business_areas.loc_search_input, '测试新增业务区1')
    #     business_areas.click_search_button()
    #     time.sleep(5)
    #     print(business_areas.get_cell_text(business_areas.table_loc, 0))
    #     assert '业务区' in business_areas.get_cell_text(business_areas.table_loc, 0), "搜索无误"

    @allure.story('刷新')
    def test_business_areas_refresh(self):
        allure_title = "刷新"
        allure.dynamic.title(allure_title)
        business_areas = list_Page(self.driver)
        business_areas.click_refresh_button()


if __name__ == '__main__':
    # pytest.main(["-s", "login_case.py", "--html=tmp/html/test.html", "--alluredir=tmp/log"])
    pytest.main(["-s", "test_list_case.py"])
