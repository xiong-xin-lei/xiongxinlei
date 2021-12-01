# 镜像管理测试用例
import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Images_Page.config_page import config_Page
from PageObject.Resource_Page.Images_Page.list_page import list_Page


def case_test_images_config(self, type, version, key, value, defaultValue, range, canSet, mustRestart, description,
                            assert_type):
    index = Index_Page(self.driver)
    index.in_image()
    image = list_Page(self.driver)
    image.click_row_config_button(type, version)
    image.unselect_frame()
    config = config_Page(self.driver)
    config.config_in()

    with allure.step("输入信息："):
        allure.attach(key, '键')
        allure.attach(value, '值')
        allure.attach(defaultValue, '默认值')
        allure.attach(range, '范围')
        allure.attach(canSet, '允许编辑')
        allure.attach(mustRestart, '重启生效')
        allure.attach(description, '描述')
    config.edit(key, value, defaultValue, range, canSet, mustRestart, description)
    assert config.is_edit_element(), '校验成功'
    config.click_operation_confirm()
