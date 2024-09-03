package cn.jx.ceshi.ceshi.mapper;

import cn.jx.ceshi.ceshi.entity.OrderInfoentity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OderInfoMapper  extends BaseMapper<OrderInfoentity> {
    @Select("")
    List<OrderInfoentity> selectGetEveryDay();
}
