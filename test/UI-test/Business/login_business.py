# 登录公共模块业务流，用于执行测试用例前调用进行账户的登录

from PageObject.Common_Page.login_page import Login_Page, Site_Page
import logging
import time


def login(driver, username, password):
    """
    登录业务
    :param driver:浏览器驱动
    :param username:用户名
    :param password:密码
    :return:None
    """
    logging.basicConfig(level=logging.NOTSET)
    logging.info(f"使用用户名:{username},密码:{password}进行登陆")
    login_page = Login_Page(driver)
    login_page.send_username(username)
    login_page.send_password(password)
    login_page.submit()
    time.sleep(2)


def site_in(driver):
    """
    站点业务
    :param driver:浏览器驱动
    :return:None
    """
    logging.basicConfig(level=logging.NOTSET)
    logging.info(f"进入站点")
    login_page = Site_Page(driver)
    login_page.submit()
    time.sleep(2)


def index_in(driver, username, password):
    login(driver, username, password)
    site_in(driver)
