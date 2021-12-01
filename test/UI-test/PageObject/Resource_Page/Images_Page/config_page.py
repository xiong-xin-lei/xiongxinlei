import time

import allure
from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class config_Page(SeleniumBase):
    loc_config = (By.XPATH, '//*[@class="el-tabs__content"]/div[3]/iframe')
    loc_search_input = (By.XPATH, '/html/body/div[1]/div[2]/div[1]/div/div/div[1]/div/input')
    loc_search = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[1]')
    loc_refresh = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[2]')

    operation_close = (By.XPATH, '/html/body/div[5]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[5]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[5]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')

    table_loc = (By.XPATH, '/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody')
    div_click = (By.XPATH, '/html/body/div[1]/div[2]/div[1]')

    def config_in(self):
        self.logger.info('进入配置页面')
        self.select_frame(self.loc_config)

    def click_search_button(self):
        self.logger.info("点击搜索按钮")
        self.click_element(self.loc_search)

    def click_refresh_button(self):
        self.logger.info("点击刷新按钮")
        self.click_element(self.loc_refresh)

    def is_edit_element(self):
        return self.is_element(self.operation_confirm)

    def click_operation_confirm(self):
        self.logger.info('点击弹窗确认')
        self.click_element(self.operation_confirm)

    def click_operation_cancel(self):
        self.logger.info('点击弹窗取消')
        self.click_element(self.operation_cancel)

    def click_operation_close(self):
        self.logger.info('点击弹窗关闭')
        self.click_element(self.operation_close)

    def images_config_search(self):
        allure_title = "搜索"
        allure.dynamic.title(allure_title)
        searchkey = 'mysqld::character_set_server'
        self.send_keys(self.loc_search_input, searchkey)
        self.click_search_button()
        time.sleep(5)
        assert searchkey in self.get_cell_text(self.table_loc, 0), "搜索无误"

    def value_edit(self, row_value, value):
        self.logger.info('双击需要编辑的行')
        self.double_click_element(row_value)
        self.logger.info('清空文本框')
        self.clear_element_text(row_value)
        self.logger.info(f"输入值{value}")
        self.send_keys(row_value, value)

    def defaultValue_edit(self, row_defaultValue, defaultValue):
        self.click_element(row_defaultValue)
        self.logger.info('清空文本框')
        self.clear_element_text(row_defaultValue)
        self.logger.info(f"输入默认值{defaultValue}")
        self.send_keys(row_defaultValue, defaultValue)

    def range_edit(self, row_range, range):
        self.click_element(row_range)
        self.logger.info('清空文本框')
        self.clear_element_text(row_range)
        self.logger.info(f"输入范围{range}")
        self.send_keys(row_range, range)

    def canSet_edit(self, row_canSet, row_canSet_text, canSet):
        self.logger.info(f"选择允许编辑{canSet}")
        if canSet == 'true':
            if self.get_element_text(row_canSet_text) != '是':
                self.click_element(row_canSet)
        else:
            if self.get_element_text(row_canSet_text) != '否':
                self.click_element(row_canSet)

    def mustRestart_edit(self, row_mustRestart, row_mustRestart_text, mustRestart):
        self.logger.info(f"选择重启生效{mustRestart}")
        if mustRestart == 'true':
            if self.get_element_text(row_mustRestart_text) != '是':
                self.click_element(row_mustRestart)
        else:
            if self.get_element_text(row_mustRestart_text) != '否':
                self.click_element(row_mustRestart)

    def description_edit(self, row_description, description):
        self.click_element(row_description)
        self.logger.info('清空文本框')
        self.clear_element_text(row_description)
        self.logger.info(f"输入描述{description}")
        self.send_keys(row_description, description)

    def edit(self, key, value, defaultValue, range, canSet, mustRestart, description):
        tr_num = len(self.find_elements((By.CLASS_NAME, 'vxe-body--row')))
        order = self.get_row_order(self.table_loc, key)
        if tr_num != 0:
            row_dblclick = (By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[3]')
            row_value = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[3]/div/input')
            row_defaultValue = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[4]/div/input')
            row_range = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[5]/div/input')
            row_canSet = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[6]/div/div')
            row_canSet_text = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[6]/div/span')
            row_mustRestart = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[7]/div/div')
            row_mustRestart_text = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[7]/div/span')
            row_description = (
                By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[8]/div/textarea')
            self.double_click_element(row_dblclick)
            self.value_edit(row_value, value)
            self.defaultValue_edit(row_defaultValue, defaultValue)
            self.range_edit(row_range, range)
            self.canSet_edit(row_canSet, row_canSet_text, canSet)
            self.mustRestart_edit(row_mustRestart, row_mustRestart_text, mustRestart)
            self.description_edit(row_description, description)
            self.click_element(self.div_click)
        else:
            assert False, '暂无数据'
