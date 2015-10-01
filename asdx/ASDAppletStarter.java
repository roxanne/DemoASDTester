package asdx;
import asd.*;
import java.applet.*;
        import java.io.*;
        import java.lang.reflect.*;
        import java.awt.*;
        import java.net.*;
        import javax.swing.*;
/**
 * Created by roxanne on 10/1/15.
 */
public class ASDAppletStarter extends JApplet implements Runnable{

// asddemo2 - 2004 Jan. 21


        //import java.applet.*;
        //import java.io.*;
        //import java.lang.reflect.*;
        //import java.awt.*;
        //import java.net.*;
        //import javax.swing.*;

//public class ASDEditorApplet extends JApplet implements Runnable {



    /** The ASDEditor */
    //private ASDEditor asdeditor;
    protected ASDTester asdtester;
protected ASDTesterGui asdtestergui;

    /** The thread where the ASDTester is run */
    private Thread asdtesterThread;

    /** If the ASDTester thread has already been started */
    private boolean asdtesterStarted;
    private String    label = null;     // current message label
    private boolean   loaded  = false;  // class loading completed
    private boolean   loading = false;  // class loading in progress
    private int       waitToLoad;

    /**
     * Initializes the applet. It creates the ASDEditor
     * thread.
     * */
    public void init() {

        validate();
        setVisible(true);
        // Set a thread to run the applet
        asdtesterThread = new Thread(this);
        asdtesterStarted = false;
    }

    /**
     * Starts the ASDTester thread.
     * */
    public void start() {
        if (asdtesterStarted) return; // already started => nothing to do
        asdtesterStarted = true;
        asdtesterThread.start();
    }


    public void stop() {
        // Nothing here for now!
    }

    /**
     * Waits for the ASDtester thread to end and sets it to null.
     * */
    public void destroy() {
        if (!asdtesterStarted) return;
        while (asdtesterThread != null && asdtesterThread.isAlive()) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
            }
        }
        asdtesterThread = null;
    }

    public void run() {

        int status;

        if (asdtester != null) return; // already running an ASDTester
        showStatus("Initializing ASDTester...");
        // Create and start the ASDEditor
        //asdtester = new asdx.ASDTester();

       // try
        {
            //asdtester = new asdx.ASDTester();
            asdtestergui = new asdx.ASDTesterGui(asdtester);
            showStatus("ASDEditor running ...");

        }
        //catch(ClassNotFoundException e)
        {}
        //catch (InvocationTargetException e)
        {}
        //catch (InstantiationException e)
        {}
        //catch (IllegalAccessException e)
        {}


        showStatus("ASDEditor running ...");


    }
/*--------------------------  paint  ---------------------------*/

    public void paint( Graphics g )
    {
        update( g );
    }

    /*--------------------------  update  --------------------------*/
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


}
