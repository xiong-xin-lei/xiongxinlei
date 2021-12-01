import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class add_Page(SeleniumBase):
    loc_add = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_name = (By.XPATH, '//input[@placeholder="请输入业务系统名称"]')
    loc_description = (By.TAG_NAME, 'textarea')
    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[2]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[2]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[2]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message--error')
    loc_non_empty_error = (By.CLASS_NAME, 'el-form-item__error')

    def add_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_add)

    def add_out(self):
        self.logger.info("退出新增页面")
        self.unselect_frame()

    def send_name(self, name):
        self.logger.info(f"输入业务系统名称{name}")
        self.send_keys(self.loc_name, name)

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

    def name_error(self):
        self.logger.info("返回业务系统名称校验文本值")
        return self.get_element_text(self.loc_non_empty_error)

    def is_add_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        time.sleep(4)
        return self.get_element_text(self.loc_message)

    def add(self, name, description):
        self.send_name(name)
        self.send_description(description)
        self.click_save_button()
