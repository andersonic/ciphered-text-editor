/********************************************************************
 * Application
 * @author andersonic
 *
 * Creates a TextEditor instance.
 *******************************************************************/
public class Application {

	public static void main(String[] args) {
		TextEditor initialEditor = new TextEditor();
		if(args.length > 0) {
			initialEditor.readInFile(args[0]);
		}

	}

}
