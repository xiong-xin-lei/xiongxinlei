# 网段管理新增测试用例
import time

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Network_Page.list_page import list_Page
from PageObject.Resource_Page.Network_Page.add_page import add_Page


def case_test_network_new(self, name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description,
                     assert_type):
    index = Index_Page(self.driver)
    index.in_network()
    time.sleep(3)
    network = list_Page(self.driver)
    network.click_new_button()
    add_network = add_Page(self.driver)
    add_network.add_in()

    with allure.step("输入信息："):
        allure.attach(name, '网段名称')
        allure.attach(startIp, '起始IP')
        allure.attach(endIp, '结束IP')
        allure.attach(gateway, '网关')
        allure.attach(netmask, '掩码')
        allure.attach(vlan, 'VLAN')
        allure.attach(topology, '拓扑结构')
        allure.attach(clusters, '关联集群')
        allure.attach(description, '描述')
    add_network.add(name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description)

    if assert_type == '1':
        allure_title = "添加成功"
        allure.dynamic.title(allure_title)
        assert add_network.is_add_element(), '校验成功'
        add_network.click_operation_confirm()
        assert True, '新增成功'

    elif assert_type == "2":
        text = add_network.not_empty_determine()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "请" in text, text

    elif assert_type == "3":
        allure_title = "集群名称重复"
        allure.dynamic.title(allure_title)
        assert add_network.is_add_element(), '校验成功'
        add_network.click_operation_confirm()
        text = add_network.get_message_text()
        assert "已存在" in text, text

    elif assert_type == "4":
        text = add_network.not_empty_determine()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "请" in text, text

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
