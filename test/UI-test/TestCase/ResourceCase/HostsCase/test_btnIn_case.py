# 主机管理测试用例
import time

import allure
import pytest

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.btnIn_page import btnIn_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_btnIn(self, key, port, userName, password):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_btnIn_button()
    btnIn_hosts = btnIn_Page(self.driver)
    btnIn_hosts.btnIn_in()

    with allure.step("输入信息："):
        allure.attach(port, '端口')
        allure.attach(userName, '用户名')
        allure.attach(password, '密码')
    btnIn_hosts.btnIn(port, userName, password)
    time.sleep(5)
    assert 'true' != btnIn_hosts.is_button_disabled(btnIn_hosts.loc_btnIn), '通过'
    btnIn_hosts.click_btnIn_button()
    time.sleep(0.1)
    assert btnIn_hosts.is_operation_element(), '校验成功'
    btnIn_hosts.click_operation_confirm()
    time.sleep(3)
    btnIn_judge_success(self, key)


# @pytest.mark.flaky(reruns=10, reruns_delay=30)
def btnIn_judge_success(self, key, num=0):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    num = num + 1
    if num != 10:
        if '中' in hosts.click_row_taskStatus(key):
            time.sleep(30)
            btnIn_judge_success(hosts, key, num)
        else:
            assert True, '出库成功'
    else:
        assert False, '出库超时'
