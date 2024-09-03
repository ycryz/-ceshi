package cn.jx.ceshi.ceshi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("department")
public class Departmententity {
    /**
     * 部门id
     */
    @NonNull
    private Integer deptId;
    /**
     * 部门名称
     */
    @NonNull
    private String deptName;
}
