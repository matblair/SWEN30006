package resourcemanagers;

import gameengine.Portal2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.SlickException;

public class AssetManager {

	private static AssetManager manager;

	// XML File Locations
	private static final String xmlpath = Messages.getString("AssetManager.loadinglistpath");
	private static final String generalresource = Messages.getString("AssetManager.generalrsrc");
	private static final String teststate = Messages.getString("AssetManager.teststatepath"); 
	private static final String mainmenu = Messages.getString("AssetManager.mainmenupath"); 

	
	// World loader
	private static Stateloader loader;
	
	private AssetManager(){	
		loader = new Stateloader();
		
	}
	
	public static AssetManager getResourceManager()
	throws SlickException{
		if(manager==null){
			manager = new AssetManager();
		}
		return manager;
		
	}
	
	public void loadAllGameAssets(final ResourceLoader assets){
		final File f = new File(xmlpath, generalresource);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			assets.loadResources(is, true);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final SlickException e) {
			e.printStackTrace();
		} finally	{
			try {
				is.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadState(final ResourceLoader assets, final int stateid){
		String resourcepath = null;
		switch(stateid){
			case(Portal2D.TESTGAMESTATE): resourcepath = teststate;
			break;
			case(Portal2D.MAINMENU): resourcepath = mainmenu;
			break;
		}
		if(resourcepath!=null){
		final File f = new File(xmlpath, resourcepath);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			loader.loadResources(is, true, assets);

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final SlickException e) {
			e.printStackTrace();
		} finally	{
			try {
				is.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		}
	}
	
}

