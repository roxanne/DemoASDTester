/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asdx;

import asd.ASDPhraseNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

 class ASDTesterGui extends JFrame 
 {  
     
       protected static Properties properties;
   private static ResourceBundle resources;
    //private static Properties properties;
  //  private final static String EXIT_AFTER_PAINT = "-exit";
  //  private static boolean exitAfterFirstPaint;
   
   
   static {
       try {
           resources = ResourceBundle.getBundle("resources.ASDTester",
                   Locale.getDefault());
            //String propertyValue = resources.getString("key")
       } catch (MissingResourceException mre) {
            System.err.println("resources/ASDTester.properties not found");
            System.exit(1);
       }
   }
   /*
static {
        try {
            properties = new Properties();
            properties.load(ASDTester.class.getResourceAsStream(
                    "resources/ASDTesterSystem.properties"));
            resources = ResourceBundle.getBundle("resources.ASDTester",
                    Locale.getDefault());
        } catch (MissingResourceException | IOException  e) {
            System.err.println("resources/ASDTester.properties "
                    + "or resources/ASDTesterSystem.properties not found");
            System.exit(1);
        }
    }
    */
     
     /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ASDTesterGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ASDTesterGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ASDTesterGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ASDTesterGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
           private ASDTester tester;
            public void run() {
                  
                new ASDTesterGui(tester).setVisible(true);
                
            }
        });
    }
   
    
     public ASDTesterGui(final ASDTester tester)
         
     {  this.tester = tester;
      
      grammarFileNameField = new JTextField(40);
      grammarFileNameField.addActionListener(
              new GrammarFileNameFieldListener(this));
      expectedTypeListField = new JTextField(40);
      expectedTypeListField.addActionListener(
              new ExpectedTypeListFieldListener(this));
      utteranceField = new JTextField(40);
      utteranceField.addActionListener(
              new UtteranceFieldListener(this));
      hyphenAsLexicalItemBox = new JCheckBox(
              "Treat hyphens (-) in words as separate lexical items");
      hyphenAsLexicalItemBox.addActionListener(
              new HyphenAsLexicalItemBoxListener(this));
      hyphenAsLexicalItemBox.setSelected(false);
      uniquelyParsedSubphrasesBox = new JCheckBox(
              "Save all uniquely-parsed subphrases");
      uniquelyParsedSubphrasesBox.addActionListener(
              new UniquelyParsedSubphrasesBoxListener(this));
      uniquelyParsedSubphrasesBox.setSelected(true);
      JPanel pane = new JPanel();
      pane.setLayout(
              new BoxLayout(pane, BoxLayout.Y_AXIS));
      pane.add(
              new LabeledTextField("Grammar file:    ", grammarFileNameField));
      pane.add(
              new LabeledTextField("Expected types:", expectedTypeListField));
      pane.add(
              new LabeledTextField("Phrase parsed: ", utteranceField));
      middlePanel = new JPanel();
      middlePanel.add(hyphenAsLexicalItemBox);
      middlePanel.add(uniquelyParsedSubphrasesBox);
      middlePanel.setMaximumSize(new Dimension(800, 20));
      pane.add(middlePanel);
      outputPane = new JTextArea();
      outputPane.setMinimumSize(new Dimension(DEFAULT_WIDTH,
              DEFAULT_HEIGHT));
      outputPane.setFont(ASDTester.FONT);
      OutputPaneMenu menu = new OutputPaneMenu(outputPane, tester);
      MouseListener popupListener = new PopupListener(menu);
      outputPane.addMouseListener(popupListener);

      pane.add(new JScrollPane(outputPane));
      getContentPane().add(pane, BorderLayout.CENTER);
      addWindowListener(new WindowCloser(this));
      // listens for window closing events (see below)
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      ActionMenu aMenu = new ActionMenu(this);
      aMenu.setMnemonic(KeyEvent.VK_A);
      menuBar.add(aMenu);
      HelpMenu hMenu = new HelpMenu(this);
      hMenu.setMnemonic(KeyEvent.VK_H);
      menuBar.add(hMenu);

         //1. Input/Open/Load the grammar files and init parse
         JToolBar toolbar = new JToolBar();//create toolbar for all icons
         add(toolbar, BorderLayout.NORTH);
         JButton newGrammarFileButton = null;

         newGrammarFileButton = new JButton(
                 new ImageIcon(getClass().getClassLoader().getResource("resources/open.gif")));

         newGrammarFileButton.setToolTipText("Use the grammar file name text field \n"
                 + " key in the URL http://www.asdnetworks.com/grammars/npX.grm");
         toolbar.add(newGrammarFileButton);
         // toolbar.addSeparator();
         newGrammarFileButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 //initializeParse();
                 tester.initializeParse();
             }
         });

         //2. Key-in the new parse and initialize
         JButton initializeParseButton = null;

         initializeParseButton = new JButton(new ImageIcon(
                 getClass().getClassLoader().getResource("resources/new.gif")));

         initializeParseButton.setToolTipText("Initialize new parse/use the ENTER KEY");
         toolbar.add(initializeParseButton);
         toolbar.addSeparator();
         initializeParseButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 //initializeParse();
                 tester.initializeParse();
             }
         });

         // 3. advance parse one step
         JButton stepParseButton = null;

         stepParseButton = new JButton(new ImageIcon(
                 getClass().getClassLoader().getResource("resources/step.png")));
         // Step parse


         stepParseButton.setToolTipText("advance parse one (1) step");
         toolbar.add(stepParseButton);
         toolbar.addSeparator();
         stepParseButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 // evaluator.completeParse();
                 tester.advance();
             }
         });


         // 4. Complete Parse
         JButton completeParseButton = null;

         completeParseButton = new JButton(new ImageIcon(
                 getClass().getClassLoader().getResource("resources/parse.png")));

         completeParseButton.setToolTipText("Complete the present parse");
         toolbar.add(completeParseButton);

         completeParseButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 // evaluator.completeParse();
                 tester.completeParse();
             }
         });

         // 5. Show Phrase Tree Structure with listener

         JButton showPhraseButton = null;
         showPhraseButton = new JButton(new ImageIcon(
                 getClass().getClassLoader().getResource("resources/blue-side-tree.png")));

         // JButton showPhraseButton = new JButton(new ImageIcon("images/blue-side-tree.png"));    //uses icon
         showPhraseButton.setToolTipText("Show parse tree phrase structure");
         toolbar.add(showPhraseButton);
         toolbar.addSeparator();
         showPhraseButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 //showBracketedPhrase();
                 tester.showPhraseStructure();
             }
         });
          
         // 6.  Show bracketed form
          JButton showBracketedButton = null;
          showBracketedButton = new JButton(new ImageIcon(
                  getClass().getClassLoader().getResource("resources/report_word.png")));

          //  JButton showTreeButton = new JButton(new ImageIcon("images/sideparse.png")); // uses  icon
          showBracketedButton.setToolTipText("Display parse in bracketed format");
          toolbar.add(showBracketedButton);
          toolbar.addSeparator();
          showBracketedButton.addActionListener(new ActionListener() {

              public void actionPerformed(ActionEvent event) {
                  // showPhraseStructure();
                  //evaluator.showSemanticValue();
                  tester.showBracketedPhrase();
              }
          });

         // 7.  Complete All Parses
          JButton allParsesButton = null;


          allParsesButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/sideparse.png")));

         //  JButton showTreeButton = new JButton(new ImageIcon("images/sideparse.png")); // uses  icon
          allParsesButton.setToolTipText("Complete all parses ");
          toolbar.add(allParsesButton);
          toolbar.addSeparator();
          allParsesButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 // showPhraseStructure();
                 //evaluator.showSemanticValue();
                 tester.allParses();
             }
         });
          

          // 8.  Select All output text area //   

          JButton copyButton = null;
          // JButton selectButton = new JButton(new ImageIcon("images/copy.gif"));    // uses  icon
          //(new ImageIcon(this.getClass().getClassLoader().getResource("images/open.gif")));
          copyButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/copy.gif")));
          //JButton copyButton = new JButton(new ImageIcon("images/copy.gif")); // uses COPY icon
          copyButton.setToolTipText("select all / copy output pane ");
          toolbar.add(copyButton);

          copyButton.addActionListener(new ActionListener() {

              public void actionPerformed(ActionEvent event) {
                  outputPane.requestFocus();
                  outputPane.selectAll();
              }
          });
        
          // 9 Erase / Clear  Select output text area

          JButton cutEraseButton = null;

          cutEraseButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/cut.gif")));
          //  JButton cutEraseButton = new JButton(new ImageIcon("images/cut.gif"));    // need ERASE icon
          cutEraseButton.setToolTipText("Clear/Delete output text area");
          toolbar.add(cutEraseButton);
          toolbar.addSeparator();
          cutEraseButton.addActionListener(new ActionListener() {

              public void actionPerformed(ActionEvent event) {
                  outputPane.setText("");
              }
          });
          
          // 10. Help   showAboutInfo  with listener
          JButton helpButton = null;
          helpButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/help.png")));
          // JButton helpButton = new JButton(new ImageIcon("images/help.png"));  // uses  icon
          helpButton.setToolTipText("Show application info; this can take a little time as it starts up system default browser");
          toolbar.add(helpButton);

          helpButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent event) {
                    
                       //showAboutInfo();
                       asdx.BrowserPage.openURL
                       ("http://www.yorku.ca/jmason/asdSoftware.html");
                      // ("http://www.yorku.ca/jmason/arithmeticExpressions.htm");
                   
                     
                    
              }
          });

         // 11 EXIT

         JButton exitButton = null;
         // JButton exitButton = new JButton(new ImageIcon("images/delete.gif"));    // uses  icon
         //(new ImageIcon(this.getClass().getClassLoader().getResource("images/open.gif")));
         exitButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/delete.gif")));

         // JButton exitButton = new JButton(new ImageIcon("images/delete.gif"));    // uses  icon
         exitButton.setToolTipText("Close application ");
         toolbar.add(exitButton);
         //toolbar.addSeparator();
         exitButton.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent event) {
                 System.exit(0);
             }
         });

          
         
      } // End of GUI contructor end ASDTesterWindow(ASDTester givenTester)


      void clearExpectedTypeListField() { expectedTypeListField.setText(""); }
      void clearGrammarFileNameField() { grammarFileNameField.setText(""); }
      void clearUtteranceField() { utteranceField.setText(""); }

      JTextField getExpectedTypeListField() { return expectedTypeListField; }
      JTextField getGrammarFileNameField() { return grammarFileNameField; }
      JTextArea getOutputPane() { return outputPane; }
      ASDTester getTester() { return tester; }
      JTextField getUtteranceField() { return utteranceField; }

      void grammarFileNameFieldChanged()
      {  clearUtteranceField();
         tester.setUtteranceNull();
         if (!tester.useGrammar(grammarFileNameField.getText().trim()))
             // grammar file was not loaded
         {  clearExpectedTypeListField();
            // Note: the grammarFileNameField is intentionally NOT
            // reset to empty here, so the user can edit the incorrect
            // file name if desired.
            return;
         }
         Set expectedTypes = tester.getParser().lexicon().phraseTypes();
         String expected = "";
         for (Iterator it = expectedTypes.iterator(); it.hasNext(); )
         {  String type = (String)it.next();
            expected += type + " ";
         }
         expectedTypeListField.setText(expected);
         expectedTypeListFieldChanged();
      } // end grammarFileNameChanged

      void expectedTypeListFieldChanged()
      {  tester.setExpectedTypeList(expectedTypeListField.getText().trim());
      }

      void hyphenAsLexicalItemBoxChanged()
      {  tester.setHyphenAsSpecialCharacter(
            hyphenAsLexicalItemBox.isSelected());
      }

      void uniquelyParsedSubphrasesBoxChanged()
      {  tester.setSaveUniquelyParsedSubphrases(
            uniquelyParsedSubphrasesBox.isSelected());
      }

      void utteranceFieldChanged()
      {  tester.setUtterance(utteranceField.getText().trim());
      }

      /**
         Displays the tree rooted at the given head node,
         with node currentNode indicated by an asterisk and an arrow.
         @param head the header node of the phrase structure
         @param currentNode the current node at the top level
         in the phrase structure
       */
      void showTree(ASDPhraseNode head, ASDPhraseNode currentNode)
      {  showTreeMark(head, "", currentNode);
         outputPane.append("\n");
      } // end showTree

      /**
         Displays the portion of the tree starting at the
         given node and indented with the given indentString as
         prefix for each line that does not represent a top-
         level node.  Top-level nodes are prefixed with three
         blanks or, in the case of the given aNode, an asterisk
         and an arrow whose purpose is to indicate the node
         which is the current node during a parse.
         @param indentString prefix for indenting of the
         current subtree
         @param aNode the node to be marked with an arrow
       */
      private void showTreeMark(ASDPhraseNode givenNode, String indentString,
                               ASDPhraseNode markNode)
      {  outputPane.append("\n");
         if (givenNode == markNode)
            outputPane.append("*->");
         else
            outputPane.append("   ");
         outputPane.append(indentString + givenNode.word() + " ");
         if (givenNode.instance() != null)
            outputPane.append(givenNode.instance().instance());
         else
            outputPane.append("nil");
         if (givenNode.subphrase() != null)
            showTreeMark(givenNode.subphrase(),indentString + "   ",
               markNode);
         if (givenNode.nextNode() != null)
            showTreeMark(givenNode.nextNode(), indentString, markNode);
      } // end showTreeMark

      static final int DEFAULT_WIDTH = 800;  // window width
      static final int DEFAULT_HEIGHT = 600; // window height
      private ASDTester tester;
      private JTextField grammarFileNameField;
      private JTextField expectedTypeListField;
      private JTextField utteranceField;
      private JCheckBox hyphenAsLexicalItemBox;
      private JCheckBox uniquelyParsedSubphrasesBox;
      private JPanel middlePanel;
      private JTextArea outputPane;

    

    

   
            
        
        
     
   } // end class ASDTesterWindow

   class LabeledTextField extends JPanel
   {  LabeledTextField(String labelText, JTextField textField)
      {  setMaximumSize(new Dimension(800,10));
         setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
         JLabel label = new JLabel(labelText);
         textField.setFont(ASDTester.FONT);
         this.add(label);
         this.add(textField);
      }
   } // end class LabeledTextField

   /**
      An instance defines what should happen when a window
      closes.
    */
   class WindowCloser extends WindowAdapter
   {  WindowCloser(ASDTesterGui w)
      {  window = w;
      }

      public void windowClosing(WindowEvent e)
      {
         System.exit(0);        // stop the program
      }

      ASDTesterGui window;
   } // end class WindowCloser

    class GrammarFileNameFieldListener implements ActionListener
   {
      GrammarFileNameFieldListener(ASDTesterGui w)
      {  window = w;
      }

    

      public void actionPerformed(ActionEvent e)
      {  window.grammarFileNameFieldChanged();
      }

      private ASDTesterGui window;
   } // end class GrammarFileNameFieldListener

   class ExpectedTypeListFieldListener implements ActionListener
   {
      ExpectedTypeListFieldListener(ASDTesterGui w)
      {  window = w;
      }

      public void actionPerformed(ActionEvent e)
      {  window.expectedTypeListFieldChanged();
      }

      private ASDTesterGui window;
   } // end class ExpectedTypeListFieldListener

   class UtteranceFieldListener implements ActionListener
   {
      UtteranceFieldListener(ASDTesterGui w)
      {  window = w;
      }

      public void actionPerformed(ActionEvent e)
      {  window.utteranceFieldChanged();
      }

      private ASDTesterGui window;
   } // end class UtteranceFieldListener

   class HyphenAsLexicalItemBoxListener implements
      ActionListener
   {  HyphenAsLexicalItemBoxListener(
         ASDTesterGui w)
      {  window = w;
      }

      public void actionPerformed(ActionEvent e)
      {  window.hyphenAsLexicalItemBoxChanged();
      }

      private ASDTesterGui window;
   } // end class HyphenAsLexicalItemBoxListener

   class UniquelyParsedSubphrasesBoxListener implements
      ActionListener
   {  UniquelyParsedSubphrasesBoxListener(
         ASDTesterGui w)
      {  window = w;
      }

      public void actionPerformed(ActionEvent e)
      {  window.uniquelyParsedSubphrasesBoxChanged();
      }

      private ASDTesterGui window;
   } // end class UniquelyParsedSubphrasesBoxListener

   class OutputPaneMenu extends JPopupMenu implements ActionListener
   {  OutputPaneMenu(JTextArea p, ASDTester t)
      {  pane = p;
         tester = t;
         setInvoker(pane);

         JMenuItem initializeItem = new JMenuItem("Initialize parse");
         initializeItem.addActionListener(this);
         add(initializeItem);
         addSeparator();
         JMenuItem advanceItem = new JMenuItem("Advance one step");
         advanceItem.addActionListener(this);
         add(advanceItem);
         JMenuItem completeParseItem = new JMenuItem("Complete parse");
         completeParseItem.addActionListener(this);
         add(completeParseItem);
         JMenuItem allParsesItem = new JMenuItem("All remaining parses");
         allParsesItem.addActionListener(this);
         add(allParsesItem);
         addSeparator();
         JMenuItem showBracketsItem = new JMenuItem("Show bracketed phrase");
         showBracketsItem.addActionListener(this);
         add(showBracketsItem);
         JMenuItem showTreeItem = new JMenuItem("Show phrase structure tree");
         showTreeItem.addActionListener(this);
         add(showTreeItem);
         addSeparator();
         JMenuItem selectAllItem = new JMenuItem("Select all");
         selectAllItem.addActionListener(this);
         add(selectAllItem);
         JMenuItem copyItem = new JMenuItem("Copy selection");
         copyItem.addActionListener(this);
         add(copyItem);
         addSeparator();
         JMenuItem clearItem = new JMenuItem("Erase output pane");
         clearItem.addActionListener(this);
         add(clearItem);
      } // end OutputPaneMenu(JTextArea p, ASDTester t)

      public void actionPerformed(ActionEvent e)
      {  if (tester == null) return;
         String command = e.getActionCommand();
         if (command.equals("Initialize parse"))
            tester.initializeParse();
         else if (command.equals("Advance one step"))
            tester.advance();
         else if (command.equals("All remaining parses"))
            tester.allParses();
         else if (command.equals("Complete parse"))
            tester.completeParse();
         else if (command.equals("Show bracketed phrase"))
            tester.showBracketedPhrase();
         else if (command.equals("Show phrase structure tree"))
            tester.showTree();
         else if (command.equals("Select all"))
         {  pane.requestFocus();
            pane.selectAll();
         }
         else if (command.equals("Copy selection"))
            pane.copy();
         else if (command.equals("Erase output pane"))
            pane.setText("");
      } // end actionPerformed

      ASDTester tester;
      JTextArea pane; // the pane to which the menu is attached.
   } // end class OutputPaneMenu

   class ActionMenu extends JMenu implements ActionListener
   {  ActionMenu(ASDTesterGui w)
      {  super("Action");
         window = w;
         tester = window.getTester();
         outputPane = window.getOutputPane();
         JMenuItem initializeMenuItem = new JMenuItem("Initialize parse",
            KeyEvent.VK_I);
         initializeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_I, ActionEvent.ALT_MASK));
         add(initializeMenuItem);
         initializeMenuItem.addActionListener(this);
         JMenuItem advanceMenuItem = new JMenuItem("Advance one Step",
            KeyEvent.VK_S);
         advanceMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_S, ActionEvent.ALT_MASK));
         add(advanceMenuItem);
         advanceMenuItem.addActionListener(this);
         JMenuItem completeParseMenuItem = new JMenuItem("Complete Parse",
            KeyEvent.VK_P);
         completeParseMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_P, ActionEvent.ALT_MASK));
         add(completeParseMenuItem);
         completeParseMenuItem.addActionListener(this);
         JMenuItem remainingMenuItem = new JMenuItem("All Remaining Parses",
            KeyEvent.VK_R);
         remainingMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_R, ActionEvent.ALT_MASK));
         remainingMenuItem.addActionListener(this);
         add(remainingMenuItem);
         addSeparator();
         JMenuItem showBracketsMenuItem = new JMenuItem(
            "Show bracketed phrase", KeyEvent.VK_B);
         showBracketsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_B, ActionEvent.ALT_MASK));
         showBracketsMenuItem.addActionListener(this);
         add(showBracketsMenuItem);
         JMenuItem showTreeMenuItem = new JMenuItem(
            "Show phrase structure tree", KeyEvent.VK_T);
         showTreeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_T, ActionEvent.ALT_MASK));
         showTreeMenuItem.addActionListener(this);
         add(showTreeMenuItem);
         addSeparator();
         JMenuItem copyAllMenuItem = new JMenuItem(
            "Select All of output pane", KeyEvent.VK_A);
         copyAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_A, ActionEvent.CTRL_MASK));
         copyAllMenuItem.addActionListener(this);
         add(copyAllMenuItem);
         JMenuItem copySelectionMenuItem = new JMenuItem("Copy Selection",
            KeyEvent.VK_C);
         copySelectionMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
         copySelectionMenuItem.addActionListener(this);
         add(copySelectionMenuItem);
         addSeparator();
         JMenuItem eraseMenuItem = new JMenuItem("Erase output pane",
            KeyEvent.VK_E);
         eraseMenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_E, ActionEvent.CTRL_MASK));
         eraseMenuItem.addActionListener(this);
         add(eraseMenuItem);
      }

      /**
        Listens for menu item events.
       */
      public void actionPerformed(ActionEvent e)
      {  String command = e.getActionCommand();
         if (command.equals("Initialize parse"))
            tester.initializeParse();
         else if (command.equals("Advance one Step"))
            tester.advance();
         else if (command.equals("Complete Parse"))
            tester.completeParse();
         else if (command.equals("All Remaining Parses"))
            tester.allParses();
         else if (command.equals("Show bracketed phrase"))
            tester.showBracketedPhrase();
         else if (command.equals("Show phrase structure tree"))
            tester.showTree();
         else if (command.equals("Select All of output pane"))
         {  outputPane.requestFocus();
            outputPane.selectAll();
         }
         else if (command.equals("Copy Selection"))
            outputPane.copy();
         else if (command.equals("Erase output pane"))
            outputPane.setText("");
      }

      ASDTester tester;
      ASDTesterGui window;
      JTextArea outputPane;
   } // end class ActionMenu

   class HelpMenu extends JMenu implements ActionListener
   {  HelpMenu(ASDTesterGui w)
      {  super("Help");
         window = w;
         tester = window.getTester();
         JMenuItem aboutMenuItem = new JMenuItem("About ASDTester",
            KeyEvent.VK_A);
         add(aboutMenuItem);
         aboutMenuItem.addActionListener(this);
      }

      /**
         Listens for menu item events.
       */
      public void actionPerformed(ActionEvent e)
      {  String command = e.getActionCommand();
         if (command.equals("About ASDTester"))
            tester.showAboutInfo();
      }

      ASDTester tester;
      ASDTesterGui window;
   
   } // end class HelpMenu


//} end GUI ASDTester
 /**
   This class can be used by any others in the asdx package,
   to pop up a given menu in response to a right mouse click.
 */
class PopupListener extends MouseAdapter
{  PopupListener(JPopupMenu m)
   {  menu = m;
   }

   public void mousePressed(MouseEvent e)
   {  maybeShowPopup(e);
   }

   public void mouseReleased(MouseEvent e)
   {  maybeShowPopup(e);
   }

   private void maybeShowPopup(MouseEvent e)
   {  if (e.isPopupTrigger())
        menu.show(e.getComponent(), e.getX(), e.getY());
   }

   private JPopupMenu menu;
} // end class PopupListener