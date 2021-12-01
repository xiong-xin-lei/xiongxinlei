# 业务区管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Business_areas_Page.list_page import list_Page
from PageObject.Resource_Page.Business_areas_Page.disabled_page import disabled_Page


def case_test_business_areas_disabled(self, name):
    index = Index_Page(self.driver)
    index.in_business_areas()
    business_areas = list_Page(self.driver)
    flag = business_areas.is_disabled_disable(name)
    # if flag != 'true':
    #     business_areas.click_row_disabled_button(name)
    #     disabled_business_areas = disabled_Page(self.driver)
    #     disabled_business_areas.disabled_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_business_areas = list_Page(self.driver)
#     disabled_business_areas.click_row_checkbox()
#     disabled_business_areas.click_disabled_button()
#     time.sleep(2)
#     disabled_business_areas.operation_confirm()
