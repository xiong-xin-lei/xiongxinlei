# 规模管理新增测试用例
import time

import allure
from PageObject.Business_Page.Scales_Page.add_page import add_Page
from PageObject.Business_Page.Scales_Page.list_page import list_Page
from PageObject.Common_Page.index_page import Index_Page


def case_test_scales_new(self, type, cpu, mem, assert_type):
    index = Index_Page(self.driver)
    index.in_scale()
    scales = list_Page(self.driver)
    time.sleep(5)
    scales.click_new_button()
    add_scales = add_Page(self.driver)
    add_scales.add_in()

    with allure.step("输入信息："):
        allure.attach(type, '类型')
        allure.attach(cpu, 'CPU')
        allure.attach(mem, '内存')
    add_scales.add(type, cpu, mem)

    if assert_type == '1':
        assert add_scales.is_add_element(), '校验成功'
        add_scales.click_operation_confirm()
        assert True, '新增成功'

    elif assert_type == "2":
        text = add_scales.name_error()
        assert "请输入" in text, text

    elif assert_type == "3":
        assert add_scales.is_add_element(), '校验成功'
        add_scales.click_operation_confirm()
        text = add_scales.get_message_text()
        assert "已存在" in text, text

    else:
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
