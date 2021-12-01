# 网段管理测试用例
import time

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Network_Page.list_page import list_Page
from PageObject.Resource_Page.Network_Page.enabled_page import enabled_Page


def case_test_network_enabled(self,name):
    index = Index_Page(self.driver)
    index.in_network()
    time.sleep(3)
    network = list_Page(self.driver)
    network.click_row_enabled_button(name)
    enabled_network = enabled_Page(self.driver)
    enabled_network.enabled_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_network = list_Page(self.driver)
#     enabled_network.click_row_checkbox()
#     enabled_network.click_enabled_button()
#     time.sleep(2)
#     enabled_network.operation_confirm()
