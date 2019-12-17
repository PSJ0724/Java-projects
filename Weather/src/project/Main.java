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
		 setTitle("java 날씨 프로젝트");
		 setSize(600,400);
	 	 setResizable(false);
	 	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	 
	 	 Font f = new Font("궁서체", Font.ITALIC, 11); 
		
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
	               setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	               super.paintComponent(g);
	           }
	       };
        
	    //panel == backgorund
	    setContentPane(background);
        panel = getContentPane();
        
		panel.setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setOpaque(false); //배경 투명하게
		
		p1.setLayout(new FlowLayout());
		//이미지 호출하고 크기 조절
		ImageIcon icon = new ImageIcon("img\\refresh.jpg");
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon( newimg );
		
		//새로고침 버튼 생성
		refreshBtn = new JButton(icon);
		refreshBtn.setSize(new Dimension(3, 3));
		refreshBtn.setContentAreaFilled(false); //이미지,글자외의 공간을 채울 것인가
		refreshBtn.setBorderPainted(false); //JButton의 테두리를 보이게 할 것인가
		refreshBtn.setFocusPainted(true);  //이미지,글자에 테두리를 보이게 할 것인가.
		refreshBtn.addActionListener(this);
		
		//도/시 입력창
		topField = new HintTextField("도/시");
		topField.setToolTipText("도/시");
		topField.setColumns(6);
		
		//시/군/구 입력창
		mdlField = new HintTextField("시/군/구");
		mdlField.setToolTipText("시/군/구");
		mdlField.setColumns(6);
		
		//읍/면/동 입력창
		leafField = new HintTextField("읍/면/동");
		leafField.setToolTipText("읍/면/동");
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
		
		
		//중앙에 메시지 날씨 정보
		
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
				JOptionPane.showMessageDialog(this, "존재하지 않는 지역입니다. 정확하게 입력해주세요.", "오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String region = RssCroller.topVal + " " + RssCroller.mdlVal + " " + RssCroller.leafVal; 
			String temp = (String) XmlSoup.maps[0].get("temp");
			String skyNum = (String) XmlSoup.maps[0].get("sky");
						
			String wfKor = (String) XmlSoup.maps[0].get("wfKor");
			/*
				 맑음
				 구름 조금
				 구름 많음
				 흐림
				 비
				 눈/비
				 눈
			*/

			//JFrame 가운데에 날씨 정보 표시
			wtLabel.setText("<html>"+ region +"<br>" + 
					wfKor + "<br>" +
					temp+"C"+	
					"</html>");
			
			wtLabel.setHorizontalAlignment(SwingConstants.CENTER);
			HashMap<String, String> weatherImageName = new HashMap<String, String>();
			
			String wfEn = ((String) XmlSoup.maps[0].get("wfEn")).replaceAll(" ", "");
			
			wfEn = (wfEn.equals("Snow/Rain"))? "Snow" : wfEn;
			setBackgroundWeatherImage(wfEn);	
			
			wtLabel.setFont(new Font("HY산B", Font.BOLD, 20));
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
			
			labels[i].setText("<html>" + hour +"시<br/>온도 : " + temp +"C</html>");
			
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
		//하늘상태
		HashMap<String, String> sky = new HashMap<String, String>();
		sky.put("1", "맑음");
		sky.put("3", "구름많음");
		sky.put("4", "흐림");
		

		//강수상태
		HashMap<String, String> pty = new HashMap<String, String>(); 
		pty.put("0", "없음");
		pty.put("1", "비");
		pty.put("2", "비/눈");
		pty.put("3", "눈");
		pty.put("4", "소나기");
		
		HashMap<String, String> wd = new HashMap<String, String>(); 
		wd.put("0", "북");
		wd.put("1", "북동");
		wd.put("2", "동");
		wd.put("3", "남동");
		wd.put("4", "남");
		wd.put("5", "남서");
		wd.put("6", "서");
		wd.put("7", "북서");
		
		String hour = (String) XmlSoup.maps[i].get("hour");
		String temp = (String) XmlSoup.maps[i].get("temp");
		String tmx = (String) XmlSoup.maps[i].get("tmx"); //최고기온
		tmx = (tmx.equals("-999.0")) ? "값이 없음" : tmx + "C";
		
		String tmn = (String) XmlSoup.maps[i].get("tmn"); //최저기온
		tmn = (tmn.equals("-999.0")) ? "값이 없음" : tmn + "C";
		
		String skyNum = (String) XmlSoup.maps[i].get("sky");
		String ptyNum = (String) XmlSoup.maps[i].get("pty"); //강수상태
		String pop = (String) XmlSoup.maps[i].get("pop"); //강수확률 %
		String ws = (String) XmlSoup.maps[i].get("ws");
		String wdNum = (String) XmlSoup.maps[i].get("wd");
		String reh = (String) XmlSoup.maps[i].get("reh");
		
		JLabel a = new JLabel("<html>" + hour +"시<br/>온도 : " + temp +"C</html>");
		
		c.add(new JLabel("<html>정량예보 시간 : " + hour +"시<br/></html>"));
		c.add(new JLabel("<html>온도 : "+ temp +"C<br/></html>"));
		c.add(new JLabel("<html>최고온도 : "+ tmx +"<br/></html>"));
		c.add(new JLabel("<html>최저온도 : "+ tmn +"<br/></html>"));
		c.add(new JLabel("<html>하늘상태 : "+ sky.get(skyNum) +"<br/></html>"));
		c.add(new JLabel("<html>강수상태 : "+ pty.get(ptyNum) +"<br/></html>"));
		c.add(new JLabel("<html>강수확률 : "+ pop +"%<br/></html>"));
		c.add(new JLabel("<html>풍속 : "+ Math.round(Double.valueOf(ws)) +"m/s<br/></html>"));
		c.add(new JLabel("<html>풍향 : "+ wd.get(wdNum) +"풍이 불고있습니다.<br/></html>"));
		c.add(new JLabel("<html>습도 : "+ reh +"%<br/></html>"));
		
		 String region = RssCroller.topVal + " " + RssCroller.mdlVal + " " + RssCroller.leafVal; 
		 setTitle(region + " " + hour + "시 날씨");
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


