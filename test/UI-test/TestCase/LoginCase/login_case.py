# 登录测试用例
import allure
from Common.selenium_library import SeleniumBase
from Business.login_business import login


def case_test_login(self, username, password, assert_type):
    se = SeleniumBase(self.driver)
    se.get("http://localhost:8080/dbscale")
    allure.attach("账号:{}\n密码:{}".format(username, password), "登录信息")
    login(self.driver, username, password)
    # se.add_page_screen_shot(f"{username}登录后页面")

    if assert_type == '1':
        allure.dynamic.title("登陆成功")
        text = self.driver.title
        assert "DBScale" == text, '登陆成功断言'

    elif assert_type == "2":
        text = self.driver.find_element_by_class_name("el-message--error").text
        allure.dynamic.title(text)
        assert "不能为空" in text, '登陆失败断言'

    elif assert_type == "3":
        text = self.driver.find_element_by_class_name("el-message--error").text
        allure.dynamic.title(text)
        assert "用户名不存在" in text, '登陆失败断言'

    elif assert_type == "4":
        text = self.driver.find_element_by_class_name("el-message--error").text
        allure.dynamic.title(text)
        assert "密码错误" in text, '登陆失败断言'

    else:
        allure.dynamic.title("未知错误")
        se.logger.info(f"未知断言类型{assert_type}")
        assert False, "未知断言类型"
