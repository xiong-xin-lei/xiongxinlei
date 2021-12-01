# 表格（table）中单元格的文本内容封装
from .base import Base


class TableElement(Base):
    def get_table_cell(self, locator, row: int, column: int):
        """返回表格（table）中单元格的文本内容

        :param locator: 定位
        :param row: 行，从 1 开始，-1 表示倒数第一行，以此类推
        :param column: 列，从 1 开始，-1 表示倒数第一列，以此类推
        :return: 单元格文本
        """
        row = int(row)
        column = int(column)
        if row == 0 or column == 0:
            raise ValueError(f"行（{row}）列（{column}）都必须非零")
        try:
            cell = self._get_cell(locator, row, column)
        except AssertionError:
            raise
        return cell.text

    def _get_cell(self, locator, row, column):
        rows = self._get_rows(locator, row)
        if len(rows) < abs(row):
            raise AssertionError(f"表格 {locator} 至少应该有 {abs(row)} 行，但是只有 {len(rows)} 行")
        index = row - 1 if row > 0 else row
        cells = rows[index].find_elements_by_xpath('./th|./td')
        if len(cells) < abs(column):
            raise AssertionError(f"表格 {locator} 至少应该有 {abs(column)} 列，但是只有 {len(cells)} 列")
        index = column - 1 if column > 0 else column
        return cells[index]

    def _get_rows(self, locator, count):
        # 按照浏览器呈现顺序排序
        table = self.find_element(locator)
        rows = table.find_elements_by_xpath("./thead/tr")
        if count < 0 or len(rows) < count:
            rows.extend(table.find_elements_by_xpath("./tbody/tr"))
        if count < 0 or len(rows) < count:
            rows.extend(table.find_elements_by_xpath("./tfoot/tr"))
        return rows

    def get_row_order(self, table_tbody, key):
        """

        :rtype: object
        """
        # table_tbody--table表格元素，精确至tbody
        # key--想要查找的值 从0开始
        # i--返回查询值所在行号，从0开始
        arr = []
        table_tr_list = self.driver.find_element(*table_tbody).find_elements_by_tag_name('tr')
        # 按行查询表格的数据，取出的数据是一整行，按\n分隔每一列的数据
        if len(table_tr_list) > 0:
            for tr in table_tr_list:
                arr.append(tr.text.split("\n"))  # 将表格数据组成二维的列表
            for i in range(len(arr)):
                for j in range(len(arr[i])):
                    if key in arr[i][j]:
                       return i + 1
        else:
            return "暂无数据"

    def get_row_orders(self, table_tbody, key):
        """

        :rtype: object
        """
        # table_tbody--table表格元素，精确至tbody
        # key--想要查找的值 从0开始
        # i--返回查询值所在行号，从0开始
        arr = []
        alone = []
        table_tr_list = self.driver.find_element(*table_tbody).find_elements_by_tag_name('tr')
        # 按行查询表格的数据，取出的数据是一整行，按\n分隔每一列的数据
        if len(table_tr_list) > 0:
            for tr in table_tr_list:
                arr.append(tr.text.split("\n"))  # 将表格数据组成二维的列表
            for i in range(len(arr)):
                for j in range(len(arr[i])):
                    if key in arr[i][j]:
                        alone.append(i + 1)
            return alone
        else:
            return "暂无数据"

    def get_cell_text(self, table_tbody, num):
        # table_tbody--table表格元素，精确至tbody
        # num--表格对应的列顺序 从0开始
        arr = []
        table_tr_list = self.driver.find_element(*table_tbody).find_elements_by_tag_name('tr')
        # 按行查询表格的数据，取出的数据是一整行，按\n分隔每一列的数据
        for tr in table_tr_list:
            arr.append(tr.text.split("\n")[num])  # 将表格数据组成二维的列表
            return arr
