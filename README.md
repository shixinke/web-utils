
# Web-Utils: Toolbox commonly used in web development

[中文文档](http://github.com/shixinke/web-utils/blob/master/README_zh.md)

## Contents

- Request Parameter Parser : Like @RequestBody and @RequestParam in Spring
- Sentinel Client Component : Make sentinel easier to use
- CAT Client Component : Make CAT easier to use


## Usage

### Step1 : add maven dependency

```xml
<dependency>
  <groupId>com.shixinke.utils</groupId>
  <artifactId>web-utils</artifactId>
  <version>1.0.2</version>
</dependency>
```

### Step2 : add configuration


```java
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestParameterResolver());
    }
}
```

### step3 : define a bean for request parameters

```java
@Data
public class UserSearchDTO extends SearchDTO {
    private Long userId;
    private String nickname;
    private List<Long> itemIds;
    private Map<String, String> configMap;
}
```

### Step4:Use annotations in the controller

```java
@RestController
@Slf4j
class TestController {
    
    @RequestMapping("/list")
    public ResponseDTO query(@RequestParameter SearchDTO searchDTO) {
        try {
            log.info(searchDTO.getPage());
        } catch (Exception ex) {
            log.error("exception:", ex);
        }
        return ResponseDTO.success();
    }
}
```

### step5:send a request 


`http://localhost:8080/list?user_id=123&item_ids=1,2,3`

`UserSearchDTO`:

- userId -> 123
- itemIds -> [1,2,3]



## Author

- shixinke <ishixinke@qq.com>


## License

Web-utils is under the Apache 2.0 license. See the [LICENSE](https://github.com/shixinke/web-utils/blob/master/LICENSE.txt) file for details.
