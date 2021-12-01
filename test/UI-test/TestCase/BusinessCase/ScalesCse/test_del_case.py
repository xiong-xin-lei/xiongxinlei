# 规模管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Scales_Page.confirm_page import confirm_Page
from PageObject.Business_Page.Scales_Page.list_page import list_Page


def case_test_scales_del(self, type, name):
    index = Index_Page(self.driver)
    index.in_scale()
    scales = list_Page(self.driver)
    flag = scales.is_del_disable(type, name)
    if flag != 'true':
        scales.click_row_del_button(type, name)
        disabled_scales = confirm_Page(self.driver)
        disabled_scales.click_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_scales = list_Page(self.driver)
#     del_scales.click_row_checkbox()
#     del_scales.click_del_button()
#     time.sleep(2)
#     del_scales.operation_confirm()
