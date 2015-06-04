/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asdx;

import asd.ASDParser;
import asd.ASDPhraseNode;
import java.awt.Font;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 *
 * @author rox
 */
public class ASDTester
{  
   public static void main(String[] args)
   {  int maxSteps = 0;
      if (args.length == 0)
         maxSteps = MAXSTEPS;
      else
         maxSteps = Integer.parseInt(args[0]);

       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ASDTester.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ASDTester.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ASDTester.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ASDTester.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      
      
      
     ASDTester thisTester = new ASDTester(maxSteps);
      
     //System.out.println("Inside ASDtesterLogic");
    // System.out.println("Max Steps = " + maxSteps);

      
   }

   ASDTester(int maxSteps)
   { window = new ASDTesterGui(this);
     window.setTitle("Augmented Syntax Diagram Tester - version " + VERSION);
     window.setVisible(true);
     maximumSteps = maxSteps; 
      
     //System.out.println("Inside ASDTesterLogic(int maxSteps) ASDtesterLogic");
    // System.out.println("Max Steps = " + maxSteps);
      
     parser = new ASDParser();
   }

   void advance()
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         return;
      }
      if (utterance == null || utterance.equals("")) return;
      String advanceResult = parser.advance();
      if (advanceResult.equals(parser.QUIT))
      {  window.getOutputPane().append(
            "\nParse quit after " + stepsThisTry + " new and "
            + steps + " total advance steps,\n"
            + "and " + backupStepsThisTry + " new and "
            + backupSteps + " total backup steps, leaving structure:\n");
         showPhraseStructure();
         // prepare for an attempt at an alternative parse
         stepsThisTry = 0;
         backupStepsThisTry = 0;
         stepsSincePause = 0;
      }
      else if (advanceResult.equals(parser.SUCCEED))
      {  steps++;
         stepsThisTry++;
         stepsSincePause++;
         window.getOutputPane().append(
            "\n" + steps + " - parse advanced to:\n");
         showPhraseStructure();
         if (parser.done())
         {  window.getOutputPane().append(
               "\nSuccessful parse after " + stepsThisTry + " new and "
               + steps  + " total advance steps,\n"
               + "and " + backupStepsThisTry + " new and "
               + backupSteps + " total backup steps.\n");
            // prepare for an attempt at an alternative parse
            stepsThisTry = 0;
            backupStepsThisTry = 0;
            stepsSincePause = 0;
         }
      }
      else if (advanceResult.equals(parser.NOADVANCE))
      {  if (parser.backup())
         {  ++backupSteps; ++backupStepsThisTry;
            window.getOutputPane().append("\nparse backed up to:\n");
            showPhraseStructure();
         }
         else
         {  window.getOutputPane().append(
               "\nParse failed after " + stepsThisTry + " new and "
            + steps + " total advance steps,\n"
            + "and " + backupStepsThisTry + " new and "
            + backupSteps + " total backup steps, leaving structure:\n");
            showPhraseStructure();
            // prepare for an attempt at an alternative parse
            stepsThisTry = 0;
            backupStepsThisTry = 0;
            stepsSincePause = 0;
         }
      }
      else  // this shouldn't occur
         window.getOutputPane().append(
            "\nInvalid result of ASDParser advance(maxSteps):"
            + advanceResult + "\n");
   } // end advance

   void allParses()
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         return;
      }
      if (utterance == null || utterance.equals("")) return;
      while (completeParse())
         ;
   }

   boolean completeParse()
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         return false;
      }
      if (utterance == null || utterance.equals(""))
         return false;
      String advanceResult; // SUCCEED, NOADVANCE, or QUIT
      while(stepsSincePause < maximumSteps)
      {  advanceResult = parser.advance();
         if (advanceResult.equals(parser.QUIT))
         {  window.getOutputPane().append(
               "\nParse quit after " + stepsThisTry + " new and "
               + steps + " total advance steps,\n"
               + "and " + backupStepsThisTry + " new and "
               + backupSteps + " total backup steps, leaving structure:\n");
            showPhraseStructure();
            // prepare for an attempt at an alternative parse
            stepsThisTry = 0;
            backupStepsThisTry = 0;
            stepsSincePause = 0;
            return false;
         }
         else if (advanceResult.equals(parser.SUCCEED))
         {  ++steps;
            ++stepsThisTry;
            ++stepsSincePause;
            if (parser.done())
            {  window.getOutputPane().append(
                  "\nSuccessful parse in " + stepsThisTry + " new and "
                  + steps  + " total advance steps,\n"
                  + "and " + backupStepsThisTry + " new and "
                  + backupSteps + " total backup steps.\n");
               showPhraseStructure();
               // prepare for an attempt at an alternative parse
               stepsThisTry = 0;
               backupStepsThisTry = 0;
               stepsSincePause = 0;
               return true;
            }
         }
         else if (advanceResult.equals(parser.NOADVANCE))
         {  if (parser.backup())
            {  ++backupSteps; ++backupStepsThisTry;
            }
            else
            {  window.getOutputPane().append(
                  "\nParse failed after " + stepsThisTry + " new and "
                  + steps + " total advance steps,\n"
                  + "and " + backupStepsThisTry + " new and "
                  + backupSteps + " total backup steps, leaving structure:\n");
               showPhraseStructure();
               // prepare for an attempt at an alternative parse
               stepsThisTry = 0;
               backupStepsThisTry = 0;
               stepsSincePause = 0;
               return false;
            }
         }
         else  // this shouldn't happen
         {  window.getOutputPane().append(
               "Invalid result of ASDParser advance(maxSteps):"
               + steps);
            return false;
         }
      }
//        JOptionPane.showMessageDialog(window,
//         "Attempt to Complete parse or to find All Remaining parses "
//         + "\npaused after step " + steps + ",\n" + maximumSteps
//         + " steps since start or last pause."
//         +"\nIt can be resumed by menu selection.");
      window.getOutputPane().append(
         "\nAttempt to Complete parse or to find All Remaining parses "
         + "\npaused after advance step " + steps + ", " + maximumSteps
         + " advance steps since start or last pause."
         +"\nIt can be resumed by menu selection.\n");
      stepsSincePause = 0;
      return false;
   } // end completeParse

   ASDParser getParser() { return parser; }

   boolean initializeParse()
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         return false;
      }
      if (utterance == null || utterance.length() == 0)
      {  JOptionPane.showMessageDialog(window,
            "The phrase to be parsed must not be empty.");
         return false;
      }
      if (expectedTypes == null || expectedTypes.size() == 0)
      {  JOptionPane.showMessageDialog(window,
            "The list of expected phrase types must not be empty.");
         return false;
      }
      parser.initialize(utterance, expectedTypes);
      phraseInitialized = true;
      steps = 0;
      stepsThisTry = 0;
      stepsSincePause = 0;
      backupSteps = 0;
      backupStepsThisTry = 0;
      window.getOutputPane().append("\nInitialized phrase structure:\n");
      showPhraseStructure();
      return true;
   } // end initializeParse

   void setExpectedTypeList(String types)
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         window.clearExpectedTypeListField();
         return;
      }
      expectedTypes = new ArrayList();
      if (types == null || types.length() == 0)
      {  JOptionPane.showMessageDialog(window,
            "The list of expected phrase types must not be empty.");
         return;
      }
      StringTokenizer st = new StringTokenizer(types);
      while (st.hasMoreTokens())
         expectedTypes.add(st.nextToken());
      if (utterance != null && utterance.length() > 0)
         initializeParse();
   } // end setExpectedTypeList

   void setHyphenAsSpecialCharacter(boolean useHyphen)
   {  String special = parser.SPECIALCHARS;
      if (useHyphen && special.indexOf("-") < 0)
         parser.setSPECIALCHARS("-" + special);
      else if (!useHyphen)
      {  int hyphenPos = special.indexOf("-");
         if (hyphenPos < 0) return; // already no hyphen
         parser.setSPECIALCHARS(special.substring(0, hyphenPos)
            + special.substring(hyphenPos + 1));
      }
      if (grammarLoaded && utterance != null && utterance.length() > 0)
         initializeParse();
   }

   void setSaveUniquelyParsedSubphrases(boolean save)
   {  parser.setSaveUniquelyParsedSubphrases(save);
   }

   void setUtterance(String newUtterance)
   {  if (!grammarLoaded)
      {  JOptionPane.showMessageDialog(window,
            "No grammar file is currently loaded.");
         window.clearGrammarFileNameField();
         window.clearUtteranceField();
         return;
      }
      utterance = newUtterance;
      if (utterance == null || utterance.length() == 0)
      {  JOptionPane.showMessageDialog(window,
            "The phrase to be parsed must not be empty.");
         return;
      }
      initializeParse();
   } // end setUtterance

   void setUtteranceNull() { utterance = null; }

   void showAboutInfo()
   {  // responds to About ASDTester choice in Help menu
      JOptionPane.showMessageDialog(window,
         "ASDTester version " + VERSION +
         "\nAuthor: James A. Mason" +
         "\nEmail: jmason@yorku.ca" +
         "\nhttp://www.yorku.ca/jmason/");
   }

   void showBracketedPhrase()
   {  if (phraseInitialized)
      {  window.getOutputPane().append("\nAfter advance step " + steps + ":");
         window.getOutputPane().append("\n" + parser.bracketPhrase() + "\n");
      }
      else
         JOptionPane.showMessageDialog(window,
            "No phrase has been initialized.");
   }

   void showPhraseStructure()
   {  if (!phraseInitialized)
         JOptionPane.showMessageDialog(window,
            "No phrase has been initialized.");
      else
      {  window.getOutputPane().append("\n" + parser.bracketPhrase());
         ASDPhraseNode head = parser.phraseStructure();
         ASDPhraseNode currentNode = parser.currentNode();
         window.showTree(head, currentNode);
      }
   } // end showPhraseStructure

   void showTree()
   {  if (phraseInitialized)
      {  window.getOutputPane().append("\nAfter advance step " + steps + ":");
         ASDPhraseNode head = parser.phraseStructure();
         ASDPhraseNode currentNode = parser.currentNode();
         window.showTree(head, currentNode);
      }
      else
         JOptionPane.showMessageDialog(window,
            "No phrase has been initialized.");
   } // end showTree

   boolean useGrammar(String fileName)
   {  if (parser.useGrammar(fileName))
      {  grammarLoaded = true;
         return true;
      }
      else
      {  JOptionPane.showMessageDialog(window,
            "Grammar file with that name could not be loaded.");
         grammarLoaded = false;
         expectedTypes = null;
         utterance = null;
         return false;
      }
   } // end useGrammar

   static final int MAXSTEPS = 10000;
   static final String VERSION = "1.2";
   static final Font FONT
      = new Font("Monospaced", Font.PLAIN, 12);
   private ASDTesterGui window;
   private ASDParser parser;
   private boolean grammarLoaded = false;
   private boolean phraseInitialized = false;
   private ArrayList expectedTypes;
   private int steps = 0;  // total advance steps since phrase initialization
   private int stepsSincePause = 0;  // steps since last pause
   private int stepsThisTry = 0;  // advance steps since phrase
                                  // initialization or last successful parse
   private int backupSteps = 0;  // total backup steps since phrase init.
   private int backupStepsThisTry = 0; // since init. or last succ. parse
   private int maximumSteps = MAXSTEPS;
   private String utterance;
   
}
    

