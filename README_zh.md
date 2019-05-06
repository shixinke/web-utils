
# Web-Utils: WEB开发工具箱

[English Document](http://github.com/shixinke/web-utils/blob/master/README.md)

## 主要内容

- 请求参数解析器 : 和Spring中的@RequestBody和@RequestParam类似，将下划线的入参解析成对应的驼峰法的bean
- Sentinel客户端组件 : 使Sentinel客户端更加容易使用
- CAT客户端组件 : 使CAT客户端更加容易使用


## 用法

### 一.加载maven依赖

```xml
<dependency>
  <groupId>com.shixinke.utils</groupId>
  <artifactId>web-utils</artifactId>
  <version>1.0.2</version>
</dependency>
```

### 二.请求参数解析器的用法

#### 1.将注解解析器加到配置中

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

#### 2.定义接收参数的bean

```java
@Data
public class UserSearchDTO extends SearchDTO {
    private Long userId;
    private String nickname;
    private List<Long> itemIds;
    private Map<String, String> configMap;
}
```

#### 3.在控制器方法中调用注解

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

#### 4.发起请求

`http://localhost:8080/list?user_id=123&item_ids=1,2,3`

以上的请求会被解析为UserSearchDTO的Bean:
- user_id参数对应到userId
- item_ids参数对应到itemIds这个列表

### 三.监控组件CAT客户端的使用

#### 1.请按照CAT官方接入指南进行相关的配置，如建立文件夹等

#### 2.在配置文件中添加如下配置:

```bash
cat.enabled=true
```

- 当为true时，表示开启cat监控

#### 3.在需要监控的方法上添加@CatLog注解

```bash
@RestController
@Slf4j
@RequestMapping("/cat")
public class CatController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("list")
    @CatLog
    public ResponseDTO list(CouponSearchDTO couponSearchDTO) {
        return ResponseDTO.success(couponService.queryList(couponSearchDTO));
    }
}
```

@CatLog注解共有以下参数：

- messageType : 监控消息类型，共有以下几种
    - TRANSACTION : 一段代码
    - TRACE : Trace信息
    - METRIC : 业务指标信息
    - EVENT: 事件信息，即一行代码信息
    - TRANSACTION_WITH_DURATION : 带时间的一段代码
- methodType : 方法类型
    - SQL : 数据库操作(添加了对MyBatis的SQL语句记录)
    - API : 对外的API接口
    - SERVICE : 业务服务
    - CACHE : 缓存
    - SEARCH :　搜索引擎
    - COMPONENT : 组件
    - COMMON : 其他
- type : 监控主体的一级分类
- page : 监控主体的二级分类
- exceptionClass : 出现异常的处理类
- exceptionHandler : 出现异常的处理方法
- empty : 当出现异常时提供的空列表对象(对于查询)
- remark : 方法备注信息

### 四.限流组件Sentinel的使用

#### 1.在apollo配置中定义限流等相关的配置项

```bash
sentinel.flow.rules.prefix = sentinel.rules.flow
sentinel.degrade.rules.prefix = sentinel.rules.degrade
sentinel.system.rules.prefix = sentinel.rules.system
sentinel.authority.rules.prefix = sentinel.rules.authority
sentinel.paramFlow.rules.prefix = sentinel.rules.paramFlow

sentinel.rules.flow.couponQueryList = [{"count":10,"grade":1,"resource":"couponQueryList"}]
```

- sentinel.flow.rules.prefix : 流量限制的规则的前缀
- sentinel.degrade.rules.prefix : 降级规则的前缀
- sentinel.system.rules.prefix : 系统规则的前缀
- sentinel.authority.rules.prefix : 黑白名单的规则的前缀
- sentinel.paramFlow.rules.prefix : 热点参数规则的前缀

- sentinel.rules.flow.couponQueryList : 具体规则couponQueryList这个资源的规则

#### 2.在需要定义资源的地方添加SentinelResource的注解

```java
@RestController
@RequestMapping("/sentinel")
public class SentinelController {

    @RequestMapping("flow")
    @SentinelResource(value = "sentinelFlow")
    public ResponseDTO flow() {
        return ResponseDTO.success();
    }
}
```



## 作者

- shixinke <ishixinke@qq.com>


## License

Web-Utils is under the Apache 2.0 license. See the [LICENSE](https://github.com/shixinke/web-utils/blob/master/LICENSE.txt) file for details.
