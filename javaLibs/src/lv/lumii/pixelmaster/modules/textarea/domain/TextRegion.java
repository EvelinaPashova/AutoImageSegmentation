
package lv.lumii.pixelmaster.modules.textarea.domain;


public class TextRegion {
    int xStart;
    int yStart;
    int xEnd;
    int yEnd;
    double mass;

    public TextRegion (int xstart, int ystart, int xend, int yend, int maxx, int maxy, double m) {
        if (xstart < 0) xStart = 0;
        else if (xstart > maxx) xStart = maxx;
        else xStart = xstart;

        if (xstart < 0) xEnd = 0;
        else if (xend > maxx) xEnd = maxx;
        else xEnd =xend;

        if (ystart < 0) yStart = 0;
        else if (ystart >maxy) yStart = maxy;
        else yStart = ystart;

        if (yend < 0) yEnd = 0;
        else if (yend > maxy) yEnd = maxy;
        else yEnd = yend;

        mass = m;
    }

    int area () {
        return width() * height();
    }

    int width() {
        return xEnd - xStart;
    }
    
    int height() {
        return yEnd - yStart;
    }

    double density () {
        return mass / area();
    }

    double aspect() {
        return (double)height() / (double)width();
    }

    public void printBoxVar() {
        System.out.printf( "xStart=%d xEnd=%d yStart=%d yEnd=%d mass=%e\n",
                            xStart, xEnd, yStart, yEnd, mass);
    }




}
