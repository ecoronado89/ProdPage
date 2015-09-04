package defaultPackage;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JLabel;

public class FirstScreen extends JFrame implements Runnable {

	private static final long serialVersionUID = 516281090191754013L;
	private JPanel contentPane;
	private List<String> pages;
	private JTextPane pageNumbersPane;
	private JLabel label;
	private JLabel lblSelectedFile;
	String temp = "";

	@Override
	public void run() {
		try {
			FirstScreen frame = new FirstScreen();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public FirstScreen() {
		
		//Initializes the list of pages to process
		pages = new ArrayList<String>();
		
		//Sets OS Look and Feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.println("Couldn't set OS look and feel. Cause: "+e);
		}
		
		//Window definition
		setTitle("Product Pages");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		
		//Locates the window in the middle of any screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Sets up main panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Adds the add file button
		JButton btnAddFile = new JButton("Add File");
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser();
			}
		});
		btnAddFile.setToolTipText("Select a txt file to load page numbers");
		btnAddFile.setBounds(323, 13, 97, 25);
		contentPane.add(btnAddFile);
		
		//Adds the Test button
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Product Page numbers
				setPPNumbers();
			}
		});
		btnTest.setBounds(323, 179, 97, 25);
		contentPane.add(btnTest);
		
		//Adds the cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(323, 217, 97, 25);
		contentPane.add(btnCancel);
		
		//Adds the pane to enter the page numbers
		pageNumbersPane = new JTextPane();
		pageNumbersPane.setToolTipText("Add Page Numbers");
		pageNumbersPane.setBounds(12, 13, 299, 229);
		
		//Makes the pageNumbersPane scrollable
		JScrollPane scrollPane = new JScrollPane(pageNumbersPane);
		scrollPane.setBounds(12, 13, 299, 229);
		contentPane.add(scrollPane);
		
		label = new JLabel("");
		label.setBounds(323, 82, 97, 16);
		label.setVisible(false);
		contentPane.add(label);
		
		lblSelectedFile = new JLabel("Selected File:");
		lblSelectedFile.setBounds(323, 53, 97, 16);
		lblSelectedFile.setVisible(false);
		contentPane.add(lblSelectedFile);
		
	}
	
	private void setPPNumbers(){
		
		String text = getTextPaneContent();
		pages.addAll(Arrays.asList(text.split("\\r?\\n")));
		
		pages.removeIf((s)->s.length()==0);
		
		pages.forEach(s -> System.out.println(s));
	}
	
	private String getTextPaneContent(){
		String text = pageNumbersPane.getText();
		
		text = text.replaceAll(",","\n");
		text = text.replaceAll("-","\n");
		return text;
	}
	
	private void fileChooser(){
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		int result = fileChooser.showOpenDialog(contentPane);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = fileChooser.getSelectedFile();
			processFile(selectedFile);
		}
	}
	
	private void processFile(File file){
		temp = "";
		try{
			Stream<String> lines = Files.lines(file.toPath());
			lblSelectedFile.setVisible(true);
			label.setVisible(true);
			label.setText(file.getName().toString());
			lines.forEach( s -> temp += s + "\n" );
			lines.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(contentPane, "Couldn't open file", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		try{
			 StyledDocument document = (StyledDocument) pageNumbersPane.getDocument();
		     document.insertString(document.getLength(), temp, null);
		}catch(BadLocationException e){
			System.out.println("Couldn't append file, reason: "+ e);
		}
		
	}
	
}
