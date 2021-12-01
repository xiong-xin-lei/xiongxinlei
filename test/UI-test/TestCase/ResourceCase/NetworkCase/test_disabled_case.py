# 网段管理测试用例
import time

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Network_Page.list_page import list_Page
from PageObject.Resource_Page.Network_Page.disabled_page import disabled_Page


def case_test_network_disabled(self,name):
    index = Index_Page(self.driver)
    index.in_network()
    time.sleep(3)
    network = list_Page(self.driver)
    network.click_row_disabled_button(name)
    disabled_network = disabled_Page(self.driver)
    disabled_network.disabled_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_network = list_Page(self.driver)
#     disabled_network.click_row_checkbox()
#     disabled_network.click_disabled_button()
#     time.sleep(2)
#     disabled_network.operation_confirm()
