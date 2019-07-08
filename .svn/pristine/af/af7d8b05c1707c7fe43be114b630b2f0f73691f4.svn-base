package com.frxs.check.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frxs.check.PackCaseActivity;
import com.frxs.check.R;
import com.frxs.check.model.GiftsCheckItem;
import com.frxs.check.model.SectionListItem;

import java.util.List;

public class GiftsListAdapter extends BaseAdapter
{
	
	private PackCaseActivity context;
	
	private List<SectionListItem> selectAddressItems;
	
	private LayoutInflater inflater;
	
	private boolean isDelete = false; // 标识列表是否是在删除状态
	
	/**
	 * 构造函数
	 */
	public GiftsListAdapter(PackCaseActivity context, List<SectionListItem> selectAddressItems)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.selectAddressItems = selectAddressItems;
	}
	
	public void setDeleteMode(boolean isDelete)
	{
		this.isDelete = isDelete;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return ((SectionListItem) getItem(position)).type;
	}
	
	@Override
	public int getViewTypeCount()
	{
		return 2;
	}
	
	@Override
	public int getCount()
	{
		return selectAddressItems.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return selectAddressItems.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		int itemViewType = getItemViewType(position);
		
		SectionListItem item = (SectionListItem) getItem(position);
		
		switch (itemViewType)
		{
		case SectionListItem.TITLE:
		{
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_gifts_title, null);
				viewHolder = new ViewHolder();
				viewHolder.tvAhelfareaName = (TextView) convertView.findViewById(R.id.tv_ahelfarea_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String section = item.getSection();
			viewHolder.tvAhelfareaName.setText(section);
			break;
		}
		case SectionListItem.ITEM:
		{
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_gifts_content, null);
				viewHolder = new ViewHolder();
//				viewHolder.tvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
//				viewHolder.tvBarCode = (TextView) convertView.findViewById(R.id.tv_barcode);
//				viewHolder.tvCheckCount = (TextView) convertView.findViewById(R.id.tv_check_count);
//				viewHolder.tvDeliveryCount = (TextView) convertView.findViewById(R.id.tv_delivery_count);
				viewHolder.tvGoodInfo = (TextView) convertView.findViewById(R.id.tv_good_info);
				convertView.setTag(viewHolder);
				
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			GiftsCheckItem checkItem = (GiftsCheckItem) item.getItem();
//			viewHolder.tvProductName.setText(checkItem.getProductName());
//			viewHolder.tvCheckCount.setText("对货数量：" + checkItem.getQty() + checkItem.getSaleUnit());
//			viewHolder.tvBarCode.setText("商品编码：" + checkItem.getSKU());
//			viewHolder.tvDeliveryCount.setText("应发数量：" + checkItem.getBuyQty() + checkItem.getSaleUnit());
			viewHolder.tvGoodInfo.setText(String.format(context.getString(R.string.gift_good), checkItem.getSKU(),
					checkItem.getProductName(), checkItem.getQty() , checkItem.getSaleUnit(), checkItem.getBuyQty() , checkItem.getSaleUnit()));
			break;
		}
		}
		
		return convertView;
	}
	
	static class ViewHolder
	{
//		TextView tvProductName;
//
//		TextView tvBarCode;
//
//		TextView tvCheckCount;
//
//		TextView tvDeliveryCount;
//
		TextView tvAhelfareaName;

		TextView tvGoodInfo;
	}
}
