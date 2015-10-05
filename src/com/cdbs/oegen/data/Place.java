/**
 * 
 */
package com.cdbs.oegen.data;


/**
 * @author toxn
 *
 */
public class Place
extends Oebject
{

	/**
	 * @author toxn
	 * 
	 */
	public class LatitudeLongitudeException
	extends RuntimeException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LatitudeLongitudeException(String message)
		{
			super(message);
		}

	}

	private static final int LATITUDE_MAX = 90;

	private static final int LATITUDE_MIN = -90;

	private static final int LONGITUDE_MAX_INT = 180;

	private static final int LONGITUDE_MIN_INT = -180;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float latitude;

	private float longitude;

	public Place()
	{
		super(null, null, null);
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude()
	{
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude()
	{
		return longitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude)
	{
		if (latitude < LATITUDE_MIN || latitude > LATITUDE_MAX)
			throw new LatitudeLongitudeException(Messages.getString("Place.0")); //$NON-NLS-1$
		this.latitude = latitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude)
	{
		if (longitude < LONGITUDE_MIN_INT || longitude > LONGITUDE_MAX_INT)
			throw new LatitudeLongitudeException(Messages.getString("Place.1")); //$NON-NLS-1$
		this.longitude = longitude;
	}

}
