# 集群管理测试用例
import time

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Clusters_Page.edit_page import edit_Page
from PageObject.Resource_Page.Clusters_Page.list_page import list_Page


def case_test_clusters_edit(self, key, business_area, name, haTag, defServs, description, assert_type):
    index = Index_Page(self.driver)
    index.in_cluster()
    clusters = list_Page(self.driver)
    clusters.button_move_over(key)
    clusters.click_row_edit_button()
    edit_clusters = edit_Page(self.driver)
    edit_clusters.edit_in()


    with allure.step("输入信息："):
        allure.attach(business_area, '所属业务区')
        allure.attach(name, '集群名称')
        allure.attach(haTag, '高可用标签')
        allure.attach(defServs, '包含软件')
        allure.attach(description, '描述')
    edit_clusters.edit(business_area, name, haTag, defServs, description)
    assert edit_clusters.is_edit_element(), '校验成功'
    edit_clusters.click_operation_confirm()

    if assert_type == '1':
        allure_title = "编辑成功"
        allure.dynamic.title(allure_title)
        assert True, allure_title

    elif assert_type == "2":
        text = edit_clusters.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "不能为空" in text, text


    elif assert_type == "3":
        text = edit_clusters.get_message_text()
        allure_title = text
        allure.dynamic.title(allure_title)
        assert "已存在" in text, text

    else:
        allure_title = "未知错误"
        allure.dynamic.title(allure_title)
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
