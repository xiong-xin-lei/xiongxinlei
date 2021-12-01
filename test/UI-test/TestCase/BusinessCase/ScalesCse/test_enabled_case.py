# 规模管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Scales_Page.list_page import list_Page
from PageObject.Business_Page.Scales_Page.confirm_page import confirm_Page


def case_test_scales_enabled(self, type, name):
    index = Index_Page(self.driver)
    index.in_scale()
    scales = list_Page(self.driver)
    flag = scales.is_enabled_disable(type, name)
    if flag != 'true':
        scales.click_row_enabled_button(type, name)
        enabled_scales = confirm_Page(self.driver)
        enabled_scales.click_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_scales = list_Page(self.driver)
#     enabled_scales.click_row_checkbox()
#     enabled_scales.click_enabled_button()
#     time.sleep(2)
#     enabled_scales.click_confirm()
