/*
  This file contains classes representing geometric objects
*/

import com.jogamp.opengl.*;
import java.util.*;

class Point {
    public int x,y;

    Point(int x,int y){
        this.x=x; this.y=y;
    }
}

class Polygon {
    // first and current point
    List<Point> vList;

    Polygon(List<Point> vList) {
        this.vList = vList;
    }

    // draw the polygon
    public void draw(GL2 gl, int how) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glLogicOp(how);
        gl.glBegin(GL2.GL_POLYGON);
        for (Point v : vList) {
            gl.glVertex2f(v.x, v.y);
        }
        gl.glEnd();
        gl.glFlush();
    }
}

class LineLoop {
    // first and current point
    Point origin;
    Point last;
    Point current;

    LineLoop(Point first, Point last, Point current) {
        this.origin = first;
        this.last = last;
        this.current = current;
    }

    // draw the polygon
    public void draw(GL2 gl, int how) {
        gl.glLogicOp(how);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(last.x, last.y);
        gl.glVertex2f(current.x, current.y);
        gl.glEnd();
        gl.glFlush();
    }
}

class Line {
    // two endpoints of a line
    Point p1;
    Point p2;

    Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // draw the line
    public void draw(GL2 gl, int how){
        gl.glLogicOp(how);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(p1.x, p1.y);
        gl.glVertex2f(p2.x, p2.y);
        gl.glEnd();
        gl.glFlush();
    }
}

class FilledRectangle {
    // two points at opposite corners of a rectangle
    Point p1;
    Point p2;

    FilledRectangle(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    // draw the rectangle as a polygon
    public void draw(GL2 gl, int how){
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glLogicOp(how);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(p1.x,p1.y);
        gl.glVertex2f(p1.x,p2.y);
        gl.glVertex2f(p2.x,p2.y);
        gl.glVertex2f(p2.x,p1.y);
        gl.glEnd();
        gl.glFlush();
    }
}

class Rectangle {
    // two points at opposite corners of a rectangle
    Point p1;
    Point p2;

    Rectangle(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    // draw the rectangle as a polygon
    public void draw(GL2 gl, int how){
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glLogicOp(how);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(p1.x,p1.y);
        gl.glVertex2f(p1.x,p2.y);
        gl.glVertex2f(p2.x,p2.y);
        gl.glVertex2f(p2.x,p1.y);
        gl.glEnd();
        gl.glFlush();
    }
}
// TODO
class ClipRectangle {

}

