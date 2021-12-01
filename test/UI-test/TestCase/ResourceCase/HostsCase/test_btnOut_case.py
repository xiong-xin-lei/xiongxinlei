# 主机管理测试用例
import time

import allure
import pytest

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.btnOut_page import btnOut_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_btnOut(self, key, port, userName, password):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_btnOut_button()
    btnOut_hosts = btnOut_Page(self.driver)
    btnOut_hosts.btnOut_in()

    with allure.step("输入信息："):
        allure.attach(port, '端口')
        allure.attach(userName, '用户名')
        allure.attach(password, '密码')
    btnOut_hosts.btnOut(port, userName, password)
    time.sleep(3)
    assert 'true' != btnOut_hosts.is_button_disabled(btnOut_hosts.loc_btnOut), '通过'
    btnOut_hosts.click_btnOut_button()
    time.sleep(0.1)
    assert btnOut_hosts.is_operation_element(), '校验成功'
    btnOut_hosts.click_operation_confirm()
    time.sleep(5)
    btnOut_judge_success(self, key)


# @pytest.mark.flaky(reruns=10, reruns_delay=30)
def btnOut_judge_success(self, key, num=0):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    num = num + 1
    if num != 10:
        if '中' in hosts.click_row_taskStatus(key):
            time.sleep(30)
            btnOut_judge_success(hosts, key, num)
        elif '失败' in hosts.click_row_taskStatus(key):
            assert False, '出库失败'
        else:
            assert True, '出库成功'
    else:
        assert False, '出库超时'
