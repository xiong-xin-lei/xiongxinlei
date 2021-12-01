import time

import allure
from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class list_Page(SeleniumBase):
    table_loc = (By.XPATH, '/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody')

    title = '主机'
    loc_list = (By.XPATH, '//*[@class="el-tabs__content"]/div[2]/iframe')
    loc_new = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="注册{title}"]')
    loc_enabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="启用{title}"]')
    loc_disabled = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="停用{title}"]')
    loc_btnIn = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="入库{title}"]')
    loc_btnOut = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="出库{title}"]')
    loc_del = (By.XPATH, f'//*[@id="btnPanel"]/button[@title="注销{title}"]')
    loc_search_input = (By.XPATH, '/html/body/div[1]/div[2]/div[1]/div/div/div[1]/div/input')
    loc_search = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[1]')
    loc_refresh = (By.XPATH, '//*[@id="btnPanel"]/*[@class="vxe-toolbar"]/div/button[2]')

    row_edit = (By.XPATH, '/html/body/ul/li[1]/button')
    row_btnIn = (By.XPATH, '/html/body/ul/li[2]/button')
    row_btnOut = (By.XPATH, '/html/body/ul/li[3]/button')
    row_del = (By.XPATH, '/html/body/ul/li[4]/button')

    loc_close = (By.XPATH, '/html/body/div[4]/div/div[1]/button')
    loc_confirm = (By.XPATH, '/html/body/div[4]/div/div[3]/button[2]')
    loc_cancel = (By.XPATH, '/html/body/div[4]/div/div[3]/button[1]')

    def list_in(self):
        self.logger.info("进入列表")
        self.select_frame(self.loc_list)

    def click_new_button(self):
        self.logger.info("点击新增按钮")
        self.click_element(self.loc_new)

    def click_enabled_button(self):
        self.logger.info("点击启用按钮")
        self.click_element(self.loc_enabled)

    def click_disabled_button(self):
        self.logger.info("点击停用按钮")
        self.click_element(self.loc_disabled)

    def click_del_button(self):
        self.logger.info("点击删除按钮")
        self.click_element(self.loc_del)

    def click_search_button(self):
        self.logger.info("点击搜索按钮")
        self.click_element(self.loc_search)

    def click_refresh_button(self):
        self.logger.info("点击刷新按钮")
        self.click_element(self.loc_refresh)

    def click_row_taskStatus(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        close_num = len(self.find_elements((By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[10]/div/a')))
        if close_num == 2:
            row_taskStatus = (By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[10]/div/a[2]/span')
        else:
            row_taskStatus = (By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[10]/div/a/span')
        self.logger.info("获取任务状态")
        return self.get_element_text(row_taskStatus)

    def click_row_monitoring(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        row_monitoring = (
            By.XPATH,
            f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[13]/div/span[1]/button')
        self.logger.info("点击行内监控按钮")
        self.click_element(row_monitoring)
        time.sleep(10)

    def click_row_units(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        row_units = (
            By.XPATH,
            f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[13]/div/span[2]/button')
        self.logger.info("点击行内单元按钮")
        self.click_element(row_units)

    def click_row_enabled_button(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        row_enabled = (
            By.XPATH,
            f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[13]/div/span[3]/button')
        self.logger.info("点击行内启用按钮")
        self.click_element(row_enabled)

    def click_row_disabled_button(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        row_disabled = (
            By.XPATH,
            f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[13]/div/span[4]/button')
        self.logger.info("点击行内停用按钮")
        assert self.is_button_disabled(row_disabled) != 'true', '按钮处于禁用状态，不可点击'
        self.click_element(row_disabled)

    def button_move_over(self, key):
        self.logger.info('元素定位')
        order = self.get_row_order(self.table_loc, key)
        row_more = (
            By.XPATH, f'/html/body/div[1]/div[2]/div[2]/div[2]/div[2]/table/tbody/tr[{order}]/td[13]/div/div/span')
        self.logger.info("鼠标悬浮于更多按钮")
        self.mouse_over_center(row_more)
        time.sleep(3)

    def click_row_del_button(self):
        self.logger.info("点击行内删除按钮")
        self.click_element(self.row_del)

    def click_row_btnIn_button(self):
        self.logger.info("点击行内入库按钮")
        self.click_element(self.row_btnIn)

    def click_row_btnOut_button(self):
        self.logger.info("点击行内出库按钮")
        self.click_element(self.row_btnOut)

    def click_row_edit_button(self):
        self.logger.info("点击行内编辑按钮")
        self.click_element(self.row_edit)

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
