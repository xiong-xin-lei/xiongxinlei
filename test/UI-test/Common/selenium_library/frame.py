# 前端frame框架选择及跳出封装

from selenium.webdriver.common.by import By
from .base import Base


class Frame(Base):
    def select_frame(self, locator):
        """通过 locator 选择 frame"""
        self.logger.info(f"选择 frame：{locator}")
        element = self.find_element(locator)
        # print(self.is_element(By.CLASS_NAME, 'layui-layer-shade'))
        # if self.is_element(By.CLASS_NAME, 'layui-layer-shade'):
        #     self.select_frame(self, locator)
        # else:
        self.driver.switch_to.frame(element)

    def unselect_frame(self):
        """跳出框架选择主框架"""
        self.logger.info("跳出框架选择主框架")
        self.driver.switch_to.default_content()
