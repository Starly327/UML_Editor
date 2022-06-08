package uml_editor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import uml_editor.Class;

public class Composite extends JPanel implements MouseMotionListener, MouseListener{

	 Canvas canvas;
	 ArrayList<Class> selectedClass;
	 ArrayList<Composite> selectedComposite;
	 Composite parent;
	 int min_x=Integer.MAX_VALUE,min_y=Integer.MAX_VALUE;
	 int max_x=Integer.MIN_VALUE,max_y=Integer.MIN_VALUE;
	 Point parentDistance = new Point();
	 int xSize,ySize;
	 Point movePoint;
	 
	public Composite(ArrayList<Class> selectedClass, ArrayList<Composite> selectedComposite) {
		this.selectedClass = new ArrayList<Class>(selectedClass);
		this.selectedComposite = new ArrayList<Composite>(selectedComposite);
		
		this.canvas = canvas.getInstance(null);
		setBound();
		
		this.setLocation(min_x - 10, min_y - 10);
		xSize = max_x - min_x ;
		ySize = max_y - min_y ;
		this.setSize(xSize, ySize);
		for(Class c : selectedClass) c.parentDistance(min_x - 10, min_y - 10);
		for(Composite com : selectedComposite) com.parentDistance(min_x - 10, min_y - 10);
		
		setOpaque(true);
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void setBound() {
		for(Class c : selectedClass) {
			min_x = Math.min(c.getX(), min_x);
			min_y = Math.min(c.getY(), min_y);
			if(c.mode=="Class") {
				max_x = Math.max(c.getX() + 120, max_x);
				max_y = Math.max(c.getY() + 130, max_y);
			}else {
				max_x = Math.max(c.getX() + 160, max_x);
				max_y = Math.max(c.getY() + 120, max_y);
			}
			c.setParent(this);
		}
		
		for(Composite com : selectedComposite) {
			min_x = Math.min(com.getX(), min_x);
			min_y = Math.min(com.getY(), min_y);
			max_x = Math.max(com.getX() + com.xSize + 20, max_x);
			max_y = Math.max(com.getY() + com.ySize + 20 , max_y);
			com.setParent(this);
		}
	}
	
	public void setParent(Composite parent) {
		this.parent = parent;
	}
	
	public Composite findGrandparent() {//��̤j��composite
		if(parent==null) return this;
		Composite temp = parent;
		while(temp.parent!=null) temp = temp.parent;
		return temp;
	}
	
	public void parentDistance(int x, int y) {//class�Pparent���Z��
		parentDistance.x = this.getX() - x;
		parentDistance.y = this.getY()- y;
	}
	
	public void parentMove(Point p) {
		this.setLocation(p.x + parentDistance.x, p.y + parentDistance.y);
		Point temp = new Point();
		temp.x = p.x + parentDistance.x;
		temp.y = p.y + parentDistance.y;
		canvas.moveToFront(this);
		for(Class c : selectedClass) c.parentMove(temp);
		for(Composite com : selectedComposite) com.parentMove(temp);
		
		canvas.repaint();
	}
	
	//---------------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e) {
		if(canvas.getmode()=="Class" || canvas.getmode()=="Use_case") {
			Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, canvas);
			Class c = new Class(p.x, p.y);
			canvas.class_list.add(c);
			canvas.add(c,0);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(canvas.getmode()=="Select") {
			if(parent!=null) {
				Composite temp = findGrandparent();
				temp.movePoint = MouseInfo.getPointerInfo().getLocation();
	            SwingUtilities.convertPointFromScreen(temp.movePoint, temp);
			}
			movePoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(movePoint, this);
            canvas.selectedComposite.clear();
            canvas.selectedComposite.add(this);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
//-----------------------------------------------------
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getmode()=="Select") {
			if(parent==null) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(p, canvas);
				//this.setLocation(p);
				Point temp = new Point();
				temp.x = (int)(p.getX() - movePoint.getX());
				temp.y = (int)(p.getY() - movePoint.getY());
				this.setLocation(temp.x, temp.y);
				canvas.moveToFront(this);
				for(Class c : selectedClass) c.parentMove(temp);
				for(Composite com : selectedComposite) com.parentMove(temp);
			}else {
				parent.mouseDragged(e);
			}
			canvas.repaint();
		}
			
	}
		

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
