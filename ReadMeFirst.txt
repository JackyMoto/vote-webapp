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
8. 修改前端包里的mobile.html 和 index.html,在页面底部
var glob={
        path:"http://192.168.8.44:8080/voting/vote/", // 后端接口的地址
        mPath:"http://127.0.0.1:8080/voting/" // 前端服务器的地址
    };

path 修改为后台包的发布地址
mPath 修改为 前端包的发布地址

8. 开始使用平台

PS:
补充 Redis两个查询和清除命令 
redis-cli keys "vote*" 
redis-cli keys "vote*" | xargs redis-cli  del

