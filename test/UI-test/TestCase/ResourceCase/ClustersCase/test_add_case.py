# 集群管理新增测试用例

import allure

from PageObject.Common_Page.index_page import Index_Page
from PageObject.Resource_Page.Clusters_Page.add_page import add_Page
from PageObject.Resource_Page.Clusters_Page.list_page import list_Page


def case_test_clusters_new(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description, assert_type):
    index = Index_Page(self.driver)
    index.in_cluster()
    cluster = list_Page(self.driver)
    cluster.click_new_button()
    add_clusters = add_Page(self.driver)
    add_clusters.add_in()

    with allure.step("输入信息："):
        allure.attach(business_area, '所属业务区')
        allure.attach(name, '集群名称')
        allure.attach(haTag, '高可用标签')
        allure.attach(nfsIp, 'NFS地址')
        allure.attach(nfsSource, 'NFS源目录')
        allure.attach(defServs, '包含软件')
        allure.attach(description, '描述')
    add_clusters.add(business_area, name, haTag, nfsIp, nfsSource, defServs, description)

    if assert_type == '1':
        assert add_clusters.is_add_element(), '校验成功'
        add_clusters.click_operation_confirm()
        assert True, '新增成功'

    elif assert_type == "2":
        text = add_clusters.not_empty_determine()
        assert "请" in text, text

    elif assert_type == "3":
        assert add_clusters.is_add_element(), '校验成功'
        add_clusters.click_operation_confirm()
        text = add_clusters.get_message_text()
        assert "已存在" in text, text

    elif assert_type == "4":
        text = add_clusters.not_empty_determine()
        assert "请" in text, text

    else:
        print(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
