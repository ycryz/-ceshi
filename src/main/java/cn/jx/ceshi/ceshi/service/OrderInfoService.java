package cn.jx.ceshi.ceshi.service;


import cn.jx.ceshi.ceshi.entity.OrderInfoentity;
import cn.jx.ceshi.ceshi.vo.fenpaiVo;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
public interface OrderInfoService  extends IService<OrderInfoentity> {
    int insert(OrderInfoentity orderInfoentity);

    String fenpai(fenpaiVo fenpaiVo);
}
