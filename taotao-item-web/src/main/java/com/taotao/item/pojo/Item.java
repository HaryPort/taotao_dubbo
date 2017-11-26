package com.taotao.item.pojo;

import org.apache.commons.lang3.StringUtils;

import com.taotao.pojo.TbItem;

public class Item extends TbItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Item(TbItem tbItem) {
		// 初始化属性
		this.setId(tbItem.getId());
		this.setCid(tbItem.getCid());
		this.setTitle(tbItem.getTitle());
		this.setStatus(tbItem.getStatus());
		this.setSellPoint(tbItem.getSellPoint());
		this.setBarcode(tbItem.getBarcode());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setImage(tbItem.getImage());
	}
	
	public String [] getImages() {
		if(StringUtils.isNotBlank(this.getImage())) {
			String imageArray = this.getImage();
			String [] sImage = imageArray.split(",");
			return sImage;
		}
		return null;
	}

}
