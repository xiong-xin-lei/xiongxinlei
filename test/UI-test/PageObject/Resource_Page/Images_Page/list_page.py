import time

import allure
from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class list_Page(SeleniumBase):
    title = '镜像'
    # loc_list = (By.XPATH, '//*[@id="pane-/dbscale/app/resource/images?id=104000000&menu=%7B%22name%22%3A%22%E8%B5%84%E6%BA%90%E7%AE%A1%E7%90%86%22%2C%22icon%22%3A%22resource.svg%22%2C%22subMenu%22%3A%7B%22name%22%3A%22%E9%95%9C%E5%83%8F%E7%AE%A1%E7%90%86%22%7D%7D"]/iframe')
    loc_list = (By.XPATH, '//*[@class="el-tabs__content"]/div[2]/iframe')
    loc_enabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="启用{title}"]')
    loc_disabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="停用{title}"]')
    loc_search_input = (By.XPATH, '/html/body/div[1]/div[2]/div[1]/div/div/div[1]/div/input')
    loc_search = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[1]')
    loc_refresh = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[2]')

    row_edit_isDisabled = (By.XPATH, '/html/body/ul/li[1]/button')
    row_del_isDisabled = (By.XPATH, '/html/body/ul/li[2]/button')

    loc_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    loc_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    table_loc = (By.XPATH, '/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody')

    def list_in(self):
        self.logger.info("进入列表")
        self.select_frame(self.loc_list)

    def click_enabled_button(self):
        self.logger.info("点击启用按钮")
        assert self.is_button_disabled(self.loc_enabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_enabled)

    def click_disabled_button(self):
        self.logger.info("点击停用按钮")
        assert self.is_button_disabled(self.loc_disabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_disabled)

    def click_search_button(self):
        self.logger.info("点击搜索按钮")
        assert self.is_button_disabled(self.loc_search) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_search)

    def click_refresh_button(self):
        self.logger.info("点击刷新按钮")
        assert self.is_button_disabled(self.loc_refresh) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(self.loc_refresh)

    def click_row_enabled_button(self, type, version):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        version_order = self.get_row_orders(self.table_loc, version)
        order = list(set(type_order).intersection(set(version_order)))
        row_enabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[8]/div/span[1]/button')
        self.logger.info("点击行内停用按钮")
        assert self.is_button_disabled(row_enabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_enabled)

    def click_row_disabled_button(self, type, version):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        version_order = self.get_row_orders(self.table_loc, version)
        order = list(set(type_order).intersection(set(version_order)))
        row_disabled = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[8]/div/span[2]/button')
        self.logger.info("点击行内停用按钮")
        assert self.is_button_disabled(row_disabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_disabled)

    def click_row_config_button(self, type, version):
        self.logger.info('元素定位')
        type_order = self.get_row_orders(self.table_loc, type)
        version_order = self.get_row_orders(self.table_loc, version)
        order = list(set(type_order).intersection(set(version_order)))
        row_config = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr{order}/td[8]/div/span[3]/button')
        self.logger.info("点击行内配置按钮")
        assert self.is_button_disabled(row_config) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_config)

    def click_row_checkbox(self, key):
        self.logger.info('元素定位')
        keyArr = key.split(",")
        for i in range(len(keyArr)):
            order = self.get_row_order(self.table_loc, key[i])
            row_checkbox = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[1]/div/span')
            self.logger.info('点击进行行选中')
            with allure.step(f"选中{key[i]}："):
                self.click_element(row_checkbox)

    def operation_close(self):
        self.logger.info("关闭弹窗")
        self.click_element(self.loc_close)

    def operation_confirm(self):
        self.logger.info("点击确认按钮")
        time.sleep(2)
        self.click_element(self.loc_confirm)

    def operation_cancel(self):
        self.logger.info("点击确认按钮")
        time.sleep(2)
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
