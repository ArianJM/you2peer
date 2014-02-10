package com.u2p.ui.adapters;

import java.util.ArrayList;

import com.u2p.ui.R;
import com.u2p.ui.component.ItemFile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemFileAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ItemFile> items;
	
	public ItemFileAdapter(Activity activity, ArrayList<ItemFile> items) {
		this.activity = activity;
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int pos) {
		return items.get(pos);
	}

	public long getItemId(int pos) {
		return items.get(pos).getId();
	}

	public View getView(int position, View contentView, ViewGroup parent) {
		View vi = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.list_item_layout, null);
		}
		
		ItemFile item = items.get(position);
		
		ImageView image = (ImageView) vi.findViewById(R.id.imagen);
		int imageResource = activity.getResources().getIdentifier(item.getRutaImagen(), null, activity.getPackageName());
		image.setImageDrawable(activity.getResources().getDrawable(imageResource));
		
		TextView nombre = (TextView) vi.findViewById(R.id.nombre);
		nombre.setText(item.getName());
		
		TextView usuario = (TextView) vi.findViewById(R.id.usuario);
		usuario.setText(item.getUser());
		
		TextView size = (TextView) vi.findViewById(R.id.size);
		size.setText(item.getSize());
		
		TextView rating = (TextView) vi.findViewById(R.id.rating);
		rating.setText(item.getRating());
		
		return vi;
	}

}
