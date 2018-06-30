import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

/********************************************************************
 * TextEditor
 * @author andersonic
 *
 * Credit and thanks to user Turk4n on Codecall for starter code 
 * related to the GUI. See tutorial here:
 * http://forum.codecall.net/topic/49721-simple-text-editor/
 * 
 * Uses vi commands because OS system keyboard shortcuts interfere
 * with all other major keyboard shortcut schemes. At present, ESC,
 * i, ZZ are implemented, as well as commands related to enciphering
 * and deciphering.
 * 
 * TOOD: Re-add toolbar if there's every functionality that isn't 
 * handled by keypresses
 *******************************************************************/

public class TextEditor extends JFrame{
	private JPasswordField code = new JPasswordField();
	private JTextArea area = new JTextArea(40,75);	
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false;
	private boolean enciphered = true; //Assumes it opens an enciphered file
	private final Filter FILTER = new Filter(this);
	
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	private Action Open = new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) 
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			SaveAs.setEnabled(true);
		}
	};
	
	private Action SaveAs = new AbstractAction("Save as") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	private Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			dispose();
		}
	};
	
	private Action Save = new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e) {
			if(!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	
	private Action New = new AbstractAction("New") {
		public void actionPerformed(ActionEvent e) {
			new TextEditor();
		}
	};
	
	private Action Encipher = new AbstractAction("Encipher") {
		public void actionPerformed(ActionEvent e) {
			// Only allow encipher if the text is not enciphered
			if(!enciphered) {
				boolean mode = FILTER.getEditMode();
				FILTER.setEditMode(true);
				String currentText = area.getText();
				area.setText("");
				String encipheredText = Cipher.encodeString(currentText, new String(code.getPassword()));
				area.setText(encipheredText);
				enciphered = true;
				FILTER.setEditMode(mode);
			}
		}
	};
	
	private Action Decipher = new AbstractAction("Decipher") {
		public void actionPerformed(ActionEvent e) {
			if(enciphered) {
				boolean mode = FILTER.getEditMode();
				FILTER.setEditMode(true);
				String currentText = area.getText();
				area.setText("");
				String decipheredText = Cipher.decodeString(currentText, new String(code.getPassword()));
				area.setText(decipheredText);
				enciphered = false;
				FILTER.setEditMode(mode);
			}	
		}
	};
	
	private Action Enciphered = new AbstractAction("Enciphered") {
		public void actionPerformed(ActionEvent e) {
			enciphered = true;
		}
	};
	
	private Action Deciphered = new AbstractAction("Deciphered") {
		public void actionPerformed(ActionEvent e) {
			enciphered = false;
		}
	};
	
	public void act(String action) {
		ActionEvent e = new ActionEvent(new Object(), 0, "");
		switch(action) {
			case "Decipher": 
				Decipher.actionPerformed(e);
				break;
			case "Encipher":
				Encipher.actionPerformed(e);
				break;
			case "Deciphered":
				Deciphered.actionPerformed(e);
			case "Enciphered":
				Enciphered.actionPerformed(e);
			default:
				break;
		}
	}
		
	/**
	 * Add a KeyListener to detect the "ESC" key, used to exit edit mode
	 * in vi.
	 */
	private void addEscListener() {
		area.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					FILTER.setEditMode(false);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					FILTER.setEditMode(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					FILTER.setEditMode(false);
				}
			}
			
		});
	}
	
	
	public TextEditor() {
		area.setFont(new Font("Courier", Font.PLAIN, 12));
		area.setLineWrap(true);
		area.setMargin(new Insets(96,96,96,96));
		AbstractDocument doc = (AbstractDocument) area.getDocument();
		doc.setDocumentFilter(FILTER);
		addEscListener();
		
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll, BorderLayout.CENTER);
		
		JPanel codePanel = new JPanel();
		codePanel.setLayout(new BorderLayout());
		codePanel.add(new JLabel("Code: "), BorderLayout.LINE_START);
		codePanel.add(code);
		add(codePanel, BorderLayout.NORTH);
		
		JMenuBar JMB = new JMenuBar();
		setJMenuBar(JMB);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu cipher = new JMenu("Cipher");
		JMB.add(file); JMB.add(edit); JMB.add(cipher);
		
		file.add(New);file.add(Open);file.add(Save);file.add(SaveAs);file.add(Quit);
		//file.addSeparator();
		
		ActionMap m = area.getActionMap();
		Action Cut = m.get(DefaultEditorKit.cutAction);
		Action Copy = m.get(DefaultEditorKit.copyAction);
		Action Paste = m.get(DefaultEditorKit.pasteAction);
		
		edit.add(Cut);edit.add(Copy);edit.add(Paste);
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");	
		
		cipher.add(Encipher); cipher.add(Decipher); 
		cipher.add(Enciphered); cipher.add(Deciphered);
		
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(currentFile);
		setVisible(true);
	}
	
	private void saveFileAs() {
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}
	
	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save " + currentFile + " ?",
					"Save",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}
	
	public void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r,null);
			r.close();
			((AbstractDocument) area.getDocument()).setDocumentFilter(FILTER);
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		} catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Editor can't find the file called " + fileName);
		}
	}
	
	public void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		} catch(IOException e) {
		}
	}
	
	public String getFileName() {
		return currentFile;
	}
	
	public void setEnciphered(boolean mode) {
		enciphered = mode;
	}
}

