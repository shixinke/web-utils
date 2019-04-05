
# Web-Utils: WEB开发工具箱

[English Document](http://github.com/shixinke/web-utils/blob/master/README.md)

## 主要内容

- 请求参数解析器 : 和Spring中的@RequestBody和@RequestParam类似，将下划线的入参解析成对应的驼峰法的bean
- Sentinel客户端组件 : 使Sentinel客户端更加容易使用
- CAT客户端组件 : 使CAT客户端更加容易使用


## 用法

### 1.加载maven依赖

```xml
<dependency>
  <groupId>com.shixinke.utils</groupId>
  <artifactId>web-utils</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 2.将注解解析器加到配置中

新建拦截器配置文件　

```java
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestParameterResolver());
    }
}
```

### 3.定义接收参数的bean

```java
@Data
public class UserSearchDTO extends SearchDTO {
    private Long userId;
    private String nickname;
    private List<Long> itemIds;
    private Map<String, String> configMap;
}
```

### 4.在控制器方法中调用注解

```java
@RestController
@Slf4j
class TestController {
    
    @RequestMapping("/list")
    public ResponseDTO query(@RequestParameter UserSearchDTO searchDTO) {
        try {
            log.info(searchDTO.getUserId());
        } catch (Exception ex) {
            log.error("exception:", ex);
        }
        return ResponseDTO.success();
    }
}
```

### 5.发起请求

`http://localhost:8080/list?user_id=123&item_ids=1,2,3`

以上的请求会被解析为UserSearchDTO的Bean:
- user_id参数对应到userId
- item_ids参数对应到itemIds这个列表


## 作者

- shixinke <ishixinke@qq.com>


## License

Web-Utils is under the Apache 2.0 license. See the [LICENSE](https://github.com/shixinke/web-utils/blob/master/LICENSE.txt) file for details.
