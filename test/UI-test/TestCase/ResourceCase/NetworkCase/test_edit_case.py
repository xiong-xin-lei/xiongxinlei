# 网段管理测试用例
import time

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Network_Page.list_page import list_Page
from PageObject.Resource_Page.Network_Page.edit_page import edit_Page


def case_test_network_edit(self, key, name, topology, cluster, description, assert_type):
    index = Index_Page(self.driver)
    index.in_network()
    time.sleep(2)
    network = list_Page(self.driver)
    network.button_move_over(key)
    network.click_row_edit_button()
    edit_network = edit_Page(self.driver)
    edit_network.edit_in()

    with allure.step("输入信息："):
        allure.attach(name, '网段名称')
        allure.attach(topology, '网络拓扑')
        allure.attach(cluster, '关联集群')
        allure.attach(description, '描述')
    edit_network.edit(name, topology, cluster, description)
    assert edit_network.is_edit_element(), '校验成功'
    edit_network.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    elif assert_type == "2":
        text = edit_network.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "不能为空" in text, text

    elif assert_type == "3":
        text = edit_network.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "已存在" in text, text

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
