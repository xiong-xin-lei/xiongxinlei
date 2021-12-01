# 业务系统管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page
from PageObject.Business_Page.Business_systems_Page.confirm_page import confirm_Page


def case_test_business_system_disabled(self, name):
    index = Index_Page(self.driver)
    index.in_business_system()
    Business_system = list_Page(self.driver)
    flag = Business_system.is_disabled_disable(name)
    if flag != 'true':
        Business_system.click_row_disabled_button(name)
        disabled_Business_system = confirm_Page(self.driver)
        disabled_Business_system.click_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_Business_system = list_Page(self.driver)
#     disabled_Business_system.click_row_checkbox()
#     disabled_Business_system.click_disabled_button()
#     time.sleep(2)
#     disabled_Business_system.operation_confirm()
