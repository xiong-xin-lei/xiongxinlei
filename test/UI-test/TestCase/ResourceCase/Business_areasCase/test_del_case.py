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
from PageObject.Resource_Page.Business_areas_Page.del_page import del_Page


def case_test_business_areas_del(self, name):
    index = Index_Page(self.driver)
    index.in_business_areas()
    business_areas = list_Page(self.driver)
    business_areas.button_move_over(name)
    business_areas.click_row_del_button()
    del_business_areas = del_Page(self.driver)
    del_business_areas.del_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_business_areas = list_Page(self.driver)
#     del_business_areas.click_row_checkbox()
#     del_business_areas.click_del_button()
#     time.sleep(2)
#     del_business_areas.operation_confirm()
