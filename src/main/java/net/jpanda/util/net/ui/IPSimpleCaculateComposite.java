package net.jpanda.util.net.ui;

import net.jpanda.util.net.IPTool;
import net.jpanda.util.net.InvalidIpAddressException;
import net.jpanda.util.net.InvalidIpMaskException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class IPSimpleCaculateComposite extends Composite {
	
	Label ipLabel,maskLabel;
	Text ipText,maskText,resultText;	
	Group resultGroup,buttonGroup;
	Button caculateButton,copyButton,resetButton,helpButton;
	

	public IPSimpleCaculateComposite(Composite parent){
		super(parent,SWT.NONE);
		init();
	}
	
	private void init(){
		GridLayout layout=new GridLayout(4,false);
		this.setLayout(layout);
		GridData data=null;
		
		ipLabel=new Label(this,SWT.NONE);
		ipLabel.setText("IP地址：");
		
		ipText=new Text(this,SWT.BORDER | SWT.SINGLE);
		data=new GridData(GridData.FILL_HORIZONTAL);
		ipText.setLayoutData(data);
		
		maskLabel=new Label(this,SWT.NONE);
		maskLabel.setText("子网掩码：");
		
		maskText=new Text(this,SWT.BORDER | SWT.SINGLE);
		data=new GridData(GridData.FILL_HORIZONTAL);
		maskText.setLayoutData(data);
		
		resultGroup=new Group(this,SWT.NONE);
		data=new GridData(GridData.FILL_BOTH);
		data.horizontalSpan=4;
		resultGroup.setLayoutData(data);
		resultGroup.setText("计算结果");
		resultGroup.setLayout(new FillLayout());
		resultText=new Text(resultGroup,SWT.MULTI | SWT.BORDER);
		
		buttonGroup=new Group(this,SWT.NONE);
		data=new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan=4;
		buttonGroup.setLayoutData(data);
		buttonGroup.setLayout(new FillLayout());
		
		
		caculateButton=new Button(buttonGroup,SWT.PUSH);
		caculateButton.setText("计算");
		caculateButton.addSelectionListener(new CaculateListener());
		
		copyButton=new Button(buttonGroup,SWT.PUSH);
		copyButton.setText("复制");
		copyButton.addSelectionListener(new CaculateListener());
		
		resetButton=new Button(buttonGroup,SWT.PUSH);
		resetButton.setText("重置");
		resetButton.addSelectionListener(new CaculateListener());
		
		helpButton=new Button(buttonGroup,SWT.PUSH);
		helpButton.setText("帮助");
		helpButton.addSelectionListener(new CaculateListener());
		
	}
	
	class CaculateListener extends SelectionAdapter{
		String[] ipClass={"回环地址","A类地址","B类地址","C类地址","D类（组播）地址","E类（保留）地址"};
		public void widgetSelected(SelectionEvent e){
			String ip=ipText.getText();
			String mask=maskText.getText();
			if(!IPTool.isValidIp(ip,false)){
				resultText.setText("非法ip地址");
				return;
			}else if(!IPTool.isValidMask(mask)){
				resultText.setText("非法子网掩码");
				return;
			}
			
			Button button=(Button)e.getSource();
			String source=button.getText();
			if(source.equals("计算")){
				String result="";
				int ipclass;
				try {
					ipclass = IPTool.getIpClass(ip);
					if(ipclass==-1){
						resultText.setText("非法ip地址");
						return;
					}
					result+=ipClass[ipclass]+":"+ip+"/"+IPTool.maskStyleChange(mask)+"\n";
					result+="网络地址是："+IPTool.getNetworkNumber(ip, mask)+"\n";
					result+="广播地址是："+IPTool.getBroadcastNumber(ip, mask)+"\n";
					result+="可分配的主机地址包括：";
					String[] assignableIp=IPTool.getStartAndEndIpInSubnet(ip, mask);
					result+=assignableIp[0]+"~"+assignableIp[1]+"\n";
					int assignableIpCount=IPTool.getAllValidIpBetweenStartAndEndIp(assignableIp[0], assignableIp[1]).size();
					result+="可用主机数："+assignableIpCount+"台\n";
					
				} catch (InvalidIpAddressException e1) {
					// TODO Auto-generated catch block
					resultText.setText("非法ip地址");
				}catch(InvalidIpMaskException e2){
					resultText.setText("非法子网掩码");
				}
				
				
				resultText.setText(result);
			}
			
			
		}
	}
	
	
}
