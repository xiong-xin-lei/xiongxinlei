# 规模管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Scales_Page.list_page import list_Page
from PageObject.Business_Page.Scales_Page.confirm_page import confirm_Page


def case_test_scales_disabled(self, type, name):
    index = Index_Page(self.driver)
    index.in_scale()
    scales = list_Page(self.driver)
    flag = scales.is_disabled_disable(type, name)
    if flag != 'true':
        scales.click_row_disabled_button(type, name)
        disabled_scales = confirm_Page(self.driver)
        disabled_scales.click_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_scales = list_Page(self.driver)
#     disabled_scales.click_row_checkbox()
#     disabled_scales.click_disabled_button()
#     time.sleep(2)
#     disabled_scales.operation_confirm()
