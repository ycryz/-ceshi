package cn.jx.ceshi.ceshi.controller;


import cn.jx.ceshi.ceshi.common.AjaxResult;
import cn.jx.ceshi.ceshi.common.RedisCommon;
import cn.jx.ceshi.ceshi.entity.Departmententity;
import cn.jx.ceshi.ceshi.entity.OrderInfoentity;
import cn.jx.ceshi.ceshi.mapper.DepartmentMapper;
import cn.jx.ceshi.ceshi.mapper.OderInfoMapper;
import cn.jx.ceshi.ceshi.service.OrderInfoService;
import cn.jx.ceshi.ceshi.service.RedisService;
import cn.jx.ceshi.ceshi.vo.DeleteIds;
import cn.jx.ceshi.ceshi.vo.PageVo;
import cn.jx.ceshi.ceshi.vo.fenpaiVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OderInfoMapper orderInfoMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RedisService redisService;
    /**
     * 添加任务订单
     * @param orderInfoentity
     * @return
     */
    @PostMapping("/add")
    public AjaxResult addOrder(@RequestBody OrderInfoentity orderInfoentity){
        log.info("添加的参数为：{}", orderInfoentity);
        int insert = orderInfoService.insert(orderInfoentity);
        if(insert<=0){
            return AjaxResult.error("添加订单失败");
        }else {
            return AjaxResult.success("添加订单成功");
        }
    }

    /**
     * 修改任务信息
     * @param orderInfoentity
     * @return
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody OrderInfoentity orderInfoentity){
        QueryWrapper<OrderInfoentity> orderInfoentityQueryWrapper = new QueryWrapper<>();
        orderInfoentityQueryWrapper.eq("id", orderInfoentity.getId());
        int update = orderInfoMapper.update(orderInfoentity, orderInfoentityQueryWrapper);
        if(update<=0){
            return AjaxResult.error("修改订单失败");
        }
        return AjaxResult.success("修改订单成功");

    }

    /**
     * 批量删除任务订单
     * @param DeleteIds
     * @return
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody DeleteIds DeleteIds){
        log.info("删除的参数为：{}", DeleteIds);
        int i = orderInfoMapper.deleteBatchIds(DeleteIds.getIds());
        if(i<=0){
            return AjaxResult.error("删除订单失败");
        }else {
            return AjaxResult.success("删除订单成功");
        }
    }

    /**
     * 查询所有订单任务
     * @param pageVo
     * @return
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody PageVo pageVo){
        if (pageVo.getPage()==0 )pageVo.setPage(1);
        if (pageVo.getPageSize()==0)pageVo.setPageSize(10);
        log.info("分页参数为：page:{},pageSize:{}",pageVo.getPage(),pageVo.getPageSize());
        PageHelper.startPage(pageVo.getPage(),pageVo.getPageSize());
        List<OrderInfoentity> orderInfoentities = orderInfoMapper.selectList(null);
        PageInfo<OrderInfoentity> pageInfo = new PageInfo<>(orderInfoentities);
        return AjaxResult.success(pageInfo);
    }
    @PostMapping("/fenpai")
    public AjaxResult fenpai(@RequestBody fenpaiVo fenpaiVo){
        log.info("分派任务的参数为：{}", fenpaiVo);
        if (fenpaiVo.getDeptNo().isBlank()){
            return AjaxResult.error("请输入处理部门的id");
        }
        if (fenpaiVo.getOrderId().isBlank()){
            return AjaxResult.error("请输入订单编号");
        }
        if (fenpaiVo.getDeptName().isBlank()){
            return AjaxResult.error("请输入处理部门的名称");
        }
        String fenpaimsg=orderInfoService.fenpai(fenpaiVo);
        return AjaxResult.success(fenpaimsg);
    }

    /**
     * 查询7月每天的工单总量、超期率
     * 超期率=超期工单总量/工单总量
     * @param
     * @return
     */
    @PostMapping("/geteveryday")
    public AjaxResult geteveryday(){
        Map<String, Object> successData = new HashMap<>();
        String Key="order_info:July_month:geteveryday";
        if (redisService.getCacheMap(Key).isEmpty()){
            List<OrderInfoentity> orderInfoentities = orderInfoMapper.selectList(null);
            //查询7月每天的工单
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Map<LocalDate, List<OrderInfoentity>> groupMap = orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07"))
                    .collect(Collectors.groupingBy(x -> {
                        if (x.getCreateTime()!= null) {
                            LocalDate localDate = LocalDate.parse(x.getCreateTime().substring(0, 10), formatter);
                            return localDate;
                        }
                        return null;
                    }));
            HashMap<String, Integer>  groupMapEveryTotal= new HashMap<>();
            groupMap.forEach((k, v)->{
                groupMapEveryTotal.put(k.toString(), v.size());
            });
            log.info("groupMapEveryTotal:{}",groupMapEveryTotal);
            //查询7月每天的超期工单
            Map<LocalDate, List<OrderInfoentity>> groupMapIsOverdue=orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07")&& detail.getIsOverdue()==1)
                    .collect(Collectors.groupingBy(x -> {
                        if (x.getCreateTime()!= null) {
                            LocalDate localDate = LocalDate.parse(x.getCreateTime().substring(0, 10), formatter);
                            return localDate;
                        }
                        return null;
                    }));
            log.info("groupMapIsOverdue:{}",groupMapIsOverdue);
            HashMap<String, BigDecimal> stringBigDecimalHashMap = new HashMap<>();
            groupMap.forEach((k, v)->{
                if (groupMapIsOverdue.containsKey(k)){
                    BigDecimal divide = new BigDecimal(groupMapIsOverdue.get(k).size()).divide(new BigDecimal(v.size()), 2, BigDecimal.ROUND_HALF_UP);
                    stringBigDecimalHashMap.put(k.toString(),divide);
                }
            });
            log.info("stringBigDecimalHashMap:{}",stringBigDecimalHashMap);

            successData.put("groupMapEveryTotal",groupMapEveryTotal);
            successData.put("stringBigDecimalHashMap",stringBigDecimalHashMap);
            redisService.setCacheMap(Key,successData);
            if (successData.isEmpty()){
                return AjaxResult.error("没有查询到数据");
            }
        }else {
            successData=redisService.getCacheMap(Key);
        }
        return AjaxResult.success(successData);
    }
    /**
     * 查询7月每个部门的工单总量、超期率。
     *超期率=超期工单总量/工单总量
     * @param
     * @return
     */
    @PostMapping("/geteverydept")
    public AjaxResult geteverydept(){
        Map<String, Object> successData = new HashMap<>();
        String Key="order_info:July_month:geteverydept";
        if (redisService.getCacheMap(Key).isEmpty()){
            List<OrderInfoentity> orderInfoentities = orderInfoMapper.selectList(null);
            List<Departmententity> departmententities = departmentMapper.selectList(null);
            //查询7月每个部门的工单
            Map<Integer, List<OrderInfoentity>> groupMap = orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07"))
                    .collect(Collectors.groupingBy(x -> x.getHandleDeptId()));
            HashMap<String, Integer>  groupMapEveryDeptTotal= new HashMap<>();
            groupMap.forEach((k, v)->{
                String deptName = departmententities.stream()
                        .filter(department -> department.getDeptId() == k)
                        .map(Departmententity::getDeptName)
                        .findFirst()
                        .orElse(null);
                groupMapEveryDeptTotal.put(deptName, v.size());
            });
            log.info("groupMapEveryTotal:{}",groupMapEveryDeptTotal);
            //查询7月每个部门的超期工单
            Map<Integer, List<OrderInfoentity>> groupMapIsOverdue=orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07")&& detail.getIsOverdue()==1)
                    .collect(Collectors.groupingBy(x -> x.getHandleDeptId()));
            HashMap<String, BigDecimal> stringBigDecimalHashMapDept = new HashMap<>();
            groupMap.forEach((k, v)->{
                if (groupMapIsOverdue.containsKey(k)){
                    String deptName = departmententities.stream()
                            .filter(department -> department.getDeptId() == k)
                            .map(Departmententity::getDeptName)
                            .findFirst()
                            .orElse(null);
                    BigDecimal divide = new BigDecimal(groupMapIsOverdue.get(k).size()).divide(new BigDecimal(v.size()), 2, BigDecimal.ROUND_HALF_UP);
                    stringBigDecimalHashMapDept.put(deptName,divide);
                }
            });
            log.info("stringBigDecimalHashMap:{}",stringBigDecimalHashMapDept);
            successData.put("groupMapEveryTotal",groupMapEveryDeptTotal);
            successData.put("stringBigDecimalHashMapDept",stringBigDecimalHashMapDept);
            redisService.setCacheMap(Key,successData);
            if (successData.isEmpty()){
                return AjaxResult.error("没有查询到数据");
            }
        }else {
            successData=redisService.getCacheMap(Key);
        }
        return AjaxResult.success(successData);
    }
    /**
     * 查询7月每个工单类型的工单总量、超期率。
     *超期率=超期工单总量/工单总量
     * @param
     * @return
     */
    @PostMapping("/geteveryorderType")
    public AjaxResult geteveryorderType(){
        Map<String, Object> successData = new HashMap<>();
        String Key="order_info:July_month:geteveryorderType";
        if (redisService.getCacheMap(Key).isEmpty()){
            //加工逻辑
            List<OrderInfoentity> orderInfoentities = orderInfoMapper.selectList(null);
            //查询7月每个工单类型的工单
            Map<String, List<OrderInfoentity>> groupMap = orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07"))
                    .collect(Collectors.groupingBy(x -> x.getOrderType()));
            HashMap<String, Integer>  groupMapEveryOrderTypeTotal= new HashMap<>();
            groupMap.forEach((k, v)->{
                String orderTypeName=this.orderTypeName(k);
                groupMapEveryOrderTypeTotal.put(orderTypeName, v.size());
            });
            log.info("groupMapEveryOrderTypeTotal:{}",groupMapEveryOrderTypeTotal);
            //查询7月每个工单类型的超期工单
            Map<String, List<OrderInfoentity>> groupMapIsOverdue=orderInfoentities.stream()
                    .filter(detail -> detail.getCreateTime()!= null && detail.getCreateTime().startsWith("2024-07")&& detail.getIsOverdue()==1)
                    .collect(Collectors.groupingBy(x -> x.getOrderType()));
            HashMap<String, BigDecimal> stringBigDecimalHashMapDept = new HashMap<>();
            groupMap.forEach((k, v)->{
                if (groupMapIsOverdue.containsKey(k)){
                    BigDecimal divide = new BigDecimal(groupMapIsOverdue.get(k).size()).divide(new BigDecimal(v.size()), 2, BigDecimal.ROUND_HALF_UP);
                    String orderTypeName=this.orderTypeName(k);
                    stringBigDecimalHashMapDept.put(orderTypeName,divide);
                }
            });
            log.info("stringBigDecimalHashMap:{}",stringBigDecimalHashMapDept);
            successData.put("groupMapEveryOrderTypeTotal",groupMapEveryOrderTypeTotal);
            successData.put("stringBigDecimalHashMapDept",stringBigDecimalHashMapDept);
            redisService.setCacheMap(Key,successData);
            if (successData.isEmpty()){
                return AjaxResult.error("没有查询到数据");
            }

            redisService.setCacheMap(Key,successData);
        }else {
            successData=redisService.getCacheMap(Key);
        }
        return AjaxResult.success(successData);
    }

    private String orderTypeName(String k) {
        if (k==null){
            return "未知工单";
        }
        switch (k){
            case "0":
                return "交办工单";
            case "1":
                return "直接答复工单";
            case "3":
                return "无效工单";
            default:
                return "未知工单";
        }
    }


}
