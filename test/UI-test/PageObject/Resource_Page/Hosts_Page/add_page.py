import time

from selenium.webdriver.common.by import By
from Common.selenium_library import SeleniumBase


class add_Page(SeleniumBase):
    loc_add = (By.XPATH, '/html/body/div[5]/div[2]/iframe')
    loc_business_area = (By.XPATH, '/html/body/div[1]/div[1]/form/div[1]/div[2]/div/div/div/div[1]/input')
    loc_clusters = (By.XPATH, '/html/body/div[1]/div[1]/form/div[2]/div[1]/div/div/div/div[1]/input')
    loc_hostIp = (By.XPATH, '/html/body/div[1]/div[1]/form/div[2]/div[2]/div/div/div/input')
    loc_room = (By.XPATH, '/html/body/div[1]/div[1]/form/div[3]/div[1]/div/div/div/input')
    loc_seat = (By.XPATH, '/html/body/div[1]/div[1]/form/div[3]/div[2]/div/div/div/input')
    loc_hddPaths = (By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[1]/div/div/div/input')
    loc_ssdPaths = (By.XPATH, '/html/body/div[1]/div[1]/form/div[4]/div[2]/div/div/div/input')
    loc_remoteStorageId = (By.XPATH, '/html/body/div[1]/div[1]/form/div[5]/div[1]/div/div/div/div[1]/input')
    loc_maxUnitCnt = (By.XPATH, '/html/body/div[1]/div[1]/form/div[5]/div[2]/div/div/div/input')
    loc_hotSpare = (By.XPATH, '/html/body/div[1]/div[1]/form/div[6]/div/div/div/div/span')
    loc_description = (By.TAG_NAME, 'textarea')

    loc_save = (By.XPATH, '/html/body/div[1]/div[3]/button[1]')
    loc_cancel = (By.XPATH, '/html/body/div[1]/div[3]/button[2]')

    operation_close = (By.XPATH, '/html/body/div[3]/div/div[1]/button')
    operation_confirm = (By.XPATH, '/html/body/div[6]/div/div[3]/button[2]')
    operation_cancel = (By.XPATH, '/html/body/div[6]/div/div[3]/button[1]')

    loc_message = (By.CLASS_NAME, 'el-message__content')
    loc_non_empty_error = (By.CLASS_NAME, 'el-form-item__error')
    div_click = (By.XPATH, '/html/body/div[1]/div[1]')

    business_area_select_ul = (By.XPATH, '/html/body/div[3]/div[1]/div[1]/ul')
    clusters_select_ul = (By.XPATH, '/html/body/div[4]/div[1]/div[1]/ul')

    def add_in(self):
        self.logger.info("进入新增页面")
        self.select_frame(self.loc_add)

    def select_business_area(self, business_area):
        self.logger.info(f"选择所属业务区{business_area}")
        self.click_element(self.loc_business_area)
        self.logger.info('元素定位')
        if business_area != 'null':
            order = self.get_items_text(self.business_area_select_ul, business_area)
            checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择所属业务区")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def select_clusters(self, clusters):
        self.logger.info(f"选择关联集群{clusters}")
        self.click_element(self.loc_clusters)
        self.logger.info('元素定位')
        if clusters != 'null':
            order = self.get_items_text(self.clusters_select_ul, clusters)
            checked = (By.XPATH, f'/html/body/div[4]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择关联集群")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_hostIp(self, hostIp):
        self.logger.info(f"输入主机IP{hostIp}")
        self.send_keys(self.loc_hostIp, hostIp)

    def send_room(self, room):
        self.logger.info(f"输入机房{room}")
        self.send_keys(self.loc_room, room)

    def send_seat(self, seat):
        self.logger.info(f"输入机位{seat}")
        self.send_keys(self.loc_seat, seat)

    def send_hddPaths(self, hddPaths):
        self.logger.info(f"输入机械盘设备{hddPaths}")
        self.send_keys(self.loc_hddPaths, hddPaths)

    def send_ssdPaths(self, ssdPaths):
        self.logger.info(f"输入固态盘设备{ssdPaths}")
        self.send_keys(self.loc_ssdPaths, ssdPaths)

    def select_remoteStorageId(self, remoteStorageId):
        self.logger.info(f"选择位置存储{remoteStorageId}")
        self.click_element(self.loc_remoteStorageId)
        self.logger.info('元素定位')
        if remoteStorageId != 'null':
            order = self.get_items_text(self.business_area_select_ul, remoteStorageId)
            checked = (By.XPATH, f'/html/body/div[3]/div[1]/div[1]/ul/li[{order}]')
            self.logger.info("选择位置存储")
            self.click_element(checked)
        else:
            self.click_element(self.div_click)

    def send_maxUnitCnt(self, maxUnitCnt):
        self.logger.info(f"输入容器上限{maxUnitCnt}")
        self.send_keys(self.loc_maxUnitCnt, maxUnitCnt)

    def select_hotSpare(self, hotSpare):
        self.logger.info(f"选择备选{hotSpare}")
        if hotSpare == 'true':
            self.click_element(self.loc_hotSpare)
        else:
            self.click_element(self.div_click)

    def send_description(self, description):
        self.logger.info(f"输入描述{description}")
        self.send_keys(self.loc_description, description)

    def click_save_button(self):
        self.logger.info("点击保存按钮")
        self.click_element(self.loc_save)

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

    def is_add_element(self):
        return self.is_element(self.operation_confirm)

    def get_message_text(self):
        time.sleep(3)
        return self.get_element_text(self.loc_message)

    def add(self, business_area, clusters, hostIp, room, seat, hddPaths, ssdPaths, remoteStorageId, maxUnitCnt,
            hotSpare, description):
        self.select_business_area(business_area)
        self.select_clusters(clusters)
        self.send_hostIp(hostIp)
        self.send_room(room)
        self.send_seat(seat)
        self.send_hddPaths(hddPaths)
        self.send_ssdPaths(ssdPaths)
        self.select_remoteStorageId(remoteStorageId)
        self.send_maxUnitCnt(maxUnitCnt)
        self.select_hotSpare(hotSpare)
        self.send_description(description)
        self.click_save_button()
