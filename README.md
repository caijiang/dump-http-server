# dump-http-server
这货只干一个事儿，把所有请求给dump下来；有2种用法。
## standalone
直接使用它的jar包

```bash
java -jar <jar-file> [port] [status]
```

## docker
启动该docker，容器名为dump-http-server。
```bash
docker run --detach \
    --name dump-http-server \
    --publish 82:80 \
    --volume /somedir:/opt/dhs/logs \
    registry.cn-shanghai.aliyuncs.com/mingshz/dump-http-server 
```
在阿里云内网地址分别为
* 经典内网: registry-internal.cn-shanghai.aliyuncs.com/mingshz/dump-http-server
* VPC: registry-vpc.cn-shanghai.aliyuncs.com/mingshz/dump-http-server

可以考虑ngxin将部分请求转发给它。
```
server {
    listen       80;
    server_name  domain.com;
    location /
    {
        proxy_pass http://localhost:82;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```