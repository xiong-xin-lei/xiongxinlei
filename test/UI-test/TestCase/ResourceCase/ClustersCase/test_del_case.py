# 集群管理测试用例


from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Clusters_Page.del_page import del_Page
from PageObject.Resource_Page.Clusters_Page.list_page import list_Page


def case_test_clusters_del(self, name):
    index = Index_Page(self.driver)
    index.in_cluster()
    clusters = list_Page(self.driver)
    clusters.button_move_over(name)
    clusters.click_row_del_button()
    del_clusters = del_Page(self.driver)
    del_clusters.del_confirm()

# @allure.story('批量删除')
# def test_batch_enabled(self):
#     del_clusters = list_Page(self.driver)
#     del_clusters.click_row_checkbox()
#     del_clusters.click_del_button()
#     time.sleep(2)
#     del_clusters.operation_confirm()
