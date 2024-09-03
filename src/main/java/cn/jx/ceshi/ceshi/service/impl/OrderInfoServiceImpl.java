package cn.jx.ceshi.ceshi.service.impl;

import cn.jx.ceshi.ceshi.common.IdGeneratorSnowflake;
import cn.jx.ceshi.ceshi.entity.Departmententity;
import cn.jx.ceshi.ceshi.entity.OrderInfoentity;
import cn.jx.ceshi.ceshi.mapper.DepartmentMapper;
import cn.jx.ceshi.ceshi.mapper.OderInfoMapper;
import cn.jx.ceshi.ceshi.service.OrderInfoService;
import cn.jx.ceshi.ceshi.vo.fenpaiVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OderInfoMapper, OrderInfoentity> implements OrderInfoService {
    @Autowired
    private OderInfoMapper orderInfoMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private IdGeneratorSnowflake idGenerator;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public int insert(OrderInfoentity orderInfoentity) {
        Long orderNo = idGenerator.snowflakeId();
        String orderNoString = orderNo.toString();
        orderInfoentity.setOrderNo(orderNoString);
        String formattedTime = formatter.format(new Date());
        orderInfoentity.setCreateTime(formattedTime);
        log.info("插入的参数为：{}", orderInfoentity);
        int insert = orderInfoMapper.insert(orderInfoentity);
        return insert;
    }

    @Override
    public String fenpai(fenpaiVo fenpaiVo) {
        log.info("fenpaiVo:{}", fenpaiVo);
        OrderInfoentity orderInfoentity = orderInfoMapper.selectById(fenpaiVo.getOrderId());
        if(orderInfoentity!=null){
            QueryWrapper<Departmententity> departmententityQueryWrapper = new QueryWrapper<>();
            departmententityQueryWrapper.eq("dept_id",fenpaiVo.getDeptNo());
            departmententityQueryWrapper.eq("dept_name",fenpaiVo.getDeptName());
            Departmententity departmententity = departmentMapper.selectOne(departmententityQueryWrapper);
            if (departmententity!=null){
                orderInfoentity.setHandleDeptId(Integer.valueOf(fenpaiVo.getDeptNo()));
                orderInfoentity.setFenpaiTime(formatter.format(new Date()));
                int i = orderInfoMapper.updateById(orderInfoentity);
                return i<=0?"分派失败" : "分派成功" ;
            }else {
                return "查询处理部门不存在";
            }
        }
        return "查询订单号失败";
    }
}
