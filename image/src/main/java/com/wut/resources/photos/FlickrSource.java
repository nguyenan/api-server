package com.wut.resources.photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wut.model.map.MappedData;
import com.wut.support.URLArguments;
import com.wut.support.datafetch.HTTPHelper;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;
import com.wut.support.xml.DomWrapper;

public class FlickrSource {

//  // This method returns a buffered image with the contents of an image
//  private static BufferedImage toBufferedImage(Image image) {
//      if (image instanceof BufferedImage) {
//          return (BufferedImage)image;
//      }
//  
//      // This code ensures that all the pixels in the image are loaded
//      image = new ImageIcon(image).getImage();
//  
//      // Determine if the image has transparent pixels; for this method's
//      // implementation, see e661 Determining If an Image Has Transparent Pixels
//      boolean hasAlpha = hasAlpha(image);
//  
//      // Create a buffered image with a format that's compatible with the screen
//      BufferedImage bimage = null;
//      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//      try {
//          // Determine the type of transparency of the new buffered image
//          int transparency = Transparency.OPAQUE;
//          if (hasAlpha) {
//              transparency = Transparency.BITMASK;
//          }
//  
//          // Create the buffered image
//          GraphicsDevice gs = ge.getDefaultScreenDevice();
//          GraphicsConfiguration gc = gs.getDefaultConfiguration();
//          bimage = gc.createCompatibleImage(
//              image.getWidth(null), image.getHeight(null), transparency);
//      } catch (HeadlessException e) {
//          // The system does not have a screen
//      }
//  
//      if (bimage == null) {
//          // Create a buffered image using the default color model
//          int type = BufferedImage.TYPE_INT_RGB;
//          if (hasAlpha) {
//              type = BufferedImage.TYPE_INT_ARGB;
//          }
//          bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
//      }
//  
//      // Copy image to buffered image
//      Graphics g = bimage.createGraphics();
//  
//      // Paint the image onto the buffered image
//      g.drawImage(image, 0, 0, null);
//      g.dispose();
//  
//      return bimage;
//  }
  
//// This method returns true if the specified image has transparent pixels
//  private static boolean hasAlpha(Image image) {
//      // If buffered image, the color model is readily available
//      if (image instanceof BufferedImage) {
//          BufferedImage bimage = (BufferedImage)image;
//          return bimage.getColorModel().hasAlpha();
//      }
//  
//      // Use a pixel grabber to retrieve the image's color model;
//      // grabbing a single pixel is usually sufficient
//       PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
//      try {
//          pg.grabPixels();
//      } catch (InterruptedException e) {
//      }
//  
//      // Get the image's color model
//      ColorModel cm = pg.getColorModel();
//      return cm.hasAlpha();
//  }


	ArrayList<MappedData> getFlickrUrls(String tags, String perpage, String page) {
		String uri = getFlickrSearchUrl(tags, perpage, page);
		//Response response = new Client(Protocol.HTTP).get(uri);
		//DomRepresentation dom = response.getEntityAsDom();
		DomWrapper dom = HTTPHelper.getPageAsDom(uri);
		
		ArrayList<MappedData> photos = new ArrayList<MappedData>();
		String expr = "//photo";
		for (DomWrapper node : dom.getExpression(expr)) {
			String id = node.getAttribute("id");
			String farm = node.getAttribute("farm");
			String server = node.getAttribute("server");
			String secret = node.getAttribute("secret");
			String ispublic = node.getAttribute("ispublic");
			String title = node.getAttribute("title");
			String photoUrl = getFlickrPhotoUrl(farm, server, id, secret);
			
			MappedData photoProperties = new MappedData();
			photoProperties.put("id", id);
			photoProperties.put("farm", farm);
			photoProperties.put("server", server);
			photoProperties.put("secret", secret);
			photoProperties.put("url", photoUrl);
			photoProperties.put("public", ispublic);
			photoProperties.put("title", title);

			photos.add(photoProperties);
		}
			
		return photos;
	}

	private String getFlickrSearchUrl(String tags, String perpage, String page) {
		String baseUrl = "http://api.flickr.com/services/rest/";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("api_key", SettingsManager.getSystemSetting("flicker.api.key"));
		parameters.put("method", "flickr.photos.search");
		parameters.put("tags", tags);
		parameters.put("tag_mode", "all");
		parameters.put("page", page);
		parameters.put("per_page", perpage);
		
		String url = URLArguments.buildUrl(baseUrl, parameters);
		System.out.println(url);
		
		return url;
	}
	
	private String getFlickrPhotoUrl(String farm, String server, String id, String secret) {
		String photoUrlFormat = "http://farm%s.static.flickr.com/%s/%s_%s.jpg";
		String photoUrl = String.format(photoUrlFormat, farm, server, id, secret);
		return photoUrl;
	}
}
