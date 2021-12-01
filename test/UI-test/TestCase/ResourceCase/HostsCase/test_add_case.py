# 主机管理新增测试用例

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.add_page import add_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_new(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,
            hotSpare, description, assert_type):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.click_new_button()
    add_hosts = add_Page(self.driver)
    add_hosts.add_in()

    with allure.step("输入信息："):
        allure.attach(business_area, '所属业务区')
        allure.attach(clusters, '所属集群')
        allure.attach(hostIp, '主机IP')
        allure.attach(room, '机房')
        allure.attach(seat, '机位')
        allure.attach(hddPaths, '机械盘设备')
        allure.attach(ssdPaths, '固态盘设备')
        allure.attach(remoteStorageId, '外置存储')
        allure.attach(maxUnitCnt, '容器上限')
        allure.attach(hotSpare, '备选')
        allure.attach(description, '描述')
    add_hosts.add(business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,
                  hotSpare, description)

    if assert_type == '1':
        allure_title = "添加成功"
        allure.dynamic.title(allure_title)
        assert add_hosts.is_add_element(), '校验成功'
        add_hosts.click_operation_confirm()
        assert True, '新增成功'

    elif assert_type == "2":
        text = add_hosts.non_empty_error()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "请" or "输" in text, text

    elif assert_type == "3":
        allure_title = "主机IP重复"
        allure.dynamic.title(allure_title)
        assert add_hosts.is_add_element(), '校验成功'
        add_hosts.click_operation_confirm()
        text = add_hosts.get_message_text()
        assert "已存在" in text, text

    elif assert_type == "4":
        text = add_hosts.non_empty_error()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "请输入" in text, text

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
