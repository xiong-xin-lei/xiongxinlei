# 截图方法封装
import os
import time
import allure
from .base import Base


class Screenshot(Base):

    def add_page_screen_shot(self, img_doc=""):
        """添加页面截图到报告中

        :param img_doc: 截图说明
        """
        file_name = "tmp/img/{}_{}.png".format(time.strftime("%Y%m%d%H%M%S", time.localtime()), img_doc)
        flag = self.driver.get_screenshot_as_file(file_name)
        if not flag:
            os.makedirs("tmp/img")
            self.driver.get_screenshot_as_file(file_name)
        with open(file_name, mode='rb') as f:
            file = f.read()
        allure.attach(file, img_doc, allure.attachment_type.PNG)
        self.logger.info("页面截图文件保存在：{}".format(file_name))
