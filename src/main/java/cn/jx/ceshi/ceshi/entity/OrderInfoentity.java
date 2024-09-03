package cn.jx.ceshi.ceshi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
public class OrderInfoentity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *工单编号
     */
    private String orderNo;
    /**
     *工单类型
     */
    private String orderType;
    /**
     *标题
     */
    private String title;
    /**
     *内容
     */
    private String content;
    /**
     *处理部门
     */
    private Integer handleDeptId;
    /**
     *创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    /**
     *分派时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String fenpaiTime;
    /**
     *是否超期
     */
    private Integer isOverdue;
}
