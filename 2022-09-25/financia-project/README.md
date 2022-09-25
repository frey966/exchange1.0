## 系统模块
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
com.financia     
├── financia-ui              // 前端框架 [80]
├── financia-app-gateway     // 网关模块 [8602]
├── financia-manage-geteway  // 网关模块 [8601]
├── financia-manage-auth     // 管理认证中心 [9200]
├── financia-app-auth        // app认证中心 [9114] 
├── financia-fegin-api       // 接口模块
│       └── financia-api-system                          // 系统接口
├── financia-common          //通用模块
│       └── financia-common-core                         // 核心模块
│       └── financia-common-datascope                    // 权限范围
│       └── financia-common-datasource                   // 多数据源
│       └── financia-common-log                          // 日志记录
│       └── financia-common-redis                        // 缓存服务
│       └── financia-common-security                     // 安全模块
        └── financia-common-model                        // 安全模块
│       └── financia-common-swagger                      // 系统接口
├── financia-manage     // 管理平台业务模块
│       └── financia-business                            // 业务模块 [9201]
│       └── financia-file                                // 文件服务 [9202]
│       └── financia-gen                                 // 代码生成 [9202]
│       └── financia-system                              // 系统模块 [9204]
├── financia-app-api        // 业务模块
│       └── financia-app-websock                            // websock交易所模块 [9102]
│       └── financia-app-exchange                           // 交易所模块 [9103]
│       └── financia-app-common                             // 公共模块   [9104]
│       └── financia-senta                                  // 分布式事务 [9105]
│       └── financia-app-stock                              // 交易-股票交易 [9107]
│       └── financia-app-swap                               // 交易-合约交易 [9108]
│       └── financia-app-conversion                         // 汇兑业务 [9109]  
│       └── financia-app-quantitative                       // 量化理财业务[9110]  
│       └── financia-app-quotes                             // 行情模块 [9101]
│       └── financia-app-super_leverage                     // 交易-超级杠杆交易 [9111]  
│       └── financia-app-currency_trading                   // 交易-币币交易 [9112]
│       └── financia-app-cs                                 // 在线客服平台 [9113]  
├── financia-visual                                         // 图形化管理模块
│       └── financia-visual-monitor                         // 监控中心 [9100]
├──pom.xml                                                  // 公共依赖
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## 架构图
## 内置功能  暂时取消掉
1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 在线体验
- admin/admin123

