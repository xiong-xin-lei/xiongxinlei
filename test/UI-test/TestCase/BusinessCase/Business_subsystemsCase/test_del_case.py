# 业务子系统管理测试用例
import time

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_subsystems_Page.confirm_page import confirm_Page
from PageObject.Business_Page.Business_subsystems_Page.list_page import list_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page as sysList_Page


def case_test_business_subsystems_del(self, system, name):
    index = Index_Page(self.driver)
    index.in_business_system()
    business_system = sysList_Page(self.driver)
    business_system.click_row_subsystems_button(system)
    business_system.unselect_frame()
    time.sleep(1)
    business_subsystems = list_Page(self.driver)
    business_subsystems.list_in()
    business_subsystems.button_move_over(name)
    business_subsystems.click_row_del_button()
    del_business_subsystems = confirm_Page(self.driver)
    del_business_subsystems.click_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_business_subsystems = list_Page(self.driver)
#     del_business_subsystems.click_row_checkbox()
#     del_business_subsystems.click_del_button()
#     time.sleep(2)
#     del_business_subsystems.operation_confirm()
