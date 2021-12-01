# 主机管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.monitoring_Page import monitoring_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_monitoring(self, key):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_monitoring()
