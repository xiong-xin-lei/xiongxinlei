# 业务子系统管理测试用例
import time

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_subsystems_Page.list_page import list_Page
from PageObject.Business_Page.Business_subsystems_Page.confirm_page import confirm_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page as sysList_Page


def case_test_business_subsystems_enabled(self, key, name):
    index = Index_Page(self.driver)
    index.in_business_system()
    business_system = sysList_Page(self.driver)
    business_system.click_row_subsystems_button(key)
    business_system.unselect_frame()
    time.sleep(1)
    business_subsystems = list_Page(self.driver)
    business_subsystems.list_in()
    flag = business_subsystems.is_enabled_disable(name)
    if flag != 'true':
        business_subsystems.click_row_enabled_button(name)
        enabled_business_subsystems = confirm_Page(self.driver)
        enabled_business_subsystems.click_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_business_subsystems = list_Page(self.driver)
#     enabled_business_subsystems.click_row_checkbox()
#     enabled_business_subsystems.click_enabled_button()
#     time.sleep(2)
#     enabled_business_subsystems.click_confirm()
