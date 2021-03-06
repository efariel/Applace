package com.retni.applacegps;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Fragment_conversaciones extends Fragment{	

	ProgressBar list_bar;
	
	LinearLayout msje_menu;
	Button msje_volver, msje_borrar;

	int delete;
	List<ParseObject> convers, convers4, date;
	List<ParseUser> otro;
	String idMio="mio", idOtro="otro";
	List<String> nomOtro = new ArrayList<String>();
	List<String> idOtros = new ArrayList<String>();
	List<Bitmap> fotOtro = new ArrayList<Bitmap>();
	List<String> fecha = new ArrayList<String>();
	List<String> id_conv = new ArrayList<String>();
	
	ListView list_conv;
	TextView msje;
	TransparentProgressDialog pd;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mensajes, container, false);

		return v;
	}	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);          
        
        msje = (TextView) getActivity().findViewById(R.id.list_mens);
        msje_menu = (LinearLayout) getActivity().findViewById(R.id.msje_menu);
		msje_volver = (Button) getActivity().findViewById(R.id.msje_volver);
		msje_borrar = (Button) getActivity().findViewById(R.id.msje_borrar);
		
		pd = new TransparentProgressDialog(getActivity(), R.drawable.prog_applace);
        
        updateList();
	}
	
private class TransparentProgressDialog extends Dialog {
		
		private ImageView iv;
			
		public TransparentProgressDialog(Context context, int resourceIdOfImage) {
			super(context, R.style.TransparentProgressDialog);
	        	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
	        	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
	        	getWindow().setAttributes(wlmp);
			setTitle(null);
			setCancelable(false);
			setOnCancelListener(null);
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			iv = new ImageView(context);
			iv.setImageResource(resourceIdOfImage);
			layout.addView(iv, params);
			addContentView(layout, params);
		}
			
		@Override
		public void show() {
			super.show();
			RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			anim.setInterpolator(new LinearInterpolator());
			anim.setRepeatCount(Animation.INFINITE);
			anim.setDuration(3000);					
			iv.setAnimation(anim);
			iv.startAnimation(anim);
		}
	}
	
	private void updateList() {
        // TODO Auto-generated method stub
		list_bar = (ProgressBar) getActivity().findViewById(R.id.list_bar1);
		
        new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                //list_bar.setVisibility(View.VISIBLE);
                pd.show();
                
            }
            
            protected Void doInBackground(Void... params) {
            	Bundle bundle = getArguments();
                delete = bundle.getInt("delete",0);
                
                if(delete==1){
        			msje_menu.setVisibility(View.VISIBLE);
        			//msje_volver.setOnClickListener(listener);
        		}
                
                Parse.initialize(getActivity(), "XyEh8xZwVO3Fq0hVXyalbQ0CF81zhcLqa0nOUDY3", "bK1hjOovj0GAmgIsH6DouyiWOHGzeVz9RxYc6vur");
                ParseUser user = new ParseUser();
                user = ParseUser.getCurrentUser();
                
                idMio = user.getObjectId();
                
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Conversacion");
                query.whereEqualTo("id_user1", idMio);
        		query.orderByDescending("_created_at");

        		try {
        			convers = query.find();
        		} catch (ParseException e) {}
                
                if(convers.size() == 0){
                	ParseQuery<ParseObject> query4 = new ParseQuery<ParseObject>("Conversacion");
                    query4.whereEqualTo("id_user2", idMio);
            		query4.orderByDescending("_created_at");
            		
            		try {
            			convers4 = query4.find();
            		} catch (ParseException e) {}            
                    
            		if(convers4.size() == 0){
            			//Se muestra el mensaje de que no tiene conversaciones
            			msje.setVisibility(View.VISIBLE);
            		} else{
            			ParseObject conv = null;
            			for(int i=0 ; i<convers4.size() ; i++){
            				conv = convers4.get(i);
            				id_conv.add(conv.getObjectId());
            				idOtro = conv.getString("id_user1");
            				idOtros.add(idOtro);
            				
            				ParseQuery query2 = ParseUser.getQuery();
            				query2.whereEqualTo("objectId", idOtro);
            		        
            		        try {
            					otro = query2.find();
            				} catch (ParseException e) {}
            		        
            		        ParseObject user_otro = null;
            		        user_otro = otro.get(0);
            		        
            		        nomOtro.add(user_otro.getString("NombreCompleto"));
            		        ParseFile imgOtro = user_otro.getParseFile("Foto");	
            				if(imgOtro != null){
            					imgOtro.getDataInBackground(new GetDataCallback() {
            				    	Bitmap bmp = null;
            				        public void done(byte[] data, com.parse.ParseException e) {
            				            if (e == null){
            				                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);	
            				                fotOtro.add(bmp);
            				            }
            				            else{
            				            	Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            				            }
            				        }
            				    }); 
            				} else{
            					Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            				}
            				
            				ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Mensaje");
            				query3.whereEqualTo("id_conversacion", conv.getObjectId());
            				query3.orderByDescending("_created_at");
            		        
            		        try {
            					date = query3.find();
            				} catch (ParseException e) {}
            		        
            		        ParseObject conv_date = null;
            		        conv_date = date.get(0);
            		        fecha.add(conv_date.getUpdatedAt().toLocaleString());
            			}
            			
            			list_conv = (ListView) getActivity().findViewById(R.id.list_men);
            		}		
                } else{	        
        			ParseObject conv = null;
        			for(int i=0 ; i<convers.size() ; i++){
        				conv = convers.get(i);
        				id_conv.add(conv.getObjectId());				
        				idOtro = conv.getString("id_user2");
        				idOtros.add(idOtro);
        				
        				ParseQuery query2 = ParseUser.getQuery();
        		        query2.whereEqualTo("objectId", idOtro);
        		        
        		        try {
        					otro = query2.find();
        				} catch (ParseException e) {}
        		        
        		        ParseObject user_otro = null;
        		        user_otro = otro.get(0);
        		        
        		        nomOtro.add(user_otro.getString("NombreCompleto"));
        		        ParseFile imgOtro = user_otro.getParseFile("Foto");	
        				if(imgOtro != null){
        					imgOtro.getDataInBackground(new GetDataCallback() {
        				    	Bitmap bmp = null;
        				        public void done(byte[] data, com.parse.ParseException e) {
        				            if (e == null){
        				                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);					                
        				                fotOtro.add(Bitmap.createScaledBitmap(bmp, 200, 200, false));
        				            }
        				            else{
        				            	Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        				            }
        				        }
        				    }); 
        				} else{
        					Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        				}
        				
        				ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("Mensaje");
        				query3.whereEqualTo("id_conversacion", conv.getObjectId());
        				query3.orderByDescending("_created_at");
        		        
        		        try {
        					date = query3.find();
        				} catch (ParseException e) {}
        		        
        		        ParseObject conv_date = null;
        		        conv_date = date.get(0);
        		        fecha.add(conv_date.getUpdatedAt().toLocaleString());        		        
        			}
        			list_conv = (ListView) getActivity().findViewById(R.id.list_men);
        		}
                return null;
            }

            protected void onPostExecute(Void result) {
            	//list_bar.setVisibility(View.GONE); 
            	pd.dismiss();
                //Custom Adapter for ListView
            	BaseAdapter listAdapter=new Cursor_AdapterConv(getActivity(), nomOtro, fecha, fotOtro, id_conv, idOtros, delete);
                list_conv.setAdapter(listAdapter);
            }
        }.execute();

    }
	
	public static class Cursor_AdapterConv extends BaseAdapter{
		
		ImageView vis_img1, vis_img2, vis_temp;		
		Context context;
	    List<String> nomOtros;
	    List<String> fechas;
	    List<Bitmap> fotOtros;
	    List<String> idOtros;
	    //int id;
	    List<String> id_conv;
	    //int pos = 0;
	    Integer delete;
	    
	    @SuppressWarnings("unused")
		private static LayoutInflater inflater = null;

	    public Cursor_AdapterConv(Context context, List<String> nomOtros, List<String> fechas, List<Bitmap> fotOtros, List<String> id_conv, List<String> idOtros, Integer delete) {
	        // TODO Auto-generated constructor stub
	        this.context = context;
	        this.nomOtros = nomOtros;
	        this.fechas = fechas;
	        this.fotOtros = fotOtros;
	        this.id_conv = id_conv;
	        this.idOtros = idOtros;
	        this.delete = delete;
	        inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    @Override
	    public int getCount() {
	        // TODO Auto-generated method stub
	        return id_conv.size();
	    }
	   
	    @Override
	    public Object getItem(int position) {
	        // TODO Auto-generated method stub
	        return id_conv.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        // TODO Auto-generated method stub
	        return position;
	    }


	    public View getView(int position, View convertView, ViewGroup parent) {
	        // TODO Auto-generated method stub
	    	
	    	if(convertView==null){
		    	@SuppressWarnings("static-access")
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		    	convertView = inflater.inflate(R.layout.fragment_conv_row, null);
		    }
	    	
	    	final CheckBox borrar = (CheckBox)convertView.findViewById(R.id.msje_delete);
	    	final TextView nombre = (TextView)convertView.findViewById(R.id.nomOtro);
		    final TextView fecha = (TextView)convertView.findViewById(R.id.fecha);
		    final ImageView img = (ImageView)convertView.findViewById(R.id.fotOtro);

		    if(delete == 1){		    	
		    	borrar.setVisibility(View.VISIBLE);
		    }
		    
		    if(fotOtros.size() != 0 ){	 
		    	setImage(img, fotOtros.get(position));
		    	//img.setImageBitmap(fotOtros.get(position));
		    }
		    if(nomOtros.size()!=0){
		    	nombre.setText(nomOtros.get(position));
		    	convertView.setTag(position);
		    }
		    if(fechas.size()!=0){
		    	fecha.setText(fechas.get(position));
		    }
		  
		    
	        convertView.setOnClickListener(new OnClickListener(){        	
				public void onClick(View v) {					
					Integer pos = (Integer) v.getTag();
					Vibrator h = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		        	h.vibrate(25);
					Intent intent = new Intent(context,Activity_conversacion.class);
					intent.putExtra("idConv", id_conv.get(pos));
					intent.putExtra("nomOtro", nomOtros.get(pos));
					intent.putExtra("fecha", fechas.get(pos));
					intent.putExtra("idOtros", idOtros.get(pos));
					intent.putExtra("fotOtro", Bitmap.createScaledBitmap(fotOtros.get(pos), 200, 200, false));
					context.startActivity(intent);	
				}		
			});        
	        return convertView;
	    } 	
	    public void loadBitmap(Bitmap b) {
			vis_temp.setImageBitmap(circle(Bitmap.createScaledBitmap(b, 200, 200, false)));
		}
		
		public void unloadBitmap() {
		   if (vis_temp != null)
			   vis_temp.setImageBitmap(null);
		}
		
		public void setImage(ImageView i, Bitmap sourceid) {
			vis_temp = i;
			unloadBitmap();	   
			loadBitmap(sourceid);
		}
		
		public Bitmap circle(Bitmap bit){
			Bitmap circleBitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);
		    BitmapShader shader = new BitmapShader (bit,  TileMode.CLAMP, TileMode.CLAMP);
		    Paint paint = new Paint();
		    paint.setShader(shader);
		    
		    Canvas c = new Canvas(circleBitmap);
		    c.drawCircle(bit.getWidth()/2, bit.getHeight()/2, bit.getWidth()/2, paint);
			
			return circleBitmap;
		}
	}
}
