部署相关步骤
1. 程序使用到MySQL，Redis，Nginx(或其他HTTP Proxy), JDK6，Tomcat，依次安装
2. DB数据库的初始化脚本在/src/main/resource/sql/init.sql
3. 服务的全部配置在/src/main/resource/application.properties中
4. 3中配置只需要修改
	MySQL: vote.jdbc.url, vote.jdbc.username, vote.jdbc.password
	Redis : redis.host, redis.port
	图片和静态资源服务(css,js,html): image.url.prefix
5. 完成以上步骤后,程序以WAR包方式发布与Tomcat中
6. 将MySQL中的数据初始化入Redis中，请求的链接为 http://${ip-address}:${port}/${project}/vote/initvotecache.do
	如果看到正常的列表，则初始化成功，没有看到列表里有内容，则说明初始化失败
7. 将前端包的html, css ,js发布到HTTP Proxy Server上,提供一个访问静态资源的地址配置给 4中的参数 image.url.prefix
8. 开始使用平台

