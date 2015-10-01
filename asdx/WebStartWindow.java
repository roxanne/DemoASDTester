package asdx;

import java.awt.*;

/**
 * Created by roxanne on 9/30/15.
 */


/*-------------------------  Class WebStartWindow  -------------------------*/

public class WebStartWindow extends Frame
{
  protected AppletStarter  applet = null;


    /*--------------------------  init  ---------------------------*/

    public void init( AppletStarter applet )
    {
        // record id of AppletStarter instance that launched us
        this.applet = applet;

        // let it know that we are alive
        applet.appletLoaded();
    }
}





