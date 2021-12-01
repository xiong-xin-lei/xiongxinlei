# 集群管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Clusters_Page.enabled_page import enabled_Page
from PageObject.Resource_Page.Clusters_Page.list_page import list_Page


def case_test_clusters_enabled(self,name):
    index = Index_Page(self.driver)
    index.in_cluster()
    clusters = list_Page(self.driver)
    clusters.click_row_enabled_button(name)
    enabled_clusters = enabled_Page(self.driver)
    enabled_clusters.enabled_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_clusters = list_Page(self.driver)
#     enabled_clusters.click_row_checkbox()
#     enabled_clusters.click_enabled_button()
#     time.sleep(2)
#     enabled_clusters.operation_confirm()
