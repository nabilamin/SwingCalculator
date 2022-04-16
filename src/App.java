import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;


/**
 * Nabil Amin
 * CMIS 242 7382 Intermediate Programming (2222) Calculator App
 * 04.09.2022
 */
public class App {

    private static class NumpadButton extends JButton {
        MouseInputAdapter hoverListener = new MouseInputAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(Color.decode("#F4FAFE"));
            };
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(Color.decode("#E9F6FF"));
            };
        };
        private NumpadButton(String text) {
            super(text);
            setBorderPainted(false);
            setFocusable(false);
            setOpaque(true);
            setBackground(Color.decode("#E9F6FF"));
            setFont(new Font(null, Font.PLAIN, 18));

            addMouseListener(hoverListener);
        }
    }

    private static class NumberKey extends NumpadButton {
        private NumberKey(String text, ActionListener listener) {
            super(text);
            addActionListener(listener);
        }
    }

    private static class FunctionKey extends NumpadButton {
        private FunctionKey(String text, ActionListener listener) {
            super(text);
            setForeground(Color.decode("#00B0D7"));
            if (text.toLowerCase() == "c") {
                setForeground(Color.decode("#FF6060"));
            }
            addActionListener(listener);
        }
    }

    private static class TextDisplay extends JTextField {
        private TextDisplay() {
            setEditable(false);
            setHorizontalAlignment(JTextField.RIGHT);
            setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 8));
            setFont(new Font(null, Font.PLAIN, 26));
        }

        private void append(String text) {
            setText(getText() + text);
        }

        private void clear() {
            setText("");
        }
    }

    private static class Calculator implements ActionListener {

        static TextDisplay display;
        static Float[] terms = new Float[2];
        static int termPointer = 0;
        static String operator = "";
        static boolean resultDisplayed = false;

        private Calculator() {
            // pass in the shared listener and return display
            display = buildUI(this);
        }

        private static void moveTermPointer() {
            if (termPointer == 0) {
                termPointer++;
            } else {
                termPointer--;
            }
        }

        private static void appendTerm(String term) {
            terms[termPointer] = Float.parseFloat(term);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            // if we hit clear, clear everything
            if(e.getActionCommand() == "c") {
                clearEverything();
                return;
            }

            // if we hit equals, do math
            if(e.getActionCommand() == "=") {
                // append what we have in the display
                appendTerm(display.getText());
                // do math
                doMath();
                return;
            }

            if(e.getSource() instanceof NumberKey) {

                // only allow one decimal per term
                if(e.getActionCommand() == "." && display.getText().indexOf(".") != -1) {
                    return;
                }

                if (resultDisplayed) {
                    // if the results are displayed, weve bugun entering a new expression
                    // clear everything
                    clearEverything();
                }
                // add the button text to the display
                display.append(e.getActionCommand());
                return;
            }

            if(e.getSource() instanceof FunctionKey) {
                // if delete, check for appropriate length
                if (e.getActionCommand() == "<-") {
                    if (display.getText().length() > 0) {
                        display.setText(display.getText().substring(0, display.getText().length() - 1));
                    }
                    return;
                }

                // if percent, check for appropriate length and divide by 100
                if (e.getActionCommand() == "%") {
                    if (display.getText().length() > 0) {
                        display.setText(Float.toString(Float.parseFloat(display.getText())/100));
                    }
                    return;
                }


                // if the result is displayed and we hit a function key, we want to use the current term
                // prevent upon hitting a number by setting the flag to false.
                if (resultDisplayed) {
                    resultDisplayed = false;
                }
                // store the operator
                operator = e.getActionCommand();
                // store the current term
                appendTerm(display.getText());
                // move the pointer
                moveTermPointer();
                // clear the display to make room for next term
                display.clear();
                return;
            }

        }

        private static void doMath() {
            float result = 0;
            if (terms[0] != null && terms[1] != null && operator != "") {
                switch (operator) {
                    case "x": result = terms[0] * terms[1];
                        break;
                    case "÷": result = terms[0] / terms[1];
                        break;
                    case "-": result = terms[0] - terms[1];
                        break;
                    case "+": result = terms[0] + terms[1];
                        break;
                }
                // set the results displayed flag
                resultDisplayed = true;
                // display the results
                display.setText(Float.toString(result));
                // move the pointer
                moveTermPointer();
            }
        }

        private static void clearEverything() {
            // clear the display
            display.clear();
            // clear the terms
            Arrays.fill(terms, (float)0);
            // reset pointer
            termPointer = 0;
            // reset operator
            operator = "";
            // reset resultFlag
            resultDisplayed = false;
        }

        private static TextDisplay buildUI(ActionListener listener) {
            JPanel display = new JPanel();
            display.setPreferredSize(new Dimension(320, 130));
            display.setBackground(Color.decode("#FAFAFA"));
            display.setLayout(new GridLayout(1, 0));
    
            JPanel numpad = new JPanel();
            numpad.setPreferredSize(new Dimension(320, 412));
            numpad.setBackground(Color.decode("#E9F6FF"));
            numpad.setLayout(new GridLayout(5,5));
    
            JButton[] items = {
                new FunctionKey("c", listener),
                new FunctionKey("%", listener),
                new FunctionKey("<-", listener),
                new FunctionKey("÷", listener),
                new NumberKey("7", listener),
                new NumberKey("8", listener),
                new NumberKey("9", listener),
                new FunctionKey("x", listener),
                new NumberKey("4", listener),
                new NumberKey("5", listener),
                new NumberKey("6", listener),
                new FunctionKey("-", listener),
                new NumberKey("1", listener),
                new NumberKey("2", listener),
                new NumberKey("3", listener),
                new FunctionKey("+", listener),
                new NumberKey("", listener),
                new NumberKey("0", listener),
                new NumberKey(".", listener),
                new FunctionKey("=", listener),
            };
    
            for (JButton item : items) {
                numpad.add(item);
            }
    
            TextDisplay textDisplay = new TextDisplay();
            display.add(textDisplay);
            
            JFrame main = new JFrame();
            main.setResizable(false);
            main.setSize(320, 568);
            main.add(display, BorderLayout.PAGE_START);
            main.add(numpad, BorderLayout.PAGE_END);
            
            main.setVisible(true);

            return textDisplay;
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Nabil Amin - CMIS 242 7382 Intermediate Programming (2222) - 4.09.22");
        new Calculator();
    }
}
