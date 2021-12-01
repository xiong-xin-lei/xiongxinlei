# 镜像管理测试用例
from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Images_Page.list_page import list_Page
from PageObject.Resource_Page.Images_Page.disabled_page import disabled_Page


def case_test_images_disabled(self, type, version):
    index = Index_Page(self.driver)
    index.in_image()
    image = list_Page(self.driver)
    image.click_row_disabled_button(type, version)
    disabled_image = disabled_Page(self.driver)
    disabled_image.disabled_confirm()

# @allure.story('批量停用')
# def test_batch_enabled(self):
#     disabled_images = list_Page(self.driver)
#     disabled_images.click_row_checkbox()
#     disabled_images.click_disabled_button()
#     time.sleep(2)
#     disabled_images.operation_confirm()
