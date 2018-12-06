package net.jpanda.util.net.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class NetUtilMain {
	
	Display display;
	Shell shell;
	
	public NetUtilMain(){
		init();
	}

	private void init(){
		display=Display.getDefault();
		Shell shell=new Shell(display);
		shell.setText("JPandaNet-网络工具集");
		shell.setLayout(new FillLayout());
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth=d.width;
		int screenHeight=d.height;
		shell.setSize(screenWidth*3/4,screenHeight*3/4);
		
		int shellWidth=shell.getBounds().width;
		int shellHeight=shell.getBounds().height;
		
		shell.setLocation((screenWidth-shellWidth)/2,(screenHeight-shellHeight)/2);
		
		
		TabFolder tf=new TabFolder(shell,SWT.NONE);
		TabItem ipSimpleCaculate=new TabItem(tf,SWT.NONE);
		ipSimpleCaculate.setText("IP简单计算工具");
		ipSimpleCaculate.setControl(new IPSimpleCaculateComposite(tf));
		
		TabItem aboutItem=new TabItem(tf,SWT.NONE);
		aboutItem.setText("关于");
		aboutItem.setControl(new AboutComposite(tf));
		
		tf.setSelection(0);
		//shell.setSize(400,250);
		
		//shell.pack();
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NetUtilMain main=new NetUtilMain();

	}

}
