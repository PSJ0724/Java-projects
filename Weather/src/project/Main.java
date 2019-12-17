package project;

import java.awt.*;	
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import javafx.scene.layout.Border;
import javafx.scene.shape.Box;

import java.awt.event.*;
import java.util.HashMap;
import java.util.concurrent.Flow;

public class Main extends JFrame implements ActionListener{
	
	JTextField topField, mdlField, leafField;
	ImageIcon image =  new ImageIcon();
	Container panel;
	JPanel background;
	JButton[] labels = new JButton[8];
	JButton refreshBtn;
	JLabel wtLabel = new JLabel();
	
	public Main() {
		// TODO Auto-generated constructor stub
		 setTitle("java ���� ������Ʈ");
		 setSize(600,400);
	 	 setResizable(false);
	 	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	 
	 	 Font f = new Font("�ü�ü", Font.ITALIC, 11); 
		
	 	background = new JPanel() {
	           public void paintComponent(Graphics g) {
	               // Approach 1: Dispaly image at at full size
	               g.drawImage(image.getImage(), 0, 0, null);
	               // Approach 2: Scale image to size of component
	               // Dimension d = getSize();
	               // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
	               // Approach 3: Fix the image position in the scroll pane
	               // Point p = scrollPane.getViewport().getViewPosition();
	               // g.drawImage(icon.getImage(), p.x, p.y, null);
	               setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
	               super.paintComponent(g);
	           }
	       };
        
	    //panel == backgorund
	    setContentPane(background);
        panel = getContentPane();
        
		panel.setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setOpaque(false); //��� �����ϰ�
		
		p1.setLayout(new FlowLayout());
		//�̹��� ȣ���ϰ� ũ�� ����
		ImageIcon icon = new ImageIcon("img\\refresh.jpg");
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon( newimg );
		
		//���ΰ�ħ ��ư ����
		refreshBtn = new JButton(icon);
		refreshBtn.setSize(new Dimension(3, 3));
		refreshBtn.setContentAreaFilled(false); //�̹���,���ڿ��� ������ ä�� ���ΰ�
		refreshBtn.setBorderPainted(false); //JButton�� �׵θ��� ���̰� �� ���ΰ�
		refreshBtn.setFocusPainted(true);  //�̹���,���ڿ� �׵θ��� ���̰� �� ���ΰ�.
		refreshBtn.addActionListener(this);
		
		//��/�� �Է�â
		topField = new HintTextField("��/��");
		topField.setToolTipText("��/��");
		topField.setColumns(6);
		
		//��/��/�� �Է�â
		mdlField = new HintTextField("��/��/��");
		mdlField.setToolTipText("��/��/��");
		mdlField.setColumns(6);
		
		//��/��/�� �Է�â
		leafField = new HintTextField("��/��/��");
		leafField.setToolTipText("��/��/��");
		leafField.setColumns(6);
		
		p1.add(topField);
		p1.add(mdlField);
		p1.add(leafField);
		p1.add(refreshBtn);
		
		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		p2.setLayout(new GridLayout(1, 8));
		
		
		for(int i = 0; i<8; i++) {
			labels[i] = new JButton();
			labels[i].addActionListener(this);
			labels[i].setBorder(new LineBorder(Color.black));
//			labels[i].setHorizontalAlignment(JLabel.CENTER);
//			labels[i].setVerticalTextPosition(JLabel.CENTER);
			labels[i].setOpaque(false);
			p2.add(labels[i]);
		}
		
		
		//�߾ӿ� �޽��� ���� ����
		
		panel.add(p1, BorderLayout.NORTH);
		panel.add(p2, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == refreshBtn) {
			boolean crollFlag = RssCroller.reset(topField.getText().trim(), mdlField.getText().trim(), leafField.getText().trim());
			if(false == crollFlag) {
				JOptionPane.showMessageDialog(this, "�������� �ʴ� �����Դϴ�. ��Ȯ�ϰ� �Է����ּ���.", "����", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String region = RssCroller.topVal + " " + RssCroller.mdlVal + " " + RssCroller.leafVal; 
			String temp = (String) XmlSoup.maps[0].get("temp");
			String skyNum = (String) XmlSoup.maps[0].get("sky");
						
			String wfKor = (String) XmlSoup.maps[0].get("wfKor");
			/*
				 ����
				 ���� ����
				 ���� ����
				 �帲
				 ��
				 ��/��
				 ��
			*/

			//JFrame ����� ���� ���� ǥ��
			wtLabel.setText("<html>"+ region +"<br>" + 
					wfKor + "<br>" +
					temp+"C"+	
					"</html>");
			
			wtLabel.setHorizontalAlignment(SwingConstants.CENTER);
			HashMap<String, String> weatherImageName = new HashMap<String, String>();
			
			String wfEn = ((String) XmlSoup.maps[0].get("wfEn")).replaceAll(" ", "");
			
			wfEn = (wfEn.equals("Snow/Rain"))? "Snow" : wfEn;
			setBackgroundWeatherImage(wfEn);	
			
			wtLabel.setFont(new Font("HY��B", Font.BOLD, 20));
			wtLabel.setForeground(Color.DARK_GRAY);
			panel.add(wtLabel, BorderLayout.CENTER);
			setVisible(true);
		}
		
		setTimeWeather();
		
		for(int i = 0; i<labels.length; i++) {
			if(e.getSource() == labels[i]) {
				new detailWeather(i);	
				return;
			}
		}
	}
	
	public void setTimeWeather() {
	 	for(int i = 0; i<labels.length; i++) {
	 		String hour = (String) XmlSoup.maps[i].get("hour");
			String temp = (String) XmlSoup.maps[i].get("temp");
			
			labels[i].setText("<html>" + hour +"��<br/>�µ� : " + temp +"C</html>");
			
	 	}
	}
	
	public void setBackgroundWeatherImage(String imageName) {
		image = new ImageIcon("img\\"+ imageName+".jpg");
		setContentPane(background);
	}

}

class detailWeather extends JFrame{
	public detailWeather(int i) {
		// TODO Auto-generated constructor stub
		
		Container c = getContentPane();
		c.setLayout(new GridLayout(10, 1));
		//�ϴû���
		HashMap<String, String> sky = new HashMap<String, String>();
		sky.put("1", "����");
		sky.put("3", "��������");
		sky.put("4", "�帲");
		

		//��������
		HashMap<String, String> pty = new HashMap<String, String>(); 
		pty.put("0", "����");
		pty.put("1", "��");
		pty.put("2", "��/��");
		pty.put("3", "��");
		pty.put("4", "�ҳ���");
		
		HashMap<String, String> wd = new HashMap<String, String>(); 
		wd.put("0", "��");
		wd.put("1", "�ϵ�");
		wd.put("2", "��");
		wd.put("3", "����");
		wd.put("4", "��");
		wd.put("5", "����");
		wd.put("6", "��");
		wd.put("7", "�ϼ�");
		
		String hour = (String) XmlSoup.maps[i].get("hour");
		String temp = (String) XmlSoup.maps[i].get("temp");
		String tmx = (String) XmlSoup.maps[i].get("tmx"); //�ְ���
		tmx = (tmx.equals("-999.0")) ? "���� ����" : tmx + "C";
		
		String tmn = (String) XmlSoup.maps[i].get("tmn"); //�������
		tmn = (tmn.equals("-999.0")) ? "���� ����" : tmn + "C";
		
		String skyNum = (String) XmlSoup.maps[i].get("sky");
		String ptyNum = (String) XmlSoup.maps[i].get("pty"); //��������
		String pop = (String) XmlSoup.maps[i].get("pop"); //����Ȯ�� %
		String ws = (String) XmlSoup.maps[i].get("ws");
		String wdNum = (String) XmlSoup.maps[i].get("wd");
		String reh = (String) XmlSoup.maps[i].get("reh");
		
		JLabel a = new JLabel("<html>" + hour +"��<br/>�µ� : " + temp +"C</html>");
		
		c.add(new JLabel("<html>�������� �ð� : " + hour +"��<br/></html>"));
		c.add(new JLabel("<html>�µ� : "+ temp +"C<br/></html>"));
		c.add(new JLabel("<html>�ְ�µ� : "+ tmx +"<br/></html>"));
		c.add(new JLabel("<html>�����µ� : "+ tmn +"<br/></html>"));
		c.add(new JLabel("<html>�ϴû��� : "+ sky.get(skyNum) +"<br/></html>"));
		c.add(new JLabel("<html>�������� : "+ pty.get(ptyNum) +"<br/></html>"));
		c.add(new JLabel("<html>����Ȯ�� : "+ pop +"%<br/></html>"));
		c.add(new JLabel("<html>ǳ�� : "+ Math.round(Double.valueOf(ws)) +"m/s<br/></html>"));
		c.add(new JLabel("<html>ǳ�� : "+ wd.get(wdNum) +"ǳ�� �Ұ��ֽ��ϴ�.<br/></html>"));
		c.add(new JLabel("<html>���� : "+ reh +"%<br/></html>"));
		
		 String region = RssCroller.topVal + " " + RssCroller.mdlVal + " " + RssCroller.leafVal; 
		 setTitle(region + " " + hour + "�� ����");
		 setSize(300,300);
		 setVisible(true);
	}
}

class HintTextField extends JTextField {  


  public HintTextField(final String hint) {  
    setText(hint);  
    setForeground(Color.GRAY);  

    this.addFocusListener(new FocusAdapter() {  
      @Override  
      public void focusGained(FocusEvent e) {  
        if (getText().equals(hint)) {  
          setText("");   
        } else {  
          setText(getText());
        }  
      }  
      @Override  
      public void focusLost(FocusEvent e) {  
        if (getText().equals(hint)|| getText().length()==0) {  
          setText(hint);    
          setForeground(Color.GRAY);  
        } else {  
          setText(getText());  
          setForeground(Color.BLACK); 
        }  
      }  
    });  
  }  
}  


