体要求：
1.框架使用：SpringBoot、SpringMVC、MyBatis-Plus、MySQL、Redis
 2.请按照Java开发规范书写代码。
1.表设计。（必选）
 工单表：
  字段包括：
   工单id(id)(自增，必填)
   工单编号(order_no)(必填)
   工单类型(order_type)(必填)0交办 1直接答复 3无效工单
   标题(title)(必填)
   内容(content)(必填)
   处理部门(handle_dept_id)
   创建时间(create_time)(必填)
   分派时间(fenpai_time)
   是否超期(is_overdue) 0否 1是
 部门表（数据在数据库中维护，不少于三条数据）：
  包括字段：
   部门id（dept_id）(必填)
   部门名称（dept_name）(必填)
2.实现该表的增(post /order/save)删(post /order/delete)改(post /order/update)查(post /order/search，需要分页)功能（必选）
 要求：
  1.添加，修改功能工单编号不能重复，必填项必须填写。
3.实现分派接口(post /order/fenpai)，接口参数(工单id，处理部门id，处理部门名称)，分派的时候，后台补填分派时间（必选）
 要求：
  1.分派时，必须验证部门id是否有效。
提示：
 超期率=超期工单总量/工单总量
4.查询7月每天的工单总量、超期率 （三选二）
5.查询7月每个部门的工单总量、超期率。（三选二）
6.查询7月每个工单类型的工单总量、超期率。（三选二）
