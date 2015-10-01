package asdx;

/**
 * Created by roxanne on 9/30/15.
 */



/*------------------------------------------------------------------------*
 * Originally implemented by Scott Flinn, in association with the
 * Imager Graphics Laboratory at the University of British Columbia
 * (scottflinn@alumni.uwaterloo.ca).
 *
 * This source code may be freely distributed and modified for any purpose
 * as long as these introductory comments are not removed.  Please be
 * aware that this represents the author's initial experiments with the
 * Java platform and should not necessarily be considered good examples
 * of Java programming.
 *------------------------------------------------------------------------*/

/*------------------------------------------------------------------------*
 *
 * The AppletStarter class is an adaptation of the AppletButton class
 *   from the Javasoft JDK tutorial.  It puts a WindowApplet subclass in
 *   a separate window where it can respond to user input and do whatever
 *   else it chooses in a separate thread.
 *
 * The AppletStarter window itself always displays a message on a blank
 *   background.  Until the applet class has been loaded and initialized
 *   the messages are drawn in the colour specified by the applet
 *   parameter LOADCOLOUR.  If the applet is set to wait for a mouse
 *   button click before loading the named applet class, then the
 *   message is underlined.  Once the applet is ready to launch, the
 *   message is underlined and drawn in the colour specified by the
 *   parameter RUNCOLOUR.  The message is constructed from the
 *   applet parameter APPLETNAME and indicates the status of the
 *   AppletStarter applet.
 *
 * The class uses the following applet parameters:
 *   APPLETCLASS
 *    - the Illusion subclass to install in the window
 *    - defaults to the WindowApplet meta-class, which isn't very interesting
 *   APPLETNAME
 *    - a descriptive label for the applet
 *    - default is WINDOWCLASS
 *   WINDOWWIDTH
 *    - the initial width of the window
 *    - default is 600
 *   WINDOWHEIGHT
 *    - the initial height of the window
 *    - default is 400
 *   WAITTOLOAD
 *    - if 1, don't load WINDOWCLASS until AppletStarter receives
 *      a mouse click
 *    - if 0, load WINDOWCLASS immediately
 *    - default is 0
 *   APPLETLABEL
 *    - the label to display in place of default labels constructed from
 *      APPLETNAME
 *
 *------------------------------------------------------------------------*/

        import  java.applet.*;
        import  java.awt.*;

/*------------------------  Class AppletStarter  -------------------------*/

public class AppletStarter extends Applet implements Runnable
{
    // class object and instanced named by WINDOWCLASS
    private Class         appletClassObject;
    private WebStartWindow applet;

    // variables to be initialized from applet parameters
    private String    appletClass;
    private String    appletName;
    private String    appletLabel;
    private int       appletWidth;
    private int       appletHeight;
    private int       waitToLoad;

    // defaults for applet parameters
    private final String  defAppletClass  = "WebStartWindow";
    private final String  defAppletName   = "WebStartWindow";
    private final int     defAppletWidth  = 600;
    private final int     defAppletHeight = 400;
    private final int     defWaitToLoad   = 0;

    private Thread    windowThread;      // thread in which windowclass runs
    private String    label = null;     // current message label
    private boolean   loaded  = false;  // class loading completed
    private boolean   loading = false;  // class loading in progress

    /*---------------------------  init  ---------------------------*/

    public void init()
    {
        String    str;

        // set initial label
        label = "Loading AppletStarter ...";

        // get the applet class, name and label
        appletClass = getParameter( "APPLETCLASS" );
        if ( appletClass == null )
            appletClass = defAppletClass;
        appletName = getParameter( "APPLETNAME" );
        if ( appletName == null )
            appletName = defAppletName;
        appletLabel = getParameter( "APPLETLABEL" );

        System.out.println( "Initializing " + appletClass + ":" + appletName );

        // get window size parameters
        str = getParameter( "WINDOWWIDTH" );
        if ( str == null )
            appletWidth = defAppletWidth;
        else
        {
            try {
                appletWidth = Integer.parseInt( str );
            } catch ( NumberFormatException e ) {
                // use default width
                System.out.println( "Couldn't parse window width: " + str );
                System.out.println( "Using default: " + defAppletWidth );
                appletWidth = defAppletWidth;
            }
        }
        str = getParameter( "WINDOWHEIGHT" );
        if ( str == null )
            appletHeight = defAppletHeight;
        else
        {
            try {
                appletHeight = Integer.parseInt( str );
            } catch ( NumberFormatException e ) {
                // use default height
                System.out.println( "Couldn't parse window height: " + str );
                System.out.println( "Using default: " + defAppletHeight );
                appletHeight = defAppletHeight;
            }
        }

        // find out if we should wait to load the applet class
        str = getParameter( "WAITTOLOAD" );
        if ( str == null )
            waitToLoad = defWaitToLoad;
        else
        {
            try {
                waitToLoad = Integer.parseInt( str );
            } catch ( NumberFormatException e ) {
                // use default
                System.out.println( "Couldn't parse wait-to-load: " + str );
                System.out.println( "Using default: " + defWaitToLoad );
                waitToLoad = defWaitToLoad;
            }
        }

        // load the demo code immediately, or display load message
        if ( waitToLoad == 0 )
            loadIllusion();
        else
        {
            // check for an applet supplied message
            if ( appletLabel == null )
                label = "Load and run " + appletName + " applet";
            else
                label = appletLabel;
        }
    }

    /*-----------------------  loadIllusion  -----------------------*/

    private void loadIllusion()
    {
        if ( ! loading )
        {
            // set label to indicate that we are loading class
            label = "Loading " + appletName + " applet ...";
            repaint();

            // create a separate thread to load applet class
            loading = true;
            windowThread = new Thread( this );
            windowThread.start();
        }
    }

    /*--------------------  loadIllusion (run)  --------------------*/

    public void run()
    {
        if ( loaded )
            return;

        // get the class object for the specified Illusion subclass
        try {
            appletClassObject = Class.forName( appletClass );
        } catch ( Exception e ) {
            // the specified class isn't somewhere that we can find it
            label = "Can't find class: " + appletClass;
            System.out.println( label );
            appletClassObject = null;
            repaint();
        }

        {
            // create an instance of this Illusion applet class
            try {
                applet = (WebStartWindow)(appletClassObject.newInstance());
                //applet.init( this );
            } catch ( InstantiationException | IllegalAccessException e ) {
                label =
                        "Couldn't create instance of " + appletClass +
                                ": " + e.toString();
                System.out.println( label );
                applet = null;
                repaint();
            }
            if ( applet != null )
            {
                // set the applet window's title and size
                applet.setTitle( appletName );
                applet.pack();
                applet.resize( appletWidth, appletHeight );

                // show the applet if we have already clicked to load
                if ( waitToLoad != 0 )
                    applet.show();

                loading = false;
                if ( ! loaded )
                    System.out.println( appletClass + "not yet loaded" );
            }
        }
    }

    /*--------------------  start/stop/destroy  --------------------*/

    // At the moment, these methods exist only for debugging purposes.

    public void start()
     {
     System.out.println( "Starting " + appletClass + ":" + appletName );
     }

     public void stop()
     {
     System.out.println( "Stopping " + appletClass + ":" + appletName );
    }

     public void destroy()
    {
     System.out.println( "Destroying " + appletClass + ":" + appletName );
     if ( loaded )
     {
    applet.hide();
     windowThread.stop();
     }
    else
    System.out.println(
            "Attempt to destroy unloaded thread (" + appletName + ")" );
     }

    /*--------------------------  paint  ---------------------------*/

    public void paint( Graphics g )
    {
        update( g );
    }

    /*--------------------------  update  --------------------------*/

    public void update( Graphics g )
    {
        int          baseline;
        Dimension    d = size();
        FontMetrics  fm  = g.getFontMetrics();

        // display a white background
        g.setColor( Color.white );
        g.fillRect( 0, 0, d.width, d.height );

        // set a default message string if necessary
        if ( label == null )
            label = "Error: label was null";

        // draw the label in red if not yet loaded, blue otherwise
        if ( loaded )
            g.setColor( Color.blue );
        else
            g.setColor( Color.red );

        // actually draw the label
        baseline = d.height - fm.getDescent();
        g.drawString( label, 0, baseline );

        // underline it if waiting to load or launch
        if ( loaded || ( ( waitToLoad != 0 ) && ( ! loading ) ) )
        {
            baseline += 1;
            g.drawLine( 0, baseline, fm.stringWidth( label ), baseline );
        }
    }

    /*------------------------  mouseDown  -------------------------*/

    public boolean mouseDown( Event event, int x, int y )
    {
        if ( ( waitToLoad != 0 ) && ( ! loading ) && ( ! loaded ) )
        {
            // load class in a separate thread
            loadIllusion();
            return true;
        }
        else if ( loaded )
        {
            // show the window if the applet has finished loading
            applet.show();
            return true;
        }
        else
            return false;
    }

    /*-------------------------  setLabel  -------------------------*/

    public synchronized void setLabel( String newLabel )
    {
        label = newLabel;
        System.out.println( appletClass + ": set label to " + label );
        repaint();
    }

    /*-----------------------  appletLoaded  -----------------------*/

    public synchronized void appletLoaded()
    {
        // set a default label if app label not provided
        if ( appletLabel == null )
            appletLoaded( "APPLET: " + appletName );
        else
            appletLoaded( appletLabel );
    }

    public synchronized void appletLoaded( String str )
    {
        loaded = true;
        label = str;
        repaint();
    }
}
/*-------------------------  Class WindowApplet  -------------------------*/



/*------------------------------------------------------------------------*/

/*------------------------------------------------------------------------*/

