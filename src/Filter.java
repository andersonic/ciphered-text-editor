import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
/********************************************************************
 * Filter
 * @author andersonic
 *
 * DocumentFilter subclass used to implement vi-like behavior in
 * TextEditor class.
 * 
 * Currently implements ESC, i, ZZ and four new commands used to
 * encipher, decipher and set whether the current document is
 * enciphered or not.
 *******************************************************************/
public class Filter extends DocumentFilter {
	private boolean editMode = false;
	private String commandSequence = "";
	private final TextEditor EDITOR;
	
	public Filter(TextEditor editor) {
		super();
		EDITOR = editor;
	}
	
	@Override
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
            String text, AttributeSet attr) throws BadLocationException {
		if(editMode) 
		{
			super.insertString(fb, offset, text, attr);
		} else {
			commandSequence += text;
			parseCommandSequence();
		}
		System.out.println("Command Sequence: " + commandSequence);
	}
	
	@Override
    public void replace(DocumentFilter.FilterBypass fb, int offset,
                        int length, String text, AttributeSet attrs) throws BadLocationException {
		if(editMode) {
			super.replace(fb, offset, length, text, attrs);
		} else {
			commandSequence += text;
			parseCommandSequence();
		}
    }	
	
	public void parseCommandSequence() {
		boolean commandActivated = true;
		if(commandSequence.contains("i")) {
			editMode = true;
		} else if(commandSequence.contains("ZZ")) {
			EDITOR.act("Encipher");
			EDITOR.saveFile(EDITOR.getFileName());
			EDITOR.dispose();
		} else if(commandSequence.contains("ve")){
			EDITOR.act("Encipher");
		} else if(commandSequence.contains("vd")){
			EDITOR.act("Decipher");
		} else if(commandSequence.contains("Ve")){
			EDITOR.act("Enciphered");
		} else if(commandSequence.contains("Vd")){
			EDITOR.act("Deciphered");
		} else {
			commandActivated = false;
		}
		
		if(commandActivated) {
			commandSequence = commandSequence.substring(commandSequence.length() - 2);
		}
	}
	
	@Override
    public void remove(DocumentFilter.FilterBypass fb, int offset,
                        int length) throws BadLocationException {
		if(editMode) {
			super.remove(fb, offset, length);
		}	
    }
	
	public void setEditMode(boolean mode) {
		editMode = mode;
	}
	
	public boolean getEditMode() {
		return editMode;
	}
}
