package me.zhengjie.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Table(name = "xf_product_channel_filter")
@Entity
public class ProductChannelFilter extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	*产品id
	*/
	@NotNull
	@Column(name = "`product_id`")
	private Long productId;


	/**
	 *产品名称
	 */
	@Column(name = "`product_name`")
	private String productName;

	/**
	*渠道id
	*/
	@NotNull
	@Column(name = "`channel_id`")
	private Long channelId;

	/**
	*渠道名称
	*/
	@Column(name = "`channel_name`")
	private String channelName;


}
