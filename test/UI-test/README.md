
一、框架结构：

Business:业务相关公共模块，如登录

Common：业务无关公共模块，如读取文件

PageObject:页面元素封装

TestCase：测试用例层

TestData:测试数据

TestSuite:测试套件

browser.ini:运行浏览器配置文件

二、断言类型解释：

1：添加/编辑成功

2：页面必填项存在“为空”状态

3：名称存在重复

4：输入格式有错误

三、注意事项

1.测试数据 新增一个空值时，请直接略过   例 姓名,年龄（为空）,性别  => name,,sex

2.测试数据 编辑页面空值暂时忽略不测 java部分会自动将空设置为改变前的值 如果需要修改空值的 请使用空格代替 例 姓名,年龄（为空）,性别  => name, ,sex

3.测试数据 选择框为空时，请使用 ‘null’

四、使用方法

1.安装 Python 环境并配置系统变量 （建议安装3.6版本）

2.下载浏览器对应版本驱动，将其添加到系统环境 （驱动下载地址：http://npm.taobao.org/mirrors/chromedriver/ ）

3.下载 allure报告生成器，并解压到任意目录，配置系统环境变量指向解压后的bin目录 （生成器下载地址：https://github.com/allure-framework/allure2/releases ，建议使用2.7.0版本）

4.导入依赖包 pip install -r requirements.txt

5.命令行 cd到UI-test目录

6.命令行 键入 python setup.py 运行整体测试项目；键入 pytest test_xxxx.py --alluredir=tmp/log运行单独测试用例

7.命令行 键入 allure serve tmp/log 生成测试报告
