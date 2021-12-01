import csv


# 读取csv格式文件封装
def read_csv(file):
    csv_reader = csv.reader(open(file))
    L = []
    for row in csv_reader:
        L.append(tuple(row))
    L.pop(0)
    return L


def read_success_csv(file, num):
    csv_reader = csv.reader(open(file))
    L = []
    for row in csv_reader:
        if row[-1] != num:
            continue
        L.append(tuple(row))
    return L
