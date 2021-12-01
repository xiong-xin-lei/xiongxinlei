# 业务区管理测试用例
import time

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Business_areas_Page.edit_page import edit_Page
from PageObject.Resource_Page.Business_areas_Page.list_page import list_Page


def case_test_business_areas_edit(self, key, name, description, assert_type):
    index = Index_Page(self.driver)
    index.in_business_areas()
    business_areas = list_Page(self.driver)
    business_areas.button_move_over(key)
    business_areas.click_row_edit_button()
    edit_business_areas = edit_Page(self.driver)
    edit_business_areas.edit_in()
    time.sleep(1)

    with allure.step("输入信息："):
        allure.attach(name, '业务区名称')
        allure.attach(description, '描述')
    edit_business_areas.edit(name, description)
    assert edit_business_areas.is_edit_element(), '校验成功'
    edit_business_areas.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    elif assert_type == "2":
        allure_title = "业务区名称不能为空"
        allure.dynamic.title(allure_title)
        text = edit_business_areas.get_message_text()
        assert "不能为空" in text, text

    elif assert_type == "3":
        allure_title = "业务区名称重复"
        allure.dynamic.title(allure_title)
        text = edit_business_areas.get_message_text()
        assert "已存在" in text, text

    else:
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
