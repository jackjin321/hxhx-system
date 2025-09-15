/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Getter
@Setter
//public class ProductDto extends BaseDTO implements Serializable {
public class ProductDto implements Serializable {

	/**
	 * 应用编号
	 */
    private Long id;


	private String productName;


	private String applyLink;
	/**
	 * 状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架
	 */
	private String status;

	private String imageUrl;

	/**
	 * 跳转类型，direct，加白访问，normal，特殊访问
	 */
	private String jumpType;
	private String cornerMark;
	private Integer minAmount;
	private Integer maxAmount;
	private Integer minMonth;
	private Integer maxMonth;
	private String rate;
	private String applyCondition;

	/**
	 * 申请人数
	 */
	private Integer applyNum;

	public String getStrMinLoan() {
		if (minAmount == null) {
			return null;
		}
		return moneyFormat(this.minAmount + "");
	}

	public String getStrMaxLoan() {
		if (maxAmount == null) {
			return null;
		}
		return moneyFormat(this.maxAmount + "");
	}

	private static String moneyFormat(String money) {
		int count = 0;
		int len = money.length() - 1;
		while (len >= 0) {
			if (Character.compare(money.toCharArray()[len], (char) 48) == 0) {
				count++;
				len--;

			} else {
				break;
			}
		}
		String showMoney = money.substring(0, money.length() - (count <= 4 ? count : count >= 8 ? 8 : 4));
		if (count == 2) {
			return showMoney += "百";
		}
		if (count == 3) {
			return showMoney += "千";
		}
		if (count >= 4 && count < 8) {
			return showMoney += "万";
		} else if (count >= 8) {
			return showMoney += "亿";
		} else {
			return showMoney;
		}
	}
}
