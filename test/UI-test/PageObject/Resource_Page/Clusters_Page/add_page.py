import time

from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

from Common.selenium_library import SeleniumBase


class add_Page(SeleniumBase):
    loc_add = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_business_area = (By.XPATH, '/html/body/div[1]/div[1]/form/div[1]/div[2]/div/div/div/div[1]/input')
    loc_name = (By.XPATH, '//input[@placeholder="请输入集群名称"]')
    loc_haTag = (By.XPATH, '//input[@placeholder="请输入高可用标签"]')
    loc_nfsIp = (By.XPATH, '//input[@placeholder="请输入NFS地址"]')
    loc_nfsSource = (By.XPATH, '//input[@placeholder="请输入NFS源目录"]')
    loc_defServs = (By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div/div/div/div[2]/input')
    loc_description = (By.TAG_NAME, 'textarea')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[5]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[5]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[5]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')
    loc_error = (By.CLASS_NAME, 'el-form-item__error')

    business_area_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    defServs_select_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    def add_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_add)

    def select_business_area(self, business_area):
        self.logger.info(f"选择所属业务区{business_area}")
        self.click_element(self.loc_business_area)
        self.logger.info('元素定位')
        if business_area != 'null':
            order = self.get_items_text(self.business_area_select_ul, business_area)
            checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择所属业务区")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_name(self, name):
        self.logger.info(f"输入集群名称{name}")
        self.send_keys(self.loc_name, name)

    def send_haTag(self, haTag):
        self.logger.info(f"输入高可用标签{haTag}")
        self.send_keys(self.loc_haTag, haTag)

    def send_nfsIp(self, nfsIp):
        self.logger.info(f"输入NFS地址{nfsIp}")
        self.send_keys(self.loc_nfsIp, nfsIp)

    def send_nfsSource(self, nfsSource):
        self.logger.info(f"输入NFS源目录{nfsSource}")
        self.send_keys(self.loc_nfsSource, nfsSource)

    def select_defServs(self, defServs):
        self.logger.info(f"选择包含软件{defServs}")
        self.click_element(self.loc_defServs)
        self.logger.info('元素定位')
        if defServs != 'null':
            defServsArr = defServs.split(";")
            for i in range(len(defServsArr)):
                order = self.get_items_text(self.defServs_select_ul, defServsArr[i])
                checked = (By.XPATH, f'/html/body/div[4]/div[1]/div[1]/ul/li[{order}]')
                self.logger.info("点击所包含软件")
                self.click_element(checked)
        else:
            self.click_element(self.loc_save)
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
        time.sleep(2)
        return self.get_element_text(self.loc_message)

    def not_empty_determine(self):
        self.logger.info("返回非空验证信息")
        time.sleep(0.3)
        return self.get_element_text(self.loc_error)

    def add(self, business_area, name, haTag, nfsIp, nfsSource, defServs, description):
        self.select_business_area(business_area)
        self.send_name(name)
        self.send_haTag(haTag)
        self.send_nfsIp(nfsIp)
        self.send_nfsSource(nfsSource)
        self.select_defServs(defServs)
        self.send_description(description)
        self.click_save_button()
