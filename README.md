# mall-server
电商后台ssm框架：spring+springMVC+Mybatis
主要有5个模块：用户模块、商品模块、购物车模块、地址模块、支付模块
## 用户模块
    表现层：controller包下UserController
    业务层：service包下impl包下的UserServiceImpl
    数据提供:dao包下UserMapper
    SQL语句：resources包下mappers包下UserMapper.xml
### 主要接口
*   
   登录接口
*
    用户退出接口
*   
   注册的接口
*
    校验接口
*   
   登录情况下获取用户信息接口
*
    查询忘记密码的问题是什么接口
*   
   校验找回密码问题
*
    密码问题重置密码接口
*   
   登录状态下重置密码接口
*
    更新用户信息接口
*   
   获取用户信息接口
## 商品模块
    表现层：controller包下ProductController、ProductManageController
    业务层：service包下impl包下的ProductServiceImpl、CategoryServiceImpl
    数据提供:dao包下ProductMapper、CategoryMapper
    SQL语句：resources包下mappers包下dao包下ProductMapper.xml、CategoryMapper.xml
### 前台主要接口
*   
   商品详情接口
*
    商品列表接口
### 后台管理主要接口
*   
   保存商品接口
*
    修改商品销售状态接口
*   
   获取商品详情接口
*
    获取商品的列表接口
*   
   查询商品接口
*
    上传商品接口  
*   
   上传商品图片接口
*   
   增加商品分类接口
*
    修改商品的名称接口
*   
   获取商品的名称接口
*
    获取商品的列表接口
*   
   查询商品接口
*
    上传商品接口  
*   
   上传商品图片接口
## 购物车模块
    表现层：controller包下CartController
    业务层：service包下impl包下的CartServiceImpl
    数据提供:dao包下CartMapper
    SQL语句：resources包下mappers包下CartMapper.xml
### 主要接口
*   
   购物车的所以商品接口
*
    添加购物车商品接口
*   
   更新购物车接口
*
    删除购物车商品接口
*   
   商品全选、反选接口
*
    查询购物车数量接口
## 地址模块
    表现层：controller包下ShippingController
    业务层：service包下impl包下的ShippingServiceImpl
    数据提供:dao包下ShippingMapper
    SQL语句：resources包下mappers包下ShippingMapper.xml
### 主要接口
*   
   增加地址接口
*
    删除地址接口
*   
   更新地址接口
*
    地址详情接口
*   
   地址列表接口
## 支付模块
  尚未更新到github主要是面对面支付


