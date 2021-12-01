import time

import allure
from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class list_Page(SeleniumBase):
    title = '规模'
    loc_new = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="新增{title}"]')
    loc_enabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="启用{title}"]')
    loc_disabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="停用{title}"]')
    loc_del = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="删除{title}"]')
    loc_search_input = (By.XPATH, '/html/body/div[1]/div[2]/div[1]/div/div/div[1]/div/input')
    loc_search = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[1]')
    loc_refresh = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[2]')

    loc_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    loc_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    table_loc = (By.XPATH, '/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody')

    def click_new_button(self):
        self.logger.info("点击新增按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_new)

    def click_enabled_button(self):
        self.logger.info("点击启用按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_enabled)

    def click_disabled_button(self):
        self.logger.info("点击停用按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_disabled)

    def click_del_button(self):
        self.logger.info("点击删除按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_del)

    def click_search_button(self):
        self.logger.info("点击搜索按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_search)

    def click_refresh_button(self):
        self.logger.info("点击刷新按钮")
        assert self.is_button_disabled(self.loc_new) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_refresh)

    def is_enabled_disable(self, type, name):
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        row_enabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[1]/button')
        return self.is_button_disabled(row_enabled)

    def click_row_enabled_button(self, type, name):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        self.logger.info("点击行内启用按钮")
        row_enabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[1]/button')
        self.logger.info("点击行内启用按钮")
        assert self.is_button_disabled(row_enabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_enabled)

    def is_disabled_disable(self, type, name):
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        row_disabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[2]/button')
        return self.is_button_disabled(row_disabled)

    def click_row_disabled_button(self, type, name):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        row_disabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[2]/button')
        self.logger.info("点击行内停用按钮")
        assert self.is_button_disabled(row_disabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_disabled)

    def is_del_disable(self, type, name):
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        row_del = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[3]/button')
        return self.is_button_disabled(row_del)

    def click_row_del_button(self, type, name):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        name_order = self.get_row_orders(self.table_loc, name)
        order = list(set(type_order).intersection(set(name_order)))
        row_del = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[7]/div/span[3]/button')
        self.logger.info("点击行内删除按钮")
        assert self.is_button_disabled(row_del) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_del)

    def click_row_checkbox(self, key):
        self.logger.info('元素定位')
        keyArr = key.split(",")
        for i in range(len(keyArr)):
            order = self.get_row_orders(self.table_loc, key[i])
            row_checkbox = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[1]/div/span')
            self.logger.info('点击进行行选中')
            with allure.step(f"选中{key[i]}："):
                self.click_element(row_checkbox)
