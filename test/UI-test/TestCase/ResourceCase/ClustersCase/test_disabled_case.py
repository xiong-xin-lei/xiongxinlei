# 集群管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Clusters_Page.list_page import list_Page
from PageObject.Resource_Page.Clusters_Page.disabled_page import disabled_Page


def case_test_clusters_disabled(self, name):
    index = Index_Page(self.driver)
    index.in_cluster()
    clusters = list_Page(self.driver)
    clusters.click_row_disabled_button(name)
    disabled_clusters = disabled_Page(self.driver)
    disabled_clusters.disabled_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_clusters = list_Page(self.driver)
#     disabled_clusters.click_row_checkbox()
#     disabled_clusters.click_disabled_button()
#     time.sleep(2)
#     disabled_clusters.operation_confirm()
