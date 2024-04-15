import java.awt.image.BufferedImage
import java.awt.{BasicStroke, BorderLayout, Color, Dimension, Font, Graphics, Graphics2D}
import java.awt.geom.*
import javax.swing.event.{ChangeEvent, ChangeListener}
import javax.swing.{JComponent, JFrame, JScrollPane, JSlider, WindowConstants}

class ImageComponent(val img: BufferedImage) extends JComponent {

  setZoom_mut(1.0)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val dim = getPreferredSize
    val _ = g.drawImage(img, 0, 0, dim.width, dim.height, this)
  }

  def setZoom_mut(zoom: Double): Unit = {
    val w = (zoom * img.getWidth()).toInt
    val h = (zoom * img.getHeight()).toInt
    setPreferredSize(new Dimension(w, h))
    revalidate()
    repaint()
  }
}

def draw(): Unit = {

  // Size of image
  val size = (500, 500)

  // create an image
  val canvas = new BufferedImage(size._1, size._2, BufferedImage.TYPE_INT_RGB)

  // get Graphics2D for the image
  val g = canvas.createGraphics()

  // clear background
  g.setColor(Color.WHITE)
  g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)

  // enable anti-aliased rendering (prettier lines and circles)
  // Comment it out to see what this does!
  g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
    java.awt.RenderingHints.VALUE_ANTIALIAS_ON)

  // draw two filled circles
  g.setColor(Color.RED)
  g.fill(new Ellipse2D.Double(30.0, 30.0, 40.0, 40.0))
  g.fill(new Ellipse2D.Double(230.0, 380.0, 40.0, 40.0))

  // draw an unfilled circle with a pen of width 3
  g.setColor(Color.MAGENTA)
  g.setStroke(new BasicStroke(3f))
  g.draw(new Ellipse2D.Double(400.0, 35.0, 30.0, 30.0))

  // draw a filled and an unfilled Rectangle
  g.setColor(Color.CYAN)
  g.fill(new Rectangle2D.Double(20.0, 400.0, 50.0, 20.0))
  g.draw(new Rectangle2D.Double(400.0, 400.0, 50.0, 20.0))

  // draw a line
  g.setStroke(new BasicStroke()) // reset to default
  g.setColor(new Color(0, 0, 255)) // same as Color.BLUE
  g.draw(new Line2D.Double(50.0, 50.0, 250.0, 400.0))

  // draw some text
  g.setColor(new Color(0, 128, 0)) // a darker green
  g.setFont(new Font("Batang", Font.PLAIN, 20))
  g.drawString("Hello World!", 155, 225)
  g.drawString("안녕 하세요", 175, 245)

  // done with drawing
  g.dispose()

  // write image to a file
//  javax.imageio.ImageIO.write(canvas, "png", new java.io.File("drawing.png"))

  val image = new ImageComponent(canvas)
  val frame = new JFrame("Drawing")

  frame.add(new JScrollPane(image))

  // size it, make the close button hide the window, and make it visible
  frame.setSize(800, 600)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.setVisible(true)


}