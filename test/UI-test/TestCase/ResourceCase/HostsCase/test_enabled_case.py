# 主机管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.enabled_page import enabled_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_enabled(self, hostIp):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.click_row_enabled_button(hostIp)
    disabled_hosts = enabled_Page(self.driver)
    disabled_hosts.enabled_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_hosts = list_Page(self.driver)
#     enabled_hosts.click_row_checkbox()
#     enabled_hosts.click_enabled_button()
#     time.sleep(2)
#     enabled_hosts.operation_confirm()
