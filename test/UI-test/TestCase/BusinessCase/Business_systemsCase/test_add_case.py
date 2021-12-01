# 业务系统新增测试用例

import allure
from PageObject.Business_Page.Business_systems_Page.add_page import add_Page
from PageObject.Business_Page.Business_systems_Page.list_page import list_Page
from PageObject.Common_Page.index_page import Index_Page


def case_test_business_system_new(self, name, description, assert_type):
    index = Index_Page(self.driver)
    index.in_business_system()
    business_system = list_Page(self.driver)
    business_system.click_new_button()
    add_business_system = add_Page(self.driver)
    add_business_system.add_in()

    with allure.step("输入信息："):
        allure.attach(name, '业务系统名称')
        allure.attach(description, '描述')
    add_business_system.add(name, description)

    if assert_type == '1':
        assert add_business_system.is_add_element(), '校验成功'
        add_business_system.click_operation_confirm()
        assert True, '新增成功'

    elif assert_type == "2":
        text = add_business_system.name_error()
        assert "请输入" in text, text

    elif assert_type == "3":
        assert add_business_system.is_add_element(), '校验成功'
        add_business_system.click_operation_confirm()
        text = add_business_system.get_message_text()
        assert "已存在" in text, text

    else:
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
