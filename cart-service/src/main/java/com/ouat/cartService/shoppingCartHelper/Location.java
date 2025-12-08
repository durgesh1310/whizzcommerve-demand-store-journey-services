package com.ouat.cartService.shoppingCartHelper;

import java.io.Serializable;

public class Location implements Serializable { 
	    /**
	 * 
	 */
	private static final long serialVersionUID = 9155150940079706039L;
		/**
	 * 
	 */
	
		private String name;
	    private double longitude;
	    private double latitude;   
	   
	    
	    public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		
		// create and initialize a point with given name and
	    // (latitude, longitude) specified in degrees
		
	    public Location(String name, double latitude, double longitude) {
	        this.name = name;
	        this.latitude  = latitude;
	        this.longitude = longitude;
	    }
	    
	    public Location() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String toString() {
	        return name + " (" + latitude + ", " + longitude + ")";
	    }

}
	
