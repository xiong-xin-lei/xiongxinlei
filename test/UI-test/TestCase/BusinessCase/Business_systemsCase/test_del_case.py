# 业务系统管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_systems_Page.confirm_page import confirm_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page


def case_test_business_system_del(self, name):
    index = Index_Page(self.driver)
    index.in_business_system()
    business_areas = list_Page(self.driver)
    business_areas.button_move_over(name)
    business_areas.click_row_del_button()
    del_business_areas = confirm_Page(self.driver)
    del_business_areas.click_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_business_areas = list_Page(self.driver)
#     del_business_areas.click_row_checkbox()
#     del_business_areas.click_del_button()
#     time.sleep(2)
#     del_business_areas.operation_confirm()
