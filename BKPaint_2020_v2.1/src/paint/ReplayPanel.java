
package paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import shape.Bucket;
import shape.Curve;
import shape.Line;
import shape.Oval;
import shape.Pencil;
import shape.Rectangle;
import shape.DrawType;
import shape.Eraser;
import shape.Polygon;
import shape.RightTriangle;
import shape.RoundRect;
import shape.SelectionShape;
import shape.Triangle;


public class ReplayPanel extends JPanel implements Runnable {

    private BufferedImage buff_img, org_img;
    private Image img, last_img;
    private PaintState paintState;
    private Line line;
    private Rectangle rect;
    private Oval oval;
    private RoundRect roundRect;
    private Triangle triangle;
    private RightTriangle rightTriangle;
    private Pencil pencil;
    private Bucket bucket;
    private Curve curve;
    private Eraser eraser;
    private int delay = 30;
    private boolean isPlaying;
    private boolean replay;
    private PaintTool paintTool;
    private JToggleButton bPlay;
    private Thread thread = null;
    private int currentState = 0;
    private int currentStep = 0;
    private int cStateElement = 0;
    private ArrayList<Point> listPoint;
    private ArrayList<DrawType> listState;
    private ArrayList<Integer> listDrawStep;
    private Graphics2D g2d, g2;
    private Point curvePoint1;
    private Point curvePoint2;
    private Point curvePoint3;
    private SelectionShape selrect;

    public void setDelay(int delay) {
        this.delay = (105 - delay) / 2;
    }

    public void setButton(JToggleButton bPlay) {
        this.bPlay = bPlay;
    }

    /**
     * Tao form ReplayPanel
     */
    public ReplayPanel() {
        listState = new ArrayList<>();
        listDrawStep = new ArrayList<>();
        paintTool = new PaintTool();
        line = new Line();
        rect = new Rectangle();
        oval = new Oval();
        pencil = new Pencil();
        roundRect = new RoundRect();
        rightTriangle = new RightTriangle();
        triangle = new Triangle();
        bucket = new Bucket();
        curve = new Curve();
        eraser = new Eraser();
        isPlaying = false;
        curvePoint1 = new Point();
        curvePoint2 = new Point();
        curvePoint3 = new Point();
        initComponents();
        paintState = new PaintState();
        this.setSize(909, 439);
        ///Khoi tao anh
        org_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) org_img.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        paintState.setData(org_img);
        buff_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) org_img.getGraphics();
        g2.drawImage(org_img, 0, 0, null);
        g2.dispose();
        readState();

    }

    public void startReplay() {
        if (thread == null) {
            refresh();
            thread = new Thread(this);
            isPlaying = true;
            thread.start();
        }
        isPlaying = true;

    }

    public void pauseReplay() {
        isPlaying = false;
    }

    public void stopReplay() {
        drawRemainderImage();
        currentStep = this.listDrawStep.size();
        currentState = listState.size();
        System.gc();
        //   currentState = listState.size();

    }

    public void flush() {
        if (isPlaying) {
            bPlay.setIcon(new ImageIcon(getClass().getResource("/icon/pause.png")));
            isPlaying = false;
        }
        
        paintState = new PaintState();
        org_img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) org_img.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.dispose();
        paintState.setData(org_img);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setPaintState(PaintState paintState) {
        this.paintState = paintState;
        readState();
    }

    public void readState() {
        listPoint = new ArrayList<>();
        listState = paintState.getListState();
        listDrawStep = paintState.getDrawStepList();
        org_img.flush();
        buff_img.flush();
        System.gc();
        org_img = null;
        buff_img = null;
        int w = paintState.getWidth();
        int h = paintState.getHeight();
        int[] data = paintState.getData();
        org_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        org_img.getRaster().setPixels(0, 0, w, h, data);
        buff_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) buff_img.getGraphics();
        g2d.drawImage(org_img, 0, 0, null);
        this.setSize(new Dimension(w, h));
        this.setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(new Dimension(w, h));
        this.revalidate();

    }

    public void refresh() {
        g2 = (Graphics2D) buff_img.getGraphics();
        g2.drawImage(org_img, 0, 0, null);
        g2.dispose();
        currentState = 0;
        currentStep = 0;
        cStateElement = 0;
        repaint();
    }

    public void dispose() {
        org_img = null;
        buff_img = null;
        line = null;
        rect = null;
        oval = null;
        eraser = null;
        pencil = null;
        bucket = null;
        curve = null;
        selrect = null;
        triangle = null;
        rightTriangle = null;
        roundRect = null;
        g2.dispose();
        g2d.dispose();
        System.gc();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g.drawImage(buff_img, 0, 0, null);

        if (isPlaying) {
            Graphics2D g2dBuffer = (Graphics2D) buff_img.getGraphics();
            if (currentState < listState.size() && listPoint != null && cStateElement < listPoint.size()) {

                DrawType drawType = listState.get(currentState);

                if (drawType instanceof Line) {
                    line.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    line.draw(g2);
                    //Neu da dat trang thai cuoi thi ve len buffer
                    if (cStateElement == listPoint.size() - 1) {
                        line.draw(g2dBuffer);
                    }
                } else if (drawType instanceof Rectangle) {
                    rect.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    rect.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        rect.draw(g2dBuffer);
                    }

                } else if (drawType instanceof RoundRect) {
                    roundRect.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    roundRect.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        roundRect.draw(g2dBuffer);
                    }

                } else if (drawType instanceof Triangle) {
                    triangle.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    triangle.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        triangle.draw(g2dBuffer);
                    }

                } else if (drawType instanceof RightTriangle) {
                    rightTriangle.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    rightTriangle.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        rightTriangle.draw(g2dBuffer);
                    }

                } else if (drawType instanceof Oval) {
                    oval.setPoint(listPoint.get(0), listPoint.get(cStateElement));
                    oval.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        oval.draw(g2dBuffer);
                    }
                } else if (drawType instanceof Curve) {
                    int state = curve.getState();
                    ArrayList<Point> list = new ArrayList<>();
                    Point start = curve.getStart();
                    if (state == 1) {
                        curvePoint3 = listPoint.get(cStateElement);
                        curvePoint1.setLocation(curvePoint3);
                        curvePoint2.setLocation(curvePoint3);
                        //kiem tra lai state khi no bang mot
                        if (cStateElement == curve.getSizeOfStateFirst() - 1) {
                            //Co dinh diem thu ba
                            curvePoint3 = listPoint.get(cStateElement);
                            curve.setState(2);
                        }
                    } else if (state == 2) {
                        curvePoint2 = listPoint.get(cStateElement);
                        curvePoint1.setLocation(curvePoint2);
                        //co dinh diem thu hai
                        if (cStateElement == curve.getSizeOfStateSecond() - 1) {
                            curve.setState(3);
                            curvePoint2 = listPoint.get(cStateElement);
                        }
                    } else if (state == 3) {
                        curvePoint1 = listPoint.get(cStateElement);
                    }
                    list.add(start);
                    list.add(curvePoint2);
                    list.add(curvePoint1);
                    list.add(curvePoint3);
                    curve.setList(list);
                    curve.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        curve.draw(g2dBuffer);
                    }
                } else if (drawType instanceof SelectionShape) {
                    if (cStateElement == 0) {
                        return;
                    }
                    selrect.setStart(listPoint.get(cStateElement));
                    selrect.draw(g2);
                    if (cStateElement == listPoint.size() - 1) {
                        selrect.draw(g2dBuffer);
                    }
                } else if (drawType instanceof Pencil) {
                    if (cStateElement < listPoint.size() - 1) {
                        System.out.println("currentState: " + currentState);
                        System.out.println("currentStep: " + currentStep);
                        System.out.println("currentStateElement: " + cStateElement);
                        System.out.println("\n\n\n");
                        pencil.setPoint(listPoint.get(cStateElement), listPoint.get(cStateElement + 1));
                        pencil.draw(g2dBuffer);
                    }
                } else if (drawType instanceof Eraser) {
                    if (cStateElement < listPoint.size() - 1) {
                        eraser.setPoint(listPoint.get(cStateElement), listPoint.get(cStateElement + 1));
                        eraser.draw(g2dBuffer);
                    }
                }
                
            }
        }
    }

    public void drawRemainderImage() {
        Graphics2D g2dBuffer = (Graphics2D) buff_img.getGraphics();
        int shapeIndex = 0;
        for (int i = this.currentStep - 1; i < paintState.getDrawStepList().size(); i++) {
            int inStepState = paintState.getDrawStepList().get(i);
            //Lay tung trang thia cua buoc ve
            switch (inStepState) {
                case PaintState.ROTATE_RIGHT:
                    rotate(90);
                    break;
                case PaintState.ROTATE_LEFT:
                    rotate(-90);
                    break;
                case PaintState.ROTATE_REVERSE:
                    rotate(180);
                    break;
                case PaintState.V_FLIP:
                    flipping(1);
                    break;
                case PaintState.H_FLIP:
                    flipping(2);
                    break;
                case PaintState.PAINTTING:
                    //Neu la painting thi se ve lai toan bo anh tu dau
                    DrawType inDrawType = paintState.getListState().get(shapeIndex);
                    if (inDrawType instanceof Line) {
                        Line inLine = (Line) inDrawType;
                        inLine.draw(g2d);
                        // System.out.println("line");
                    } else if (inDrawType instanceof Triangle) {
                        Triangle inTriangle = (Triangle) inDrawType;
                        inTriangle.draw(g2d);
                    } else if (inDrawType instanceof RightTriangle) {
                        RightTriangle inRightTriangle = (RightTriangle) inDrawType;
                        inRightTriangle.draw(g2d);
                    } else if (inDrawType instanceof Rectangle) {
                        Rectangle inRect = (Rectangle) inDrawType;
                        inRect.draw(g2d);
                    } else if (inDrawType instanceof RoundRect) {
                        RoundRect inRoundRect = (RoundRect) inDrawType;
                        inRoundRect.draw(g2d);
                    } else if (inDrawType instanceof Oval) {
                        Oval inOval = (Oval) inDrawType;
                        inOval.draw(g2d);
                    } else if (inDrawType instanceof Polygon) {
                        Polygon inPolygon = (Polygon) inDrawType;
                        inPolygon.draw(g2d);
                    } else if (inDrawType instanceof Curve) {
                        Curve inCurve = (Curve) inDrawType;
                        inCurve.draw(g2d);

                    } else if (inDrawType instanceof SelectionShape) {
                        SelectionShape inselrect = (SelectionShape) inDrawType;
                        inselrect.draw(g2d);
                    } else if (inDrawType instanceof Pencil) {
                        Pencil inPencil = (Pencil) inDrawType;
                        for (int j = 1; j < inPencil.getDraggedPoint().size(); j++) {
                            inPencil.setPoint(inPencil.getDraggedPoint().get(j - 1), inPencil.getDraggedPoint().get(j));
                            inPencil.draw(g2d);
                        }

                    } else if (inDrawType instanceof Eraser) {
                        Eraser inEraser = (Eraser) inDrawType;
                        for (int j = 1; j < inEraser.getDraggedPoint().size(); j++) {
                            inEraser.setPoint(inEraser.getDraggedPoint().get(j - 1), inEraser.getDraggedPoint().get(j));
                            inEraser.draw(g2d);
                        }

                    } else if (inDrawType instanceof Eraser) {
                        eraser = (Eraser) inDrawType;
                        listPoint = eraser.getDraggedPoint();
                    } else if (inDrawType instanceof Bucket) {
                        Bucket inBucket = (Bucket) inDrawType;
                        inBucket.draw(buff_img);
                    }
                    shapeIndex++;
                    break;
            }
        }
        repaint();

    }

    //replay qua tung diem
    @Override
    public void run() {
        int increDelay = 60;
        while (currentStep < paintState.getDrawStepList().size()) {
            if (isPlaying == false) {
                continue;
            }

            int inStepState = listDrawStep.get(currentStep);
            //Lay tung trang thia cua buoc ve
            switch (inStepState) {
                case PaintState.ROTATE_RIGHT:
                    rotate(90);
                     {
                        try {
                            Thread.sleep(delay * increDelay);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ReplayPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case PaintState.ROTATE_LEFT:
                    rotate(-90);
                    try {
                        Thread.sleep(delay * increDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReplayPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case PaintState.ROTATE_REVERSE:
                    rotate(180);
                    try {
                        Thread.sleep(delay * increDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReplayPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case PaintState.V_FLIP:
                    flipping(1);
                    try {
                        Thread.sleep(delay * increDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReplayPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case PaintState.H_FLIP:
                    flipping(2);
                    try {
                        Thread.sleep(delay * increDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReplayPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case PaintState.PAINTTING:
                    if (listPoint == null) {
                        DrawType inDrawType = listState.get(currentState);
                        if (inDrawType instanceof Line) {
                            line = (Line) inDrawType;
                            listPoint = line.getDraggedPoint();
                        } else if (inDrawType instanceof Triangle) {
                            triangle = (Triangle) inDrawType;
                            listPoint = triangle.getDraggedPoint();
                        } else if (inDrawType instanceof RightTriangle) {
                            rightTriangle = (RightTriangle) inDrawType;
                            listPoint = rightTriangle.getDraggedPoint();
                        } else if (inDrawType instanceof Rectangle) {
                            rect = (Rectangle) inDrawType;
                            listPoint = rect.getDraggedPoint();
                        } else if (inDrawType instanceof RoundRect) {
                            roundRect = (RoundRect) inDrawType;
                            listPoint = roundRect.getDraggedPoint();
                        } else if (inDrawType instanceof Oval) {
                            oval = (Oval) inDrawType;
                            listPoint = oval.getDraggedPoint();
                        } else if (inDrawType instanceof Curve) {
                            curve = (Curve) inDrawType; //tu tu da
                            curve.resetState();
                            listPoint = curve.getDraggedPoint();
                        } else if (inDrawType instanceof Pencil) {
                            pencil = (Pencil) inDrawType;
                            listPoint = pencil.getDraggedPoint();

                        } else if (inDrawType instanceof SelectionShape) {
                            selrect = (SelectionShape) inDrawType;
                            listPoint = selrect.getDraggedPoint();
                        } else if (inDrawType instanceof Bucket) {
                            Bucket inBucket = (Bucket) inDrawType;
                            inBucket.draw(buff_img);
                        }
                    } else {   //Neu diem da duoc khoi tao
                        //Kiem tra xem hinh hien tai da dat den trang thai cuoi cung cua hinh chua

                        if (cStateElement == listPoint.size()) {
                            listPoint = null;
                            cStateElement = 0;
                            currentState++;
                            currentStep++;
                        } else {   //Neu van con trang thai cho viec ve hinh va trong danh sach cac diem cua hinh hien tai
                            //Diem con chua phai la diem cuoi cung thi se tang trang thai cua diem hien tai len mot
                            ++cStateElement;

                        }
                    }
                    break;
            }

            if (inStepState != PaintState.PAINTTING) {
                currentStep++;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.out.println("error here: replayPanel in run() method");
            }
            repaint();
        }
        System.gc();
        isPlaying = false;
        thread = null;
        replay = false;
        bPlay.setIcon(new ImageIcon(getClass().getResource("/icon/pause.png")));

    }

    public BufferedImage getBuffer() {
        return buff_img;
    }

    public void rotate(int alpha) {
        AffineTransform tx = new AffineTransform();
        if (alpha == 90) {
            tx.translate(buff_img.getHeight(), 0);
            tx.rotate(alpha * Math.PI / 180.0, 0, 0);
        } else if (alpha == -90) {
            tx.translate(0, buff_img.getWidth());
            tx.rotate(alpha * Math.PI / 180, 0, 0);
        } else if (alpha == 180) {
            tx.translate(buff_img.getWidth(), buff_img.getHeight());
            tx.rotate(alpha * Math.PI / 180, 0, 0);
        }
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        buff_img = op.filter(buff_img, null);
        this.setSize(new Dimension((int) (buff_img.getWidth()), (int) (buff_img.getHeight())));
        g2d = (Graphics2D) buff_img.getGraphics();
        repaint();
    }

    public void flipping(int typeFlip) {
        AffineTransform tx = null;
        if (typeFlip == 1) {   //lat nguoc anh
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -buff_img.getHeight());
        } else if (typeFlip == 2) { //Lat ngang anh
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-buff_img.getWidth(), 0);
        }
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        buff_img = op.filter(buff_img, null);
        g2d = (Graphics2D) buff_img.getGraphics();
        repaint();
    }
}
