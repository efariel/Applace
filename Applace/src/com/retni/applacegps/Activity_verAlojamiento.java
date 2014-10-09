package com.retni.applacegps;

import java.util.ArrayList;
import java.util.List;


import com.parse.DeleteCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Activity_verAlojamiento extends ActionBarActivity{
	
	RatingBar ratingBar;
	TextView txtRatingValue;
	Button btnSubmit;
	ViewPager viewPager;
    PagerAdapter adapter;
    String id_aloj = "jaja";
    TextView vis_tit, vis_estado, vis_rating_count, vis_precio, vis_descrip;
    LinearLayout vis_tv, vis_wifi, vis_telefono, vis_piscina, vis_calefaccion, vis_cocina, vis_estacionamiento;
    LinearLayout vis_lavadora, vis_papel, vis_quincho, vis_aireacondicionado, vis_desayuno, vis_perro;
    RatingBar vis_rating;
    List<Bitmap> fotitos = new ArrayList<Bitmap>();
    int[] fotos;
    
    TextView vis_comentario;
    String texto = "";
    ProgressBar delete_bar;
    
    List<Boolean> services = new ArrayList<Boolean>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_veralojamiento);
	
		fotos = new int[] { R.drawable.img01,
                R.drawable.img02, R.drawable.img03,
                R.drawable.img04, R.drawable.img05, R.drawable.img06,
                R.drawable.img07};
		
		delete_bar = (ProgressBar) findViewById(R.id.delete_bar);
		
		vis_tit = (TextView) findViewById(R.id.vis_tit);
		vis_estado = (TextView) findViewById(R.id.vis_estado);
		vis_rating = (RatingBar) findViewById(R.id.vis_rating);
		vis_rating_count = (TextView) findViewById(R.id.vis_count_rating);
		vis_precio = (TextView) findViewById(R.id.vis_precio);
		vis_descrip = (TextView) findViewById(R.id.vis_descrip);
		
		vis_tv = (LinearLayout) findViewById(R.id.vis_tv);
		vis_wifi = (LinearLayout) findViewById(R.id.vis_wifi);
		vis_telefono = (LinearLayout) findViewById(R.id.vis_telefono);
		vis_piscina = (LinearLayout) findViewById(R.id.vis_piscina);
		vis_calefaccion = (LinearLayout) findViewById(R.id.vis_calefaccion);
		vis_cocina = (LinearLayout) findViewById(R.id.vis_cocina);
		vis_estacionamiento = (LinearLayout) findViewById(R.id.vis_estacionamiento);
		vis_lavadora = (LinearLayout) findViewById(R.id.vis_lavadora);
		vis_papel = (LinearLayout) findViewById(R.id.vis_papel);
		vis_quincho = (LinearLayout) findViewById(R.id.vis_quincho);
		vis_aireacondicionado = (LinearLayout) findViewById(R.id.vis_aireacondicionado);
		vis_desayuno = (LinearLayout) findViewById(R.id.vis_desayuno);
		vis_perro = (LinearLayout) findViewById(R.id.vis_perro);
		
		Bundle bundle = getIntent().getExtras();
		id_aloj=bundle.getString("idAloj");
		Toast.makeText( getApplicationContext(),id_aloj,Toast.LENGTH_SHORT ).show();
		
        Parse.initialize(this, "XyEh8xZwVO3Fq0hVXyalbQ0CF81zhcLqa0nOUDY3", "bK1hjOovj0GAmgIsH6DouyiWOHGzeVz9RxYc6vur");
        
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alojamiento");
		query.whereEqualTo("objectId", id_aloj);
		
		List<ParseObject> car = null;
		try {
			car = query.find();
		} catch (ParseException e) {

		}
		
		if(car.size()!=0){
			ParseObject caract = car.get(0);
			vis_tit.setText(caract.getString("titulo"));
			if(caract.getBoolean("estado")==true){
				vis_estado.setText("Disponible");
			} else if(caract.getBoolean("estado")==false){
				vis_estado.setText("No disponible");
			}
			
			vis_rating.setRating((float) caract.getDouble("calificacion"));
			vis_rating_count.setText(""+caract.getInt("count_calificacion"));
			vis_precio.setText("$"+caract.getInt("precio"));
			vis_descrip.setText(caract.getString("descripcion"));
			
			if(caract.getBoolean("articulos_higiene")==false)
				vis_papel.setVisibility(View.GONE);
			if(caract.getBoolean("aire_acond")==false)
				vis_aireacondicionado.setVisibility(View.GONE);
			if(caract.getBoolean("cocina")==false) 
				vis_cocina.setVisibility(View.GONE);
			if(caract.getBoolean("calefaccion")==false)
				vis_calefaccion.setVisibility(View.GONE);
			if(caract.getBoolean("desayuno")==false)
				vis_desayuno.setVisibility(View.GONE);
			if(caract.getBoolean("estacionamiento")==false)
				vis_estacionamiento.setVisibility(View.GONE);
			if(caract.getBoolean("lavadora")==false)
				vis_lavadora.setVisibility(View.GONE);
			if(caract.getBoolean("mascota")==false)
				vis_perro.setVisibility(View.GONE);
			if(caract.getBoolean("piscina")==false)
				vis_piscina.setVisibility(View.GONE);
			if(caract.getBoolean("quincho")==false)
				vis_quincho.setVisibility(View.GONE);
			if(caract.getBoolean("tv")==false) 
				vis_tv.setVisibility(View.GONE);
			if(caract.getBoolean("internet")==false)
				vis_wifi.setVisibility(View.GONE);
			if(caract.getBoolean("telefono")==false)
				vis_telefono.setVisibility(View.GONE);
			
			ParseFile img = caract.getParseFile("foto");	
			if(img != null){
			    img.getDataInBackground(new GetDataCallback() {
			    	Bitmap bmp = null;
			        public void done(byte[] data, com.parse.ParseException e) {
			            if (e == null){
			                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);			                
			                fotitos.add(bmp);
			            }
			            else{
			            	//Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			            }
			        }
			    }); 
			} else{
				//Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
			viewPager = (ViewPager) findViewById(R.id.pager_fotos);
	        adapter = new ViewPagerAdapter(Activity_verAlojamiento.this, fotos);
	        viewPager.setAdapter(adapter);
		}			
		else {
			
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);		
		
		menu.findItem(R.id.como_funciona).setVisible(false);
		menu.findItem(R.id.codiciones).setVisible(false);
		menu.findItem(R.id.politica).setVisible(false);
		menu.findItem(R.id.ayuda).setVisible(false);
		
		menu.findItem(R.id.action_search).setVisible(false);
		menu.findItem(R.id.action_edit).setVisible(false);
		menu.findItem(R.id.action_config).setVisible(false);
		menu.findItem(R.id.action_share).setVisible(false);
		menu.findItem(R.id.action_update).setVisible(false);
		menu.findItem(R.id.action_camara).setVisible(false);
		menu.findItem(R.id.action_delete).setVisible(true);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
	    {
	        case R.id.action_delete:
	        	AlertDialog.Builder dialog = new AlertDialog.Builder(Activity_verAlojamiento.this);  
    	        dialog.setTitle("Eliminar Alojamiento");		
    	        dialog.setIcon(R.drawable.ic_launcher);	
    	        
    	        View v = getLayoutInflater().inflate( R.layout.dialog, null );
    	        TextView text = (TextView) v.findViewById(R.id.dialog_text);
    	        text.setText("�Desea borrar de forma permanente este anuncio?");
    			
    	        dialog.setView(v);
    	        dialog.setNegativeButton("Cancelar", null);  
    	        dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {  
    	            public void onClick(DialogInterface dialogo1, int id) {
    	            	delete_bar.setVisibility(View.VISIBLE);
    	            	Toast.makeText( getApplicationContext(),"Eliminando anuncio...",Toast.LENGTH_SHORT ).show();
    	            	//Ac� se elimina el alojamiento de la base de datos
    	            	ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alojamiento");
    	        		query.whereEqualTo("objectId", id_aloj);
    	        		
    	        		List<ParseObject> obj = null;
    	        		try {
    	        			obj = query.find();
    	        		} catch (ParseException e) {
    	        		}
    	        		if(obj.size()!=0){
    	        			obj.get(0).deleteInBackground(new DeleteCallback() {
    	        				public void done(ParseException e) {
    	        				    if (e == null) {	
    	        				    	delete_bar.setVisibility(View.INVISIBLE);
    	        				    	Toast.makeText( getApplicationContext(),"Alojamiento eliminado correctamente!",Toast.LENGTH_SHORT ).show();
    	        				    	finish();    	        						
    	        				    } else{
    	        				    	Toast.makeText(getApplicationContext(), "Error al borrar el alojamiento, por favor intente nuevamente.", Toast.LENGTH_SHORT).show();
    	        				    }
    	        				}
    	        		    });
    	        		}                    	
    	            }  
    	        });  
    	        
    	        dialog.show();
            	break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	 
	    return true;
	}
}
