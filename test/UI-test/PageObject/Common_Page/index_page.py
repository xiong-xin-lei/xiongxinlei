import time

from Business.Base_url import url
from Common.selenium_library import SeleniumBase
from selenium.webdriver.common.by import By


class Index_Page(SeleniumBase):
    public_address = '//*[@id="index"]/section/section/aside/ul'
    loc_tab = (By.XPATH, '//*[@class="el-tabs__content"]/div[2]/iframe')

    """首页页面对象"""

    # 资源管理
    resource_loc = (By.XPATH, public_address + '/li[1]/div/span')
    business_areas_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[1]/span')
    cluster_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[2]/span')
    network_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[3]/span')
    image_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[4]/span')
    remote_storage_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[5]/span')
    host_loc = (By.XPATH, public_address + '/li[1]/ul/li/ul/li[6]/span')

    def click_resource(self):
        self.logger.info("点击资源管理模块")
        self.click_element(self.resource_loc)

    def in_business_areas(self):
        self.logger.info("进入业务区管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.business_areas_loc)
        self.select_frame(self.loc_tab)

    def in_cluster(self):
        self.logger.info("点击集群管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.cluster_loc)
        self.select_frame(self.loc_tab)

    def in_network(self):
        self.logger.info("点击网段管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.network_loc)
        self.select_frame(self.loc_tab)

    def in_image(self):
        self.logger.info("点击镜像管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.image_loc)
        self.select_frame(self.loc_tab)

    def in_remote_storage(self):
        self.logger.info("点击存储管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.remote_storage_loc)
        self.select_frame(self.loc_tab)

    def in_host(self):
        self.logger.info("点击主机管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_element(self.host_loc)
        self.select_frame(self.loc_tab)

    # 业务管理
    business_loc = (By.XPATH, public_address + '/li[2]/div')
    business_system_loc = (By.XPATH, public_address + '/li[2]/ul/li/ul/li[1]/span')
    scale_loc = (By.XPATH, public_address + '/li[2]/ul/li/ul/li[2]/span')

    def click_business(self):
        self.logger.info("点击业务管理模块")
        self.click_element(self.business_loc)

    def in_business_system(self):
        self.logger.info("点击业务系统模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_business()
        time.sleep(0.2)
        self.click_element(self.business_system_loc)
        self.select_frame(self.loc_tab)

    def in_scale(self):
        self.logger.info("点击规模管理模块")
        se = SeleniumBase(self.driver)
        se.get(url)
        self.click_business()
        time.sleep(0.2)
        self.click_element(self.scale_loc)
        self.select_frame(self.loc_tab)

    # 工单管理
    order_loc = (By.XPATH, public_address + '/li[3]/div/span')
    mysql_order_loc = (By.XPATH, public_address + '/li[3]/ul/li/ul/li[1]/span')
    cmha_order_loc = (By.XPATH, public_address + '/li[3]/ul/li/ul/li[2]/span')
    apache_order_loc = (By.XPATH, public_address + '/li[3]/ul/li/ul/li[3]/span')
    nginx_order_loc = (By.XPATH, public_address + '/li[3]/ul/li/ul/li[4]/span')
    order_cfg_loc = (By.XPATH, public_address + '/li[3]/ul/li/ul/li[5]/span')

    def click_order(self):
        self.logger.info("点击工单管理模块")
        self.click_element(self.order_loc)

    def click_mysql_order(self):
        self.logger.info("点击MySQL工单模块")
        self.click_element(self.mysql_order_loc)

    def click_cmha_order(self):
        self.logger.info("点击CMHA工单模块")
        self.click_element(self.cmha_order_loc)

    def click_apache_order(self):
        self.logger.info("点击Apache工单模块")
        self.click_element(self.apache_order_loc)

    def click_nginx_order(self):
        self.logger.info("点击Nginx工单模块")
        self.click_element(self.nginx_order_loc)

    def click_order_cfg(self):
        self.logger.info("点击工单配置工单模块")
        self.click_element(self.order_cfg_loc)

    # 服务管理
    servGroup_loc = (By.XPATH, public_address + '/li[4]/div/span')
    mysql_servGroup_loc = (By.XPATH, public_address + '/li[4]/ul/li/ul/li[1]/span')
    cmha_servGroup_loc = (By.XPATH, public_address + '/li[4]/ul/li/ul/li[2]/span')
    apache_servGroup_loc = (By.XPATH, public_address + '/li[4]/ul/li/ul/li[3]/span')
    nginx_servGroup_loc = (By.XPATH, public_address + '/li[4]/ul/li/ul/li[4]/span')

    def click_servGroup(self):
        self.logger.info("点击服务管理模块")
        self.click_element(self.servGroup_loc)

    def click_mysql_servGroup(self):
        self.logger.info("点击MySQL管理模块")
        self.click_element(self.mysql_servGroup_loc)

    def click_cmha_servGroup(self):
        self.logger.info("点击CMHA管理模块")
        self.click_element(self.cmha_servGroup_loc)

    def click_apache_servGroup(self):
        self.logger.info("点击Apache管理模块")
        self.click_element(self.apache_servGroup_loc)

    def click_nginx_servGroup(self):
        self.logger.info("点击Nginx管理模块")
        self.click_element(self.nginx_servGroup_loc)

    # 系统管理
    system_loc = (By.XPATH, public_address + '/li[5]/div/span')
    task_loc = (By.XPATH, public_address + '/li[5]/ul/li/ul/li[1]/span')
    operateLog_loc = (By.XPATH, public_address + '/li[5]/ul/li/ul/li[2]/span')

    def click_system(self):
        self.logger.info("点击系统管理模块")
        self.click_element(self.system_loc)

    def click_task(self):
        self.logger.info("点击任务管理模块")
        self.click_element(self.task_loc)

    def click_operateLog(self):
        self.logger.info("点击操作记录模块")
        self.click_element(self.operateLog_loc)
