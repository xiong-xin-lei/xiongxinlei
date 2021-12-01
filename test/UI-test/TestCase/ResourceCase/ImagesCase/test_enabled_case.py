# 镜像管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Images_Page.list_page import list_Page
from PageObject.Resource_Page.Images_Page.enabled_page import enabled_Page


def case_test_images_enabled(self, type, version):
    index = Index_Page(self.driver)
    index.in_image()
    image = list_Page(self.driver)
    image.click_row_enabled_button(type, version)
    enabled_image = enabled_Page(self.driver)
    enabled_image.enabled_confirm()

# @allure.story('批量启用')
# def test_batch_enabled(self):
#     enabled_network = list_Page(self.driver)
#     enabled_network.click_row_checkbox()
#     enabled_network.click_enabled_button()
#     time.sleep(2)
#     enabled_network.operation_confirm()
