import pytest

if __name__ == '__main__':
    pytest.main(["-s", "TestSuite/test_login.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_resource_suite/test_business_areas.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_resource_suite/test_cluster.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_resource_suite/test_network.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_resource_suite/test_image.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_resource_suite/test_hosts.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_business_suite/test_business_system.py", "--alluredir=tmp/log"])
    pytest.main(["-s", "TestSuite/test_business_suite/test_scales.py", "--alluredir=tmp/log"])
