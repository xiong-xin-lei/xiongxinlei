import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class after_edit_Page(SeleniumBase):
    loc_edit = (By.XPATH, '/html/body/div[5]/div[2]/iframe')

    loc_hotSpare = (By.XPATH, '/html/body/div[1]/div[1]/form/div[2]/div/div')
    loc_description = (By.TAG_NAME, 'textarea')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[3]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[3]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[3]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')
    loc_non_empty_error = (By.CLASS_NAME, 'el-form-item__error')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    business_area_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    clusters_select_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')

    def edit_in(self):
        self.logger.info("进入入库后编辑页面")
        self.select_frame(self.loc_edit)

    def select_hotSpare(self, hotSpare):
        self.logger.info(f"选择备选{hotSpare}")
        if hotSpare == 'true':
            if self.get_element_text(self.loc_hotSpare) == '是':
                self.click_element(self.div_click)
            else:
                self.click_element(self.loc_hotSpare)
        else:
            if self.get_element_text(self.loc_hotSpare) == '是':
                self.click_element(self.loc_hotSpare)
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

    def non_empty_error(self):
        self.logger.info("返回非空校验文本")
        return self.get_element_text(self.loc_non_empty_error)

    def is_edit_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        time.sleep(3)
        return self.get_element_text(self.loc_message)

    def edit(self, hotSpare, description):
        self.select_hotSpare(hotSpare)
        self.send_description(description)
        self.click_save_button()
