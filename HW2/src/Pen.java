/*
   This file contains the drawing pens used by the Painter
   application.

   All pens are subclasses of the abstract class Pen.

   The code defines how each type of pen responds to mouse events.
*/
import com.jogamp.opengl.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

abstract class Pen {
    protected static final int FIRST=1, CONTINUE=2;
    protected int state;
    protected float r,g,b;
    protected GL2 gl;

    Pen(GL2 gl){
        this.gl = gl;
        state = FIRST;
    }

    public void mouseDown(MouseEvent e){}
    public void mouseUp(MouseEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void setColor(float r, float g, float b){
        this.r = r; this.g = g; this.b = b;
    }
}

// TODO
class ClipRectanglePen extends Pen {

    ClipRectanglePen(GL2 gl){
        super(gl);
    }

}

class RectanglePen extends Pen {
    Point p1,p2;
    Rectangle rct;

    RectanglePen(GL2 gl){
        super(gl);
    }

    public void mouseDown(MouseEvent e){
        int xnow = e.getX();
        int ynow = e.getY();
        p1 = p2 = new Point(xnow,ynow);
        rct = new Rectangle(p1,p2);
        gl.glColor3f(1-r,1-g,1-b);
        rct.draw(gl, GL2.GL_XOR);
    }

    public void mouseUp(MouseEvent e){
        // erase the last rectangle
        rct.draw(gl, GL2.GL_XOR);
        // get the new corner point
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        rct = new Rectangle(p1,p2);
        gl.glColor3f(r,g,b);
        // draw the new version permanently
        rct.draw(gl, GL2.GL_COPY);
    }

    public void mouseDragged(MouseEvent e){
        // erase the last rectangle
        rct.draw(gl, GL2.GL_XOR);
        // get the new corner point
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        rct = new Rectangle(p1,p2);
        // draw the new version
        rct.draw(gl, GL2.GL_XOR);
    }
}

class FilledRectanglePen extends Pen {
    Point p1,p2;
    FilledRectangle rct;

    FilledRectanglePen(GL2 gl){
        super(gl);
    }

    public void mouseDown(MouseEvent e){
        int xnow = e.getX();
        int ynow = e.getY();
        p1 = p2 = new Point(xnow,ynow);
        rct = new FilledRectangle(p1,p2);
        gl.glColor3f(1-r,1-g,1-b);
        rct.draw(gl, GL2.GL_XOR);
    }

    public void mouseUp(MouseEvent e){
        // erase the last rectangle
        rct.draw(gl, GL2.GL_XOR);
        // get the new corner point
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        rct = new FilledRectangle(p1,p2);
        gl.glColor3f(r,g,b);
        // draw the new version permanently
        rct.draw(gl, GL2.GL_COPY);
    }

    public void mouseDragged(MouseEvent e){
        // erase the last rectangle
        rct.draw(gl, GL2.GL_XOR);
        // get the new corner point
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        rct = new FilledRectangle(p1,p2);
        // draw the new version
        rct.draw(gl, GL2.GL_XOR);
    }
}

class LinePen extends Pen {
    Point p1,p2;
    Line line;

    LinePen(GL2 gl){
        super(gl);
    }
    public void mouseDown(MouseEvent e){
        int xnow = e.getX();
        int ynow = e.getY();
        p1 = p2 = new Point(xnow,ynow);
        line = new Line(p1,p2);
        gl.glColor3f(1-r,1-g,1-b);
        line.draw(gl, GL2.GL_XOR);
    }

    public void mouseUp(MouseEvent e){
        // erase the last line
        line.draw(gl, GL2.GL_XOR);
        // get the new endpoint
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        line = new Line(p1,p2);
        gl.glColor3f(r,g,b);
        // draw the new version permanently
        line.draw(gl, GL2.GL_COPY);
    }

    public void mouseDragged(MouseEvent e){
        // erase the last line
        line.draw(gl, GL2.GL_XOR);
        // get the new endpoint
        int xnow = e.getX();
        int ynow = e.getY();
        p2 = new Point(xnow,ynow);
        line = new Line(p1,p2);
        // draw the new version
        line.draw(gl, GL2.GL_XOR);
    }
}

class LineLoopPen extends Pen {
    Point origin, last, current;
    LineLoop loop;
    int cnt = 0;

    LineLoopPen(GL2 gl) {
        super(gl);
    }

    public void mouseDown(MouseEvent e) {
        int xnow = e.getX();
        int ynow = e.getY();
        if (cnt < 1)
            origin = last = current = new Point(xnow, ynow);
        last = current;
        current = new Point(xnow, ynow);
        loop = new LineLoop(origin, last, current);
        gl.glColor3f(1 - r, 1 - g, 1 - b);

        // if user clicked to end polygon
        if (last.x == current.x && last.y == current.y && cnt > 0) {
            current = origin;
            loop = new LineLoop(origin, last, current);
            loop.draw(gl, GL2.GL_XOR);
            cnt = 0;
        } else {
            // draw as normal and increment cnt
            loop.draw(gl, GL2.GL_XOR);
            cnt++;
        }

    }

    public void mouseUp(MouseEvent e) {
        // erase the last line
        loop.draw(gl, GL2.GL_XOR);

        gl.glColor3f(r, g, b);

        // draw the new version permanently
        loop.draw(gl, GL2.GL_COPY);

        // get the new current vertex
        int xnow = e.getX();
        int ynow = e.getY();
        current = new Point(xnow, ynow);

        loop = new LineLoop(origin, last, current);
    }

    public void mouseDragged(MouseEvent e) {
        // erase the last line
        loop.draw(gl, GL2.GL_XOR);
        // get the new current endpoint
        int xnow = e.getX();
        int ynow = e.getY();
        current = new Point(xnow, ynow);
        loop = new LineLoop(origin, last, current);
        // draw the new version
        loop.draw(gl, GL2.GL_XOR);
    }
}

class FilledPolygonPen extends Pen {
    Point origin, last, current;
    Polygon fp;
    int cnt = 0;
    List<Point> vList = new ArrayList<Point>();

    FilledPolygonPen(GL2 gl) {
        super(gl);
    }

    public void mouseDown(MouseEvent e) {
        int xnow = e.getX();
        int ynow = e.getY();

        if (cnt < 1)
            origin = current = last = new Point(xnow, ynow);
        last = current;
        current = new Point(xnow, ynow);

        // user clicked to end polygon
        if (cnt > 1 && current.x == vList.get(vList.size() - 1).x && current.y == vList.get(vList.size() - 1).y) {
            gl.glColor3f(1 - r, 1 - g, 1 - b);
            fp = new Polygon(vList);
            fp.draw(gl, GL2.GL_XOR);
            cnt = 0;
            vList = new ArrayList<Point>();
        } else {
            vList.add(current);
            System.out.println("Added vertex " + cnt);
            cnt++;
        }
    }
}