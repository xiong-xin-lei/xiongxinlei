# 网段管理测试用例


from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Network_Page.del_page import del_Page
from PageObject.Resource_Page.Network_Page.list_page import list_Page


def case_test_network_del(self, name):
    index = Index_Page(self.driver)
    index.in_network()
    network = list_Page(self.driver)
    network.button_move_over(name)
    network.click_row_del_button()
    del_network = del_Page(self.driver)
    del_network.del_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_network = list_Page(self.driver)
#     del_network.click_row_checkbox()
#     del_network.click_del_button()
#     time.sleep(2)
#     del_network.operation_confirm()
