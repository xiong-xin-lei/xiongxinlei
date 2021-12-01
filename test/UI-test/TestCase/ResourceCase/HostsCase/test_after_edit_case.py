# 主机管理测试用例

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.after_edit_page import after_edit_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_after_edit(self, key, hotSpare, description, assert_type):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_edit_button()
    edit_hosts = after_edit_Page(self.driver)
    edit_hosts.edit_in()

    with allure.step("输入信息："):
        allure.attach(hotSpare, '备选')
        allure.attach(description, '描述')
    edit_hosts.edit(hotSpare, description)
    assert edit_hosts.is_edit_element(), '校验成功'
    edit_hosts.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"