# 业务系统管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page
from PageObject.Business_Page.Business_systems_Page.confirm_page import confirm_Page


def case_test_business_system_enabled(self, name):
    index = Index_Page(self.driver)
    index.in_business_system()
    Business_system = list_Page(self.driver)
    flag = Business_system.is_enabled_disable(name)
    if flag != 'true':
        Business_system.click_row_enabled_button(name)
        enabled_Business_system = confirm_Page(self.driver)
        enabled_Business_system.click_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_Business_system = list_Page(self.driver)
#     enabled_Business_system.click_row_checkbox()
#     enabled_Business_system.click_enabled_button()
#     time.sleep(2)
#     enabled_Business_system.click_confirm()
