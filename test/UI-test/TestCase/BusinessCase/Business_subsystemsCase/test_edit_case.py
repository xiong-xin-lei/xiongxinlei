# 业务子系统管理测试用例
import time

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Business_Page.Business_subsystems_Page.edit_page import edit_Page
from PageObject.Business_Page.Business_subsystems_Page.list_page import list_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page as sysList_Page


def case_test_business_subsystems_edit(self, system, key, name, description, assert_type):
    index = Index_Page(self.driver)
    index.in_business_system()
    business_system = sysList_Page(self.driver)
    business_system.click_row_subsystems_button(system)
    business_system.unselect_frame()
    time.sleep(1)
    business_subsystems = list_Page(self.driver)
    business_subsystems.list_in()
    business_subsystems.button_move_over(key)
    business_subsystems.click_row_edit_button()
    edit_business_subsystems = edit_Page(self.driver)
    edit_business_subsystems.edit_in()
    time.sleep(1)

    with allure.step("输入信息："):
        allure.attach(name, '业务子系统名称')
        allure.attach(description, '描述')
    edit_business_subsystems.edit(name, description)
    assert edit_business_subsystems.is_edit_element(), '校验成功'
    edit_business_subsystems.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    elif assert_type == "2":
        allure_title = "业务区名称不能为空"
        allure.dynamic.title(allure_title)
        text = edit_business_subsystems.get_message_text()
        assert "不能为空" in text, text

    elif assert_type == "3":
        allure_title = "业务区名称重复"
        allure.dynamic.title(allure_title)
        text = edit_business_subsystems.get_message_text()
        assert "已存在" in text, text

    else:
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
