package com.happy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 * 
 * @author Administrator
 * 
 */
public class Category {
	/**
	 * 基本状态
	 */
	public static final int DEF = 0;
	/**
	 * 删除
	 */
	public static final int DEL = 1;
	/**
	 * 状态
	 */
	private int status = DEF;

	/**
	 * 分类名
	 */
	private String mCategoryName;
	/**
	 * 分类的内容
	 */
	private List<SongInfo> mCategoryItem = new ArrayList<SongInfo>();

	public Category(String mCategroyName) {
		mCategoryName = mCategroyName;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void addItem(SongInfo songInfo) {
		mCategoryItem.add(songInfo);
	}

	/**
	 * 根据索引获取子内容
	 * 
	 * @param pPosition
	 * @return
	 */
	public Object getItem(int pPosition) {
		if (mCategoryItem.size() == 0) {
			return null;
		}
		return mCategoryItem.get(pPosition);
	}

	public int getmCategoryItemCount() {
		return mCategoryItem.size();
	}

	public List<SongInfo> getmCategoryItem() {
		return mCategoryItem;
	}

	public void setmCategoryItem(List<SongInfo> mCategoryItem) {
		this.mCategoryItem = mCategoryItem;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}