# 业务区管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Business_areas_Page.list_page import list_Page
from PageObject.Resource_Page.Business_areas_Page.enabled_page import enabled_Page


def case_test_business_areas_enabled(self, name):
    index = Index_Page(self.driver)
    index.in_business_areas()
    business_areas = list_Page(self.driver)
    flag = business_areas.is_enabled_disable(name)
    if flag != 'true':
        business_areas.click_row_enabled_button(name)
        enabled_business_areas = enabled_Page(self.driver)
        enabled_business_areas.enabled_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_business_areas = list_Page(self.driver)
#     enabled_business_areas.click_row_checkbox()
#     enabled_business_areas.click_enabled_button()
#     time.sleep(2)
#     enabled_business_areas.operation_confirm()
