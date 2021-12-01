# 主机管理测试用例

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Hosts_Page.del_page import del_Page
from PageObject.Resource_Page.Hosts_Page.list_page import list_Page


def case_test_hosts_del(self, key):
    index = Index_Page(self.driver)
    index.in_host()
    hosts = list_Page(self.driver)
    hosts.button_move_over(key)
    hosts.click_row_del_button()
    del_hosts = del_Page(self.driver)
    del_hosts.del_confirm()

# @allure.story('注销删除')
# def test_batch_enabled(self):
#     del_hosts = list_Page(self.driver)
#     del_hosts.click_row_checkbox()
#     del_hosts.click_del_button()
#     time.sleep(2)
#     del_hosts.operation_confirm()
