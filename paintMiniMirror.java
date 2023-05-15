import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


class Activity implements Serializable
{
    int oldX ,oldY, x,  y,
    lX , lY ,rX , rY , rWidth , rHeight , xRadius , yRadius ;
    Color color=null;
    
    byte mode;
    
    {oldX=oldY=x=y=lX=lY=rX=rY=rWidth=rHeight=xRadius=rHeight=mode=0;}

}


class CustomButton extends Button
{
    int mode;  
    
    
    public void paint(Graphics g)
    {
        //g.clearRect(getX(),getY(),getWidth(),getHeight());
        
        
      
        
        switch(mode)
        {
            case 1:  
                    g.setColor(Color.red);
                    //g.drawLine((int)(0.83*getWidth()),(int)(0.17*getHeight()),(int)(0.16*getWidth()),(int)(0.64*getHeight()));
                     //g.drawLine((int)(0.83*getWidth()),(int)(0.23*getHeight()),(int)(0.16*getWidth()),(int)(0.70*getHeight()));
                      //g.drawLine((int)(0.83*getWidth()),(int)(0.29*getHeight()),(int)(0.16*getWidth()),(int)(0.76*getHeight()));
                      
                      Polygon p2=new Polygon();
                      p2.addPoint((int)(0.83*getWidth()),(int)(0.17*getHeight()));
                       p2.addPoint((int)(0.83*getWidth()),(int)(0.29*getHeight()));
                        p2.addPoint((int)(0.16*getWidth()),(int)(0.76*getHeight()));
                         p2.addPoint((int)(0.16*getWidth()),(int)(0.64*getHeight()));
                     
                         g.fillPolygon(p2);
                    //g.setColor(Color.black);
                
                break;
            case 2:  g.drawLine((int)(0.20*getWidth()),(int)(0.29*getHeight()),(int)(0.66*getWidth()),(int)(0.64*getHeight()));
                g.drawLine((int)(0.20*getWidth())+1,(int)(0.29*getHeight())+1,(int)(0.66*getWidth())+1,(int)(0.64*getHeight())+1);
            
            break;
            case 3: g.drawRect((int)(0.17*getWidth()),(int)(0.11*getHeight()),(int)(0.61*getWidth()),(int)(0.53*getHeight()));
                
            break;
            case 4:  g.drawOval((int)(0.20*getWidth()),(int)(0.12*getHeight()),(int)(0.50*getWidth()),(int)(0.59*getHeight())); break;
            case 5: Polygon p=new Polygon(); p.addPoint((int)(0.33*getWidth()),(int)(0.11*getHeight()));  p.addPoint((int)(0.62*getWidth()),(int)(0.11*getHeight()));
            p.addPoint((int)(0.20*getWidth()),(int)(0.88*getHeight())); g.drawPolygon(p);   break;
            
            case 6: 
                g.setColor(Color.blue);
                g.fillRect((int)(0.17*getWidth()),(int)(0.11*getHeight()),(int)(0.61*getWidth()),(int)(0.53*getHeight())); break;
        
        }
           
            /*
                switch(mode)
        {
            case 1: drawImageOnComponent("C:\\Users\\Dhruv Pc\\Documents\\From Desktop\\pencil.png",g); break;
            case 2: drawImageOnComponent("C:\\Users\\Dhruv Pc\\Documents\\From Desktop\\line.png",g); break;
            case 3: drawImageOnComponent("C:\\Users\\Dhruv Pc\\Documents\\From Desktop\\rectangle.png",g); break;
            case 4: drawImageOnComponent("C:\\Users\\Dhruv Pc\\Documents\\From Desktop\\circle.png",g); break;
            case 5: drawImageOnComponent("C:\\Users\\Dhruv Pc\\Documents\\From Desktop\\polygon.png",g); break;
        
        }
            */
            
        
    
    }
    
    
    void drawImageOnComponent(String filepath,Graphics g)
    {
        Toolkit kit=Toolkit.getDefaultToolkit();
        Image img=kit.getImage(filepath);
        g.drawImage(img, 0 , 0, this);
        
    }
    
    CustomButton(int m)
    {
        mode=m;
        
    }

}


class CustomDialog extends Dialog implements WindowListener
{
    
    CustomDialog(Frame m)
    {super(m); addWindowListener(this);}
    
    public void windowOpened(WindowEvent e)
    {}

    public void windowClosing(WindowEvent e)
    {show(false);}
    
    public void windowClosed(WindowEvent e)
    {}
    
    public void windowActivated(WindowEvent e)
    {}
    
    public void windowDeactivated(WindowEvent e)
    {}
    
    public void windowIconified(WindowEvent e)
    {}
    
    public void windowDeiconified(WindowEvent e)
    {
        
    }

}


class PaintingApplication extends Frame implements ActionListener,MouseListener,MouseMotionListener
{
    
    volatile Vector<Activity> currentActivities=new Vector<>();
    volatile boolean isOnline=false;
    volatile boolean isClient=false;
    Thread requestThread;
    
    MenuItem startMirror,stopMirror;
    volatile ServerSocket server;
    volatile Socket socket;
    volatile ObjectInputStream objectInput;
    volatile ObjectOutputStream objectOutput;
    
    
    Thread sendingThread,receivingThread;
    Thread updationThread;
    
    Button connect=new Button("Connect"),accept=new Button("Accept Request");
    Label targetIp=new Label("Target IP");
    TextField targetIpText=new TextField();
    TextField ipInfoText=new TextField("");
    
    MyCanvas sheet,tempSheet=new MyCanvas();
    
    MenuItem configureRadius=new MenuItem("Configure Radius "),exit=new MenuItem("Exit"),save=new MenuItem("Save"),refresh=new MenuItem("Refresh");
    MenuItem open=new MenuItem("Open");
    MenuItem ipInfo=new MenuItem("My Address");
    
    CustomDialog radiusDialog,saveDialog,openDialog,mirrorStartDialog,mirrorStopDialog,ipInfoDialog=new CustomDialog(this);
    
    int x,y,oldX,oldY,rX,rY,rHeight,rWidth;
    
    boolean lineStarted=false,lineReleased=false;
    int lX,lY;
    
    MenuBar menuBar;
    Panel toolBar;
    Menu menu;
    Menu mirror;
   int mode=1;
    
    int xRadius=100,yRadius=100;
    
    Panel colorBox;
    Panel tools=new Panel();
    Button blue=new Button(),cyan=new Button(),dark_gray=new Button(),
            gray=new Button(),green=new Button(),
            
            light_gray=new Button(),magenta=new Button(),orange=new Button(),
            pink=new Button()
            ,red=new Button(),white=new Button(),yellow=new Button(),black=new Button();
    
    
   Color currentColor=Color.black;
    Button selectedColor=new Button();
    CustomButton pencil=new CustomButton(1);
    CustomButton line=new CustomButton(2),rectangle=new CustomButton(3),circle=new CustomButton(4),polygon=new CustomButton(5),
            solidRectangle=new CustomButton(6);
    
 
    TextField xRadiusText=new TextField(""+xRadius),yRadiusText=new TextField(""+yRadius);
        Button setRadius=new Button("Set");
        
    TextField saveText=new TextField(""),openText=new TextField();
    Button saveButton=new Button("Save"),saveCancelButton=new Button("Cancel"),openButton=new Button("Open"),openCancelButton=new Button("Cancel");
        
         Label xLabel=new Label("X"),yLabel=new Label("Y");
        
    
    { blue.setBackground(Color.blue); blue.setActionCommand("blue");
      cyan.setBackground( Color.cyan); cyan.setActionCommand("cyan"); 
      dark_gray.setBackground( Color.darkGray); dark_gray.setActionCommand("darkGray");
      gray.setBackground( Color.gray);  gray.setActionCommand("gray");
      green.setBackground( Color.green); green.setActionCommand("green");
     // light_gray.setBackground( Color.lightGray);  light_gray.setActionCommand("lightGray");
      magenta.setBackground( Color.magenta); magenta.setActionCommand("magenta");
      orange.setBackground( Color.orange); orange.setActionCommand("orange");
      pink.setBackground( Color.pink); pink.setActionCommand("pink");
      red.setBackground( Color.red);  red.setActionCommand("red");
      white.setBackground( Color.white); white.setActionCommand("white");
      yellow.setBackground( Color.yellow); yellow.setActionCommand("yellow");
      black.setBackground(Color.black); black.setActionCommand("black");
      line.setActionCommand("line");
      circle.setActionCommand("circle");
      rectangle.setActionCommand("rectangle");
      solidRectangle.setActionCommand("solidRectangle");
      polygon.setActionCommand("polygon");
      pencil.setActionCommand("pencil");
      
    }
    
    
    boolean rectStarted=false;
    boolean released=false;
    boolean isLoadingFile=false;
    
class MyCanvas extends Canvas
{
    
    
    public void paint(Graphics c)
    {
     c.setColor(currentColor); 
     Activity act=new Activity();
     
     //System.out.println("Current Thread in paint()"+Thread.currentThread());
    // System.out.println(currentActivities.size());
     
     
        if(isLoadingFile==false)
        { //System.out.println("Current mode = "+mode);
        
     
        switch(mode)
        {
            case 0:  c.drawLine(oldX, oldY, x, y); act.oldX=oldX; act.oldY=oldY; act.x=x; act.y=y; act.mode=0; act.color=currentColor; currentActivities.add(act); 

             if(isOnline)
                        sendNewActivity(act);
             
             break;

//c.drawRect(x, y, 0, 0); break;
            case 1: //Line case
             //   System.out.println("line");
                 if(lineReleased==true)
                {
                    c.drawLine(lX, lY, x, y);
                    lineStarted=false;
                    lineReleased=false;
                    
                    act.mode=1;
                    act.lX=lX; act.lY=lY; act.x=x; act.y=y;
                    act.color=currentColor; 
                    
                    currentActivities.add(act);
                    
                    if(isOnline)
                        sendNewActivity(act);
                
                }
                
                    
                
                break;
            case 2: 
                
                if(rectStarted==false)
                {
                    rX=x;
                    rY=y;
                    
                   rectStarted=true;
                   released=false;
                }
                
                else if(released==true)
                { if(x>=rX)
                    rWidth=x-rX;
                    
                    else {rWidth=rX-x; rX=x;}
                    
                    if(y>=rY)
                    rHeight=y-rY;
                    
                    else {rHeight=rY-y; rY=y;}
                    
                    
                    
                    rectStarted=false;
                    c.drawRect(rX, rY, rWidth, rHeight);
                    
                    act.rX=rX; act.rY=rY; act.rWidth=rWidth; act.rHeight=rHeight; act.mode=2;
                    act.color=currentColor; 
                    currentActivities.add(act);
                   
                    if(isOnline)
                        sendNewActivity(act);
                }
                 break;
                
            case 3: 
                
                c.drawOval(x, y, xRadius, yRadius);
                
               act.x=x; act.y=y; act.xRadius=xRadius; act.yRadius=yRadius;
               act.mode=3;
               act.color=currentColor; 
               currentActivities.add(act);
               
                if(isOnline)
                        sendNewActivity(act);
                    
               
                break;
            case 4: break; // was supposed to be polygon feature but was removed
            case 5: 
               //  System.out.println("Entered case 5 solid rectangle");
                if(rectStarted==false)
                {
                    rX=x;
                    rY=y;
                    
                   rectStarted=true;
                   released=false;
                }
                
                else if(released==true)
                {
                    
                      
                    if(x>=rX)
                    rWidth=x-rX;
                    
                    else {rWidth=rX-x; rX=x;}
                    
                    if(y>=rY)
                    rHeight=y-rY;
                    
                    else {rHeight=rY-y; rY=y;}
                    
                    
                    rectStarted=false;
                    
                    c.fillRect(rX, rY, rWidth, rHeight);
                    
                    act.rX=rX; act.rY=rY; act.rWidth=rWidth; act.rHeight=rHeight; act.mode=5;
                    act.color=currentColor; 
                    currentActivities.add(act);
                    
                   if(isOnline)
                    { sendNewActivity(act);System.out.println("sent a filled rectangle");}
                    
                    
                }
                 break;
        
        }
        
        }
        
        else
        {
            switch(mode)
            {
                case 0:   c.drawLine(oldX, oldY, x, y);   break;
                case 1:   c.drawLine(lX, lY, x, y); break;
                case 2:   c.drawRect(rX, rY, rWidth, rHeight);  break;
                case 3:   c.drawOval(x, y, xRadius, yRadius);  break;
                case 5:   c.fillRect(rX, rY, rWidth, rHeight);  break;
                
                
            
            }
        
        
        
        
        }
        
        
    }

    
}


  synchronized  void loadImageOnScreen()
    {
        Activity a;
        //boolean previousWasPencil=false;
        isLoadingFile=true;
        int savedMode=mode;
        Color savedColor=currentColor;
        int savedXRadius=xRadius;
        int savedYRadius=yRadius;
        
       // System.out.println("loadingImage()");
        
        //This following if isnt even used anymore forreal purposes 
       
        
        for(int i=0;i<currentActivities.size();i++)
        {
            a=currentActivities.get(i);
            
            mode=a.mode;
            
                if(mode==0)
            {
                oldX=a.oldX; oldY=a.oldY;
                x=a.x;
                y=a.y;
                currentColor=a.color;
                sheet.paint(sheet.getGraphics());
            }
            
            else if(mode==1)
            {
                lX=a.lX; lY=a.lY; x=a.x; y=a.y;
                currentColor=a.color;
                sheet.paint(sheet.getGraphics());
                
            
            }
                
            else if(mode==2)
            {
                currentColor=a.color;
                
                rX=a.rX; rY=a.rY; rWidth=a.rWidth; rHeight=a.rHeight;
                sheet.paint(sheet.getGraphics());
                
            
            }
            
            else if(mode==3)
            {
                currentColor=a.color;
                
                x=a.x; y=a.y; xRadius=a.xRadius; yRadius=a.yRadius;
                
                
                sheet.paint(sheet.getGraphics());
                
            
            }
                
            else if(mode==5)
            {
                currentColor=a.color;
                
                rX=a.rX; rY=a.rY; rWidth=a.rWidth; rHeight=a.rHeight;
                
                sheet.paint(sheet.getGraphics());
                
                
                
                
            
            }
        }
    
    
        isLoadingFile=false;
        mode=savedMode;
        currentColor=savedColor;
        
        xRadius=savedXRadius;
        yRadius=savedYRadius;
        
      //  System.out.println("loading on reciever "+isClient);
    }

    void openImage(String filename)
    {
    //    System.out.println("opening "+filename);
        
        try{
            FileInputStream file=new FileInputStream(filename);
            ObjectInputStream input=new ObjectInputStream(file);
            
            currentActivities.clear();
            Object o;
            //This loop reads all objects from the file
            try{
                while(true)
                {
                    o=input.readObject();
                    
                    currentActivities.add((Activity)o);
                }
                
            }catch(EOFException e){}
            catch(Exception e){}
            
            loadImageOnScreen();
            
            
        
        }catch(FileNotFoundException not){
        
            
            try{
                openText.setText("File not Found");
                openText.setEditable(false);
                Thread.sleep(1500);
                openText.setText("");
                openText.setEditable(true);
                
                
            }catch(InterruptedException e){}
            
        return;
        }
        catch(Exception e)
        {}
        
        openDialog.show(false);
    }

 
    void saveImage(String filename)
    {
       try{
           FileOutputStream file=new FileOutputStream(filename);
           ObjectOutputStream output=new ObjectOutputStream(file);
           
           for(int i=0;i<currentActivities.size();i++)
           {
               output.writeObject(currentActivities.get(i));
           }
           
       
       }catch(Exception e){}
        
       try{
           saveText.setText("file saved");
           Thread.sleep(1500);
           saveText.setText("");
       
       }catch(InterruptedException e){}
    
    }


    public void mouseDragged(MouseEvent m)
    {
        sheet.paint(sheet.getGraphics());
        
        oldX=x;
        oldY=y;
        
        x=m.getX();
        y=m.getY();
       // System.out.println("motion");
    }
    
    public void mouseMoved(MouseEvent m)
    {
       
            if(isLoadingFile)
                return;
            
            
       /*  if(isClient&&isOnline)
           sendData();
         else if(isOnline)
            recieveData();
         
         */
         
     /*  if(isOnline&&!isClient)
           performNewActivity();
       */
     
           oldX=x;
           oldY=y;
     
        x=m.getX(); y=m.getY();}
    
    
    public void mousePressed(MouseEvent m)
    {
        x=m.getX();
        y=m.getY();
        
        if(mode==1&&lineStarted==false)
        {
                    lineStarted=true;
                    lX=x;
                    lY=y;
                
                
                
        
        }
        
        
        
        
        
        sheet.paint(sheet.getGraphics());
    }
    
    
    public void mouseReleased(MouseEvent m)
    {
      //  System.out.println("released");
     
        if(mode==2)
            rectStarted=true;
        released=true;
        
        if(mode==1)
            lineReleased=true;
        
        
        
        sheet.paint(sheet.getGraphics());
        
    }
    
    
    public void mouseClicked(MouseEvent m)
    { //System.out.println("clicked");
          
    }
    
    public void mouseEntered(MouseEvent m)
    {
       loadImageOnScreen();
    
    }
    
    
    public void mouseExited(MouseEvent m)
    {loadImageOnScreen();
    }
    
    
    
    void reAdjustComponents()
    {
         toolBar.setBounds(1,50,this.getWidth(),(int)(0.10*getHeight()));
        sheet.setBounds((int)(0.04*getHeight()),toolBar.getY()+toolBar.getHeight()+   (int)(0.02*getHeight())  ,getWidth()-(int)(0.08*getHeight()),getHeight()-(int)(0.1*getHeight()));
        colorBox.setBounds((int)(0.45*getWidth()),(int)(0.02*getHeight()),(int)(0.28*getWidth()),(int)(0.04*getHeight())*2);
        
       selectedColor.setBounds(colorBox.getX()-(int)(0.08*getWidth()),(int)(0.01*getHeight()) ,(int)(0.04*getWidth()),(int)(0.04*getWidth()));
        tools.setBounds((int)(0.1*getWidth()),(int)(0.02*getHeight()),(int)(0.24*getWidth()),(int)(0.04*getHeight()));
       
       colorBox.repaint();
    }
    
    public void paint(Graphics pen)
    {
        reAdjustComponents();
        
        
    
    }
    
    //Totally unneccessary method 
    void setAllBgs(Component[] c)
    {
        for(int i=0;i<c.length;i++)
            c[i].setBackground(Color.white);
    
    }
    
    
    class Closer extends WindowAdapter
    {
        public void windowClosing(WindowEvent w)
        {System.exit(0);}
    }
    
    
    PaintingApplication()
    {   
        setSize(620,440);
        setBackground(Color.gray);
        setLayout(null);
        
        setTitle("Offline Mode");
        
        mirror=new Menu("Mirroring");
        
        saveDialog=new CustomDialog(this);
        saveDialog.setSize(200,130);
        
        saveDialog.setLocation(100,100);
        
       // saveDialog.show();
        
        saveDialog.setLayout(null);
        
        saveText.setBounds(20,40,150,25);
        
        saveDialog.add(saveText);
        saveDialog.addWindowListener(saveDialog);
        
        saveButton.setBounds(20,90,60,25);
        saveButton.addActionListener(this);
        saveDialog.add(saveButton);
        
        saveCancelButton.setBounds(100,90,60,25);
        saveCancelButton.addActionListener(this);
        saveDialog.add(saveCancelButton);
        
        saveButton.setActionCommand("saveButton");
        saveCancelButton.setActionCommand("saveCancelButton");
        
        
        /*________Code for Open Dialogbox ui__________*/
        open.addActionListener(this);
        open.setActionCommand("open");
        openDialog=new CustomDialog(this);
        openDialog.setSize(200,130);
        
        openDialog.setLocation(100,100);
        
     //   openDialog.show();
        
        openDialog.setLayout(null);
        
        openText.setBounds(20,40,150,25);
        
        openDialog.add(openText);
        openDialog.addWindowListener(openDialog);
        
        openButton.setBounds(20,90,60,25);
        openButton.addActionListener(this);
        openDialog.add(openButton);
        
        openCancelButton.setBounds(100,90,60,25);
        openCancelButton.addActionListener(this);
        openDialog.add(openCancelButton);
        
        openButton.setActionCommand("openButton");
        openCancelButton.setActionCommand("openCancelButton");
        
        openDialog.setTitle("Open file ");
        /*__________________*/
        
        radiusDialog=new CustomDialog(this);
        radiusDialog.setSize(200,200);
        radiusDialog.setLocation(100,100);
        
        radiusDialog.addWindowListener(radiusDialog);
        
        saveDialog.setTitle("Enter file name");
        
        
           //  radiusDialog.show();
        radiusDialog.setLayout(null);
        
        radiusDialog.setTitle("Oval Radius Config");
        
        
        xRadiusText.setBounds(40,60,120,25);
        radiusDialog.add(xRadiusText);
        
        yRadiusText.setBounds(40,120,120,25);
        radiusDialog.add(yRadiusText);
        
        setRadius.setBounds(60,160,50,20);
        
        radiusDialog.add(setRadius);
        
        setRadius.addActionListener(this);
        setRadius.setActionCommand("setRadius");
        
        xLabel.setBounds(20,60,20,25);
        yLabel.setBounds(20,120,20,25);
        
        
        radiusDialog.add(xLabel);
        radiusDialog.add(yLabel);
        
        menuBar=new MenuBar();
        setMenuBar(menuBar);
        
        menu=new Menu("File");
        
        menuBar.add(menu);
        
      
        
        menuBar.add(mirror);
        
        startMirror=new MenuItem("Send");
        stopMirror=new MenuItem("Recieve");
        
        mirror.add(startMirror);
        mirror.add(stopMirror);
        
        startMirror.addActionListener(this);
        
        stopMirror.addActionListener(this);
        
        
        mirrorStartDialog=new CustomDialog(this);
        mirrorStartDialog.setTitle("Start Screen Mirroring");
        mirrorStopDialog=new CustomDialog(this);
        mirrorStopDialog.setTitle("Accept Mirroring Request");
        
        
        mirrorStopDialog.setBounds(100,100,250,100);
        mirrorStartDialog.setBounds(100,100,250,115);
        
        connect.addActionListener(this);
        
        accept.addActionListener(this);
        
        mirrorStartDialog.add(targetIp);
        mirrorStartDialog.setLayout(null);
        targetIp.setBounds(10,40,60,30);
        mirrorStartDialog.add(targetIpText);
        targetIpText.setBounds(80,40,110,25);
        mirrorStartDialog.add(connect);
        
        connect.setBounds(90,70,60,30);
        
        mirrorStopDialog.add(accept);
        mirrorStopDialog.setLayout(null);
        accept.setBounds(80,40,100,30);
        
        mirror.add(ipInfo);
        
        ipInfo.addActionListener(this);
        
        ipInfoDialog.setTitle("Device Ip Address");
        
        ipInfoDialog.setBounds(100,100,250,100);
        
        ipInfoDialog.add(ipInfoText);
        
        ipInfoText.setBounds(80,40,100,30);
        ipInfoText.setEnabled(false);
        
        MenuItem lightRefresh=new MenuItem("Light Refresh");
        lightRefresh.setActionCommand("Light Refresh");
        lightRefresh.addActionListener(this);
        
        /*____________________________*/
        
        
        menu.add(open);
           menu.add(configureRadius);
        open.setActionCommand("open");
        open.addActionListener(this);
        
        menu.add(refresh);
        refresh.setActionCommand("refresh");
        refresh.addActionListener(this);
        
        menu.add(lightRefresh); // locally added
        
        save.addActionListener(this);
        menu.add(save);
        save.setActionCommand("save");
        
        
        
        /*_________________________*/
        
        
        
        menu.add(exit);
        
        
        
        
        exit.addActionListener(this);
        configureRadius.addActionListener(this);
        configureRadius.setActionCommand("configureRadius");
        
        
        
        toolBar=new Panel();
        toolBar.setBounds(1,50,this.getWidth(),44);
        
        toolBar.setBackground(Color.lightGray);
        
        toolBar.setLayout(null);
        
        add(toolBar);
        
        sheet=new MyCanvas();
        
        sheet.setBounds((int)(0.04*getHeight()),toolBar.getY()+toolBar.getHeight()+   (int)(0.02*getHeight())  ,getWidth()-(int)(0.08*getHeight()),getHeight()-(int)(0.1*getHeight()));
        
        sheet.setBackground(Color.white);
        
        add(sheet);
        
        colorBox=new Panel();
        colorBox.setBackground(Color.white);
          colorBox.setBounds((int)(0.45*getWidth()),(int)(0.02*getHeight()),(int)(0.28*getWidth()),(int)(0.04*getHeight())*2);
        toolBar.add(colorBox);
        
        colorBox.setLayout(new GridLayout(2,7));
        colorBox.add(blue);
        colorBox.add(cyan);
        
        colorBox.add(dark_gray);
        colorBox.add(gray );
        colorBox.add(green);
     //   colorBox.add(light_gray);
        colorBox.add(magenta);
        colorBox.add(orange);
        colorBox.add(pink);
        colorBox.add(red);
        colorBox.add(white);
       colorBox.add(yellow );
       colorBox.add(black);
        
       selectedColor.setBounds(colorBox.getX()-(int)(0.08*getWidth()),(int)(0.01*getHeight()) ,(int)(0.04*getWidth()),(int)(0.04*getWidth())*4);
       selectedColor.setBackground(currentColor);
       selectedColor.setEnabled(false);
       toolBar.add(selectedColor);
       
       black.addActionListener(this);
       blue.addActionListener(this);
       cyan.addActionListener(this);
       dark_gray.addActionListener(this);
       gray.addActionListener(this);
       green.addActionListener(this);
       light_gray.addActionListener(this);
       magenta.addActionListener(this);
       orange.addActionListener(this);
       pink.addActionListener(this);
       red.addActionListener(this);
       white.addActionListener(this);
       yellow.addActionListener(this);
       
             tools.setBounds((int)(0.1*getWidth()),(int)(0.02*getHeight()),(int)(0.24*getWidth()),(int)(0.04*getHeight()));
       tools.setBackground(Color.white);
       toolBar.add(tools);
       
       
       
       tools.setLayout(new GridLayout());
       
       Component[] c=new Component[]{pencil,line,rectangle,circle,polygon};
       
       this.setAllBgs(c);
       
       tools.add(pencil);
       tools.add(line);
       tools.add(rectangle);
       tools.add(circle);
     //  tools.add(polygon);
       tools.add(solidRectangle);
       
       pencil.addActionListener(this);
       line.addActionListener(this);
       rectangle.addActionListener(this);
       circle.addActionListener(this);
      // polygon.addActionListener(this);
       solidRectangle.addActionListener(this);
       
       addWindowListener(new Closer());
       
      sheet.addMouseListener(this);
       sheet.addMouseMotionListener(this);
        setVisible(true);
    
    
    }
    
    void setStreams()
    {  
        try{ 
     
         
            objectOutput=new ObjectOutputStream(socket.getOutputStream());
           // objectOutput.flush();
     
        
            objectInput=new ObjectInputStream(socket.getInputStream());
            
            
           // while(objectInput.read()!=-1)System.out.printf("Hello");
           
           
            
        }catch(Exception e){System.out.println(e);}
    
   //  System.out.println("Successfully exits setStream()");
    }
    
    public void connectToPartner()
    {isClient=true;
      
       try{
           socket=new Socket(this.targetIpText.getText(),8088);
           
           isOnline=true;
           isClient=true;
           
           setStreams();
           setTitle("Online Mode");
           
           updationThread=new Thread()
           {
               public void run()
               {
                   while(isOnline)
                   performNewActivity();
               
               }
           
           };
       
           updationThread.start(); //Client updation thread started
           
           
       }catch(Exception e){System.out.println(e);}
        
        
        
        }
    
    
    void acceptConnectionFromPartner()
    {
        isClient=false;
        PaintingApplication app=this;
        
        receivingThread=new Thread()
        {
            public void run()
            {
                try{ 
                server=new ServerSocket(8088);
                socket=server.accept();
                app.setTitle("Online Mode");
                
                isOnline=true;
                isClient=false;
                
                setStreams();
                
                updationThread=new Thread()
                {
                    public void run()
                    {
                       
                        try{while(isOnline)
                        app.performNewActivity();}catch(Exception e){ System.out.println("networking terminated");}
                    }
                
                };
                updationThread.setName("updationThread");
                updationThread.start();
                
                
                
                }catch(Exception e){System.out.println(e);}
            }
        
        };
        
        receivingThread.start();
        
        
        
        
        
       // System.out.println("Connected to sender");
        }
    
    
    public void sendNewActivity(Activity a)
    {//Use the commented part for guaranteed execution of this service
        try{
            objectOutput.writeObject(a);
            
        
        }catch(Exception e){System.out.println(e+" activity");}
    
        
        //Following is an experimental code
    /*    try{
            objectOutput.writeObject(currentActivities);
        
        
        }catch(Exception e){refresh();}
        */
    }
    
    public void performNewActivity()
    {//Use the commented part for guaranteed execution of this service
        
        try{
        
            Activity a=(Activity)objectInput.readObject();
        
            
            useThisObject(a);
        
        }catch(Exception e)
        {System.out.println(e+" perform ");
        
        //e.printStackTrace();
        
        refresh();}
    
    
        //Following is an experimental code
       /* try{
            
            currentActivities=(Vector<Activity>)objectInput.readObject();
        
          for(int i=0;i<currentActivities.size();i++)
              useThisObject(currentActivities.get(i));
          
        }catch(Exception e){refresh();}
*/
    }
    
    public void useThisObject(Activity a)
    {
    
      currentActivities.add(a);
        //boolean previousWasPencil=false;
        isLoadingFile=true;
        int savedMode=mode;
        Color savedColor=currentColor;
        int savedXRadius=xRadius;
        int savedYRadius=yRadius;
        
      //  System.out.println("Using Image object recieved ()");
        
       
            
            mode=a.mode;
            
                if(mode==0)
            {
                oldX=a.oldX; oldY=a.oldY;
                x=a.x;
                y=a.y;
                currentColor=a.color;
                sheet.paint(sheet.getGraphics());
            }
            
            else if(mode==1)
            {
                lX=a.lX; lY=a.lY; x=a.x; y=a.y;
                currentColor=a.color;
                sheet.paint(sheet.getGraphics());
                
            
            }
                
            else if(mode==2)
            {
                currentColor=a.color;
                
                rX=a.rX; rY=a.rY; rWidth=a.rWidth; rHeight=a.rHeight;
                sheet.paint(sheet.getGraphics());
                
            
            }
            
            else if(mode==3)
            {
                currentColor=a.color;
                
                x=a.x; y=a.y; xRadius=a.xRadius; yRadius=a.yRadius;
                
                
                sheet.paint(sheet.getGraphics());
                
            
            }
                
            else if(mode==5)
            {
                currentColor=a.color;
                
                rX=a.rX; rY=a.rY; rWidth=a.rWidth; rHeight=a.rHeight;
                sheet.paint(sheet.getGraphics());
                
                
            
            }
     
    
    
        isLoadingFile=false;
        mode=savedMode;
        currentColor=savedColor;
        
        xRadius=savedXRadius;
        yRadius=savedYRadius;
        
       
    
    }
    
    void checker(){
        System.out.println(server.isClosed());
        System.out.println(socket.isClosed());}
    
    public void sendData()
    {
        
        if(!isOnline)
            return;
     
        
       
   
    }
    
    public void recieveData()
    {
        
        if(!isOnline)
            return;
        
        
   
        
      
}
    
    
    
    
    @Override
  public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
        case "black": selectedColor.setBackground(Color.black); currentColor=Color.black; break;
        case "blue":  selectedColor.setBackground(Color.blue); currentColor=Color.blue; break;
        case "cyan":  selectedColor.setBackground(Color.cyan); currentColor=Color.cyan; break;
        case "darkGray": selectedColor.setBackground(Color.darkGray); currentColor=Color.darkGray; break;
        case "gray":  selectedColor.setBackground(Color.gray); currentColor=Color.gray; break;
        case "green":  selectedColor.setBackground(Color.green); currentColor=Color.green; break;
        case "lightGray":  selectedColor.setBackground(Color.lightGray); currentColor=Color.lightGray; break;
        case "magenta": selectedColor.setBackground(Color.magenta); currentColor=Color.magenta; break;
        case "orange":  selectedColor.setBackground(Color.orange); currentColor=Color.orange; break;
        case "pink":  selectedColor.setBackground(Color.pink); currentColor=Color.pink; break;
        case "red":  selectedColor.setBackground(Color.red);  currentColor=Color.red;  break;
        case "white":  selectedColor.setBackground(Color.white); currentColor=Color.white; break;
        case "yellow":   selectedColor.setBackground(Color.yellow); currentColor=Color.yellow; break;
        case "pencil" : mode=0; break;
        case "line" : mode=1; break;
        case "rectangle" : mode=2; break;
        case "circle" : mode=3; break;
        case "polygon" : mode=4; break;
        case "solidRectangle" : mode=5;   break;
        case "setRadius" : 
            
                    try{
                            xRadius=Integer.parseInt(xRadiusText.getText()) ;
                           yRadius=Integer.parseInt(yRadiusText.getText());
                           
                           radiusDialog.show(false);
                    }catch(Exception ex)
                    {
                        
                        xRadiusText.setText("only whole numbers");
                        yRadiusText.setText("only whole numbers");
                        
                        xRadiusText.setEditable(false);
                        yRadiusText.setEditable(false);
                        
                        try{ Thread.sleep(1000); }catch(InterruptedException zz){}
                        
                        xRadiusText.setText(""+xRadius);
                        yRadiusText.setText(""+yRadius);
                        
                        xRadiusText.setEditable(true);
                        yRadiusText.setEditable(true);
                        
                    
                    }
                           break;
                
        case "configureRadius" : radiusDialog.show(true); break;
        
        case "saveCancelButton": saveDialog.show(false); break;
        
        case "saveButton" : saveImage(saveText.getText()); saveDialog.show(false); break;
        
        
        case "save" : saveDialog.show(true); break;
        
        case "refresh" :  refresh(); break;
        
        case "openCancelButton" : openDialog.show(false); break;
        
        case "open": openDialog.show(); break;
        
        case "openButton" :   openImage(openText.getText()); break;
        
        case "Send" :  mirrorStartDialog.show(true); break;
        
        case "Recieve" : mirrorStopDialog.show(true); break;
        
        case "Connect" :/*System.out.println(e.getActionCommand());*/ connectToPartner(); /* System.out.println("done connecting to partner");*/ break;
        
        case "Accept Request" : /*System.out.println(e.getActionCommand());*/ acceptConnectionFromPartner(); break;
        
        case "My Address" :  ipInfoDialog.show(true); ipInfoText.setText(getIpOfDevice()); break;
        
        case "Light Refresh":  currentActivities.clear(); break;
        }
        
        if(e.getSource()==exit)
            System.exit(0);
       
  // Thread.currentThread().setName("Main Thread");
   // System.out.println(mode);System.out.println("Current Thread "+Thread.currentThread());
    
   
    }
  
  public void refresh()
  {
  currentActivities.clear(); requestThread=null; sendingThread=null; updationThread=null; receivingThread=null;
        
        socket=null; server=null; objectOutput=null; objectInput=null; isOnline=false; setTitle("Offline Mode");
        System.gc(); 
  }
   
  //Following method is not necessary by any means , it is just a user defined implementation
   String getIpOfDevice()
    {
        String ip="";
        
        try{
        Runtime r=Runtime.getRuntime();
        
        Process process=r.exec("cmd /c ipconfig");
        
        BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line="";
        
        while((line=reader.readLine())!=null)
        {
            
            if(line.contains("IPv4"))
                break;
        }
        
        if(line==null)
         return "Ip Address could not be identified";
        
        int colon=line.indexOf(':'); colon++;
        
        while(line.charAt(colon)==' ')colon++;
        
        ip="";
        
        while(line.charAt(colon)!=' ')
        { ip=ip+(char)line.charAt(colon++);
        
        if(colon==line.length())break;
        }
        
        }catch(Exception e){System.out.println(e);}
    return ip;}


}


class A 
{
    
    
    public static void main(String[] args)
    { 
        PaintingApplication p=new PaintingApplication();
        
     
        
        
    }
    
}
