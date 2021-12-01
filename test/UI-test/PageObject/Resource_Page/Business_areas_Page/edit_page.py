import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class edit_Page(SeleniumBase):
    loc_edit = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_name = (By.XPATH, '/html/body/div[1]/div[1]/form/div[2]/div/div/input')
    loc_name_error = (By.XPATH, '//*[@id="add"]/div[1]/form/div[2]/div/div[2]')
    loc_description = (By.TAG_NAME, 'textarea')
    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[3]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[3]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[3]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')

    def edit_in(self):
        self.logger.info("进入编辑页面")
        self.select_frame(self.loc_edit)

    def edit_out(self):
        self.logger.info("退出编辑页面")
        self.unselect_frame()

    def send_name(self, name):
        self.logger.info('清空文本框')
        self.clear_element_text(self.loc_name)
        self.logger.info(f"输入业务区名称{name}")
        self.send_keys(self.loc_name, name)

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

    def edit(self, name, description):
        self.send_name(name)
        self.send_description(description)
        self.click_save_button()


'''    
    def add_itnerary(self, itinerary_name, routes, itinerary_day, remark1):
        self.logger.info('添加行程数据并保存')
        self.send_keys(self.loc_itinerary_name, itinerary_name)
        self.send_keys(self.loc_routes, routes)
        self.send_keys(self.loc_routes_days, itinerary_day)
        self.send_keys(self.loc_remark, remark1)
        self.click_element(self.loc_save_itinerary)
'''
