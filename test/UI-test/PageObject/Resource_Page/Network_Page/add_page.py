import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class add_Page(SeleniumBase):
    loc_add = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_name = (By.XPATH, '//input[@placeholder="请输入网段名称"]')
    loc_startIp = (By.XPATH, '//input[@placeholder="请输入起始IP"]')
    loc_endIp = (By.XPATH, '//input[@placeholder="请输入结束IP"]')
    loc_gateway = (By.XPATH, '//input[@placeholder="请输入网关"]')
    loc_netmask = (By.XPATH, '//input[@placeholder="请输入掩码"]')
    loc_vlan = (By.XPATH, '//input[@placeholder="请输入VLAN"]')
    loc_topology = (By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[2]/div/div/div/div[2]/input')
    loc_clusters = (By.XPATH, '/html/body/div[1]/div[1]/form/div[5]/div[1]/div/div/div/div[1]/input')
    loc_description = (By.TAG_NAME, 'textarea')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[3]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[5]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[3]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')
    loc_error = (By.CLASS_NAME, 'el-form-item__error')

    topology_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    clusters_select_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    def add_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_add)

    def send_name(self, name):
        self.logger.info(f"输入网段名称{name}")
        self.send_keys(self.loc_name, name)

    def send_startIp(self, startIp):
        self.logger.info(f"输入起始IP{startIp}")
        self.send_keys(self.loc_startIp, startIp)

    def send_endIp(self, endIp):
        self.logger.info(f"输入结束IP{endIp}")
        self.send_keys(self.loc_endIp, endIp)

    def send_gateway(self, gateway):
        self.logger.info(f"输入网关{gateway}")
        self.send_keys(self.loc_gateway, gateway)

    def send_vlan(self, vlan):
        self.logger.info(f"输入VLAN{vlan}")
        self.send_keys(self.loc_vlan, vlan)

    def send_netmask(self, netmask):
        self.logger.info(f"输入掩码{netmask}")
        self.send_keys(self.loc_netmask, netmask)

    def select_topology(self, topology):
        self.logger.info(f"选择拓扑结构{topology}")
        self.click_element(self.loc_topology)
        self.logger.info('元素定位')
        # print(topology, 'null', topology != 'null')
        if topology != 'null':
            topologyArr = topology.split(";")
            for i in range(len(topologyArr)):
                order = self.get_items_text(self.topology_select_ul, topologyArr[i])
                checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
                self.logger.info("选择拓扑结构")
                self.click_element(checked)
        else:
            self.click_element(self.div_click)
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

    def is_add_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        self.logger.info("返回Message消息")
        time.sleep(3)
        return self.get_element_text(self.loc_message)

    def not_empty_determine(self):
        self.logger.info("返回非空验证信息")
        return self.get_element_text(self.loc_error)

    def add(self, name, startIp, endIp, gateway, netmask, vlan, topology, clusters, description):
        self.send_name(name)
        self.send_startIp(startIp)
        self.send_endIp(endIp)
        self.send_gateway(gateway)
        self.send_netmask(netmask)
        self.send_vlan(vlan)
        self.select_topology(topology)
        self.select_clusters(clusters)
        self.send_description(description)
        self.click_save_button()
