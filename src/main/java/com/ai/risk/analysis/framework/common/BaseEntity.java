package com.ai.risk.analysis.framework.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity 基础类
 *
 * @author Steven
 */
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

}
