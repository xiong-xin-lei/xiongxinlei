from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class del_Page(SeleniumBase):

    loc_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    loc_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    def del_close(self):
        self.logger.info("关闭弹窗")
        self.click_element(self.loc_close)

    def del_confirm(self):
        self.logger.info("点击确认按钮")
        self.click_element(self.loc_confirm)

    def del_cancel(self):
        self.logger.info("点击确认按钮")
        self.click_element(self.loc_cancel)
