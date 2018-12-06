package net.jpanda.util.net.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AboutComposite extends Composite {

	private Label label1;
	
	public AboutComposite(Composite parent){
		super(parent,SWT.NONE);
		init();
	}
	
	private void init(){
		this.setLayout(new FillLayout());
		
		label1=new Label(this,SWT.NONE);
		label1.setText("\n\nJPandaNet-网络工具集 ver1.0beta(2012-9)\n作者：应晓云\n\n\nEmail:yxy@jpanda.net");
		label1.setAlignment(SWT.CENTER);
		
	}
}
