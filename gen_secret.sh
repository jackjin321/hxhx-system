!#/bin/bash
#相关配置信息
SERVER=".55.172."
PASSWORD="abc123"
COUNTRY="US"
STATE="beijing"
CITY="beijing"
ORGANIZATION="ssl"
ORGANIZATIONAL_UNIT="IT"
EMAIL="123@123.com"

###开始生成文件###
echo "开始生成文件"

#切换到生产密钥的目录

#生成ca私钥(使用aes256加密)
openssl genrsa -aes256 -passout pass:$PASSWORD  -out ca-key.pem 2048
#生成ca证书，填写配置信息
openssl req -new -passin "pass:$PASSWORD" -days 3650 -key ca-key.pem -sha256 -out ca.pem -subj "/C=$COUNTRY/ST=$STATE/L=$CITY/O=$ORGANIZATION/OU=$ORGANIZATIONAL_UNIT/CN=$SERVER/emailAddress=$EMAIL"

#生成server证书私钥文件
openssl genrsa -out key.key 4096
#生成server证书请求文件
openssl req -subj "/C=$COUNTRY/ST=$STATE/L=$CITY/O=$ORGANIZATION/OU=$ORGANIZATIONAL_UNIT/CN=$SERVER/emailAddress=$EMAIL" -new -key key.key -out server.csr
