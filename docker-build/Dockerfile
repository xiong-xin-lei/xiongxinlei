FROM dbscale/dbscale-kube-web:2105-tomcat9.0.45-amd64

ENV MYSQL_VERSION="8.0.27"

RUN yum install -y https://repo.mysql.com/mysql80-community-release-el7.rpm \
    && yum -y install yum-utils openssl \
    && yum-config-manager --enable mysql80-server \
    && rpmkeys --import /etc/pki/rpm-gpg/RPM-GPG-KEY-mysql \
    && yum install -y \
      "mysql-community-client-${MYSQL_VERSION}" \
    && yum clean all

COPY war/dbscale.war /usr/local/tomcat/webapps/
COPY sql /opt/web-manager/sql
COPY script /opt/web-manager/script
COPY dbscale /etc/dbscale

RUN chmod +x /opt/web-manager/script/init.sh

CMD ["/bin/bash"]
