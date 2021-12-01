from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class btnOut_Page(SeleniumBase):

    loc_btnOut_page = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_port = (By.XPATH, '/html/body/div/div[1]/form/div[2]/div/div/input')
    loc_userName = (By.XPATH, '/html/body/div/div[1]/form/div[3]/div/div/input')
    loc_password = (By.XPATH, '/html/body/div/div[1]/form/div[4]/div/div/input')

    loc_btnOut_checkout = (By.XPATH, '/html/body/div/div[3]/button[1]')
    loc_btnOut = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div/div[3]/button[3]')

    loc_message = (By.CLASS_NAME, 'el-message__content')
    loc_non_empty_error = (By.CLASS_NAME, 'el-form-item__error')

    operation_close = (By.XPATH, '/html/body/div[3]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[3]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[3]/div/div[3]/button[1]')

    def btnOut_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_btnOut_page)

    def send_port(self, port):
        self.logger.info(f"输入端口{port}")
        self.send_keys(self.loc_port, port)

    def send_userName(self, userName):
        self.logger.info(f"输入用户名称{userName}")
        self.send_keys(self.loc_userName, userName)

    def send_password(self, password):
        self.logger.info(f"输入密码{password}")
        self.send_keys(self.loc_password, password)

    def click_btnOut_checked_button(self):
        self.logger.info("点击入库校验按钮")
        self.click_element(self.loc_btnOut_checkout)

    def click_btnOut_button(self):
        self.logger.info("点击入库按钮")
        assert self.is_button_disabled(self.loc_btnOut) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_btnOut)

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

    def is_operation_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        return self.get_element_text(self.loc_message)

    def btnOut(self, port, userName, password):
        self.send_port(port)
        self.send_userName(userName)
        self.send_password(password)
        self.click_btnOut_checked_button()


