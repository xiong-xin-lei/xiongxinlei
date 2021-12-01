import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class edit_Page(SeleniumBase):
    loc_edit = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_business_area = (By.XPATH, '/html/body/div[1]/div[1]/form/div[1]/div[2]/div/div/div/div/input')
    loc_name = (By.XPATH, '//*[@id="edit"]/div[1]/form/div[2]/div[1]/div/div/div/input')
    loc_haTag = (By.XPATH, '//*[@id="edit"]/div[1]/form/div[2]/div[2]/div/div/div/input')
    loc_defServs = (By.XPATH, '/html/body/div[1]/div[1]/form/div[3]/div/div/div/div[2]/input')
    loc_select_close = (By.CLASS_NAME, 'el-icon-close')
    loc_description = (By.TAG_NAME, 'textarea')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    select_business_area_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    select_defServs_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    operation_close = (By.XPATH, '/html/body/div[5]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[5]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[5]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')

    def edit_in(self):
        self.logger.info("进入编辑页面")
        self.select_frame(self.loc_edit)

    def select_business_area(self, business_area):
        self.logger.info('选择所属业务区')
        self.click_element(self.loc_business_area)
        if business_area != 'null':
            order = self.get_items_text(self.select_business_area_ul, business_area)
            checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("点击所属业务区")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_name(self, name):
        self.logger.info('清空文本框')
        self.clear_element_text(self.loc_name)
        self.logger.info(f"输入集群名称{name}")
        self.send_keys(self.loc_name, name)

    def send_haTag(self, haTag):
        self.logger.info('清空文本框')
        self.clear_element_text(self.loc_haTag)
        self.logger.info(f"输入高可用标签{haTag}")
        self.send_keys(self.loc_haTag, haTag)

    def select_defServs(self, defServs):
        self.logger.info('清空选择')
        close_num = len(self.find_elements(self.loc_select_close))
        num = 0
        for i in range(close_num):
            num = num + 1
            if num == close_num:
                time.sleep(0.2)
                self.click_element((By.XPATH, '/html/body/div[1]/div[1]/form/div[3]/div/div/div/div[1]/span/span/i'))
            else:
                time.sleep(0.2)
                self.click_element((By.XPATH, '/html/body/div[1]/div[1]/form/div[3]/div/div/div/div[1]/span/span[1]/i'))
        self.logger.info(f"选择包含软件{defServs}")
        time.sleep(0.2)
        self.click_element(self.loc_defServs)
        time.sleep(0.2)
        self.logger.info('元素定位')
        if defServs != 'null':
            defServsArr = defServs.split(";")
            for i in range(len(defServsArr)):
                order = self.get_items_text(self.select_defServs_ul, defServsArr[i])
                checked = (By.XPATH, f'/html/body/div[4]/div[1]/div[1]/ul/li[{order}]')
                self.logger.info("点击所包含软件")
                time.sleep(0.2)
                self.click_element(checked)
        else:
            self.click_element(self.loc_save)
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

    def edit(self, business_area, name, haTag, defServs, description):
        self.select_business_area(business_area)
        self.send_name(name)
        self.send_haTag(haTag)
        self.select_defServs(defServs)
        self.send_description(description)
        self.click_save_button()
