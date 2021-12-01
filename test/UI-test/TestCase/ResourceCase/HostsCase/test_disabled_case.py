# 主机管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.disabled_page import disabled_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_disabled(self, hostIp):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.click_row_disabled_button(hostIp)
    disabled_hosts = disabled_Page(self.driver)
    disabled_hosts.disabled_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_hosts = list_Page(self.driver)
#     disabled_hosts.click_row_checkbox()
#     disabled_hosts.click_disabled_button()
#     time.sleep(2)
#     disabled_hosts.operation_confirm()
