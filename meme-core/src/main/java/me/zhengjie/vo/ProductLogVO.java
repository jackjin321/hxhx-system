package me.zhengjie.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long productId;
}
