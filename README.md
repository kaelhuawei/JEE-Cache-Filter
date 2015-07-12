# JEE-Cache-Filter
在专项优化web性能过程中写的Cache Filter,服务端是Tomcat7。最优缓存策略制定参见我的文章：[web性能优化(二) 合理利用浏览器缓存](https://github.com/kaelhuawei/blog/blob/master/web/web%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96(%E4%BA%8C)%20%E5%90%88%E7%90%86%E5%88%A9%E7%94%A8%E6%B5%8F%E8%A7%88%E5%99%A8%E7%BC%93%E5%AD%98.md "web性能优化(二) 合理利用浏览器缓存")  
***  
####使用参考  
* DisableETagFilter
web.xml filter配置  
```xml
<filter>
		<filter-name>disableETagFilter</filter-name>
		<filter-class>com.huawei.universe.ckm.web.filter.DisableETagFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>disableETagFilter</filter-name>
		<servlet-name>default</servlet-name>
	</filter-mapping>
```
* CacheFilter
web.xml filter配置  
```xml
<filter-name>cacheFilter</filter-name>
		<filter-class>com.huawei.universe.ckm.web.filter.CacheFilter</filter-class>
		<init-param>
			<param-name>expiration</param-name>
			<param-value>31536000</param-value>
		</init-param>
		<!--<init-param>
			<param-name>vary</param-name>
			<param-value>Accept-Encoding</param-value>
		</init-param>
		<init-param>
			<param-name>private</param-name>
			<param-value>true</param-value>
		</init-param>-->
	</filter>
	<filter-mapping>
		<filter-name>cacheFilter</filter-name>
		<url-pattern>*.jpeg</url-pattern>
	</filter-mapping>
	<filter-mapping>
		<filter-name>cacheFilter</filter-name>
		<url-pattern>*.png</url-pattern>
	</filter-mapping>
	<filter-mapping>
		<filter-name>cacheFilter</filter-name>
		<url-pattern>*.css</url-pattern>
	</filter-mapping>
	<filter-mapping>
		<filter-name>cacheFilter</filter-name>
		<url-pattern>*.js</url-pattern>
	</filter-mapping>
```  
