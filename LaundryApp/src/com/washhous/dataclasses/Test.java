package com.washhous.dataclasses;
//package com.lupin.remedies;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Stack;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lupin.remedies.database.SQLDataHelper;
//
//@SuppressLint({ "Recycle", "ShowToast" })
//public class Test extends BaseAdapter {
//
//	SQLDataHelper helper;
//	// Context context;
//	private Activity activity;
//	ArrayList<ProductData> arraylist;
//	int fava = 0;
//
//	// private ArrayList<String> data = new ArrayList<String>();
//	private List<ProductData> productList = new ArrayList<ProductData>();
//	// private List<OrderdetailData> orderdetailList = new
//	// ArrayList<OrderdetailData>();
//	private ImageLoader imageLoader;
//	protected SharedPreferences sharedPref;
//	protected boolean isRegisterd;
//	protected Editor editor;
//	private static LayoutInflater inflater = null;
//
//	public ProductAdapter(Activity a, List<ProductData> productLists,
//			ArrayList<String> mStrings) {
//		// this.context = a;
//		activity = a;
//		// data = mStrings;
//		this.productList = productLists;
//
//		this.arraylist = new ArrayList<ProductData>();
//		this.arraylist.addAll(productLists);
//		inflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		imageLoader = new ImageLoader(activity.getApplicationContext());
//		Log.d("size", productList.size() + "");
//		sharedPref = activity.getSharedPreferences("check", 0);
//		editor = sharedPref.edit();
//
//	}
//
//	public ProductAdapter() {
//
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return productList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	public class ViewHolder {
//
//		TextView tvProductName;
//		TextView tvProductBrand;
//		TextView tvProductPack;
//		TextView tvProductMrp;
//		// ImageView imageFavourite;
//		ImageView imageAddToCart;
//		ImageView imagePlus;
//		ImageView imageMinus;
//		ImageView imageProduct;
//		EditText tvCartCount;
//		LinearLayout layoutMain;
//
//	}
//
//	private class MyTextWatcher implements TextWatcher {
//
//		private View view;
//
//		private MyTextWatcher(View view) {
//			this.view = view;
//		}
//
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			// do nothing
//
//		}
//
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//			// do nothing
//		}
//
//		public void afterTextChanged(Editable s) {
//
//			EditText qtyView = (EditText) view.findViewById(R.id.txt_cartCount);
//
//			// System.out.println("position " + position);
//
//			String cartcount = qtyView.getText().toString();
//
//			if (cartcount.equals("0")) {
//				qtyView.setText("1");
//				Toast.makeText(activity, "Please enter valid amount",
//						Toast.LENGTH_SHORT).show();
//			}
//
//		}
//	}
//
//	@SuppressLint("InflateParams")
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = null;
//		View vi = null;
//		ViewHolder holder = null;
//		if (vi == null) {
//
//			vi = inflater.inflate(R.layout.product_list_item, null);
//
//			helper = new SQLDataHelper(activity);
//
//			String id = productList.get(position).getId();
//			Log.d("id", id);
//			holder = new ViewHolder();
//			holder.layoutMain = (LinearLayout) vi
//					.findViewById(R.id.linear_main_layout);
//
//			holder.tvProductName = (TextView) vi
//					.findViewById(R.id.txt_productName);
//			holder.tvProductBrand = (TextView) vi
//					.findViewById(R.id.txt_productBrand);
//			holder.tvProductPack = (TextView) vi
//					.findViewById(R.id.txt_productPack);
//			holder.tvProductMrp = (TextView) vi
//					.findViewById(R.id.txt_product_mrp);
//			holder.tvCartCount = (EditText) vi.findViewById(R.id.txt_cartCount);
//
//			// holder.imageFavourite = (ImageView)
//			// vi.findViewById(R.id.favorite);
//			holder.imageAddToCart = (ImageView) vi
//					.findViewById(R.id.image_addtocart);
//
//			holder.imagePlus = (ImageView) vi.findViewById(R.id.imagePlus);
//			holder.imageMinus = (ImageView) vi.findViewById(R.id.imageMinus);
//			ImageView imgProduct = (ImageView) vi
//					.findViewById(R.id.imgv_product_image);
//			SQLDataHelper helper1 = new SQLDataHelper(activity);
//			imageLoader.DisplayImage(productList.get(position).getpImage(),
//					imgProduct);
////			boolean imagechanged = helper1.checkimageChanged(productList.get(
////					position).getpCode());
////			// DisplayImage function from ImageLoader Class
////			if (imagechanged) {
////				imageLoader.DisplayImage(productList.get(position).getpImage(),
////						imgProduct, true);
////			} else {
////				imageLoader.DisplayImage(productList.get(position).getpImage(),
////						imgProduct, false);
////			}
//			holder.tvProductName.setText(productList.get(position)
//					.getProductName());
//			holder.tvProductBrand
//					.setText(productList.get(position).getPBrand());
//			holder.tvProductPack.setText(productList.get(position).getpPack());
//			holder.tvProductMrp.setText(productList.get(position).getpMrp());
//			holder.tvCartCount.addTextChangedListener(new MyTextWatcher(vi));
//
//			String productCode = helper.checkProductCodeAddToCart(productList
//					.get(position).getpCode());
//			if (productCode != null) {
//
//				holder.imageAddToCart.setImageResource(R.drawable.cartfill);
//			} else {
//
//				holder.imageAddToCart.setImageResource(R.drawable.cart);
//			}
//
//			/*
//			 * // holder.tvCartCount.setText(cartCount);
//			 * holder.imageFavourite.setTag(position + "");
//			 * 
//			 * String fav = productList.get(position).getFavourite(); if (fav !=
//			 * null) { if (fav.equals("1")) { holder.imageFavourite
//			 * .setImageResource(R.drawable.favoritedetail); fava = 1; } }
//			 */
//
//			holder.layoutMain.setTag(position + "");
//			holder.layoutMain.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					int position = Integer.parseInt((String) v.getTag());
//					Bundle bundle = new Bundle();
//
//					bundle.putString("ProductId", productList.get(position)
//							.getId());
//
//					bundle.putString("ProductQuantity",
//							productList.get(position).getQuantitiy());
//
//					Fragment fragment;
//					fragment = new ProductDetail();
//					fragment.setArguments(bundle);
//
//					FragmentManager fragmentManager = ((MyApplication) activity
//							.getApplication()).getManager();
//					FragmentTransaction ft = fragmentManager.beginTransaction();
//					Stack<Fragment> stk = ((MyApplication) activity
//							.getApplication()).getFragmentStack();
//					if (stk.size() > 0) {
//						stk.lastElement().onPause();
//						ft.hide(stk.lastElement());
//						stk.push(fragment);
//					}
//
//					ft.add(R.id.content_frame, fragment).commit();
//				}
//
//			});
//
//			/*
//			 * holder.imageFavourite.setTag(position + "");
//			 * holder.imageFavourite.setOnClickListener(new OnClickListener() {
//			 * 
//			 * @Override public void onClick(View v) { // TODO Auto-generated
//			 * method stub Log.d("call", "call"); ImageView view = (ImageView)
//			 * v; int position = Integer.parseInt((String) v.getTag());
//			 * 
//			 * int i = helper.updateFavourite(productList.get(position)
//			 * .getId()); if (i == 1) { fava = 1;
//			 * view.setImageResource(R.drawable.favoritedetail);
//			 * Toast.makeText(v.getContext(),
//			 * "Successfully added item to Favorites",
//			 * Toast.LENGTH_SHORT).show(); } else {
//			 * view.setImageResource(R.drawable.favorite); fava = 0;
//			 * 
//			 * Toast.makeText(v.getContext(),
//			 * "Item has been removed from Favorites",
//			 * Toast.LENGTH_SHORT).show(); }
//			 * 
//			 * } });
//			 */
//			holder.imageAddToCart.setTag(position + "");
//			holder.imageAddToCart.setOnClickListener(new OnClickListener() {
//
//				@SuppressLint("ShowToast")
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					isRegisterd = sharedPref.getBoolean("isRegister", false);
//					editor.putBoolean("fromsplashscreen", false);
//					if (isRegisterd) {
//						ImageView view = (ImageView) v;
//						View v1 = (View) v.getParent();
//						int position = Integer.parseInt((String) v.getTag());
//						EditText tvCartCount = (EditText) v1
//								.findViewById(R.id.txt_cartCount);
//						String cartquantity = tvCartCount.getText().toString();
//
//						if (cartquantity.equals("") || cartquantity.equals("0")) {
//							Toast.makeText(v.getContext(),
//									"Please enter valid cart count",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							helper.updateCartcount(productList.get(position)
//									.getpCode(), cartquantity);
//							view.setImageResource(R.drawable.cartfill);
//
//							Toast.makeText(v.getContext(),
//									"Successfully added item to order list",
//									Toast.LENGTH_SHORT).show();
//							tvCartCount.setText("1");
//
//						}
//
//					} else {
//
//						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//								activity);
//						alertDialogBuilder.setTitle("Lupin Registration");
//						alertDialogBuilder
//								.setMessage("Please register to proceed.")
//								.setCancelable(false)
//								.setPositiveButton("Cancel",
//										new DialogInterface.OnClickListener() {
//
//											public void onClick(
//													DialogInterface dialog,
//													int id) {
//												dialog.cancel();
//
//											}
//										})
//								.setNegativeButton("Register",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int id) {
//												// if this button is clicked,
//												// just close
//												// the dialog box and do nothing
//
//												// if this button is clicked,
//												// close
//												// current activity
//												// final Stack<Fragment> stk =
//												// ((MyApplication)
//												// getApplication())
//												// .getFragmentStack();
//												// if (stk.size() > 0) {
//												// for (int i = stk.size(); i >
//												// 0; i--)
//												// {
//												// stk.remove(0);
//												// }
//												// }
//												// finish();
//
//												editor.putBoolean(
//														"fromorderdetail", true);
//												editor.commit();
//												Intent register = new Intent(
//														activity,
//														Registration.class);
//												activity.startActivity(register);
//
//											}
//										});
//
//						// create alert dialog
//						AlertDialog alertDialog = alertDialogBuilder.create();
//
//						// show it
//						alertDialog.show();
//
//					}
//
//				}
//			});
//
//			holder.imageMinus.setTag(position + "");
//			holder.imageMinus.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					ImageView view3 = (ImageView) v;
//					View v1 = (View) v.getParent();
//					EditText tvCartCount = (EditText) v1
//							.findViewById(R.id.txt_cartCount);
//					String a = tvCartCount.getText().toString();
//					if (!a.equals("") && !a.equals("0")) {
//						Log.d("", a);
//						int cartcount = Integer.parseInt(a);
//
//						if (cartcount > 1) {
//							cartcount--;
//							String str = String.valueOf(cartcount);
//							tvCartCount.setText(str);
//						} else {
//							view3.setEnabled(false);
//						}
//					} else {
//						Toast.makeText(v.getContext(),
//								"Please enter valid cart count",
//								Toast.LENGTH_SHORT).show();
//					}
//
//				}
//			});
//			holder.imagePlus.setTag(position + "");
//			holder.imagePlus.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					ImageView view = (ImageView) v;
//					View v1 = (View) v.getParent();
//					EditText tvCartCount = (EditText) v1
//							.findViewById(R.id.txt_cartCount);
//					String a = tvCartCount.getText().toString();
//					if (!a.equals("") && !a.equals("0")) {
//						Log.d("", a);
//						int cartcount = Integer.parseInt(a);
//						cartcount++;
//
//						String str = String.valueOf(cartcount);
//						tvCartCount.setText(str);
//					} else {
//						Toast.makeText(v.getContext(),
//								"Please enter valid cart count",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//
//			holder.tvProductName.setTag(position + "");
//			holder.tvProductName.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					int position = Integer.parseInt((String) v.getTag());
//					Bundle bundle = new Bundle();
//					// bundle.putString("ProductName", productList.get(position)
//					// .getProductName());
//					bundle.putString("ProductId", productList.get(position)
//							.getId());
//
//					bundle.putString("ProductQuantity",
//							productList.get(position).getQuantitiy());
//					// bundle.putString("ProductPack", productList.get(position)
//					// .getpPack());
//					// bundle.putString("ProductMrp", productList.get(position)
//					// .getpMrp());
//					// bundle.putString("ProductTherapeuticSegment", productList
//					// .get(position).getpTherapeuticSegment());
//					// bundle.putString("ProductFavourite", fava+"");
//					Fragment fragment;
//					fragment = new ProductDetail();
//					fragment.setArguments(bundle);
//
//					FragmentManager fragmentManager = ((MyApplication) activity
//							.getApplication()).getManager();
//					FragmentTransaction ft = fragmentManager.beginTransaction();
//					Stack<Fragment> stk = ((MyApplication) activity
//							.getApplication()).getFragmentStack();
//					if (stk.size() > 0) {
//						stk.lastElement().onPause();
//						ft.hide(stk.lastElement());
//						stk.push(fragment);
//					}
//
//					ft.add(R.id.content_frame, fragment).commit();
//				}
//			});
//
//			// imageLoader.DisplayImage(url, holder.imgUser);
//		}
//		return vi;
//	}
//
//	protected void callFragmentActivity(Fragment fragment) {
//		// TODO Auto-generated method stub
//		if (fragment != null) {
//
//			FragmentManager fragmentManager = ((MyApplication) activity
//					.getApplication()).getManager();
//			FragmentTransaction ft = fragmentManager.beginTransaction();
//			Stack<Fragment> stk = ((MyApplication) activity.getApplication())
//					.getFragmentStack();
//			if (stk.size() > 0) {
//				stk.lastElement().onPause();
//				ft.hide(stk.lastElement());
//				stk.push(fragment);
//				FragmentManager fm = ((MyApplication) activity.getApplication())
//						.getManager();// or
//										// 'getSupportFragmentManager();'
//				int count = fm.getBackStackEntryCount();
//				for (int i = 0; i < count; ++i) {
//					fm.popBackStack();
//				}
//			}
//		}
//	}
//
//	public void filter(String charText) {
//		charText = charText.toLowerCase(Locale.getDefault());
//		productList.clear();
//		if (charText.length() == 0) {
//			productList.addAll(arraylist);
//		} else {
//			for (ProductData st : arraylist) {
//				if (st.getProductName().toLowerCase(Locale.getDefault())
//						.contains(charText)) {
//					productList.add(st);
//				} else if (st.getPBrand().toLowerCase(Locale.getDefault())
//						.contains(charText)) {
//					productList.add(st);
//				} else if (st.getpMrp().toLowerCase(Locale.getDefault())
//						.contains(charText)) {
//					productList.add(st);
//				}
//			}
//		}
//		notifyDataSetChanged();
//	}
//
//}
