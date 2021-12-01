# 主机管理测试用例

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.before_edit_page import before_edit_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_before_edit(self, key, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths,
                                remoteStorageId, maxUnitCnt, hotSpare, description, assert_type):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_edit_button()
    edit_hosts = before_edit_Page(self.driver)
    edit_hosts.edit_in()

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
    edit_hosts.edit(business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,
                  hotSpare, description)
    assert edit_hosts.is_edit_element(), '校验成功'
    edit_hosts.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    elif assert_type == "2":
        text = edit_hosts.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "不能为空" in text, text

    elif assert_type == "3":
        text = edit_hosts.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "已存在" in text, text

    elif assert_type == "4":
        text = edit_hosts.non_empty_error()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "请" in text, text

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"