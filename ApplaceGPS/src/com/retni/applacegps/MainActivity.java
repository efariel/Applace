package com.retni.applacegps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;



//import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

public class MainActivity extends Activity {
	
	TextView ubicacion;
	private MapView mapView;
	private IMapController mapController;
	double lat=0;
	double lon=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener milocListener = new MiLocationListener();
		milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000*2, 10, milocListener);
		
		ubicacion = (TextView)findViewById(R.id.ubicacion);
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setTileSource(TileSourceFactory.MAPNIK);
		mapView.setMultiTouchControls(true);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();	
		mapController.setZoom(18);
			
		Location valor = getMyLocation();
		if(valor==null){
			Toast.makeText( getApplicationContext(),"No se puede acceder",Toast.LENGTH_SHORT ).show();
		}
		else{
			milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000*2, 10, milocListener);
			ubicacion.setText("Latitud: "+valor.getLatitude()+" y Longitud: "+valor.getLongitude());
			do{	
				GeoPoint myLocation = new GeoPoint(getMyLocation());
					
				SimpleLocationOverlay myLocationOverlay = new SimpleLocationOverlay(this);
				mapView.getOverlays().add(myLocationOverlay);
		 
				mapController.setCenter(myLocation);
				myLocationOverlay.setLocation(myLocation);
				lat=valor.getLatitude();
				lon=valor.getLongitude();
				
			}
			while(lat!=valor.getLatitude() || lon!=valor.getLongitude());
		}		
	}
	
	
	Location getMyLocation(){
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
	
	public class MiLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc){
			lat = loc.getLatitude();
			lon = loc.getLongitude();
			String coordenadas = "Mis coordenadas son: " + "Latitud = " + lat + " Longitud = " + lon;
			ubicacion.setText(coordenadas);
			Toast.makeText( getApplicationContext(),"Cambio ubicacion",Toast.LENGTH_SHORT ).show();
		}
		
		@Override
		public void onProviderDisabled(String provider){
			Toast.makeText( getApplicationContext(),"GpsApplace Desactivado",Toast.LENGTH_SHORT ).show();
		}
		
		@Override
		public void onProviderEnabled(String provider){
			Toast.makeText( getApplicationContext(),"GpsApplace Activado",Toast.LENGTH_SHORT ).show();
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
