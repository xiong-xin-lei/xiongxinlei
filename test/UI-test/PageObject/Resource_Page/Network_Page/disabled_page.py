from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class disabled_Page(SeleniumBase):

    loc_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    loc_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    def disabled_close(self):
        self.logger.info("关闭弹窗")
        self.click_element(self.loc_close)

    def disabled_confirm(self):
        self.logger.info("点击确认按钮")
        self.click_element(self.loc_confirm)

    def disabled_cancel(self):
        self.logger.info("点击确认按钮")
        self.click_element(self.loc_cancel)

'''    
    def add_itnerary(self, itinerary_name, routes, itinerary_day, remark1):
        self.logger.info('添加行程数据并保存')
        self.send_keys(self.loc_itinerary_name, itinerary_name)
        self.send_keys(self.loc_routes, routes)
        self.send_keys(self.loc_routes_days, itinerary_day)
        self.send_keys(self.loc_remark, remark1)
        self.click_element(self.loc_save_itinerary)
'''
