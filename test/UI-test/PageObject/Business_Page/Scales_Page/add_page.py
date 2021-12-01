import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class add_Page(SeleniumBase):
    loc_add = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_type = (By.XPATH, '//input[@placeholder="请选择类型"]')
    loc_cpu = (By.XPATH, '//input[@placeholder="请输入CPU数量"]')
    loc_mem = (By.XPATH, '//input[@placeholder="请输入内存容量"]')
    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    type_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    loc_message = (By.CLASS_NAME, 'el-message--error')
    loc_non_empty_error = (By.CLASS_NAME, 'el-form-item__error')

    def add_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_add)

    def add_out(self):
        self.logger.info("退出新增页面")
        self.unselect_frame()

    def select_type(self, type):
        self.logger.info(f"选择类型{type}")
        self.click_element(self.loc_type)
        self.logger.info('元素定位')
        if type != 'null':
            order = self.get_items_text(self.type_select_ul, type)
            checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择类型")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_cpu(self, cpu):
        self.logger.info(f"输入CPU数量{cpu}")
        self.send_keys(self.loc_cpu, cpu)

    def send_mem(self, mem):
        self.logger.info(f"输入内存容量{mem}")
        self.send_keys(self.loc_mem, mem)

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

    def cpu_error(self):
        self.logger.info("返回业务系统名称校验文本值")
        return self.get_element_text(self.loc_non_empty_error)

    def is_add_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        time.sleep(4)
        return self.get_element_text(self.loc_message)

    def add(self, type, cpu, mem):
        time.sleep(3)
        self.select_type(type)
        self.send_cpu(cpu)
        self.send_mem(mem)
        self.click_save_button()
