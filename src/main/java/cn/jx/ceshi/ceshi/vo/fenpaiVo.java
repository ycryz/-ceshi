package cn.jx.ceshi.ceshi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class fenpaiVo {
    private String orderId;
    private String deptNo;
    private String deptName;
}
