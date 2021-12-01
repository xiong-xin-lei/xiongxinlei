import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class edit_Page(SeleniumBase):
    loc_edit = (By.XPATH, '/html/body/div[5]/div[2]/iframe')

    loc_name = (By.XPATH, '//*[@id="edit"]/div[1]/form/div[1]/div[2]/div/div/div/input')
    loc_topology = (By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[2]/div/div/div/div[2]/input')
    loc_clusters = (By.XPATH, '/html/body/div[1]/div[1]/form/div[5]/div/div/div/div/div/input')
    loc_description = (By.TAG_NAME, 'textarea')
    loc_select_close = (By.CLASS_NAME, 'el-icon-close')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[5]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[5]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[5]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')

    topology_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    clusters_select_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')



    def edit_in(self):
        self.logger.info("进入编辑页面")
        self.select_frame(self.loc_edit)

    def send_name(self, name):
        self.logger.info('清空文本框')
        self.clear_element_text(self.loc_name)
        self.logger.info(f"输入网段名称{name}")
        self.send_keys(self.loc_name, name)

    def select_topology(self, topology):
        self.logger.info('清空选择')
        close_num = len(self.find_elements(self.loc_select_close))
        num = 0
        for i in range(close_num):
            num = num + 1
            if num == close_num:
                time.sleep(0.2)
                self.click_element((By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[2]/div/div/div/div[1]/span/span/i'))
            else:
                time.sleep(0.2)
                self.click_element((By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[2]/div/div/div/div[1]/span/span[1]/i'))
        self.logger.info(f"选择拓扑结构{topology}")
        self.click_element(self.loc_topology)
        self.logger.info('元素定位')
        if topology != 'null':
            topologyArr = topology.split(";")
            for i in range(len(topologyArr)):
                order = self.get_items_text(self.topology_select_ul, topologyArr[i])
                checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
                self.logger.info("选择拓扑结构")
                self.click_element(checked)
        else:
            self.click_element(self.loc_save)
        self.click_element(self.div_click)

    def select_clusters(self, clusters):
        self.logger.info(f"选择关联集群{clusters}")
        self.click_element(self.loc_clusters)
        self.logger.info('元素定位')
        if clusters != 'null':
            order = self.get_items_text(self.clusters_select_ul, clusters)
            checked = (By.XPATH, f'/html/body/div[4]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择关联集群")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_description(self, description):
        self.logger.info('清空文本框')
        self.clear_element_text(self.loc_description)
        self.logger.info(f"输入描述{description}")
        self.send_keys(self.loc_description, description)

    def click_save_button(self):
        self.logger.info("点击保存按钮")
        self.click_element(self.loc_save)

    def click_cancel_button(self):
        self.logger.info("点击取消按钮")
        self.click_element(self.loc_cancel)

    def click_operation_confirm(self):
        self.logger.info('点击弹窗确认')
        self.click_element(self.operation_confirm)

    def click_operation_cancel(self):
        self.logger.info('点击弹窗取消')
        self.click_element(self.operation_cancel)

    def click_operation_close(self):
        self.logger.info('点击弹窗关闭')
        self.click_element(self.operation_close)

    def is_edit_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        time.sleep(5)
        return self.get_element_text(self.loc_message)

    def edit(self, name, topology, cluster, description):
        self.send_name(name)
        self.select_topology(topology)
        self.select_clusters(cluster)
        self.send_description(description)
        self.click_save_button()
