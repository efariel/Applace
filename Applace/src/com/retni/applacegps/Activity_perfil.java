package com.retni.applacegps;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_perfil extends ActionBarActivity{
	
	String passUser, mailUser, nameUser = "Nombre", userNAME = "holy";
	Bitmap foto,bito;
	ImageView perf_foto;
	TextView nombre, mail;
	ProgressBar perf_bar;
	private String name = "";
	byte[] data1;
	
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);	
        
        name = Environment.getExternalStorageDirectory() + "/test.jpg";
		
        perf_foto = (ImageView) findViewById(R.id.perf_foto);
		nombre = (TextView) findViewById(R.id.perf_name);
		mail = (TextView) findViewById(R.id.perf_mail2);	
		perf_bar = (ProgressBar) findViewById(R.id.perf_bar);
        
		Parse.initialize(this, "XyEh8xZwVO3Fq0hVXyalbQ0CF81zhcLqa0nOUDY3", "bK1hjOovj0GAmgIsH6DouyiWOHGzeVz9RxYc6vur");
        ParseUser user = new ParseUser();
        user = ParseUser.getCurrentUser();
	    
	    if(user!=null){
	    	mailUser = user.getEmail();
	    	userNAME = user.getUsername();
		    nameUser = (String) user.getString("NombreCompleto");
		    
		    ParseFile img = user.getParseFile("Foto");
		    if(img != null){
			    img.getDataInBackground(new GetDataCallback() {
			    	Bitmap bmp = null;
			        public void done(byte[] data, com.parse.ParseException e) {
			            if (e == null){
			            	BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
			                options.inPurgeable = true; // inPurgeable is used to free up memory while required
			        	    
			                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			                setImage(perf_foto, bmp);			                
			            }
			            else{
			            	Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			            }
			        }
			    }); 
		    }
		    else{
		    	Drawable myDrawable = getResources().getDrawable(R.drawable.fondo_verde);
		    	Bitmap fo = ((BitmapDrawable) myDrawable).getBitmap();
		    	
		    	setImage(perf_foto, fo);
		    }
	    }	    
	    nombre.setText(nameUser);
	    mail.setText(mailUser);
	    perf_foto.setOnClickListener(listener);
	}
	
	public void loadBitmap(Bitmap b) {
		perf_foto.setImageBitmap(circle(b));
	}
	
	public void unloadBitmap() {
		   if (perf_foto != null)
			   perf_foto.setImageBitmap(null);
		   if (foto!= null) {
			   foto.recycle();
		   }
		   foto = null;
		}
	
	public void setImage(ImageView i, Bitmap sourceid) {
		   unloadBitmap();
		   perf_foto = i;
		   loadBitmap(sourceid);
		}
	
	private OnClickListener listener = new OnClickListener(){	
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
				case R.id.perf_foto:
					AlertDialog.Builder dialog = new AlertDialog.Builder(Activity_perfil.this);  
        	        dialog.setTitle("Editar foto de perfil");		
        	        dialog.setIcon(R.drawable.ic_launcher);	
        	        
        	        View vista = getLayoutInflater().inflate( R.layout.dialog, null );
        	        
        	        TextView text = (TextView) vista.findViewById(R.id.dialog_text);
        	        text.setText("Para editar la fotograf�a debes tomar una foto o escogerla desde tu galer�a.");
        			      
        	        dialog.setView(vista);
        	        dialog.setNegativeButton("Buscar en galer�a", new DialogInterface.OnClickListener() {  
        	            public void onClick(DialogInterface dialogo1, int id) {
        	            	Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        	            	int code = SELECT_PICTURE;
        	            	startActivityForResult(intent, code);	
        	            }  
        	        });   
        	        dialog.setPositiveButton("Tomar fotograf�a", new DialogInterface.OnClickListener() {  
        	            public void onClick(DialogInterface dialogo2, int id) {
        	            	Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
        	       			int code = TAKE_PICTURE;
        	       			Uri output = Uri.fromFile(new File(name));
               		    	intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
               		    	startActivityForResult(intent, code);	
        	            }  
        	        });          	        
        	        dialog.show();					
					break;
			}
		}
	};
	
	public void save_image(Bitmap bit){      
		perf_bar.setVisibility(View.VISIBLE);
		Toast.makeText( getApplicationContext(),"Subiendo im�gen",Toast.LENGTH_LONG ).show();
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bit.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    data1 = stream.toByteArray();
	    
	    bito = bit;
	    
	    ParseFile file = new ParseFile(userNAME+"_perfil.jpg", data1);
		
		ParseUser jUser = new ParseUser();
		jUser = ParseUser.getCurrentUser();
		jUser.put("Foto", file);
		jUser.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {					
					perf_bar.setVisibility(View.INVISIBLE);
					Toast.makeText( getApplicationContext(),"Finalizado",Toast.LENGTH_SHORT ).show();
					setImage(perf_foto, bito);
					finish();
					startActivity(getIntent());
			    } else {
			    	Toast.makeText( getApplicationContext(),"Error en guardar la im�gen.",Toast.LENGTH_SHORT ).show();
			    }
			}
		});    
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
    	if (requestCode == TAKE_PICTURE) {
    		if (data != null) {
    			save_image((Bitmap) data.getParcelableExtra("data"));
    		}
    		else{    			
    			BitmapFactory.Options options = new BitmapFactory.Options();
    	        options.inSampleSize = 2;
    			save_image(BitmapFactory.decodeFile(name, options));
    			//perf_foto.setImageBitmap(BitmapFactory.decodeFile(name));
    			new MediaScannerConnectionClient() {
    				private MediaScannerConnection msc = null; {
    					msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
    				}
    				public void onMediaScannerConnected() { 
    					msc.scanFile(name, null);
    				}
    				public void onScanCompleted(String path, Uri uri) { 
    					msc.disconnect();
    				} 
    			};				
    		}    	
    	} else if (requestCode == SELECT_PICTURE){
    		Uri selectedImage = data.getData();
    		InputStream is;
    		try {
    			is = getContentResolver().openInputStream(selectedImage);
    	    	BufferedInputStream bis = new BufferedInputStream(is);
    	    	foto = BitmapFactory.decodeStream(bis);
    	    	save_image(foto);			
    		} catch (FileNotFoundException e) {}
    	}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);	
		
		menu.findItem(R.id.como_funciona).setVisible(false);
		menu.findItem(R.id.codiciones).setVisible(false);
		menu.findItem(R.id.politica).setVisible(false);
		menu.findItem(R.id.ayuda).setVisible(false);
		
		menu.findItem(R.id.action_search).setVisible(false);
		menu.findItem(R.id.action_share).setVisible(false);
		menu.findItem(R.id.action_update).setVisible(false);
		menu.findItem(R.id.action_camara).setVisible(false);
		
		getSupportActionBar().setTitle("Perfil");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
	    {
	        case R.id.action_edit:
	            Toast.makeText(this, "Editar", Toast.LENGTH_SHORT).show();
	            Intent intent = new Intent(Activity_perfil.this, Activity_editarPerfil.class );
				startActivity(intent);
	            break;
	        case R.id.action_config:
	            Toast.makeText(this, "Configurar", Toast.LENGTH_SHORT).show();
	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	 
	    return true;
	}
}