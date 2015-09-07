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

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class FirstScreen extends JFrame implements Runnable {

	private static final long serialVersionUID = 516281090191754013L;
	private JPanel contentPane;
	private List<String> pages;
	private JTextPane pageNumbersPane;
	private JLabel lblFileName;
	private JLabel lblSelectedFile;
	JButton btnTestProd;
	JButton btnTestStage;
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
		setFont(new Font("Dialog", Font.PLAIN, 12));
		
		//Initializes the list of pages to process
		pages = new ArrayList<String>();
		
		//Sets OS Look and Feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.println("Couldn't set OS look and feel. Cause: "+e);
		}
		
		//Window definition
		setTitle("Product Page Testing Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 556, 360);
		setResizable(false);
		
		//Locates the window in the middle of any screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Sets up main panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Adds the Test button
		btnTestProd = new JButton("Production");
		btnTestProd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnTestProd.setEnabled(false);
		btnTestProd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Product Page numbers
				Util.Settings.production = true;
				proceed();
			}
		});
		btnTestProd.setBounds(397, 210, 141, 41);
		contentPane.add(btnTestProd);
		
		btnTestStage = new JButton("Stage");
		btnTestStage.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnTestStage.setEnabled(false);
		btnTestStage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Product Page numbers
				Util.Settings.production = false;
				proceed();
			}
		});
		btnTestStage.setBounds(397, 156, 141, 41);
		contentPane.add(btnTestStage);
		
		
		//Adds the add file button
		JButton btnAddFile = new JButton("Add File");
		btnAddFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser();
			}
		});
		btnAddFile.setToolTipText("Select a txt file to load page numbers");
		btnAddFile.setBounds(397, 13, 141, 41);
		contentPane.add(btnAddFile);
		
		
		//Adds the cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(397, 264, 141, 41);
		contentPane.add(btnCancel);
		
		//Adds the pane to enter the page numbers
		pageNumbersPane = new JTextPane();
		pageNumbersPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pageNumbersPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				btnTestProd.setEnabled(true);
				btnTestStage.setEnabled(true);
				if(arg0.getKeyCode() == 8 || arg0.getKeyCode() == 127){
					if(pageNumbersPane.getCaretPosition()==0){
						btnTestProd.setEnabled(false);
						btnTestStage.setEnabled(false);
					}
				}
				
			}
		});
		pageNumbersPane.setToolTipText("Add Page Numbers");
		pageNumbersPane.setBounds(12, 13, 299, 229);
		
		//Makes the pageNumbersPane scrollable
		JScrollPane scrollPane = new JScrollPane(pageNumbersPane);
		scrollPane.setBounds(12, 13, 349, 292);
		contentPane.add(scrollPane);
		
		lblFileName = new JLabel("");
		lblFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setBounds(397, 90, 141, 16);
		lblFileName.setVisible(false);
		contentPane.add(lblFileName);
		
		lblSelectedFile = new JLabel("Selected File:");
		lblSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectedFile.setBounds(397, 61, 141, 16);
		lblSelectedFile.setVisible(false);
		contentPane.add(lblSelectedFile);
		
	}
	
	private void proceed(){
		divideAndClean();
		if(hasValidPageNumbersOnly()){
			setGlobal();		
			this.dispose();
			startTest();
		}else{
			wrongInput();
		}
	}
	
	private void wrongInput(){
		pages.clear();
		btnTestProd.setEnabled(false);
		btnTestStage.setEnabled(false);
		lblFileName.setVisible(false);
		lblSelectedFile.setVisible(false);
		JOptionPane.showMessageDialog(contentPane, 
				"Valid Page numbers only. \n Page numbers are 5 digits numbers ", 
				"Wrong input", JOptionPane.ERROR_MESSAGE);
	}
	
	private boolean hasValidPageNumbersOnly(){
		boolean correct = true;
		for(String page : pages){
			correct = (page.matches("[0-9]+")&& page.length()==5 )?true:false;
		}
		return correct;
	}
	
	private void divideAndClean(){
		String text = getTextPaneContent();
		//Adds all the lines, removing the ones containing \n (new line) 
		pages.addAll(Arrays.asList(text.split("\\r?\\n")));
		//Removes all blank lines in the text field
		pages.removeIf((s)->s.length()==0);
	}
	
	private String getTextPaneContent(){
		String text = pageNumbersPane.getText();
		
		text = text.replaceAll(",","\n");
		text = text.replaceAll("-","\n");
		return text;
	}
	
	private void setGlobal(){
		Util.Settings.productPages = pages;
	}
	
	private void startTest(){
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { TestingProductPage.class });
		testng.addListener(tla);
		testng.run();
	}
	
	private void fileChooser(){
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		int result = fileChooser.showOpenDialog(contentPane);
		if(result == JFileChooser.APPROVE_OPTION){
			btnTestProd.setEnabled(true);
			btnTestStage.setEnabled(true);
			File selectedFile = fileChooser.getSelectedFile();
			processFile(selectedFile);
		}
	}
	
	private void processFile(File file){
		try{
			Stream<String> lines = Files.lines(file.toPath());
			lblSelectedFile.setVisible(true);
			lblFileName.setVisible(true);
			lblFileName.setText(file.getName().toString());
			lines.forEach( s -> temp += s + "\n" );
			lines.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(contentPane, "Couldn't open file. Cause: "+e, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		try{
			 StyledDocument document = (StyledDocument) pageNumbersPane.getDocument();
		     document.insertString(document.getLength(), temp, null);
		}catch(BadLocationException e){
			System.out.println("Couldn't append file, reason: "+ e);
		}
		
	}
}
