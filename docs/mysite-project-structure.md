# mysite04, 05 Package Structure

<pre>
    [src]
	|--- [main]
		|--- [java]
		|	|--- com
		|	|	|--- douzone
		|	|	|		|--- config
		|	|	|		|	|--- app
		|	|	|		|	|	|--- DBConfig.java
		|	|	|		|	|	|--- MyBatisConfig.java
		|	|	|		|	|--- web
		|	|	|		|	|	|--- MVCConfig.java
		|	|	|		|	|	|--- SecurityConfig.java
		|	|	|		|	|	|--- MessageConfig.java
		|	|	|		|	|	|--- FileuploadConfig.java
		|	|	|		|--- mysite
		|	|	|		|	|--- config
		|	|	|		|	|	|--- app
		|	|	|		|	|	|	|--- AppConfig.java
		|	|	|		|	|	|--- web
		|	|	|		|	|	|	|--- WebConfig.java
		|	|	|		|	|--- controller
		|	|	|		|	|--- service
		|	|	|		|	|--- repository
		|--- [resources]
		|	|--- com
		|	|	|--- douzone
		|	|	|		|--- mysite
		|	|	|		|	|--- config
		|	|	|		|	|	|--- app
		|	|	|		|	|	|	|--- jdbc.propertis
		|	|	|		|	|	|	|--- mybatis
		|	|	|		|	|	|	|	|--- configuration.xml
		|	|	|		|	|	|	|	|--- mappers
		|	|	|		|	|	|	|	|	|--- board.xml
		|	|	|		|	|	|	|	|	|--- user.xml
		|	|	|		|	|	|--- web
		|	|	|		|	|	|	|--- fileupload.properties
		|	|	|		|	|	|	|--- message
		|	|	|		|	|	|	|	|--- message_ko.properties
		|	|	|		|	|	|	|	|--- message_jp.properties
</pre>